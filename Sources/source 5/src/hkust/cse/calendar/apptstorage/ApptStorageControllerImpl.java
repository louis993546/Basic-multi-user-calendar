package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeMachine;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.user.User;
import hkust.cse.calendar.unit.user.UserManagement;

/* This class is for managing the Appt Storage according to different actions */
public class ApptStorageControllerImpl {

	/* Remove the Appt from the storage */
	public final static int REMOVE = 1;

	/* Modify the Appt the storage */
	public final static int MODIFY = 2;

	/* Add a new Appt into the storage */
	public final static int NEW = 3;
	
	/* Transfer the ownership of a Appt */
	public final static int TRANSFER = 4;
	
	/*
	 * Add additional flags which you feel necessary
	 */
	
	/* The Appt storage */
	private ApptStorage mApptStorage;

	/* Create a new object of ApptStorageControllerImpl from an existing storage of Appt */
	public ApptStorageControllerImpl(ApptStorage storage) {
		mApptStorage = storage;
	}
	
	/*public void setTimeMachine(TimeMachine machine) {
		mApptStorage.setTimeMachine(machine);
	}*/
	
	public Appt[] RetrieveAppts(TimeSpan time) {
		return mApptStorage.RetrieveAppts(time);
	}
	
	public Appt[] RetrieveAppts(TimeSpan time, int frequency) {
		return mApptStorage.RetrieveAppts(time, frequency);
	}

	/* Retrieve the Appt's in the storage for a specific user within the specific time span */
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		return mApptStorage.RetrieveAppts(entity, time);
	}

	// overload method to retrieve appointment with the given joint appointment id
	public Appt RetrieveAppts(int joinApptID) {
		return mApptStorage.RetrieveAppts(joinApptID);
	}
	
	public Appt[] RetrieveAppt(Location location, TimeSpan time) {
		return mApptStorage.RetrieveAppts(location, time);
	}
	
	/* Manage the Appt in the storage
	 * parameters: the Appt involved, the action to take on the Appt */
	public void ManageAppt(Appt appt, int action) {

		if (action == NEW) {				// Save the Appt into the storage if it is new and non-null
			if (appt == null)
				return;
			mApptStorage.SaveAppt(appt);
		} else if (action == MODIFY || action == TRANSFER) {		// Update the Appt in the storage if it is modified and non-null
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
	public void LoadApptFromXml() {
		mApptStorage.LoadApptFromXml();
	}
	
	public void PutApptToXml() {
		mApptStorage.PutApptToXml();
	}
	
	public void LoadLocFromXml() {
		mApptStorage.LoadLocFromXml();
	}
	
	public void PutLocToXml() {
		mApptStorage.PutLocToXml();
	}
	
	public Location[] getLocationList() {
		return mApptStorage.getLocationList();
	}
	
	public void setLocationList(Location[] locations) {
		mApptStorage.setLocationList(locations);
	}
	
	public UserManagement LoadUserFromXml() {
		return mApptStorage.LoadUserFromXml();
	}
	
	public void PutUserToXml() {
		mApptStorage.PutUserToXml();
	}
}
