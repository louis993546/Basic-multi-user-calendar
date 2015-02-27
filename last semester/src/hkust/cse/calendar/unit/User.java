package hkust.cse.calendar.unit;

import java.io.Serializable;

public class User implements Serializable {

	private String Password;				// User password
	private String ID;						// User id
	private String Fullname;
	private String Address;
	private int admin;						//admin=1,normal=0
	//private int mNotice;					//deleted use=1,on groupevent waitlist=2,location had removed=3
	private int deleted;
	// Getter of the user id
	public User(){}
	// Constructor of class 'User' which set up the user id and password
	public User(String id, String pass) {
		ID = id;
		Password = pass;
	}

	// Another getter of the user id
	public String toString() {
		return getID();
	}

	public String getPassword() {
		return Password;
	}


	public void setPassword(String password) {
		Password = password;
	}


	public String getID() {
		return ID;
	}


	public void setID(String iD) {
		ID = iD;
	}


	public String getFullname() {
		return Fullname;
	}


	public void setFullname(String fullname) {
		Fullname = fullname;
	}


	public String getAddress() {
		return Address;
	}


	public void setAddress(String address) {
		Address = address;
	}


	public int getAdmin() {
		return admin;
	}


	public void setAdmin(int admin) {
		this.admin = admin;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}


	/*public int getNotice() {
		return Notice;
	}


	public void setNotice(int notice) {
		Notice = notice;
	}*/
}
