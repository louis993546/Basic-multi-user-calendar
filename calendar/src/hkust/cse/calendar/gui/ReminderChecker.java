package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Reminder;
import hkust.cse.calendar.unit.TimeMachine;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
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
	
	public void updateRal()
	{
		ral = cg.controller.getReminders();
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
					updateRal(); //TODO fail to update reminder list
					System.out.println("");
					for (Reminder r:ral)
					{
						//TODO compare time with the fake clock
						System.out.println(r.getTitle() + " : " + r.getTime());
						System.out.println("Fake system time: " +timeM.getTMTimestamp()); 
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
											ral.remove(r);//? remove in for loop?
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
				catch (ConcurrentModificationException e)
				{
					//Do nothing, and 10 seconds later it should refresh again, hopefully they won't be editing/reading at the same time again
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

