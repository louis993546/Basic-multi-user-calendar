package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.gui.AcceptOrNotDialog;
import hkust.cse.calendar.unit.MessageBody;
import hkust.cse.calendar.unit.TimeMachine;

import java.time.LocalDateTime;
import java.util.SortedMap;
import java.util.TreeMap;

public class MessageStorage {
	static SortedMap<Integer, MessageBody> deleteUser = new TreeMap<Integer, MessageBody>();
	//deleteUser.put(-1, new MessageBody(-1,-1,-1,MessageBody.UserResponse.NotYet,LocalDateTime.now());
	SortedMap<Integer, MessageBody> deleteLocation = new TreeMap<Integer, MessageBody>();

	private MessageStorage(){
		
	}
	
	public static SortedMap<Integer, MessageBody> getDeleteUser(){
		return deleteUser;
	}
	
	public SortedMap<Integer, MessageBody> getDeleteLocation(){
		return deleteLocation;
	}

	public static void popupMsgAndSave(int i, String userOrLocation) {
		MessageBody tmpMB = deleteUser.get(i);
		System.out.println("user with id "+tmpMB.getUserToBeDeletedID()+ "will be deleted. Do you accept?");
		AcceptOrNotDialog tmpDialog=new AcceptOrNotDialog();
		//if yes, if no
		
		
	}

	public static SortedMap<Integer, LocalDateTime> getCreatorToLastRelatedEventMap_notfinish(
			int IDofUserToBeDeleted) {
		// TODO Auto-generated method stub
		SortedMap<Integer, LocalDateTime> tmpmap=new TreeMap<Integer, LocalDateTime>();
		LocalDateTime tmptime = TimeMachine.getInstance().getTMTimestamp().toLocalDateTime().plusHours(6);
		tmpmap.put(IDofUserToBeDeleted, tmptime);
		return tmpmap;
	}
	
	
}

