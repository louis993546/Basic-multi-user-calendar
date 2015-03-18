package hkust.cse.calendar.unit;

import hkust.cse.calendar.unit.user.User;

public class PendingRequest {
	public final static int TYPE_LOCATION = 1;
	public final static int REMOVE_LOCATION = 2;
	public final static int REMOVE_APPOINTMENT = 3;
	
	private int type;
	private User from;
	private User to;
	private Object obj;
	
	public PendingRequest(int type, User from, User to, Object obj) {
		this.type = type;
		this.from = from;
		this.to = to;
		this.obj = obj;
	}

	public int getType() {
		return type;
	}

	public User getFrom() {
		return from;
	}

	public User getTo() {
		return to;
	}

	public Object getObj() {
		return obj;
	}
}
