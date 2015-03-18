package hkust.cse.calendar.unit;


import java.util.HashMap;



public class Userlist {

	private User user;
	private HashMap<Integer,Appt> map;
	
	public User getuser() {
		return user;
	}
	
	public void setuser(User _user) {
		this.user = _user;
	}
	
	public HashMap<Integer,Appt> getMap() {
		return map;
	}
	
	public void setMap(HashMap<Integer,Appt> map) {
		this.map = map;
	}

	
}
