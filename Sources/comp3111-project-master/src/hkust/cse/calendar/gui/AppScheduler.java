package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.locationstorage.LocationStorageController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserStorageController;
import hkust.cse.calendar.xmlfactory.ApptXmlFactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class AppScheduler extends JDialog implements ActionListener, ComponentListener {

	private JLabel syearL;
	public JTextField syearF;
	private JLabel smonthL;
	public JTextField smonthF;
	private JLabel sdayL;
	public JTextField sdayF;

	private JLabel eyearL;
	public JTextField eyearF;
	private JLabel emonthL;
	public JTextField emonthF;
	private JLabel edayL;
	public JTextField edayF;

	private JLabel sTimeHL;
	public JTextField sTimeH;
	private JLabel sTimeML;
	public JTextField sTimeM;

	private JLabel eTimeHL;
	public JTextField eTimeH;
	private JLabel eTimeML;
	public JTextField eTimeM;

	private DefaultListModel model;
	private JTextField titleField;
	private JButton jointBut;

	public JRadioButton freqonetime;
	public JRadioButton freqdaily;
	public JRadioButton freqweekly;
	public JRadioButton freqmonthly;
	private ButtonGroup freqbg;

	//	private JCheckBox weekEvery;
	//	private JCheckBox weekMon;
	//	private JCheckBox weekTue;
	//	private JCheckBox weekWed;
	//	private JCheckBox weekThur;
	//	private JCheckBox weekFri;
	//	private JCheckBox weekSat;
	//	private JCheckBox weekSun;
	//	private ButtonGroup weekgp;

	private JComboBox<Location> locationlist;
	private JComboBox reminderField;

	private JRadioButton publicevent;
	private JRadioButton privateevent;
	private ButtonGroup eventbg;

	private JButton saveBut;
	private JButton CancelBut;
	private JButton acceptBut;
	private JButton rejectBut;

	private Appt NewAppt;
	private CalGrid parent;
	private LocationStorageController locationController;
	private UserStorageController userController;
	private boolean isNew = true;
	private boolean isChanged = true;
	private boolean isJoint = false;
	
	private LinkedList<String> invited;
	private User[] invitedppl;

	private JTextArea detailArea;

	private JSplitPane pDes;
	JPanel detailPanel;

	//	private JTextField attendField;
	//	private JTextField rejectField;
	//	private JTextField waitingField;
	private int selectedApptId = -1;

	private void commonConstructor(String title, CalGrid cal, boolean inivitation) {
		parent = cal;
		this.setAlwaysOnTop(false);
		setTitle(title);
		setModal(false);

		userController = UserStorageController.getInstance();
		
		Container contentPane;
		contentPane = getContentPane();

		JPanel psDate = new JPanel();
		Border sdateBorder = new TitledBorder(null, "START DATE");
		psDate.setBorder(sdateBorder);
		syearL = new JLabel("YEAR: ");
		psDate.add(syearL);
		syearF = new JTextField(5);
		psDate.add(syearF);
		smonthL = new JLabel("MONTH: ");
		psDate.add(smonthL);
		smonthF = new JTextField(4);
		psDate.add(smonthF);
		sdayL = new JLabel("DAY: ");
		psDate.add(sdayL);
		sdayF = new JTextField(4);
		psDate.add(sdayF);

		/*JPanel pDate = new JPanel();
		pDate.setLayout(new BorderLayout());

		pDate.add("West", psDate);
		pDate.add("East", peDate);*/

		JPanel psTime = new JPanel();
		Border stimeBorder = new TitledBorder(null, "START TIME");
		psTime.setBorder(stimeBorder);
		sTimeHL = new JLabel("HOUR: ");
		psTime.add(sTimeHL);
		sTimeH = new JTextField(8);
		psTime.add(sTimeH);
		sTimeML = new JLabel("MINUTE: ");
		psTime.add(sTimeML);
		sTimeM = new JTextField(8);
		psTime.add(sTimeM);

		JPanel peTime = new JPanel();
		Border etimeBorder = new TitledBorder(null, "END TIME");
		peTime.setBorder(etimeBorder);
		eTimeHL = new JLabel("HOUR: ");
		peTime.add(eTimeHL);
		eTimeH = new JTextField(8);
		peTime.add(eTimeH);
		eTimeML = new JLabel("MINUTE: ");
		peTime.add(eTimeML);
		eTimeM = new JTextField(8);
		peTime.add(eTimeM);

		JPanel pTime = new JPanel();
		pTime.setLayout(new BorderLayout());
		pTime.add("West", psTime);
		pTime.add("East", peTime);

		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		top.setBorder(new BevelBorder(BevelBorder.RAISED));

		top.add(psDate, BorderLayout.NORTH);
		top.add(pTime, BorderLayout.CENTER);

		contentPane.add("North", top);

		JPanel freqPanel = new JPanel();
		freqPanel.setLayout(new BorderLayout());

		JPanel frequencyPanel = new JPanel();
		Border frequencyBorder = new TitledBorder(null, "FREQUENCY");
		frequencyPanel.setBorder(frequencyBorder);
		freqonetime = new JRadioButton("One Time ");
		freqdaily = new JRadioButton("Daily ");
		freqweekly = new JRadioButton("Weekly ");
		freqmonthly = new JRadioButton("Monthly ");
		freqbg = new ButtonGroup();
		freqonetime.setActionCommand("OneTime");
		freqdaily.setActionCommand("Daily");
		freqweekly.setActionCommand("Weekly");
		freqmonthly.setActionCommand("Monthly");		
		freqbg.add(freqonetime);
		freqbg.add(freqdaily);
		freqbg.add(freqweekly);
		freqbg.add(freqmonthly);
		frequencyPanel.add(freqonetime);
		frequencyPanel.add(freqdaily);
		frequencyPanel.add(freqweekly);
		frequencyPanel.add(freqmonthly);

		freqonetime.setSelected(true);
		freqonetime.addActionListener(this);
		freqdaily.addActionListener(this);
		freqweekly.addActionListener(this);
		freqmonthly.addActionListener(this);

		JPanel peDate = new JPanel();
		Border edateBorder = new TitledBorder(null, "END DATE");
		peDate.setBorder(edateBorder);
		eyearL = new JLabel("YEAR: ");
		peDate.add(eyearL);
		eyearF = new JTextField(5);
		eyearF.setEditable(false);
		eyearF.setEnabled(false);
		peDate.add(eyearF);
		emonthL = new JLabel("MONTH: ");
		peDate.add(emonthL);
		emonthF = new JTextField(4);
		emonthF.setEditable(false);
		emonthF.setEnabled(false);
		peDate.add(emonthF);
		edayL = new JLabel("DAY: ");
		peDate.add(edayL);
		edayF = new JTextField(4);
		edayF.setEditable(false);
		edayF.setEnabled(false);
		peDate.add(edayF);

		freqPanel.add(frequencyPanel, BorderLayout.NORTH);
		freqPanel.add(peDate, BorderLayout.SOUTH);

		JPanel titleAndTextPanel = new JPanel();
		Border titleBorder = new TitledBorder(null, "TITLE");
		titleField = new JTextField(30);
		titleField.setEditable(true);
		jointBut = new JButton("Invite Friend(s)");
		jointBut.addActionListener(this);
		titleAndTextPanel.setBorder(titleBorder);
		titleAndTextPanel.add(titleField);
		titleAndTextPanel.add(jointBut);

		JPanel locationPanel = new JPanel();
		Border locationBorder = new TitledBorder(null, "LOCATION");
		locationPanel.setBorder(locationBorder);
		Location[] l = locationController.retrieveLocations();
		String[] ls = new String[l.length];
		for(int i = 0; i < l.length; i++) {
			ls[i] = l[i].getLocationName();
		}
		locationlist = new JComboBox<Location>(l);
		locationlist.setEditable(false);
		locationlist.addActionListener(this);
		locationPanel.add(locationlist);

		JPanel reminderPanel = new JPanel();
		Border reminderBorder = new TitledBorder(null, "REMINDER");
		reminderPanel.setBorder(reminderBorder);
		String[] reminderList = {"Off", "0","15","30","45","60"};
		reminderField = new JComboBox<String>(reminderList);
		reminderField.setEditable(false);
		reminderPanel.add(reminderField);

		JPanel info1 = new JPanel();
		info1.setLayout(new BorderLayout());
		info1.add(freqPanel, BorderLayout.NORTH);
		info1.add(titleAndTextPanel, BorderLayout.SOUTH);

		JPanel info2 = new JPanel();
		info2.setLayout(new BorderLayout());
		info2.add(locationPanel, BorderLayout.NORTH);
		info2.add(reminderPanel, BorderLayout.SOUTH);

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BorderLayout());
		infoPanel.add(info1, BorderLayout.NORTH);
		infoPanel.add(info2, BorderLayout.SOUTH);

		detailPanel = new JPanel();
		detailPanel.setLayout(new BorderLayout());
		Border detailBorder = new TitledBorder(null, "Appointment Description");
		detailPanel.setBorder(detailBorder);
		detailArea = new JTextArea(10, 10);

		detailArea.setEditable(true);
		JScrollPane detailScroll = new JScrollPane(detailArea);
		detailPanel.add(detailScroll);

		pDes = new JSplitPane(JSplitPane.VERTICAL_SPLIT, infoPanel,
				detailPanel);

		top.add(pDes, BorderLayout.SOUTH);

		if (NewAppt != null) {
			detailArea.setText(NewAppt.getInfo());
		}

		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.RIGHT));


		publicevent = new JRadioButton("Public");
		privateevent = new JRadioButton("Private");
		eventbg = new ButtonGroup();
		publicevent.setActionCommand("publicevent");
		privateevent.setActionCommand("privateevent");	
		eventbg.add(publicevent);
		eventbg.add(privateevent);
		panel2.add(publicevent);
		panel2.add(privateevent);
		
		privateevent.setSelected(true);
		publicevent.addActionListener(this);
		privateevent.addActionListener(this);
		
		if(inivitation==true){
			acceptBut = new JButton("Accept");
			acceptBut.addActionListener(this);
			panel2.add(acceptBut);		
			
			rejectBut = new JButton("Reject");
			rejectBut.addActionListener(this);
			panel2.add(rejectBut);
			rejectBut.show(true);
		}else{
			saveBut = new JButton("Save");
			saveBut.addActionListener(this);
			panel2.add(saveBut);
		}


		CancelBut = new JButton("Cancel");
		CancelBut.addActionListener(this);
		panel2.add(CancelBut);

		contentPane.add("South", panel2);
		//NewAppt = new Appt();	//YinYin: I think this a big bug

		if (this.getTitle().equals("Join Appointment Content Change") || this.getTitle().equals("Join Appointment Invitation")){
//			inviteBut.show(false);
			rejectBut.show(true);
			CancelBut.setText("Consider Later");
			saveBut.setText("Accept");
		}
		if (this.getTitle().equals("Someone has responded to your Joint Appointment invitation") ){
//			inviteBut.show(false);
			rejectBut.show(false);
			CancelBut.show(false);
			saveBut.setText("confirmed");
		}
		if (this.getTitle().equals("Join Appointment Invitation") || this.getTitle().equals("Someone has responded to your Joint Appointment invitation") || this.getTitle().equals("Join Appointment Content Change")){
			allDisableEdit();
		}
		pack();

	}

	AppScheduler(String title, CalGrid cal, int selectedApptId) {
		this.selectedApptId = selectedApptId;
		locationController = LocationStorageController.getInstance();
		if(title.equals("Invitation")){
			commonConstructor(title, cal,true);	
		}else{
			commonConstructor(title, cal,false);
		}
		
	}
	

	AppScheduler(String title, CalGrid cal) {
		locationController = LocationStorageController.getInstance();
		commonConstructor(title, cal,false);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == freqonetime) {
			eyearF.setEditable(false);
			eyearF.setEnabled(false);
			emonthF.setEditable(false);
			emonthF.setEnabled(false);
			edayF.setEditable(false);
			edayF.setEnabled(false);
		}
		else if(e.getSource() == freqdaily || e.getSource() == freqmonthly || e.getSource() == freqweekly) {
			eyearF.setEditable(true);
			eyearF.setEnabled(true);
			emonthF.setEditable(true);
			emonthF.setEnabled(true);
			edayF.setEditable(true);
			edayF.setEnabled(true);
		}

		if (e.getSource() == jointBut) {
			JointApptUserManager inviteuser = new JointApptUserManager(this.parent, AppScheduler.this, NewAppt);
		}
		else if (e.getSource() == CancelBut) {
			setVisible(false);
			dispose();
		} 
		else if (e.getSource() == saveBut) {
			boolean succeed = saveButtonResponse();
			if(succeed) {
				/* 
				 * This is for if the appt is modified from the UserApptManager,
				 * we would like to update the Appt List in the manager
				 * */
				if(UserApptManager.getInstance() != null) {
					UserApptManager.updateApptList();
				}
				
				setVisible(false);
				dispose();
			}
		}
		else if (e.getSource() == acceptBut) {
			
			if (JOptionPane.showConfirmDialog(this, "Accept this joint appointment?", "Confirmation", JOptionPane.YES_NO_OPTION) == 0){
				NewAppt.addAttendant(getCurrentUser());
				NewAppt.getRejectList().remove(getCurrentUser().ID());
				NewAppt.getWaitingList().remove(getCurrentUser().ID());
				
				ApptXmlFactory apptxmlfactory = new ApptXmlFactory();
				Appt[] appts = this.parent.controller.RetrieveAppts(NewAppt.getOwner(), NewAppt.getJoinID());
				for(Appt appt : appts){
					apptxmlfactory.removeApptFromXml("appt.xml", appt, NewAppt.getOwner().ID());
					apptxmlfactory.saveApptToXml("appt.xml", appt, NewAppt.getOwner().ID());					
				}

				this.setVisible(false);
				dispose();
			}
			
			/* 
			 * This is for if the appt is modified from the UserApptManager,
			 * we would like to update the Appt List in the manager
			 * */
			if(UserApptManager.getInstance() != null) {
				UserApptManager.updateApptList();
			}
			
			setVisible(false);
			dispose();
			
		} 
		else if (e.getSource() == rejectBut) {
			if (JOptionPane.showConfirmDialog(this, "Reject this joint appointment?", "Confirmation", JOptionPane.YES_NO_OPTION) == 0){
				
				NewAppt.addReject(getCurrentUser());
				NewAppt.getAttendList().remove(getCurrentUser().ID());
				NewAppt.getWaitingList().remove(getCurrentUser().ID());
				this.parent.controller.ManageAppt(NewAppt, ApptStorageControllerImpl.REMOVE);
				ApptXmlFactory apptxmlfactory = new ApptXmlFactory();
				Appt[] appts = this.parent.controller.RetrieveAppts(NewAppt.getOwner(), NewAppt.getJoinID());
				for(Appt appt : appts){
					apptxmlfactory.removeApptFromXml("appt.xml", appt, NewAppt.getOwner().ID());
//					apptxmlfactory.saveApptToXml("appt.xml", appt, NewAppt.getOwner().ID());
				}

			};
			this.setVisible(false);
			dispose();
		}

		
		parent.getAppList().clear();
		parent.updateAppList();
		parent.repaint();
	}

	private JPanel createPartOperaPane() {
		JPanel POperaPane = new JPanel();
		JPanel browsePane = new JPanel();
		JPanel controPane = new JPanel();

		POperaPane.setLayout(new BorderLayout());
		TitledBorder titledBorder1 = new TitledBorder(BorderFactory
				.createEtchedBorder(Color.white, new Color(178, 178, 178)),
				"Add Participant:");
		browsePane.setBorder(titledBorder1);

		POperaPane.add(controPane, BorderLayout.SOUTH);
		POperaPane.add(browsePane, BorderLayout.CENTER);
		POperaPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		return POperaPane;

	}

	private int[] getValidDate() {

		int[] date = new int[3];
		date[0] = Utility.getNumber(syearF.getText());
		date[1] = Utility.getNumber(smonthF.getText());
		if (date[0] < 1980 || date[0] > 2100) {
			JOptionPane.showMessageDialog(this, "Please input proper year of the start date",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (date[1] <= 0 || date[1] > 12) {
			JOptionPane.showMessageDialog(this, "Please input proper month of the start date",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		date[2] = Utility.getNumber(sdayF.getText());
		int monthDay = CalGrid.monthDays[date[1] - 1];
		if (date[1] == 2) {
			GregorianCalendar c = new GregorianCalendar();
			if (c.isLeapYear(date[0]))
				monthDay = 29;
		}
		if (date[2] <= 0 || date[2] > monthDay) {
			JOptionPane.showMessageDialog(this,
					"Please input proper month day of the start date", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return date;
	}
	private int[] getValidEndDate() {

		// valide and get the end date 
		int[] date = new int[3];
		date[0] = Utility.getNumber(eyearF.getText());
		date[1] = Utility.getNumber(emonthF.getText());
		if (date[0] < 1980 || date[0] > 2100) {
			JOptionPane.showMessageDialog(this, "Please input proper year of the end date",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (date[1] <= 0 || date[1] > 12) {
			JOptionPane.showMessageDialog(this, "Please input proper month of the end date",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		date[2] = Utility.getNumber(edayF.getText());
		int monthDay = CalGrid.monthDays[date[1] - 1];
		if (date[1] == 2) {
			GregorianCalendar c = new GregorianCalendar();
			if (c.isLeapYear(date[0]))
				monthDay = 29;
		}
		if (date[2] <= 0 || date[2] > monthDay) {
			JOptionPane.showMessageDialog(this,
					"Please input proper month day of the end date", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return date;
	}


	private int getTime(JTextField h, JTextField min) {

		int hour = Utility.getNumber(h.getText());
		if (hour == -1)
			return -1;
		int minute = Utility.getNumber(min.getText());
		if (minute == -1)
			return -1;

		return (hour * 60 + minute);

	}
	
	private int getReminder(JComboBox reminder) {

		int minute = Utility.getNumber(reminder.getSelectedItem().toString());
		if (minute == -1)
			return -1;

		return (minute);

	}

	private String rollBackReminderTimestamp(Appt appt) {
		if(appt.getReminder().getReminderTimestamp() == null) {
			return "Off";
		}
		else {
			return new Integer(new TimeSpan(appt.TimeSpan().StartTime(),appt.getReminder().getReminderTimestamp()).TimeLength()).toString();
		}	
	}	

	private int[] getValidTimeInterval() {

		int[] result = new int[2];
		result[0] = getTime(sTimeH, sTimeM);
		result[1] = getTime(eTimeH, eTimeM);
		if ((result[0] % 15) != 0 || (result[1] % 15) != 0) {
			JOptionPane.showMessageDialog(this,
					"Minute Must be 0, 15, 30, or 45 !", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		if (!sTimeM.getText().equals("0") && !sTimeM.getText().equals("15") && !sTimeM.getText().equals("30") && !sTimeM.getText().equals("45") 
				|| !eTimeM.getText().equals("0") && !eTimeM.getText().equals("15") && !eTimeM.getText().equals("30") && !eTimeM.getText().equals("45")){
			JOptionPane.showMessageDialog(this,
					"Minute Must be 0, 15, 30, or 45 !", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		if (result[1] == -1 || result[0] == -1) {
			JOptionPane.showMessageDialog(this, "Please check time",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (result[1] <= result[0]) {
			JOptionPane.showMessageDialog(this,
					"End time should be bigger than \nstart time",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if ((result[0] < AppList.OFFSET * 60)
				|| (result[1] > (AppList.OFFSET * 60 + AppList.ROWNUM * 2 * 15))) {
			JOptionPane.showMessageDialog(this, "Out of Appointment Range !",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return result;
	}

	private boolean saveButtonResponse() {
		
		// create an appt
		int[] time = getValidTimeInterval();
		int[] date = getValidDate();

		if(time == null || date == null) {
			return false;
		}

		Timestamp start = CreateTimeStamp(date, time[0]);
		Timestamp end = CreateTimeStamp(date, time[1]);
		TimeSpan startTime = new TimeSpan(start, end);
		TimeSpan startDate = new TimeSpan(startTime);
		
		String freq = freqbg.getSelection().getActionCommand();
		TimeSpan endTime = startTime;
		if(freq != "OneTime") {
			int[] enddate = getValidEndDate();
			Timestamp start_of_end_date = CreateTimeStamp(enddate,time[0]);
			Timestamp end_of_end_date = CreateTimeStamp(enddate,time[1]);
			endTime = new TimeSpan(start_of_end_date,end_of_end_date);
		}

		Timestamp reminder = Utility.convertReminderToTimestamp(getReminder(reminderField), start);
				
		NewAppt.setLocation((Location) locationlist.getSelectedItem());
		NewAppt.setTimeSpan(startTime);
		NewAppt.setStartDate(startDate);
		NewAppt.setEndDate(endTime);
		NewAppt.setTitle(titleField.getText());
		NewAppt.setInfo(detailArea.getText());
		NewAppt.setReminderTime(reminder);
		NewAppt.setFrequency(freq.trim());
		NewAppt.setOwner(getCurrentUser());
		
		boolean isPublic = eventbg.getSelection().getActionCommand() == "publicevent" ? true : false;
		NewAppt.setIsPublic(isPublic);
		boolean isJoint = NewAppt.getAllPeople().isEmpty();
		NewAppt.setJoint(!isJoint);
		
		// set id & joint id for old appt		
		if (selectedApptId != -1){ // old appt
			NewAppt.setID(selectedApptId);
			if(isJoint){  
				NewAppt.setJoinID(-1);
			}else if(!isJoint && NewAppt.getJoinID()==-1){
				int joinid = this.parent.controller.getAssignedJointID();
				NewAppt.setJoinID(joinid);
				this.parent.controller.setAssignedApptID(joinid+1);
			}

		}else{ // new appt
			int apptid = this.parent.controller.getAssignedApptID();
			NewAppt.setID(apptid);
			this.parent.controller.setAssignedApptID(apptid+1);
			if(!isJoint){ // !isJoint = joint appt
				int joinid = this.parent.controller.getAssignedJointID();
				NewAppt.setJoinID(joinid);
				this.parent.controller.setAssignedApptID(joinid+1);
			}
		}


		ArrayList<Appt> apptlist = new ArrayList<Appt>();
		apptlist.add(NewAppt);


		int frequency = -1; // that mean one time event
		if (freq == "Daily"){
			frequency = TimeSpan.DAY;
		}
		else if (freq == "Weekly") {
			frequency = TimeSpan.WEEK;
		}
		else if (freq == "Monthly") {
			frequency = TimeSpan.MONTH;
		}
		 // if it is not one time event, we recursively schedule new appts until the end date
		if(frequency != -1) {
			Utility.createRepeatingAppts(NewAppt, frequency, apptlist,getReminder(reminderField));
		}

		// overlap checking
		if(this.parent.controller.checkOverLaps(apptlist)) {
			JOptionPane.showMessageDialog(null, "Appointment Overlapped!" , "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
//		System.out.println(this.parent.controller.checkotherApptsHaveLocation(NewAppt, locationlist.getSelectedItem().toString()));
		if(this.parent.controller.checkotherApptsHaveLocation(NewAppt, locationlist.getSelectedItem().toString())) {
			JOptionPane.showMessageDialog(null, "Appointment Overlapped! Your location has been used by other people in this timeslot." , "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		// overlap checking GROUP event
		if(NewAppt.isJoint()){
			invited = NewAppt.getAllPeople();
			invitedppl = initializeUserList(invited);
			if(!this.parent.controller.checkotherUsersTimespan(startTime, invitedppl)){
				JOptionPane.showMessageDialog(null, "Appointment Overlapped! Other people used in this timeslot." , "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if(!this.parent.controller.checkLocationCapacityEnough(NewAppt)) {
				JOptionPane.showMessageDialog(null, "Location Capacity not enough!" , "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		// if we are modifing an appt, remove the old appts in the memory first
		if(selectedApptId != -1){
			this.parent.controller.removeApptFromXml(NewAppt);
			this.parent.controller.ManageAppt(NewAppt, parent.controller.REMOVE); // for memory
		}

		// store the appts to the memory
		for (Appt appt : apptlist) {
			// System.out.println(this.parent.controller.getAssignedApptID());
			this.parent.controller.ManageAppt(appt, parent.controller.NEW); // for memory
		}

		this.parent.controller.saveApptToXml(NewAppt);

		return true;
	}
	
	private User[] initializeUserList(LinkedList<String> ppl) {
		User[] suggestedpeople = new User[ppl.size()];
		int i = 0;
		User[] all = userController.retrieveUsers();
		for (User people : all) {
			if (ppl.contains(people.ID())) {
				suggestedpeople[i] = people;
				i++;
			}
		}
		return suggestedpeople;
	}

	private Timestamp CreateTimeStamp(int[] date, int time) {
		Timestamp stamp = new Timestamp(0);
		stamp.setYear(date[0]);
		stamp.setMonth(date[1] - 1);
		stamp.setDate(date[2]);
		stamp.setHours(time / 60);
		stamp.setMinutes(time % 60);
		return stamp;
	}

	// set value of modify view
	public void updateSetApp(Appt appt) {

		NewAppt = appt;
		
		Timestamp start = appt.TimeSpan().StartTime();
		Timestamp end = appt.TimeSpan().EndTime();
		
		if(selectedApptId != -1) {
			locationlist.setSelectedItem(appt.getLocation());
			reminderField.setSelectedItem(rollBackReminderTimestamp(appt));
			if (!appt.getFrequency().equals("OneTime")) {

				if (appt.getFrequency().equals("Daily")) {
					freqdaily.setSelected(true);
				} 
				else if (appt.getFrequency().equals("Weekly")) {
					freqweekly.setSelected(true);
				} 
				else if (appt.getFrequency().equals("Monthly")) {
					freqmonthly.setSelected(true);
				}
				
				eyearF.setEditable(true);
				eyearF.setEnabled(true);
				emonthF.setEditable(true);
				emonthF.setEnabled(true);
				edayF.setEditable(true);
				edayF.setEnabled(true);
			}
			
			start = appt.getStartDate().StartTime();
			end = appt.getStartDate().EndTime();
		}
		
		Timestamp start_of_end_date = appt.getEndDate().StartTime();

		setTitle(appt.getTitle());
		titleField.setText(appt.getTitle());
		detailArea.setText(appt.getInfo());
		syearF.setText(String.valueOf(start.getYear()+1900));
		smonthF.setText(String.valueOf(start.getMonth()+1));
		sdayF.setText(String.valueOf(start.getDate()));

		eyearF.setText(String.valueOf(start_of_end_date.getYear()+1900));
		emonthF.setText(String.valueOf(start_of_end_date.getMonth()+1));
		edayF.setText(String.valueOf(start_of_end_date.getDate()));

		sTimeH.setText(String.valueOf(start.getHours()));
		sTimeM.setText(String.valueOf(start.getMinutes()));
		eTimeH.setText(String.valueOf(end.getHours()));
		eTimeM.setText(String.valueOf(end.getMinutes()));
		
		if(appt.isPublic()) {
			publicevent.setSelected(true);
		}
		else {
			privateevent.setSelected(true);
		}
	}

	public void componentHidden(ComponentEvent e) {

	}

	public void componentMoved(ComponentEvent e) {

	}

	public void componentResized(ComponentEvent e) {

		Dimension dm = pDes.getSize();
		double width = dm.width * 0.93;
		double height = dm.getHeight() * 0.6;
		detailPanel.setSize((int) width, (int) height);

	}

	public void componentShown(ComponentEvent e) {

	}

	public User getCurrentUser()		// get the id of the current user
	{
		return this.parent.mCurrUser;
	}

	public void allDisableEdit() {
		timeDisableEdit();
		freqdaily.setEnabled(false);
		freqweekly.setEnabled(false);
		freqmonthly.setEnabled(false);
		freqonetime.setEnabled(false);
		reminderField.setEditable(false);
		publicevent.setEnabled(false);
		privateevent.setEnabled(false);
		titleField.setEditable(false);
		detailArea.setEditable(false);
	}
	
	public void timeDisableEdit() {
		syearF.setEditable(false);
		smonthF.setEditable(false);
		sdayF.setEditable(false);
		eyearF.setEditable(false);
		emonthF.setEditable(false);
		edayF.setEditable(false);
		sTimeH.setEditable(false);
		sTimeM.setEditable(false);
		eTimeH.setEditable(false);
		eTimeM.setEditable(false);
	}

	public static void testu(Appt appt) {
		/*for (Appt lol = appt ; lol.TimeSpan().EndTime().before(new Timestamp(114,10,30,23,59,59,0)) ; lol = lol.clone()){
			System.out.println(lol.TimeSpan().StartTime()+" "+lol.getReminder().getReminderTimestamp());
		}*/
	}	
	public static Date setTime(int hour,int minute) {
		Calendar c = GregorianCalendar.getInstance();
		Date d = c.getTime();
		d.setMinutes(d.getMinutes()+1);
		return d;

	}

	//	private void setfreqbutton(boolean select) {
	//		weekMon.setSelected(select);
	//		weekTue.setSelected(select);
	//		weekWed.setSelected(select);
	//		weekThur.setSelected(select);
	//		weekFri.setSelected(select);
	//		weekSat.setSelected(select);
	//		weekSun.setSelected(select);
	//	}

	//	private void disablefreqbutton() {
	//		weekEvery.setEnabled(false);
	//		weekMon.setEnabled(false);
	//		weekTue.setEnabled(false);;
	//		weekWed.setEnabled(false);
	//		weekThur.setEnabled(false);
	//		weekFri.setEnabled(false);
	//		weekSat.setEnabled(false);
	//		weekSun.setEnabled(false);
//		}
	
	
}