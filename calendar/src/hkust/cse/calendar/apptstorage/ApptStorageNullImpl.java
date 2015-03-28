package hkust.cse.calendar.apptstorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import hkust.cse.calendar.unit.Appointment;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

public class ApptStorageNullImpl extends ApptStorage {
//TODO access everything through adb
	
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
		return adb.getApptByTime(d);
	}

	@Override
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		// TODO fix this returveAppts with user and time
		//retrieve an appointment record by a given user object and timespan
//		temp implementation. It just gets everything
//		because the SQL part is not done yet
		System.out.println(time.StartTime().toString());
		System.out.println(time.EndTime().toString());
				return adb.getApptByTime(time);
//
//		ArrayList<Appointment> dataALA = adb.getAppointmentList();
//		ArrayList<Appt> dataLLA = new ArrayList<Appt>();
//		mAppts = new HashMap<Integer, Appt>();
//		if (dataALA.size()!=0)
//		{
//			for (Appointment a:dataALA)
//			{
//				Appt tempAppt = new Appt(a);
//				dataLLA.add(tempAppt);
//			}
//		}
//		Appt[] tempArray = new Appt[dataLLA.size()];
//		for (int i = 0; i<dataLLA.size(); i++)
//		{
//			tempArray[i] = dataLLA.get(i);
//		}
//		return tempArray;
	}

	@Override
	public Appt RetrieveAppts(int joinApptID) {
		return adb.getApptByJID(joinApptID);
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
		//there is a mAppts<int, appt> in the super class of this
		ArrayList<Appointment> dataALA = adb.getAppointmentList();
		if (dataALA.size()!=0)
		{
			LinkedList<Appt> dataLLA = new LinkedList<Appt>();
			for (Appointment a:dataALA)
			{
				Appt tempAppt = new Appt(a);
				dataLLA.add(tempAppt);
			}
			if (dataLLA.size()!=0)
			{
				for (Appt b:dataLLA)
				{
					mAppts = new HashMap<Integer, Appt>();
					mAppts.put(b.getID(), b);
				}
			}
		}
	}

}
