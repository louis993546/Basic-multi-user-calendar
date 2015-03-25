package hkust.cse.calendar.unit;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedList;

public class Appt implements Serializable {

	private TimeSpan mTimeSpan;					// Include day, start time and end time of the appointments
	private String mTitle;						// The Title of the appointments
	private String mInfo;						// Store the content of the appointments description
	private int mApptID;						// The appointment id
	private int joinApptID;						// The join appointment id
	private boolean isjoint;					// The appointment is a joint appointment
	private LinkedList<String> attend;			// The Attendant list
	private LinkedList<String> reject;			// The reject list
	private LinkedList<String> waiting;			// The waiting list
	private Appointment a;
	
	public Appt() {								// A default constructor used to set all the attribute to default values
		//original
		mApptID = 0;
		mTimeSpan = null;
		mTitle = "Untitled";
		mInfo = "";
		isjoint = false;
		attend = new LinkedList<String>();
		reject = new LinkedList<String>();
		waiting = new LinkedList<String>();
		joinApptID = -1;
		//delete the above code once all //TO-DO on this page is done
		
		a = new Appointment();
	}

	// Getter of the mTimeSpan
	public TimeSpan TimeSpan() {
		return a.getTimeSpan();
	}
	
	// Getter of the appointment title
	public String getTitle() {
		return a.getTitle();
	}

	// Getter of appointment description
	public String getInfo() {
		return a.getDescription();
	}

	// Getter of the appointment id
	public int getID() {
		return a.getID();
	}
	
	// Getter of the join appointment id
	public int getJoinID(){
		return a.getJID();
	}

	public void setJoinID(int joinID){
		a.setJID(joinID);
	}
	
	// Getter of the attend LinkedList<String>
	public LinkedList<String> getAttendList(){
		return a.getAttend();
	}
	
	// Getter of the reject LinkedList<String>
	public LinkedList<String> getRejectList(){
		return a.getReject();
	}
	
	// Getter of the waiting LinkedList<String>
	public LinkedList<String> getWaitingList(){
		return a.getWaiting();
	}
	
	public LinkedList<String> getAllPeople(){
		LinkedList<String> allList = new LinkedList<String>();
		allList.addAll(getAttendList());
		allList.addAll(getRejectList());
		allList.addAll(getWaitingList());
		return allList;
	}
	
	public void addAttendant(String addID){
		//TO-DO
		if (attend == null)
			attend = new LinkedList<String>();
		attend.add(addID);
	}
	
	public void addReject(String addID){
		//TO-DO
		if (reject == null)
			reject = new LinkedList<String>();
		reject.add(addID);
	}
	
	public void addWaiting(String addID){
		//TO-DO
		if (waiting == null)
			waiting = new LinkedList<String>();
		waiting.add(addID);
	}
	
	public void setWaitingList(LinkedList<String> waitingList){
		//TO-DO
		waiting = waitingList;
	}
	
	public void setWaitingList(String[] waitingList){
		//TO-DO
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (waitingList !=null){
			for (int a=0; a<waitingList.length; a++){
				tempLinkedList.add(waitingList[a].trim());
			}
		}
		waiting = tempLinkedList;
	}
	
	public void setRejectList(LinkedList<String> rejectLinkedList) {
		//TO-DO
		reject = rejectLinkedList;
	}
	
	public void setRejectList(String[] rejectList){
		//TO-DO
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (rejectList !=null){
			for (int a=0; a<rejectList.length; a++){
				tempLinkedList.add(rejectList[a].trim());
			}
		}
		reject = tempLinkedList;
	}
	
	public void setAttendList(LinkedList<String> attendLinkedList) {
		//TO-DO
		attend = attendLinkedList;
	}
	
	public void setAttendList(String[] attendList){
		//TO-DO
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
		return a.getTitle();
	}

	// Setter of the appointment title
	public void setTitle(String t) {
		a.setTitle(t);
	}

	// Setter of the appointment description
	public void setInfo(String in) {
		a.setDescription(in);
	}

	// Setter of the mTimeSpan
	public void setTimeSpan(TimeSpan d) {
		//TO-DO
		mTimeSpan = d;
	}

	// Setter if the appointment id
	public void setID(int id) {
		mApptID = id;
	}
	
	// check whether this is a joint appointment
	public boolean isJoint(){
		//TO-DO
		return isjoint;
	}

	// setter of the isJoint
	public void setJoint(boolean isjoint){
		this.isjoint = isjoint;
	}
}
