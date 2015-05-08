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
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class UpdateAccountInfoDialog extends JFrame implements ActionListener{

	private JTextField usernameTF;		//TextField for username
	private JTextField firstnameTF;
	private JTextField lastnameTF;
	private JPasswordField password1PF;	//PasswordField for password2
	private JPasswordField password2PF;	//PasswordField for password2
	private JButton updateB;			//Button for confirm signup
	private JButton cancelB;			//Button for cancel signup
	private JCheckBox adminCB;
	private UserDB udb;
	private int uid;

	//constructor: construct the GUI
	public UpdateAccountInfoDialog (int uid)// get uid
	{
		this.uid=uid;
		udb = new UserDB();

		setTitle("Update Account Info");
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
		messPanel.add(new JLabel("Update Info"));
		sud.add(messPanel);

		JPanel lnPanel = new JPanel();
		lnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		lnPanel.add(new JLabel("Last name:"));
		lastnameTF = new JTextField(15);
		;
		lastnameTF.setText(udb.getUserWithUID(uid).LastName());
		lnPanel.add(lastnameTF);
		sud.add(lnPanel);
		
		JPanel fnPanel = new JPanel();
		fnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		fnPanel.add(new JLabel("First name:"));
		firstnameTF = new JTextField(15);
		firstnameTF.setText(udb.getUserWithUID(uid).FirstName());
		fnPanel.add(firstnameTF);
		sud.add(fnPanel);
		
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		namePanel.add(new JLabel("Email address (username):"));
		usernameTF = new JTextField(15);
		usernameTF.setText(udb.getUserWithUID(uid).ID());
		namePanel.add(usernameTF);
		sud.add(namePanel);

		JPanel pwPanel1 = new JPanel();
		pwPanel1.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pwPanel1.add(new JLabel("Password:"));
		password1PF = new JPasswordField(15);
		password1PF.setText(udb.getUserWithUID(uid).Password());
		pwPanel1.add(password1PF);
		sud.add(pwPanel1);

		JPanel pwPanel2 = new JPanel();
		pwPanel2.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pwPanel2.add(new JLabel("Type password again:"));
		password2PF = new JPasswordField(15);
		password2PF.setText(udb.getUserWithUID(uid).Password());
		pwPanel2.add(password2PF);
		sud.add(pwPanel2);
		
		JPanel adminPanel = new JPanel();
		adminPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		adminPanel.add(new JLabel("Admin:"));
		adminCB = new JCheckBox();
		adminCB.setSelected(udb.getUserWithUID(uid).Admin()==1);
		adminPanel.add(adminCB);
		sud.add(adminPanel);

		contentPane.add("North", sud);

		JPanel butPanel = new JPanel();
		cancelB = new JButton("Cancel");
		cancelB.addActionListener(this);
		butPanel.add(cancelB);
		updateB = new JButton("Update");
		updateB.addActionListener(this);
		butPanel.add(updateB);

		contentPane.add("South", butPanel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//define all button clicks
		if (e.getSource() == updateB) //sign-up button
		{
			//get all answers
			String lastnameS = lastnameTF.getText();
			String firstnameS = firstnameTF.getText();
			String usernameS = usernameTF.getText();
			String password1S = new String(password1PF.getPassword());
			String password2S = new String(password2PF.getPassword());
			int admin = 0;
			if (adminCB.isSelected() == true)
			{
				admin = 1;
			}
			
			if (lastnameS != null && !(lastnameS.isEmpty())) //lastname cannot be null
			{
				if (firstnameS != null && !(firstnameS.isEmpty())) //first name cannot be null
				{
					if (usernameS != null && !(usernameS.isEmpty())) //email cannot be null
					{
						if (password1S != null && !(password1S.isEmpty())) //password1 cannot be null
						{
							if (password2S != null && !(password2S.isEmpty())) //password 2 cannot be null
							{
								if (ValidEmail(usernameS) == true) //check if email is valid
								{
									if (password1S.compareTo(password2S) == 0) //check if the 2 passwords are the same
									{
										if (ValidString(password1S) == true) //check if password is valid
										{
											User newuser = new User(usernameS, password1S, admin, firstnameS, lastnameS);
											//continue insert into udb
											if (udb.modifyUser(uid,newuser) == true) //check if addUser is successful
											{
												//messageBox: successful!
												//close this dialog afterwards
												JOptionPane.showMessageDialog(null, "Account update successful!");
												dispose();
											}
											else
											{
												JOptionPane.showConfirmDialog(null, "Something went wrong. Please try again", "OK", JOptionPane.YES_NO_OPTION);
											}
										}
										else
										{
											JOptionPane.showConfirmDialog(null, "Password is not valid", "OK", JOptionPane.YES_NO_OPTION);
										}
									}
									else
									{
										JOptionPane.showConfirmDialog(null, "The 2 passwords does not match!", "OK", JOptionPane.YES_NO_OPTION);
									}
								}
								else
								{
									JOptionPane.showConfirmDialog(null, "Email is invalid", "OK", JOptionPane.YES_NO_OPTION);
								}
							}
							else
							{
								JOptionPane.showMessageDialog(null, "Password 2 name cannot be null.");
							}
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Password 1 name cannot be null.");
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Email name cannot be null.");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "First name cannot be null.");
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Last name cannot be null.");
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
	
	public static boolean ValidEmail(String s)
	{
		char[] sChar = s.toCharArray();
		for (int i = 0; i< sChar.length; i++)
		{
			if (sChar[i] == '@')
				return true;
		}
		return false;
	}
}
