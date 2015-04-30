package hkust.cse.calendar.unit;

import java.io.Serializable;

public class User implements Serializable {

	private String mPassword;				// User password
	private String mID;						// User id
	private int mAdmin;

	// Getter of the user id
	public String ID() {		
		return mID;
	}

	// Constructor of class 'User' which set up the user id and password
	public User(String id, String pass) {
		mID = id;
		mPassword = pass;
	}

	// Constructor of class 'User' which set up the user id, password and admin permission
	public User(String id, String pass, int admin) {
		mID = id;
		mPassword = pass;
		mAdmin = admin;
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

	// Setter of user id

	public void ID(String id)
	{
		mID = id;
	}

	// Setter of user admin permission
	public void Admin(int a)
	{
		mAdmin = a;
	}

	// Getter of user admin permission
	public int Admin()
	{
		return mAdmin;
	}
}
