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

public class TimeMachine {
	private static TimeMachine mTimeMachine = new TimeMachine();
	private GregorianCalendar mToday;

	
	public static TimeMachine getInstance() {
		return mTimeMachine;
	}
	
	public TimeMachine() {
		mToday = new GregorianCalendar();
		
	}
	
	public Timestamp getMTimestamp() {
		return new Timestamp(mToday.getTimeInMillis());
	}
	
	public GregorianCalendar getMToday() {
		return mToday;
	}
	
	public void setTimeMachine(int year, int month, int day, int hour, int minute, int second) {
		mToday.set(year, month, day, hour, minute, second);
//		System.out.println(month);
	}
}
