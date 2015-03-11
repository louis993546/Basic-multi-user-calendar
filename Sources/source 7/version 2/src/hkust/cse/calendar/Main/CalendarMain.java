//main method, code starts here
package hkust.cse.calendar.Main;


import javax.swing.UIManager;

import hkust.cse.calendar.gui.LoginDialog;
import hkust.cse.calendar.unit.SingletonMediator;
import hkust.cse.calendar.unit.User;

import com.thoughtworks.xstream.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import hkust.cse.calendar.unit.AdminUser;

import java.lang.NullPointerException;

public class CalendarMain{
	public static boolean logOut = false;
	private static User root;
	private static HashMap<String, User> userListMain;
	public static XStream xstreamRetrieve;
	//public static BufferedWriter fileWriter;
	//private static Object readObject;
	
	public static void main (String[] args) {
		while(true){
			logOut = false;
			try{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}catch(Exception e){
				
			}
			try{
				/*first create a read file*/
				
				xstreamRetrieve = new XStream();
				
				xstreamRetrieve.alias("map", java.util.HashMap.class);
				xstreamRetrieve.aliasField("string", hkust.cse.calendar.unit.User.class, "User");
				
			}
			finally{}
			/*catch(IOException e){
				throw new RuntimeException(e);
				//System.out.println("Something went wrong");
				//return;
			}*/
			
					try{
						//userListMain = (HashMap<String,User>)xstreamRetrieve.fromXML(new File("C:\\Users\\User\\Desktop\\Justino Lingio\\Studies\\2014\\2014_Fall\\Comp Courses\\Comp 3111\\Project\\Code\\calendar\\savedUsers.xml"));
						userListMain = (HashMap<String,User>)xstreamRetrieve.fromXML(new File("savedUsers.xml"));
						//readObject = xstreamRetrieve.fromXML(new File("C:\\Users\\User\\Desktop\\Justino Lingio\\Studies\\2014\\2014_Fall\\Comp Courses\\Comp 3111\\Project\\Code\\calendar\\savedUsers.xml"));
					}catch(Exception e){
						if(e.getMessage().equals("-1") == true){
							System.out.println("==============================It doesn't save to an XML file===========================");
							userListMain = new HashMap<String,User>();
							root = new AdminUser("root", "systemadmin", "root", "root", "root@stu.ust.hk", userListMain); // hard code an adminUser object
							userListMain.put("root", root); // save the root user
						}else{
							if(e.getMessage().equals("AWT-EventQueue-0")){
								userListMain = new HashMap<String,User>();
								root = new AdminUser("root", "systemadmin", "root", "root", "root@stu.ust.hk", userListMain); // hard code an adminUser object
								userListMain.put("root", root); // save the root user
							}
						}
						
					} finally {
						SingletonMediator.getBrainInstance().setUserHashMap(userListMain);		
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
		