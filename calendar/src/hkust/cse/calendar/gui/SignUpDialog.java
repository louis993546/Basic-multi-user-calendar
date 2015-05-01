package hkust.cse.calendar.gui;

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

public class SignUpDialog extends JFrame implements ActionListener{

	private JTextField usernameTF;		//TextField for username
	private JPasswordField password1PF;	//PasswordField for password2
	private JPasswordField password2PF;	//PasswordField for password2
	private JTextField emailTF;			//TextField for email
	private JButton signupB;			//Button for confirm signup
	private JButton cancelB;			//Button for cancel signup
	private UserDB udb;

	//constructor: construct the GUI
	public SignUpDialog ()
	{
		udb = new UserDB();

		setTitle("Sign up");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		Container contentPane;
		contentPane = getContentPane();

		//create a new JPanel to hold everything
		JPanel sud = new JPanel();
		sud.setLayout(new BoxLayout(sud, BoxLayout.Y_AXIS));

		//messPanel contains message to be displayed
		JPanel messPanel = new JPanel();
		messPanel.add(new JLabel("Create account to login"));
		sud.add(messPanel);

		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		namePanel.add(new JLabel("User Name:"));
		usernameTF = new JTextField(15);
		namePanel.add(usernameTF);
		sud.add(namePanel);

		JPanel pwPanel1 = new JPanel();
		pwPanel1.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pwPanel1.add(new JLabel("Password:  "));
		password1PF = new JPasswordField(15);
		pwPanel1.add(password1PF);
		sud.add(pwPanel1);

		JPanel pwPanel2 = new JPanel();
		pwPanel2.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pwPanel2.add(new JLabel("Type password again:  "));
		password2PF = new JPasswordField(15);
		pwPanel2.add(password2PF);
		sud.add(pwPanel2);
		
		//TODO add admin selection box type thing

		contentPane.add("North", sud);

		JPanel butPanel = new JPanel();
		cancelB = new JButton("Cancel");
		cancelB.addActionListener(this);
		butPanel.add(cancelB);
		signupB = new JButton("Create");
		signupB.addActionListener(this);
		butPanel.add(signupB);

		contentPane.add("South", butPanel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//define all button clicks
		if (e.getSource() == signupB) //sign-up button
		{
			//get all answers
			String usernameS = usernameTF.getText();
			String password1S = new String(password1PF.getPassword());
			String password2S = new String(password2PF.getPassword());
			int admin; //TODO get Admin or not
			
			if (ValidString(usernameS) == true)
			{
				if (password1S == password2S) //check if the 2 passwords are the same
				{
					if (ValidString(password1S) == true)
					{
						User newuser = new User(usernameS, password1S, 0); //TODO user constructor with admin rights
						if (udb.checkIfExist(newuser) == false) //check if username is available
						{
							//continue insert into udb
							if (udb.addUser(newuser) == true)
							{
								//messageBox: successful!
								//close this dialog afterwards
							}
							else
							{
								//messagebox: something wrong when inserting. Please try again
							}
						}
						else
						{
							//messagebox: id already exist
						}
					}
					else
					{
						//messagebox: password not valid
					}
				}
				else
				{
					//MessageBox: password does not match
				}
			}
			else
			{
				//messageBox: username not valid
			}
		}
		else if (e.getSource() == cancelB) //cancel button
		{
			//close this and go back
			int n = JOptionPane.showConfirmDialog(null, "Discard all changes?",
					"Confirm", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION)
				dispose();
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
