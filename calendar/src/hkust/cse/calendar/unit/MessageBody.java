package hkust.cse.calendar.unit;

import java.time.LocalDateTime;

public class MessageBody {
	int userToBeDeletedID;
	int locationID;
	int apptID;
	UserResponse response;
	LocalDateTime expireDateTime;
	int receiverID;
	
	public int getUserToBeDeletedID() {
		return userToBeDeletedID;
	}

	public int getLocationID() {
		return locationID;
	}

	public int getApptID() {
		return apptID;
	}

	public UserResponse getResponse() {
		return response;
	}

	public LocalDateTime getExpireDateTime() {
		return expireDateTime;
	}

	public int getReceiverID() {
		return receiverID;
	}

	@Override
	public String toString() {
		return "\nMessageBody [userToBeDeletedID=" + userToBeDeletedID
				+ ", \nlocationID=" + locationID + ", \napptID=" + apptID
				+ ", \nresponse=" + response + ", \nexpireDateTime="
				+ expireDateTime + ", \nreceiverID=" + receiverID + "]";
	}

	public enum UserResponse {
		Yes, No, NotYet
	}
	
	public MessageBody(int userID, int locationID, int apptID,
			UserResponse response, LocalDateTime expireDateTime, int receiverID) {
		//super();
		this.userToBeDeletedID = userID;
		this.locationID = locationID;
		this.apptID = apptID;
		this.response = response;
		this.expireDateTime = expireDateTime;
		this.receiverID=receiverID;
		//UserResponse.Yes;
	}
	
}

