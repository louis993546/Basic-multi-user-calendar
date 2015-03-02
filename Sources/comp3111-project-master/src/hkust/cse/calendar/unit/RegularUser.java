package hkust.cse.calendar.unit;

public class RegularUser extends User {

	public RegularUser(String id, String pass) {
		super(id, pass);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isAdmin() {
		return false;
	}
}
