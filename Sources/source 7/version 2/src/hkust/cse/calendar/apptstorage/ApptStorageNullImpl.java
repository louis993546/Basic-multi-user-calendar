package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.AdminUser;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.SingletonMediator;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.apptstorage.reminderThread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.lang.NullPointerException;

import com.thoughtworks.xstream.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class ApptStorageNullImpl extends ApptStorage {
	

	private User defaultUser = null;
	private Location[] _locations;
	private BufferedWriter fileWriter;
	private static XStream apptsWriter;
	
	
	public ApptStorageNullImpl(User user)
	{
		defaultUser = user;
		
		/*added by us*/
		_locations = hkust.cse.calendar.unit.SingletonMediator.getBrainInstance().getAllLocations(); // initialize the location list
		mAppts = null;
		reminderThread rem = new reminderThread(user);
		rem.start();
		mAssignedApptID = 0;
		apptsWriter = new XStream();
		apptsWriter.alias("map", java.util.HashMap.class);
		apptsWriter.aliasField("Appt", hkust.cse.calendar.unit.Appt.class, "Appt");
		/*initialize the fileWriter*/
	/*	try{
			fileWriter = new BufferedWriter(new FileWriter("savedAppts.xml"));
		}
		catch(IOException e){
			throw new RuntimeException(e);
		}*/
		
		
		LoadApptFromXml(); // initialize mAppts
		
		/*up to here*/
	}
	
	
	// added by me
	@Override
	public Location[] getLocationList() {
		// TODO Auto-generated method stub
		return _locations;
	}

	@Override
	public void setLocationList(Location[] locations) {
		// TODO Auto-generated method stub
		_locations = locations;
	}
	// up to here
	
	@Override
	public boolean SaveAppt(Appt appt) {
		// TODO Auto-generated method stub

		/*check whether the time span does not overlap with already existing overlaps*/
		if(appt.isJoint()) {
			if(SingletonMediator.getBrainInstance().checkTimeClash(appt)) {
				return false;
			}
		} else {
			Iterator<TimeSpan> itrMap = mAppts.keySet().iterator(); 
			Appt tempCheck = new Appt();	
			itrMap = mAppts.keySet().iterator();
			while(itrMap.hasNext()){
				//check whether the time span does not overlap with another time span
				tempCheck = mAppts.get(itrMap.next());
				if(tempCheck.getUserName().equals(appt.getUserName())) {
					if(tempCheck.Overlap(appt) && !(tempCheck.getUpdate())){
						return false;
					}
				} else if(tempCheck.isJoint()) {
					for(String s : tempCheck.getAttendList()) {
						if(s == appt.getUserName()) {
							if(tempCheck.Overlap(appt) && !tempCheck.getUpdate()) {
								return false;
							}
						}
					}
				}
			}
		}
		appt.setUpdate(false);
		appt.setUseName(defaultUser.ID()); // set the user name
		mAppts.put(appt.TimeSpan(),appt); // adding a new appointment
		return true;
		
		/*if the timeSpan of the appt does not overlap with already existing timeSpan, add  it to the HashMap*/
		
	}

	@Override
	public Appt[] RetrieveAppts(TimeSpan d) {
		// TODO Auto-generated method stub
		/*return an array of appointments*/
		if(mAppts.equals(null)){
			return null;
		}
		Iterator it = mAppts.entrySet().iterator();
		ArrayList<Appt> tempAppts = new ArrayList<Appt>();
		while(it.hasNext()) {
			HashMap.Entry pairs = (HashMap.Entry)it.next();
			//System.out.println(((TimeSpan) pairs.getKey()).StartTime() + ":" + ((TimeSpan) pairs.getKey()).EndTime() + " = " + pairs.getValue());
			//System.out.println("Title: " + ((Appt)pairs.getValue()).getTitle());
			//System.out.println(((TimeSpan) pairs.getKey()).StartTime().toString() + "-" + ((TimeSpan) pairs.getKey()).EndTime().toString());
			//System.out.println(((Appt) pairs.getValue()).TimeSpan().toString());
			if(((TimeSpan) pairs.getKey()).myOverlap(d)) {
				tempAppts.add((Appt) pairs.getValue());
				//System.out.println("I am here");
			}
			else if(((Appt) pairs.getValue()).getType() == 1 && ((Appt) pairs.getValue()).TimeSpan().StartTime().before(d.StartTime())) {
				tempAppts.add((Appt) pairs.getValue());
			}else if(((Appt) pairs.getValue()).getType() == 2 && ((Appt) pairs.getValue()).TimeSpan().StartTime().getDay() == d.StartTime().getDay() && ((Appt) pairs.getValue()).TimeSpan().StartTime().before(d.StartTime())) {
				tempAppts.add((Appt) pairs.getValue());
			}else if(((Appt) pairs.getValue()).getType() == 3 && ((Appt) pairs.getValue()).TimeSpan().StartTime().getDate() == d.StartTime().getDate() && ((Appt) pairs.getValue()).TimeSpan().StartTime().before(d.StartTime())) {
				tempAppts.add((Appt) pairs.getValue());
			}
			//it.remove();
		}
	
		Appt tempArr[] = new Appt[tempAppts.size()];
		int j=0;
		for(int i = 0; i<tempAppts.size(); i++) {
			if(tempAppts.get(i).getUserName().equals(defaultUser.ID())&&!tempAppts.get(i).isJoint()){
				tempArr[i] = tempAppts.get(i);
				j++;
			} else if(tempAppts.get(i).getUserName().equals(defaultUser.ID())&&tempAppts.get(i).isJoint()&&tempAppts.get(i).getIsConfirmed()) {
				tempArr[i] = tempAppts.get(i);
				j++;
			}
			else {
				tempArr[i] = null;
			}
			//System.out.println("About to check for joint");
			if(tempAppts.get(i).isJoint()&&tempAppts.get(i).getIsConfirmed()) {
				//System.out.println("This appointment is confirmed and it should show on GUI");
				for(int k=0; k<tempAppts.get(i).getAttendList().size(); k++) {
					if(tempAppts.get(i).getAttendList().get(k).equals(defaultUser.ID())) {
						tempArr[i] = tempAppts.get(i);
						j++;
						break;
					}
				}
			}
			//System.out.println("getting here");
			//System.out.println(tempArr[i].getInfo());
		}
		Appt tempArr2[] = new Appt[j];
		for(int i = 0, k = 0; i<tempArr.length; i++) {
			if(tempArr[i] != null) {
				tempArr2[k] = tempArr[i];
				k++;
			}
		}
		return tempArr2;
	}

	@Override
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Appt RetrieveAppts(int joinApptID) {
		// TODO Auto-generated method stub
		/*if the map is empty, return null*/
		if(mAppts.isEmpty()){
			return null;
		}
		/*loop trough the map in order to find the appointment with the given joinApptID*/
		Collection<Appt> colOfMap = mAppts.values();
		Iterator<Appt> itrMap = colOfMap.iterator();
		while(itrMap.hasNext()){
			// check whether the time span does not overlap with another time span
			if(itrMap.next().getJoinID() == joinApptID){ //if the ID is found, return the appointment
				return (itrMap.next());
			}
		}
		return null; // ID has not been found, return null
	}

	@Override
	public void UpdateAppt(Appt appt) {
		// TODO Auto-generated method stub
		/*replace the current appointment with the new version*/
		if(mAppts.isEmpty()){return;}
		mAppts.put(appt.TimeSpan(),  appt);
	}

	@Override
	public void RemoveAppt(Appt appt) {
		// TODO Auto-generated method stub
		/*if the map is empty, return*/
		if(mAppts.isEmpty()){return;}
		/*simply remove an existing appointment from the HashMap*/	
		mAppts.remove(appt.TimeSpan());
	}

	@Override
	public User getDefaultUser() {
		// TODO Auto-generated method stub
		return defaultUser;
	}

	@Override
	public void LoadApptFromXml() {
		// TODO Auto-generated method stub
		
		try{
			
			mAppts = (HashMap<TimeSpan,Appt>)apptsWriter.fromXML(new File("savedAppts.xml"));
	
		}catch(Exception e){
			if(e.getMessage().equals("-1") == true){
					//System.out.println("===============================================Loading from XML appointments==============================================================");
					mAppts = new HashMap<TimeSpan,Appt>();
				
			} else{ //if(e.getMessage().equals("AWT-EventQueue-0") == true){
						//System.out.println("===============================================Loading from XML appointments==============================================================");
						mAppts = new HashMap<TimeSpan,Appt>();
					
				
			}
					
		
		}
		
	}
	
	@Override
	public int getGlobalID() {
		// TODO Auto-generated method stub
		return mAssignedApptID;
	}
	
	@Override
	public void saveApptToXml(){
		
		/*=================================================added by me=========================================================*/
		try{
			fileWriter = new BufferedWriter(new FileWriter("savedAppts.xml"));
			System.out.println(hkust.cse.calendar.apptstorage.ApptStorage.mAppts);
			fileWriter.write(apptsWriter.toXML(hkust.cse.calendar.apptstorage.ApptStorage.mAppts));
			
			
		}catch(IOException exc){
			throw new RuntimeException(exc);
		}finally{
			if(fileWriter.equals(null) == false){
				try{fileWriter.flush(); fileWriter.close();} catch(IOException etr){}
			}
		}
		/*=================================================up to here==========================================================*/
		
	}

}
