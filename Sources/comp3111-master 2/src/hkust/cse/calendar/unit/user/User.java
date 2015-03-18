package hkust.cse.calendar.unit.user;

import hkust.cse.calendar.unit.Appt;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

	private String mPassword;				// User password
	private String mID;						// User id
	private String mEmail;
	private String mFirstName;
	private String mLastName;
	private String mPhoneNum;
	private ArrayList<Appt> mBookmarks;
	protected Boolean isAdmin;
	
	public String getPhoneNum() {
		return mPhoneNum;
	}

	public void setPhoneNum(String mPhoneNum) {
		this.mPhoneNum = mPhoneNum;
	}

	// Getter of the user id
	public String ID() {		
		return mID;
	}

	// Constructor of class 'User' which set up the user id and password
	public User(String id, String pass) {
		mID = id;
		mPassword = pass;
		isAdmin = false;
		mFirstName = "";
		mLastName = "";
		mEmail = "";
		mPhoneNum = "";
		mBookmarks = new ArrayList<Appt>();
	}

	public String getFirstName() {
		return mFirstName;
	}

	public void setFirstName(String mFirstName) {
		this.mFirstName = mFirstName;
	}

	public String getLastName() {
		return mLastName;
	}

	public void setLastName(String mLastName) {
		this.mLastName = mLastName;
	}

	// Another getter of the user id
	public String toString() {
		return ID();
	}

	// Getter of the user password
	public String Password() {
		return mPassword;
	}

	// Setter of the user password
	public void Password(String pass) {
		mPassword = pass;
	}
	
	// Getter of the email
	public String getEmail() {
		return mEmail;
	}
	
	// Setter of user email
	public void setEmail(String email) {
		mEmail = email;
	}
	
	public Boolean IsAdmin() {
		return this.isAdmin;
	}
	
	public ArrayList<Appt> getBookmark() {
		return mBookmarks;
	}
	
	public void AddBookmark(Appt appt) {
		Boolean contain = false;
		for(Appt bookmark : mBookmarks)
			if(bookmark.getID() == appt.getID()) contain = true;
		if(!contain) mBookmarks.add(appt);
	}
	
	public void RemoveBookmark(Appt appt) {
		if(mBookmarks.contains(appt)) mBookmarks.remove(appt);
	}
}
