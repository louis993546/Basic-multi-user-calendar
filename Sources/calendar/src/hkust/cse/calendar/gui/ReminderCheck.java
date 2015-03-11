package hkust.cse.calendar.gui;

import java.sql.Timestamp;
import java.util.Date;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.apptstorage.ApptStorageMemImpl;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import javax.swing.JOptionPane;

public class ReminderCheck implements Runnable {
	User mCurrUser;
	Appt[] Apptlist;
	int size = 0;
	
	
    public ReminderCheck(User mCurrUser, Appt[] getTodayAppt) {
    	this.mCurrUser = mCurrUser;
    	this.Apptlist = getTodayAppt;
    }

	@Override
    public void run() {
    	try {
    		sort();
    		reminderChecking();
    	} catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void sort() {
    	this.size = Apptlist.length;
    	for(int i=0;i<size;i++)
	    	for(int k = i; k < size;k++){
	    		if(k+1<size){
	    			Timestamp reminder=Apptlist[k].Reminder().StartTime();
	    			Timestamp reminder2=Apptlist[k+1].Reminder().StartTime();
	        		long re = reminder.getTime();
	        		long re2 = reminder2.getTime();
	    			if(re2 < re){
	    				Appt temp;
	    				temp= Apptlist[k];
	    				Apptlist[k]=Apptlist[k+1];
	    				Apptlist[k+1]=temp;
	    			}
	    		}
	    	}    	
	}

	private void reminderChecking() throws InterruptedException {
    	Date date= new Date();
		Timestamp reminder=Apptlist[0].Reminder().StartTime();
		boolean Flag = true;
    	this.size = Apptlist.length;
    	for(int k = 0; k < size;k++){
    		reminder=Apptlist[k].Reminder().StartTime();
    		Timestamp time = new Timestamp(date.getTime());
    		long end = reminder.getTime();
    		for(long now = date.getTime();now<end;now += 60*1000) {
    			if(now<end)
    				Thread.sleep(60*1000);
    		}
    		if(time.before(Apptlist[k].TimeSpan().StartTime()))
    			JOptionPane.showMessageDialog(null, "Appointment: " + Apptlist[k].getTitle() + "\nWill start on:\n" + Apptlist[k].TimeSpan().mStartTime, "Notification", JOptionPane.ERROR_MESSAGE);
    	}
    }
}
