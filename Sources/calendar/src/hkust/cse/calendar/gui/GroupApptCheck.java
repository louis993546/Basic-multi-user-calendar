package hkust.cse.calendar.gui;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.apptstorage.ApptStorageMemImpl;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Save;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.unit.Userlist;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class GroupApptCheck implements Runnable {
	
    public GroupApptCheck() {
    }

	@Override
    public void run() {
    	try {
    		GetAppt();
    		ApptChecking();
    	} catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
	private void GetAppt() {
		//get Group Appt
		Save _list;
		List<Userlist> ulist = new ArrayList<Userlist>();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Save.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		    Save map =(Save)jaxbUnmarshaller.unmarshal( new File("appointment.xml") );
			_list = map;
			ulist=_list.getApptlist();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* Get ALL User ID
		 * scan all waiting list
		 * put to list map
		 */
	}
	
	private void ApptChecking() throws InterruptedException {
		/*for(i = 0; i < waitlist.size; i++	)
		 * if(waitinglist.id==CurrUser)
		 * 	Start Scheduler
		 * 
		 * 
		 * 
		 */
    }
}
