package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserStorageController;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class UserManager extends JDialog implements ActionListener, ListSelectionListener {
	private UserStorageController userController;
	private JList<User> userList;
	private DefaultListModel<User> userListModel;
	private JList<String> toBeDeleteUserList;
	private JScrollPane userScrollPane;
	private JScrollPane toBeDeleteUserScrollPane;
	private JTextField userInput;
	private JButton modifyButton;
	private JButton deleteButton;
	private JButton cancelButton;

	public UserManager() {
		userController = UserStorageController.getInstance();

		setTitle("User Manager");
		setSize(500, 350);

		Container contentPane;
		contentPane = getContentPane();
		
		userListModel = new DefaultListModel<User>();
		userList = new JList<User>(userListModel);
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.addListSelectionListener(this);
		
		JPanel upper = new JPanel();
		Border userBorder = new TitledBorder("Users");
		upper.setBorder(userBorder);
		upper.setLayout(new BorderLayout());
		userScrollPane = new JScrollPane(userList , JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		userScrollPane.setPreferredSize(new Dimension(400, 150));
		upper.add(userScrollPane);
		contentPane.add("North", upper);

		toBeDeleteUserList = new JList<String>();
		toBeDeleteUserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		toBeDeleteUserList.setEnabled(false);
		toBeDeleteUserList.addListSelectionListener(this);

		JPanel centre = new JPanel();
		Border toBeDeleteUsersBorder = new TitledBorder("To Be Delete");
		centre.setBorder(toBeDeleteUsersBorder);
		centre.setLayout(new BorderLayout());
		toBeDeleteUserScrollPane = new JScrollPane(toBeDeleteUserList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		toBeDeleteUserScrollPane.setPreferredSize(new Dimension(400, 150));
		centre.add(toBeDeleteUserScrollPane);
		contentPane.add("Center", centre);

		JPanel bottom = new JPanel();
		userInput = new JTextField(10);
		userInput.setEditable(false);
		userInput.setEnabled(false);
		bottom.add(userInput);

		modifyButton = new JButton("Modify");
		modifyButton.addActionListener(this);
		bottom.add(modifyButton);

		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(this);
		bottom.add(deleteButton);

		cancelButton = new JButton("Close");
		cancelButton.addActionListener(this);
		bottom.add(cancelButton);

		contentPane.add("South", bottom);
		pack();
		setLocationRelativeTo(null);
		updateUserList();
		setVisible(true);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == userList) {
			toBeDeleteUserList.clearSelection();
			if(userList.getSelectedValue() != null) {
				userInput.setText(userList.getSelectedValue().ID());
			}
		}
		else if(e.getSource() == toBeDeleteUserList) {
			userList.clearSelection();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == modifyButton) {
			User selectedUser = userList.getSelectedValue();

			if(selectedUser == null) {
				JOptionPane.showMessageDialog(null, "No user is selected!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else {
				ProfileManager userProfileManager = new ProfileManager(selectedUser);
			}
		}
		else if(e.getSource() == deleteButton) {
			User selectedUser = userList.getSelectedValue();

			if(selectedUser == null) {
				JOptionPane.showMessageDialog(null, "No user is selected!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else {
				if(selectedUser.isAdmin()) {
					JOptionPane.showMessageDialog(null, "Admin cannot be delete!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					Object[] options ={ "Yes", "No" };  
					int option = JOptionPane.showOptionDialog(null, "Are you sure to delete " + selectedUser.ID() + "?\nOperation cannot be canceled", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]); 
					if(option == 0) {
						userController.putUserToBeDeletedList(selectedUser);
						updateUserList();
						userList.clearSelection();
					}	
				}
			}
		}
		else if(e.getSource() == cancelButton) {
			setVisible(false); //you can't see me!
			dispose(); //Destroy the JFrame object
		}
	}

	private void updateUserList() {
		User[] users = userController.retrieveUsers();
		ArrayList<String> toBeDeleteUser = userController.retrieveUsersFromToBeDeletedList();
		ArrayList<User> userId = new ArrayList<User>();

		for(int i = 0; i < users.length; i++) {
			if(!toBeDeleteUser.contains(users[i].ID())) {
				userId.add(users[i]);
			}
		}

		userList.setListData(userId.toArray(new User[userId.size()]));
		this.toBeDeleteUserList.setListData(toBeDeleteUser.toArray(new String[toBeDeleteUser.size()]));
	}
}
