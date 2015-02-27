package hkust.cse.calendar.apptstorage;//
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Save;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.unit.Location;

import java.util.HashMap;
import java.util.List;


public abstract class ApptStorage {

	public static HashMap<Integer,Appt> mAppts;		//a hashmap to save every thing to it, write to memory by the memory based storage implementation	
	public User defaultUser;	//a user object, now is single user mode without login
	public static int mAssignedApptID;	//a global appointment ID for each appointment record
	public static HashMap<Integer,Appt> gAppts;
	public static int gAssignedApptID;
	public ApptStorage() {	//default constructor
	}
	
	public abstract Save getsaveList();
	
	public abstract void setsaveList(Save list);

	public abstract Location[] getLocationList();
	
	public abstract void setLocationList(Location[] locations);
	
	public abstract void SaveAppt(Appt appt);	//abstract method to save an appointment record

	public abstract Appt[] RetrieveAppts(TimeSpan d);	//abstract method to retrieve an appointment record by a given timespan

	public abstract Appt[] RetrieveAppts(User entity, TimeSpan time);	//overloading abstract method to retrieve an appointment record by a given user object and timespan
	
	public abstract Appt RetrieveAppts(int joinApptID);					// overload method to retrieve appointment with the given joint appointment id

	public abstract void UpdateAppt(Appt appt);	//abstract method to update an appointment record

	public abstract void RemoveAppt(Appt appt);	//abstract method to remove an appointment record

	public abstract void LoadAppt(Appt appt);	//abstract method to remove an appointment record
	
	public abstract User getDefaultUser();		//abstract method to return the current user object
	
	public abstract void LoadApptFromXml();		//abstract method to load appointment from xml reocrd into hash map
	
	public abstract void SaveLocation(Location[] locations);
	
	public abstract void updateUser(User user);
	/*
	 * Add other methods if necessary
	 */
	public abstract void setDefaultUser(User user);

}
