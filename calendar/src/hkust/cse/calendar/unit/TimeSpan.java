package hkust.cse.calendar.unit;

import java.io.Serializable;
import java.sql.Timestamp;

/* This class represents the time span between two points of time */
public class TimeSpan implements Serializable {
	/* The starting time of the time span */
	private Timestamp mStartTime;
	/* The ending time of the time span */
	private Timestamp mEndTime;

	/* Create a new TimeSpan object with the specific starting time and ending time */
	@SuppressWarnings("deprecation")
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

	public TimeSpan(int currentY, int currentM,
			int currentD, int hour, int min, int endHour, int endMin) {

		Timestamp start = new Timestamp(0);
		start.setYear(currentY);
		start.setMonth(currentM-1); //set month is 0-based, ie. 0~11
		start.setDate(currentD);
		start.setHours(hour);
		start.setMinutes(min);

		Timestamp end = new Timestamp(0);
		end.setYear(currentY);
		end.setMonth(currentM-1);
		end.setDate(currentD);
		end.setHours(endHour);
		end.setMinutes(endMin);
		
		if (start.getYear() >= 1900) {
			start.setYear(start.getYear() - 1900);
		}
		if (end.getYear() >= 1900) {
			end.setYear(end.getYear() - 1900);
		}
		mStartTime = start;
		mEndTime = end;
	}

	public String toString() {
		return 
			"\nsYear    : " + mStartTime.getYear()   +
			"\tsMonth   : " + mStartTime.getMonth()  +
			"\tsDate    : " + mStartTime.getDate()   +
			"\tsHours   : " + mStartTime.getHours()  +
			"\tsMinutes : " + mStartTime.getMinutes()+
			"\neYear    : " + mEndTime.  getYear()   +
			"\teMonth   : " + mEndTime.  getMonth()  +
			"\teDate    : " + mEndTime.  getDate()   +
			"\teHours   : " + mEndTime.  getHours()  +
			"\teMinutes : " + mEndTime.  getMinutes();
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
		System.out.println("\nTimeSpan d:     test of Overlap function"+d+"\nTimeSpan this: test of Overlap function"+this);
		if (d.EndTime().before(mStartTime) || d.EndTime().equals(mStartTime)){	// If the time span ends before or at the starting time of this time span then these two time spans do not overlap
			return false;
		}
		if (d.StartTime().equals(mEndTime) || mEndTime.before(d.StartTime())){	// If the time span starts after or at the ending time of this time span then these two time spans do not overlap
			return false;
		}
		return true;		// Else, the time span overlaps with this time span

	}

	/* Calculate the length of the time span if the starting time and ending time are within the same day */
	@SuppressWarnings("deprecation")
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

	/* Set the starting time */
	public void StartTime(Timestamp s) {
		mStartTime = s;
	}

	/* Set the ending time */
	public void EndTime(Timestamp e) {
		mEndTime = e;
	}
}
