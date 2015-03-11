package hkust.cse.calendar.userstorage;

import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.xmlfactory.UserXmlFactory;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class UserStorage {
	
	protected static UserStorage instance = null;
	protected static String userFile = "user.xml";
	protected static String toBeDeleteUserFile = "toBeDeleteUserFile.xml";
	public HashMap<String, User> mUsers;
	public ArrayList<String> mToBeDeletedUsers;
	public UserXmlFactory userXmlFactory;
	
	public UserStorage() {
	}
	
	public abstract Integer getNumberOfUsers();
	
	public abstract void saveUser(User user);
	
	public abstract void updateUser(User user);
	
	public abstract void removeUser(User user);
	
	public abstract void loadUserFromXml();
	
	public abstract void saveUserToXml(User user);
	
	public abstract void updateUserInXml(User user);
	
	public abstract void removeUserFromXml(User user);
	
	public abstract User retrieveUser(String userId, String password);
	
	public abstract User[] retrieveUsers();
	
	public abstract User getUserById(String id);
	
	public abstract boolean checkUserExists(String id);
	
	public abstract void putUserToBeDeletedList(User user);
	
	public abstract void removeUserFromToBeDeletedList(User user);
	
	public abstract ArrayList<String> retrieveUsersFromToBeDeletedList();
	
	public abstract void loadUserFromToBeDeletedListXml();
	
	public abstract void addUserToToBeDeletedListXml(User user);
	
	public abstract void removeUserFromToBeDeletedListXml(User user);
	
	public abstract boolean checkUserInToBeDeleteList(User user);
}
