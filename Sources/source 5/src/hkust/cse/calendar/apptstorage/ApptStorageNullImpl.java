package hkust.cse.calendar.apptstorage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.user.User;
import hkust.cse.calendar.unit.user.UserManagement;

public class ApptStorageNullImpl extends ApptStorage {

	private User defaultUser = null;
	
	Location[] _locations;
	
	@Override
	public Location[] getLocationList(){
		return _locations;
	}
	
	@Override
	public void setLocationList(Location[] locations){
		_locations = locations;
	}
	
	
	public ApptStorageNullImpl( User user )
	{
		defaultUser = user;
	}
	
	@Override
	public void SaveAppt(Appt appt) {
		// TODO Auto-generated method stub

	}

	@Override
	public Appt[] RetrieveAppts(TimeSpan d) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Appt[] RetrieveAppts(TimeSpan d, int f) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Appt RetrieveAppts(int joinApptID) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Appt[] RetrieveAppts(Location location, TimeSpan time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void UpdateAppt(Appt appt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void RemoveAppt(Appt appt) {
		// TODO Auto-generated method stub

	}

	@Override
	public User getDefaultUser() {
		// TODO Auto-generated method stub
		return defaultUser;
	}

	@Override
	public void LoadApptFromXml() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void PutApptToXml() {
		//TODO Auto-generated method stub
	}
	
	@Override
	public void LoadLocFromXml() {
		// TODO Auto-generated method stub
	}
	
	public void PutLocToXml() {
		// TODO Auto-generated method stub
	}

	@Override
	public UserManagement LoadUserFromXml() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void PutUserToXml() {
		// TODO Auto-generated method stub
	}
}
