//MORE COMMENTS

package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.Appointment;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class ApptDB {
	Connection c = null;
    Statement stmt = null;
    String sql = null;

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
	          " REMINDER_TIME        INT                NOT NULL," +
	          " REMINDER_UNIT        INT                NOT NULL," +
	          " ARW                  TEXT               NOT NULL)";
	      stmt.executeUpdate(sql);
	    } catch (Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	  }
    
    public boolean addARW(Appointment a)
    {
    	try {
			stmt = c.createStatement();
			sql = "INSERT INTO ";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
    	
    }
    
    public boolean addAppt(Appointment a)
	{
		try {
			stmt = c.createStatement();
			SimpleDateFormat dtSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			String dt = dtSDF.toString();
			dt = dt + a.getTitle();
			dt = "" + dt.hashCode();
			sql = "INSERT INTO APPOINTMENT (TITLE, DESCRIPTION, LOCATION, START_TIME_HOUR, START_TIME_MINUTE, START_TIME_YEAR, "
				+ "START_TIME_MONTH, START_TIME_DAY, END_TIME_HOUR, END_TIME_MINUTE, END_TIME_YEAR, END_TIME_MONTH, END_TIME_DAY, "
				+ "REMINDER, REMINDER_TIME, REMINDER_UNIT, ARW) " +
					"VALUES ('" + a.getTitle() + "','" + a.getDescription() + "','" + a.getLocation() + "'," +
				a.getStartHour() + "," + a.getStartMin() + "," + a.getStartYear() + "," + a.getStartMonth() + "," + a.getStartDay() + "," +
				a.getEndHour() + "," + a.getEndMin() + "," + a.getEndYear() + "," + a.getEndMonth() + "," + a.getEndDay() + "," +
				a.getReminder() + "," + a.getReminderTime() + "," + a.getReminderUnit() + "," + dt + ");";
			stmt.executeUpdate(sql);
			//create 1 new table with 3 columns
			//TODO fix this SQL syntax error
			
//			java.sql.SQLException: near "1967676291": syntax error
//			CREATE TABLE IF NOT EXISTS 1967676291 (ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  ATTEND TEXT,  REJECT TEXT,  WAITING TEXT)
//			java.sql.SQLException: [SQLITE_ERROR] SQL error or missing database (near "1967676291": syntax error)

//			System.out.println(dt);
//			sql = "CREATE TABLE IF NOT EXISTS " + dt + 
//					" (ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
//					"  ATTEND TEXT," +
//					"  REJECT TEXT," +
//					"  WAITING TEXT)";
//			System.out.println(sql);
//			stmt.executeUpdate(sql);
			return true;
		}
		catch (SQLException sqle) {
			System.err.println( sqle.getClass().getName() + ": " + sqle.getMessage() );
		}
		return false;
	}
    
    public LinkedList<LinkedList<String>> getARWList(String name)
    {
    	LinkedList<String> aal = new LinkedList<String>();
    	LinkedList<String> ral = new LinkedList<String>();
    	LinkedList<String> wal = new LinkedList<String>();
    	try
    	{
    		stmt = c.createStatement();
    		ResultSet rs = stmt.executeQuery("SELECT * FROM " + name + ";");
    		while (rs.next())
    		{
    			String attend = rs.getString("ATTEND");
    			String reject = rs.getString("REJECT");
    			String waiting = rs.getString("WAITING");
				aal.add(attend);
				ral.add(reject);
				wal.add(waiting);
    		}
    		LinkedList<LinkedList<String>> arwList = new LinkedList<LinkedList<String>>();
    		arwList.add(aal);
    		arwList.add(ral);
    		arwList.add(wal);
    		return arwList;
    	}
    	catch (SQLException e)
    	{
    		System.err.println( e.getClass().getName() + ": " + e.getMessage() );
    		return null;
    	}
    }
    
	public ArrayList<Appointment> getAppointmentList() {
		ArrayList<Appointment> temp = new ArrayList<Appointment>();
		try {
			stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery( "SELECT * FROM APPOINTMENT;" );
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
	        String ARW = "";
	        ArrayList<String> attend = new ArrayList<String>();
	        ArrayList<String> reject = new ArrayList<String>();
	        ArrayList<String> waiting = new ArrayList<String>();
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
				ARW = rs.getString("ARW");
				
				//TODO these 4 lines are just temporary
				//remove these codes once bug in addAppt has been fixed
				LinkedList<LinkedList<String>> arwList = new LinkedList<LinkedList<String>>();
				arwList.add(new LinkedList<String>());
				arwList.add(new LinkedList<String>());
				arwList.add(new LinkedList<String>());
//				LinkedList<LinkedList<String>> arwList = getARWList(ARW);
				//TODO extract the 3 lists from arwList
				Appointment tempAppointment = new Appointment(TITLE, DESCRIPTION, LOCATION, START_TIME_HOUR, START_TIME_MINUTE, START_TIME_YEAR, START_TIME_MONTH, START_TIME_DAY, END_TIME_HOUR, END_TIME_MINUTE, END_TIME_YEAR, END_TIME_MONTH, END_TIME_DAY, REMINDER, REMINDER_TIME, REMINDER_UNIT, arwList.get(0), arwList.get(1), arwList.get(2));
				temp.add(tempAppointment);
	        }
			return temp;
		} catch (SQLException e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
		return null;
	}

	

	public int getApptID(Appointment a)
	{
		//return id iff every details match
		try {
			ArrayList<Integer> idAL = new ArrayList<Integer>();
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM APPOINTMENT WHERE "
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
					+ "REMINDER='" + a.getReminder()
					+ "REMINDER_TIME='" + a.getReminderTime()
					+ "REMINDER_UNIT='" + a.getReminderUnit()
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
			ResultSet rs = stmt.executeQuery("SELECT * FROM APPOINTMENT WHERE "
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
//		    c.commit();
			return true;
		}
		catch (SQLException e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return false;
		}
	}
}
