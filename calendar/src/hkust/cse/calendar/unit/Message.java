package hkust.cse.calendar.unit;

public class Message {
	private int messageID;
	private int type;
	private String userUIDList;
	private int editID;
	
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
	public String getUserUIDList() {
		return userUIDList;
	}
	public void setUserUIDList(String userUIDList) {
		this.userUIDList = userUIDList;
	}
	public int getEditID() {
		return editID;
	}
	public void setEditID(int editID) {
		this.editID = editID;
	}
	
}
