package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserStorageController;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.text.Position;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class JointApptUserManager extends JDialog implements ActionListener {
	private UserStorageController userController;	
	private CalGrid parent;
	private AppScheduler lock;
	
	private LinkedList<String> allList;
	
	private JList<User> remainingList;
	private DefaultListModel<User> remainingListModel;
	private JScrollPane remainingSP;
	
	private JList<User> invitedList;
	private DefaultListModel<User> invitedListModel;
	private JScrollPane invitedSP;

	private JLabel remaininguserL;
	private JLabel inviteduserL;
	
	private JButton addBut;
	private JButton removeBut;
	private JButton addallBut;
	private JButton removeallBut;
	
	private JButton saveButton;
	private JButton cancelButton;

	private ArrayList<User> inviteduserName = new ArrayList<User>();
	private ArrayList<User> remaininguserName = new ArrayList<User>();
	
	private Appt new_appt;
	private User current;
	
	// ?? should have list(s) containing participants ??
	public JointApptUserManager(CalGrid appt, AppScheduler appScheduler, Appt newappt) {
		parent = appt;
		new_appt = newappt;
		lock = appScheduler;
		userController = UserStorageController.getInstance();
		setAlwaysOnTop(true);
		setTitle("Select Participants: ");
		Container contentPane;
		contentPane = getContentPane();
		
		remainingListModel = new DefaultListModel<User>();
		remainingList = new JList<User>();
		remainingList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		invitedListModel = new DefaultListModel<User>();
		invitedList = new JList<User>();
		invitedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		JPanel top = new JPanel();
		
	    JPanel leftPanel = new JPanel(new BorderLayout());
	    remaininguserL = new JLabel("Available Users:");
	    leftPanel.add(remaininguserL);
		remainingSP = new JScrollPane(remainingList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		remainingSP.setPreferredSize(new Dimension(200, 300));
		leftPanel.add(remainingSP);

	    JPanel rightPanel = new JPanel(new BorderLayout());
	    inviteduserL = new JLabel("Invited Users:");
	    rightPanel.add(inviteduserL);
		invitedSP = new JScrollPane(invitedList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		invitedSP.setPreferredSize(new Dimension(200, 300));
		rightPanel.add(invitedSP);

	    top.add("West", leftPanel);
	    top.add("East", rightPanel);
	    
		JPanel center = new JPanel();
		
		addBut = new JButton(">>");
		addBut.addActionListener(this);
		addallBut = new JButton(">> All");
		addallBut.addActionListener(this);
		removeBut = new JButton("<<");
		removeBut.addActionListener(this);
		removeallBut = new JButton("<< All");
		removeallBut.addActionListener(this);
		center.add(addBut);
		center.add(addallBut);
		center.add(removeBut);
		center.add(removeallBut);

	    JPanel bottom = new JPanel();
		
	    saveButton = new JButton("Invite");
		saveButton.addActionListener(this);
		bottom.add(saveButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		bottom.add(cancelButton);
		
		contentPane.add("North", top);
		contentPane.add("Center", center);
		contentPane.add("South", bottom);

		updateUserList(inviteduserName, remaininguserName);
		pack();
		setVisible(true);		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if (e.getSource() == addBut) {
			if(remainingList.getSelectedValuesList() == null) {
				JOptionPane.showMessageDialog(null, "No user is selected!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else {
				ArrayList<User> selecteduser = new ArrayList<User> (remainingList.getSelectedValuesList());
				addItem(remaininguserName, inviteduserName, selecteduser, true);
			}
		} else if (e.getSource() == addallBut) {
			if(remainingList.getComponentCount() == 0) {
				JOptionPane.showMessageDialog(null, "All users are invited!", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				addallItem(true);
			}
		} else if (e.getSource() == removeBut) {
			if(invitedList.getSelectedValuesList() == null) {
				JOptionPane.showMessageDialog(null, "No user is selected!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else {
				ArrayList<User> selecteduser = new ArrayList<User> (invitedList.getSelectedValuesList());
				addItem(inviteduserName, remaininguserName, selecteduser, false);
			}
		} else if (e.getSource() == removeallBut) {
			if(invitedList.getComponentCount() == 0) {
				JOptionPane.showMessageDialog(null, "No user is invited!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else {
				addallItem(false);
			}
		}
		
		if (e.getSource() == saveButton) {
			User[] empty = null;
			if (inviteduserName.size() > 0) {
				User[] wait = new User[inviteduserName.size()];
				for (int i = 0; i < inviteduserName.size(); i++) {
					wait[i] = inviteduserName.get(i);
				}
				new_appt.setWaitingList(wait);
			} else {
				new_appt.setWaitingList(empty);
			}
			new_appt.setAttendList(empty);
			new_appt.setRejectList(empty);
			setVisible(false);
			if (new_appt.getAllPeople().size() > 0) {
				SuggestedTime suggestion = new SuggestedTime(parent, lock, new_appt);
			}
		} else if (e.getSource() == cancelButton) {
			setVisible(false);
			dispose();
		}
	}
	
	private void updateUserList(ArrayList<User> inviteduserName, ArrayList<User> remaininguserName) {
		if (inviteduserName.isEmpty() && remaininguserName.isEmpty()) {
			initializeList();
		} else {
			remainingList.setListData(remaininguserName.toArray(new User[remaininguserName.size()]));
			invitedList.setListData(inviteduserName.toArray(new User[inviteduserName.size()]));
		}
	}
	
	private void initializeList() {
		allList = new_appt.getAllPeople();
		User[] users = userController.retrieveUsers();
		inviteduserName = new ArrayList<User>();
		remaininguserName = new ArrayList<User>();
		for(int i = 0; i < users.length; i++) {
			if(users[i] != parent.mCurrUser){
				if(checkuserattended(allList, users[i].ID())) {
					inviteduserName.add(users[i]);
				} else {
					remaininguserName.add(users[i]);
				}
			}
		}
		remainingList.setListData(remaininguserName.toArray(new User[remaininguserName.size()]));
		invitedList.setListData(inviteduserName.toArray(new User[inviteduserName.size()]));
	}
	
	private void addItem(ArrayList<User> addfromM, ArrayList<User> addtoM, ArrayList<User> nametoadd, boolean option) {
		for (int i = 0; i < nametoadd.size(); i++) {
			addfromM.remove(nametoadd.get(i));
			addtoM.add(nametoadd.get(i));
		}
		if (option) {
			updateUserList(addtoM, addfromM);
		} else {
			updateUserList(addfromM, addtoM);
		}
	}

	private void addallItem(boolean option) {
		if (option) {
			remainingList.setSelectionInterval(0, remainingList.getLastVisibleIndex());
			ArrayList<User> selecteduser = new ArrayList<User> (remainingList.getSelectedValuesList());
			addItem(remaininguserName, inviteduserName, selecteduser, true);
		} else {
			invitedList.setSelectionInterval(0, invitedList.getLastVisibleIndex());
			ArrayList<User> selecteduser = new ArrayList<User> (invitedList.getSelectedValuesList());
			addItem(inviteduserName, remaininguserName, selecteduser, false);
		}
			
	}
	
	private boolean checkuserattended(LinkedList<String> attended, String userid) {
		for (int i = 0; i < attended.size(); i++){
			if (attended.contains(userid)) {
				return true;
			}
		}
		return false;
	}
}