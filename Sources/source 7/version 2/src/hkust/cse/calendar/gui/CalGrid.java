package hkust.cse.calendar.gui;

import hkust.cse.calendar.Main.CalendarMain;
import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.apptstorage.ApptStorageNullImpl;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.SingletonMediator;
import hkust.cse.calendar.unit.SingletonMediator.NotifyInitiator;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.metal.MetalBorders.Flush3DBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class CalGrid extends JFrame implements ActionListener {
	
	
	/*only for an administrator*/
	private CalGrid userRef;
	private JFrame adminFrame;
	private JButton killAdminFrame;
	private Vector<User> usersVect;
	/*up to here*/
	
	
	private BufferedWriter fileWriter;

	private static final long serialVersionUID = 1L;
	public ApptStorageControllerImpl controller;
	public User mCurrUser;
	private String mCurrTitle = "Desktop Calendar - No User - ";
	private GregorianCalendar today;
	public int currentD;
	public int currentM;
	public int currentY;
	public int previousRow;
	public int previousCol;
	public int currentRow;
	public int currentCol;
	private BasicArrowButton eButton;
	private BasicArrowButton wButton;
	private JLabel year;
	private JComboBox month;
	
	private final Object[][] data = new Object[6][7];
	//private final Vector[][] apptMarker = new Vector[6][7];
	private final String[] names = { "Sunday", "Monday", "Tuesday",
			"Wednesday", "Thursday", "Friday", "Saturday" };
	private final String[] months = { "January", "Feburary", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };
	JTable tableView;
	public static AppList applist;
	private Appt[][] allApptsForMonth;
	public static final int[] monthDays = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
			31, 30, 31 };
	private JTextPane note;

	private JSplitPane upper;
	private JSplitPane whole;
	private JScrollPane scrollpane;
	private StyledDocument mem_doc = null;
	private SimpleAttributeSet sab = null;
	// private boolean isLogin = false;
	private JMenu Appmenu = new JMenu("Appointment");
	
	private GroupInvitationDialogue[] groupInvite;
	private DeleteUserConfirm[] deleteUser;
	private DeleteUserConfirm[] deleteLocation;
	//private 
	
	private final String[] holidays = {
			"New Years Day\nSpring Festival\n",
			"President's Day (US)\n",
			"",
			"Ching Ming Festival\nGood Friday\nThe day following Good Friday\nEaster Monday\n",
			"Labour Day\nThe Buddha’s Birthday\nTuen Ng Festival\n",
			"",
			"Hong Kong Special Administrative Region Establishment Day\n",
			"Civic Holiday(CAN)\n",
			"",
			"National Day\nChinese Mid-Autumn Festival\nChung Yeung Festival\nThanksgiving Day\n",
			"Veterans Day(US)\nThanksgiving Day(US)\n", "Christmas\n" };

	private AppScheduler setAppDial;

	public CalGrid(ApptStorageControllerImpl con) {
		super();
		
		
		
		controller = con;
		mCurrUser = null;
		
		
		

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				
				
				
				try{
					controller.saveApptToXml(); // save appointments to an XML file
					
					if(controller.getDefaultUser().getAdminStatus()){
						controller.getDefaultUser().setUserLocations(controller.getLocationList()); // save the user�s locations
						//SingletonMediator.getBrainInstance().setAllLocations(SingletonMediator.getBrainInstance().getAllLocations()); // the singletonMediator stores a copy of locations as well
						//hkust.cse.calendar.unit.SingletonMediator.getBrainInstance().saveAllLocations();; // save all the locations to an XML file
					}
					hkust.cse.calendar.unit.SingletonMediator.getBrainInstance().saveAllLocations(); // save all the locations to an XML file
					fileWriter = new BufferedWriter(new FileWriter("savedUsers.xml")); // initialize the fileWriter variable to a file
					fileWriter.write(hkust.cse.calendar.Main.CalendarMain.xstreamRetrieve.toXML(SingletonMediator.getBrainInstance().getAllUsers()));
					
					
				}catch(IOException exc){
					throw new RuntimeException(exc);
				}finally{
					if(fileWriter.equals(null) == false){
						try{fileWriter.close();} catch(IOException etr){}
					}
				}
				/*=================================================up to here==========================================================*/
				
				
				
				System.exit(0);
			}
		});


		/*for an administrator only*/
		adminFrame = null;
		userRef = null;
		killAdminFrame = new JButton(); // only for notifying an action
		killAdminFrame.addActionListener(this);
		killAdminFrame.setVisible(false);
		usersVect = null;
		//controller = con;
		//mCurrUser = null;
		
		setAppDial = null;
		previousRow = 0;
		previousCol = 0;
		currentRow = 0;
		currentCol = 0;

		applist = new AppList();
		applist.setParent(this);

		setJMenuBar(createMenuBar());

		today = new GregorianCalendar();
		currentY = today.get(Calendar.YEAR);
		currentD = today.get(today.DAY_OF_MONTH);
		//currentD = 15;
		int temp = today.get(today.MONTH) + 1;
		//System.out.println(today.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
		currentM = 12;

		getDateArray(data);
		
		
		
		JPanel leftP = new JPanel();
		leftP.setLayout(new BorderLayout());
		leftP.setPreferredSize(new Dimension(500, 300));

		JLabel textL = new JLabel("Important Days");
		textL.setForeground(Color.red);

		note = new JTextPane();
		note.setEditable(false);
		note.setBorder(new Flush3DBorder());
		mem_doc = note.getStyledDocument();
		sab = new SimpleAttributeSet();
		StyleConstants.setBold(sab, true);
		StyleConstants.setFontSize(sab, 30);

		JPanel noteP = new JPanel();
		noteP.setLayout(new BorderLayout());
		noteP.add(textL, BorderLayout.NORTH);
		noteP.add(note, BorderLayout.CENTER);

		leftP.add(noteP, BorderLayout.CENTER);

		eButton = new BasicArrowButton(SwingConstants.EAST);
		eButton.setEnabled(true);
		eButton.addActionListener(this);
		wButton = new BasicArrowButton(SwingConstants.WEST);
		wButton.setEnabled(true);
		wButton.addActionListener(this);

		year = new JLabel(new Integer(currentY).toString());
		month = new JComboBox();
		month.addActionListener(this);
		month.setPreferredSize(new Dimension(200, 30));
		for (int cnt = 0; cnt < 12; cnt++)
			month.addItem(months[cnt]);
		month.setSelectedIndex(temp - 1);

		JPanel yearGroup = new JPanel();
		yearGroup.setLayout(new FlowLayout());
		yearGroup.setBorder(new Flush3DBorder());
		yearGroup.add(wButton);
		yearGroup.add(year);
		yearGroup.add(eButton);
		yearGroup.add(month);

		leftP.add(yearGroup, BorderLayout.NORTH);

		TableModel dataModel = prepareTableModel();
		
		tableView = new JTable(dataModel) {
			public TableCellRenderer getCellRenderer(int row, int col) {
				
				String tem = (String) data[row][col];
				Appt[] apptsForToday = getApptsForDay((String)data[row][col]);
			
				if (tem.equals("") == false) {
					try {
						if (today.get(Calendar.YEAR) == currentY
								&& today.get(today.MONTH) + 1 == currentM
								&& today.get(today.DAY_OF_MONTH) == Integer
										.parseInt(tem)) {
							return new CalCellRenderer(true, apptsForToday.length);
						}
						else {
							return new CalCellRenderer(false, apptsForToday.length);
						}
					} catch (Throwable e) {
						System.exit(1);
					}

				}
				return new CalCellRenderer(false, 0);
			}
		};

		tableView.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tableView.setRowHeight(40);
		tableView.setRowSelectionAllowed(false);
		tableView.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				//System.out.println("I am clicking here!");
				mousePressResponse();
			}

			public void mouseReleased(MouseEvent e) {
				//System.out.println("I am releasing here!");
				mouseResponse();
			}
		});

		JTableHeader head = tableView.getTableHeader();
		head.setReorderingAllowed(false);
		head.setResizingAllowed(true);

		scrollpane = new JScrollPane(tableView);
		scrollpane.setBorder(new BevelBorder(BevelBorder.RAISED));
		scrollpane.setPreferredSize(new Dimension(536, 260));

		upper = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftP, scrollpane);

		whole = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upper, applist);
		getContentPane().add(whole);

		
		initializeSystem(); // for you to add.
		//mCurrUser = getCurrUser(); // totally meaningless code
		Appmenu.setEnabled(true);

		UpdateCal();
		pack();				// sized the window to a preferred size
		setVisible(true);	//set the window to be visible
	}

	private Appt[] getApptsForDay(String day) {
		Appt[] tempAppt1 = null;
		Appt[] tempAppt2 = null;
		if(!day.equals("")){
			Timestamp morning = new Timestamp(Integer.parseInt(year.getText()), month.getSelectedIndex(), Integer.parseInt(day), 7, 59, 0, 0);
			//System.out.println(morning.toString());
			Timestamp evening = new Timestamp(Integer.parseInt(year.getText()), month.getSelectedIndex(), Integer.parseInt(day), 18, 1, 0, 0);
			//System.out.println(evening.toString());
			TimeSpan fullDay = new TimeSpan(morning, evening);
			//System.out.println(fullDay.toString());
			
			tempAppt1 = controller.RetrieveAppts(fullDay);
		}
		
		
		return tempAppt1;
	}

	public TableModel prepareTableModel() {

		TableModel dataModel = new AbstractTableModel() {

			public int getColumnCount() {
				return names.length;
			}

			public int getRowCount() {
				return 6;
			}

			public Object getValueAt(int row, int col) {
				
				return data[row][col];
			}

			public String getColumnName(int column) {
				return names[column];
			}

			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				return false;
			}

			public void setValueAt(Object aValue, int row, int column) {
				System.out.println("Setting value to: " + aValue);
				data[row][column] = aValue;
			}
		};
		return dataModel;
	}

	public void getDateArray(Object[][] data) {
		GregorianCalendar c = new GregorianCalendar(currentY, currentM - 1, 1);
		int day;
		int date;
		Date d = c.getTime();
		c.setTime(d);
		day = d.getDay();
		date = d.getDate();

		if (c.isLeapYear(currentY)) {

			monthDays[1] = 29;
		} else
			monthDays[1] = 28;

		int temp = day - date % 7;
		if (temp > 0)
			day = temp + 1;
		else if (temp < 0)
			day = temp + 1 + 7;
		else
			day = date % 7;
		day %= 7;
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 7; j++) {
				int temp1 = i * 7 + j - day + 1;
				if (temp1 > 0 && temp1 <= monthDays[currentM - 1])
					data[i][j] = new Integer(temp1).toString();
				else
					data[i][j] = new String("");
					//data[i][j] = new Integer(temp1).toString();
			}

	}

	JMenuBar createMenuBar() {

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("Manual Scheduling")) {
					controller.setLocationList(SingletonMediator.getBrainInstance().getAllLocations());
					//AppScheduler a = new AppScheduler("New", CalGrid.this);
					setAppDial = new AppScheduler("New", CalGrid.this);
					if(setAppDial.noLocationsCheck() == false){// if the object has no locations, return
						return;
					}
					
					setAppDial.updateSetApp(hkust.cse.calendar.gui.Utility
							.createDefaultAppt(currentY, currentM, currentD,
									mCurrUser));
					setAppDial.setLocationRelativeTo(null);
					setAppDial.show();
					
					//if(a.noLocationsCheck() == false){ // if the object has no locations, return
					//	return;
					//}
					
					//a.updateSetApp(hkust.cse.calendar.gui.Utility
					//		.createDefaultAppt(currentY, currentM, currentD,
					//				mCurrUser));
					//a.setLocationRelativeTo(null);
					//a.show();
					TableModel t = prepareTableModel();
					tableView.setModel(t);
					tableView.repaint();
				}

			}
		};
		JMenuBar menuBar = new JMenuBar();
		menuBar.getAccessibleContext().setAccessibleName("Calendar Choices");
		JMenuItem mi;
		
		JMenu Access = (JMenu) menuBar.add(new JMenu("Access"));
		Access.setMnemonic('A');
		Access.getAccessibleContext().setAccessibleDescription(
				"Account Access Management");

		mi = (JMenuItem) Access.add(new JMenuItem("Logout"));
		mi.setMnemonic('L');
		mi.getAccessibleContext().setAccessibleDescription("For user logout");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null, "Logout?",
						"Comfirm", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION){
					//controller.dumpStorageToFile();
					//System.out.println("closed");
					if(setAppDial != null){
						setAppDial.setVisible(false);
						setAppDial.dispose();
					}
					
					killAdminFrame.doClick(); // this button gives a notification to the administrator�s window in order to kill it
					if(adminFrame != null){ // if the administrator�s window is used, destroy it
						adminFrame.setVisible(false);
						adminFrame.dispose();
					}
					
					try{
						//hkust.cse.calendar.Main.CalendarMain.fileWriter.write("=====================Something is bad\n=====================");
						controller.saveApptToXml();
						
						if(controller.getDefaultUser().getAdminStatus()){
							controller.getDefaultUser().setUserLocations(controller.getLocationList());// set the user�s locations
							//hkust.cse.calendar.unit.SingletonMediator.getBrainInstance().setAllLocations(SingletonMediator.getBrainInstance().getAllLocations()); // the singletonMediator stores a copy of locations as well
							//hkust.cse.calendar.unit.SingletonMediator.getBrainInstance().saveAllLocations();; // save all the locations to an XML file
						}
						
						hkust.cse.calendar.unit.SingletonMediator.getBrainInstance().saveAllLocations(); // save all the locations to an XML file
						
						fileWriter = new BufferedWriter(new FileWriter("savedUsers.xml"));
						fileWriter.write(hkust.cse.calendar.Main.CalendarMain.xstreamRetrieve.toXML(SingletonMediator.getBrainInstance().getAllUsers()));
						
					
					}catch(IOException exc){
						throw new RuntimeException(exc);
					}finally{
						if(fileWriter.equals(null) == false){
							try{fileWriter.close();} catch(IOException etr){}
						}
					}
					/*=================================================up to here==========================================================*/
					
					
					dispose();
					CalendarMain.logOut = true;
					return;	//return to CalendarMain()
				}
			}
		});
		
		mi = (JMenuItem) Access.add(new JMenuItem("Exit"));
		mi.setMnemonic('E');
		mi.getAccessibleContext().setAccessibleDescription("Exit Program");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null, "Exit Program ?",
						"Comfirm", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION){
					
					if(adminFrame != null){ // if the administrator's window is used, destroy it
						adminFrame.setVisible(false);
						adminFrame.dispose();
					}
				//System.out.println("=================The saving file information\n" + hkust.cse.calendar.Main.CalendarMain.xstreamRetrieve.toXML(hkust.cse.calendar.gui.LoginDialog.userList));
					
					try{
						controller.saveApptToXml(); // save appointments to an XML file
						
							if(controller.getDefaultUser().getAdminStatus()){
								controller.getDefaultUser().setUserLocations(controller.getLocationList()); // set the list of locations
								//SingletonMediator.getBrainInstance().setAllLocations(controller.getLocationList()); // the singletonMediator stores a copy of locations as well
							}
						SingletonMediator.getBrainInstance().saveAllLocations(); // save all the locations to an XML file
						
						
						fileWriter = new BufferedWriter(new FileWriter("savedUsers.xml"));
						fileWriter.write(hkust.cse.calendar.Main.CalendarMain.xstreamRetrieve.toXML(SingletonMediator.getBrainInstance().getAllUsers()));
						
						//hkust.cse.calendar.Main.CalendarMain.fileWriter.write(hkust.cse.calendar.Main.CalendarMain.xstreamRetrieve.toXML(hkust.cse.calendar.gui.LoginDialog.userList));
					
					}catch(IOException exc){
						throw new RuntimeException(exc);
					}finally{
						if(fileWriter.equals(null) == false){
							try{fileWriter.close();} catch(IOException etr){}
						}
					}
					
					
					
					
					System.exit(0);
				}

			}
		});

		menuBar.add(Appmenu);
		Appmenu.setEnabled(false);
		Appmenu.setMnemonic('p');
		Appmenu.getAccessibleContext().setAccessibleDescription(
				"Appointment Management");
		mi = new JMenuItem("Manual Scheduling");
		mi.addActionListener(listener);
		Appmenu.add(mi);
		
		/*added by me*/
		/*controller.getDefaultUser().setUserLocations(controller.getLocationList()); // copy the locations to the user
		mi = new JMenuItem("Manage Locations");
		mi.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				LocationsDialog dlg = new LocationsDialog(controller);
			}
		});
		Appmenu.add(mi);*/
		/*up to here*/
		
		
		
		/*=====================================================================added by me again====================================================================*/
		/*JMenuItem userData;
		JMenu ManageUser = (JMenu) menuBar.add(new JMenu("Manage User"));
		userData = (JMenuItem) ManageUser.add(new JMenuItem("Manage User"));
		userData.setMnemonic('M');
		userData.getAccessibleContext().setAccessibleDescription("Manage user infromation");
		userData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserManagement userMan = new UserManagement(mCurrUser);
			}
			
	   });*/
		
		/*=====================================================================up to here===========================================================================*/
		
		/*==========================================================================added by me for an administrator=================================================*/
		if((controller.getDefaultUser().getAdminStatus())){ // check if the current user is an administrator
			
			controller.getDefaultUser().setUserLocations(controller.getLocationList()); // copy the locations to the user
			//mi = new JMenuItem("Manage Locations");
			/*mi.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					LocationsDialog dlg = new LocationsDialog(controller);
				}
			});*/
			//Appmenu.add(mi);
			
			
			
			/*an administrator has a separate JFrame which consists of the administrator�s functions*/
			adminFrame = new JFrame();
			adminFrame.setTitle("Administrator's window");
			
			/*set some restrictions on the frame*/
			adminFrame.setLayout(new BorderLayout());
			adminFrame.setLocationByPlatform(true);
			adminFrame.setSize(300, 140);
			adminFrame.setResizable(false); // do not allow to resize the dialog box
			adminFrame.setAlwaysOnTop(true); // bring the user�s frame in front of the rest of frames 
			
			/*add a panel which contains all the functions*/
			
			JPanel adminPanel = new JPanel();
			adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.Y_AXIS));
			
			/*for the Drop Box fields*/
			JPanel panelDropBox = new JPanel();
			panelDropBox.add(new JLabel("Users: "));
			usersVect = new Vector<User>(hkust.cse.calendar.unit.SingletonMediator.getBrainInstance().getAllUsers().values()); // convert a hash map into a list
			JComboBox<User> users = new JComboBox<User>(usersVect);
			
			/*set root as the default value*/
			int sizeOfVector = usersVect.size(); // the size of the above vector 
			for(int i = 0;  i < sizeOfVector; i++){
				if(users.getItemAt(i).toString().equals("root")){// if the item is "root", set it and break
					users.setSelectedIndex(i);
					break;
				}
		
			} 
			
			
			//users.setPrototypeDisplayValue(mCurrUser); // in order to set the length of the Drop Box
			panelDropBox.add(users);
			panelDropBox.setBackground(Color.green);
			adminPanel.add(panelDropBox);
			
			/*for buttons*/
			JPanel btnPanel = new JPanel();
			JButton locButton = new JButton("     Locations     ");
			locButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					LocationsDialog dlg = new LocationsDialog(controller);
				}
			});
			
			JButton delButton = new JButton("Delete user");
			delButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
				 System.out.println("The selected user is being deleted \"" + users.getSelectedItem().toString() + "\"");
				 if(users.getSelectedItem().toString().equals("root")) {
					 JOptionPane.showMessageDialog(null, "The \"root\" user cannot be deleted");
					 return;
				 }
				 /*if the being deleted user is not root, delete it and remove from all the lists*/
				 String delUserName = users.getSelectedItem().toString();
				 int indexDel = users.getSelectedIndex();
				 SingletonMediator.getBrainInstance().deleteUser(delUserName);
				 //users.remove(indexDel);// remove the user's item
				 //hkust.cse.calendar.unit.SingletonMediator.getBrainInstance().deleteUser(users.getSelectedItem().toString()); // use the SingletonMediator object to delete the selected user
				 JOptionPane.showMessageDialog(null, delUserName + " has been deleted"); // give some feedback to the user
				 usersVect.remove(indexDel); // remove the item from the vector 
				}
			});
			
			btnPanel.add(locButton, BorderLayout.CENTER);
			btnPanel.add(delButton, BorderLayout.PAGE_END);
			btnPanel.setBackground(Color.green);
			adminPanel.add(btnPanel);
			
			
			
			adminPanel.setBackground(Color.green);
			adminFrame.add(adminPanel); // add everything to the main frame
			adminFrame.setVisible(true); // make the frame visible
			adminFrame.setBackground(Color.green);
			
			
			JMenuItem userData;
			JMenu ManageUser = (JMenu) menuBar.add(new JMenu("Manage User"));
			userData = (JMenuItem) ManageUser.add(new JMenuItem("Manage User"));
			userData.setMnemonic('M');
			userData.getAccessibleContext().setAccessibleDescription("Manage user infromation");
			userData.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new UserManagement(mCurrUser);
				}
				
		   });
						
			/*change the calgrid so that it would react to the selected user*/
			/*============================================================================================================================*/
			users.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					User tempInterface = hkust.cse.calendar.unit.SingletonMediator.getBrainInstance().getAnyUser(users.getSelectedItem().toString());// retrieve the selected user
					if(userRef != null){
						userRef.setVisible(false);
						userRef.dispose();
						userRef = null; // kill a user�s CalGrid
					}
					
					setVisible(false); // kill the administrator�s CalGrid (keep it in memory)
					if((tempInterface.getAdminStatus() == false) && (userRef == null)){
						userRef = new CalGrid(new ApptStorageControllerImpl(new ApptStorageNullImpl(tempInterface)));
						userRef.killAdminFrame.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent click){
								SingletonMediator.getBrainInstance().setAllLocations(controller.getLocationList()); // the singletonMediator stores a copy of locations as well
								SingletonMediator.getBrainInstance().saveAllLocations();; // save all the locations to an XML file
									adminFrame.setVisible(false);
									adminFrame.dispose();
									setVisible(false);
									dispose();
								
							}
						});
					}else{
						setVisible(true); // if the root user is chosen again, just display his CalGrid
					}
				}
			});
			/*use the SingletonMediator class to retrieve every appointment*/					
			
		}else{// if the user is not an administrator, create is simple menuBar;
			/*that's only for an administrator*/
			JMenuItem userData;
			JMenu ManageUser = (JMenu) menuBar.add(new JMenu("Manage User"));
			userData = (JMenuItem) ManageUser.add(new JMenuItem("Manage User"));
			userData.setMnemonic('M');
			userData.getAccessibleContext().setAccessibleDescription("Manage user infromation");
			userData.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new UserManagement(mCurrUser);
				}
				
		   });
		}
		
		
		/*=============================================================================up to here====================================================================*/
		
		
		return menuBar;
	}

	private void initializeSystem() {

		mCurrUser = this.controller.getDefaultUser();	//get User from controller
		SingletonMediator.getBrainInstance().setUser(mCurrUser);
		System.out.println("in here");
		ArrayList<Appt> groupAppts = SingletonMediator.getBrainInstance().notifyGroupUsers();
		groupInvite = new GroupInvitationDialogue[groupAppts.size()];
		
		for(int i=0; i<groupAppts.size(); i++) {
			groupInvite[i] = new GroupInvitationDialogue(this, groupAppts.get(i));
		}
		
		ArrayList<NotifyInitiator> listDeleteUser = SingletonMediator.getBrainInstance().getNotifyInitiatorList();
		System.out.println("Size of notify arraylist: " + listDeleteUser.size());
		deleteUser = new DeleteUserConfirm[listDeleteUser.size()];
		for(int i=0; i<listDeleteUser.size(); i++) {
			System.out.println("prompting users one by one");
			deleteUser[i] = new DeleteUserConfirm(this, listDeleteUser.get(i));
		}
		
		ArrayList<NotifyInitiator> listDeleteLocation = SingletonMediator.getBrainInstance().getNotifyInitiatorListLocation();
		System.out.println("Locations to be deleted received." + listDeleteLocation.size());
		deleteLocation = new DeleteUserConfirm[listDeleteLocation.size()];
		for(int i=0; i<listDeleteLocation.size(); i++) {
			System.out.println("prompting locations one by one");
			deleteLocation[i] = new DeleteUserConfirm(this, listDeleteLocation.get(i));
		}
		checkUpdateJoinAppt();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == eButton) {
			if (year == null)
				return;
			currentY = currentY + 1;
			year.setText(new Integer(currentY).toString());
			CalGrid.this.setTitle("Desktop Calendar - No User - (" + currentY
					+ "-" + currentM + "-" + currentD + ")");
			getDateArray(data);
			if (tableView != null) {
				TableModel t = prepareTableModel();
				tableView.setModel(t);
				tableView.repaint();

			}
			UpdateCal();
		} else if (e.getSource() == wButton) {
			if (year == null)
				return;
			currentY = currentY - 1;
			year.setText(new Integer(currentY).toString());
			CalGrid.this.setTitle("Desktop Calendar - No User - (" + currentY
					+ "-" + currentM + "-" + currentD + ")");
			getDateArray(data);
			if (tableView != null) {
				TableModel t = prepareTableModel();
				tableView.setModel(t);
				tableView.repaint();

			}
			UpdateCal();
		} else if (e.getSource() == month) {
			if (month.getSelectedItem() != null) {
				currentM = month.getSelectedIndex() + 1;
				try {
					mem_doc.remove(0, mem_doc.getLength());
					mem_doc.insertString(0, holidays[currentM - 1], sab);
				} catch (BadLocationException e1) {

					e1.printStackTrace();
				}

				CalGrid.this.setTitle("Desktop Calendar - No User - ("
						+ currentY + "-" + currentM + "-" + currentD + ")");
				getDateArray(data);
				if (tableView != null) {
					TableModel t = prepareTableModel();
					tableView.setModel(t);
					tableView.repaint();
				}
				UpdateCal();
			}
		}
	}

	// update the appointment list on gui
	public void updateAppList() {
		applist.clear();
		applist.repaint();
		applist.setTodayAppt(getApptsForDay(new Integer(currentD).toString()));
	}

	public void UpdateCal() {
		if (mCurrUser != null) {
			mCurrTitle = "Desktop Calendar - " + mCurrUser.ID() + " - ";
			this.setTitle(mCurrTitle + "(" + currentY + "-" + currentM + "-"
					+ currentD + ")");
			Appt[] monthAppts = null;
			GetMonthAppts();

//			for (int i = 0; i < 6; i++)
//				for (int j = 0; j < 7; j++)
//					apptMarker[i][j] = new Vector(10, 1);

			TableModel t = prepareTableModel();
			this.tableView.setModel(t);
			this.tableView.repaint();
			updateAppList();
		}
	}

//	public void clear() {
//		for (int i = 0; i < 6; i++)
//			for (int j = 0; j < 7; j++)
//				apptMarker[i][j] = new Vector(10, 1);
//		TableModel t = prepareTableModel();
//		tableView.setModel(t);
//		tableView.repaint();
//		applist.clear();
//	}

	private Appt[] GetMonthAppts() {
		Timestamp start = new Timestamp(0);
		start.setYear(currentY);
		start.setMonth(currentM - 1);
		start.setDate(1);
		start.setHours(0);
		Timestamp end = new Timestamp(0);
		end.setYear(currentY);
		end.setMonth(currentM - 1);
		GregorianCalendar g = new GregorianCalendar(currentY, currentM - 1, 1);
		end.setDate(g.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
		end.setHours(23);
		TimeSpan period = new TimeSpan(start, end);
		return controller.RetrieveAppts(mCurrUser, period);
	}

	private void mousePressResponse() {
		previousRow = tableView.getSelectedRow();
		previousCol = tableView.getSelectedColumn();
	}
	
	private void mouseResponse() {
		int[] selectedRows = tableView.getSelectedRows();
		int[] selectedCols = tableView.getSelectedColumns();
		if(tableView.getSelectedRow() == previousRow && tableView.getSelectedColumn() == previousCol){
			currentRow = selectedRows[selectedRows.length - 1];
			currentCol = selectedCols[selectedCols.length - 1];
		}
		else if(tableView.getSelectedRow() != previousRow && tableView.getSelectedColumn() == previousCol){
			currentRow = tableView.getSelectedRow();
			currentCol = selectedCols[selectedCols.length - 1];
		}
		else if(tableView.getSelectedRow() == previousRow && tableView.getSelectedColumn() != previousCol){
			currentRow = selectedRows[selectedRows.length - 1];
			currentCol = tableView.getSelectedColumn();
		}
		else{
			currentRow = tableView.getSelectedRow();
			currentCol = tableView.getSelectedColumn();
		}
		
		if (currentRow > 5 || currentRow < 0 || currentCol < 0
				|| currentCol > 6)
			return;

		if (tableView.getModel().getValueAt(currentRow, currentCol) != "")
			try {
				currentD = new Integer((String) tableView.getModel()
						.getValueAt(currentRow, currentCol)).intValue();
			} catch (NumberFormatException n) {
				return;
			}
		CalGrid.this.setTitle(mCurrTitle + "(" + currentY + "-" + currentM
				+ "-" + currentD + ")");
		updateAppList();
	}

	public boolean IsTodayAppt(Appt appt) {
		if (appt.TimeSpan().StartTime().getYear() + 1900 != currentY)
			return false;
		if ((appt.TimeSpan().StartTime().getMonth() + 1) != currentM)
			return false;
		if (appt.TimeSpan().StartTime().getDate() != currentD)
			return false;
		return true;
	}

	public boolean IsMonthAppts(Appt appt) {

		if (appt.TimeSpan().StartTime().getYear() + 1900 != currentY)
			return false;

		if ((appt.TimeSpan().StartTime().getMonth() + 1) != currentM)
			return false;
		return true;
	}

	public Appt[] GetTodayAppt() {
		Integer temp;
		temp = new Integer(currentD);
		Timestamp start = new Timestamp(0);
		start.setYear(currentY);
		start.setMonth(currentM-1);
		start.setDate(currentD);
		start.setHours(0);
		start.setMinutes(0);
		start.setSeconds(0);
		
		Timestamp end = new Timestamp(0);
		end.setYear(currentY);
		end.setMonth(currentM-1);
		end.setDate(currentD);
		end.setHours(23);
		end.setMinutes(59);
		end.setSeconds(59);
		
		TimeSpan period = new TimeSpan(start, end);
		return controller.RetrieveAppts(mCurrUser, period);
	}

	public AppList getAppList() {
		return applist;
	}

	public User getCurrUser() {
		return mCurrUser;
	}
	
	// check for any invite or update from join appointment
	public void checkUpdateJoinAppt(){
		// Fix Me!
	}

}
