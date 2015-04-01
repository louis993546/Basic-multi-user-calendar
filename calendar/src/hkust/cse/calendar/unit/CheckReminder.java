package hkust.cse.calendar.unit;

import java.sql.Timestamp;
import java.util.Date;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import javax.swing.JOptionPane;


public class CheckReminder extends Thread {
	User mCurrUser;
	Appt[] Apptlist;
	int size = 0;
	
	
    public CheckReminder(User mCurrUser, Appt[] getTodayAppt) {
    	this.mCurrUser = mCurrUser;
    	this.Apptlist = getTodayAppt;
    }


    public void run() {
	    while(true){
			try {
				Thread.sleep(50000);
				while(true){
					System.out.println("try\n");
					Thread.sleep(10000);
		    		
				}
	    	} catch (InterruptedException e) {
	            System.out.println("interrupted\n");
	        }
	    }
    }
    
    private void sort() {
    	
    }

	private void reminder() throws InterruptedException {
    	Date date= new Date();
		Timestamp reminder=new Timestamp(0);//Apptlist[0].Reminder().StartTime();
		boolean Flag = true;
    	this.size = Apptlist.length;
    	for(int k = 0; k < size;k++){
    		reminder=new Timestamp(0);//Apptlist[k].Reminder().StartTime();
    		Timestamp time = new Timestamp(date.getTime());
    		long end = reminder.getTime();
    		for(long now = date.getTime();now<end;now += 60*1000) {
    			if(now<end)
    				Thread.sleep(60*1000);
    		}
    		if(time.before(Apptlist[k].TimeSpan().StartTime()))
    			System.out.println("CheckReminder.reminder()");
//    			JOptionPane.showMessageDialog(null, "Appointment: " + Apptlist[k].getTitle() + "\nWill start on:\n" + Apptlist[k].TimeSpan().mStartTime, "Notification", JOptionPane.ERROR_MESSAGE);
    	}
    }
}
