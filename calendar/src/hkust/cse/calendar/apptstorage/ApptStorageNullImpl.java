package hkust.cse.calendar.apptstorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import hkust.cse.calendar.unit.Appointment;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

public class ApptStorageNullImpl extends ApptStorage {
	
	private User defaultUser = null;
	private ApptDB adb = new ApptDB();
	
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
		// TODO this only check time because phrase 1
		//retrieve an appointment record by a given user object and timespan
		return adb.getApptByTime(time);
	}

	@Override
	public Appt RetrieveAppts(int joinApptID) {
		return adb.getApptByJID(joinApptID);
	}

	@Override
	public void UpdateAppt(Appt appt) {
		adb.modifyAppt(appt.getID(), appt.getAppointment());

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
		//load appointment from xml record into hash map
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
