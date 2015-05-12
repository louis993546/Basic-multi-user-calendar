package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptDB;
import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.apptstorage.ApptStorageNullImpl;
import hkust.cse.calendar.apptstorage.LocationDB;
import hkust.cse.calendar.apptstorage.MessageDB;
import hkust.cse.calendar.apptstorage.UserDB;
import hkust.cse.calendar.unit.Message;
import hkust.cse.calendar.unit.User;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

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
	private MessageDB mdb;
	private ApptDB adb;
	private LocationDB ldb;

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
		mdb = new MessageDB();
		adb = new ApptDB();
		ldb = LocationDB.getInstance();

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
		userName.setText("a@");
		namePanel.add(userName);
		top.add(namePanel);

		//pwPanel contains a JLabel and a JTextField for the password input
		JPanel pwPanel = new JPanel();
		pwPanel.add(new JLabel("Password:"));
		password = new JPasswordField(15);
		password.setText("a");
		password.addActionListener(new ActionListener()
		{
            public void actionPerformed(ActionEvent e)
            {
            	button.doClick();
            }
        });
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
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == button) //Login button
		{
			String un = userName.getText();
			String pw = password.getText();
			User user = new User(un, pw, 0); 
			boolean allow = udb.checkIfExist(user);
			if (allow)
			{
				user = udb.getFullUser(user);
				CalGrid grid = new CalGrid(new ApptStorageControllerImpl(new ApptStorageNullImpl(user)), false);
				System.out.println("Admin: " + grid.controller.getDefaultUser().Admin());
				System.out.println("User ID: " + user.getUID());
				ArrayList<Message> allMessages = mdb.getAllMessageForUser(user.getUID());
				System.out.println("allMessages.size(): " + allMessages.size());
				for (int i = 0; i<allMessages.size(); i++)
				{
					switch (allMessages.get(i).getType())
					{
					case 1: //Location deletion confirmation
						int n1 = JOptionPane.showConfirmDialog(null, "Can I delete locatoin X?", "Confirm Location Deletion", JOptionPane.YES_NO_OPTION);
						if (n1 == JOptionPane.YES_OPTION)
						{
							if (allMessages.get(i).getUserUIDList().size() == 1)
							{
								LocationDB.getInstance().deleteLocation(allMessages.get(i).getEditID());	//remove location
								mdb.deleteMessage(allMessages.get(i).getMessageID());	//remove message
							}
							else
							{	//remove user from list
								ArrayList<Integer> newUIDList = allMessages.get(i).getUserUIDList();
								newUIDList.remove(user.getUID());
								Message tempM = new Message(allMessages.get(i).getType(), newUIDList ,allMessages.get(i).getEditID());
								mdb.modifyMessage(allMessages.get(i).getMessageID(), tempM);
							}
						}
						else
						{
							mdb.deleteMessage(allMessages.get(i).getMessageID());	//remove message
						}
						break;
					case 2: //User deletion confirmation
						int n2 = JOptionPane.showConfirmDialog(null, "Can I delete user X?", "Confirm User Deletion", JOptionPane.YES_NO_OPTION);
						if (n2 == JOptionPane.YES_OPTION)
						{
							if (allMessages.get(i).getUserUIDList().size() == 1)
							{
								udb.deleteUser(allMessages.get(i).getEditID());		//remove user
								mdb.deleteMessage(allMessages.get(i).getMessageID());	//remove message
							}
							else
							{	//remove user from list
								ArrayList<Integer> newUIDList = allMessages.get(i).getUserUIDList();
								newUIDList.remove(user.getUID());
								Message tempM = new Message(allMessages.get(i).getType(), newUIDList ,allMessages.get(i).getEditID());
								mdb.modifyMessage(allMessages.get(i).getMessageID(), tempM);
							}
						}
						else
						{
							mdb.deleteMessage(allMessages.get(i).getMessageID());	//remove message
						}
						break;
					case 3: //Appointment deletion confirmation
						int n3 = JOptionPane.showConfirmDialog(null, "Can I delete appointment X?", "Confirm Appointment Deletion", JOptionPane.YES_NO_OPTION);
						if (n3 == JOptionPane.YES_OPTION)
						{
							if (allMessages.get(i).getUserUIDList().size() == 1)
							{
								adb.deleteAppt(allMessages.get(i).getEditID());		//remove user
								mdb.deleteMessage(allMessages.get(i).getMessageID());	//remove message
							}
							else
							{	//remove user from list
								ArrayList<Integer> newUIDList = allMessages.get(i).getUserUIDList();
								newUIDList.remove(user.getUID());
								Message tempM = new Message(allMessages.get(i).getType(), newUIDList ,allMessages.get(i).getEditID());
								mdb.modifyMessage(allMessages.get(i).getMessageID(), tempM);
							}
						}
						else
						{
							mdb.deleteMessage(allMessages.get(i).getMessageID());	//remove message
						}
						break;
					case 4: //confirm appointment invitation
						if ((adb.getApptByID(allMessages.get(i).getEditID()) != null) && (grid.timeMachine.getTMTimestamp().after(adb.getApptByID(allMessages.get(i).getEditID()).TimeSpan().StartTime()) == true))
						{
							mdb.deleteMessage(allMessages.get(i).getMessageID());
							adb.deleteAppt(allMessages.get(i).getEditID());
						}
						else
						{
							String op = "Will you attend this event :";
							String apptTitle = adb.getApptByID(allMessages.get(i).getEditID()).getTitle();
							op = op + apptTitle;
							int n4 = JOptionPane.showConfirmDialog(null, op, "Cofirm Appointment Invitation", JOptionPane.YES_NO_CANCEL_OPTION);
							if (n4 == JOptionPane.YES_NO_OPTION)
							{
								if (allMessages.get(i).getUserUIDList().size() == 1)
								{
									mdb.deleteMessage(allMessages.get(i).getMessageID());	//remove message
								}
								else
								{	//remove user from list
									mdb.removeUIDFromUIDList(user.getUID(), allMessages.get(i).getMessageID());
								}
								adb.addUIDToGoingList(user.getUID(), allMessages.get(i).getMessageID());
								adb.removeUIDFromWaitingList(user.getUID(), allMessages.get(i).getMessageID());
							}
							else
							{
								adb.deleteAppt(allMessages.get(i).getEditID());
								mdb.removeAllEmptyMessages();
							}
						}
						break;
					}
				}
				grid.updateDB();
				grid.updateAppList();
				grid.UpdateCal();
				grid.updateReminderCheckerApptlist();
				grid.setVisible(true);
				setVisible(false);	
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Username or password is incorrect.", "Try again.", JOptionPane.YES_NO_OPTION);
			}
		}
		else if (e.getSource() == noLoginButton)
		{
			User user = new User( "noname", "nopass");
			CalGrid grid = new CalGrid(new ApptStorageControllerImpl(new ApptStorageNullImpl(user)), true);
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
