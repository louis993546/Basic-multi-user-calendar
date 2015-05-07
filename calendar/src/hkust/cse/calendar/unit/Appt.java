/**
* This class is based on the skleton code, but since all variables have been replaced with a Appointment 
* object, all methods have been modify, but their functionality remains the same
* 
* @see Appointment.java
* @version 1.0
*/

package hkust.cse.calendar.unit;

import java.io.Serializable;
import java.util.LinkedList;

public class Appt
{

	private Appointment a;

	public Appt() {								// A default constructor used to set all the attribute to default values
		a = new Appointment();
	}
	
	public Appt(Appointment ap)
	{
		a = ap;
	}

	// Getter of the mTimeSpan
	public TimeSpan TimeSpan() {
		return a.getTimeSpan();
	}

	public Appointment getAppointment()
	{
		return a;
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
	public LinkedList<Integer> getAttendList(){
		return a.getGoingList();
	}

	// Getter of the reject LinkedList<String>
//	public LinkedList<String> getRejectList(){
//		return a.getReject();
//	}

	// Getter of the waiting LinkedList<String>
	public LinkedList<Integer> getWaitingList(){
		return a.getWaitingList();
	}

	public LinkedList<Integer> getAllPeople(){
		LinkedList<Integer> allList = new LinkedList<Integer>();
		allList.addAll(getAttendList());
//		allList.addAll(getRejectList());
		allList.addAll(getWaitingList());
		return allList;
	}

//	public void addAttendant(String addID){
//		if (a.getGoingList() == null)
//			a.initiateAttend();
//		a.addToAttend(addID);
//	}

//	public void addReject(String addID){
//		if (a.getReject() == null)
//			a.initiateReject();
//		a.addToReject(addID);
//	}

//	public void addWaiting(String addID){
//		if (a.getWaitingList() == null)
//			a.initiateWaiting();
//		a.addWaiting(addID);
//	}

//	public void setWaitingList(LinkedList<String> waitingList){
//		a.setWaiting(waitingList);
//	}

//	public void setWaitingList(String[] waitingList){
//		LinkedList<String> tempLinkedList = new LinkedList<String>();
//		if (waitingList !=null){
//			for (int a=0; a<waitingList.length; a++){
//				tempLinkedList.add(waitingList[a].trim());
//			}
//		}
//		a.setWaiting(tempLinkedList);
//	}

//	public void setRejectList(LinkedList<String> rejectLinkedList) {
//		a.setReject(rejectLinkedList);
//	}

//	public void setRejectList(String[] rejectList){
//		LinkedList<String> tempLinkedList = new LinkedList<String>();
//		if (rejectList !=null){
//			for (int a=0; a<rejectList.length; a++){
//				tempLinkedList.add(rejectList[a].trim());
//			}
//		}
//		a.setReject(tempLinkedList);
//	}

	public void setAttendList(LinkedList<Integer> attendLinkedList) {
		a.setAttend(attendLinkedList);
	}

//	public void setAttendList(String[] attendList){
//		LinkedList<Integer> tempLinkedList = new LinkedList<Integer>();
//		if (attendList !=null){
//			for (int a=0; a<attendList.length; a++){
//				//TODO turn string into integer
//				tempLinkedList.add(attendList[a].trim());
//			}
//		}
//		a.setAttend(tempLinkedList);
//	}
	
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
	@SuppressWarnings("deprecation")
	public void setTimeSpan(TimeSpan d) {
		a.setStartDateTime(d.StartTime().getHours(), d.StartTime().getMinutes(), d.StartTime().getYear()+1900, d.StartTime().getMonth(), d.StartTime().getDay());
		a.setEndDateTime(d.EndTime().getHours(), d.EndTime().getMinutes(), d.EndTime().getYear()+1900, d.EndTime().getMonth(), d.EndTime().getDay());
	}

	// Setter if the appointment id
	public void setID(int id) {
		a.setID(id);
	}

	// check whether this is a joint appointment
	public boolean isJoint(){
		return a.getIsJoint();
	}

	// setter of the isJoint
	public void setJoint(boolean isjoint){
		a.setIsJoint(isjoint);
	}
}
