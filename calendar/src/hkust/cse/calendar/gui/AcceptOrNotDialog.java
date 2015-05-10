package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptDB;
import hkust.cse.calendar.apptstorage.LocationDB;
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
	private JPanel panel;
	private JLabel comment1;
	private JLabel comment2;
	private JButton bAccept;
	private JButton bReject;
	private MessageBody.UserResponse response = MessageBody.UserResponse.NotYet;
	private String userOrLoc;
	private int msgid;

	public AcceptOrNotDialog(int msgid, String userOrLoc) {
		this.userOrLoc = userOrLoc;
		this.msgid = msgid;

		// choose type
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

		} else {// maybe invite

		}
		this.setSize(400, 100);
		this.setAlwaysOnTop(true);

		panel = new JPanel();
		// System.out.println("trying to make group invitation dialogue");

		bAccept = new JButton("Accept");
		bAccept.addActionListener(this);

		bReject = new JButton("Reject");
		bReject.addActionListener(this);

		panel.add(comment1);
		// groupApptFramePanel.add(comment2);
		panel.add(bAccept);
		panel.add(bReject);

		this.add(panel);
		pack();
		this.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bAccept) {
			// choose type for accept
			if (userOrLoc.equals("user")) {
				SortedMap<Integer, MessageBody> deleteUser = MessageStorage
						.getDeleteUser();

				MessageBody tmpMessageBody = deleteUser.get(msgid);

				int userToBeDeletedID = tmpMessageBody.getUserToBeDeletedID();
				// save id before del for check if last confirm
				deleteUser.remove(msgid);
				// find if userToBeDeleted exist

				if (!MessageStorage.isExistID(userToBeDeletedID, "user")) {
					// this really delete the user and related events
					ApptDB adb = new ApptDB();
					adb.delEventsWithUser(userToBeDeletedID);
					// TODO udb.deleteUser(userToBeDeletedID);
					// since user is the last one to confirm

				}// else need more people confirm so do nothing
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
					ApptDB adb = new ApptDB();
					adb.delEventsWithLocation(locationToBeDeletedID);
					LocationDB ldb = LocationDB.getInstance();
					ldb.deleteLocation(locationToBeDeletedID);

				}// else need more confirm
				;
			} else { // invite?

			}

			this.dispose();
		} else if (e.getSource() == bReject) {
			// choose type for reject
			// remove all msg related to this user or location
			if (userOrLoc.equals("user")) {
				SortedMap<Integer, MessageBody> deleteUser = MessageStorage
						.getDeleteUser();

				MessageBody tmpMessageBody = deleteUser.get(msgid);

				int userToBeDeletedID = tmpMessageBody.getUserToBeDeletedID();

				MessageStorage.deleteAllMsgWithUserOrLocationToBeDeleted(
						userToBeDeletedID, "user");
			} else if (userOrLoc.equals("location")) {
				SortedMap<Integer, MessageBody> deleteLocation = MessageStorage
						.getDeleteLocation();

				MessageBody tmpMessageBody = deleteLocation.get(msgid);

				int locationToBeDeletedID = tmpMessageBody.getLocationToBeDeletedID();

				MessageStorage.deleteAllMsgWithUserOrLocationToBeDeleted(
						locationToBeDeletedID, "location");
			} else {// invite?

			}

			this.dispose();
		}

	}
}
