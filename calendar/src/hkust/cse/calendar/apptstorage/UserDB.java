package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class UserDB 
{
	
	Connection c = null;
    Statement stmt = null;
    String sql = null;
    ResultSet rs = null;

	public UserDB()
	{
		try 
		{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:user.db");
			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS USERTABLE " +
			"(UID		INTEGER            NOT NULL        PRIMARY KEY AUTOINCREMENT," + 
			" ID		TEXT                NOT NULL," + // ID is actually email address
			" FIRSTNAME	TEXT                NOT NULL," +
			" LASTNAME	TEXT                NOT NULL," +
			" PASSWORD	TEXT                NOT NULL," + 
			" ADMIN		INT                 NOT NULL)"; 
			stmt.executeUpdate(sql);
		} 
		catch (SQLException e) 
		{
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean addUser(User u)
	{
		try {
			stmt = c.createStatement();
			sql = "INSERT INTO USERTABLE (ID, FIRSTNAME, LASTNAME, PASSWORD, ADMIN) VALUES ('" + 
					u.ID() + "','" + u.FirstName() + "','" + u.LastName() + "','" + u.Password() + "'," + u.Admin() + ")";
			stmt.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteUser(int uid)
	{
		//TODO check if this method works correctly
		try
		{
			stmt = c.createStatement();
		    String sql = "DELETE from USERTABLE WHERE UID=" +uid + ";";
		    stmt.executeUpdate(sql);
			return true;
		}
		catch (SQLException e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return false;
		}
	}

	public boolean modifyUser(int uid, User unew)
	{
		//TODO check if this method works correctly
		try
		{
			stmt = c.createStatement();
			sql = "UPDATE USERTABLE set " + 
			"ID = "       + unew.ID()       + ", " +
			"PASSWORD = " + unew.Password() + ", " +
			//TODO update ln and fn
			"ADMIN = "	  + unew.Admin()    + "WHERE " + 
			"UID = " + uid + ";" ;
			stmt.executeUpdate(sql);
			return true;	
		}
		catch (SQLException e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return false;
		}
	}

	public boolean checkIfIDExist(String i) //only checks id (i.e. email/username)
	{
		try
		{
			ArrayList<User> userAL = new ArrayList<User>();
			stmt = c.createStatement();
			sql = "SELECT * FROM USERTABLE WHERE (" + 
			"ID = '" + i + "');";
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				String id = rs.getString("ID");
				String pw = rs.getString("PASSWORD");
				int admin = rs.getInt("ADMIN");
				String ln = rs.getString("LASTNAME");
				String fn = rs.getString("FIRSTNAME");
				User newUser = new User(id, pw, admin, fn, ln);
				userAL.add(newUser);
			}
			switch (userAL.size())
			{
				case 1: return true;
				default: return false;
			}	
		}
		catch (SQLException e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    return false;
		}
	}
	
	public boolean checkIfExist(User u) //Only checks email and password
	{
		try
		{
			ArrayList<User> userAL = new ArrayList<User>();
			stmt = c.createStatement();
			sql = "SELECT * FROM USERTABLE WHERE (" + 
			"ID = '" + u.ID() + "' AND " + 
			"PASSWORD = '" + u.Password() + "');";
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				String id = rs.getString("ID");
				String pw = rs.getString("PASSWORD");
				int admin = rs.getInt("ADMIN");
				String ln = rs.getString("LASTNAME");
				String fn = rs.getString("FIRSTNAME");
				User newUser = new User(id, pw, admin, fn, ln);
				userAL.add(newUser);
			}
			switch (userAL.size())
			{
				case 1: return true;
				default: return false;
			}	
		}
		catch (SQLException e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    return false;
		}
	}

	public boolean isAdmin(User u)
	{
		//TODO check if a user is admin
		try
		{
			ArrayList<User> userAL = new ArrayList<User>();
			stmt = c.createStatement();
			sql = "SELECT * FROM USERTABLE WHERE (" + 
			"ID = '" + u.ID() + "');";
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				int uid = rs.getInt("UID");
				String email = rs.getString("ID");
				String pw = rs.getString("PASSWORD");
				int admin = rs.getInt("ADMIN");
				String ln = rs.getString("LASTNAME");
				String fn = rs.getString("FIRSTNAME");
				User newUser = new User(uid, email, pw, admin, fn, ln);
				userAL.add(newUser);
			}
			switch (userAL.size())
			{
				case 1:
				{
					if (userAL.get(0).Admin() == 1)
						return true;
					else
						return false;
				}
				default: return false;
			}	
		}
		catch (SQLException e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    return false;
		}
	}
	
	public String getLastname(int uid)
	{
		try
		{
			ArrayList<String> userAL = new ArrayList<String>();
			stmt = c.createStatement();
			sql = "SELECT * FROM USERTABLE WHERE (" + 
			"UID = '" + uid + "');";
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				String ln = rs.getString("LASTNAME");
				userAL.add(ln);
			}
			if (userAL.size() == 1)
			{
				return userAL.get(0);
			}
			else
				return null;
		}
		catch (SQLException e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    return "";
		}
	}
	
	public String getFirsttname(int uid)
	{
		try
		{
			ArrayList<String> userAL = new ArrayList<String>();
			stmt = c.createStatement();
			sql = "SELECT * FROM USERTABLE WHERE (" + 
			"UID = '" + uid + "');";
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				String ln = rs.getString("FIRSTNAME");
				userAL.add(ln);
			}
			if (userAL.size() == 1)
			{
				return userAL.get(0);
			}
			else
				return null;
		}
		catch (SQLException e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    return "";
		}
	}
	
	public User getUserWithUID(int uid)
	{
		try
		{
			ArrayList<User> userAL = new ArrayList<User>();
			stmt = c.createStatement();
			sql = "SELECT * FROM USERTABLE WHERE (ID = '" + uid + "');";
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				int a = rs.getInt("UID");
				String email = rs.getString("ID");
				String pw = rs.getString("PASSWORD");
				int admin = rs.getInt("ADMIN");
				String ln = rs.getString("LASTNAME");
				String fn = rs.getString("FIRSTNAME");
				User newUser = new User(a, email, pw, admin, fn, ln);
				userAL.add(newUser);
			}
			if (userAL.size() == 1)
				return userAL.get(0);
			else
				return null;
		}
		catch (SQLException e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    return null;
		}
	}
}