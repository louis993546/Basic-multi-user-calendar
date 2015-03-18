package hkust.cse.calendar.gui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.apptstorage.ApptStorageMemImpl;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Save;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.unit.Userlist;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


public class UserDialog extends JFrame implements ActionListener{
	
	private ApptStorageControllerImpl _controller;
	private LoginDialog parent;
	private JTextField userName;
	private JTextField fullName;
	private JPasswordField password;
	private JPasswordField newpw;
	private JTextField email;
	private JButton button;
	private JCheckBox admin;
	
	public UserDialog(String title,ApptStorageControllerImpl controller)
	{
		setTitle(title);
		
		if(this.getTitle().equals("signup")){
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					parent.setVisible(true);
					
				}
			});
		}
		_controller=controller;
		Container contentPane;
		contentPane = getContentPane();
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		
		
		JPanel namePanel = new JPanel();
		namePanel.add(new JLabel("User Name:"));
		userName = new JTextField(15);
		namePanel.add(userName);
		top.add(namePanel);	
		
		
		JPanel pwPanel = new JPanel();
		pwPanel.add(new JLabel("Password:  "));
		password = new JPasswordField(15);
		pwPanel.add(password);
		top.add(pwPanel);
		
		if((this.getTitle()).equals("userinfo")){
			JPanel newpwPanel = new JPanel();
			newpwPanel.add(new JLabel("New Password:  "));
			newpw = new JPasswordField(15);
			newpwPanel.add(newpw);
			top.add(newpwPanel);
		}
		
		JPanel fnamePanel = new JPanel();
		fnamePanel.add(new JLabel("Full Name:"));
		fullName = new JTextField(15);
		fnamePanel.add(fullName);
		top.add(fnamePanel);
		
		JPanel mailPanel = new JPanel();
		mailPanel.add(new JLabel("Email:  "));
		email = new JTextField(15);
		mailPanel.add(email);
		top.add(mailPanel);
		
		if((this.getTitle()).equals("signup")){
			JPanel adminPanel = new JPanel();
			adminPanel.add(new JLabel("Admin  "));
			admin = new JCheckBox();
			adminPanel.add(admin);
			top.add(adminPanel);
		}
		if(this.getTitle().equals("userinfo")){
			userName.setText(_controller.getDefaultUser().getID());
			email.setText(_controller.getDefaultUser().getAddress());
			fullName.setText(_controller.getDefaultUser().getFullname());
			userName.setEditable(false);
		}
		
		contentPane.add("North", top);
		
		JPanel butPanel = new JPanel();
		butPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		button = new JButton("Done");
		button.addActionListener(this);
		butPanel.add(button);
		contentPane.add("South", butPanel);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==button){
			if((this.getTitle()).equals("signup"))
			{

				User newuser=new User(userName.getText(), password.getText());
				newuser.setAddress(email.getText());
				newuser.setFullname(fullName.getText());
				if(admin.isSelected())newuser.setAdmin(1);
				else newuser.setAdmin(0);
				if(userName.getText().equalsIgnoreCase("") || password.getText().equalsIgnoreCase("")){
					JOptionPane.showMessageDialog(this,"Please make sure you have enter something.".intern());
				}else if(!(ValidString(userName.getText())&&ValidString(password.getText()))){
					JOptionPane.showMessageDialog(this,"id and pw can contains only letters and numbers".intern());
				}else if(check_adduser(newuser)){
					CalGrid grid = new CalGrid(new ApptStorageControllerImpl(new ApptStorageMemImpl(newuser)));
					setVisible( false );
				}
			}
			if((this.getTitle()).equals("userinfo"))
			{
				if(newpw.getText().equalsIgnoreCase("")||fullName.getText().equalsIgnoreCase("")||
						email.getText().equalsIgnoreCase("")||password.getText().equalsIgnoreCase("")){
					JOptionPane.showMessageDialog(this,"Please make sure you enter something.".intern());
				}else if(!ValidString(newpw.getText())){
					JOptionPane.showMessageDialog(this,"pw can contains only letters and numbers".intern());
				}else if(!password.getText().equals(_controller.getDefaultUser().getPassword())){
					JOptionPane.showMessageDialog(this,"wrong password".intern());
				}else {
					User u=new User(_controller.getDefaultUser().getID(),newpw.getText());
					u.setAdmin(_controller.getDefaultUser().getAdmin());
					u.setAddress(email.getText());
					u.setFullname(fullName.getText());
					_controller.updateUser(u);
					setVisible( false );
				}
			}
		}
		
	}

	public boolean check_adduser(User u){
		List<Userlist> list=new ArrayList<Userlist>();
		Userlist b=new Userlist();
		HashMap<Integer,Appt> m=new HashMap<Integer,Appt>();
		b.setuser(u);
		b.setMap(m);
		Save s=new Save();
		File file = new File("appointment.xml");
		if(file.exists()){
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(Save.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			    Save map =(Save)jaxbUnmarshaller.unmarshal( new File("appointment.xml") );
				if(map!=null){
					list=map.getApptlist();
					for (Userlist a:list){
						if(a.getuser().getID().equals(u.getID())){
							JOptionPane.showMessageDialog(null, "User exist",
			                        "Input Error", JOptionPane.ERROR_MESSAGE);
							return false;
						}
					}
					list.add(b);
					s.setApptlist(list);
				}else{
					List<Userlist> list1=new ArrayList<Userlist>();
					list1.add(b);
					s.setApptlist(list1);
				}
				
				
				
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			List<Userlist> list1=new ArrayList<Userlist>();
			list1.add(b);
			s.setApptlist(list1);
		}
		JAXBContext save;
		try {
			save = JAXBContext.newInstance(Save.class);
			Marshaller jaxbMarshaller = save.createMarshaller();
			jaxbMarshaller.marshal(s, file);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
		
		
	}
	public void setParent(LoginDialog parent) {
		this.parent = parent;
	}
	// This method checks whether a string is a valid user name or password, as they can contains only letters and numbers
		public static boolean ValidString(String s)
		{
			char[] sChar = s.toCharArray();
			for(int i = 0; i < sChar.length; i++)
			{
				int sInt = (int)sChar[i];
				if(sInt < 48 || sInt > 122)
					return false;
				if(sInt > 57 && sInt < 65)
					return false;
				if(sInt > 90 && sInt < 97)
					return false;
			}
			return true;
		}	
	
	
		
}
