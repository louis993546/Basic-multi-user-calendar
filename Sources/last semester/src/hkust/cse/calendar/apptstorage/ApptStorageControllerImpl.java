package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.apptstorage.ApptStorageMemImpl;

import java.util.HashMap;
import java.util.LinkedList;
import java.sql.Timestamp;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Save;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.unit.Location;
/* This class is for managing the Appt Storage according to different actions */
public class ApptStorageControllerImpl {

	/* Remove the Appt from the storage */
	public final static int REMOVE = 1;

	/* Modify the Appt the storage */
	public final static int MODIFY = 2;

	/* Add a new Appt into the storage */
	public final static int NEW = 3;
	

	/* Load Appt into the storage */
	public final static int LOAD = 4;
	
	/*
	 * Add additional flags which you feel necessary
	 */
	
	/* The Appt storage */
	private ApptStorage mApptStorage;
	private LinkedList<String> invitedUserList=new LinkedList<String>();
	
    public HashMap getApptMap()
    {
        return mApptStorage.mAppts;
    }
	
	public Location[] getLocationList(){
		return mApptStorage.getLocationList();
	}

	public void setLocationList(Location[] locations){
		mApptStorage.setLocationList(locations);
	}
	
	/* Create a new object of ApptStorageControllerImpl from an existing storage of Appt */
	public ApptStorageControllerImpl(ApptStorage storage) {
		mApptStorage = storage;
	}

	/* Retrieve the Appt's in the storage for a specific user within the specific time span */
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		return mApptStorage.RetrieveAppts(entity, time);
	}

	// overload method to retrieve appointment with the given joint appointment id
	public Appt RetrieveAppts(int joinApptID) {
		return mApptStorage.RetrieveAppts(joinApptID);
	}
	
	/* Manage the Appt in the storage
	 * parameters: the Appt involved, the action to take on the Appt */
	public void ManageAppt(Appt appt, int action) {

		if (action == NEW) {				// Save the Appt into the storage if it is new and non-null
			if (appt == null)
				return;
			mApptStorage.SaveAppt(appt);
		} else if (action == MODIFY) {		// Update the Appt in the storage if it is modified and non-null
			if (appt == null)
				return;
			mApptStorage.UpdateAppt(appt);
		} else if (action == REMOVE) {		// Remove the Appt from the storage if it should be removed
			mApptStorage.RemoveAppt(appt);
		} else if (action == LOAD) {
			mApptStorage.LoadAppt(appt);
		}
	}
	
	public boolean getCurrentAppt(Timestamp t){
		return ApptStorageMemImpl.getCurrentApptFromHashmap(t);
	}

	/* Get the defaultUser of mApptStorage */
	public User getDefaultUser() {
		return mApptStorage.getDefaultUser();
	}

	// method used to load appointment from xml record into hash map
	public void LoadApptFromXml(){
		mApptStorage.LoadApptFromXml();
	}
	public void updateUser(User user){
		mApptStorage.updateUser(user);
		mApptStorage.setDefaultUser(user);
	};
	
	
	
	public void SaveLocationList(Location[] locations){
		mApptStorage.SaveLocation(locations);
	}
	
	public void setinvitedUserList(LinkedList<String> input){
		this.invitedUserList=input;
	}
	
	public LinkedList<String> getinvitedUserList(){
		return this.invitedUserList;
	}
	
	public Save getSave(){
		return mApptStorage.getsaveList();
	}

	public void setSave(Save s) {
		mApptStorage.setsaveList(s);
		
	}
}
