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

public class AddUserDialog extends JFrame{
	
	private Appt currAppt;
	private ApptStorageControllerImpl _controller;
	private JPanel centerPanel;
	private JLabel sUserL;
	private JList list;
	private DefaultListModel model;
	private JScrollPane sPane;
	private JLabel aUserL;
	private JComboBox aUserCB;
	private JButton addBut;
	private JButton removeBut;
	private JButton confirmBut;
	private UserManagement um;
	private ArrayList<String> arrayList;
	private ArrayList<String> selectedUsers;
	
	public AddUserDialog(Appt a, ApptStorageControllerImpl controller) {
		setLayout(new BorderLayout());
		selectedUsers = new ArrayList();
		currAppt = a;
		_controller = controller;
		centerPanel = new JPanel();
		sUserL = new JLabel("Selected Participants : ");
		model = new DefaultListModel();
		list = new JList(model);
		sPane = new JScrollPane(list);
		aUserL = new JLabel("Available Users : ");
		aUserCB = new JComboBox();
		addBut = new JButton("Add");
		addBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = aUserCB.getSelectedIndex();
		        model.addElement(aUserCB.getSelectedItem());
		        selectedUsers.add((String) aUserCB.getSelectedItem());
		        aUserCB.removeItemAt(index);
		      }
		});
		removeBut = new JButton("Remove");
		removeBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = list.getSelectedIndex();
				if (index != -1){
					aUserCB.addItem(model.get(index));
					model.removeElementAt(index);
					selectedUsers.remove(index);
				}
		      }
		});
		confirmBut = new JButton("Confirm");
		confirmBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currAppt.setWaitingList(new LinkedList<String>(selectedUsers));
				
				//not yet completed so add all user to waiting list for testing
				//currAppt.setWaitingList(new LinkedList<String>(arrayList));
				
				setVisible(false);
				dispose();
		      }
		});
		um = UserManagement.getInstance();
		centerPanel.add(sUserL);
		centerPanel.add(sPane);
		centerPanel.add(aUserL);
		centerPanel.add(aUserCB);
		centerPanel.add(addBut);
		centerPanel.add(removeBut);
		centerPanel.add(confirmBut);
		add(centerPanel, BorderLayout.CENTER);
		clearAllList();
		this.setSize(300, 300);
		this.show();
		arrayList = um.getAllUserIDs();
		for(int i=0; i<arrayList.size(); i++) {
			if(!arrayList.get(i).equals(a.getAttendList().getFirst()))
				aUserCB.addItem(arrayList.get(i));
		}
	}
	
	public void clearAllList() {
		currAppt.getWaitingList().clear();
		currAppt.getRejectList().clear();
		currAppt.getAttendList().clear();
		currAppt.addAttendant(_controller.getDefaultUser().ID());
	}
	
	//select user and place them into waiting list
	
}
