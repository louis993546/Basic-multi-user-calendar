/**
* Each reminder object consists of 2 variable: a title and a time. 
*/

package hkust.cse.calendar.unit;

import java.sql.Timestamp;

public class Reminder {
	private String title;
	private Timestamp t;
	
	public Reminder(String a, Timestamp b)
	{
		title = a;
		t = b;
	}
	
	public void setTitle(String t)
	{
		title = t;
	}
	
	public void setTime(Timestamp a)
	{
		t = a;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public Timestamp getTime()
	{
		return t;
	}
	
	@Override
	public String toString()
	{
		return title;
	}
}
