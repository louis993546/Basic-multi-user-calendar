package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.Appointment;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class ApptDB {
	Connection c = null;
    Statement stmt = null;
    String sql = null;
    ResultSet rs = null;

    public ApptDB()
	{
	c = null;
	stmt = null;
    try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:appt.db");
	    stmt = c.createStatement();
	    sql = "CREATE TABLE IF NOT EXISTS APPOINTMENT " +
	   	  	  "(ID                   INTEGER            NOT NULL        PRIMARY KEY AUTOINCREMENT," +
	   	      " TITLE                TEXT               NOT NULL," +
	   	      " DESCRIPTION          TEXT               NOT NULL," +
	   	      " LOCATION             TEXT               NOT NULL," +
	          " START_TIME_HOUR      INT                NOT NULL," +
	          " START_TIME_MINUTE    INT                NOT NULL," +
	          " START_TIME_YEAR      INT                NOT NULL," +
	          " START_TIME_MONTH     INT                NOT NULL," +
	          " START_TIME_DAY       INT                NOT NULL," +
	          " END_TIME_HOUR        INT                NOT NULL," +
	          " END_TIME_MINUTE      INT                NOT NULL," +
	          " END_TIME_YEAR        INT                NOT NULL," +
	          " END_TIME_MONTH       INT                NOT NULL," +
	          " END_TIME_DAY         INT                NOT NULL," +
	          " REMINDER             INT                NOT NULL," +
	          " REMINDER_TIME        INT                		," +
	          " REMINDER_UNIT        INT                		," +
	          " USER                 INT                NOT NULL," +
	          " GOING				 TEXT						," +
	          " WAITING				 TEXT						," +
	          " ARW                  TEXT               NOT NULL)";
	      stmt.executeUpdate(sql);
	    } catch (Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	  }
    
    public boolean addAppt(Appointment a)
	{
		try {
			stmt = c.createStatement();
			SimpleDateFormat dtSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			String dt = dtSDF.toString();
			dt = dt + a.getTitle();
			dt = "" + dt.hashCode();
			sql = "INSERT INTO APPOINTMENT ("
				+ "TITLE, "
				+ "DESCRIPTION, "
				+ "LOCATION, "
				+ "START_TIME_HOUR, "
				+ "START_TIME_MINUTE, "
				+ "START_TIME_YEAR, "
				+ "START_TIME_MONTH, "
				+ "START_TIME_DAY, "
				+ "END_TIME_HOUR, "
				+ "END_TIME_MINUTE, "
				+ "END_TIME_YEAR, "
				+ "END_TIME_MONTH, "
				+ "END_TIME_DAY, "
				+ "REMINDER, "
				+ "REMINDER_TIME, "
				+ "REMINDER_UNIT, USER, GOING, WAITING) " +
					"VALUES ('" 
				+ a.getTitle() + "','" 
				+ a.getDescription() + "','" 
				+ a.getLocation() + "',"
				+ a.getStartHour() + "," 
				+ a.getStartMin() + "," 
				+ a.getStartYear() + "," 
				+ a.getStartMonth() + "," 
				+ a.getStartDay() + "," 
				+ a.getEndHour() + "," 
				+ a.getEndMin() + "," 
				+ a.getEndYear() + "," 
				+ a.getEndMonth() + "," 
				+ a.getEndDay() + "," 
				+ a.getReminder() + "," 
				+ a.getReminderTime() + "," 
				+ a.getReminderUnit() + "," 
				+ a.getCreaterID() + "," 
				+ LinkedListToString(a.getGoingList()) + ","
				+ LinkedListToString(a.getWaitingList()) + ","
				+ ");";
			stmt.executeUpdate(sql);
			return true;
		}
		catch (SQLException sqle) {
			System.err.println( sqle.getClass().getName() + ": " + sqle.getMessage() );
		}
		return false;
	}
    
	public ArrayList<Appointment> getAppointmentList() {
		ArrayList<Appointment> temp = new ArrayList<Appointment>();
		try {
			stmt = c.createStatement();
	        rs = stmt.executeQuery( "SELECT * FROM APPOINTMENT;" );
	        String TITLE = "";
	        String DESCRIPTION = "";
	        String LOCATION = "";
	        int START_TIME_HOUR;
	        int START_TIME_MINUTE;
	        int START_TIME_YEAR;
	        int START_TIME_MONTH;
	        int START_TIME_DAY;
	        int END_TIME_HOUR;
	        int END_TIME_MINUTE;
	        int END_TIME_YEAR;
	        int END_TIME_MONTH;
	        int END_TIME_DAY;
	        int REMINDER;
	        int REMINDER_TIME;
	        int REMINDER_UNIT;
	        int USER;
	        String GOING;
	        String WAITING;
	        int ID;
//	        ArrayList<String> attend = new ArrayList<String>();
//	        ArrayList<String> reject = new ArrayList<String>();
//	        ArrayList<String> waiting = new ArrayList<String>();
	        while ( rs.next() ) {
				TITLE = rs.getString("TITLE");
				DESCRIPTION = rs.getString("DESCRIPTION");
				START_TIME_HOUR = rs.getInt("START_TIME_HOUR");
				START_TIME_MINUTE =rs.getInt("START_TIME_MINUTE");
				START_TIME_YEAR =rs.getInt("START_TIME_YEAR");
				START_TIME_MONTH =rs.getInt("START_TIME_MONTH");
				START_TIME_DAY =rs.getInt("START_TIME_DAY");
				END_TIME_HOUR =rs.getInt("END_TIME_HOUR");
				END_TIME_MINUTE =rs.getInt("END_TIME_MINUTE");
				END_TIME_YEAR =rs.getInt("END_TIME_YEAR");
				END_TIME_MONTH =rs.getInt("END_TIME_MONTH");
				END_TIME_DAY =rs.getInt("END_TIME_DAY");
				REMINDER =rs.getInt("REMINDER");
				REMINDER_TIME =rs.getInt("REMINDER_TIME");
				LOCATION = rs.getString("LOCATION");
				REMINDER_UNIT = rs.getInt("REMINDER_UNIT");
				GOING = rs.getString("GOING");
				WAITING = rs.getString("WAITING");
				USER = rs.getInt("USER");
				ID = rs.getInt("ID");
				
				LinkedList<Integer> goingUIDLL = StringToLinkedList(GOING);
				LinkedList<Integer> waitingUIDLL = StringToLinkedList(WAITING);
				//TODO these 4 lines are just temporary
				//remove these codes once bug in addAppt has been fixed
//				LinkedList<LinkedList<String>> arwList = new LinkedList<LinkedList<String>>();
//				arwList.add(new LinkedList<String>());
//				arwList.add(new LinkedList<String>());
//				arwList.add(new LinkedList<String>());
//				LinkedList<LinkedList<String>> arwList = getARWList(ARW);
				//TODO [Phrase 2] extract the 3 lists from arwList
				Appointment tempAppointment = new Appointment(TITLE, DESCRIPTION, LOCATION, START_TIME_HOUR, START_TIME_MINUTE, START_TIME_YEAR, START_TIME_MONTH, START_TIME_DAY, END_TIME_HOUR, END_TIME_MINUTE, END_TIME_YEAR, END_TIME_MONTH, END_TIME_DAY, REMINDER, REMINDER_TIME, REMINDER_UNIT, goingUIDLL,waitingUIDLL, ID, USER);
				temp.add(tempAppointment);
	        }
			return temp;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
		return null;
	}
	
	public Appt[] getApptByUserTime(User u, TimeSpan d)
	{
		//TODO retrieve appts iff both u and d 
		Appt[] abt = getApptByTime(d);
		ArrayList<Appt> temp = new ArrayList<Appt>();
		for (Appt a:abt)
		{
			if (a.getAppointment().getCreaterID() == u.UID())
			{
				temp.add(a);
			}
		}
		Appt[] temparray = new Appt[temp.size()];
		for (int i = 0; i<temp.size(); i++)
		{
			temparray[i] = temp.get(i);
		}
		return temparray;
	}

	public Appt[] getApptByTime(TimeSpan d)
	{
		ArrayList<Appointment> temp = new ArrayList<Appointment>();
		try {
			stmt = c.createStatement();
		    int START_TIME_YEAR = d.StartTime().getYear()+1900;
		    int START_TIME_MONTH = d.StartTime().getMonth()+1;
		    int START_TIME_DAY = d.StartTime().getDate();
		    int END_TIME_YEAR = d.EndTime().getYear()+1900;
		    int END_TIME_MONTH = d.EndTime().getMonth()+1;
		    int END_TIME_DAY = d.EndTime().getDate();
		    String TITLE = "";
		    String DESCRIPTION = "";
		    String LOCATION = "";
		    int REMINDER;
		    int REMINDER_TIME;
		    int REMINDER_UNIT;
		    String ARW = "";
		    String GOING = "";
		    String WAITING = "";
		    int createrID;
		    int ID;
		    ArrayList<String> attend = new ArrayList<String>();
		    ArrayList<String> reject = new ArrayList<String>();
		    ArrayList<String> waiting = new ArrayList<String>();
		    String sql = "SELECT * FROM APPOINTMENT WHERE ("
			    	+ " START_TIME_YEAR=" + START_TIME_YEAR
			    	+ " AND START_TIME_MONTH=" + START_TIME_MONTH
			    	+ " AND START_TIME_DAY=" + START_TIME_DAY
			    	+ " AND END_TIME_YEAR=" + END_TIME_YEAR
			    	+ " AND END_TIME_MONTH=" + END_TIME_MONTH
			    	+ " AND END_TIME_DAY=" + END_TIME_DAY
			    	+ ");" ;
		    rs = stmt.executeQuery(sql);
		    while ( rs.next() ) {
				TITLE = rs.getString("TITLE");
				DESCRIPTION = rs.getString("DESCRIPTION");
				int START_TIME_HOUR = rs.getInt("START_TIME_HOUR");
				int START_TIME_MINUTE =rs.getInt("START_TIME_MINUTE");
				START_TIME_YEAR =rs.getInt("START_TIME_YEAR");
				START_TIME_MONTH =rs.getInt("START_TIME_MONTH");
				START_TIME_DAY =rs.getInt("START_TIME_DAY");
				int END_TIME_HOUR =rs.getInt("END_TIME_HOUR");
				int END_TIME_MINUTE =rs.getInt("END_TIME_MINUTE");
				END_TIME_YEAR =rs.getInt("END_TIME_YEAR");
				END_TIME_MONTH =rs.getInt("END_TIME_MONTH");
				END_TIME_DAY =rs.getInt("END_TIME_DAY");
				REMINDER =rs.getInt("REMINDER");
				REMINDER_TIME =rs.getInt("REMINDER_TIME");
				LOCATION = rs.getString("LOCATION");
				REMINDER_UNIT = rs.getInt("REMINDER_UNIT");
				ARW = rs.getString("ARW");
				createrID = rs.getInt("USER");
				ID = rs.getInt("ID");
				
				//TODO these codes are temporary
				//remove these codes once bug in addAppt has been fixed
				//How-to: serialize the 3 lists
//				LinkedList<LinkedList<String>> arwList = new LinkedList<LinkedList<String>>();
				LinkedList<Integer> goingUIDLL = StringToLinkedList(GOING);
				LinkedList<Integer> waitingUIDLL = StringToLinkedList(WAITING);
//				arwList.add(new LinkedList<String>());
//				arwList.add(new LinkedList<String>());
//				arwList.add(new LinkedList<String>());
				
				//TODO [Phrase 2] extract the 2 lists from arwList
				Appointment tempAppointment = new Appointment(TITLE, DESCRIPTION, LOCATION, START_TIME_HOUR, START_TIME_MINUTE, START_TIME_YEAR, START_TIME_MONTH, START_TIME_DAY, END_TIME_HOUR, END_TIME_MINUTE, END_TIME_YEAR, END_TIME_MONTH, END_TIME_DAY, REMINDER, REMINDER_TIME, REMINDER_UNIT, goingUIDLL, waitingUIDLL, ID, createrID);
				temp.add(tempAppointment);
		    }
			Appt[] temparray = new Appt[temp.size()];
			for (int i = 0; i<temp.size(); i++)
			{
				Appt tempappt = new Appt(temp.get(i));
				temparray[i] = tempappt;
			}
			return temparray;
		} catch (SQLException e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		} catch (NullPointerException e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		return null;
	}
	
	//TODO we will need a lot of methods for the 2 lists
	
	public String LinkedListToString(LinkedList<Integer> list)
	{
		//Syntax: each UID will be deperated with a "/" symbol
		//e.g. String listS = "1/3/7/9/12";
		String op = "";
		for (Integer a:list)
		{
			op = op + a + "/";
		}
		return op;
	}
	
	public LinkedList<Integer> StringToLinkedList(String listS)
	{
		//Syntax: each UID will be deperated with a "/" symbol
		//e.g. String listS = "/1/3/7/9/12";
		LinkedList<Integer> op = new LinkedList<Integer>();
		int id;
		int dash = listS.indexOf("/");
		while (dash >= 0 )
		{
			id = Integer.parseInt(listS.substring(0, dash-1));
			op.add(id);
			listS = listS.substring(dash+1);
			dash = listS.indexOf("/");
		}
		return op;
	}
	
	public boolean IsHeInWaitingList(int apptID, int uid)
	{
		//TODO check if someone(uid) exists in an specific appointment(apptID)'s waiting list
		return false;
	}
	
	public boolean IsHeInGoingList(int apptID, int uid)
	{
		//TODO check if someone(uid) exists in an specific appointment(apptID)'s going list
		return false;
	}
	
	public Appt getApptByJID(int j)
	{
		//TODO [Phrase 2?] get Appt by JID
		try
		{
			ArrayList<Appointment> AppointmentALJID = new ArrayList<Appointment>();
			stmt = c.createStatement();
			sql = "SELECT * FROM APPOINTMENT WHERE JID=" + j;
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				//This part is pretty much the same as getApptByTime,
				//And since the structure of the DB is not finalized yet, 
				//Finish this part later
			}
		}
		catch (SQLException e)
		{
			//TODO what to do with this exception?
		}
		return null;
	}

	public int getApptID(Appointment a)
	{
		//return id iff every details match
		try {
			ArrayList<Integer> idAL = new ArrayList<Integer>();
			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM APPOINTMENT WHERE "
					+ "TITLE='" + a.getTitle()
					+ "DESCIRPTION='" + a.getDescription()
					+ "LOCATION='" + a.getLocation()
					+ "START_TIME_HOUR='" + a.getStartHour()
					+ "START_TIME_MINUTE='" + a.getStartMin()
					+ "START_TIME_YEAR='" + a.getStartYear()
					+ "START_TIME_MONTH='" + a.getStartMonth()
					+ "START_TIME_DAY='" + a.getStartDay()
					+ "END_TIME_HOUR='" + a.getEndHour()
					+ "END_TIME_MINUTE='" + a.getEndMin()
					+ "END_TIME_YEAR='" + a.getEndYear()
					+ "END_TIME_MONTH='" + a.getEndMonth()
					+ "END_TIME_DAY='" + a.getEndDay()
					+ "';");
			while (rs.next()) {
				int ans = rs.getInt("ID");
				idAL.add(ans);
			}
			switch (idAL.size()) {
				case 0: return 0;			//does not exist
				case 1:
					return idAL.get(0);	//only 1 exist (ideal scenario)
				default: return -1;			//exist multiple times (should never occur)
			}
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
		    return -2;
		}
	}
	
	public int getApptIDByTitle(String title)
	{
		try {
			ArrayList<Integer> idAL = new ArrayList<Integer>();
			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM APPOINTMENT WHERE "
					+ "TITLE='" + title
					+ "';");
			while (rs.next()) {
				int ans = rs.getInt("ID");
				idAL.add(ans);
			}
			switch (idAL.size()) {
			case 0: return 0;			//does not exist
			case 1:
				return idAL.get(0);	//only 1 exist (ideal scenario)
			default: return -1;			//exist multiple times (should never occur)
			}
		}
		catch (SQLException e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    return -2;
		}
	}
	
	public boolean deleteAppt(int id)
	{
		try
		{
			stmt = c.createStatement();
		    String sql = "DELETE from APPOINTMENT WHERE ID=" + id + ";";
		    stmt.executeUpdate(sql);
			return true;
		}
		catch (SQLException e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return false;
		}
	}
	
	public boolean modifyAppt(int id, Appointment newAppt)
	{
		try
		{	
			stmt = c.createStatement();
	        sql = "UPDATE APPOINTMENT SET"
	    		  + "        TITLE = '"           + newAppt.getTitle()
	    		  + "' ,   DESCRIPTION = '"     + newAppt.getDescription()
	    		  + "' ,   LOCATION = '"        + newAppt.getLocation()
	    		  + "' ,   START_TIME_HOUR="    + newAppt.getStartHour()
	    		  + "  ,   START_TIME_MINUTE= " + newAppt.getStartMin()
	    		  + "  ,   START_TIME_YEAR= "   + newAppt.getStartYear()
	    		  + "  ,   START_TIME_MONTH= "  + newAppt.getStartMonth()
	    		  + "  ,   START_TIME_DAY= "    + newAppt.getStartDay()
	    		  + "  ,   END_TIME_HOUR= "     + newAppt.getEndHour()
	    		  + "  ,   END_TIME_MINUTE= "   + newAppt.getEndMin()
	    		  + "  ,   END_TIME_YEAR= "     + newAppt.getEndYear()
	    		  + "  ,   END_TIME_MONTH= "    + newAppt.getEndMonth()
	    		  + "  ,   END_TIME_DAY= "      + newAppt.getEndDay()
	    		  + "  ,   REMINDER= "          + newAppt.getReminder()
	    		  + "  ,   REMINDER_TIME= "     + newAppt.getReminderTime()
	    		  + "  ,   REMINDER_UNIT= "     + newAppt.getReminderUnit()
	    		  + "  ,   GOING = "            + newAppt.getGoingList()
	    		  + "  ,   WAITING = "          + newAppt.getWaitingList()
	    		  + "  WHERE ID="               + id
	    		  + ";";
			stmt.executeUpdate(sql);

			return true;
		}
		catch (SQLException e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return false;
		}
	}

	//TODO not sure if these 2 functions will be useful
	public ArrayList<Integer> getApptIDListFromGoing(int uid)
	{
		return null;
	}
	
	public ArrayList<Integer> getApptIDListFromWaiting(int uid)
	{
		return null;
	}
}
