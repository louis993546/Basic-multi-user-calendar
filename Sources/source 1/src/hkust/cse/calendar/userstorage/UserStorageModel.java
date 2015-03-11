package hkust.cse.calendar.userstorage;

import java.util.ArrayList;
import java.util.HashMap;

import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.xmlfactory.UserXmlFactory;

public class UserStorageModel extends UserStorage {

	public static UserStorage getInstance() {
		if(instance == null) {
			instance = new UserStorageModel();
		}
		return instance;
	}

	private UserStorageModel() {
		mUsers = new HashMap<String, User>();
		mToBeDeletedUsers = new ArrayList<String>();
		userXmlFactory = new UserXmlFactory();
		loadUserFromXml();
		loadUserFromToBeDeletedListXml();
	}

	@Override
	public Integer getNumberOfUsers() {
		return new Integer(mUsers.size());
	}
	
	@Override
	public void saveUser(User user) {
		// TODO Auto-generated method stub
		mUsers.put(user.ID(), user);
		saveUserToXml(user);
	}

	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub
		//mUsers.put(user.ID(), user);
		updateUserInXml(user);
	}

	@Override
	public void removeUser(User user) {
		// TODO Auto-generated method stub
		mUsers.remove(user.ID());
		removeUserFromXml(user);
	}

	@Override
	public void loadUserFromXml() {
		// TODO Auto-generated method stub
		userXmlFactory.loadUserFromXml(UserStorage.userFile, mUsers);
	}

	@Override
	public User[] retrieveUsers() {
		// TODO Auto-generated method stub
		ArrayList<User> users = new ArrayList<User>();
		for(User user : mUsers.values()) {
			users.add(user);
		}
		return users.toArray(new User[users.size()]);
	}

	@Override
	public User getUserById(String id) {
		// TODO Auto-generated method stub
		return mUsers.get(id);
	}

	@Override
	public boolean checkUserExists(String id) {
		// TODO Auto-generated method stub
		if(getUserById(id) == null) {
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public User retrieveUser(String userId, String password) {
		// TODO Auto-generated method stub
		if(mUsers.get(userId) == null) {
			return null;
		}
		else if(mUsers.get(userId).Password().equals(password)) {
			return mUsers.get(userId);
		}
		return null;
	}

	@Override
	public void saveUserToXml(User user) {
		// TODO Auto-generated method stub
		userXmlFactory.saveUserToXml(UserStorage.userFile, user);
	}
	
	public void updateUserInXml(User user) {
		userXmlFactory.updateUserInXml(UserStorage.userFile, user);
	}
	
	public void removeUserFromXml(User user) {
		userXmlFactory.removeUserFromXml(UserStorage.userFile, user);
	}

	@Override
	public void putUserToBeDeletedList(User user) {
		// TODO Auto-generated method stub
		mToBeDeletedUsers.add(user.ID());
		addUserToToBeDeletedListXml(user);
	}

	@Override
	public void removeUserFromToBeDeletedList(User user) {
		// TODO Auto-generated method stub
		mToBeDeletedUsers.remove(user.ID());
		removeUserFromToBeDeletedListXml(user);
	}

	@Override
	public ArrayList<String> retrieveUsersFromToBeDeletedList() {
		// TODO Auto-generated method stub
		return mToBeDeletedUsers;
	}

	@Override
	public void addUserToToBeDeletedListXml(User user) {
		// TODO Auto-generated method stub
		userXmlFactory.addUserToToBeDeletedListXml(UserStorage.toBeDeleteUserFile, user);
	}

	@Override
	public void removeUserFromToBeDeletedListXml(User user) {
		// TODO Auto-generated method stub
		userXmlFactory.removeUserFromToBeDeletedListXml(UserStorage.toBeDeleteUserFile, user);
	}

	@Override
	public void loadUserFromToBeDeletedListXml() {
		// TODO Auto-generated method stub
		userXmlFactory.loadUserFromToBeDeletedListXml(UserStorage.toBeDeleteUserFile, mToBeDeletedUsers);
	}

	@Override
	public boolean checkUserInToBeDeleteList(User user) {
		// TODO Auto-generated method stub
		return mToBeDeletedUsers.contains(user.ID());
	}
}
