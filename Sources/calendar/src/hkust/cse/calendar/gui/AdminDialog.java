package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.Save;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.unit.Userlist;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class AdminDialog extends JFrame implements ActionListener{
	private ApptStorageControllerImpl _controller;
	private DefaultListModel<Object> listModel;
	private JList<Object> list;
	private JTextField Users;
	private JButton edit;
	private JButton _delete;
	private JTextField userName,fullName,password,email;
	private JCheckBox admin,delete;
	
	public AdminDialog(ApptStorageControllerImpl controller){
		super("edit user");
		_controller=controller;
		setLayout(new FlowLayout());
		
		listModel = new DefaultListModel<Object>();
		initialize();
		list = new JList<Object>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(new JScrollPane(list),BorderLayout.WEST);
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		
		JPanel butPanel = new JPanel();
		butPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JPanel namePanel = new JPanel();
		namePanel.add(new JLabel("User Name:"));
		userName = new JTextField(15);
		namePanel.add(userName);
		userName.setEditable(false);
		
		JPanel pwPanel = new JPanel();
		pwPanel.add(new JLabel("Password:  "));
		password = new JTextField(15);
		pwPanel.add(password);
		
		JPanel fnamePanel = new JPanel();
		fnamePanel.add(new JLabel("Full Name:"));
		fullName = new JTextField(15);
		fnamePanel.add(fullName);
		
		JPanel mailPanel = new JPanel();
		mailPanel.add(new JLabel("Email:  "));
		email = new JTextField(15);
		mailPanel.add(email);
		
		JPanel adminPanel = new JPanel();
		adminPanel.add(new JLabel("Admin  "));
		admin = new JCheckBox();
		adminPanel.add(admin);
		
		JPanel dPanel = new JPanel();
		dPanel.add(new JLabel("delete  "));
		delete = new JCheckBox();
		dPanel.add(delete);
		
		top.add(namePanel);
		top.add(pwPanel);
		top.add(fnamePanel);
		top.add(mailPanel);
		top.add(adminPanel);
		top.add(dPanel);
		
		_delete = new JButton("Delete");
		_delete.addActionListener(this);
		edit = new JButton("Edit");
		edit.addActionListener(this);
		butPanel.add( _delete);
		butPanel.add(edit);
		add(top);
		add(butPanel);
		list.setSelectedIndex(0);
		list.addListSelectionListener(new ListSelectionListener(){
			
			public void valueChanged(ListSelectionEvent e) {
					User s = (User) list.getSelectedValue();
					userName.setText(s.getID());
					password.setText(s.getPassword());
					email.setText(s.getAddress());
					fullName.setText(s.getFullname());
					admin.setSelected(s.getAdmin()==1);
					delete.setSelected(s.getDeleted()==1);
				}
			}
		);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		User _new = (User) list.getSelectedValue();
		if(e.getSource()==_delete){
			_new.setDeleted(1);
			_controller.updateUser(_new);
			
		}else if(e.getSource()==edit){
			if(fullName.getText().equalsIgnoreCase("")||
					email.getText().equalsIgnoreCase("")||password.getText().equalsIgnoreCase("")){
				JOptionPane.showMessageDialog(this,"Please make sure you enter something.".intern());
			}else if(!ValidString(password.getText())){
				JOptionPane.showMessageDialog(this,"pw can contains only letters and numbers".intern());
			}else {
				_new.setAddress(email.getText());
				_new.setFullname(fullName.getText());
				_new.setPassword(password.getText());
				if(admin.isSelected()){
					_new.setAdmin(1);
				}else _new.setAdmin(0);
				
				if(delete.isSelected()){
					_new.setDeleted(1);
				}else _new.setDeleted(0);
			}
			_controller.updateUser(_new);
		}
		
	}
	
	private void initialize(){
		Save s=_controller.getSave();
		List<User> u= new ArrayList<User>();
		List<Userlist> list=s.getApptlist();
		for(Userlist a:list){
			u.add(a.getuser());
		}
		for(User b:u)listModel.addElement(b);
	}
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
