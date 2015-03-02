//main method, code starts here
package hkust.cse.calendar.Main;


import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

import hkust.cse.calendar.gui.LoginDialog;
import hkust.cse.calendar.gui.ReminderCheck;


public class CalendarMain {
	public static boolean logOut = false;
	
	
	public static void main(String[] args) {
		while(true){
			logOut = false;
			try{
		//	UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}catch(Exception e){
			}
			LoginDialog loginDialog = new LoginDialog();
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
		