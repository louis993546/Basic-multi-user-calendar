package hkust.cse.calendar.unit;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class MessageBody implements Serializable {
	int userToBeDeletedID;
	int locationID;
	int apptID;
	//LocalDateTime expireDateTime;
	Timestamp expireDateTimeTS;
	int receiverID;
	
	public int getUserToBeDeletedID() {
		return userToBeDeletedID;
	}

	public int getLocationToBeDeletedID() {
		// TODO Auto-generated method stub
		return locationID;
	}

	public int getLocationID() {
		return locationID;
	}

	public int getApptID() {
		return apptID;
	}

	

	public LocalDateTime getExpireDateTime() {
		return expireDateTimeTS.toLocalDateTime();
	}

	public int getReceiverID() {
		return receiverID;
	}

	@Override
	public String toString() {
		return "\nMessageBody [userToBeDeletedID=" + userToBeDeletedID
				+ ", \nlocationID=" + locationID + ", \napptID=" + apptID
				+ ", \nexpireDateTime="
				+ expireDateTimeTS.toLocalDateTime() + ", \nreceiverID=" + receiverID + "]";
	}


	
	public MessageBody(int userID, int locationID, int apptID,
			 LocalDateTime expireDateTime, int receiverID) {
		//super();
		this.userToBeDeletedID = userID;
		this.locationID = locationID;
		this.apptID = apptID;
		
		this.expireDateTimeTS = Timestamp.valueOf(expireDateTime);
		this.receiverID=receiverID;
		//UserResponse.Yes;
	}
	
}

