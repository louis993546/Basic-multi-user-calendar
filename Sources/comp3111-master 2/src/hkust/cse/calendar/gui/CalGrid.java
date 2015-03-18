package hkust.cse.calendar.gui;

import hkust.cse.calendar.Main.CalendarMain;
import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.PendingEngine;
import hkust.cse.calendar.unit.PendingRequest;
import hkust.cse.calendar.unit.TimeMachine;
import hkust.cse.calendar.listener.TimeMachineListener;
import hkust.cse.calendar.notification.EmailService;
import hkust.cse.calendar.notification.INotification;
import hkust.cse.calendar.notification.NotificationFactory;
import hkust.cse.calendar.notification.NotificationService;
import hkust.cse.calendar.notification.SmsService;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.user.User;
import hkust.cse.calendar.gui.LocationsDialog;
import hkust.cse.calendar.unit.user.UserManagement;

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
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
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
import javax.tools.JavaFileManager.Location;


public class CalGrid extends JFrame implements ActionListener, TimeMachineListener {

	// private User mNewUser;
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
	private JTable tableView;
	private AppList applist;
	public static final int[] monthDays = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
			31, 30, 31 };
	private JTextPane note;

	private JSplitPane upper;
	private JSplitPane whole;
	private JScrollPane scrollpane;
	private StyledDocument mem_doc = null;
	private SimpleAttributeSet sab = null;
	// private boolean isLogin = false;
	private JMenu Appmenu = new JMenu("Appointment");;

	private final String[] holidays = {
			"New Years Day\nSpring Festival\n",
			"President's Day (US)\n",
			"",
			"Ching Ming Festival\nGood Friday\nThe day following Good Friday\nEaster Monday\n",
			"Labour Day\nThe Buddha��s Birthday\nTuen Ng Festival\n",
			"",
			"Hong Kong Special Administrative Region Establishment Day\n",
			"Civic Holiday(CAN)\n",
			"",
			"National Day\nChinese Mid-Autumn Festival\nChung Yeung Festival\nThanksgiving Day\n",
			"Veterans Day(US)\nThanksgiving Day(US)\n", "Christmas\n" };

	private AppScheduler setAppDial;
	public TimeMachine timeMachine;
	private TimeMachineDialog tm;
	private NotificationFactory nfactory;

	public CalGrid(ApptStorageControllerImpl con) {
		super();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				CalGrid.this.controller.PutApptToXml();
				CalGrid.this.controller.PutLocToXml();
				controller.PutUserToXml();
				System.exit(0);
			}
		});

		controller = con;
		mCurrUser = null;
		nfactory = new NotificationFactory();

		previousRow = 0;
		previousCol = 0;
		currentRow = 0;
		currentCol = 0;
		timeMachine = new TimeMachine();
		timeMachine.addElpasedListener(this);
		//controller.setTimeMachine(timeMachine);

		applist = new AppList();
		applist.setParent(this);

		setJMenuBar(createMenuBar());

		today = new GregorianCalendar();
		currentY = today.get(Calendar.YEAR);
		currentD = today.get(today.DAY_OF_MONTH);
		int temp = today.get(today.MONTH) + 1;
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
				CalCellRenderer renderer = null;
				
				if (tem.equals("") == false) {
					try {
						if (today.get(Calendar.YEAR) == currentY
								&& today.get(today.MONTH) + 1 == currentM
								&& today.get(today.DAY_OF_MONTH) == Integer
										.parseInt(tem)) {
							// this renderer is used to mark the current day as red color
							renderer = new CalCellRenderer(today);
						}
					} catch (Throwable e) {
						System.exit(1);
					}

				}
				
				// this renderer is used to mark all day or blank column except today
				renderer = new CalCellRenderer(null);
				
				if(!tem.equals("")) {
					if(controller.RetrieveAppts(mCurrUser, new TimeSpan(
							new Timestamp(currentY, currentM - 1, Integer.parseInt(tem), 0, 0, 0, 0),
							new Timestamp(currentY, currentM - 1, Integer.parseInt(tem), 23, 59, 59, 0))).length > 0)
						renderer.setBackground(Color.cyan);
				}
				
				return renderer;
			}
		};

		tableView.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tableView.setRowHeight(40);
		tableView.setRowSelectionAllowed(false);
		tableView.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				mousePressResponse();
			}

			public void mouseReleased(MouseEvent e) {
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
		
		//Pending Engine
		for(PendingRequest request: PendingEngine.getInstance().checkPendingRequest(mCurrUser)) {
			
			if(request.getType() == PendingRequest.REMOVE_LOCATION) {
				String loc = (String)request.getObj();
				
				JOptionPane.showMessageDialog(null, "The location \"" + loc + "\" has been removed by admin " + request.getFrom().ID() + "." ,"Info", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(request.getType() == PendingRequest.REMOVE_APPOINTMENT) {
				String title = (String)request.getObj();
				
				JOptionPane.showMessageDialog(null, "The appointment \"" + title + "\" has been removed due to location removed." ,"Info", JOptionPane.INFORMATION_MESSAGE);
			}
			
			/*
			if(request.getType() == PendingRequest.TYPE_LOCATION) {
				
				hkust.cse.calendar.unit.Location loc = (hkust.cse.calendar.unit.Location)request.getObj();
    			int n = JOptionPane.showConfirmDialog(null, "The user \"" + request.getFrom().ID() + "\" is requested to add a location \"" + loc.getName() + "\". Do you accept the request?",
    					"Confirm", JOptionPane.YES_NO_OPTION);
    			if (n == JOptionPane.YES_OPTION) {
    				
    				hkust.cse.calendar.unit.Location[] locations = controller.getLocationList();
    				ArrayList<hkust.cse.calendar.unit.Location> locations_arr = new ArrayList<hkust.cse.calendar.unit.Location>(Arrays.asList(locations));
    				
    				Boolean found = false;
    				for(hkust.cse.calendar.unit.Location tmp : locations_arr) {
    					if(tmp.getName().equals(loc.getName()))
    						found = true;
    				}
    				
    				if(!found) {
    					locations_arr.add(loc);
    					
    					
    					controller.setLocationList(locations_arr.toArray(locations));
    					
    					JOptionPane.showMessageDialog(null, "The location \"" + loc.getName() + "\" has been added." ,"Info", JOptionPane.INFORMATION_MESSAGE);
    				}
    				else {
    					
    					JOptionPane.showMessageDialog(null, "The location \"" + loc.getName() + "\" exists. No change has been made." ,"Info", JOptionPane.INFORMATION_MESSAGE);
    					
    				}
    				
    			}
    			else {
    				
    				JOptionPane.showMessageDialog(null, "You have declined the request." ,"Info", JOptionPane.INFORMATION_MESSAGE);
    			}
				
			}*/
			
			
			
		}

		UpdateCal();
		pack();				// sized the window to a preferred size
		setVisible(true);	//set the window to be visible
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
			}

	}

	JMenuBar createMenuBar() {

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("Manual Scheduling")) {
					if(controller.getLocationList().length==0)
						JOptionPane.showMessageDialog(null, "Cannot Make An Appointment Because Of No Locations !",
								"Error", JOptionPane.ERROR_MESSAGE);
					else{
						AppScheduler a = new AppScheduler("New", CalGrid.this);
						a.updateSetApp(hkust.cse.calendar.gui.Utility
								.createDefaultAppt(currentY, currentM, currentD,
										mCurrUser));
						a.setLocationRelativeTo(null);
						a.show();
					}
					TableModel t = prepareTableModel();
					tableView.setModel(t);
					tableView.repaint();
				}
				else if(e.getActionCommand().equals("Time Machine")) {
					if(tm == null) tm = new TimeMachineDialog(timeMachine);
					tm.setLocationRelativeTo(null);
					tm.show();
					//TableModel t = prepareTableModel();
					//tableView.setModel(t);
					//tableView.repaint();
				}
				else if(e.getActionCommand().equals("Settings")) {
					UserSettings a = new UserSettings(mCurrUser);
					a.setLocationRelativeTo(null);
					a.show();
				}
				else if(e.getActionCommand().equals("Create Group Event")) {
					GroupEventDialog g = new GroupEventDialog(controller);
					g.show();
				}
				else if(e.getActionCommand().equals("Bookmarks")) {
					BookmarkList g = new BookmarkList(CalGrid.this);
					g.show();
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

		mi = (JMenuItem) Access.add(new JMenuItem("Settings"));
		mi.addActionListener(listener);
		
		mi = (JMenuItem) Access.add(new JMenuItem("Logout"));	//adding a Logout menu button for user to logout
		mi.setMnemonic('L');
		mi.getAccessibleContext().setAccessibleDescription("For user logout");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null, "Logout?",
						"Comfirm", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION){
					//controller.dumpStorageToFile();
					//System.out.println("closed");
					CalGrid.this.controller.PutApptToXml();
					CalGrid.this.controller.PutLocToXml();
					controller.PutUserToXml();
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
					CalGrid.this.controller.PutApptToXml();
					CalGrid.this.controller.PutLocToXml();
					controller.PutUserToXml();
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
		
		mi = new JMenuItem("Time Machine");
		mi.addActionListener(listener);
		Appmenu.add(mi);

		//Appmenu.add(mi);

		//only allow admin modify location
		if(this.controller.getDefaultUser().IsAdmin()) {
			mi = new JMenuItem("Manage Location");
			mi.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0){
					LocationsDialog dlg = new LocationsDialog(controller);
				}
			
				});
	
			Appmenu.add(mi);
		}
		
		if(this.controller.getDefaultUser().IsAdmin()) {
			
			JMenu Admin = (JMenu) menuBar.add(new JMenu("Administration"));
			mi = (JMenuItem) Admin.add(new JMenuItem("Inspect User"));
			mi.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					UserList list = new UserList(CalGrid.this);
					list.show();
				}
			
				});
			
			/*
			mi = (JMenuItem) Admin.add(new JMenuItem("Pending Request"));
			mi.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0){
					
					
				}
			
				});
			*/
		}
		
		mi = new JMenuItem("Create Group Event");
		mi.addActionListener(listener);
		Appmenu.add(mi);

		mi = new JMenuItem("Bookmarks");
		mi.addActionListener(listener);
		Appmenu.add(mi);
		
		return menuBar;
	}

	private void initializeSystem() {

		mCurrUser = this.controller.getDefaultUser();	//get User from controller
		controller.LoadApptFromXml();
		controller.LoadLocFromXml();
		// Fix Me !
		// Load the saved appointments from disk
		checkUpdateJoinAppt();
	}
	
	
	public void timeElapsed(TimeMachine sender) {
		if(sender.IsRewind()) {
			//remove any event that are derived from schedule within the rewind time
			//Appt[] pastAppts = controller.RetrieveAppts(mCurrUser, new TimeSpan(
			
			return;
		}
		
		Timestamp curr = sender.getCurrentTime();
		Appt[] appts = controller.RetrieveAppts(mCurrUser, new TimeSpan(
				new Timestamp(curr.getYear(), curr.getMonth(), curr.getDate(), 0, 0, 0, 0),
				new Timestamp(curr.getYear(), curr.getMonth(), curr.getDate(), 23, 59, 59, 0)));
		String info = "";
		String infosms = "";
		String infoemail = "";
		String message = "";

		//the timestamp to check next occurrence event (if no reminder need)
		Timestamp next = (Timestamp)sender.getCurrentTime().clone();
		next.setTime(sender.getCurrentTime().getTime() + sender.getTimeDelay());
		
		//another timestamp to check next need reminder event
		Timestamp next2 = (Timestamp)sender.getCurrentTime().clone();
		next2.setTime(sender.getCurrentTime().getTime());
		
		for(Appt appt : appts) {
			//next occurrence event at next time
			if(curr.before(appt.TimeSpan().StartTime()) && appt.TimeSpan().StartTime().compareTo(next) <= 0) {
				//check if this appt is scheduled task or not
				if(appt.getFrequency() != Appt.SINGLE) {
					//scheduled task & create past event
					//check if past event has been created in case rewind
					next2.setTime(appt.TimeSpan().StartTime().getTime() + 15 * 60000);
					Appt[] pastAppts = controller.RetrieveAppts(mCurrUser, new TimeSpan(appt.TimeSpan().StartTime(), next2));
					
					// only scheduled appt in the time slot, create an new appt here
					if(pastAppts.length == 1) {
						//uncomment this if remove future schedule apply
						//Appt pastAppt = (Appt)appt.clone();
						//pastAppt.setFrequency(Appt.SINGLE);
						//pastAppt.setDerivedFromSchedule(true);
						//controller.ManageAppt(pastAppt, ApptStorageControllerImpl.NEW);
					}
				}
			}
			
			if(appt.needReminder()) {
				Timestamp reminderTime = appt.getReminderTime();
				//offset start time by reminder time
				next2.setTime(appt.TimeSpan().StartTime().getTime() - (reminderTime.getHours() * 60 + reminderTime.getMinutes()) * 60000);
				if(curr.compareTo(next2) <= 0 && next2.compareTo(next) < 0) {
					message = appt.TimeSpan().StartTime().getHours() + ":" + appt.TimeSpan().StartTime().getMinutes() + "  " + appt.getTitle() + "\n";
					info += message;
					
					if(appt.sendSms())
						infosms += message;
					
					if(appt.sendEmail())
						infoemail += message;
				}
			}
		}
		
		if(!info.equals("")) {
			INotification alert = nfactory.GetNotificationService(NotificationService.Alert,
					"",
					"",
					"The following appointment(s) will be happened:" + "\n" + info
					);
			alert.Send();
		}
		
		if(!infosms.equals("")) {
			INotification sms = nfactory.GetNotificationService(NotificationService.Sms,
					mCurrUser.getFirstName() + " " + mCurrUser.getLastName(),
					mCurrUser.getPhoneNum(),
					infosms
					);
			sms.Send();
		}
		
		if(!infoemail.equals("")) {
			INotification email = nfactory.GetNotificationService(NotificationService.Email,
					mCurrUser.getFirstName() + " " + mCurrUser.getLastName(),
					mCurrUser.getEmail(),
					infoemail
					);
			email.Send();
		}
	}
	
	public void timeStopped(TimeMachine sender) {
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
		applist.setTodayAppt(GetTodayAppt());
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
		
		//get non past appointments that involve the current user
		TimeSpan currentTime = new TimeSpan(timeMachine.getCurrentTime(), new Timestamp(2300, 9, 9, 12, 0, 0, 0));
		Appt[] appts = controller.RetrieveAppts(mCurrUser, currentTime);
		for(int i=0; i<appts.length; i++) {
			//tell all participant that the appointment has changed
			//if(appts[i].getAllPeople().contains(mCurrUser.ID()) /* && content changed*/) {
			//	AppScheduler a = new AppScheduler("Join Appointment Content Change", CalGrid.this);
			//	a.show();
			//}
			
			//tell initiator if someone responded to the appointment
			//if(appts[i].getAttendList().getFirst() == mCurrUser.ID() /*&& someone responded to the appointment(either accept or reject)*/) {
			//	AppScheduler b = new AppScheduler("Someone has responded to your Joint Appointment invitation", CalGrid.this);
			//	b.show();
			//	//set the appointment back to no one responded
			//}
			
			//the current user is still in the waiting list of the appointment
			if(appts[i].getWaitingList().contains(mCurrUser.ID())) {
				AppScheduler c = new AppScheduler("Join Appointment Invitation", CalGrid.this);
				c.updateSetApp(appts[i]);
				c.show();
			}
		}
	}
	
	public void navigate(Appt appt) {
		currentY = appt.TimeSpan().StartTime().getYear() + 1900;
		year.setText(new Integer(currentY).toString());
		currentM = appt.TimeSpan().StartTime().getMonth() + 1;
		month.setSelectedIndex(currentM - 1);
		currentD = appt.TimeSpan().StartTime().getDate();
		UpdateCal();
	}

}
