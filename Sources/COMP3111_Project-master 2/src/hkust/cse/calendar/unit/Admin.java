package hkust.cse.calendar.unit;

public class Admin extends User {
	
	public Admin(String id, String pass) {
		super(id, pass);
	}
	
	public boolean isAdmin() {
		return true;
	}
}
