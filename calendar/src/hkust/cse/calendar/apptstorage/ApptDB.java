package hkust.cse.calendar.apptstorage;

import java.sql.*;
import javax.swing.JOptionPane;

public class ApptDB {
	public ApptDB()
	  {
		Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:appt.db");
//	      JOptionPane.showMessageDialog(null, "Opened database successfully");

	      stmt = c.createStatement();
	      String sql = "CREATE TABLE IF NOT EXISTS COMPANY " +
	    		  	   "(ID                   INT PRIMARY KEY    NOT NULL," +
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
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
//	    JOptionPane.showMessageDialog(null, "Table created successfully");
	  }
}
