package hkust.cse.calendar.apptstorage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import hkust.cse.calendar.unit.Appointment;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Reminder;
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
		LoadApptFromXml();
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
		LoadApptFromXml();
	}

	@Override
	public void RemoveAppt(Appt appt) {
		//remove appointment record
		adb.deleteAppt(appt.getAppointment().getID());
		LoadApptFromXml();
	}

	@Override
	public User getDefaultUser() {
		return defaultUser; //return current user object
	}

	@Override
	public void LoadApptFromXml() {
		//load appointment from xml record into hash map
		ArrayList<Appointment> dataALA = adb.getAppointmentList();
		if (true)//dataALA.size()!=0)  ,this is because when deleting the last appointment, need to update the reminder list
		{
			LinkedList<Appt> dataLLA = new LinkedList<Appt>();
			for (Appointment a:dataALA)
			{
				Appt tempAppt = new Appt(a);
				dataLLA.add(tempAppt);
			}
			mAppts = new HashMap<Integer, Appt>();
			if (dataLLA.size()!=0)
			{
				for (Appt b:dataLLA)
				{
					mAppts.put(b.getID(), b);
				}
			}
			//generate reminder table and store it to reminderAL
			reminderAL = new ArrayList<Reminder>();
			for (Appointment a:dataALA)
			{
				if (a.getReminder() == 1) //there is a reminder
				{
					//TODO calculate the actual time
					// {"Minute(s)", "Hour(s)", "Day(s)", "Week(s)"
					// 1               2          3          4     
					int msToMinus = 0;
					Timestamp tempTS = a.getTimeSpan().StartTime();
					//get start time of a
					//get reminder unit
					switch (a.getReminderUnit())
					{
					case 1:
						msToMinus = a.getReminderTime()*60*1000; 
						break;
					case 2:
						msToMinus = a.getReminderTime()*60*60*1000;
						break;
					case 3:
						msToMinus = a.getReminderTime()*60*60*24*1000;
						break;
					case 4:
						msToMinus = a.getReminderTime()*60*60*24*7*1000;
						break;
					default:
					}
					//set new timestamp
					tempTS.setTime(tempTS.getTime()- msToMinus);
					Reminder newReminder = new Reminder(a.getTitle(), tempTS);
					reminderAL.add(newReminder);
				}
			}
		}
	}

}
