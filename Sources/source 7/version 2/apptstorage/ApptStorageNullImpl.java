package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

public class ApptStorageNullImpl extends ApptStorage {
	

	private User defaultUser = null;
	private Location[] _locations;
	
	public ApptStorageNullImpl(User user)
	{
		defaultUser = user;
		/*added by us*/
		mAppts = new HashMap<TimeSpan,Appt>();
		mAssignedApptID = 0;
		
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
		
		Iterator<TimeSpan> itrMap = mAppts.keySet().iterator(); 
		Appt tempCheck = new Appt();	
				
		itrMap = mAppts.keySet().iterator();
				 
		while(itrMap.hasNext()){
			//check whether the time span does not overlap with another time span
			tempCheck = mAppts.get(itrMap.next());
			if(tempCheck.TimeSpan().Overlap(appt.TimeSpan())){
				return false;
			}
		}
		mAppts.put(appt.TimeSpan(),appt); // adding a new appointment
		
		System.out.println("Added new appointment");
		return true;
		
		/*if the timeSpan of the appt does not overlap with already existing timeSpan, add  it to the HashMap*/
		
	}

	@Override
	public Appt[] RetrieveAppts(TimeSpan d) {
		// TODO Auto-generated method stub
		/*return an array of appointments*/
		Iterator it = mAppts.entrySet().iterator();
		ArrayList<Appt> tempAppts = new ArrayList<Appt>();
		while(it.hasNext()) {
			HashMap.Entry pairs = (HashMap.Entry)it.next();
			//System.out.println(((TimeSpan) pairs.getKey()).StartTime() + ":" + ((TimeSpan) pairs.getKey()).EndTime() + " = " + pairs.getValue());
			//System.out.println(((TimeSpan) pairs.getKey()).StartTime().toString() + "\n" + ((TimeSpan) pairs.getKey()).EndTime().toString());
			if(((TimeSpan) pairs.getKey()).myOverlap(d)) {
				tempAppts.add((Appt) pairs.getValue());
				//System.out.println("I am here");
			}
			else if(((Appt) pairs.getValue()).getType() == 1 && ((Appt) pairs.getValue()).TimeSpan().StartTime().before(d.StartTime())) {
				tempAppts.add((Appt) pairs.getValue());
			}else if(((Appt) pairs.getValue()).getType() == 2 && ((Appt) pairs.getValue()).TimeSpan().StartTime().getDay() == d.StartTime().getDay()) {
				tempAppts.add((Appt) pairs.getValue());
			}else if(((Appt) pairs.getValue()).getType() == 3 && ((Appt) pairs.getValue()).TimeSpan().StartTime().getDate() == d.StartTime().getDate()) {
				tempAppts.add((Appt) pairs.getValue());
			}
			//it.remove();
		}
	
		Appt tempArr[] = new Appt[tempAppts.size()];
		for(int i =0; i<tempAppts.size(); i++) {
			tempArr[i] = tempAppts.get(i);
			//System.out.println("getting here");
			//System.out.println(tempArr[i].getInfo());
		}
		return tempArr;
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
		
	}

}
