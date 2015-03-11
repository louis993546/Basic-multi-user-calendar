package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.apptstorage.ApptStorageMemImpl;
import hkust.cse.calendar.unit.Save;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.unit.Userlist;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public class LoginDialog extends JFrame implements ActionListener
{
	private JTextField userName;
	private JPasswordField password;
	private JButton button;
	private JButton closeButton;
	private JButton signupButton;
	User user = new User( "noname", "nopass");
	ApptStorageControllerImpl controller = new ApptStorageControllerImpl(new ApptStorageMemImpl(user));
	public LoginDialog()		// Create a dialog to log in
	{
		
		setTitle("Log in");
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		
		Container contentPane;
		contentPane = getContentPane();
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		
		JPanel messPanel = new JPanel();
		messPanel.add(new JLabel("Please input your user name and password to log in."));
		top.add(messPanel);
		
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
		
		JPanel signupPanel = new JPanel();
		signupPanel.add(new JLabel("If you don't have an account, please sign up:"));
		signupButton = new JButton("Sign up now");
		signupButton.addActionListener(this);
		signupPanel.add(signupButton);
		top.add(signupPanel);
		
		contentPane.add("North", top);
		
		JPanel butPanel = new JPanel();
		butPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		button = new JButton("Log in");
		button.addActionListener(this);
		butPanel.add(button);
		
		closeButton = new JButton("Close program");
		closeButton.addActionListener(this);
		butPanel.add(closeButton);
		
		contentPane.add("South", butPanel);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);	
		
	}
	

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == button)
		{
			// When the button is clicked, check the user name and password, and try to log the user in
			
			//login();
			User user = new User( userName.getText(), password.getText());
			User u=checkuser(user);
			if(u!=null){
				CalGrid grid = new CalGrid(new ApptStorageControllerImpl(new ApptStorageMemImpl(u)));
				setVisible( false );
			}
			
		}
		else if(e.getSource() == signupButton)
		{
			// Create a new account
			UserDialog signup=new UserDialog("signup",null);
			signup.setParent(this);
			setVisible( false );
		}
		else if(e.getSource() == closeButton)
		{
			int n = JOptionPane.showConfirmDialog(null, "Exit Program ?",
					"Confirm", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION)
				System.exit(0);			
		}
	}
	
	
	public static User checkuser(User u){
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Save.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		    Save map =(Save)jaxbUnmarshaller.unmarshal( new File("appointment.xml") );
		    
			List<Userlist> list=new ArrayList<Userlist>();
			if(map!=null){
				list=map.getApptlist();
				for (Userlist a:list){
					if(a.getuser().getID().equals(u.getID())&&a.getuser().getPassword().equals(u.getPassword()))
						return a.getuser();
						
					}
			}
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "User not exist \n go register or contact admin for more information",
                "Input Error", JOptionPane.ERROR_MESSAGE);
		return null;
	}
}
