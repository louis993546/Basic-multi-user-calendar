package hkust.cse.calendar.unit;

import java.io.Serializable;

public class User implements Serializable {

	private int uID;						// Unique user ID
	private String mPassword;				// User password
	private String mID;						// User email
	private String mFirstName;
	private String mLastName;
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
		this(id, pass);
		mAdmin = admin;
	}
	
	public User(String id, String pass, int admin, String fn, String ln)
	{
		this(id, pass, admin);
		mFirstName = fn;
		mLastName = ln;
	}
	
	public User(int u, String id, String pass, int admin, String fn, String ln)
	{
		this(id, pass, admin, fn, ln);
		uID = u;
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
	
	public void FirstName(String fn)
	{
		mFirstName = fn;
	}
	
	public String FirstName()
	{
		return mFirstName;
	}
	
	public void LastName(String ln)
	{
		mLastName = ln;
	}
	
	public String LastName()
	{
		return mLastName;
	}
	
	public int UID()
	{
		return uID;
	}
}
