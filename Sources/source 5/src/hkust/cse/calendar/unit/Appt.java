package hkust.cse.calendar.unit;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

public class Appt implements Serializable, Cloneable {
	
	public final static int SINGLE = 1;
	public final static int DAILY = 2;
	public final static int WEEKLY = 3;
	public final static int MONTHLY = 4;
	
	private TimeSpan mTimeSpan;					// Include day, start time and end time of the appointments

	private String mTitle;						// The Title of the appointments

	private String mInfo;						// Store the content of the appointments description

	private int mApptID;						// The appointment id
	
	private int joinApptID;						// The join appointment id

	private boolean isjoint;					// The appointment is a joint appointment
	
	private LinkedList<String> attend;			// The Attendant list
	
	private LinkedList<String> reject;			// The reject list
	
	private LinkedList<String> waiting;			// The waiting list
	
	private int frequency;						// The frequency of the appointment (e.g. WEEKLY)
	
	private boolean reminder;					// Whether a reminder is needed
	
	private Timestamp reminderTime;				// Time before the appointment to remind
	
	private boolean derivedFromSchedule;		// Is this event derived from schedule
	
	private boolean isScheduled;
	
	private Location location;
	
	private boolean sendEmail;
	
	private boolean sendSms;
	
	public Appt() {								// A default constructor used to set all the attribute to default values
		mApptID = 0;
		mTimeSpan = null;
		mTitle = "Untitled";
		mInfo = "";
		isjoint = false;
		attend = new LinkedList<String>();
		reject = new LinkedList<String>();
		waiting = new LinkedList<String>();
		joinApptID = -1;
		frequency = SINGLE;
		reminder = false;
		derivedFromSchedule = false;
		setReminderTime(0, 0);
	}

	// Getter of the mTimeSpan
	public TimeSpan TimeSpan() {
		return mTimeSpan;
	}
	
	// Getter of the appointment title
	public String getTitle() {
		return mTitle;
	}

	// Getter of appointment description
	public String getInfo() {
		return mInfo;
	}

	// Getter of the appointment id
	public int getID() {
		return mApptID;
	}
	
	// Getter of the join appointment id
	public int getJoinID(){
		return joinApptID;
	}

	public void setJoinID(int joinID){
		this.joinApptID = joinID;
	}
	// Getter of the attend LinkedList<String>
	public LinkedList<String> getAttendList(){
		return attend;
	}
	
	// Getter of the reject LinkedList<String>
	public LinkedList<String> getRejectList(){
		return reject;
	}
	
	// Getter of the waiting LinkedList<String>
	public LinkedList<String> getWaitingList(){
		return waiting;
	}
	
	public LinkedList<String> getAllPeople(){
		LinkedList<String> allList = new LinkedList<String>();
		allList.addAll(attend);
		allList.addAll(reject);
		allList.addAll(waiting);
		return allList;
	}
	
	public void addAttendant(String addID){
		if (attend == null)
			attend = new LinkedList<String>();
		attend.add(addID);
	}
	
	public void addReject(String addID){
		if (reject == null)
			reject = new LinkedList<String>();
		reject.add(addID);
	}
	
	public void addWaiting(String addID){
		if (waiting == null)
			waiting = new LinkedList<String>();
		waiting.add(addID);
	}
	
	public void setWaitingList(LinkedList<String> waitingList){
		waiting = waitingList;
	}
	
	public void setWaitingList(String[] waitingList){
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (waitingList !=null){
			for (int a=0; a<waitingList.length; a++){
				tempLinkedList.add(waitingList[a].trim());
			}
		}
		waiting = tempLinkedList;
	}
	
	public void setRejectList(LinkedList<String> rejectLinkedList) {
		reject = rejectLinkedList;
	}
	
	public void setRejectList(String[] rejectList){
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (rejectList !=null){
			for (int a=0; a<rejectList.length; a++){
				tempLinkedList.add(rejectList[a].trim());
			}
		}
		reject = tempLinkedList;
	}
	
	public void setAttendList(LinkedList<String> attendLinkedList) {
		attend = attendLinkedList;
	}
	
	public void setAttendList(String[] attendList){
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (attendList !=null){
			for (int a=0; a<attendList.length; a++){
				tempLinkedList.add(attendList[a].trim());
			}
		}
		attend = tempLinkedList;
	}
	// Getter of the appointment title
	public String toString() {
		return mTitle;
	}

	// Setter of the appointment title
	public void setTitle(String t) {
		mTitle = t;
	}

	// Setter of the appointment description
	public void setInfo(String in) {
		mInfo = in;
	}

	// Setter of the mTimeSpan
	public void setTimeSpan(TimeSpan d) {
		mTimeSpan = d;
	}

	// Setter if the appointment id
	public void setID(int id) {
		mApptID = id;
	}
	
	// check whether this is a joint appointment
	public boolean isJoint(){
		return isjoint;
	}

	// setter of the isJoint
	public void setJoint(boolean isjoint){
		this.isjoint = isjoint;
	}

	public int getFrequency(){
		return frequency;
	}
	
	public void setFrequency(int f){
		switch(f){
		case 1:
			frequency = SINGLE;
			break;
		case 2:
			frequency = DAILY;
			break;
		case 3:
			frequency = WEEKLY;
			break;
		case 4:
			frequency = MONTHLY;
		}
	}
	
	public boolean needReminder(){
		return reminder;
	}
	
	public void setReminder(boolean r){
		reminder =r;
	}
	
	public Timestamp getReminderTime(){
		return reminderTime;
	}
	
	public void setReminderTime(int h, int m){
		reminderTime = new Timestamp(0, 0, 0, h, m, 0, 0);
	}
	
	public void setDerivedFromSchedule(boolean b) {
		derivedFromSchedule = b;
	}
	
	public boolean getDerivedFromSchedule() {
		return derivedFromSchedule;
	}
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void setScheduled(boolean s) {
		isScheduled = s;
	}
	
	public boolean IsScheduled() {
		return isScheduled;
	}
	
	public void setLocation(Location l) {
		location = l;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setSendEmail(boolean b) {
		sendEmail = b;
	}
	
	public boolean sendEmail() {
		return sendEmail;
	}
	
	public void setSendSms(boolean b) {
		sendSms = b;
	}
	
	public boolean sendSms() {
		return sendSms;
	}
}
