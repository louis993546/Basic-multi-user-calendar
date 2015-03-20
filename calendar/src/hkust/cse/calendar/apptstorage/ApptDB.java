package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.Appointment;

import java.sql.*;
import java.util.ArrayList;

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
//	      JOptionPane.showMessageDialog(null, "Opened database successfully");

	      stmt = c.createStatement();
	      sql = "CREATE TABLE IF NOT EXISTS APPOINTMENT " +
	    		  	   "(ID    INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
	    		       " TITLE                TEXT               NOT NULL," +
	    		       " DESCRIPTION          TEXT               NOT NULL," +
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
	    		       " LOCATION             TEXT               NOT NULL)";
	      stmt.executeUpdate(sql);
//	      stmt.close();
//	      c.close();
	    } catch ( Exception e ) {
	      JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
//	    JOptionPane.showMessageDialog(null, "Table created successfully");
	  }
	
	public boolean addNewAppt(String t, String d, String l) {
		return false;
		//SQL
	}
	
	public ArrayList<Appointment> getAppointmentList() {
		ArrayList<Appointment> temp = new ArrayList<Appointment>(); 
		try {
			stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery( "SELECT * FROM APPOINTMENT;" );
	        String TITLE = "";
	        String DESCRIPTION = "";
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
	        String LOCATION = "";
	        Appointment tempAppointment = new Appointment();
	        while ( rs.next() ) {
	        	//create new appointment
	        	//store every data into appointment
	        	//temp.add(appointment)
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
				tempAppointment.setAppointment(TITLE, DESCRIPTION, LOCATION, START_TIME_HOUR, START_TIME_MINUTE, START_TIME_YEAR, START_TIME_MONTH, START_TIME_DAY, END_TIME_HOUR, END_TIME_MINUTE, END_TIME_YEAR, END_TIME_MONTH, END_TIME_DAY, REMINDER, REMINDER_TIME, REMINDER_UNIT);
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
	
	public boolean addAppt(Appointment a)
	//DONE
	{
		try {
			stmt = c.createStatement();
			String x = a.getTitle();
			sql = "INSERT INTO APPOINTMENT (TITLE, DESCRIPTION, LOCATION, START_TIME_HOUR, START_TIME_MINUTE, START_TIME_YEAR, "
				+ "START_TIME_MONTH, START_TIME_DAY, END_TIME_HOUR, END_TIME_MINUTE, END_TIME_YEAR, END_TIME_MONTH, END_TIME_DAY, "
				+ "REMINDER, REMINDER_TIME, REMINDER_UNIT) " +
					"VALUES ('" + a.getTitle() + "','" + a.getDescription() + "','" + a.getLocation() + "'," + 
				a.getStartHour() + "," + a.getStartMin() + "," + a.getStartYear() + "," + a.getStartMonth() + "," + a.getStartDay() + "," + 
				a.getEndHour() + "," + a.getEndMin() + "," + a.getEndYear() + "," + a.getEndMonth() + "," + a.getEndDay() + "," + 
				a.getReminder() + "," + a.getReminderTime() + "," + a.getReminderUnit() + ");";
			stmt.executeUpdate(sql);
			return true;
		}
		catch (SQLException sqle) {
			System.err.println( sqle.getClass().getName() + ": " + sqle.getMessage() );
		}
		return false;
	}
}
