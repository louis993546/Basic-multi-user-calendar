package hkust.cse.calendar.unit;

import hkust.cse.calendar.gui.Notification;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

public class Reminder implements Serializable {
	private Timestamp mReminderTime;
	
	public Reminder() {
		mReminderTime = null;
	}
	
	public void setReminderTimestamp(Timestamp reminderTime) {
		mReminderTime = reminderTime;
	}
	
	public Timestamp getReminderTimestamp() {
		return mReminderTime;
	}

}
