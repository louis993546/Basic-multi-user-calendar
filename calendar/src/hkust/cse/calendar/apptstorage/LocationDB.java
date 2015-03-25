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
	      stmt = c.createStatement();
	      sql = "CREATE TABLE IF NOT EXISTS LOCATION " +
	                   "(ID   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
	                   " LOCATION       TEXT                NOT NULL)";
	      System.out.println(sql);
	      stmt.executeUpdate(sql);
	    } catch ( Exception e ) {
	      JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	  }

	public boolean checkIfExists(String l) {
		return false;
	}

	public boolean addLocation(String l) throws SQLException {
		if (!checkIfExists(l)) {
			stmt = c.createStatement();
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
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		} catch (NullPointerException e) {
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
				System.out.println("id = " + ans);
				idAL.add(ans);
			}
			switch (idAL.size()) {
				case 0: return 0;			//does not exist
				case 1:
					System.out.println(idAL.get(0));
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
		    String sql = "DELETE from LOCATION WHERE ID=" + id + ";";
		    stmt.executeUpdate(sql);
		    c.commit();
		    return true;
		}
		catch (SQLException e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    return false;
		}	
	}
	
	public boolean modifyLocation(int id, String what)
	{
		try
		{
			stmt = c.createStatement();
		    String sql = "UPDATE LOCATION set LOCATION = " + what + " where ID=" + id + ";";
		    stmt.executeUpdate(sql);
		    c.commit();
		    return true;
		}
		catch (SQLException e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    return false;
		}
	}
}
