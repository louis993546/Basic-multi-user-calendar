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
						System.out.println("Fake system time: " + (timeM.getMTimestamp().getYear()+1900) + "/" + (1 + timeM.getMTimestamp().getMonth()) + "/" + timeM.getMTimestamp().getDate() + " " + timeM.getMTimestamp().getHours() + ":" + timeM.getMTimestamp().getMinutes() + ":" + timeM.getMTimestamp().getSeconds());
//						System.out.println(timeM.getMTimestamp().getYear());
//						System.out.println(timeM.getMTimestamp().getMonth());
//						System.out.println(timeM.getMTimestamp().getDate());
//						System.out.println(timeM.getMTimestamp().getHours());
//						System.out.println(timeM.getMTimestamp().getMinutes());
						if (r.getTime().getYear() == timeM.getMTimestamp().getYear())
						{
							if (r.getTime().getMonth() == timeM.getMTimestamp().getMonth())
							{
								if (r.getTime().getDate() == timeM.getMTimestamp().getDate())
								{
									if (r.getTime().getMinutes() == timeM.getMTimestamp().getMinutes())
									{
										if (r.getTime().getHours() == timeM.getMTimestamp().getHours())
										{
											System.out.println("The is the reminder for " + r.getTitle());
											JOptionPane.showMessageDialog(null, "This is the reminder for " + r.getTitle() );
											//TODO delete printed reminder
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

