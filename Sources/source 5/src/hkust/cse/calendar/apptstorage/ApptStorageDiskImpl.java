package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeMachine;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.user.User;
import hkust.cse.calendar.unit.user.UserManagement;

import java.sql.Timestamp;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;



public class ApptStorageDiskImpl extends ApptStorage {

	private User defaultUser = null;
	//private TimeMachine timeMachine = null;
	
	public ApptStorageDiskImpl( User user )
	{
		defaultUser = user;
		mAssignedApptID = 1;
		mAppts = new HashMap<Integer, Appt>();
		_locations = new Location[0];
		xstream = new XStream();
	}
	
	/*public void setTimeMachine(TimeMachine machine) {
		this.timeMachine = machine;
	}*/
	
	public Location[] getLocationList() {
		return _locations;
	}
	
	public void setLocationList(Location[] locations) {
		_locations = locations.clone();
	}

	@Override
	public void SaveAppt(Appt appt) {
		// TODO Auto-generated method stub
		/*if(mAppts.containsKey(appt.getID())){
			mAppts.put(appt.getID(), appt);
		}*/
		//else{
			mAppts.put(mAssignedApptID, appt);
			appt.setID(mAssignedApptID);
			mAssignedApptID++;
		//}
	}
	
	@Override
	public Appt[] RetrieveAppts(TimeSpan d) {
		// TODO Auto-generated method stub
		List<Appt> tempList = new ArrayList<Appt>();
		int apptNum = 0;
		Iterator<Map.Entry<Integer, Appt>> entries = mAppts.entrySet().iterator();
		//for(int num = 0;num < mAssignedApptID;num++){
		while(entries.hasNext()){
			Map.Entry<Integer, Appt> entry = entries.next();
			//if(mAppts.containsKey(num)){
				//Appt apptAtD = (Appt)mAppts.get(num).clone();
				Appt apptAtD = (Appt)entry.getValue().clone();
				switch(apptAtD.getFrequency()){
				case Appt.SINGLE:
					if(apptAtD.TimeSpan().Overlap(d)){
						tempList.add(apptAtD);
						apptNum++;
					}
					break;
				case Appt.DAILY:
					//check if the input time is before the schedule starting time
					//if before the schedule starting time, ignore the schedule
					if(d.EndTime().before(apptAtD.TimeSpan().StartTime())) continue;
					
					apptAtD.setTimeSpan(new TimeSpan(
							new Timestamp(
									d.StartTime().getYear(),
									d.StartTime().getMonth(),
									d.StartTime().getDate(),
									apptAtD.TimeSpan().StartTime().getHours(),
									apptAtD.TimeSpan().StartTime().getMinutes(),
									apptAtD.TimeSpan().StartTime().getSeconds(),
									apptAtD.TimeSpan().StartTime().getNanos()
								),
							new Timestamp(
									d.EndTime().getYear(),
									d.EndTime().getMonth(),
									d.EndTime().getDate(),
									apptAtD.TimeSpan().EndTime().getHours(),
									apptAtD.TimeSpan().EndTime().getMinutes(),
									apptAtD.TimeSpan().EndTime().getSeconds(),
									apptAtD.TimeSpan().EndTime().getNanos()
								)
							)
						);
					
					//check if the schedule is before the time machine time
					//if before the time machine time, ignore the schedule
					//if(apptAtD.TimeSpan().StartTime().before(this.timeMachine.getCurrentTime())) continue;
					if(apptAtD.TimeSpan().Overlap(d)){
						tempList.add(apptAtD);
						apptNum++;
					}
					
					break;
				case Appt.WEEKLY:
					//check if the input time is before the schedule starting time
					//if before the schedule starting time, ignore the schedule
					if(d.EndTime().before(apptAtD.TimeSpan().StartTime())) continue;
					
					if(apptAtD.TimeSpan().StartTime().getDay() == d.StartTime().getDay()){
						apptAtD.setTimeSpan(
								new TimeSpan(
										new Timestamp(
												d.StartTime().getYear(),
												d.StartTime().getMonth(),
												d.StartTime().getDate(),
												apptAtD.TimeSpan().StartTime().getHours(),
												apptAtD.TimeSpan().StartTime().getMinutes(),
												apptAtD.TimeSpan().StartTime().getSeconds(),
												apptAtD.TimeSpan().StartTime().getNanos()
												),
										new Timestamp(
												d.EndTime().getYear(),
												d.EndTime().getMonth(),
												d.EndTime().getDate(),
												apptAtD.TimeSpan().EndTime().getHours(),
												apptAtD.TimeSpan().EndTime().getMinutes(),
												apptAtD.TimeSpan().EndTime().getSeconds(),
												apptAtD.TimeSpan().EndTime().getNanos()
												)
										)
								);
						
						//check if the schedule is before the time machine time
						//if before the time machine time, ignore the schedule
						//if(apptAtD.TimeSpan().StartTime().before(this.timeMachine.getCurrentTime())) continue;
						if(apptAtD.TimeSpan().Overlap(d)){
							tempList.add(apptAtD);
							apptNum++;
						}
					}
					break;
				case Appt.MONTHLY:
					//check if the input time is before the schedule starting time
					//if before the schedule starting time, ignore the schedule
					if(d.EndTime().before(apptAtD.TimeSpan().StartTime())) continue;
					
					if(apptAtD.TimeSpan().StartTime().getDate() == d.StartTime().getDate()){
						apptAtD.setTimeSpan(
								new TimeSpan(
										new Timestamp(
												d.StartTime().getYear(),
												d.StartTime().getMonth(),
												apptAtD.TimeSpan().StartTime().getDate(),
												apptAtD.TimeSpan().StartTime().getHours(),
												apptAtD.TimeSpan().StartTime().getMinutes(),
												apptAtD.TimeSpan().StartTime().getSeconds(),
												apptAtD.TimeSpan().StartTime().getNanos()
											),
										new Timestamp(
												d.EndTime().getYear(),
												d.EndTime().getMonth(),
												apptAtD.TimeSpan().EndTime().getDate(),
												apptAtD.TimeSpan().EndTime().getHours(),
												apptAtD.TimeSpan().EndTime().getMinutes(),
												apptAtD.TimeSpan().EndTime().getSeconds(),
												apptAtD.TimeSpan().EndTime().getNanos()
											)
									)
								);
						
						//check if the schedule is before the time machine time
						//if before the time machine time, ignore the schedule
						//if(apptAtD.TimeSpan().StartTime().before(this.timeMachine.getCurrentTime())) continue;
						if(apptAtD.TimeSpan().Overlap(d)){
							tempList.add(apptAtD);
							apptNum++;
						}
					}
					break;
				//}
			}
		}
			Appt[] timeAppt = new Appt[apptNum];
			tempList.toArray(timeAppt);
			
			return timeAppt;
	}
	
	@Override
	public Appt[] RetrieveAppts(TimeSpan d, int f) {
		
		Appt[] appts = RetrieveAppts(d);
		ArrayList<Appt> newappts = new ArrayList<Appt>();
		
		for(Appt appt : appts) {
			if(appt.getFrequency() == f) {
				newappts.add(appt);
			}
		}
		
		appts = new Appt[newappts.size()];
		return newappts.toArray(appts);
		
		/*
		// TODO Auto-generated method stub
		List<Appt> tempList = new ArrayList<Appt>();
		int apptNum = 0;
		//for(int num = 0;num < mAssignedApptID;num++){
			//if(mAppts.containsKey(num)){		
		Iterator<Map.Entry<Integer, Appt>> entries = mAppts.entrySet().iterator();
		while(entries.hasNext()){
			Map.Entry<Integer, Appt> entry = entries.next();
				Appt apptAtD = entry.getValue();
				//Appt apptAtD = (Appt)mAppts.get(num);
				int apptsLen = 0;
				
				switch(f) {
					case Appt.SINGLE:
						apptsLen = RetrieveAppts(d).length;
					break;
					case Appt.DAILY:
						apptsLen = RetrieveAppts(
								new TimeSpan(
									new Timestamp(
											apptAtD.TimeSpan().StartTime().getYear(),
											apptAtD.TimeSpan().StartTime().getMonth(),
											apptAtD.TimeSpan().StartTime().getDate(),
											d.StartTime().getHours(),
											d.StartTime().getMinutes(),
											0,
											0),
									new Timestamp(
											apptAtD.TimeSpan().EndTime().getYear(),
											apptAtD.TimeSpan().EndTime().getMonth(),
											apptAtD.TimeSpan().EndTime().getDate(),
											d.EndTime().getHours(),
											d.EndTime().getMinutes(),
											0,
											0)
								)).length;
					break;
					case Appt.WEEKLY:
						if(apptAtD.TimeSpan().StartTime().getDay() == d.StartTime().getDay())
							apptsLen = RetrieveAppts(
									new TimeSpan(
										new Timestamp(
												apptAtD.TimeSpan().StartTime().getYear(),
												apptAtD.TimeSpan().StartTime().getMonth(),
												apptAtD.TimeSpan().StartTime().getDate(),
												d.StartTime().getHours(),
												d.StartTime().getMinutes(),
												0,
												0),
										new Timestamp(
												apptAtD.TimeSpan().EndTime().getYear(),
												apptAtD.TimeSpan().EndTime().getMonth(),
												apptAtD.TimeSpan().EndTime().getDate(),
												d.EndTime().getHours(),
												d.EndTime().getMinutes(),
												0,
												0)
									)).length;
					break;
					case Appt.MONTHLY:
						if(apptAtD.TimeSpan().StartTime().getDate() == d.StartTime().getDate())
							apptsLen = RetrieveAppts(
									new TimeSpan(
										new Timestamp(
												apptAtD.TimeSpan().StartTime().getYear(),
												apptAtD.TimeSpan().StartTime().getMonth(),
												d.StartTime().getDate(),
												d.StartTime().getHours(),
												d.StartTime().getMinutes(),
												0,
												0),
										new Timestamp(
												apptAtD.TimeSpan().EndTime().getYear(),
												apptAtD.TimeSpan().EndTime().getMonth(),
												d.EndTime().getDate(),
												d.EndTime().getHours(),
												d.EndTime().getMinutes(),
												0,
												0)
									)).length;
					break;
				}
				if(apptsLen > 0) {
					tempList.add(apptAtD);
					apptNum++;
				}
			//}
		}
			Appt[] timeAppt = new Appt[apptNum];
			tempList.toArray(timeAppt);
			return timeAppt;
			*/
	}

	@Override
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		// TODO Auto-generated method stub
		Appt[] tempList=RetrieveAppts(time);
		List<Appt> tempList2 = new ArrayList<Appt>();
		int userApptNum=0;
		for(int i=0;i<tempList.length;i++){
			if(tempList[i].getAllPeople().contains(entity.ID())){
				userApptNum++;
				tempList2.add(tempList[i]);
			}
		}
		if(userApptNum!=0){
			Appt[] outApptList=new Appt[userApptNum];
			tempList2.toArray(outApptList);
			return outApptList;
		}
		else{
			Appt[] outApptList=new Appt[0];
			return outApptList;
		}
	}

	@Override
	public Appt[] RetrieveAppts(Location location, TimeSpan time) {
		// TODO Auto-generated method stub
		Appt[] tempList=RetrieveAppts(time);
		List<Appt> tempList2 = new ArrayList<Appt>();
		int userApptNum=0;
		for(int i=0;i<tempList.length;i++){
			if(tempList[i].getLocation().getName().equals(location.getName())){
				userApptNum++;
				tempList2.add(tempList[i]);
			}
		}
		if(userApptNum!=0){
			Appt[] outApptList=new Appt[userApptNum];
			tempList2.toArray(outApptList);
			return outApptList;
		}
		else{
			Appt[] outApptList=new Appt[0];
			return outApptList;
		}
	}
	
	@Override
	public Appt RetrieveAppts(int joinApptID) {
		// TODO Auto-generated method stub
		if(mAppts.containsKey(joinApptID))
			return (Appt)mAppts.get(joinApptID);
		else
			return null;
	}

	@Override
	public void UpdateAppt(Appt appt) {
		// TODO Auto-generated method stub
			mAppts.put(appt.getID(), appt);
	}

	@Override
	public void RemoveAppt(Appt appt) {
		// TODO Auto-generated method stub
		mAppts.remove(appt.getID());
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
			File f = new File("Apptfile.xml");
			if (f.exists() && f.isFile()){
				mAppts = (HashMap<Integer, Appt>) xstream.fromXML(f);
				Iterator<Map.Entry<Integer, Appt>> IDloop = mAppts.entrySet().iterator();
				int temp = 0;
				while(IDloop.hasNext()){
					Map.Entry<Integer, Appt> IDtemp = IDloop.next();
						temp = IDtemp.getKey();
				}
				mAssignedApptID = temp+1;
			} 
		}catch(Exception e){
			System.err.println("Error in XML Read: " + e.getMessage());
		}
	}

	@Override
	public void PutApptToXml() {
		// TODO Auto-generated method stub
		try {
			xstream.toXML(mAppts, new FileWriter("Apptfile.xml"));
		} catch (IOException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void LoadLocFromXml() {
		// TODO Auto-generated method stub
		try{	
			File f = new File("Locfile.xml");
			if (f.exists() && f.isFile()){
				_locations = (Location[]) xstream.fromXML(f);
			}
		}catch(Exception e){
			System.err.println("Error in XML Read: " + e.getMessage());
		}
	}
	
	@Override
	public void PutLocToXml() {
		// TODO Auto-generated method stub
		try {
			xstream.toXML(_locations, new FileWriter("Locfile.xml"));
		} catch (IOException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public UserManagement LoadUserFromXml() {
		// TODO Auto-generated method stub
		try{	
			File f = new File("Userfile.xml");
			if (f.exists() && f.isFile()){
				return (UserManagement) xstream.fromXML(f);
			}
		}catch(Exception e){
			System.err.println("Error in XML Read: " + e.getMessage());
		}
		return null;
	}
	
	@Override
	public void PutUserToXml() {
		// TODO Auto-generated method stub
		try {
			xstream.toXML(UserManagement.getInstance(), new FileWriter("Userfile.xml"));
		} catch (IOException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
