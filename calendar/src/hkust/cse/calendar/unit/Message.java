package hkust.cse.calendar.unit;

import java.util.ArrayList;

public class Message {
	private int messageID;
	private int type; //1: del Location; 2: del user; 3: del appt; 4: recieve invite
	private ArrayList<Integer> userUIDList;
	private int editID;
	
	public Message()
	{
	}
	
	public Message(int b, ArrayList<Integer> c, int d)
	{
		type = b;
		userUIDList = c;
		editID = d;
	}
	
	public Message(int a, int b, ArrayList<Integer> c, int d)
	{
		messageID = a;
		type = b;
		userUIDList = c;
		editID = d;
	}
	
	public int getMessageID() {
		return messageID;
	}
	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public ArrayList<Integer> getUserUIDList() {
		return userUIDList;
	}
	public void setUserUIDList(ArrayList<Integer> userUIDList) {
		this.userUIDList = userUIDList;
	}
	public int getEditID() {
		return editID;
	}
	public void setEditID(int editID) {
		this.editID = editID;
	}
	public boolean removeUIDFromUUL(int uid)
	{
		if (userUIDList.contains(uid) == true)
		{
			userUIDList.remove(uid);
			return true;
		}
		else
		{
			return false;
		}
	}
}
