package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JOptionPane;

public class reminderThread extends Thread{
	private User runningUser;
	
	public reminderThread(User user){
		runningUser = user;
	}
	
	
	public boolean compare(int index, TimeSpan startTime, int curHour, int curMin){
		int minutes=startTime.StartTime().getMinutes();
		int hours=startTime.StartTime().getHours();
		if(index==2){
			minutes-=15;
		}
		else if(index==3){
			minutes-=30;
		}
		else if(index==4){
			hours-=1;
		}
		else if(index==5){
			hours-=2;
		}
		
		if(minutes<0){
			hours-=1;
			minutes=Math.abs(minutes);
			minutes=60-minutes;
		}
		
		if(hours==curHour&&minutes==curMin){
			return true;
		}
		else
			return false;
	}
	private Date checkDate;
	@Override
	public void run(){
		//boolean dailyDone=false;
		checkDate = new Date();
		int checkSec = checkDate.getSeconds(); // return the current minutes
		//JOptionPane.showMessageDialog(null, "Current time= " + checkDate.getHours() + ":" + checkDate.getMinutes() + '\n' + "remaining time= " + (((checkMin/15 + 1)*15) - checkMin));
		if(checkSec != 0){
			//int temp = (((checkMin/15 + 1)*15) - checkMin); // Farhad�s algorithm
			try{
				sleep(((60-checkSec)*1000));
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		try{
			checkDate = new Date();
			int curMinute=checkDate.getMinutes();
			while(true){
				//checkDate = new Date();
				//curMinute = checkDate.getMinutes();
				//System.out.println("HI I AM THE REMINDER THREAD");
				//JOptionPane.showMessageDialog(null, "Current time= " + checkDate.getHours() + ":" + checkDate.getMinutes());
				Iterator it = null;
				try{
				it = ApptStorage.mAppts.entrySet().iterator();
				}catch(Exception e){ // try to handle the exception
					System.out.println("======================================Thread exception is " + e.getMessage() + "========================================");
					
					
					synchronized (reminderThread.class){
						it = null;
						it = ApptStorage.mAppts.entrySet().iterator();
					}
				}
				if(it == null){return;}
				while (it.hasNext()) {
			        Map.Entry pairs = (Map.Entry)it.next();
			        
			        if(((Appt) pairs.getValue()).isJoint() == true && ((Appt) pairs.getValue()).getIsConfirmed() == true && ((Appt) pairs.getValue()).getAttendList().contains(runningUser.ID()) == true){
			        	System.out.println("===================This if statments works for thread who blab bl;a" + runningUser.ID());
			        }
			        
			        
			        
			        /*added by Justin*/
			        if((((Appt) pairs.getValue()).isJoint() == false && ((Appt) pairs.getValue()).getUserName().equals(runningUser.ID()) == true) || (
			        		((Appt) pairs.getValue()).isJoint() == true && ((Appt) pairs.getValue()).getIsConfirmed() == true && ((Appt) pairs.getValue()).getUserName().equals(runningUser.ID()) == true) || 
			        	(	((Appt) pairs.getValue()).isJoint() == true && ((Appt) pairs.getValue()).getIsConfirmed() == true && ((Appt) pairs.getValue()).getAttendList().contains(runningUser.ID()) == true)){
			        /*up to here by Justin*/	
			        //System.out.println("Reminder = " + ((Appt) pairs.getValue()).getReminder());  //debugging check
			        //if(((Appt) pairs.getValue()).getReminder()==1){
			        	if(((Appt) pairs.getValue()).getType()==0){
			        		if(((Appt) pairs.getValue()).getDone()==false){
				        		if(((TimeSpan) pairs.getKey()).StartTime().getYear()==checkDate.getYear()&&((TimeSpan) pairs.getKey()).StartTime().getMonth()==checkDate.getMonth()&&((TimeSpan) pairs.getKey()).StartTime().getDate()==checkDate.getDate()&&compare(((Appt) pairs.getValue()).getReminder(),((TimeSpan) pairs.getKey()),checkDate.getHours(),curMinute)){
				        			JOptionPane.showMessageDialog(null, "Title: " + ((Appt) pairs.getValue()).getTitle() + '\n' + "Timespan: " + ((TimeSpan) pairs.getKey()).StartTime() + " - " + ((TimeSpan) pairs.getKey()).EndTime() + '\n' + "Location: " + ((Appt) pairs.getValue()).getLocation().toString() + "Description: " + ((Appt) pairs.getValue()).getInfo());
				        			((Appt) pairs.getValue()).setDone(true);
				        		}
			        		}
			        	}
			        	else if(((Appt) pairs.getValue()).getType()==1){
			        		if(((Appt) pairs.getValue()).getDone()==false){
			        			if(((TimeSpan) pairs.getKey()).StartTime().getYear()==checkDate.getYear()&&((TimeSpan) pairs.getKey()).StartTime().getMonth()==checkDate.getMonth()&&((TimeSpan) pairs.getKey()).StartTime().getDate()==checkDate.getDate()&&compare(((Appt) pairs.getValue()).getReminder(),((TimeSpan) pairs.getKey()),checkDate.getHours(),curMinute)){
			        				JOptionPane.showMessageDialog(null, "Title: " + ((Appt) pairs.getValue()).getTitle() + '\n' + "Timespan: " + ((TimeSpan) pairs.getKey()).StartTime() + " - " + ((TimeSpan) pairs.getKey()).EndTime() + '\n' + "Location: " + ((Appt) pairs.getValue()).getLocation().toString() + "Description: " + ((Appt) pairs.getValue()).getInfo());
			        				((Appt) pairs.getValue()).setDone(true);
			        			}
			        		}
			        		else{
			        			if(compare(((Appt) pairs.getValue()).getReminder(),((TimeSpan) pairs.getKey()),checkDate.getHours(),curMinute)){
			        				JOptionPane.showMessageDialog(null, "Title: " + ((Appt) pairs.getValue()).getTitle() + '\n' + "Timespan: " + ((TimeSpan) pairs.getKey()).StartTime() + " - " + ((TimeSpan) pairs.getKey()).EndTime() + '\n' + "Location: " + ((Appt) pairs.getValue()).getLocation().toString() + "Description: " + ((Appt) pairs.getValue()).getInfo());
			        			}
			        		}
			        	}
			        	else if(((Appt) pairs.getValue()).getType()==2){
			        		if(((Appt) pairs.getValue()).getDone()==false){
			        			if(((TimeSpan) pairs.getKey()).StartTime().getYear()==checkDate.getYear()&&((TimeSpan) pairs.getKey()).StartTime().getMonth()==checkDate.getMonth()&&((TimeSpan) pairs.getKey()).StartTime().getDate()==checkDate.getDate()&&compare(((Appt) pairs.getValue()).getReminder(),((TimeSpan) pairs.getKey()),checkDate.getHours(),curMinute)){
				        			JOptionPane.showMessageDialog(null, "Title: " + ((Appt) pairs.getValue()).getTitle() + '\n' + "Timespan: " + ((TimeSpan) pairs.getKey()).StartTime() + " - " + ((TimeSpan) pairs.getKey()).EndTime() + '\n' + "Location: " + ((Appt) pairs.getValue()).getLocation().toString() + "Description: " + ((Appt) pairs.getValue()).getInfo());
				        			((Appt) pairs.getValue()).setDone(true);
				        		}
			        		}
			        		else{
			        			if(((TimeSpan) pairs.getKey()).StartTime().getDay()==checkDate.getDay()&&compare(((Appt) pairs.getValue()).getReminder(),((TimeSpan) pairs.getKey()),checkDate.getHours(),curMinute)){
			        				JOptionPane.showMessageDialog(null, "Title: " + ((Appt) pairs.getValue()).getTitle() + '\n' + "Timespan: " + ((TimeSpan) pairs.getKey()).StartTime() + " - " + ((TimeSpan) pairs.getKey()).EndTime() + '\n' + "Location: " + ((Appt) pairs.getValue()).getLocation().toString() + "Description: " + ((Appt) pairs.getValue()).getInfo());
			        			}
			        		}
			        	}
			        	else if(((Appt) pairs.getValue()).getType()==3){
			        		if(((Appt) pairs.getValue()).getDone()==false){
			        			if(((TimeSpan) pairs.getKey()).StartTime().getYear()==checkDate.getYear()&&((TimeSpan) pairs.getKey()).StartTime().getMonth()==checkDate.getMonth()&&((TimeSpan) pairs.getKey()).StartTime().getDate()==checkDate.getDate()&&compare(((Appt) pairs.getValue()).getReminder(),((TimeSpan) pairs.getKey()),checkDate.getHours(),curMinute)){
				        			JOptionPane.showMessageDialog(null, "Title: " + ((Appt) pairs.getValue()).getTitle() + '\n' + "Timespan: " + ((TimeSpan) pairs.getKey()).StartTime() + " - " + ((TimeSpan) pairs.getKey()).EndTime() + '\n' + "Location: " + ((Appt) pairs.getValue()).getLocation().toString() + "Description: " + ((Appt) pairs.getValue()).getInfo());
				        			((Appt) pairs.getValue()).setDone(true);
				        		}
			        		}
			        		else{
			        			if(((TimeSpan) pairs.getKey()).StartTime().getDate()==checkDate.getDate()&&compare(((Appt) pairs.getValue()).getReminder(),((TimeSpan) pairs.getKey()),checkDate.getHours(),curMinute)){
			        				JOptionPane.showMessageDialog(null, "Title: " + ((Appt) pairs.getValue()).getTitle() + '\n' + "Timespan: " + ((TimeSpan) pairs.getKey()).StartTime() + " - " + ((TimeSpan) pairs.getKey()).EndTime() + '\n' + "Location: " + ((Appt) pairs.getValue()).getLocation().toString() + "Description: " + ((Appt) pairs.getValue()).getInfo());
			        			}
			        		}
			        	}
			        	
			        //}
			        	
			        
			        }			        
			    }
				while(curMinute==checkDate.getMinutes()){
					sleep(3000);
					checkDate=new Date();
				}
				curMinute=checkDate.getMinutes();
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
	}
}
