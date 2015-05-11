package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.Message;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class MessageDB {

	Connection c = null;
	Statement stmt = null;
	String sql = null;

	public MessageDB() {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:message.db");
			stmt = c.createStatement();
			sql = "CREATE TABLE IF NOT EXISTS MessageTable "
					+ "(MessageID          INTEGER             NOT NULL               PRIMARY KEY AUTOINCREMENT,"
					+ " Type       		   INTEGER             NOT NULL,"
					+ " UserUIDList        TEXT                NOT NULL,"
					+ " editID             INTEGER             NOT NULL)";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": "
					+ e.getMessage());
			System.exit(0);
		}
	}
	
	public String ArrayListToString(ArrayList<Integer> list)
	{
		//e.g. String listS = "1/3/7/9/12/";
		if (list.size()>0)
		{
			String op = "";
			for (Integer a:list)
			{
				op = op + a + "/";
			}
			op = op.substring(0, op.length()-1);
			return op;
		}
		else
			return "";	
	}
	
	public ArrayList<Integer> StringToArrayList(String listS)
	{
		if (listS.startsWith("[") == true)
		{
			listS = listS.substring(1, listS.length());
		}
		if (listS.endsWith("]") == true)
		{
			listS = listS.substring(0, listS.length()-1);
		}
		ArrayList<Integer> op = new ArrayList<Integer>();
		if (listS.length()>0)
		{
			String[] listA = listS.split("/");
			for (String a:listA)
			{
				op.add(Integer.parseInt(a));
			}
		}
		return op;
	}

	public boolean addMessage(Message m)
	{
		try {
			stmt = c.createStatement();
			sql = "INSERT INTO MessageTable (Type, UserUIDList, editID) "
					+ "VALUES ( " + m.getType() + ",'" + ArrayListToString(m.getUserUIDList()) + "'," + m.getEditID() + " );";
			stmt.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// a function to load message into some kind of map
	// return an array list of string
	public ArrayList<Message> getMessageList() {
		ArrayList<Message> temp = new ArrayList<Message>();
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM MessageTable;");
			while (rs.next()) {
				int messageID = rs.getInt("MessageID");
				int type = rs.getInt("Type");
				String userUidList = rs.getString("UserUIDList");
				int editID = rs.getInt("editID");
				Message newMessage = new Message(messageID, type, StringToArrayList(userUidList), editID);
				temp.add(newMessage);
			}
			return temp;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": "
					+ e.getMessage());
			System.exit(0);
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": "
					+ e.getMessage());
			System.exit(0);
		}
		return null;
	}

	public boolean deleteMessage(int id) {
		try {
			stmt = c.createStatement();
			String sql = "DELETE from MessageTable WHERE ID=" + id + ";";
			stmt.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}

	public boolean modifyMessage(int id, Message m) {
		try {
			stmt = c.createStatement();
			String sql = "UPDATE MessageTable set " 
					+ " Type = " + m.getType()
					+ " UserUIDList = " + ArrayListToString(m.getUserUIDList())
					+ " editID = " + m.getEditID()
					+ " where ID=" + id + ";";
			stmt.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}
	
	public ArrayList<Message> getAllMessageForUser(int uid)
	{
		ArrayList<Message> allMessage= getMessageList();
		ArrayList<Message> messageForYou = new ArrayList<Message>();
		for (Message m:allMessage)
		{
			if (m.getUserUIDList().contains(uid) == true)
			{
				messageForYou.add(m);
			}
		}
		return messageForYou;
	}

}
