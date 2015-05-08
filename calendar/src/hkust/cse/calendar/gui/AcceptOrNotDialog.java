package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.MessageStorage;
import hkust.cse.calendar.unit.MessageBody;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.SortedMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.TableModel;

public class AcceptOrNotDialog extends JFrame implements ActionListener {
	private JPanel groupApptFramePanel;
	private JLabel comment1;
	private JLabel comment2;
	private JButton bAcceptGroupInv;
	private JButton bRejectGroupInv;
	private MessageBody.UserResponse response = MessageBody.UserResponse.NotYet;
	private String userOrLoc;
	private int msgid;

	public AcceptOrNotDialog(int msgid, String userOrLoc) {
		// "user with id "+tmpMB.getUserToBeDeletedID()etedID()+
		// "will be deleted. Do you accept?");
		this.userOrLoc = userOrLoc;
		this.msgid = msgid;
		if (userOrLoc.equals("user")) {
			this.setTitle("User with id " + " will be deleted. Do you accept?");
			comment1 = new JLabel(
					"This user is in some event created by you in the future");
			// comment2 = new JLabel("Time: " + invite.TimeSpan().toString());

		} else if (userOrLoc.equals("location")) {
			this.setTitle("Location with id "
					+ " will be deleted. Do you accept?");
			comment1 = new JLabel(
					"This location is used by some event created by you in the future");

		} else {

		}
		this.setSize(400, 100);
		this.setAlwaysOnTop(true);

		groupApptFramePanel = new JPanel();
		// System.out.println("trying to make group invitation dialogue");

		bAcceptGroupInv = new JButton("Accept");
		bAcceptGroupInv.addActionListener(this);

		bRejectGroupInv = new JButton("Reject");
		bRejectGroupInv.addActionListener(this);

		groupApptFramePanel.add(comment1);
		// groupApptFramePanel.add(comment2);
		groupApptFramePanel.add(bAcceptGroupInv);
		groupApptFramePanel.add(bRejectGroupInv);

		this.add(groupApptFramePanel);
		pack();
		this.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bAcceptGroupInv) {
			if (userOrLoc.equals("user")) {
				SortedMap<Integer, MessageBody> deleteUser = MessageStorage
						.getDeleteUser();
				MessageBody tmpMessageBody = deleteUser.get(msgid);
				int userToBeDeletedID = tmpMessageBody.getUserToBeDeletedID();
				deleteUser.remove(msgid);
				// find if userToBeDeleted exist
				
				if (!MessageStorage.isExistID(userToBeDeletedID, "user")) {
					// really delete the user
					// since user is the last one to confirm

				}
				;

			} else if (userOrLoc.equals("location")) {
				SortedMap<Integer, MessageBody> deleteLocation = MessageStorage
						.getDeleteLocation();
				MessageBody tmpMessageBody = deleteLocation.get(msgid);
				int locationToBeDeletedID = tmpMessageBody
						.getLocationToBeDeletedID();
				deleteLocation.remove(msgid);
				// find if locationToBeDel exist
				if (!MessageStorage
						.isExistID(locationToBeDeletedID, "location")) {
					// really delete the location
					// since user is the last one to confirm
					
				}//else need more confirm
				;
			} else {

			}

			this.dispose();
		} else if (e.getSource() == bRejectGroupInv) {
			// remove all msg related to this user or location
			this.dispose();
		}

	}
}
