package hkust.cse.calendar.notification;

import javax.swing.JOptionPane;

public class AlertService implements INotification {

	private String username;
	private String address;
	private String message;
	
	public AlertService(String username, String address, String message) {
		this.username = username;
		this.address = address;
		this.message = message;
	}
	
	@Override
	public void Send() {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null,
 			    message,
 			    "Appointment!",
 			    JOptionPane.INFORMATION_MESSAGE);
	}

	

}
