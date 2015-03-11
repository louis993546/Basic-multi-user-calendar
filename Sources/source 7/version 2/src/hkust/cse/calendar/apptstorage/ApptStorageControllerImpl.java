package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.Appt;
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
	
	/*
	 * Add additional flags which you feel necessary
	 */
	
	/* The Appt storage */
	private ApptStorage mApptStorage;
	private boolean timeSpanCheck = false;

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
			timeSpanCheck = mApptStorage.SaveAppt(appt);
		} else if (action == MODIFY) {		// Update the Appt in the storage if it is modified and non-null
			if (appt == null)
				return;
			mApptStorage.UpdateAppt(appt);
		} else if (action == REMOVE) {		// Remove the Appt from the storage if it should be removed
			mApptStorage.RemoveAppt(appt);
		} 
	}

	/* Get the defaultUser of mApptStorage */
	public User getDefaultUser() {
		return mApptStorage.getDefaultUser();
	}

	// method used to load appointment from xml record into hash map
	public void LoadApptFromXml(){
		mApptStorage.LoadApptFromXml();
	}
	
	// method used to save appointments to a XML file
	public void saveApptToXml(){
		mApptStorage.saveApptToXml();
	}
	
	public Appt[] RetrieveAppts(TimeSpan time) {
		return mApptStorage.RetrieveAppts(time);
	}
	
	// added methods by me
	/*this method returns to the GUI the list*/
	public Location[] getLocationList(){
		return mApptStorage.getLocationList();
	}
	
	/*this method sets the locations from the GUI*/
	public void setLocationList(Location[] locations){
		mApptStorage.setLocationList(locations);
	}
	//up to here
	
	public boolean getStateOfSpan(){
		return timeSpanCheck;
	}
	
	public ApptStorage getmAppStorage(){
		return mApptStorage;
	}
}
