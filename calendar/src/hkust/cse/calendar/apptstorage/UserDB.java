package hkust.cse.calendar.apptstorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class UserDB 
{
	public UserDB()
	{
		Connection c = null;
		Statement stmt = null;
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
		catch ( Exception e ) 
		{
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
	}

	public boolean addUser(User u)
	{
		//TODO
	}

	public boolean deleteUser(User u)
	{
		//TODO
	}

	public boolean modifyUser(User uold, User unew)
	{
		//TODO
		sql = "UPDATE USERTABLE set " + 
		"ID = "       + unew.ID()       + ", " +
		"PASSWORD = " + unew.Password() + ", " +
		"ADMIN = "	  + unew.Admin()    + "WHERE " + 
		"ID = " + uold.ID() + ";" ;
	}
}