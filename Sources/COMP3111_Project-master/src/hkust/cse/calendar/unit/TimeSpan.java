package hkust.cse.calendar.unit;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

/* This class represents the time span between two points of time */
public class TimeSpan implements Serializable {
	/* The starting time of the time span */
	private Timestamp mStartTime;
	/* The ending time of the time span */
	private Timestamp mEndTime;
	/* Remove the Appt from the storage */
	public final static int MONTH = 1;

	/* Modify the Appt the storage */
	public final static int WEEK = 2;

	/* Add a new Appt into the storage */
	public final static int DAY = 3;
	
	public final static int HOUR = 4;
	
	public final static int MINUTE = 5;
	
	/* Create a new TimeSpan object with the specific starting time and ending time */
	public TimeSpan(Timestamp start, Timestamp end) {

		if (start.getYear() >= 1900) {
			start.setYear(start.getYear() - 1900);
		}
		if (end.getYear() >= 1900) {
			end.setYear(end.getYear() - 1900);
		}
		mStartTime = start;
		mEndTime = end;
	}
	
	public TimeSpan(TimeSpan b) {

		mStartTime = new Timestamp(b.StartTime().getTime());
		mEndTime = new Timestamp(b.EndTime().getTime());
		
	}

	/* Get the starting time */
	public Timestamp StartTime() {
		return mStartTime;
	}

	/* Get the ending time */
	public Timestamp EndTime() {
		return mEndTime;
	}

	/* Check whether a time span overlaps with this time span */
	public boolean Overlap(TimeSpan d) {
		if (d.EndTime().before(mStartTime) || d.EndTime().equals(mStartTime))	// If the time span ends before or at the starting time of this time span then these two time spans do not overlap
			return false;
		if (d.StartTime().equals(mEndTime) || mEndTime.before(d.StartTime()))	// If the time span starts after or at the ending time of this time span then these two time spans do not overlap
			return false;
		return true;		// Else, the time span overlaps with this time span

	}

	/* Calculate the length of the time span if the starting time and ending time are within the same day */
	public int TimeLength() {
		
		/* return -1 if the starting time and ending time are not in the same day */
		if (mStartTime.getYear() != mEndTime.getYear())
			return -1;
		if (mStartTime.getMonth() != mEndTime.getMonth())
			return -1;
		if (mStartTime.getDay() != mEndTime.getDay())
			return -1;

		/* Calculate the number of minutes within the time span */
		int result = mStartTime.getHours() * 60 + mStartTime.getMinutes()
				- mEndTime.getHours() * 60 - mEndTime.getMinutes();
		if (result < 0)
			return -1;
		else
			return result;
	}
	
	public static TimeSpan addTime(TimeSpan s, int frequency, int num){
		
		Timestamp start = s.StartTime();
		Timestamp end = s.EndTime();
		
		Calendar c = GregorianCalendar.getInstance();
		Timestamp new_start= new Timestamp(start.getTime());
		Timestamp new_end= new Timestamp(end.getTime());
		
	    c.setTime(new_start);
	    switch(frequency){
	    case WEEK:
		    c.add(Calendar.WEEK_OF_YEAR, num);
	    	break;
	    case MONTH:
		    c.add(Calendar.MONTH, num);
	    	break;
	    case DAY:
		    c.add(Calendar.DATE, num);
	    	break;
	    case HOUR:
	    	c.add(Calendar.HOUR, num);
	    case MINUTE:
	    	c.add(Calendar.MINUTE, num);
	    }
	    
	    new_start.setTime(c.getTime().getTime());	
	    
	    c.setTime(new_end);
	    switch(frequency){
	    case WEEK:
		    c.add(Calendar.WEEK_OF_YEAR, num);
	    	break;
	    case MONTH:
		    c.add(Calendar.MONTH, num);
	    	break;
	    case DAY:
		    c.add(Calendar.DATE, num);
	    	break;
	    case HOUR:
	    	c.add(Calendar.HOUR, num);
	    case MINUTE:
	    	c.add(Calendar.MINUTE, num);
	    }

	    new_end.setTime(c.getTime().getTime());
	    
	    return new TimeSpan(new_start,new_end);
	}

	public static int getBetween(int type, TimeSpan ta, TimeSpan tb){
		switch(type){
			case DAY :
				return getDaysBetween(ta,tb);
			case WEEK :
				return getWeeksBetween(ta,tb);
			case MONTH :
				return getMonthsBetween(ta,tb);
			default:
				return 0;
		}
	}
	
	public static int getWeeksBetween (TimeSpan ta, TimeSpan tb) {

		Timestamp a = ta.StartTime();
		Timestamp b = tb.StartTime();
	    if (b.before(a)) {
	        return -getWeeksBetween(tb, ta);
	    }
	    a = resetTime(a);
	    b = resetTime(b);

	    Calendar cal = new GregorianCalendar();
	    cal.setTime(a);
	    int weeks = 0;
	    while (cal.getTime().before(b)|| cal.getTime().equals(b)) {
	        cal.add(Calendar.WEEK_OF_YEAR, 1);
	        weeks++;
	    }
	    return weeks;
	}

	public static int getDaysBetween (TimeSpan ta, TimeSpan tb) {

		Timestamp a = ta.StartTime();
		Timestamp b = tb.StartTime();
	    if (b.before(a)) {
	        return -getDaysBetween(tb, ta);
	    }
	    a = resetTime(a);
	    b = resetTime(b);

	    Calendar cal = new GregorianCalendar();
	    cal.setTime(a);
	    int years = 0;
	    while (cal.getTime().before(b)|| cal.getTime().equals(b)) {
	        cal.add(Calendar.DAY_OF_WEEK, 1);
	        years++;
	    }
	    return years;
	}

	public static int getMonthsBetween (TimeSpan ta, TimeSpan tb) {

		Timestamp a = ta.StartTime();
		Timestamp b = tb.StartTime();
	    if (b.before(a)) {
	        return -getMonthsBetween(tb, ta);
	    }
	    a = resetTime(a);
	    b = resetTime(b);

	    Calendar cal = new GregorianCalendar();
	    cal.setTime(a);
	    int months = 0;
	    while (cal.getTime().before(b) || cal.getTime().equals(b)) {
	        cal.add(Calendar.MONTH, 1);
	        months++;
	    }
	    return months;
	}

	public static boolean CheckWeek(TimeSpan ta, TimeSpan tb){
		return ta.StartTime().getDay()==tb.StartTime().getDay();
	}
	
	public static boolean CheckMonth(TimeSpan ta, TimeSpan tb){
		return ta.StartTime().getDate()==tb.StartTime().getDate();
	}
	
	public static boolean freqCheck(TimeSpan ta, TimeSpan tb){		
		TimeSpan temp = new TimeSpan(new Timestamp(tb.StartTime().getTime()),new Timestamp(tb.EndTime().getTime()));
		
		temp.StartTime().setYear(ta.StartTime().getYear());
		temp.StartTime().setMonth(ta.StartTime().getMonth());
		temp.StartTime().setDate(ta.StartTime().getDate());
		temp.EndTime().setYear(ta.EndTime().getYear());
		temp.EndTime().setMonth(ta.EndTime().getMonth());
		temp.EndTime().setDate(ta.EndTime().getDate());
			
		return ta.Overlap(temp);
		
	}
	
	public static Timestamp resetTime (Timestamp d) {
	    Calendar cal = new GregorianCalendar();
	    cal.setTime(d);
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    return new Timestamp(cal.getTimeInMillis());
	}


	/* Set the starting time */
	public void StartTime(Timestamp s) {
		mStartTime = s;
	}

	/* Set the ending time */
	public void EndTime(Timestamp e) {
		mEndTime = e;
	}
	
	public static Timestamp CreateTimeStamp(int[] date, int time) {
		Timestamp stamp = new Timestamp(0);
		stamp.setYear(date[0]);
		stamp.setMonth(date[1] - 1);
		stamp.setDate(date[2]);
		stamp.setHours(time / 60);
		stamp.setMinutes(time % 60);
		return stamp;
	}
	

	public String toString() {
		return "Start from " + mStartTime + "\t Until " + mEndTime;
	}
	
	
}
