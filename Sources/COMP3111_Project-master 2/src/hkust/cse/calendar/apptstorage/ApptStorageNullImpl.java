package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.locationstorage.LocationStorageController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserStorageController;
import hkust.cse.calendar.xmlfactory.ApptXmlFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

//import sun.org.mozilla.javascript.internal.ast.Assignment; ???

public class ApptStorageNullImpl extends ApptStorage {

	public ApptStorageNullImpl(User user)
	{
		defaultUser = user;
		mAppts = new HashMap<TimeSpan, Appt>();
		mAssignedApptID = 0;
		mAssignedJointID = 0;
		apptXmlFactory = new ApptXmlFactory();
		userStorage = UserStorageController.getInstance();

		loadApptFromXml(user, mAppts);
		mUserToAppts.put(defaultUser, mAppts);

		ArrayList<Appt> apptlist = new ArrayList<Appt>(); // store the related joint appts from other users

		User[] users = userStorage.retrieveUsers();
		int topid=0;
		for(User u : users){
			if(u == defaultUser) continue;
			HashMap<TimeSpan, Appt> appts = new HashMap<TimeSpan, Appt> ();
			loadApptFromXml(u, appts);
			mUserToAppts.put(u, appts);
			loadJointAppts(appts,apptlist);

		}
		int id[] = getMaxIDs();
		setAssignedJointID(id[1]+1);

		//ApptStorage.mUserToAppts.put(defaultUser, mAppts);

 		// add the related joint appt from other users to mAppt
		// add the related joint appt from other users to mAppt
		for(Appt appt : apptlist){
			mAppts.put(appt.TimeSpan(),appt);
		}

	}

	@Override
	public void SaveAppt(Appt appt) {
		mAppts.put(appt.TimeSpan(),appt);
	}

	@Override
	public boolean checkOverlap(Appt appt,Appt entry){

		// if the user is in waiting list, don't check that appt
		//if(isCurrUserInTheList(defaultUser.ID(),entry.getWaitingList())) return false; 

		if(appt.TimeSpan().Overlap(entry.TimeSpan())){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean checkOverLaps(ArrayList<Appt> appts){
		// for non-joint appt - only check overlap with all appts of that user
		for (Appt appt: appts) {

				for (Entry<TimeSpan, Appt> entry : mAppts.entrySet()) {

					// if it is an old appt, then don't check overlap
					if(appt.getID() == entry.getValue().getID()) continue;
					// if overlap, then return true. otherwise, check next appt
					if(checkOverlap(appt,entry.getValue())){
						return true;
					}

				}	
				if(appt.isJoint()){
				// for joint appt - check overlap will all appts in the list
				
					for(String people : appt.getAllPeople()){
						HashMap<TimeSpan,Appt> apptslist = mUserToAppts.get(UserStorageController.getInstance().getUserById(people));
						for(Appt mAppt : apptslist.values()){
	
							// if the joint appointment owner modified his appt, don't check itself 
							if(appt.isJoint() && (mAppt.getJoinID()==appt.getJoinID() )) continue;
	
							// Otherwise, check if the time overlapped
							if( mAppt.TimeSpan().Overlap(appt.TimeSpan())) {
								return true;
							}
						}
					}
					
				}

		}


		return false;


	}


	@Override
	public Appt[] RetrieveAppts(TimeSpan d) {
		ArrayList<Appt> temp = new ArrayList<Appt>();

		for (Entry<TimeSpan, Appt> entry : mAppts.entrySet()) {
			// if the user is in waiting list, don't retrieve that appt
			if(	isCurrUserInTheList(defaultUser.ID(),entry.getValue().getWaitingList()) ||	isCurrUserInTheList(defaultUser.ID(),entry.getValue().getRejectList())) continue; 
			// if overlap, then retrieve that appt
			if (d.Overlap(entry.getKey()))
				temp.add(entry.getValue());			
		}

		return temp.toArray(new Appt[temp.size()]);
	}

	@Override
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		// TODO Auto-generated method stub
		//defaultUser = entity;
		ArrayList<Appt> appts = new ArrayList<Appt>();

		for(Entry<TimeSpan, Appt> entry : mAppts.entrySet()) {
			// if the user is in waiting list, don't retrieve that appt
			if(isCurrUserInTheList(defaultUser.ID(), entry.getValue().getWaitingList()) || isCurrUserInTheList(defaultUser.ID(),entry.getValue().getRejectList()) || entry.getValue().getWaitingList().size() > 0 || entry.getValue().getRejectList().size() > 0) 
				continue; 
			// if overlap, then retrieve that appt
			if (time.Overlap(entry.getKey()))
				appts.add(entry.getValue());			
		}

		for(Entry<User,HashMap<TimeSpan, Appt>> entry : mUserToAppts.entrySet()) {
			if(!entry.getKey().ID().equals(defaultUser.ID())) {
				for(Entry<TimeSpan, Appt> appt : entry.getValue().entrySet()) {
					if (appt.getValue().isPublic() && time.Overlap(appt.getKey()) && appt.getValue().getWaitingList().size() == 0 && appt.getValue().getRejectList().size() == 0)
						appts.add(appt.getValue());
				}
			}
		}

		return appts.toArray(new Appt[appts.size()]);
	}

	public Appt[] RetrieveAppts2(User entity, TimeSpan time) {
		// TODO Auto-generated method stub
		//defaultUser = entity;
		ArrayList<Appt> appts = new ArrayList<Appt>();

		HashMap<TimeSpan, Appt> userAppts = mUserToAppts.get(entity);
		for(Appt appt : userAppts.values()) {
			if(time.Overlap(appt.TimeSpan())) {
				appts.add(appt);
			}

		}

		return appts.toArray(new Appt[appts.size()]);
	}


	@Override
	public Appt[] RetrieveJointApptsInWaitlist() {
		ArrayList<Appt> temp = new ArrayList<Appt>();
		for(Iterator<Entry<TimeSpan, Appt>>it=mAppts.entrySet().iterator();it.hasNext();){
			Entry<TimeSpan, Appt> entry = it.next();
			Appt appt = entry.getValue();
			if(appt.isJoint() && isCurrUserInTheList(defaultUser.ID(), appt.getWaitingList())){
				temp.add(appt);
			}	

		}
		return temp.toArray(new Appt[temp.size()]);

	}	


	@Override
	public Appt[] RetrieveAppts(User user, int joinApptID) {
		ArrayList<Appt> temp = new ArrayList<Appt>();
		HashMap<TimeSpan,Appt>appts = ApptStorage.mUserToAppts.get(user);
		for(Iterator<Entry<TimeSpan, Appt>>it=appts.entrySet().iterator();it.hasNext();){
			Entry<TimeSpan, Appt> entry = it.next();
			Appt appt = entry.getValue();
			if(joinApptID == appt.getJoinID()){
				temp.add(appt);
			}	

		}

		return temp.toArray(new Appt[temp.size()]);
	}

	@Override
	public void UpdateAppt(Appt appt) {
		// must use the iterator because once you remove an appt, the date of appt.getValue() contain the deleted appt so that it will crash the system
		for(Iterator<Entry<TimeSpan, Appt>>it=mAppts.entrySet().iterator();it.hasNext();){
			Entry<TimeSpan, Appt> entry = it.next();
			Appt oldAppt = entry.getValue();
			if(oldAppt.getID() == appt.getID()){
				it.remove();
				mAppts.put(appt.TimeSpan(),appt);
			}	

		}

	}

	@Override
	public void RemoveAppt(Appt appt) {
		// must use the iterator because once you remove an appt, the date of appt.getValue() contain the deleted appt so that it will crash the system
		//removeApptFromXml(appt); 
		mAppts.remove(appt.TimeSpan());
		for(Iterator<Entry<TimeSpan, Appt>>it = mAppts.entrySet().iterator(); it.hasNext();){
			Entry<TimeSpan, Appt> entry = it.next();
			if(entry.getValue().getID() == appt.getID()){
				it.remove();
			}		
		}
	}

	@Override
	public User getDefaultUser() {
		// TODO Auto-generated method stub
		return defaultUser;
	}

	/* begining of xml management functions*/
	@Override
	public void loadApptFromXml(User user, HashMap<TimeSpan,Appt> appts) {
		int id = apptXmlFactory.loadApptFromXml(ApptStorage.apptFile, appts, user.ID());
		// if we are loading the current user's appt, then we set mAssignedApptID equal to the largest appt id +1 of the user
		if(user.ID().equals(defaultUser.ID())) 
			mAssignedApptID = id;
	}

	@Override
	public void saveApptToXml(Appt appt) {
		apptXmlFactory.saveApptToXml(ApptStorage.apptFile, appt, defaultUser.ID());
	}
	@Override
	public void removeApptFromXml(Appt appt) {
		apptXmlFactory.removeApptFromXml(ApptStorage.apptFile, appt, defaultUser.ID());
	}
	/* end of xml management functions*/

	@Override // added a method to get the assigned appt id
	public int getAssignedApptID(){
		return mAssignedApptID;
	}
	@Override
	public void setAssignedApptID(int id) {
		mAssignedApptID = id;
	}
	@Override 
	public int getAssignedJointID(){
		return mAssignedJointID;
	}
	@Override
	public void setAssignedJointID(int id) {
		mAssignedJointID = id;
	}
	@Override
	public boolean checkApptsHaveLocation(String locationName) {
		// TODO Auto-generated method stub
		for(HashMap<TimeSpan, Appt> apptMap: mUserToAppts.values()) {
			for(Appt appt : apptMap.values()) {
				if(appt.getLocation().getLocationName().equals(locationName)) {
					return true;
				}
			}
		}

		return false;

		/*for(Appt mAppt : mAppts.values()) {
			if(mAppt.getLocation().getLocationName().equals(locationName)) {
				return true;
			}
		}
		return false;*/
	}

	@Override
	public boolean checkotherApptsHaveLocation(Appt appt, String locationName) {

		for(Entry<User, HashMap<TimeSpan, Appt>> apptslistEntry : mUserToAppts.entrySet()){
			HashMap<TimeSpan,Appt> apptslist = apptslistEntry.getValue(); 
			for(Appt mAppt : apptslist.values()) {

				
				// if the joint appointment owner modified his appt, don't check it 
				if(appt.isJoint() && !isCurrUserInTheList(defaultUser.ID(), mAppt.getAllPeople()) ) continue;
				else if (mAppt.getID()==appt.getID()) continue;

				// modified: non-joint to joint, joint to joint, don't check itself
				if((appt.isJoint() && (mAppt.getJoinID()==appt.getJoinID()||(mAppt.getOwner() == defaultUser && mAppt.getID()==appt.getID())))) continue;
				// modified: non-joint to non-joint don't check itself 
				if(!appt.isJoint() && mAppt.getOwner() == defaultUser && mAppt.getID()==appt.getID()) continue;

//				System.out.println(appt.TimeSpan());
//				System.out.println(locationName);
//				System.out.println(mAppt.TimeSpan());
//				System.out.println(mAppt.getLocation().toString());
//				System.out.println("-----");
				
				// Otherwise, check if the locations are same or time overlapped
				boolean isSameLocation = mAppt.getLocation().toString().equals(locationName);
				boolean isOverLap = mAppt.TimeSpan().Overlap(appt.TimeSpan());
				//System.out.println(mAppt.getTitle()+" "+mAppt.TimeSpan().StartTime()+" "+appt.TimeSpan().StartTime()+" "+isOverLap);
//				System.out.println(isSameLocation);
//				System.out.println(isOverLap);
//				System.out.println("----------");
				if(isSameLocation && isOverLap) {
					return true;
				}
			}	
		}

		return false;
	}

	/* Tempory method to get all the appts for the admin appts
	 * Later will change it to get the specifi
	 * */

	@Override
	public Appt[] retrieveAllAppts(User user) {
		// TODO Auto-generated method stub
		ArrayList<Appt> appts = new ArrayList<Appt>();
		HashMap<TimeSpan, Appt> userAppt = ApptStorage.mUserToAppts.get(user);
		ArrayList<Integer> addedAppt = new ArrayList<Integer>();

		if(userAppt == null) {
			return null;
		}

		for(Appt appt : userAppt.values()) {
			boolean added = false;			

			for(int i = 0; i < addedAppt.size(); i++) {
				if(addedAppt.get(i).intValue() == appt.getID()) {
					added = true;
					break;
				}
			}

			if(!added) {
				addedAppt.add(new Integer(appt.getID()));
				//				addedAppt.sort(null);
				appts.add(appt);
			}
		}
		/*
		appts.sort(new Comparator<Appt>() {
			@Override
			public int compare(Appt o1, Appt o2) {
				// TODO Auto-generated method stub
				return (int) (o1.getStartDate().StartTime().getTime() - o2.getStartDate().StartTime().getTime());
			}
		});
		 */
		return appts.toArray(new Appt[appts.size()]);
	}

	@Override
	public boolean checkotherUsersTimespan(TimeSpan suggestedTimeSpan, User[] users){
		for(User user : users) {
			Appt[] apptsOfUser = RetrieveAppts2(user, suggestedTimeSpan);
			if(apptsOfUser.length != 0) {
				return false;
			}
		}
		return true;
	}
	// 
	//
	public void loadJointAppts(HashMap<TimeSpan, Appt> appts, ArrayList<Appt> apptlist){
		for(Iterator<Entry<TimeSpan, Appt>>it=appts.entrySet().iterator();it.hasNext();){
			Entry<TimeSpan, Appt> entry = it.next();
			Appt appt = entry.getValue();
			if(appt.isJoint() && (isCurrUserInTheList(defaultUser.ID(), appt.getAttendList()) || isCurrUserInTheList(defaultUser.ID(), appt.getWaitingList()))) {				
				Appt clone = entry.getValue().clone(entry.getValue().TimeSpan());
				clone.setID(getAssignedApptID()+1);
				apptlist.add(clone);
			}
		}
	}

	public boolean isCurrUserInTheList(String username, LinkedList<String> list){
		for(String name : list){
			if(username.equals(name)){
				return true;
			}
		}
		return false;
	}

	@Override
	public TimeSpan[] getSuggestedTimeSpan(User[] users, Timestamp stampstart) {
		//		System.out.println(users[0].ID());
		//		TimeMachine timeMachine = TimeMachine.getInstance();
		ArrayList<TimeSpan> suggestedTimeSpanList = new ArrayList<TimeSpan>();

		//		Timestamp suggestedStartTimestamp = timeMachine.getNowTimestamp();
		Timestamp suggestedStartTimestamp = stampstart;
		suggestedStartTimestamp.setMinutes((suggestedStartTimestamp.getMinutes() / 15) * 15);
		suggestedStartTimestamp.setSeconds(0);

		//		Timestamp suggestedEndTimestamp = timeMachine.getNowTimestamp();
		Timestamp suggestedEndTimestamp = new Timestamp(0);
		suggestedEndTimestamp.setYear(suggestedStartTimestamp.getYear()+1900);
		suggestedEndTimestamp.setMonth(suggestedStartTimestamp.getMonth());
		suggestedEndTimestamp.setDate(suggestedStartTimestamp.getDate());
		suggestedEndTimestamp.setHours(suggestedStartTimestamp.getHours()+1);
		// TODO may be have some problem
		suggestedEndTimestamp.setMinutes((suggestedStartTimestamp.getMinutes() / 15) * 15);
		suggestedEndTimestamp.setSeconds(0);

		TimeSpan suggestedTimeSpan = new TimeSpan(suggestedStartTimestamp, suggestedEndTimestamp);

		if(suggestedTimeSpan.StartTime().getHours() > 17 || (suggestedTimeSpan.StartTime().getHours() == 5 && suggestedTimeSpan.StartTime().getMinutes() > 0)) {
			suggestedTimeSpan = TimeSpan.addTime(suggestedTimeSpan, TimeSpan.DAY, 1);

			suggestedTimeSpan.StartTime().setHours(8);
			suggestedTimeSpan.StartTime().setMinutes(0);
			suggestedTimeSpan.StartTime().setSeconds(0);

			suggestedTimeSpan.EndTime().setHours(9);
			suggestedTimeSpan.EndTime().setMinutes(0);
			suggestedTimeSpan.EndTime().setSeconds(0);
		}
		//		System.out.println(suggestedTimeSpan.toString());
		Appt[] appts = RetrieveAppts2(defaultUser, suggestedTimeSpan);
		while(appts.length != 0) {
			suggestedTimeSpan = TimeSpan.addTime(suggestedTimeSpan, TimeSpan.MINUTE, 15);
			appts = RetrieveAppts2(defaultUser, suggestedTimeSpan);
		}
		//		System.out.println(suggestedTimeSpan.toString());
		int i = 0;
		while(true) {
			//TODO: Generate suggested timespan
			if(checkotherUsersTimespan(suggestedTimeSpan, users)) {
				suggestedTimeSpanList.add(suggestedTimeSpan);
				//				System.out.println("added" + suggestedTimeSpan.toString());
				i++;
			}
			//			System.out.println(suggestedTimeSpan.toString());
			if(suggestedTimeSpan.StartTime().getHours() > 17 || (suggestedTimeSpan.StartTime().getHours() == 5 && suggestedTimeSpan.StartTime().getMinutes() > 0)) {
				suggestedTimeSpan = TimeSpan.addTime(suggestedTimeSpan, TimeSpan.DAY, 1);

				suggestedTimeSpan.StartTime().setHours(8);
				suggestedTimeSpan.StartTime().setMinutes(0);
				suggestedTimeSpan.StartTime().setSeconds(0);

				suggestedTimeSpan.EndTime().setHours(9);
				suggestedTimeSpan.EndTime().setMinutes(0);
				suggestedTimeSpan.EndTime().setSeconds(0);
			}
			//			System.out.println(suggestedTimeSpan.toString() + "beforeadd");
			suggestedTimeSpan = TimeSpan.addTime(suggestedTimeSpan, TimeSpan.MINUTE, 15);
			appts = RetrieveAppts2(defaultUser, suggestedTimeSpan);
			while(appts.length != 0) {
				suggestedTimeSpan = TimeSpan.addTime(suggestedTimeSpan, TimeSpan.MINUTE, 15);
				appts = RetrieveAppts2(defaultUser, suggestedTimeSpan);
			}
			//			System.out.println(suggestedTimeSpan.toString() + "afteradd");
			if(i == 5) {
				break;
			}
		}

		return suggestedTimeSpanList.toArray(new TimeSpan[suggestedTimeSpanList.size()]);
	} 

	public int[] getMaxIDs(){
		int[] id = new int[2];
		id[0] = -1;
		id[1] = -1;
		for(Entry<User, HashMap<TimeSpan, Appt>> apptslistEntry : mUserToAppts.entrySet()){
			HashMap<TimeSpan,Appt> apptslist = apptslistEntry.getValue(); 
			for(Appt mAppt : apptslist.values()) {
				if(id[0]<mAppt.getID())
					id[0] = mAppt.getID();
				if(id[1]<mAppt.getJoinID()) 
					id[1] = mAppt.getJoinID();
			}	
		}
		return id;
	}

	@Override
	public boolean checkLocationCapacityEnough(Appt appt) {
		int locationCapacity = appt.getLocation().getCapacity();
		int peoplesize = appt.getAllPeople().size() + 1;
//		System.out.println(peoplesize);
//		System.out.println(locationCapacity);
		if (peoplesize <= locationCapacity) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void deleteApptWithLocationName(String locationName) {
		// TODO Auto-generated method stub
		Iterator<Entry<TimeSpan, Appt>> it = mAppts.entrySet().iterator();
		while(it.hasNext()) {
			Appt appt = it.next().getValue();
			if(appt.getLocation().getLocationName().equals(locationName) && appt.getOwner().ID().equals(defaultUser.ID())) {
				it.remove();
				apptXmlFactory.deleteApptWithLocationName(defaultUser, locationName);
			}
		}
	}

	public Appt[] getApptForLocation(Location location) {
		// TODO Auto-generated method stub
		LocationStorageController locationController = LocationStorageController.getInstance();
		ArrayList<Appt> appts = new ArrayList<Appt>();
		ArrayList<Integer> addedId = new ArrayList<Integer>();

		for(Appt appt : mAppts.values()) {
				boolean added = false;

				for(Integer id : addedId) {
					if(id.intValue() == appt.getID()) {
						added = true;
						break;
					}
				}

				if(appt.getLocation().getLocationName().equals(location.getLocationName()) && !added) {
					appts.add(appt);
					addedId.add(new Integer(appt.getID()));
				}
		}

		return appts.toArray(new Appt[appts.size()]);
	}
	
	
	@Override
	public Appt[] getApptThatLocationInToBeDelete() {
		// TODO Auto-generated method stub
		LocationStorageController locationController = LocationStorageController.getInstance();
		Location[] locationsToBeDelete = locationController.getLocationInToBeDelete();
		ArrayList<Appt> appts = new ArrayList<Appt>();
		ArrayList<Integer> addedId = new ArrayList<Integer>();

		for(Appt appt : mAppts.values()) {
			for(Location location : locationsToBeDelete) {
				Appt[] ApptsInLocation = getApptForLocation(location);
				for (int i=0;i<ApptsInLocation.length;i++)
					appts.add(ApptsInLocation[i]);				
			}
		}

		return appts.toArray(new Appt[appts.size()]);
	}

}
