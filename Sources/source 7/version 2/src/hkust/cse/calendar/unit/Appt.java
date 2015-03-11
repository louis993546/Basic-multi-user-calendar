package hkust.cse.calendar.unit;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedList;

import hkust.cse.calendar.unit.Location;

public class Appt implements Serializable,  Comparable<Appt> {

	private TimeSpan mTimeSpan;					// Include day, start time and end time of the appointments
	
	private boolean updAvail;                    // whether it is possible to update
	// added by us
	private Location locAppt;					// the location of an appointment
	
	private String userName;                    // user�s ID for regular events       
	//private boolean delNo; 					// prevent an object from being deleted
	
	private int typeOfAppt;                  // determines the type of the appointment
	
	private boolean canDelete;               // helps to protect an app from being deleted
	
	private boolean isDone;
	
	private int reminder;
	
	private boolean notification;
	
	//private boolean notification; 				// checks whether the appointment need a notification
	// up to here
	
	private String mTitle;						// The Title of the appointments

	private String mInfo;						// Store the content of the appointments description

	private int mApptID;						// The appointment id
	
	private int joinApptID;						// The join appointment id

	private boolean isjoint;					// The appointment is a joint appointment
	
	private boolean isConfirmed;				// set to true after all waiting list attendents are attending
	
	private LinkedList<String> attend;			// The Attendant list
	
	private LinkedList<String> reject;			// The reject list
	
	private LinkedList<String> waiting;			// The waiting list
	
	public Appt() {								// A default constructor used to set all the attribute to default values
		mApptID = 0;
		mTimeSpan = null;
		typeOfAppt = 0; // one-off
		updAvail = true;
		locAppt = null;
		mTitle = "Untitled";
		mInfo = "";
		canDelete = true;
		notification = false;
		isjoint = false;
		attend = new LinkedList<String>();
		reject = new LinkedList<String>();
		waiting = new LinkedList<String>();
		joinApptID = -1;
		reminder = 0;//no reminder first
		userName = null;
		isConfirmed = false;
		//delNo = false;
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
	
	public Location getLocation(){
		return locAppt;
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
	
	public void setIsConfirmed(boolean c) {
		this.isConfirmed = c;
	}
	
	public boolean getIsConfirmed() {
		return this.isConfirmed;
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
		waiting.remove(addID);
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
		//System.out.println(addID);
	}
	
	public void removeWaiting(String removeID){
		if(waiting != null){
			waiting.remove(removeID); // clear the entire list
		}
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

	/*set the type of the appointment*/
	public void setType(int source){
		//System.out.println("the repeat type is" + source);
		typeOfAppt = source;
	}
	
	public int getType(){
		return typeOfAppt;
	}
	
	/*if anything is wrong, it's wrong with this function*/
	@Override
	public int compareTo(Appt compare) {
		
		 int result = this.mTimeSpan.StartTime().compareTo(compare.TimeSpan().StartTime());
		 return result;
	}
	
	/*set the location of the appointment*/
	public void setLocation(String l){
		locAppt = new Location(l);
	}
	
	
	public void setReminder(int rem) {
		//System.out.println("Reminder timer" + rem);
		reminder = rem;
	}
	
	public int getReminder() {
		return reminder;
	}
	

	public void setUpdate(boolean source){
		updAvail = source;
	}
	
	public boolean getUpdate(){
		return updAvail;
	}
	
	public boolean getDone(){
		return isDone;
	}
	
	public void setDone(boolean x){
		isDone = x;
	}
	
	public void setDelete(boolean source){
		canDelete = source;
	}
	
	public boolean canDelete(){
		return canDelete;
	}
	
	public void setUseName(String user){
		userName = user;
	}
	
	public String getUserName(){
		return userName;
	}
	
	public boolean Overlap(Appt other) {
		Timestamp thisStartTimeOfDay = new Timestamp(2000, 1, 1, this.TimeSpan().StartTime().getHours(), this.TimeSpan().StartTime().getMinutes(), 0, 0);
		Timestamp thisEndTimeOfDay = new Timestamp(2000, 1, 1, this.TimeSpan().EndTime().getHours(), this.TimeSpan().EndTime().getMinutes(), 0, 0);
		
		TimeSpan thisTimeSpan = new TimeSpan(thisStartTimeOfDay, thisEndTimeOfDay);
		
		Timestamp otherStartTimeOfDay = new Timestamp(2000, 1, 1, other.TimeSpan().StartTime().getHours(), other.TimeSpan().StartTime().getMinutes(), 0, 0);
		Timestamp otherEndTimeOfDay = new Timestamp(2000, 1, 1, other.TimeSpan().EndTime().getHours(), other.TimeSpan().EndTime().getMinutes(), 0, 0);
		
		TimeSpan otherTimeSpan = new TimeSpan(otherStartTimeOfDay, otherEndTimeOfDay);
		//System.out.println("this is " + this.getType());
		//System.out.println("other is " + other.getType());
		
		if(this.getType() == 0) {//only one time
			if(other.getType() == 0) {
				return this.TimeSpan().Overlap(other.TimeSpan());
			}else if(other.getType() == 1) {//daily
				if(this.TimeSpan().Overlap(other.TimeSpan())) {
					return true;
				} else if(this.TimeSpan().after(other.TimeSpan())) {
					
					return thisTimeSpan.Overlap(otherTimeSpan);
				} else
					return false;
			}else if(other.getType() == 2) {//weekly
				if(this.TimeSpan().Overlap(other.TimeSpan())) {
					return true;
				} else if(this.TimeSpan().after(other.TimeSpan())) {
					if(this.TimeSpan().StartTime().getDay() == other.TimeSpan().StartTime().getDay()) {
						return thisTimeSpan.Overlap(otherTimeSpan);
					}else 
						return false;
				} else 
					return false;
			}else if(other.getType() == 3) {//monthly
				if(this.TimeSpan().Overlap(other.TimeSpan())) {
					return true;
				} else if(this.TimeSpan().after(other.TimeSpan())) {
					if(this.TimeSpan().StartTime().getDate() == other.TimeSpan().StartTime().getDate()) {
						return thisTimeSpan.Overlap(otherTimeSpan);
					} else
						return false;
				}else
					return false;
			}
		}else if(this.getType() == 1) {//daily
			if(other.getType() == 0) {
				if(this.TimeSpan().Overlap(other.TimeSpan())) {
					return true;
				} else if(other.TimeSpan().after(this.TimeSpan())) {
					return thisTimeSpan.Overlap(otherTimeSpan);
				} else
					return false;
			}else if(other.getType() == 1) {//daily
				if(this.TimeSpan().Overlap(other.TimeSpan())) {
					return true;
				} else {
					return otherTimeSpan.Overlap(thisTimeSpan);
				}
			}else if(other.getType() == 2) {//weekly
				if(this.TimeSpan().Overlap(other.TimeSpan())) {
					return true;
				} else {
					System.out.println("ITS HERE DUUUUUDE");
					System.out.println(thisTimeSpan.toString());
					System.out.println(otherTimeSpan.toString());
					
					return thisTimeSpan.Overlap(otherTimeSpan);
				}
			}else if(other.getType() == 3) {//monthly
				if(this.TimeSpan().Overlap(other.TimeSpan())) {
					return true;
				} else {
					System.out.println("ITS HERE DUUUUUDE");
					System.out.println(thisTimeSpan.toString());
					System.out.println(otherTimeSpan.toString());
					
					return thisTimeSpan.Overlap(otherTimeSpan);
				}
			}
		}else if(this.getType() == 2) {//weekly
			if(other.getType() == 0) {
				if(this.TimeSpan().Overlap(other.TimeSpan())) {
					return true;
				} else if(this.TimeSpan().before(other.TimeSpan())) {
					System.out.println("ITS HERE DUUUUUDE5");
					if(this.TimeSpan().StartTime().getDay() == other.TimeSpan().StartTime().getDay()) {
						System.out.println("ITS HERE DUUUUUDE6");
						return thisTimeSpan.Overlap(otherTimeSpan);
					}else {
						System.out.println("ITS HERE DUUUUUDE7");
						return false;
					}
				} else
					return false;
			}else if(other.getType() == 1) {//daily
				if(this.TimeSpan().Overlap(other.TimeSpan())) {
					return true;
				} else {
					System.out.println("this is weekly other is daily");
					return thisTimeSpan.Overlap(otherTimeSpan);
				}
			}else if(other.getType() == 2) {//weekly
				if(this.TimeSpan().Overlap(other.TimeSpan())) {
					return true;
				} else {//this is a special case look into it later
					if(this.TimeSpan().StartTime().getDay() == other.TimeSpan().StartTime().getDay()) {
						return thisTimeSpan.Overlap(otherTimeSpan);
					} else {
						return false;
					}
				}
			}else if(other.getType() == 3) {//monthly
				if(this.TimeSpan().Overlap(other.TimeSpan())) {
					return true;
				} else {
					if(thisTimeSpan.Overlap(otherTimeSpan))
						return true;
					else
						return false;	
				}
					
			}
		}else if(this.getType() == 3) {//monthly
			if(other.getType() == 0) {
				if(this.TimeSpan().Overlap(other.TimeSpan())) {
					return true;
				} else if(this.TimeSpan().StartTime().getDate() == other.TimeSpan().StartTime().getDate()) {
					return thisTimeSpan.Overlap(otherTimeSpan);
				}
			}else if(other.getType() == 1) {//daily
				return thisTimeSpan.Overlap(otherTimeSpan);
			}else if(other.getType() == 2) {//weekly
				return thisTimeSpan.Overlap(otherTimeSpan);
			}else if(other.getType() == 3) {//monthly
				if(this.TimeSpan().Overlap(other.TimeSpan())) {
					return true;
				}else if(this.TimeSpan().StartTime().getDate()==other.TimeSpan().StartTime().getDate()) {
					return thisTimeSpan.Overlap(otherTimeSpan);
				} else {
					return false;
				}
			}
		}
		return false;
	}
}

