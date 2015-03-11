//main method, code starts here
package hkust.cse.calendar.Main;


import javax.swing.*;

import hkust.cse.calendar.apptstorage.ApptDB;
import hkust.cse.calendar.apptstorage.LocationDB;
import hkust.cse.calendar.apptstorage.UserDB;
import hkust.cse.calendar.gui.LoginDialog;


public class CalendarMain {
	public static boolean logOut = false;
	
	public static void main(String[] args) {
		//starting point
		while(true){
			logOut = false;
			try{
		//	UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}catch(Exception e){
				
			}
			LoginDialog loginDialog = new LoginDialog(); //create a new Login Dialog
			
			//Create/read database
			ApptDB adb = new ApptDB();
			UserDB udb = new UserDB();
			LocationDB ldb = new LocationDB();
			
			//go to constructor of LoginDialog.java
			while(logOut == false){
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
		