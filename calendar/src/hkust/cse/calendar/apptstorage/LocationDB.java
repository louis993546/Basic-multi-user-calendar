package hkust.cse.calendar.apptstorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class LocationDB {
	
	Connection c = null;
    Statement stmt = null;
    String sql = null;
    
	public LocationDB()
	  {
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:location.db");
//	      JOptionPane.showMessageDialog(null, "Opened database successfully");

	      stmt = c.createStatement();
	      sql = "CREATE TABLE IF NOT EXISTS LOCATION " +
	                   "(ID             INTEGER NOT NULL ," +
//	                   				 + "PRIMARY KEY GENERATED ALWAYS AS IDENTITY"
//	                   				 + "(START WITH 1, INCREMENT BY 1)," +
	                   " LOCATION       TEXT                NOT NULL)"; 
	      stmt.executeUpdate(sql);
//	      stmt.close();
//	      c.close();
	    } catch ( Exception e ) {
	      JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
//	    JOptionPane.showMessageDialog(null, "Table created successfully");
	  }
	
	public boolean checkIfExists(String l) {
		return false;
		
	}
	
	public boolean addLocation(String l) throws SQLException {
		if (!checkIfExists(l)) {
			stmt = c.createStatement();
			sql = "INSERT INTO LOCATION (LOCATION) " +
	                "VALUES ('" + l + "' );"; 
			stmt.executeUpdate(sql);
			return true;
		}
		else return false;
	}
}
