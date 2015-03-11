package hkust.cse.calendar.system;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.gui.Notification;
import hkust.cse.calendar.unit.Appt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class NotificationCentre extends JFrame {
	private static NotificationCentre instance = new NotificationCentre();
	private Timer mTimer;
	private TimeMachine mTimeMachine;
	private ApptStorageControllerImpl apptController;
	
	public static NotificationCentre getInstance() {
		return instance;
	}

	private NotificationCentre() {
		mTimeMachine = TimeMachine.getInstance();
		mTimer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				popUpReminderIfNeede();
			}		
		});		
		mTimer.start();
	}

	public void setApptController(ApptStorageControllerImpl apptCon) {
		apptController = apptCon;
	}

	private void popUpReminderIfNeede() {
		Appt[] apptList = apptController.RetrieveAppts(apptController.getDefaultUser(), mTimeMachine.getTodayTimeSpan());
		
		for(Appt appt : apptList) {
			if(appt.getReminder().getReminderTimestamp() != null) {
				if(isTimesUp(appt.getReminder().getReminderTimestamp(), mTimeMachine.getNowTimestamp())) {
					String message = "";
					
					if(appt.TimeSpan().StartTime().getMinutes() == mTimeMachine.getNowTimestamp().getMinutes()) {
						message = appt.getTitle() + " will be starting now";
						new Notification(message, appt.getLocation().getLocationName());
					}
					else if(appt.TimeSpan().StartTime().after(mTimeMachine.getNowTimestamp())) {
						String minute = (appt.TimeSpan().StartTime().getMinutes() == 0) ? "00" : new Integer(appt.TimeSpan().StartTime().getMinutes()).toString();
						message = appt.getTitle() + " will be started at " + appt.TimeSpan().StartTime().getHours() + ":"
								+ minute;
						new Notification(message, appt.getLocation().getLocationName());
					}
					
					appt.setReminderTime(null);
				}
			}
		}
	}

	private boolean isTimesUp(Timestamp t1, Timestamp t2) {
		return t1.getYear() == t2.getYear() && 
				t1.getMonth() == t2.getMonth() && 
				t1.getDate() == t2.getDate() && 
				t1.getHours() == t2.getHours() && 
				t1.getMinutes() == t2.getMinutes() &&
				t1.getSeconds() == t2.getSeconds()
				? true : false;
	}
}
