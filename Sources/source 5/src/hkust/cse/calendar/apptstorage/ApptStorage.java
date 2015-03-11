package hkust.cse.calendar.apptstorage;//

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeMachine;
import hkust.cse.calendar.unit.TimeSpan;

import java.util.HashMap;

import com.thoughtworks.xstream.XStream;

import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.user.User;
import hkust.cse.calendar.unit.user.UserManagement;


public abstract class ApptStorage {
	public HashMap<Integer, Appt> mAppts;	//a hashmap to save every thing to it, write to memory by the memory based storage implementation
	public User defaultUser;	//a user object, now is single user mode without login
	public int mAssignedApptID;	//a global appointment ID for each appointment record
	public Location[] _locations;
	public XStream xstream;
	protected TimeMachine timeMachine;
	public ApptStorage() {	//default constructor
}
	
	public void setTimeMachine(TimeMachine machine) {
		this.timeMachine = machine;
	}
	
	public abstract Location[] getLocationList();
	
	public abstract void setLocationList(Location[] locations);

	public abstract void SaveAppt(Appt appt);	//abstract method to save an appointment record

	public abstract Appt[] RetrieveAppts(TimeSpan d);	//abstract method to retrieve an appointment record by a given timespan
	
	public abstract Appt[] RetrieveAppts(TimeSpan d, int f);

	public abstract Appt[] RetrieveAppts(User entity, TimeSpan time);	//overloading abstract method to retrieve an appointment record by a given user object and timespan
	
	public abstract Appt RetrieveAppts(int joinApptID);					// overload method to retrieve appointment with the given joint appointment id

	public abstract Appt[] RetrieveAppts(Location location, TimeSpan time);
	
	public abstract void UpdateAppt(Appt appt);	//abstract method to update an appointment record

	public abstract void RemoveAppt(Appt appt);	//abstract method to remove an appointment record
	
	public abstract User getDefaultUser();		//abstract method to return the current user object

	public abstract void LoadApptFromXml();	//abstract method to load appointment from xml record into hash map
	
	public abstract void PutApptToXml();	//abstract method to put appointment from hash map into xml record
	
	public abstract void LoadLocFromXml();
	
	public abstract void PutLocToXml();
	
	public abstract UserManagement LoadUserFromXml();
	
	public abstract void PutUserToXml();
	
	/*
	 * Add other methods if necessary
	 */

}
