package hkust.cse.calendar.unit;


import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.unit.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
//import java.util.Map;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.thoughtworks.xstream.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SingletonMediator {
	/*a static object which handles all the system requests*/
	private volatile static SingletonMediator systemBrain;
	
	private HashMap<String, User> allUsers;
	
	//private static HashMap<TimeSpan, Appt> allAppts;
	
	private HashMap<TimeSpan, Appt> allAppts;
	private Location[] allLocations;
	private XStream locXStream;
	private BufferedWriter fileWriter;
	
	private static User thisUser;
	
	public class NotifyInitiator { 
		public String userName;
		public int confirmed;
		
		public String deleteUser;
		public Location deleteLocation;
		
		public NotifyInitiator(String u, String d) {
			userName = u;
			deleteUser = d;
			confirmed = -1;
			deleteLocation=null;
		}
		
		public NotifyInitiator(String u, Location l){
			userName = u;
			deleteUser = null;
			confirmed = -1;
			deleteLocation = l;
		}
		
		/*public boolean getConfirmed() {
			return confirmed;
		}
		
		public void setConfirmed(boolean c) {
			confirmed = c;
		}*/
	}
	
	ArrayList<NotifyInitiator> notifyInitiatorList;
	
	private SingletonMediator() {
		notifyInitiatorList = new ArrayList<NotifyInitiator>();
		//allAppts=hkust.cse.calendar.apptstorage.ApptStorage.mAppts;
		try{
			locXStream = new XStream();
			locXStream.alias("Location", hkust.cse.calendar.unit.Location.class);
			
			allLocations = (Location[]) locXStream.fromXML(new File("savedLocations.xml"));
		}catch(Exception e){
			
			allLocations = null;
		}
	}
	
	/*static method to return the only SingletonMediator object in the entire system*/
	public static SingletonMediator getBrainInstance(){
		if(systemBrain==null){
			synchronized (SingletonMediator.class){
				if(systemBrain==null){
					systemBrain=new SingletonMediator();
				}
			}
		}
		return systemBrain;
	}
	
	public void setUser(User current){
		System.out.println("Current user is: " + current);
		thisUser = current;
	}
	
	public void setUserHashMap(HashMap<String, User> u) {
		this.allUsers = u;
	}
	
	
	public boolean checkTimeClash(Appt group){
		Iterator it = ApptStorage.mAppts.entrySet().iterator();
		while(it.hasNext()){
			HashMap.Entry pairs = (HashMap.Entry)it.next();
			//((Appt) pairs.getValue())
			if(!(((Appt) pairs.getValue()).isJoint())){
				if(((Appt) pairs.getValue()).Overlap(group)){
					if(group.getAllPeople().contains(((Appt) pairs.getValue()).getUserName()))
						return true;
				}
					
			}
			else{
				if(((Appt) pairs.getValue()).Overlap(group)){
					for(String s : ((Appt) pairs.getValue()).getAllPeople()){
						if(group.getWaitingList().contains(s))
							return true;
					}
					if(((Appt) pairs.getValue()).getLocation().getName().equals(group.getLocation().getName()))
						return true;
				}
			}
		}
		
		return false;
	}
	
	public void deleteLocation(Location location){
		int index=-1;
		ArrayList<Location> temp = new ArrayList<Location>(Arrays.asList(allLocations));
		for(int i = 0; i<temp.size();i++){
			if(temp.get(i).getName().equals(location.getName())){
				index=i;
				break;
			}
		}
		temp.remove(index);
		allLocations = null;
		allLocations = new Location[temp.size()];
		for(int arrSize = 0; arrSize < temp.size(); arrSize++){
				allLocations[arrSize] = temp.get(arrSize);
			
		}
	}
	
	/*return to the user the closest TimeSpan which is suitable for every group member*/
	public TimeSpan[] checkGroupTimeSpans(ArrayList<String> participants, String location){
		//there are no participants
		if(participants.isEmpty()){
			return null;
		}
		
		int numOfParticipants = participants.size(); // get the number of participants
		Appt temp = new Appt();
		temp.setJoint(true);
		System.out.println("----------");
		for(int i=0;i<numOfParticipants;i++){
			System.out.println(participants.get(i));
			temp.addWaiting(participants.get(i));
		}
		System.out.println("-----------" + " " + numOfParticipants);
		temp.setLocation(location);
		int hoursToAdd=0;
		Date check = new Date();
		boolean nextDay=false;
		int startHour=check.getHours()+1;
		if(startHour>=18){
			nextDay=true;
			startHour=8;
			check.setHours(18);
			check.setMinutes(0);
			check.setSeconds(0);
		}
		else if(startHour<8){
			hoursToAdd=8-check.getHours();
			check.setMinutes(0);
			check.setSeconds(0);
		}
		else{
			hoursToAdd=0;
			check.setHours(startHour);
			check.setMinutes(0);
			check.setSeconds(0);
		}
		
		TimeSpan arrOfAppts[] = new TimeSpan[5];
		int counter = 0;
		Timestamp startTime;
		Timestamp endTime;
		if(nextDay){
			startTime = new Timestamp(check.getTime()+(14*60*60*1000));
			endTime = new Timestamp((startTime.getTime() + (60*60*1000)));
		}
		else{
			startTime = new Timestamp(check.getTime()+(hoursToAdd*60*60*1000));
			endTime = new Timestamp((startTime.getTime() + (60*60*1000)));
		}
		startTime.setNanos(0);
		endTime.setNanos(0);
		TimeSpan tempCheck = new TimeSpan(startTime, endTime);
		
		temp.setTimeSpan(tempCheck);
		while(true){
			if(checkTimeClash(temp)){
				long time = startTime.getTime();
				startHour++;
				if(startHour==18){
					startTime = new Timestamp(time+(14*60*60*1000));
				}
				else{
					startTime = new Timestamp(time+(60*60*1000));
				}
				endTime = new Timestamp((startTime.getTime() + (60*60*1000)));
				tempCheck = new TimeSpan(startTime, endTime);
				temp.setTimeSpan(tempCheck);
				//continue;	
			}
			else{
				arrOfAppts[counter]=tempCheck;
				counter++;
				if(counter==5)
					break;
				else{
					long time = startTime.getTime();
					startHour++;
					if(startHour==18){
						startTime = new Timestamp(time+(14*60*60*1000));
					}
					else{
						startTime = new Timestamp(time+(60*60*1000));
					}
					endTime = new Timestamp((startTime.getTime() + (60*60*1000)));
					tempCheck = new TimeSpan(startTime, endTime);
					temp.setTimeSpan(tempCheck);
				}
			}
		}
		
		return arrOfAppts;
	}
	
	/*Getter of all the system users, excluding the root*/
	public HashMap<String, User> getAllUsers(){
		return allUsers;
	}
	
	/*Delete all the userís appointments*/
	public void deleteAppointments(String user){
		
	}
	
	//notify the users about the group event
	public ArrayList<Appt> notifyGroupUsers(){
		System.out.println("In notify Group Users");
		ArrayList<Appt> temp = new ArrayList<Appt>();
		Iterator it = ApptStorage.mAppts.entrySet().iterator();
		while(it.hasNext()){
			HashMap.Entry pairs = (HashMap.Entry)it.next();
			if(((Appt) pairs.getValue()).isJoint()){
				for(String s : ((Appt) pairs.getValue()).getWaitingList()){
					System.out.println(s + " : " + thisUser.ID());
					if(s.equals(thisUser.ID())){
						System.out.println("adding to temp");
						temp.add(((Appt) pairs.getValue()));
						break;
					}
				}
			}
		}
		if(temp.size()!=0) {
			System.out.println("You are added to a group appt");
		}
		return temp;
	}
	
	//group event is confirmed (if all users accept)
	public void confirmGroup(Appt group){
		if(group.getWaitingList().isEmpty()){
			System.out.println("This is set to true!!!!!");
			group.setIsConfirmed(true);
		}
	}
	
	public String getThisUserName(){
		return thisUser.ID();
	}
	
	public static void removeAppt(Appt temp){
		ApptStorage.mAppts.remove(temp.TimeSpan(), temp);
	}
	public User getAnyUser(String userName){
		return (allUsers.get(userName));
	}
	
	public Location[] getAllLocations(){
		return (this.allLocations);
	}
	
	public void deleteUser(String dUser) {
		int size = notifyInitiatorList.size();
				System.out.println("inside mediator deleteuser");
		Iterator it = ApptStorage.mAppts.entrySet().iterator();
		//synchronized {
			while(it.hasNext()){
				HashMap.Entry pairs = (HashMap.Entry)it.next();
				//((Appt) pairs.getValue())
				//Entry item = (Entry) it.next();
				if(((Appt) pairs.getValue()).isJoint()){
					if(((Appt) pairs.getValue()).getUserName().equals(dUser)){
						//ApptStorage.mAppts.remove(((Appt) pairs.getValue()).TimeSpan(), ((Appt) pairs.getValue()));
						//return true;
						//it.remove();
					}
					else{
						System.out.println("about to add to notify initiator list1");
						for(String s : ((Appt) pairs.getValue()).getAllPeople()){
							System.out.println("about to add to notify initiator list2");
							if(s.equals(dUser)){
								System.out.println("adding to notify initiator list3");
								notifyInitiatorList.add(new NotifyInitiator(((Appt) pairs.getValue()).getUserName(),dUser));
								break;
							}
						}
					}
						
				}
				else{
					if(((Appt) pairs.getValue()).getUserName().equals(dUser)){
						//ApptStorage.mAppts.remove(((Appt) pairs.getValue()).TimeSpan(), ((Appt) pairs.getValue()));
					}
				} 
			
			}
		//}
		if(size==notifyInitiatorList.size()){
			allUsers.remove(dUser);
		}
		
	}
	
	// save the locations
		public void saveAllLocations(){
			try{
				fileWriter = new BufferedWriter(new FileWriter("savedLocations.xml")); // initialize the fileWriter variable to a file
				fileWriter.write(locXStream.toXML(allLocations));
				
			}catch(IOException exc){
				throw new RuntimeException(exc);
			}finally{
				if(fileWriter.equals(null) == false){
					try{fileWriter.close();} catch(IOException etr){}
				}
			}
		}
		
		public void setAllLocations(Location[] sourceLocation){
			this.allLocations = sourceLocation;
		}
		
		public Location getLocationFromString(String name){
			for(int i =0; i < allLocations.length; i++){
				if(allLocations[i].getName().equals(name)){
					return allLocations[i];
				}
			}
			return null;
		}
	
	public void notifyDeleteLocation(String dLocation){
		System.out.println("location passed to farhad: "+dLocation);
		int size = notifyInitiatorList.size();
		
		Iterator it = ApptStorage.mAppts.entrySet().iterator();
		while(it.hasNext()){
			HashMap.Entry pairs = (HashMap.Entry)it.next();
			System.out.println("NIGGGGGGAAAAAA "+((Appt) pairs.getValue()).getLocation().getName());
			if(((Appt) pairs.getValue()).getLocation().getName().equals(dLocation)){
				System.out.println("####adding the location##### "+ dLocation);
				for(int i=0; i<allLocations.length; i++) {
					if(allLocations[i].getName().equals(dLocation)) {
						notifyInitiatorList.add(new NotifyInitiator(((Appt) pairs.getValue()).getUserName(),allLocations[i]));
						break;
					}
				}
				
			}
		}
		if(size==notifyInitiatorList.size()){
			for(int i=0; i<allLocations.length; i++) {
				if(allLocations[i].getName().equals(dLocation)) {
					deleteLocation(allLocations[i]);
					break;
				}
			}
		}
	}
	
	public ArrayList<NotifyInitiator> getNotifyInitiatorListLocation(){
		ArrayList<NotifyInitiator> temp = new ArrayList<NotifyInitiator>();
		for(int i=0;i<notifyInitiatorList.size();i++){
			//System.out.println(notifyInitiatorList.get(i).userName + " : " + );
			if(notifyInitiatorList.get(i).deleteLocation!=null){
				if(notifyInitiatorList.get(i).userName.equals(thisUser.ID())){
					//System.out.println("I am prompting dude");
					temp.add(notifyInitiatorList.get(i));
				}
			}
		}
		return temp;
	}
	
	public ArrayList<NotifyInitiator> getNotifyInitiatorList(){
		ArrayList<NotifyInitiator> temp = new ArrayList<NotifyInitiator>();
		System.out.println("Size of notify init: " + notifyInitiatorList.size());
		for(int i=0;i<notifyInitiatorList.size();i++){
			//System.out.println(notifyInitiatorList.get(i).userName + " : " + );
			if(notifyInitiatorList.get(i).deleteUser!=null){
				if(notifyInitiatorList.get(i).userName.equals(thisUser.ID())){
					//System.out.println("I am prompting dude");
					temp.add(notifyInitiatorList.get(i));
				}
			}
		}
		return temp;
	}
	
	public void checkDeleteUser() {
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(int i=0;i<notifyInitiatorList.size();i++){
			if(notifyInitiatorList.get(i).deleteUser!=null){
				if(notifyInitiatorList.get(i).confirmed==1){
					if(!(temp.contains(notifyInitiatorList.get(i).deleteUser))){
						temp.add(notifyInitiatorList.get(i).deleteUser);
					}
					//System.out.println("******adding index " +i);
					indexes.add(i);
				}
			}
		}
		for(int j = 0;j<indexes.size();j++){
			//System.out.println("removing from list: "+notifyInitiatorList.get(indexes.get(j)).userName);
			notifyInitiatorList.remove((int)indexes.get(j));
			//notifyInitiatorList.
		}
		
		//System.out.println("#################");
		indexes.clear();
		for(int i=0;i<notifyInitiatorList.size();i++){
			if(notifyInitiatorList.get(i).deleteUser!=null){
				if(notifyInitiatorList.get(i).confirmed==-1){
					if(temp.contains(notifyInitiatorList.get(i).deleteUser)){
						temp.remove(notifyInitiatorList.get(i).deleteUser);
					}
				}
			}
		}
		for(int i=0;i<notifyInitiatorList.size();i++){
			if(notifyInitiatorList.get(i).deleteUser!=null){
				if(notifyInitiatorList.get(i).confirmed==0){
					if(temp.contains(notifyInitiatorList.get(i).deleteUser)){
						temp.remove(notifyInitiatorList.get(i).deleteUser);
					}
					indexes.add(i);
				}
			}
		}
		for(int j = 0;j<indexes.size();j++){
			notifyInitiatorList.remove((int)indexes.get(j));
		}
		indexes.clear();
		for(int i = 0;i<temp.size();i++){
			Iterator it = ApptStorage.mAppts.entrySet().iterator();
			while(it.hasNext()){
				HashMap.Entry pairs = (HashMap.Entry)it.next();
				//((Appt) pairs.getValue())
				if(((Appt) pairs.getValue()).isJoint()){
					if(((Appt) pairs.getValue()).getUserName().equals(temp.get(i))){
						it.remove();
					}
					else{
						if(((Appt) pairs.getValue()).getAllPeople().contains(temp.get(i))){
							if(((Appt) pairs.getValue()).getWaitingList().contains(temp.get(i))){
								((Appt) pairs.getValue()).getWaitingList().remove(temp.get(i));
							}
							else if(((Appt) pairs.getValue()).getAttendList().contains(temp.get(i))){
								((Appt) pairs.getValue()).getAttendList().remove(temp.get(i));
							}
						}
					}
				}
				else{
					if(((Appt) pairs.getValue()).getUserName().equals(temp.get(i))){
						it.remove();
					}
				}
			}
			allUsers.remove(temp.get(i));
		}
	}
	
	public void checkDeleteLocation(){
		ArrayList<Location> temp = new ArrayList<Location>();
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(int i=0;i<notifyInitiatorList.size();i++){
			if(notifyInitiatorList.get(i).deleteLocation!=null){
				if(notifyInitiatorList.get(i).confirmed==1){
					if(!(temp.contains(notifyInitiatorList.get(i).deleteLocation))){
						temp.add(notifyInitiatorList.get(i).deleteLocation);
					}
					//System.out.println("******** adding index "+i);
					indexes.add(i);
				}
			}
		}
		for(int j = 0;j<indexes.size();j++){
			//System.out.println("Removing the accepted person"+notifyInitiatorList.get(indexes.get(j)).userName);
			notifyInitiatorList.remove((int)indexes.get(j));
		}
		indexes.clear();
		for(int i=0;i<notifyInitiatorList.size();i++){
			if(notifyInitiatorList.get(i).deleteLocation!=null){
				if(notifyInitiatorList.get(i).confirmed==-1){
					if(temp.contains(notifyInitiatorList.get(i).deleteLocation)){
						temp.remove(notifyInitiatorList.get(i).deleteLocation);
					}
				}
			}
		}
		for(int i=0;i<notifyInitiatorList.size();i++){
			if(notifyInitiatorList.get(i).deleteLocation!=null){
				if(notifyInitiatorList.get(i).confirmed==0){
					if(temp.contains(notifyInitiatorList.get(i).deleteLocation)){
						temp.remove(notifyInitiatorList.get(i).deleteLocation);
					}
					indexes.add(i);
				}
			}
		}
		for(int j = 0;j<indexes.size();j++){
			notifyInitiatorList.remove((int)indexes.get(j));
		}
		indexes.clear();
		for(int i = 0;i<temp.size();i++){
			Iterator it = ApptStorage.mAppts.entrySet().iterator();
			while(it.hasNext()){
				HashMap.Entry pairs = (HashMap.Entry)it.next();
				//((Appt) pairs.getValue())
				if(((Appt) pairs.getValue()).getLocation().getName().equals(temp.get(i).getName())){
					it.remove();
				}
			}
			deleteLocation(temp.get(i));
		}
	}
}