package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.apptstorage.ApptStorageNullImpl;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.gui.NewUser;
import hkust.cse.calendar.unit.SingletonMediator;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.thoughtworks.xstream.*;


public class LoginDialog extends JFrame implements ActionListener {	

	/*added by me*/
	private BufferedWriter fileWriter;
	//public static HashMap<String, User> userList;
	/*up to here*/
	private JTextField userName;
	private JPasswordField password;
	private JButton button;
	private JButton closeButton;
	private JButton signupButton;
	public static CalGrid grid;
	
	public LoginDialog() {
		
		setTitle("Log in");
		
		/*initialize the fileWriter*/
		/*try{
			fileWriter = new BufferedWriter(new FileWriter("savedUsers.xml"));
			
		}
		catch(IOException e){
			throw new RuntimeException(e);
		}*/
		
		/*initialize the hashmap with a copy from the main function*/
		//userList = loadList;
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				
				
				/*=================================================added by me=========================================================*/
				try{
					//hkust.cse.calendar.Main.CalendarMain.fileWriter.write("=====================Something is bad\n=====================");
					fileWriter = new BufferedWriter(new FileWriter("savedUsers.xml"));
					fileWriter.write(hkust.cse.calendar.Main.CalendarMain.xstreamRetrieve.toXML(SingletonMediator.getBrainInstance().getAllUsers()));
					
					//hkust.cse.calendar.Main.CalendarMain.fileWriter.write(hkust.cse.calendar.Main.CalendarMain.xstreamRetrieve.toXML(hkust.cse.calendar.gui.LoginDialog.userList));
				
				}catch(IOException exc){
					throw new RuntimeException(exc);
				}finally{
					if(fileWriter.equals(null) == false){
						try{fileWriter.close();} catch(IOException etr){}
					}
				}
				/*=================================================up to here==========================================================*/
				
				
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

		//button = new JButton("Log in (No user name and password required)");
		button = new JButton("                           Log in                               ");
		button.addActionListener(this);
		butPanel.add(button);
		
		closeButton = new JButton("                  Close program                      ");
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
			//User user = new User( "noname", "nopass");
			
			/*==============================added by me======================================================*/	
			
			
			/*checks if the text fields are not empty*/
			boolean emptyField = true;
			String tempTest = userName.getText();
			int sizeOfString = userName.getText().length();
			
			for(int i = 0; i < sizeOfString; i++){
				if(tempTest.charAt(i) != ' '){
					emptyField = false;
					break;
				}
				
			}
			
			if(emptyField == true){
				JOptionPane.showMessageDialog(this, "Please fill in all the text fields");
				return;
			}
			
			
			tempTest = password.getText();
			sizeOfString = password.getText().length();
			
			for(int i = 0; i < sizeOfString; i++){
				if(tempTest.charAt(i) != ' '){
					emptyField = true;
					break;
				}
				
			}
			
			/*if at least one of the fields is empty, return*/
			if(emptyField == false){
				JOptionPane.showMessageDialog(this, "Please fill in all the text fields");
				return;
			}
			
			if(SingletonMediator.getBrainInstance().getAllUsers().isEmpty() == true || SingletonMediator.getBrainInstance().getAllUsers().get(userName.getText()) == null){
				JOptionPane.showMessageDialog(this, "Such a user doesn't exist");
				return;
			}
			else{ 
				if(SingletonMediator.getBrainInstance().getAllUsers().get(userName.getText()).Password().equals(password.getText()) == false){
					JOptionPane.showMessageDialog(this, "The password is incorrect");
					return;
				}
				else{
					grid = new CalGrid(new ApptStorageControllerImpl(new ApptStorageNullImpl(SingletonMediator.getBrainInstance().getAllUsers().get(userName.getText()))));
				}
			}
			
			/*============================================up to here================================================*/
			
			//grid = new CalGrid(new ApptStorageControllerImpl(new ApptStorageNullImpl(user)));
			setVisible( false );
		}
		else if(e.getSource() == signupButton) {
			// Create a new account
			/*a HashMap is used to store all the users*/
			new NewUser(SingletonMediator.getBrainInstance().getAllUsers());
			
		}
		else if(e.getSource() == closeButton) {
			int n = JOptionPane.showConfirmDialog(null, "Exit Program ?",
					"Confirm", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION){
				
				/*=================================================added by me=========================================================*/
				try{
					//hkust.cse.calendar.Main.CalendarMain.fileWriter.write("=====================Something is bad\n=====================");
					fileWriter = new BufferedWriter(new FileWriter("savedUsers.xml"));
					fileWriter.write(hkust.cse.calendar.Main.CalendarMain.xstreamRetrieve.toXML(SingletonMediator.getBrainInstance().getAllUsers()));
					
				
				}catch(IOException exc){
					throw new RuntimeException(exc);
				}finally{
					if(fileWriter.equals(null) == false){
						try{fileWriter.close();} catch(IOException etr){}
					}
				}
				/*=================================================up to here==========================================================*/
				System.exit(0);			
			}
		}
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
