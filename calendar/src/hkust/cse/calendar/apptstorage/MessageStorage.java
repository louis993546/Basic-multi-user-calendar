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
import java.util.TreeMap;

public class MessageStorage {
	static SortedMap<Integer, MessageBody> deleteUser = new TreeMap<Integer, MessageBody>();
	// deleteUser.put(-1, new
	// MessageBody(-1,-1,-1,MessageBody.UserResponse.NotYet,LocalDateTime.now());
	static SortedMap<Integer, MessageBody> deleteLocation = new TreeMap<Integer, MessageBody>();

	private MessageStorage() {

	}

	public static SortedMap<Integer, MessageBody> getDeleteUser() {
		return deleteUser;
	}

	public static SortedMap<Integer, MessageBody> getDeleteLocation() {
		// TODO Auto-generated method stub
		return deleteLocation;
	}

	public static void popupMsgAndSave(int msgid, String userOrLocation) {
		if(userOrLocation.equals("user")){
			//MessageBody tmpMB = deleteUser.get(msgid);
			//System.out.println("user with id " + tmpMB.getUserToBeDeletedID()
					//+ "will be deleted. Do you accept?");
			AcceptOrNotDialog tmpDialog = new AcceptOrNotDialog(msgid,"user");
			
			// if yes, if no
		} else if (userOrLocation.equals("location")){
			AcceptOrNotDialog tmpDialog = new AcceptOrNotDialog(msgid,"location");
		}

	}

	public static SortedMap<Integer, LocalDateTime> getCreatorToLastRelatedEventMap_notfinish(
			int IDofUserToBeDeleted) {
		// TODO Auto-generated method stub
		// locked the user
		SortedMap<Integer, LocalDateTime> tmpmap = new TreeMap<Integer, LocalDateTime>();

		// appt[] apptlist=getApptListInvolveUserInFuture(uid);
		ApptDB adb=new ApptDB();
		Appt[] apptlist =adb.getFutureApptWithUser(IDofUserToBeDeleted);// getApptListInvolveUserInFuture(IDofUserToBeDeleted);

		Map<Integer, List<LocalDateTime>> CreatorToEndTimesMap = new HashMap<Integer, List<LocalDateTime>>();
		for (Appt tmpappt : apptlist) {
			int tmpUserID = tmpappt.getAppointment().getCreaterUID();
			LocalDateTime endTime = tmpappt.getAppointment().getTimeSpan()
					.EndTime().toLocalDateTime();
			List<LocalDateTime> tmplist = new ArrayList<LocalDateTime>();// default
																			// empty
			if (CreatorToEndTimesMap.containsKey(tmpUserID)) {
				tmplist = CreatorToEndTimesMap.get(tmpUserID);

			}
			tmplist.add(endTime);
			CreatorToEndTimesMap.put(tmpUserID, tmplist);
		}
		// now CreatorToEndTimesMap is group by creator
		// try get max endtime for diff creator

		SortedMap<Integer, LocalDateTime> CreatorToMaxEndTimeMap = new TreeMap<Integer, LocalDateTime>();
		for (int i : CreatorToEndTimesMap.keySet()) {
			List<LocalDateTime> listOfDateTime = CreatorToEndTimesMap.get(i);
			LocalDateTime tmpmax = Collections.max(listOfDateTime, new Comparator<LocalDateTime>() {
				public int compare(LocalDateTime o1, LocalDateTime o2) {
					return o1.compareTo(o2);
				}
			});
			CreatorToMaxEndTimeMap.put(i, tmpmax);

		}
		//now we got max time

		return CreatorToMaxEndTimeMap;
	}
	
	// find id (user, location)
	public static boolean isExistID(int id ,String userOrLoc){
		if(userOrLoc.equals("user")){
			for (MessageBody tmpmb: deleteUser.values()) {
				if(tmpmb.getUserToBeDeletedID()==id){
					return true;
				}
			}
		}else if(userOrLoc.equals("location")){
			
		}
		return false;
	}



}
