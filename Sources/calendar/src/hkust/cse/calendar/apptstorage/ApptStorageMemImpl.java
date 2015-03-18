package hkust.cse.calendar.apptstorage;

import java.sql.Timestamp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.LocationList;
import hkust.cse.calendar.unit.Save;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.unit.Userlist;

public class ApptStorageMemImpl extends ApptStorage {

	private User defaultUser = null;
	
	Location[] _locations;
	Save _save;
	LocationList _list;
	
	public ApptStorageMemImpl( User user )
	{
		 mAppts = new HashMap<Integer,Appt>();
         //User defaultUsertmp = new User("id", "password");
         defaultUser = user;

         mAssignedApptID = 0;
	}

	public  Location[] getLocationList(){
		return _locations;
	}
	
	public void setLocationList(Location[] locations){
		_locations =locations;
	}
	@Override
	public void SaveAppt(Appt appt) {
		// TODO Auto-generated method stub
		//System.out.println("Update");
		mAssignedApptID++;
		while(mAppts.get(mAssignedApptID)!=null){
			mAssignedApptID++;
		}
        appt.setID(mAssignedApptID);
        mAppts.put(mAssignedApptID, appt);
        
        SavetoXml(mAppts);
 	}

	@Override
	public void LoadAppt(Appt appt) {
		// TODO Auto-generated method stub
		//System.out.println("Update");
		mAppts.remove(appt);
		mAssignedApptID++;
        appt.setID(mAssignedApptID);
        mAppts.put(mAssignedApptID, appt);
        
 	}

	@Override
	public Appt[] RetrieveAppts(TimeSpan d) {
		// TODO Auto-generated method stub
		Appt temp;
        Vector<Appt> v = new Vector<Appt>(10, 1);
        for (int i = 1; i <= mAssignedApptID; i++) {
                temp = (Appt) mAppts.get(i);
                if (temp != null && d.Overlap(temp.TimeSpan()))
                        v.add(temp);
        }
        if (v.size() == 0)
        	return null;
        Appt[] result = new Appt[v.size()];
        for (int j = 0; j < result.length; j++)
                result[j] = (Appt) v.elementAt(j);
        return result;
	}

	@Override
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		// TODO Auto-generated method stub
		Vector<Appt> v = new Vector<Appt>(10, 1);
		Appt[] appts = RetrieveAppts(time);
		Appt[] result = null;
		if (appts != null) {
            for (int i = 0; i < appts.length; i++) {
                    v.add(appts[i]);
            }

            if (v.size() == 0)
            	return null;

            result = new Appt[v.size()];
            for (int j = 0; j < result.length; j++)
                    result[j] = (Appt) v.elementAt(j);
		}

		return result;
	}

	@Override
	public Appt RetrieveAppts(int joinApptID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void UpdateAppt(Appt appt) {
		// TODO Auto-generated method stub
		RemoveAppt(appt);
        //SaveAppt(appt);
		mAppts.put(appt.getID(), appt);
		SavetoXml(mAppts);
	}

	@Override
	public void RemoveAppt(Appt appt) {
		// TODO Auto-generated method stub
		if (appt == null)
            return;
		mAppts.remove(appt.getID());
		
		SavetoXml(mAppts);
	}
	
	public static boolean getCurrentApptFromHashmap(Timestamp t){
		// Check the system time is equal to the appt time here. If yes, then result true.
		Appt temp;
        Vector<Appt> v = new Vector<Appt>(10, 1);
		for (int i = 1; i <= mAssignedApptID; i++) {
            temp = (Appt) mAppts.get(i);
            if (temp != null && temp.TimeSpan().StartTime()==t)
                return true;
            else
            	return false;
		}
		return false;
		
	}

	@Override
	public User getDefaultUser() {
		// TODO Auto-generated method stub
		return defaultUser;
	}

	@Override
	public void LoadApptFromXml()  {
		// TODO Auto-generated method stub
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Save.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		    Save map =(Save)jaxbUnmarshaller.unmarshal( new File("appointment.xml") );
			_save = map;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(LocationList.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		    LocationList list =(LocationList)jaxbUnmarshaller.unmarshal( new File("Location.xml") );
			_list = list;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Userlist> list=new ArrayList<Userlist>();
		list=_save.getApptlist();
		for(Userlist a:list){
			if(a.getuser().getID().equals(defaultUser.getID()))mAppts=a.getMap();
		}
	    List<Location> location = new ArrayList<Location>();
	    location = _list.getLocations();
	    int size = location.size();
	    _locations = new Location[size];
	    _locations = location.toArray(new Location[size]);
	    setLocationList(_locations);
	    
	}
	
	public void SaveLocation(Location[] temp){
		try {
			JAXBContext contextObj = JAXBContext.newInstance(LocationList.class);  
			  
			Marshaller marshallerObj = contextObj.createMarshaller();  
			marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  

			ArrayList<Location> list=new ArrayList<Location>();  
			int size = temp.length;
			for(int i = 0 ; i< size; i++){
				Location loc = new Location(temp[i].getName());  
				list.add(loc);
			}
			
			LocationList locationlist = new LocationList(list);  
			try {
				marshallerObj.marshal(locationlist, new FileOutputStream("Location.xml"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}  
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public void SavetoXml(HashMap<Integer,Appt> map){
		List<Userlist> list=new ArrayList<Userlist>();
		if(_save==null){
			Userlist a=new Userlist();
			a.setuser(defaultUser);
			a.setMap(map);
			list.add(a);
			Save s=new Save();
			s.setApptlist(list);
			_save=s;
		}else{
			list=_save.getApptlist();
			
			for(Userlist a:list){
				if(a.getuser().getID().equals(defaultUser.getID())){
					a.setMap(map);
				}
			}
			_save.setApptlist(list);
		}
		
		
        
		try {
			 
			File file = new File("appointment.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Save.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.marshal(_save, file);
		      } catch (JAXBException e) {
			e.printStackTrace();
		      }
	}

	@Override
	public Save getsaveList() {
		return _save;
	}

	@Override
	public void setsaveList(Save list) {
		_save=list;
	}
	
	public void updateUser(User user){
		List<Userlist> list=new ArrayList<Userlist>();
		list=_save.getApptlist();
		Userlist delete=new Userlist();
		
		if(user.getDeleted()==2){
			for(Userlist a:list){
				if(a.getuser().getID().equals(user.getID()))delete=a;
			}
			list.remove(delete);
		}else{
			for(Userlist a:list){
				if(a.getuser().getID().equals(user.getID())){
					a.setuser(user);
				}
			}
		}
			_save.setApptlist(list);
		try {
			 
			File file = new File("appointment.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Save.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.marshal(_save, file);
		      } catch (JAXBException e) {
			e.printStackTrace();
		      }
	}
	
	public void setDefaultUser(User user) {
		defaultUser=user;
	}
	
}


