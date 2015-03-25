package hkust.cse.calendar.unit;

import java.util.LinkedList;

public class Appointment implements Comparable<Appointment> {
	private String title;
	private String description;
	private String location;
	private int startHour;
	private int startMin;
	private int startYear;
	private int startMonth;
	private int startDay;
	private int endHour;
	private int endMin;
	private int endYear;
	private int endMonth;
	private int endDay;
	private int reminder;
	private int reminderTime;
	private int reminderUnit;
	private LinkedList<String> attend;
	private LinkedList<String> reject;
	private LinkedList<String> waiting;
	private int id;
	private int jid;
	
	public Appointment(String t, String d, String l, int shr, int smin, int syr, int smon, int sday, int ehr, int emin, int eyr, int emon, int eday, int r, int rt, int ru, LinkedList<String> aal, LinkedList<String> ral, LinkedList<String> wal) {
		title = t;
		description = d;
		location = l;
		startHour = shr;
		startMin = smin;
		startYear = syr;
		startMonth = smon;
		startDay = sday;
		endHour = ehr;
		endMin = emin;
		endYear = eyr;
		endMonth = emon;
		endDay = eday;
		reminder = r;
		reminderTime = rt;
		reminderUnit = ru;
		attend = aal;
		reject = ral;
		waiting = wal;
	}
	
	public Appointment() {
		// TODO Auto-generated constructor stub
	}
	
	public void setAppointment(String t, String d, String l, int shr, int smin, int syr, int smon, int sday, int ehr, int emin, int eyr, int emon, int eday, int r, int rt, int ru, LinkedList<String> aal, LinkedList<String> ral, LinkedList<String> wal) {
		title = t;
		description = d;
		location = l;
		startHour = shr;
		startMin = smin;
		startYear = syr;
		startMonth = smon;
		startDay = sday;
		endHour = ehr;
		endMin = emin;
		endYear = eyr;
		endMonth = emon;
		endDay = eday;
		reminder = r;
		reminderTime = rt;
		reminderUnit = ru;
		attend = aal;
		reject = ral;
		waiting = wal;
	}

	public boolean setStartDateTime(int shr, int smin, int syr, int smon, int sday) {
		return false;
		//check if date is valid
		//check if time is valid
	}
	
	public boolean setEndDateTime(int ehr, int emin, int eyr, int emon, int eday) {
		return false;
		//check if date is valid
		//check if time is valid
	}
	
	public boolean waitingToAttend(String name)
	{
		return false;

	}
	
	public boolean waitingToReject(String name)
	{
		return false;

	}
	
	public boolean addWaiting(String name)
	{
		return false;

	}
	
	//get functions
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getLocation() {
		return location;
	}
	
	public int getStartHour() {
		return startHour;
	}
	
	public int getStartMin() {
		return startMin;
	}
	
	public int getStartYear() {
		return startYear;
	}
	
	public int getStartMonth() {
		return startMonth;
	}
	
	public int getStartDay() {
		return startDay;
	}

	public int getEndHour() {
		return endHour;
	}
	
	public int getEndMin() {
		return endMin;
	}
	
	public int getEndYear() {
		return endYear;
	}
	
	public int getEndMonth() {
		return endMonth;
	}
	
	public int getEndDay() {
		return endDay;
	}
	
	public int getReminder() {
		return reminder;
	}
	
	public int getReminderTime() {
		return reminderTime;
	}
	
	public int getReminderUnit() {
		return reminderUnit;
	}
	
	public LinkedList<String> getAttend() {
		return attend;
	}
	
	public LinkedList<String> getReject() {
		return reject;
	}
	
	public LinkedList<String> getWaiting() {
		return waiting;
	}
	
	public int getID() {
		return id;
	}
	
	public int getJID() {
		return jid;
	}
	
	public boolean isDateValid(int yr, int mon, int day) {
		return false;
	}
	
	public boolean isTimeValid(int hr, int min) {
		if (hr >= 0 && hr <= 24 && min >= 0 && min <= 60)
			return true;
		return false;
	}
	
	@Override
	public int compareTo(Appointment a) {
		// TODO Auto-generated method stub
		return 0;
	}
}
