package hkust.cse.calendar.unit;

import hkust.cse.calendar.gui.CalGrid;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedList;

public class Appt implements Serializable {
	private TimeSpan mTimeSpan;					// Include day, start time and end time of the appointments
	private String mFreq;	 					// Frequency of the appointmen
	private TimeSpan startDateTimeSpan;
	private TimeSpan endDateTimeSpan;			// end date
	private Reminder mReminder;					// Reminder for the appointment
	
	private int mApptID;						// The appointment id
	private int joinApptID;						// The join appointment i
	private String mTitle;						// The Title of the appointment
	private String mInfo;						// Store the content of the appointments description
	private Location mLocation;					// Location of the appointment
	private boolean ispublic;					// The appointment is a public appointment
	private User owner;
	
	private boolean isjoint;					// The appointment is a joint appointment
	private LinkedList<String> attend;			// The Attendant list
	private LinkedList<String> reject;			// The reject list
	private LinkedList<String> waiting;			// The waiting list
	
	
	public Appt() {								// A default constructor used to set all the attribute to default values
		mApptID = -1;
		mTimeSpan = null;
		startDateTimeSpan = null;
		endDateTimeSpan = null;
		//reminder = null;
		mReminder = new Reminder(/*this*/);
		mTitle = "Untitled";
		mInfo = "";
		mLocation = null;
		isjoint = false;
		attend = new LinkedList<String>();
		reject = new LinkedList<String>();
		waiting = new LinkedList<String>();
		joinApptID = -1;
		ispublic = false;
		owner = null;
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
	
	public void setLocation(Location location) {
		this.mLocation = location;
	}
	
	public Location getLocation() {
		return mLocation;
	}
	
	public void setFrequency(String frequency) {
		this.mFreq = frequency;
	}
	
	public String getFrequency() {
		return mFreq;
	}
	
	public void setReminderTime(Timestamp reminderTime) {
		this.mReminder.setReminderTimestamp(reminderTime);
	}
	
	public Reminder getReminder() {
		return mReminder;
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
	
	public void addAttendant(User addID){
		if (attend == null)
			attend = new LinkedList<String>();
		attend.add(addID.ID());
	}
	
	public void addReject(User addID){
		if (reject == null)
			reject = new LinkedList<String>();
		reject.add(addID.ID());
	}
	
	public void addWaiting(User addID){
		if (waiting == null)
			waiting = new LinkedList<String>();
		waiting.add(addID.ID());
	}
	
	public void setWaitingList(LinkedList<String> waitingList){
		waiting = waitingList;
	}
	
	public void setWaitingList(User[] waitingList){
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (waitingList !=null){
			for (int a=0; a<waitingList.length; a++){
				tempLinkedList.add(waitingList[a].ID());
			}
		}
		waiting = tempLinkedList;
	}
	
	public void setRejectList(LinkedList<String> rejectLinkedList) {
		reject = rejectLinkedList;
	}
	
	public void setRejectList(User[] rejectList){
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (rejectList !=null){
			for (int a=0; a<rejectList.length; a++){
				tempLinkedList.add(rejectList[a].ID());
			}
		}
		reject = tempLinkedList;
	}
	
	public void setAttendList(LinkedList<String> attendLinkedList) {
		attend = attendLinkedList;
	}
	
	public void setAttendList(User[] attendList){
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (attendList !=null){
			for (int a=0; a<attendList.length; a++){
				tempLinkedList.add(attendList[a].ID());
			}
		}
		attend = tempLinkedList;
	}
	// Getter of the appointment title
	public String toString() {
		if(owner.ID().equals(CalGrid.currUser.ID())) {
			return mTitle + "@" + mLocation.getLocationName();
		}
		else {
			return owner.ID() + " - " + mTitle + "@" + mLocation.getLocationName();
		}
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
	
	public Appt[] retrieveAppt(TimeSpan timeSpan) {
//		return frequeny.retrieveOtherAppt(this, timeSpan);
		return null;
	}

	// set and get the end date timespan
	public void setEndDate(TimeSpan s){
		endDateTimeSpan = s;
	}
	public TimeSpan getEndDate(){
		return endDateTimeSpan;
	}
	
	public void setStartDate(TimeSpan s) {
		startDateTimeSpan = s;
	}
	
	public TimeSpan getStartDate() {
		return startDateTimeSpan;
	}
		
	public Appt clone(TimeSpan slot){
		Appt clone = new Appt();
		
		clone.setTimeSpan(new TimeSpan(slot));
		clone.setFrequency(this.getFrequency());
		clone.setStartDate(new TimeSpan(this.getStartDate()));
		clone.setEndDate(new TimeSpan(this.getEndDate()));
		// reminder set outside clone
		clone.setID(this.getID());
		clone.setJoinID(this.getJoinID());
		clone.setTitle(this.getTitle());
		clone.setInfo(this.getInfo());
		clone.setLocation(this.getLocation());		
		clone.setIsPublic(this.isPublic());
		clone.setJoint(this.isJoint());
		clone.setOwner(this.getOwner());
		clone.setWaitingList(this.getWaitingList());
		clone.setRejectList(this.getRejectList());
		clone.setAttendList(this.getAttendList());

		return clone;
	}
	
	public boolean isPublic() {
		return ispublic;
	}
	
	public void setIsPublic(boolean isPublic) {
		ispublic = isPublic;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
		if(isJoint()) {
			this.attend.add(owner.ID());
		}
	}
	
}
