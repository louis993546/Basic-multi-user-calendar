/**
* This class was created in an attempt to fit more data into each appointment without messing around with
* how the existing skelton code access each Appt.
* most of the whole program does not access Appointment object directly, but through Appt
*
* @deprecated a lot of getter for Timestamp are deprecated, and it is still being used by this program
* 			  through our the whole program
* @see Appt.java, ApptDB.java
* @version 1.0
*/

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
	private int startMonth;//1~12
	private int startDay;
	private int endHour;
	private int endMin;
	private int endYear;
	private int endMonth;//1~12
	private int endDay;
	private int reminder;
	private int reminderTime;
	private int reminderUnit;
	private LinkedList<Integer> going;
//	private LinkedList<String> reject;
	private LinkedList<Integer> waiting;
	private int id;
	private int jid;
	private boolean isJoint;
	private int createrID;

	public Appointment(String t, String d, String l, int shr, int smin, int syr, int smon, int sday, int ehr, int emin, int eyr, int emon, int eday, int r, int rt, int ru, LinkedList<Integer> aal,  LinkedList<Integer> wal, int i, int uid) 
	{
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
		going = aal;
		waiting = wal;
		id = i;
		createrID = uid;
	}
	
	public Appointment() {
		title = "Untitled";
		description = "";
		location = "";
		id = 0;
		jid = -1;
		going = new LinkedList<Integer>();
//		reject = new LinkedList<String>();
		waiting = new LinkedList<Integer>();
		//TODO not sure if time needs to be initiate
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

	public void setStartEndYear(int yr)
	{
		//Warning: this does not do any checking
		startYear = yr;
		endYear = yr;
	}
	
	public void setStartEndMonth(int m)
	{
		//Warning: this does not do any checking
		startMonth = m;
		endMonth = m;
	}
	
	public void setStartEndDay(int d)
	{
		//Warning: this does not do any checking
		startDay = d;
		endDay = d;
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
		if (going.remove(name) == true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

//	public boolean deleteFromReject(String name)
//	{
//		if (reject.remove(name) == true)
//		{
//			return true;
//		}
//		else
//		{
//			return false;
//		}
//	}

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

//	public boolean waitingToAttend(int uid)
//	{
//		//TODO get UID first
//		if (waiting.remove(uid) == true)
//		{
//			going.add(uid);
//			return true;
//		}
//		else
//		{
//			return false;
//		}
//	}

//	public boolean waitingToReject(String name)
//	{
//		if (waiting.remove(name) == true)
//		{
//			reject.add(name);
//			return true;
//		}
//		else
//		{
//			return false;
//		}
//	}

	public boolean addToAttend(int uid)
	{
		going.add(uid);
		return true;
	}

//	public boolean addToReject(String name)
//	{
//		reject.add(name);
//		return true;
//	}

	public boolean addWaiting(int uid)
	{
		waiting.add(uid);
		return false;
	}

	public boolean setIsJoint(boolean i)
	{
		isJoint = i;
		return true;
	}

	public void initiateAttend()
	{
		going = new LinkedList<Integer>();
	}

//	public void initiateReject()
//	{
//		reject = new LinkedList<String>();
//	}

	public void initiateWaiting()
	{
		waiting = new LinkedList<Integer>();
	}

	public void setAttend(LinkedList<Integer> a)
	{
		going = a;
	}

//	public void setReject(LinkedList<String> r)
//	{
//		reject = r;
//	}

	public void setWaiting(LinkedList<Integer> w)
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

	public LinkedList<Integer> getGoingList() {
		System.out.println("going");
		return going;
	}

//	public LinkedList<String> getReject() {
//		return reject;
//	}

	public LinkedList<Integer> getWaitingList() {
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
		//TODO Check if date is valid
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
		Timestamp st = new Timestamp(startYear-1900, startMonth-1, startDay, startHour, startMin, 0, 0);
		Timestamp et = new Timestamp(endYear-1900, endMonth-1, endDay, endHour, endMin, 0, 0);//month of timestamp is 0-based, ie. 0~11
		TimeSpan a = new TimeSpan(st, et);
		return a;
	}

	@Override
	public int compareTo(Appointment a) {
		//TODO comparing appointments
		return 0;
	}

	public void setEndDateTime(int hours, int minutes, int year, int month, int day) {
		// TODO Auto-generated method stub
		if (isTimeValid(hours, minutes) == true)
		{
			endHour = hours;
			endMin = minutes;
			setStartEndYear(year);
			setStartEndMonth(month);
			setStartEndDay(day);
			
		}
	}

	public void setStartDateTime(int hours, int minutes, int year, int month, int day) {
		if (isTimeValid(hours, minutes) == true)
		{
			startHour = hours;
			startMin = minutes;
			setStartEndYear(year);
			setStartEndMonth(month);
			setStartEndDay(day);
		}	
	}
	
	public int getCreaterUID()
	{
		return createrID;
	}
	
	public void setCreaterID(int id)
	{
		createrID = id;
	}
	
	public boolean isThisUIDInGoing(int uid)
	{
		return going.contains(uid);
	}
	
	public boolean isThisUIDInWaiting(int uid)
	{
//		return waiting.contains(uid);
//		System.out.println("The whole waiting list of this appointment: " + waiting);
		for (int a:waiting)
		{
			if (a == uid)
			{
//				System.out.println("The following should be added: " + uid);
				return true;
			}
		}
		return false;
	}
}
