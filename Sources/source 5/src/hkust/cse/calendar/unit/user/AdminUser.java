package hkust.cse.calendar.unit.user;

import java.io.Serializable;
import java.util.ArrayList;


public class AdminUser extends User implements Serializable {

	private ArrayList<String> pendingList;
	
	public AdminUser(String id, String pass) {
		super(id, pass);
		// TODO Auto-generated constructor stub
		this.isAdmin = true;
	}

}
