package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Vector;
import java.util.Date;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;


public class AppScheduler extends JDialog implements ActionListener,
		ComponentListener {

	private JLabel yearL;
	private JTextField yearF;
	private JLabel monthL;
	private JTextField monthF;
	private JLabel dayL;
	private JTextField dayF;
	private JLabel sTimeHL;
	private JTextField sTimeH;
	private JLabel sTimeML;
	private JTextField sTimeM;
	private JLabel eTimeHL;
	private JTextField eTimeH;
	private JLabel eTimeML;
	private JTextField eTimeM;

	private DefaultListModel model;
	private JTextField titleField;

	private JTextField reminderField;
	
	private JButton saveBut;
	private JButton CancelBut;
	private JButton inviteBut;
	private JButton rejectBut;
	
	private Appt selectedAppt = null;
	private Appt NewAppt;
	private CalGrid parent;
	private boolean isNew = true;
	private boolean isChanged = true;
	private boolean isJoint = false;

	private JTextArea detailArea;
	private JComboBox frequencyField;
	private JComboBox reminderBox;
	private JComboBox locField;
	private JSplitPane pDes;
	private JSplitPane fDes;
	JPanel detailPanel;

	private JTextField attendField;
	private JTextField rejectField;
	private JTextField waitingField;
	private int selectedApptId = -1;
	private boolean groupappt = false;
	private LinkedList<String> invitedUserlist;
	private GregorianCalendar apptday;

	private void commonConstructor(String title, CalGrid cal) {
		parent = cal;
		this.setAlwaysOnTop(true);
		setTitle(title);
		setModal(false);

		Container contentPane;
		contentPane = getContentPane();
		
		JPanel pDate = new JPanel();
		Border dateBorder = new TitledBorder(null, "DATE");
		pDate.setBorder(dateBorder);

		yearL = new JLabel("YEAR: ");
		pDate.add(yearL);
		yearF = new JTextField(6);
		pDate.add(yearF);
		monthL = new JLabel("MONTH: ");
		pDate.add(monthL);
		monthF = new JTextField(4);
		pDate.add(monthF);
		dayL = new JLabel("DAY: ");
		pDate.add(dayL);
		dayF = new JTextField(4);
		pDate.add(dayF);

		JPanel psTime = new JPanel();
		Border stimeBorder = new TitledBorder(null, "START TIME");
		psTime.setBorder(stimeBorder);
		sTimeHL = new JLabel("Hour");
		psTime.add(sTimeHL);
		sTimeH = new JTextField(4);
		psTime.add(sTimeH);
		sTimeML = new JLabel("Minute");
		psTime.add(sTimeML);
		sTimeM = new JTextField(4);
		psTime.add(sTimeM);

		JPanel peTime = new JPanel();
		Border etimeBorder = new TitledBorder(null, "END TIME");
		peTime.setBorder(etimeBorder);
		eTimeHL = new JLabel("Hour");
		peTime.add(eTimeHL);
		eTimeH = new JTextField(4);
		peTime.add(eTimeH);
		eTimeML = new JLabel("Minute");
		peTime.add(eTimeML);
		eTimeM = new JTextField(4);
		peTime.add(eTimeM);

		JPanel pTime = new JPanel();
		pTime.setLayout(new BorderLayout());
		pTime.add("West", psTime);
		pTime.add("East", peTime);

		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		top.setBorder(new BevelBorder(BevelBorder.RAISED));
		top.add(pDate, BorderLayout.NORTH);
		top.add(pTime, BorderLayout.CENTER);

		contentPane.add("North", top);

		JPanel titleAndTextPanel = new JPanel();
		JLabel titleL = new JLabel("TITLE");
		titleField = new JTextField(15);
		titleAndTextPanel.add(titleL);
		titleAndTextPanel.add(titleField);
		
		Location[] locations = cal.controller.getLocationList();
		int size = 0;
		if(locations !=null)
			size = locations.length;
		String[] name = new String[size];
		if(locations == null)
			locations = new Location[0];
		else
			for(int i = 0;i < size;i++)
				name[i] = locations[i].getName();
		
		JLabel locationL = new JLabel("LOCATION");
		locField = new JComboBox<String>(name);
		titleAndTextPanel.add(locationL);
		titleAndTextPanel.add(locField);
		
		JPanel freqAndremindPanel = new JPanel();
		JLabel frequencyL = new JLabel("FREQUENCY");
		String[] frequencySelection = {"One Time", "Daily", "Weekly", "Monthly"};
		frequencyField = new JComboBox<String>(frequencySelection);
		freqAndremindPanel.add(frequencyL);
		freqAndremindPanel.add(frequencyField);
		
		JLabel reminderL = new JLabel("REMINDER");
		reminderField = new JTextField(3);
		freqAndremindPanel.add(reminderL);
		freqAndremindPanel.add(reminderField);
		String[] remindertime  = {"None", "Minutes", "Hours", "Days", "Weeks"};
		reminderBox = new JComboBox<String>(remindertime);
		freqAndremindPanel.add(reminderBox);
		
		detailPanel = new JPanel();
		detailPanel.setLayout(new BorderLayout());
		Border detailBorder = new TitledBorder(null, "Appointment Description");
		detailPanel.setBorder(detailBorder);
		detailArea = new JTextArea(20, 30);

		detailArea.setEditable(true);
		JScrollPane detailScroll = new JScrollPane(detailArea);
		detailPanel.add(detailScroll);


		fDes = new JSplitPane(JSplitPane.VERTICAL_SPLIT, titleAndTextPanel, freqAndremindPanel);
		
		pDes = new JSplitPane(JSplitPane.VERTICAL_SPLIT, fDes,
				detailPanel);

		top.add(pDes, BorderLayout.SOUTH);
		

		if (NewAppt != null) {
			detailArea.setText(NewAppt.getInfo());

		}
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.RIGHT));

		inviteBut = new JButton("Invite");
		inviteBut.addActionListener(this);
		panel2.add(inviteBut);
		
		saveBut = new JButton("Save");
		saveBut.addActionListener(this);
		panel2.add(saveBut);

		rejectBut = new JButton("Reject");
		rejectBut.addActionListener(this);
		panel2.add(rejectBut);
		rejectBut.show(false);

		CancelBut = new JButton("Cancel");
		CancelBut.addActionListener(this);
		panel2.add(CancelBut);

		contentPane.add("South", panel2);
		NewAppt = new Appt();

		if (this.getTitle().equals("Join Appointment Content Change") || this.getTitle().equals("Join Appointment Invitation")){
			inviteBut.show(false);
			rejectBut.show(true);
			CancelBut.setText("Consider Later");
			saveBut.setText("Accept");
		}
		if (this.getTitle().equals("Someone has responded to your Joint Appointment invitation") ){
			inviteBut.show(false);
			rejectBut.show(false);
			CancelBut.show(false);
			saveBut.setText("confirmed");
		}
		if (this.getTitle().equals("Join Appointment Invitation") || this.getTitle().equals("Someone has responded to your Joint Appointment invitation") || this.getTitle().equals("Join Appointment Content Change")){
			allDisableEdit();
		}
		pack();
		this.setAlwaysOnTop(true);

	}
	
	AppScheduler(String title, CalGrid cal, int selectedApptId) {
		this.selectedApptId = selectedApptId;
		commonConstructor(title, cal);
	}

	AppScheduler(String title, CalGrid cal) {
		commonConstructor(title, cal);
	}
	
	public void actionPerformed(ActionEvent e) {

		// distinguish which button is clicked and continue with require function
		if (e.getSource() == CancelBut) {

			setVisible(false);
			dispose();
		}  else if (e.getSource() == rejectBut){
			if (JOptionPane.showConfirmDialog(this, "Reject this joint appointment?", "Confirmation", JOptionPane.YES_NO_OPTION) == 0){
				NewAppt.addReject(getCurrentUser());
				NewAppt.getAttendList().remove(getCurrentUser());
				NewAppt.getWaitingList().remove(getCurrentUser());
				this.setVisible(false);
				dispose();
			}
		} else if (e.getSource()== inviteBut){
			//saveButtonResponse();
			groupappt = true;
			InviteDialog invite = new InviteDialog("invite", parent.controller);
			invite.setParent(this);
			this.setAlwaysOnTop(false);
			invite.setVisible(true);
			//invitedUserlist = invite.userlist;
			//for (int i=0 ;i<this.invitedUserlist.size();i++){
        		//NewAppt.setWaitingList(invite.getUserlist());
			//}
			//saveButtonResponse();
		}else if (e.getSource() == saveBut) {
			saveButtonResponse();

		}
		parent.getAppList().clear();
		parent.getAppList().setTodayAppt(parent.GetTodayAppt());
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
		date[0] = Utility.getNumber(yearF.getText());
		date[1] = Utility.getNumber(monthF.getText());
		if (date[0] < 1980 || date[0] > 2100) {
			JOptionPane.showMessageDialog(this, "Please input proper year",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (date[1] <= 0 || date[1] > 12) {
			JOptionPane.showMessageDialog(this, "Please input proper month",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		date[2] = Utility.getNumber(dayF.getText());
		int monthDay = CalGrid.monthDays[date[1] - 1];
		if (date[1] == 2) {
			GregorianCalendar c = new GregorianCalendar();
			if (c.isLeapYear(date[0]))
				monthDay = 29;
		}
		if (date[2] <= 0 || date[2] > monthDay) {
			JOptionPane.showMessageDialog(this,
			"Please input proper month day", "Input Error",
			JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return date;
	}
	private int[] getReminderDate() {
		int[] date = new int[3];
		date[0] = Utility.getNumber(yearF.getText());
		date[1] = Utility.getNumber(monthF.getText());
		int rcheck = reminderBox.getSelectedIndex();
		int rvalue = Utility.getNumber(reminderField.getText());
		int start=getTime(sTimeH, sTimeM);
		if (rvalue > 60 || rvalue < 0){
			JOptionPane.showMessageDialog(this, "Please input proper reminder (0 to 60)",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		if (date[1] <= 0 || date[1] > 12) {
			JOptionPane.showMessageDialog(this, "Please input proper month",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		date[2] = Utility.getNumber(dayF.getText());
		int monthDay = CalGrid.monthDays[date[1] - 1];
		if (date[1] == 2) {
			GregorianCalendar c = new GregorianCalendar();
			if (c.isLeapYear(date[0]))
				monthDay = 29;
		}
		if (date[2] <= 0 || date[2] > monthDay) {
			JOptionPane.showMessageDialog(this,
			"Please input proper month day", "Input Error",
			JOptionPane.ERROR_MESSAGE);
			return null;
		}
		switch (rcheck) {
		case 0: //None
			break;
		case 1: //Minute
			break;
		case 2: //Hour
			if((start-rvalue*60) < 0){
					date[2]=date[2] - rvalue/24;
				if(date[2]<=0){
					date[1]=date[1] - 1;
					if(date[1]==0){
						date[0]=date[0]-1;
						date[1]=date[1]+12;
					}
					monthDay=CalGrid.monthDays[date[1] - 1];
					date[2]=date[2]+monthDay;
				}
			}
			break;
		case 3: //Day
			date[2] = date[2] - rvalue;
			while(date[2]<=0){
				date[1]=date[1] - 1;
				if(date[1]==0){
					date[0]=date[0]-1;
					date[1]=date[1]+12;
				}
				monthDay=CalGrid.monthDays[date[1] - 1];
				date[2]=date[2]+monthDay;
			}
			break;
		case 4: //Week
			date[2] = date[2] - rvalue*7;
			while(date[2]<=0){
				date[1]=date[1] - 1;
				if(date[1]==0){
					date[0]=date[0]-1;
					date[1]=date[1]+12;
				}
				monthDay=CalGrid.monthDays[date[1] - 1];
				date[2]=date[2]+monthDay;
			}
			break;
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
	
	private int getreminder(){
		int result = getTime(sTimeH, sTimeM);
		int rcheck = reminderBox.getSelectedIndex();
		int rvalue = Utility.getNumber(reminderField.getText());
		switch (rcheck) {
		case 0: //None
			break;
		case 1: //Minute
			result = result - rvalue;
			break;
		case 2: //Hour
			result = result - (rvalue%24)*60;
			break;
		case 3: //Day
			break;
		case 4: //Week
			break;
		}
		return result;
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

	private void saveButtonResponse() {
		// Fix Me!
		// Save the appointment to the hard disk
		boolean finish = true;
		int[] date = getValidDate();
        if (date == null) {
                JOptionPane.showMessageDialog(this,
                                "Please input proper month day", "Input Error",
                                JOptionPane.ERROR_MESSAGE);
                finish = false;
        }
        int[] rdate = getReminderDate();
        if (rdate == null) {
                JOptionPane.showMessageDialog(this,
                                "Please input proper reminder (0 to 60)", "Input Error",
                                JOptionPane.ERROR_MESSAGE);
                finish = false;
        }
        Appt appt = new Appt();
        int[] time = new int[2];
        int reminder = getreminder();
        time = getValidTimeInterval();
        if (time == null) {
        	finish = false;
        }
        appt.setTimeSpan(new TimeSpan(CreateTimeStamp(date, time[0]),
                        CreateTimeStamp(date, time[1])));
        appt.setReminder(new TimeSpan(CreateTimeStamp(rdate, reminder),
        				CreateTimeStamp(rdate, reminder)));
        String temps = titleField.getText().trim();
        String info = detailArea.getText();
        String location = (String) locField.getSelectedItem();
        if (temps.equals("") || temps.equals("Untitled")) {
                JOptionPane.showMessageDialog(this, "Please Input A Proper Title",
                                "Input Error", JOptionPane.ERROR_MESSAGE);

                finish = false;
        } else {
                appt.setTitle(temps);
                appt.setInfo(info);
                appt.setLocation(location);
        }

        //User[] users = new User[1];
        //users[0] = this.parent.mCurrUser;

        //check if there is any time conflict
        Date systemdate= new Date();
        Timestamp systemtime = new Timestamp(systemdate.getTime());
        if (this.getTitle()!= "Modify" ){
        	boolean isConflict = false;
        	TimeSpan inputTime = new TimeSpan(CreateTimeStamp(date, time[0]),CreateTimeStamp(date, time[1]));
        	Vector<Appt> thisDayAppt = parent.GetThisDayAppt(date[0], date[1], date[2]);
        	int i = thisDayAppt.size()-1;
        	while (i>=0) {
                if (!thisDayAppt.get(i).equals(selectedAppt)){
                        TimeSpan tempTimeSpan = thisDayAppt.get(i).TimeSpan();
                        if (tempTimeSpan.Overlap(inputTime)) {
                                //parent.getAppList().flashAppt(thisDayAppt.get(i));
                                this.setAlwaysOnTop(false);
                                parent.setAlwaysOnTop(true);
                                JOptionPane.showMessageDialog(parent, "Time conflict with other appointment!",
                                                "Input Error", JOptionPane.ERROR_MESSAGE);
                                parent.setAlwaysOnTop(false);
                                this.setAlwaysOnTop(true);
                                isConflict = true;
                        }
                }
                i--;
        	}
        	if (isConflict) {
                sTimeH.grabFocus();
                sTimeH.selectAll();
                finish = false;
        	}
        } else {
        	if(appt.TimeSpan().mStartTime.after(systemtime))
        		parent.controller.ManageAppt(appt, 2);
        	else{
        		JOptionPane.showMessageDialog(this, "You are not able to modify past appointment.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);

        		finish = false;
        	}
        		
        }
        if(finish && groupappt){
        	//this.invitedUserlist=NewAppt.getWaitingList().;
        	//for (int i=0 ;i<this.invitedUserlist.length;i++){
        		//appt.setWaitingList(NewAppt.getWaitingList());
        	//}
        	appt.setInitiator(parent.mCurrUser);
        	appt.setJoint(true);
        	NewAppt = appt;
        	NewAppt.setWaitingList(this.invitedUserlist);

            parent.controller.ManageAppt(appt, 3);
            if (parent.IsTodayAppt(NewAppt)) {
                    parent.getAppList().clear();
                    parent.getAppList().setTodayAppt(parent.GetTodayAppt());
            }
            //apptday = new GregorianCalendar();
            //apptday.set(date[0], date[1], date[2]);
            //new CalCellRenderer(apptday, Color.blue);
            
            setVisible(false);
			dispose();
        	
        } else if (finish){
        	NewAppt = appt;

            parent.controller.ManageAppt(appt, 3);
            if (parent.IsTodayAppt(NewAppt)) {
                    parent.getAppList().clear();
                    parent.getAppList().setTodayAppt(parent.GetTodayAppt());
            }
            //apptday = new GregorianCalendar();
            //apptday.set(date[0], date[1], date[2]);
            //new CalCellRenderer(apptday, Color.blue);
            setVisible(false);
			dispose();
            
        }
        
        
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

	public void updateSetApp(Appt appt) {
		// Fix Me!
		if (appt == null)
            return;
		NewAppt = appt;
		yearF.setText((appt.TimeSpan().StartTime().getYear() + 1900) + "");
		monthF.setText((appt.TimeSpan().StartTime().getMonth() + 1) + "");
		dayF.setText(appt.TimeSpan().StartTime().getDate() + "");
		locField.setSelectedItem(appt.getLocation());;
		sTimeH.setText(appt.TimeSpan().StartTime().getHours() + "");
		sTimeM.setText(appt.TimeSpan().StartTime().getMinutes() + "");
		eTimeH.setText(appt.TimeSpan().EndTime().getHours() + "");
		eTimeM.setText(appt.TimeSpan().EndTime().getMinutes() + "");
		detailArea.setText(appt.getInfo());
		titleField.setText(appt.getTitle());
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
	
	public String getCurrentUser()		// get the id of the current user
	{
		return this.parent.mCurrUser.getID();
	}
	
	private void allDisableEdit(){
		yearF.setEditable(false);
		monthF.setEditable(false);
		dayF.setEditable(false);
		sTimeH.setEditable(false);
		sTimeM.setEditable(false);
		eTimeH.setEditable(false);
		eTimeM.setEditable(false);
		titleField.setEditable(false);
		detailArea.setEditable(false);
	}
	
	public void setinvitedUserlist(LinkedList<String> input){
		invitedUserlist = input;
	}
}
