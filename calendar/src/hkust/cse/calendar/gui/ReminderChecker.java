package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Reminder;
import hkust.cse.calendar.unit.TimeMachine;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

public class ReminderChecker {
	Timer timer;
	ArrayList<Reminder> ral;
	CalGrid cg;
	TimeMachine timeM;
	
	public ReminderChecker(CalGrid c, TimeMachine tm)
	{
		timer = new Timer();
		ral = new ArrayList<Reminder>();
		cg = c;
		timeM = tm;
		cg.controller.LoadApptFromXml();
		ral = cg.controller.getReminders();
		timer.schedule(new ReminderAction(), 0, 10000);
	}
	
	class ReminderAction extends TimerTask
	{
		int loop = 10000;
		public void run()
		{
			if (loop >0)
			{
				try
				{
					ral = cg.controller.getReminders(); //TODO fail to update reminder list
					for (Reminder r:ral)
					{
						//TODO compare time with the fake clock
						System.out.println(r.getTitle() + " : " + r.getTime());
						java.util.Date date= new java.util.Date();
						System.out.println("Fake system time: " +timeM.getTMTimestamp()); 
//								(timeM.getTMTimestamp().getYear()+1900) + "/" + 
//								(1 + timeM.getTMTimestamp().getMonth()) + "/" + 
//								timeM.getTMTimestamp().getDate() + " " + 
//								timeM.getTMTimestamp().getHours() + ":" + 
//								timeM.getTMTimestamp().getMinutes() + ":" + 
//								timeM.getTMTimestamp().getSeconds());
//						System.out.println(timeM.getTMTimestamp().getYear());
//						System.out.println(timeM.getTMTimestamp().getMonth());
//						System.out.println(timeM.getTMTimestamp().getDate());
//						System.out.println(timeM.getTMTimestamp().getHours());
//						System.out.println(timeM.getTMTimestamp().getMinutes());
						if (r.getTime().getYear() == timeM.getTMTimestamp().getYear())
						{
							if (r.getTime().getMonth() == timeM.getTMTimestamp().getMonth())
							{
								if (r.getTime().getDate() == timeM.getTMTimestamp().getDate())
								{
									if (r.getTime().getMinutes() == timeM.getTMTimestamp().getMinutes())
									{
										if (r.getTime().getHours() == timeM.getTMTimestamp().getHours())
										{
											System.out.println("The is the reminder for " + r.getTitle());
											JOptionPane.showMessageDialog(null, "This is the reminder for " + r.getTitle() );
											//TODO delete printed reminder
											//ral[] -r ??
											ral.remove(r);//?
										}
									}
								}
							}
						}
					}
				}
				catch (NullPointerException e)
				{
//					System.out.println("Nothing appointments with reminders yet");
					//It means no appointments with reminder
					//i.e. do nothing
				}
//				System.out.println("Still running: " + loop);
				loop--;
			}
			else
			{
				System.out.println("Done");
				timer.cancel();
			}
		}
	}
}

