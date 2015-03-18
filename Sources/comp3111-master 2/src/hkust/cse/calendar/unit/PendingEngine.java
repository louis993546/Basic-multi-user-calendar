package hkust.cse.calendar.unit;

import java.util.ArrayList;

import hkust.cse.calendar.unit.user.User;

public class PendingEngine {

	private static PendingEngine instance = null;
	private ArrayList<PendingRequest> requests;
	
	public static PendingEngine getInstance() {
		if(instance == null) {
			instance = new PendingEngine();
		}
		return instance;
	}
	
	private PendingEngine() {
		requests = new ArrayList<PendingRequest>();
	}
	
	public void addPendingRequest(int type, User from, User to, Object obj) {
		requests.add(new PendingRequest(type, from, to, obj));
	}
	
	public ArrayList<PendingRequest> checkSentRequest(User user) {
		ArrayList<PendingRequest> userRequests = new ArrayList<PendingRequest>();
		
		for(PendingRequest request : requests) {
			if(request.getFrom() == user) {
				userRequests.add(request);
			}
		}
		
		return userRequests;
	}
	
	public ArrayList<PendingRequest> checkPendingRequest(User user) {
		ArrayList<PendingRequest> userRequests = new ArrayList<PendingRequest>();
		ArrayList<PendingRequest> requests2 = new ArrayList<PendingRequest>();
		
		for(PendingRequest request : requests) {
			if(request.getTo() == user) {
				userRequests.add(request);
			}
			else {
				requests2.add(request);
			}
		}
		
		requests = requests2;
		
		return userRequests;
	}
	
}
