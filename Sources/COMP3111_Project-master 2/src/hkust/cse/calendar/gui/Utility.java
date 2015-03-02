package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;


public class Utility {

	public static int getNumber(String s) {
		if (s == null)
			return -1;
		if (s.trim().indexOf(" ") != -1) {
			JOptionPane.showMessageDialog(null,
					"Can't Contain Whitespace In Number !");
			return -1;
		}
		int result = 0;
		try {
			result = Integer.parseInt(s);
		} catch (NumberFormatException n) {
			return -1;
		}
		return result;
	}

	public static Appt createDefaultAppt(int currentY, int currentM,
			int currentD, User me) {
		Appt newAppt = new Appt();
		//newAppt.setID(0);
		Timestamp start = new Timestamp(0);
		start.setYear(currentY);
		start.setMonth(currentM - 1);
		start.setDate(currentD);
		start.setHours(9);
		start.setMinutes(0);

		Timestamp end = new Timestamp(0);
		end.setYear(currentY);
		end.setMonth(currentM - 1);
		end.setDate(currentD);
		end.setHours(9);
		end.setMinutes(30);
		
		newAppt.setEndDate(new TimeSpan(start,end));
		newAppt.setTimeSpan(new TimeSpan(start, end));
		User[] temp = new User[1];
		temp[0] = me;
		// newAppt.setParticipants(temp);

		newAppt.setTitle("Untitled");
		//newAppt.setInfo("Input description of this appointment");
		return newAppt;
	}

	public static Appt createDefaultAppt(int currentY, int currentM,
			int currentD, User me, int startTime) {
		Appt newAppt = new Appt();
		//newAppt.setID(0);
		Timestamp start = new Timestamp(0);
		start.setYear(currentY);
		start.setMonth(currentM - 1);
		start.setDate(currentD);
		start.setHours(startTime / 60);
		start.setMinutes(startTime % 60);

		int dur = startTime + 60;
		Timestamp end = new Timestamp(0);
		end.setYear(currentY);
		end.setMonth(currentM - 1);
		end.setDate(currentD);
		end.setHours(dur / 60);
		end.setMinutes(dur % 60);

		newAppt.setLocation(new Location("", 0));
		newAppt.setEndDate(new TimeSpan(start,end));
		newAppt.setTimeSpan(new TimeSpan(start, end));
		User[] temp = new User[1];
		temp[0] = me;

		newAppt.setTitle("Untitled");
		newAppt.setInfo("Input description of this appointment");
		return newAppt;
	}
	
	
	/* 
	 * function: this function will recurively clone the appts for you, and put it back to the ArrayList that you provide
	 * @param[appt]: the original appointment 
	 * @param[frequency]: the frequency of the repeating appt, eg daily, monthly, weekly
	 * @param[applist]: the ArrayList that you want the function to give the clone appts back to you
	 * @param[reminder]: the reminder minutes of this series of appts
	 * */

	public static void createRepeatingAppts(Appt appt, int frequency, ArrayList<Appt> apptlist, int reminder){
		
		int iteration = TimeSpan.getBetween(frequency,appt.getStartDate(),appt.getEndDate());
		for(int i=1;i<iteration;i++){
			TimeSpan new_startTime = TimeSpan.addTime(appt.getStartDate(),frequency,i);
			Appt clone = appt.clone(new_startTime); 
			clone.setReminderTime(convertReminderToTimestamp(reminder,new_startTime.StartTime())); // set remainder
			apptlist.add(clone);
		}
		
	}
	
	/* 
	 * function: input a timestamp, the function will minus the reminder minutes, and then give the new timestamp to you
	 * @param[minute]: the minute before a certain timestamp 
	 * @param[start]: a certain timestamp that we want to set an offset 
	 * */
	
	public static Timestamp convertReminderToTimestamp(int minutes, Timestamp start) {
		if(minutes==-1)
			return null;
		Calendar c = GregorianCalendar.getInstance();
		Timestamp temp = start;
		if(temp.getYear()>1900)
			temp.setYear(start.getYear() - 1900);
		c.setTime(new Date(temp.getTime()));
		c.add(Calendar.MINUTE, -minutes);
		return new Timestamp(c.getTime().getTime());

	}
	
	/* 
	 * function: input an appointment, the function get back the reminder minutes, 
	 * if the reminder is null, it will return -1 
	 * */
	
	public static int reminderTimestampToMinutes(Appt appt) {
		if(appt.getReminder().getReminderTimestamp() == null) {
			return -1;
		}
		else {
			return (new TimeSpan(appt.TimeSpan().StartTime(),appt.getReminder().getReminderTimestamp()).TimeLength());
		}	
	}	

}
