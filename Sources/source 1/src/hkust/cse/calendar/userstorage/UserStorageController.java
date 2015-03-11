package hkust.cse.calendar.userstorage;

import java.util.ArrayList;

import hkust.cse.calendar.unit.User;

public class UserStorageController {
	public final static int NEW = 1;
	public final static int MODIFY = 2;
	public final static int DELETE = 3;
	
	private static UserStorageController userController = new UserStorageController(UserStorageModel.getInstance());
	private UserStorage mUserStorage;
	
	private UserStorageController(UserStorage userStorage) {
		mUserStorage = userStorage;
	}
	
	public static UserStorageController getInstance() {
		return userController;
	}
	
	public void manageUsers(User user, int action) {
		if(action == NEW) {
			mUserStorage.saveUser(user);
		}
		else if(action == MODIFY) {
			mUserStorage.updateUser(user);
		}
		else if(action == DELETE) {
			mUserStorage.removeUser(user);
		}
	}
	
	public Integer getNumberOfUsers() {
		return mUserStorage.getNumberOfUsers();
	}
	
	public void loadUserFromXml() {
		mUserStorage.loadUserFromXml();
	}
	
	public User retrieveUser(String userId, String password) {
		return mUserStorage.retrieveUser(userId, password);
	}
	
	public User[] retrieveUsers() {
		return mUserStorage.retrieveUsers();
	}
	
	public User getUserById(String id) {
		return mUserStorage.getUserById(id);
	}
	
	public boolean checkUserExists(String id) {
		return mUserStorage.checkUserExists(id);
	}
	
	public ArrayList<String> retrieveUsersFromToBeDeletedList() {
		return mUserStorage.retrieveUsersFromToBeDeletedList();
	}
	
	public void putUserToBeDeletedList(User user) {
		mUserStorage.putUserToBeDeletedList(user);
	}
	
	public void removeUserFromToBeDeletedList(User user) {
		mUserStorage.removeUserFromToBeDeletedList(user);
	}
	
	public boolean checkUserInToBeDeleteList(User user) {
		return mUserStorage.checkUserInToBeDeleteList(user);
	}
}
