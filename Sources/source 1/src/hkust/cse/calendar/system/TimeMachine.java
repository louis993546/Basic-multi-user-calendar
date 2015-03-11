package hkust.cse.calendar.system;

import hkust.cse.calendar.unit.TimeSpan;

import java.util.Calendar;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

import javax.swing.Timer;

public class TimeMachine {
	private static TimeMachine mTimeMachine = new TimeMachine();
	private GregorianCalendar mToday;
	private Timer mTimer;
	
	public static TimeMachine getInstance() {
		return mTimeMachine;
	}
	
	public TimeMachine() {
		mToday = new GregorianCalendar();
		mTimer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mToday.add(Calendar.SECOND, 1);
			}
		});
		mTimer.start();
	}
	
	public void setTimeMachine(int year, int month, int date, int hourOfDay, int minute, int second) {
		mTimer.stop();
		mToday.set(year, month, date, hourOfDay, minute, second);
		mTimer.start();
	}
	
	public long getTimestampOffset() {
		Date now = new Date();
		return mToday.getTimeInMillis() - now.getTime();
	}
	
	public GregorianCalendar getToday() {
		return mToday;
	}
	
	public Timestamp getNowTimestamp() {
		return new Timestamp(mToday.getTimeInMillis());
	}
	
	public TimeSpan getTodayTimeSpan() {
		int[] date = {mToday.get(Calendar.YEAR), mToday.get(Calendar.MONTH) + 1, mToday.get(Calendar.DAY_OF_MONTH)};
		Timestamp start = TimeSpan.CreateTimeStamp(date, 0);
		Timestamp end = TimeSpan.CreateTimeStamp(date, 23 * 60 + 59);
		TimeSpan todaytimeSpan = new TimeSpan(start, end);
		return todaytimeSpan;
	}
}
