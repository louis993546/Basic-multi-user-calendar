package hkust.cse.calendar.unit;

import java.sql.Timestamp;
import hkust.cse.calendar.gui.CalGrid;

public class TimeMachine {
	//private GregorianCalendar mToday;
	//private Date timeAndDate;
	private static TimeMachine uniqueTimeMachine=new TimeMachine(); 
	private long offset=0; // unit ms
	private CalGrid cg;// 
	
	private TimeMachine() {
		//offset=0;
		//this.cg=cg;
	}
	
	public static TimeMachine getInstance(){
		return uniqueTimeMachine;
		
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
		long now = date.getTime();
		
		offset = endtime - now;
		
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

	public CalGrid getCg() {
		return cg;
	}

	public void setCg(CalGrid cg) {
		this.cg = cg;
	}
	
}
