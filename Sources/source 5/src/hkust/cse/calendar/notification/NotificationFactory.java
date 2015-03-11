package hkust.cse.calendar.notification;

public class NotificationFactory {

	
	public INotification GetNotificationService(NotificationService service, String username, String address, String message) {
		switch(service) {
		case Alert:
			return new AlertService(username, address, message);
		case Sms:
			return new SmsService(username, address, message);
		case Email:
			return new EmailService(username, address, message);
		}
		return null;
	}
}
