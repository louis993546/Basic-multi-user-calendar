package hkust.cse.calendar.gui;

import hkust.cse.calendar.Main.CalendarMain;
import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserStorage;
import hkust.cse.calendar.userstorage.UserStorageController;
import hkust.cse.calendar.xmlfactory.ApptXmlFactory;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConfirmDeleteMeDialog extends JDialog implements ActionListener {
	private User mUser;
	private UserStorageController userController;
	private JButton confirmButton;
	
	public ConfirmDeleteMeDialog(User user) {
		mUser = user;
		userController = UserStorageController.getInstance();
		
		setTitle("Delete confirmation - " + mUser.ID());
		setSize(250, 200);
		Container contentPane;
		contentPane = getContentPane();
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		
		JPanel msgPanel = new JPanel();
		msgPanel.add(new JLabel("Your account is going to be deleted =(\nAll your information and appointment will be deleted"));
		top.add(msgPanel);
		
		JPanel buttonPanel = new JPanel();
		confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(this);
		buttonPanel.add(confirmButton);
		top.add(buttonPanel);
		
		contentPane.add("Center", top);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == confirmButton) {
			userController.manageUsers(mUser, UserStorageController.DELETE);
			userController.removeUserFromToBeDeletedList(mUser);
			
			for(Iterator<Entry<User, HashMap<TimeSpan, Appt>>> it = ApptStorage.mUserToAppts.entrySet().iterator(); it.hasNext();) {
				User key = it.next().getKey();
				if(key.ID().equals(mUser.ID())) {
					it.remove();
				}
			}
			
			ApptXmlFactory apptFactory = new ApptXmlFactory();
			apptFactory.deleteUserAppt(mUser);
			
			CalendarMain.logOut = true;
			setVisible(false); //you can't see me!
			dispose(); //Destroy the JFrame object
		}
	}

}
