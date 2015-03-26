package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

public class ApptStorageNullImpl extends ApptStorage {

	private User defaultUser = null;
	private ApptDB adb = new ApptDB();
	private LocationDB ldb = new LocationDB();
	private UserDB usb = new UserDB();
	
	public ApptStorageNullImpl( User user )
	{
		defaultUser = user;
	}
	
	@Override
	public void SaveAppt(Appt appt) {
		//Save appointment
		adb.addAppt(appt.getAppointment());
	}

	@Override
	public Appt[] RetrieveAppts(TimeSpan d) {
		// TODO Auto-generated method stub
		//retrieve an appointment record by a given timespan
		
		//use toArray
		return null;
	}

	@Override
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		// TODO Auto-generated method stub
		//retrieve an appointment record by a given user object and timespan
		return null;
	}

	@Override
	public Appt RetrieveAppts(int joinApptID) {
		// TODO Auto-generated method stub
		//retrieve appointment with the given joint appointment id
		return null;
	}

	@Override
	public void UpdateAppt(Appt appt) {
		// TODO Auto-generated method stub
		//update appointment record

	}

	@Override
	public void RemoveAppt(Appt appt) {
		//remove appointment record
		adb.deleteAppt(appt.getAppointment().getID());
	}

	@Override
	public User getDefaultUser() {
		// TODO Auto-generated method stub
		return defaultUser; //return current user object
	}

	@Override
	public void LoadApptFromXml() {
		// TODO Auto-generated method stub
		//load appointment from xml record into hash map
	}

}
