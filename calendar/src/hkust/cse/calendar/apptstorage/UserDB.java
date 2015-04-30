package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class UserDB 
{
	
	Connection c = null;
    Statement stmt = null;
    String sql = null;
    ResultSet rs = null;

	public UserDB()
	{
		//TODO check if this section works correctly
		try 
		{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:user.db");
			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS USERTABLE " +
			"(ID		TEXT PRIMARY KEY    NOT NULL," + 
				" PASSWORD	TEXT                NOT NULL," + 
				" ADMIN		INT                 NOT NULL)"; 
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} 
		catch (SQLException e) 
		{
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
	}

	public boolean addUser(User u)
	{
		//TODO
		stmt = c.createStatement();
		sql = "INSERT INTO USERTABLE (ID, PASSWORD, ADMIN) VALUES ('" + 
			u.ID() + "','" + u.Password() + "'," + u.Admin() + ")";
		stmt.executeUpdate();
	}

	public boolean deleteUser(User u)
	{
		//TODO check if this method works correctly
		try
		{
			stmt = c.createStatement();
		    String sql = "DELETE from USERTABLE WHERE ID=" + u.ID() + ";";
		    stmt.executeUpdate(sql);
			return true;
		}
		catch (SQLException e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return false;
		}
	}

	public boolean modifyUser(User uold, User unew)
	{
		//TODO check if this method works correctly
		try
		{
			stmt = c.createStatement();
			sql = "UPDATE USERTABLE set " + 
			"ID = "       + unew.ID()       + ", " +
			"PASSWORD = " + unew.Password() + ", " +
			"ADMIN = "	  + unew.Admin()    + "WHERE " + 
			"ID = " + uold.ID() + ";" ;
			stmt.executeUpdate(sql);
			return true;	
		}
		catch (SQLException e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return false;
		}
	}

	public boolean checkIfexist(User u)
	{
		//TODO return true if user exist and password is correct
		try
		{
			ArrayList<User> userAL = new ArrayList<User>();
			stmt = c.createStatement();
			sql = "SELECT count(*) FROM USERTABLE WHERE (" + 
			"ID = " + u.ID() + " AND " + 
			"PASSWORD = " + u.Password() + ");";
			rs = stmt.executeUpdate(sql);
			while (rs.next())
			{
				String id = rs.getString("ID");
				String pw = rs.getString("PASSWORD");
				int admin = rs.getInt("ADMIN");
				User newUser = new User(id, pw, admin);
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
		//TODO
	}
}