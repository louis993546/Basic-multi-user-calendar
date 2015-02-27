package hkust.cse.calendar.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SignUpDialog extends JFrame implements ActionListener{

	private JTextField usernameTF;		//TextField for username
	private JPasswordField password1PF;	//PasswordField for password2
	private JPasswordField password2PF;	//PasswordField for password2
	private JTextField emailTF;			//TextField for email
	private JButton signupB;			//Button for confirm signup
	private JButton cancelB;			//Button for cancel signup
	
	//constructor
	public SignUpDialog () {
		//construct the GUI
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//define all button clicks
		if (e.getSource() == signupB) //signup button
		{
			//get all textfield
			//check if username is available
			//check if the 2 passwords are the same
			//?
		}
		else if (e.getSource() == cancelB) //cancel button
		{
			//close this and go back
		}
		
	}
}
