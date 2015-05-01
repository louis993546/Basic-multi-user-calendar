package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.apptstorage.ApptStorageNullImpl;
import hkust.cse.calendar.apptstorage.UserDB;
import hkust.cse.calendar.unit.User;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class LoginDialog extends JFrame implements ActionListener
{
	private JTextField userName;
	private JPasswordField password;
	private JButton button;
	private JButton noLoginButton;
	private JButton closeButton;
	private JButton signupButton;
	private UserDB udb;

	//constructor
	public LoginDialog()		// Create a dialog to log in
	{
		setTitle("Log in");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		udb = new UserDB();

		Container contentPane;
		contentPane = getContentPane();

		//create a new JPanel to hold everything
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

		//messPanel contains message to be displayed
		JPanel messPanel = new JPanel();
		messPanel.add(new JLabel("Please input your user name and password to log in."));
		top.add(messPanel);

		//namePanel contains a JLabel and a JTextField for the username input
		JPanel namePanel = new JPanel();
		namePanel.add(new JLabel("Email:      "));
		userName = new JTextField(15);
		namePanel.add(userName);
		top.add(namePanel);

		//pwPanel contains a JLabel and a JTextField for the password input
		JPanel pwPanel = new JPanel();
		pwPanel.add(new JLabel("Password:"));
		password = new JPasswordField(15);
		pwPanel.add(password);
		top.add(pwPanel);


		//signupPanel contains a signup button
		JPanel signupPanel = new JPanel();
		signupPanel.add(new JLabel("If you don't have an account, please sign up:"));
		signupButton = new JButton("Sign up now");
		signupButton.addActionListener(this);
		signupPanel.add(signupButton);
		top.add(signupPanel);

		contentPane.add("North", top);

		JPanel butPanel = new JPanel();
		butPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		noLoginButton = new JButton("Log in (No user name and password required)");
		noLoginButton.addActionListener(this);
		butPanel.add(noLoginButton);
		
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

	//This is where you code for each button click
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == button) //Login button
		{
			// When the button is clicked, check the user name and password, and try to log the user in
			String un = userName.getText();
			String pw = password.getText();
			System.out.println("username: " + un);
			System.out.println("password: " + pw);
			//Current method: create user "noname" with password "nopass", and simplay display the CalGrid Dialog
			User user = new User(un, pw, 0);  //TODO change to user input
			boolean allow = udb.checkIfExist(user); //TODO currently this should always return false
			if (allow)
			{
				CalGrid grid = new CalGrid(new ApptStorageControllerImpl(new ApptStorageNullImpl(user)));
				setVisible( false );	
			}
			else
			{
				//output username or password is incorrect. Please try again
				JOptionPane.showMessageDialog(null, "Username or password is incorrect.", "Try again.", JOptionPane.YES_NO_OPTION);
			}
		}
		else if (e.getSource() == noLoginButton)
		{
			User user = new User( "noname", "nopass");
			CalGrid grid = new CalGrid(new ApptStorageControllerImpl(new ApptStorageNullImpl(user)));
			setVisible( false );	
		}
		else if(e.getSource() == signupButton) //Sign-up button
		{
			SignUpDialog sud = new SignUpDialog();
		}
		else if(e.getSource() == closeButton) //close button
		{
			//ask if the user really want to leave
			int n = JOptionPane.showConfirmDialog(null, "Exit Program ?",
					"Confirm", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION)
				System.exit(0);
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
