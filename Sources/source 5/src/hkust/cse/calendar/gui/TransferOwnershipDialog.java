package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.user.UserManagement;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class TransferOwnershipDialog extends JFrame{
	
	private Appt currAppt;
	private ApptStorageControllerImpl _controller;
	private JPanel centerPanel;
	private JList list;
	private DefaultListModel model;
	private JLabel aUserL;
	private JComboBox aUserCB;
	private JButton confirmBut;
	private UserManagement um;
	private ArrayList<String> arrayList;
	private ArrayList<String> selectedUsers;
	private LinkedList<String> linkedList;
	private LinkedList<String> currItem;
	
	public TransferOwnershipDialog(Appt a, ApptStorageControllerImpl controller) {
		setLayout(new BorderLayout());
		selectedUsers = new ArrayList();
		currAppt = a;
		_controller = controller;
		centerPanel = new JPanel();
		model = new DefaultListModel();
		list = new JList(model);
		aUserL = new JLabel("Available Users : ");
		aUserCB = new JComboBox();
		confirmBut = new JButton("Confirm");
		confirmBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = aUserCB.getSelectedIndex();
		        model.addElement(aUserCB.getSelectedItem());
		        selectedUsers.add((String) aUserCB.getSelectedItem());
		        aUserCB.removeItemAt(index);
		        currItem = (LinkedList<String>)currAppt.getAttendList().clone();
		        currItem.addLast(currAppt.getAttendList().getFirst());
		        currItem.removeFirstOccurrence(currAppt.getAttendList().getFirst());
		        currItem.addFirst(selectedUsers.get(0));
		        currItem.removeLastOccurrence(selectedUsers.get(0));
				currAppt.setAttendList(currItem);
				
				//not yet completed so add all user to waiting list for testing
				//currAppt.setWaitingList(new LinkedList<String>(arrayList));
				
				setVisible(false);
				dispose();
		      }
		});
		um = UserManagement.getInstance();
		centerPanel.add(aUserL);
		centerPanel.add(aUserCB);
		centerPanel.add(confirmBut);
		add(centerPanel, BorderLayout.CENTER);
		this.setSize(300, 300);
		this.show();
		linkedList = (LinkedList<String>) a.getAttendList().clone();
		Iterator<String> attendIt = linkedList.iterator();
		while(attendIt.hasNext()) {
			String attendID = (String) attendIt.next();
			if(!attendID.equals(a.getAttendList().getFirst())){
				aUserCB.addItem(attendID);
			}
		}
	}
	//select user and place them into waiting list
	
}