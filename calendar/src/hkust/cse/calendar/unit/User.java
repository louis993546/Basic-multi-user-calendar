package hkust.cse.calendar.unit;

import java.io.Serializable;

public class User implements Serializable {

	private int mID;						// Unique user ID
	private String mPassword;				// User password
	private String mEmail;						// User email
	private String mFirstName;
	private String mLastName;
	private int mAdmin;

	// Getter of the user id
	public String ID() {		
		return mEmail;
	}

	// Constructor of class 'User' which set up the user id and password
	public User(String email, String pass) {
		mEmail = email;
		mPassword = pass;
	}

	// Constructor of class 'User' which set up the user id, password and admin permission
	public User(String email, String pass, int admin) {
		this(email, pass);
		mAdmin = admin;
	}
	
	public User(String email, String pass, int admin, String fn, String ln)
	{
		this(email, pass, admin);
		mFirstName = fn;
		mLastName = ln;
		
	}
	
	public User(int id, String email, String pass, int admin, String fn, String ln)
	{
		this(email, pass, admin, fn, ln);
		mID = id;
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

	public void Email(String email)
	{
		mEmail = email;
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
		return mID;
	}
}
