package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.gui.AcceptOrNotDialog;
import hkust.cse.calendar.gui.AppList;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.MessageBody;
import hkust.cse.calendar.unit.TimeMachine;
import hkust.cse.calendar.unit.TimeSpan;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class MessageStorage {
	static SortedMap<Integer, MessageBody> deleteUser = new ConcurrentSkipListMap<Integer, MessageBody>();
	// deleteUser.put(-1, new
	// MessageBody(-1,-1,-1,MessageBody.UserResponse.NotYet,LocalDateTime.now());
	static SortedMap<Integer, MessageBody> deleteLocation = new ConcurrentSkipListMap<Integer, MessageBody>();

	private MessageStorage() {

	}

	public static SortedMap<Integer, MessageBody> getDeleteUser() {
		return deleteUser;
	}

	public static SortedMap<Integer, MessageBody> getDeleteLocation() {
		// TODO Auto-generated method stub
		return deleteLocation;
	}

	// public static void popupMsgAndSave(int msgid, String userOrLocation) {
	// // if(userOrLocation.equals("user")){
	// // //MessageBody tmpMB = deleteUser.get(msgid);
	// // //System.out.println("user with id " + tmpMB.getUserToBeDeletedID()
	// // //+ "will be deleted. Do you accept?");
	// // AcceptOrNotDialog tmpDialog = new AcceptOrNotDialog(msgid,"user");
	// //
	// // // if yes, if no
	// // } else if (userOrLocation.equals("location")){
	// // AcceptOrNotDialog tmpDialog = new AcceptOrNotDialog(msgid,"location");
	// // }
	//
	// }

	public static SortedMap<Integer, LocalDateTime> getCreatorToLastRelatedEventMap(
			int IDofUserToBeDeleted) {
		// TODO Auto-generated method stub
		// locked the user
		System.out.println("MessageStorage.getCreatorToLastRelatedEventMap()");
		SortedMap<Integer, LocalDateTime> tmpmap = new ConcurrentSkipListMap<Integer, LocalDateTime>();

		// appt[] apptlist=getApptListInvolveUserInFuture(uid);
		ApptDB adb = new ApptDB();
		Appt[] apptlist = adb.getFutureApptWithUser(IDofUserToBeDeleted);// getApptListInvolveUserInFuture(IDofUserToBeDeleted);
		// this may work
		
		Map<Integer, List<LocalDateTime>> CreatorToEndTimesMap = new HashMap<Integer, List<LocalDateTime>>();
		for (Appt tmpappt : apptlist) {

			LocalDateTime endTime = tmpappt.getAppointment().getTimeSpan()
					.EndTime().toLocalDateTime();
			LocalDateTime currTime = TimeMachine.getInstance().getTMTimestamp()
					.toLocalDateTime();

			if (currTime.isBefore(endTime)) {// check again to ensure only
												// future events count

				int tmpUserID = tmpappt.getAppointment().getCreaterUID();

				List<LocalDateTime> tmplist;
				if (CreatorToEndTimesMap.containsKey(tmpUserID)) {
					tmplist = CreatorToEndTimesMap.get(tmpUserID);

				} else {
					tmplist = new ArrayList<LocalDateTime>();
				}

				tmplist.add(endTime);

				CreatorToEndTimesMap.put(tmpUserID, tmplist);
			}
		}
		// now CreatorToEndTimesMap is group by creator
		// try get max endtime for diff creator

		SortedMap<Integer, LocalDateTime> CreatorToMaxEndTimeMap = new ConcurrentSkipListMap<Integer, LocalDateTime>();
		for (int i : CreatorToEndTimesMap.keySet()) {
			List<LocalDateTime> listOfDateTime = CreatorToEndTimesMap.get(i);
			LocalDateTime tmpmax = Collections.max(listOfDateTime,
					new Comparator<LocalDateTime>() {
						public int compare(LocalDateTime o1, LocalDateTime o2) {
							return o1.compareTo(o2);
						}
					});// really need to give comparator??
			CreatorToMaxEndTimeMap.put(i, tmpmax);

		}
		// now we got max time
		System.out.println("MessageStorage.getCreatorToLastRelatedEventMap()");
		return CreatorToMaxEndTimeMap;
	}

	// find id (user, location)
	public static boolean isExistID(int id, String userOrLoc) {
		if (userOrLoc.equals("user")) {
			for (MessageBody tmpmb : deleteUser.values()) {
				if (tmpmb.getUserToBeDeletedID() == id) {
					return true;
				}
			}
		} else if (userOrLoc.equals("location")) {

		}
		return false;
	}

	public static void deleteAllMsgWithUserOrLocationToBeDeleted(
			int userOrLocToBeDeletedID, String userOrLoc) {
		if (userOrLoc.equals("user")) {
			for (int tmpkey : deleteUser.keySet()) {
				MessageBody tmpMessageBody = deleteUser.get(tmpkey);//
				if (tmpMessageBody.getUserToBeDeletedID() == userOrLocToBeDeletedID) {
					deleteUser.remove(tmpkey);
				}
			}

		} else if (userOrLoc.equals("location")) {
			for (int tmpkey : deleteLocation.keySet()) {
				MessageBody tmpMessageBody = deleteLocation.get(tmpkey);//
				if (tmpMessageBody.getLocationToBeDeletedID() == userOrLocToBeDeletedID) {
					deleteLocation.remove(tmpkey);
				}
			}
		} else {
			System.out
					.println("This part should never run. "
							+ "MessageStorage.deleteAllMsgWithUserOrLocationToBeDeleted()");
		}

	}

}
