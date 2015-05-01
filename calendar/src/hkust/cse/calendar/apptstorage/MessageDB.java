package hkust.cse.calendar.apptstorage;

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
			sql = "CREATE TABLE IF NOT EXISTS MESSAGETABLE "
					+ "(ID   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
					+ " MESSAGE       TEXT                NOT NULL,"
					+ "CAPACITY INTEGER NOT NULL)";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": "
					+ e.getMessage());
			System.exit(0);
		}
	}

	public boolean checkIfExists(String l) {
		try {
			ArrayList<Integer> idAL = new ArrayList<Integer>();
			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM MESSAGETABLE WHERE MESSAGE='"
							+ l + "';");
			while (rs.next()) {
				int ans = rs.getInt("ID");
				idAL.add(ans);
			}
			if (idAL.size() > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": "
					+ e.getMessage());
			System.exit(0);
			return false;
		}
	}

	public boolean addMessage(String l, int cap) throws SQLException {
		if (!checkIfExists(l)) {
			stmt = c.createStatement();
			sql = "INSERT INTO MESSAGETABLE (MESSAGE, CAPACITY) "
					+ "VALUES ( '" + l + "'" + ",'" + cap + "' );";
			stmt.executeUpdate(sql);
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "Message already exists",
					"Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	// a function to load message into some kind of map
	// return an array list of string
	public ArrayList<String> getMessageList() {
		ArrayList<String> temp = new ArrayList<String>();
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM MESSAGETABLE;");
			while (rs.next()) {
				String name = rs.getString("MESSAGE");
				temp.add(name);
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

	public int getMessageID(String l) {
		try {
			ArrayList<Integer> idAL = new ArrayList<Integer>();
			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM MESSAGETABLE WHERE MESSAGE='"
							+ l + "';");
			while (rs.next()) {
				int ans = rs.getInt("ID");
				idAL.add(ans);
			}
			switch (idAL.size()) {
			case 0:
				return 0; // does not exist
			case 1:
				return idAL.get(0); // only 1 exist (ideal scenario)
			default:
				return -1; // exist multiple times (should never occur)
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": "
					+ e.getMessage());
			System.exit(0);
			return -2;
		}
	}

	public boolean deleteMessage(int id) {
		try {
			stmt = c.createStatement();
			String sql = "DELETE from MESSAGETABLE WHERE ID=" + id + ";";
			stmt.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}

	public boolean modifyMessage(int id, String what) {
		try {
			stmt = c.createStatement();
			String sql = "UPDATE MESSAGETABLE set MESSAGE = " + what
					+ " where ID=" + id + ";";
			stmt.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}

	public int getCapacityByName(String locname) {
		try {
			ArrayList<Integer> idAL = new ArrayList<Integer>();
			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM MESSAGETABLE WHERE MESSAGE='"
							+ locname + "';");
			while (rs.next()) {
				int ans = rs.getInt("CAPACITY");
				idAL.add(ans);
			}
			switch (idAL.size()) {
			case 0:
				return 0; // does not exist
			case 1:
				return idAL.get(0); // only 1 exist (ideal scenario)
			default:
				return -1; // exist multiple times (should never occur)
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": "
					+ e.getMessage());
			System.exit(0);
			return -2;
		}

	}
}
