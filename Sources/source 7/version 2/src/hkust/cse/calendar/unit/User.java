package hkust.cse.calendar.unit;

import java.io.Serializable;
import hkust.cse.calendar.unit.Location;

public abstract class User implements Serializable {
	
	
	/*added by me*/
	/*compulsory fields*/
	protected String firstName;				// User’s first Name
	protected String lastName;				// User’s last Name
	protected String email;					// User’s email
	
	
	/*optional field*/
	protected String otherInformation;		// User’s extra inforamtion which is not required 
	/*up to here*/
	
	protected String mPassword;				// User password
	protected String mID;						// User id
	
	protected Location[] userLocations;       // a list of specific user’s locations   
	
	protected boolean isAdmin; 				// a boolean variable which shows whether the user is an administrator
	
	
	// Setter of the user id
	public abstract void setID(String newID);
	
	
	// Getter of the user id
	public abstract String ID();

	// Constructor of class 'User' which set up the user id and password
	public User() {}// default constructor
		/*mID = id;
		mPassword = pass;
		firstName = null;
		lastName = null;
		email = null;
		otherInformation = null;
		isAdmin = false;*/		
	


	// Another getter of the user id
	public abstract String toString();

	// Getter of the user password
	public abstract String Password();

	// Setter of the user password
	public abstract void  Password(String pass);
	
	// Getter of the firstName
	public abstract String getFirstName();
	
	// Setter of the firstName
	public abstract void setFirstName(String name);
	
	
	// Getter of the lastName
	
	public abstract String getLastName();
	
	
	// Setter of the lastName
	public abstract void setLastName(String surname);
	
	// Getter of the email address
	public abstract String getEmail();
	
	// Setter of the email address
	public abstract void setEmail(String mail);
	
	// Setter of the extra information
	public abstract void setOtherInfmormation(String extra);
	
	// Getter of the extra information
	public abstract String getOtherInformation();
	
	
	// Getter of the admin status
	public abstract boolean getAdminStatus();
	
	
	// Getter of the location list 	
	public abstract Location[] getUserLocations();
	
	
	// Setter of the user’s locations
	public abstract void setUserLocations(Location[] locations);
}
