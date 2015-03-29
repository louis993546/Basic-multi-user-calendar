package hkust.cse.calendar.unit;

import java.sql.Timestamp;
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
	private boolean isJoint;
	private User creater;

	public Appointment(String t, String d, String l, int shr, int smin, int syr, int smon, int sday, int ehr, int emin, int eyr, int emon, int eday, int r, int rt, int ru, LinkedList<String> aal, LinkedList<String> ral, LinkedList<String> wal, int i) {
		//TODO create unique id for each appointment upon creation
		//TODO initiate creater
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
		id = i;
	}

	public Appointment() {
		//TODO timespan has not been implemented
		title = "Untitled";
		description = "";
		location = "";
		id = 0;
		jid = -1;
		attend = new LinkedList<String>();
		reject = new LinkedList<String>();
		waiting = new LinkedList<String>();
		//time
	}

	public boolean setTitle(String t)
	{
		title = t;
		return true;
	}

	public boolean setDescription(String d)
	{
		description = d;
		return true;
	}

	public boolean setLocation(String l)
	{
		location = l;
		return true;
	}

	public boolean setReminder(int r, int rt, int ru)
	{
		reminder = r;
		reminderTime = rt;
		reminderUnit = ru;
		return true;
	}

	public boolean setStartDateTime(int shr, int smin, int syr, int smon, int sday) {
		//TODO check if start date is valid
		//Not sure if this is actually necessary
		//check getValidDate from AppScheduler
		return false;
		//check if date is valid
		//leap year
		//if day exist in that month
		//larger than 0
		//check if time is valid
	}

	public boolean setEndDateTime(int ehr, int emin, int eyr, int emon, int eday) {
		//TODO check if end date is valid
		//Not sure if this is actually necessary
		//check getValidDate from AppScheduler
		return false;
		//check if date is valid
		//check if time is valid
	}

	@SuppressWarnings("deprecation")
	public boolean endAfterStart(int shr, int smin, int syr, int smon, int sday, int ehr, int emin, int eyr, int emon, int eday)
	{
		Timestamp st = new Timestamp(syr-1900, smon, sday, shr, smin, 0, 0);
		Timestamp et = new Timestamp(eyr-1900, emon, eday, ehr, emin, 0, 0);
		if (st.compareTo(et) < 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean setID(int i)
	{
		id = i;
		return true;
	}

	public boolean setJID(int j)
	{
		jid = j;
		return true;
	}

	public boolean deleteFromAttend(String name)
	{
		if (attend.remove(name) == true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean deleteFromReject(String name)
	{
		if (reject.remove(name) == true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean deleteFromWaiting(String name)
	{
		if (waiting.remove(name) == true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean waitingToAttend(String name)
	{
		if (waiting.remove(name) == true)
		{
			attend.add(name);
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean waitingToReject(String name)
	{
		if (waiting.remove(name) == true)
		{
			reject.add(name);
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean addToAttend(String name)
	{
		attend.add(name);
		return true;
	}

	public boolean addToReject(String name)
	{
		reject.add(name);
		return true;
	}

	public boolean addWaiting(String name)
	{
		waiting.add(name);
		return false;
	}

	public boolean setIsJoint(boolean i)
	{
		isJoint = i;
		return true;
	}

	public void initiateAttend()
	{
		attend = new LinkedList<String>();
	}

	public void initiateReject()
	{
		reject = new LinkedList<String>();
	}

	public void initiateWaiting()
	{
		waiting = new LinkedList<String>();
	}

	public void setAttend(LinkedList<String> a)
	{
		attend = a;
	}

	public void setReject(LinkedList<String> r)
	{
		reject = r;
	}

	public void setWaiting(LinkedList<String> w)
	{
		waiting = w;
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

	public boolean getIsJoint()
	{
		return isJoint;
	}

	public boolean isDateValid(int yr, int mon, int day) {
		return false;
	}

	public boolean isTimeValid(int hr, int min) {
		if (hr >= 0 && hr <= 24 && min >= 0 && min <= 60)
			return true;
		return false;
	}

	@SuppressWarnings("deprecation")
	public TimeSpan getTimeSpan()
	{
		Timestamp st = new Timestamp(startYear-1900, startMonth, startDay, startHour, startMin, 0, 0);
		Timestamp et = new Timestamp(endYear-1900, endMonth, endDay, endHour, endMin, 0, 0);
		TimeSpan a = new TimeSpan(st, et);
		return a;
	}

	@Override
	public int compareTo(Appointment a) {
		//TODO comparing appointments
		return 0;
	}
}
