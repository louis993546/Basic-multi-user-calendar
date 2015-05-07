package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.gui.AcceptOrNotDialog;
import hkust.cse.calendar.unit.MessageBody;

import java.util.SortedMap;
import java.util.TreeMap;

public class MessageStorage {
	static SortedMap<Integer, MessageBody> deleteUser = new TreeMap<Integer, MessageBody>();
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
	
	
}

