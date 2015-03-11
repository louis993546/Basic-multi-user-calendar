package hkust.cse.calendar.unit;

import java.io.Serializable;

public abstract class User implements Serializable {

	protected String mPassword;				// User password
	protected String mID;						// User id
	protected String mFirstName;
	protected String mLastName;
	protected String mEmail;					// User email
	protected TimeSpan mBirthday;				// User Birthday
	protected String mRole;
	
	public String getRole() {
		return mRole;
	}
	
	public void setRole(String role) {
		mRole = role;
	}

	// Getter of the user id
	public String ID() {		
		return mID;
	}

	// Constructor of class 'User' which set up the user id and password
	public User(String id, String pass) {
		mID = id;
		mPassword = pass;
		mFirstName = "";
		mLastName = "";
		mEmail = "";
		mBirthday = null;
	}

	// Getter of the user password
	public String Password() {
		return mPassword;
	}

	// Setter of the user password
	public void Password(String pass) {
		mPassword = pass;
	}
	
	public void setEmail(String email) {
		mEmail = email;
	}
	
	public String getEmail() {
		return mEmail;
	}
	
	public void setBirthday(TimeSpan bDay) {
		mBirthday = bDay;
	}
	
	public TimeSpan getBirthday() {
		return mBirthday;
	}
	
	public void setName(String firstname, String lastnamem) {
		mFirstName = firstname;
		mLastName = lastnamem;
	}
	
	public String getName() {
		return mFirstName + ' ' + mLastName;
	}
	
	public void setPassword(String mPassword) {
		this.mPassword = mPassword;
	}

	public String getFirstName() {
		return mFirstName;
	}
	
	public String getLastName() {
		return mLastName;
	}
	
	public String toString() {
		return getName() + " (" + ID() + ")";
	}
	
	public abstract boolean isAdmin();
}
