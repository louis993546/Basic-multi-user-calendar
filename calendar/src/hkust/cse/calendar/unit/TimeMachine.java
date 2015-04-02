package hkust.cse.calendar.unit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.Timer;






import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.apptstorage.ApptStorageNullImpl;
import hkust.cse.calendar.gui.CalGrid;
import hkust.cse.calendar.unit.TimeSpan;

import java.sql.Timestamp;
import java.util.Date;



public class TimeMachine {
	//private GregorianCalendar mToday;
	//private Date timeAndDate;
	private long offset; // unit ms
	private CalGrid cg;// 
	public TimeMachine(CalGrid cg) {
		offset=0;
		this.cg=cg;
	}
	
	public Timestamp getTMTimestamp() {
		java.util.Date date= new java.util.Date();
		long now=date.getTime();
		//now+offset -> timestamp
		return new Timestamp(offset+now);
		
		
	}
	
	public void setTimeMachine(int year, int month, int day, int hour, int minute, int second) {
		//mToday.set(year, month, day, hour, minute, second){;
		
		//yyymmddhhmm -> timestamp -> gettime
		Timestamp end = new Timestamp(0);
		end.setYear(year-1900);
		end.setMonth(month-1);
		end.setDate(day);
		end.setHours(hour);
		end.setMinutes(minute);
		end.setSeconds(second);
		long endtime=end.getTime();
		
		//date() -> gettime
		java.util.Date date= new java.util.Date();
		long now=date.getTime();
		
		offset=endtime-now;
		
		this.cg.today.setTime(this.getTMTimestamp());
		this.cg.UpdateCal();

		
	}
//	public String toString(){
//		return "Time of TM : " + this.getTMTimestamp().getYear()+ "/"+
//				(this.getTMTimestamp().getMonth()+1) +"/"+
//				this.getTMTimestamp().getDay()+" "+
//				this.getTMTimestamp().getHours()+":"+
//				this.getTMTimestamp().getMinutes()+":"+
//				this.getTMTimestamp().getSeconds();
//	}
//		System.out.println(month);
	
}
