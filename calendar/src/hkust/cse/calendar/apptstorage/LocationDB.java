package hkust.cse.calendar.apptstorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
	                   "(ID   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
//	                   				 + "PRIMARY KEY GENERATED ALWAYS AS IDENTITY"
//	                   				 + "(START WITH 1, INCREMENT BY 1)," +
	                   " LOCATION       TEXT                NOT NULL)";
	      stmt.executeUpdate(sql);
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
			//need to figure how to to increment id automatically
			sql = "INSERT INTO LOCATION (LOCATION) " +
	                "VALUES ( '" + l + "' );";
			stmt.executeUpdate(sql);
			return true;
		}
		else return false;
	}

	//a function to load location into some kind of map
	//return an array list of string
	public ArrayList<String> getLocationList() {
		ArrayList<String> temp = new ArrayList<String>();
		try {
			stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery( "SELECT * FROM LOCATION;" );
	        while ( rs.next() ) {
				String name = rs.getString("LOCATION");
				temp.add(name);
	        }
			return temp;
		} catch (SQLException e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		} catch (NullPointerException e) {
			//What is happening?
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
	    return null;
	}

	public int getLocationID(String l) {
		try {
			ArrayList<Integer> idAL = new ArrayList<Integer>();
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LOCATION WHERE LOCATION='" + l + "';");
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
		    System.exit(0);
		    return -2;
		}
	}

	public boolean deleteLocation(int id) {
		try {
			stmt = c.createStatement();
		    String sql = "DELETE from LOCATION where ID=" + id + ";";
		    stmt.executeUpdate(sql);
		    c.commit();
		    return true;
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		    return false;
		}
		
	}
}
