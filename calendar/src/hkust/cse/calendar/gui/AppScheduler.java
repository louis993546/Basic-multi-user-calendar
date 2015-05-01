package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptDB;
import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.apptstorage.LocationDB;
import hkust.cse.calendar.unit.Appointment;
import hkust.cse.calendar.unit.Appt;
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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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


public class AppScheduler extends JDialog implements ActionListener, ComponentListener {

	//need to read all rows of appt.db and location.db

	private JLabel yearL;
	private JLabel monthL;
	private JLabel dayL;
	private JLabel sTimeHL;
	private JLabel sTimeML;
	private JLabel eTimeHL;
	private JLabel eTimeML;

	private String[] monthS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
	private String[] timeHS = {"08", "09", "10", "11",
			"12", "13", "14", "15", "16", "17", "18"};//8am~6pm
	private String[] timeMS = {"00", "15", "30", "45"};
	private String[] reminderS = {"Minute(s)", "Hour(s)", "Day(s)", "Week(s)"};
	private ArrayList<String> locationAL;
	private String[] repeatS = {"Daily", "Weekly", "Monthly", "Yearly", "Decennially", "Centennially", "Millennially"};

	private JTextField yearSF;
	private JComboBox<String> monthSF;
	private JTextField daySF;
	private JTextField yearEF;
	private JComboBox<String> monthEF;
	private JTextField dayEF;
	private JComboBox<String> sTimeH;
	private JComboBox<String> sTimeM;
	private JComboBox<String> eTimeH;
	private JComboBox<String> eTimeM;
	private JTextField reminderTF;
	private JComboBox<String> reminderCB;
	private JCheckBox reminderChB;
	
	private JComboBox<String> lCB;

	private DefaultListModel model;
	private JTextField titleField;

	private JButton saveBut;
	private JButton CancelBut;
	private JButton inviteBut;
	private JButton rejectBut;

	private Appt NewAppt;
	private CalGrid parent;
	private boolean isNew = true;
	private boolean isChanged = true;
	private boolean isJoint = false;

	private JCheckBox rChB;
	private JTextField rTF;
	private JComboBox rCB;
	
	private JTextArea detailArea;

	private JSplitPane pDes;
	JPanel detailPanel;
	
	private Appt tempAppt;
	private int saveOrModify = 0;
	
	private int idofappt=0;

//	private JTextField attendField;
//	private JTextField rejectField;
//	private JTextField waitingField;
	private int selectedApptId = -1;

	private LocationDB ldb;
	private ApptDB adb;

	private void commonConstructor(String title, CalGrid cal, int startTime) {
		parent = cal;
		this.setAlwaysOnTop(true);
		setTitle(title);
		setModal(false);
		ldb = new LocationDB();
		locationAL = ldb.getLocationList();

		Container contentPane;
		contentPane = getContentPane();

		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

		//Title JPanel
		JPanel titleAndTextPanel = new JPanel();
		titleAndTextPanel.setLayout(new BorderLayout());
		Border titleBorder = new TitledBorder(null, "Title");
		titleAndTextPanel.setBorder(titleBorder);
		titleField = new JTextField(50);
		titleAndTextPanel.add(titleField);

		//Start JPanel
		JPanel pStart = new JPanel();
		Border startBorder = new TitledBorder(null, "START TIME");
		pStart.setBorder(startBorder);
		yearL = new JLabel("YEAR: ");
		pStart.add(yearL);
		yearSF = new JTextField(6);
		pStart.add(yearSF);
		monthL = new JLabel("MONTH: ");
		pStart.add(monthL);
		monthSF = new JComboBox<String>(monthS);
		pStart.add(monthSF);
		dayL = new JLabel("DAY: ");
		pStart.add(dayL);
		daySF = new JTextField(4);
		pStart.add(daySF);
		sTimeHL = new JLabel("Hour");
		pStart.add(sTimeHL);
		sTimeH = new JComboBox<String>(timeHS);
		pStart.add(sTimeH);
		sTimeML = new JLabel("Minute");
		pStart.add(sTimeML);
		sTimeM = new JComboBox<String>(timeMS);
		pStart.add(sTimeM);

		//TODO
		//These 3 lines give default value for new appointment
		//according to upper right table.
		//But cannot set hour or min in this constructor
		//It may be done by adding parameter, 
		//eg. (String title, CalGrid cal, int startTime=480)
		//480 means 8:00am = 8*60min
		titleField.setText("Default");
		daySF.setText(String.valueOf(cal.currentD));
		monthSF.setSelectedIndex(cal.currentM - 1);//1~12 ->0~11 (index of list) 
		yearSF.setText(String.valueOf(cal.currentY));
		//startTime/60->hour; startTime%60->min
		//hourlist:{08,09,...,18}, minlist:{00,15,30,45}
		//eg.495-> hour= 8, min=15   ->  index:{0, 1}
		//so, 8-8=0, 15/15=1
		//req function are (startTime/60)-8->indexOfHour; (startTime%60)/15->indexOfMin
		sTimeH.setSelectedIndex((startTime/60)-8);
		sTimeM.setSelectedIndex((startTime%60)/15);
		
		//End JPanel
		JPanel pEnd = new JPanel();
		Border endBorder = new TitledBorder(null, "END TIME");
		pEnd.setBorder(endBorder);
//		yearL = new JLabel("YEAR: ");
//		pEnd.add(yearL);
//		yearEF = new JTextField(6);
//		pEnd.add(yearEF);
//		monthL = new JLabel("MONTH: ");
//		pEnd.add(monthL);
//		monthEF = new JComboBox<String>(monthS);
//		pEnd.add(monthEF);
//		dayL = new JLabel("DAY: ");
//		pEnd.add(dayL);
//		dayEF = new JTextField(4);
//		pEnd.add(dayEF);
		//end date == start date
		
		eTimeHL = new JLabel("Hour");
		pEnd.add(eTimeHL);
		eTimeH = new JComboBox<String>(timeHS);
		pEnd.add(eTimeH);
		eTimeML = new JLabel("Minute");
		pEnd.add(eTimeML);
		eTimeM = new JComboBox<String>(timeMS);
		pEnd.add(eTimeM);

		//set end time=start time
		eTimeH.setSelectedIndex(sTimeH.getSelectedIndex());
		eTimeM.setSelectedIndex(sTimeM.getSelectedIndex());
		
		//Location Panel
		JPanel lPanel = new JPanel();
		Border lBorder = new TitledBorder(null, "Location");
		lPanel.setBorder(lBorder);
		locationAL.add("N/A");
		lCB = new JComboBox(locationAL.toArray());
		lCB.setSelectedItem("N/A");
		lPanel.add(lCB);
		
		//Repeat Panel
		JPanel rPanel = new JPanel();
		Border rBorder = new TitledBorder(null, "Repeat(Optional)");
		rPanel.setBorder(rBorder);
		rChB = new JCheckBox("On");
		rTF = new JTextField(5);
		rCB = new JComboBox(repeatS);
		rPanel.add(rChB);
		rPanel.add(rTF);
		rPanel.add(rCB);

		//Location + Repeat Panel
		JPanel lrPanel = new JPanel();
		lrPanel.add("West", lPanel);
		lrPanel.add("East", rPanel);

		//Description JPanel
		detailPanel = new JPanel();
		detailPanel.setLayout(new BorderLayout());
		Border detailBorder = new TitledBorder(null, "Appointment Description");
		detailPanel.setBorder(detailBorder);
		detailArea = new JTextArea(20, 50);
		detailArea.setEditable(true);
		JScrollPane detailScroll = new JScrollPane(detailArea);
		detailPanel.add(detailScroll);

		//Reminder JPanel
		JPanel pReminder = new JPanel();
		Border reminderBorder = new TitledBorder(null, "Reminder(Optional)");
		pReminder.setBorder(reminderBorder);
		reminderChB = new JCheckBox("On");
		pReminder.add(reminderChB);
		reminderTF = new JTextField(5);
		pReminder.add(reminderTF);
		reminderCB = new JComboBox<String>(reminderS);
		pReminder.add(reminderCB);
		JLabel reminderL = new JLabel("before the event");
		pReminder.add(reminderL);

		//Every all these JPanels to top JPanel
		top.add(titleAndTextPanel);
		top.add(pStart);
		top.add(pEnd);
		top.add(lrPanel);
		top.add(detailPanel);
		top.add(pReminder);
		contentPane.add("North", top);

		if (NewAppt != null) {
			detailArea.setText(NewAppt.getInfo());
		}

		//button JPanel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		//Invite button
		inviteBut = new JButton("Invite");
		inviteBut.addActionListener(this);
		buttonPanel.add(inviteBut);

		//reject button
		rejectBut = new JButton("Reject");
		rejectBut.addActionListener(this);
		buttonPanel.add(rejectBut);
		rejectBut.setVisible(false);

		//Cancel button
		CancelBut = new JButton("Cancel");
		CancelBut.addActionListener(this);
		buttonPanel.add(CancelBut);

		//Save/Accept/Confirm button
		saveBut = new JButton("Save");
		saveBut.addActionListener(this);
		buttonPanel.add(saveBut);

		contentPane.add("South", buttonPanel);
		NewAppt = new Appt();

		if (this.getTitle().equals("Join Appointment Content Change") || this.getTitle().equals("Join Appointment Invitation")){
			inviteBut.setVisible(false);
			rejectBut.setVisible(true);
			CancelBut.setText("Consider Later");
			saveBut.setText("Accept");
		}
		if (this.getTitle().equals("Someone has responded to your Joint Appointment invitation") ){
			inviteBut.setVisible(false);
			rejectBut.setVisible(false);
			CancelBut.setVisible(false);
			saveBut.setText("confirmed");
		}
		if (this.getTitle().equals("Join Appointment Invitation") || this.getTitle().equals("Someone has responded to your Joint Appointment invitation") || this.getTitle().equals("Join Appointment Content Change")){
			allDisableEdit();
		}
		pack();

	}

	AppScheduler(String title, CalGrid cal, int startTime, int selectedApptId) {
		
		this.selectedApptId = selectedApptId;
		commonConstructor(title, cal, startTime);
	}
	
	AppScheduler(String title, CalGrid cal,int startTime) {
		
		
		commonConstructor(title, cal, startTime);
	}

	AppScheduler(String title, CalGrid cal) {
		commonConstructor(title, cal, 480);
	}
	
	String getTimeInCorrectFormat(int time)
	{
		String timeS;
		if (time<10)
			return ("0"+time);
		else
			return ""+time;
	}
	
	AppScheduler(String title, CalGrid cal, Appt appt)
	{
		//TODO need to get all info to their box/field but it is not working perfectly
		tempAppt = appt;
		saveOrModify=1;
		commonConstructor(title, cal, 480);
		idofappt=appt.getID();
		updateSetApp(appt);
	}

	public void actionPerformed(ActionEvent e) {
		// distinguish which button is clicked and continue with require function
		if (e.getSource() == CancelBut) {
			setVisible(false);
			dispose();
		} else if (e.getSource() == saveBut) {
			try {
				if(saveButtonResponse()==true){ //mean data of new appointment is valid
					setVisible(false);
					dispose();
				}
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		} else if (e.getSource() == rejectBut){
			if (JOptionPane.showConfirmDialog(this, "Reject this joint appointment?", "Confirmation", JOptionPane.YES_NO_OPTION) == 0){
				NewAppt.addReject(getCurrentUser());
				NewAppt.getAttendList().remove(getCurrentUser());
				NewAppt.getWaitingList().remove(getCurrentUser());
				this.setVisible(false);
				dispose();
			}
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

	private int[] getValidDate(JTextField a, JComboBox b, JTextField d) {

		int[] date = new int[3];
		date[0] = Utility.getNumber(a.getText());//yyyy
		date[1] = Utility.getNumber(b.getSelectedItem().toString());//mm
		if (date[0] < 1980 || date[0] > 2100) {//yyyy
			JOptionPane.showMessageDialog(this, "Please input proper year",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (date[1] <= 0 || date[1] > 12) {//mm 1~12
			JOptionPane.showMessageDialog(this, "Please input proper month",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		date[2] = Utility.getNumber(d.getText());
		int monthDay = CalGrid.monthDays[date[1] - 1];
		if (date[1] == 2) {//feb 28 or 29
			GregorianCalendar c = new GregorianCalendar();
			if (c.isLeapYear(date[0]))
				monthDay = 29;
		}
		if (date[2] <= 0 || date[2] > monthDay) {//dd
			JOptionPane.showMessageDialog(this,
			"Please input proper month day", "Input Error",
			JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return date;
	}

	private int getTime(JComboBox h, JComboBox eTimeM2) {

		int hour = h.getSelectedIndex()+8;//eg. 8am -> index 0
		if (hour == -1)
			return -1;
		int minute = eTimeM2.getSelectedIndex()*15;//{00, 15,30, 45}
		if (minute == -1)
			return -1;

		return (hour * 60 + minute);

	}

	private int[] getValidTimeInterval() {
		int[] result = new int[2];
		result[0] = getTime(sTimeH, sTimeM);//eg. 480 ->8am
		result[1] = getTime(eTimeH, eTimeM);
		if ((result[0] % 15) != 0 || (result[1] % 15) != 0) {
			JOptionPane.showMessageDialog(this,
					"Minute Must be 0, 15, 30, or 45 !", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

//		if (!sTimeM.getSelectedItem().toString().equals("0") && !sTimeM.getSelectedItem().toString().equals("15")
//				&& !sTimeM.getSelectedItem().toString().equals("30") && !sTimeM.getSelectedItem().toString().equals("45")
//			|| !eTimeM.getSelectedItem().toString().equals("0") && !eTimeM.getSelectedItem().toString().equals("15")
//			&& !eTimeM.getSelectedItem().toString().equals("30") && !eTimeM.getSelectedItem().toString().equals("45")){
//			JOptionPane.showMessageDialog(this,
//					"Minute Must be 0, 15, 30, or 45 !", "Input Error",
//					JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
		//above check not needed?
		
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
	
	private static boolean isValidDate(String input) {
        String formatString = "MM/dd/yyyy";

        try {
            SimpleDateFormat format = new SimpleDateFormat(formatString);
            format.setLenient(false);
            format.parse(input);
        } catch (ParseException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }

	@SuppressWarnings("deprecation")
	private boolean saveButtonResponse() throws SQLException {
		// TODO unfinished save button
		int[] startDate = getValidDate(yearSF, monthSF, daySF);
		int[] endDate = null;
		if (startDate != null)
			endDate=startDate.clone();
		int[] startAndEndTime =getValidTimeInterval();
		if ((startDate==null) || (endDate==null) || (startAndEndTime==null))
			return false; 
		int shr = startAndEndTime[0]/60;
		int smin = startAndEndTime[0]%60;
		int ehr = startAndEndTime[1]/60;
		int emin = startAndEndTime[1]%60;
		Timestamp tempTS2 = new Timestamp(startDate[0]-1900, startDate[1]-1, startDate[2], shr, smin, 0, 0);
		if (tempTS2.before(parent.timeMachine.getTMTimestamp()))
		{
			JOptionPane.showMessageDialog(this, "It's all in the past",
					"Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		//check if end date earlier then start date
		String title = titleField.getText().trim();
		String description = detailArea.getText();
		String location = lCB.getSelectedItem().toString();
		
		//get reminders
		boolean reminderOnOff = reminderChB.isSelected(); 
		int reminderOnOffInt = 0;
		int reminderTime = 0;
		int reminderUnit = 0;
		
		if (reminderOnOff == true)
		{
			reminderOnOffInt = 1;
			reminderTime = Integer.parseInt(reminderTF.getText());
			if (reminderCB.getSelectedItem().toString() == "Minute(s)") //TODO may have something wrong with this one
			{
			//	System.out.println("reminderUnit = 1;");
				reminderUnit = 1;
			}
			else if (reminderCB.getSelectedItem().toString() == "Hour(s)")
			{
				reminderUnit = 2;
			}
			else if (reminderCB.getSelectedItem().toString() == "Day(s)")
			{
				reminderUnit = 3;
			}
			else if (reminderCB.getSelectedItem().toString() == "Week(s)")
			{
				reminderUnit = 4;
			}
			else
			{
				System.out.println("Something's wrong");
			}
		}
		
		//TODO currently it provide 3 empty linkedlist
		LinkedList<String> temp = new LinkedList<String>();
		Appointment newAppt = new Appointment(title, description, location, shr, smin, startDate[0], startDate[1], startDate[2], ehr, emin, endDate[0], endDate[1], endDate[2], reminderOnOffInt, reminderTime, reminderUnit, temp, temp, temp, 12);
		//TODO need to provide a createrID
		
		TimeSpan wholeDay=new TimeSpan(startDate[0], startDate[1],	startDate[2], 0, 0, 23, 59);
		Appt[] listAppt=parent.controller.RetrieveAppts(parent.mCurrUser, wholeDay);
		for (Appt tempAppt : listAppt) {
			if(tempAppt.TimeSpan().Overlap(newAppt.getTimeSpan())==true && idofappt!=tempAppt.getID()){
				JOptionPane.showMessageDialog(this, "You are trying to choose a time slot which conflicts with another appointment. Please choose another time slot.",
						"Time Conflict", JOptionPane.ERROR_MESSAGE);
				return false;//cannot save
			}				
		}

		adb = new ApptDB();
		if (saveOrModify == 0)	//new appt
			adb.addAppt(newAppt);
		else					//modify appt
			adb.modifyAppt(tempAppt.getID(), newAppt);
		
		//frequency(total of n-1 of appts)
		boolean freqOnOff = rChB.isSelected();
		if (freqOnOff==true)
		{
			int freqNum = Integer.parseInt(rTF.getText());
			String freqUnit = rCB.getSelectedItem().toString();

			//calculate new date(s)
			if (freqOnOff == true && freqNum>1)
			{
				for (int i = 0; i<freqNum-1; i++)
				{
					if (freqUnit == "Daily")
					{
						//day +1
						//need to check if the month/year changes
						long adding = (long) 1 * 24 * 60 * 60 * 1000;
						Timestamp tempTS = newAppt.getTimeSpan().StartTime();
						tempTS.setTime(tempTS.getTime()+adding); //now tempTS contains the new day
						newAppt.setStartEndYear(tempTS.getYear()+1900); //set year
						newAppt.setStartEndMonth(tempTS.getMonth()+ 1 ); //set month
						newAppt.setStartEndDay(tempTS.getDate()); //set day
						if (newAppt.getStartYear()<2100)
						{
							wholeDay=new TimeSpan(newAppt.getStartYear(), newAppt.getStartMonth(), newAppt.getStartDay(), 0, 0, 23, 59);
							listAppt=parent.controller.RetrieveAppts(parent.mCurrUser, wholeDay);
							for (Appt tempAppt : listAppt) {
								if(tempAppt.TimeSpan().Overlap(newAppt.getTimeSpan())==true && idofappt!=tempAppt.getID()){
									JOptionPane.showMessageDialog(this, "You are trying to choose a time slot which conflicts with another appointment. Please choose another time slot.",
											"Time Conflict", JOptionPane.ERROR_MESSAGE);
									return false;//cannot save
								}				
							}
							adb = new ApptDB();
							if (saveOrModify == 0)	//new appt
								adb.addAppt(newAppt);
							else					//modify appt
								adb.modifyAppt(tempAppt.getID(), newAppt);
						}
					}
					else if (freqUnit == "Weekly")
					{
						//day +7
						//need to check if the month/year changes
						long adding = (long) 7 * 24 * 60 * 60 * 1000;
						Timestamp tempTS = newAppt.getTimeSpan().StartTime();
						tempTS.setTime(tempTS.getTime()+adding); //now tempTS contains the new day
						newAppt.setStartEndYear(tempTS.getYear()+1900); //set year
						newAppt.setStartEndMonth(tempTS.getMonth()+ 1 ); //set month
						newAppt.setStartEndDay(tempTS.getDate()); //set day
						if (newAppt.getStartYear()<2100)
						{
							wholeDay=new TimeSpan(newAppt.getStartYear(), newAppt.getStartMonth(), newAppt.getStartDay(), 0, 0, 23, 59);
							listAppt=parent.controller.RetrieveAppts(parent.mCurrUser, wholeDay);
							for (Appt tempAppt : listAppt) {
								if(tempAppt.TimeSpan().Overlap(newAppt.getTimeSpan())==true && idofappt!=tempAppt.getID()){
									JOptionPane.showMessageDialog(this, "You are trying to choose a time slot which conflicts with another appointment. Please choose another time slot.",
											"Time Conflict", JOptionPane.ERROR_MESSAGE);
									return false;//cannot save
								}				
							}
							adb = new ApptDB();
							if (saveOrModify == 0)	//new appt
								adb.addAppt(newAppt);
							else					//modify appt
								adb.modifyAppt(tempAppt.getID(), newAppt);
						}
					}
					else if (freqUnit == "Monthly")
					{
						int y = newAppt.getStartYear(); //remember to +1900
						int m = newAppt.getStartMonth();
						int d = newAppt.getStartDay();
						if (m + 1 == 13)
						{
							m = 1;
							y = y+1;
						}
						else
							m++;
						newAppt.setStartEndYear(y);
						newAppt.setStartEndMonth(m);
						System.out.println(m + "/" + d + "/" + y);
						System.out.println(isValidDate(m + "/" + d + "/" + y));
						if (isValidDate(m + "/" + d + "/" + y ) == true) //
						{
							if (newAppt.getStartYear()<2100)
							{
								wholeDay=new TimeSpan(newAppt.getStartYear(), newAppt.getStartMonth(), newAppt.getStartDay(), 0, 0, 23, 59);
								listAppt=parent.controller.RetrieveAppts(parent.mCurrUser, wholeDay);
								for (Appt tempAppt : listAppt) {
									if(tempAppt.TimeSpan().Overlap(newAppt.getTimeSpan())==true && idofappt!=tempAppt.getID()){
										JOptionPane.showMessageDialog(this, "You are trying to choose a time slot which conflicts with another appointment. Please choose another time slot.",
												"Time Conflict", JOptionPane.ERROR_MESSAGE);
										return false;//cannot save
									}				
								}
								adb = new ApptDB();
								if (saveOrModify == 0)	//new appt
									adb.addAppt(newAppt);
								else					//modify appt
									adb.modifyAppt(tempAppt.getID(), newAppt);
							}
						}
						
					}
					else if (freqUnit == "Yearly")
					{
						//year + 1
						newAppt.setStartEndYear(startDate[0] + i + 1);
						if (newAppt.getStartYear()<2100)
						{
							wholeDay=new TimeSpan(newAppt.getStartYear(), newAppt.getStartMonth(), newAppt.getStartDay(), 0, 0, 23, 59);
							listAppt=parent.controller.RetrieveAppts(parent.mCurrUser, wholeDay);
							for (Appt tempAppt : listAppt) {
								if(tempAppt.TimeSpan().Overlap(newAppt.getTimeSpan())==true && idofappt!=tempAppt.getID()){
									JOptionPane.showMessageDialog(this, "You are trying to choose a time slot which conflicts with another appointment. Please choose another time slot.",
											"Time Conflict", JOptionPane.ERROR_MESSAGE);
									return false;//cannot save
								}				
							}
							adb = new ApptDB();
							if (saveOrModify == 0)	//new appt
								adb.addAppt(newAppt);
							else					//modify appt
								adb.modifyAppt(tempAppt.getID(), newAppt);
						}
					}
					else if (freqUnit == "Decennially")
					{
						//year + 10
						newAppt.setStartEndYear(startDate[0] + (i*10) + 10);
						if (newAppt.getStartYear()<2100)
						{
							wholeDay=new TimeSpan(newAppt.getStartYear(), newAppt.getStartMonth(), newAppt.getStartDay(), 0, 0, 23, 59);
							listAppt=parent.controller.RetrieveAppts(parent.mCurrUser, wholeDay);
							for (Appt tempAppt : listAppt) {
								if(tempAppt.TimeSpan().Overlap(newAppt.getTimeSpan())==true && idofappt!=tempAppt.getID()){
									JOptionPane.showMessageDialog(this, "You are trying to choose a time slot which conflicts with another appointment. Please choose another time slot.",
											"Time Conflict", JOptionPane.ERROR_MESSAGE);
									return false;//cannot save
								}				
							}
							adb = new ApptDB();
							if (saveOrModify == 0)	//new appt
								adb.addAppt(newAppt);
							else					//modify appt
								adb.modifyAppt(tempAppt.getID(), newAppt);
						}
					}
					else if (freqUnit == "Centennially")
					{
						//year + 100
						newAppt.setStartEndYear(startDate[0] + (i*100) + 100);
						if (newAppt.getStartYear()<2100)
						{
							wholeDay=new TimeSpan(newAppt.getStartYear(), newAppt.getStartMonth(), newAppt.getStartDay(), 0, 0, 23, 59);
							listAppt=parent.controller.RetrieveAppts(parent.mCurrUser, wholeDay);
							for (Appt tempAppt : listAppt) {
								if(tempAppt.TimeSpan().Overlap(newAppt.getTimeSpan())==true && idofappt!=tempAppt.getID()){
									JOptionPane.showMessageDialog(this, "You are trying to choose a time slot which conflicts with another appointment. Please choose another time slot.",
											"Time Conflict", JOptionPane.ERROR_MESSAGE);
									return false;//cannot save
								}				
							}
							adb = new ApptDB();
							if (saveOrModify == 0)	//new appt
								adb.addAppt(newAppt);
							else					//modify appt
								adb.modifyAppt(tempAppt.getID(), newAppt);
						}
					}
					else if (freqUnit == "Millennially")
					{
						//year + 1000
						newAppt.setStartEndYear(startDate[0] + (i*1000) + 1000);
						if (newAppt.getStartYear()<2100)
						{
							wholeDay=new TimeSpan(newAppt.getStartYear(), newAppt.getStartMonth(), newAppt.getStartDay(), 0, 0, 23, 59);
							listAppt=parent.controller.RetrieveAppts(parent.mCurrUser, wholeDay);
							for (Appt tempAppt : listAppt) {
								if(tempAppt.TimeSpan().Overlap(newAppt.getTimeSpan())==true && idofappt!=tempAppt.getID()){
									JOptionPane.showMessageDialog(this, "You are trying to choose a time slot which conflicts with another appointment. Please choose another time slot.",
											"Time Conflict", JOptionPane.ERROR_MESSAGE);
									return false;//cannot save
								}				
							}
							adb = new ApptDB();
							if (saveOrModify == 0)	//new appt
								adb.addAppt(newAppt);
							else					//modify appt
								adb.modifyAppt(tempAppt.getID(), newAppt);
						}
					}
					else
					{
						//somethings wrong
					}
				}
			}
		}
		
		//change the data of this appscheduler object
		
		//call savebuttonresponse again
		//
		parent.updateDB();
		parent.UpdateCal();
		parent.updateDB();
		parent.updateReminderCheckerApptlist();
		return true;
	}

	@SuppressWarnings("deprecation")
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
		// TODO set TF/CB/etc of this AppScheduler
		sTimeH.setSelectedItem(getTimeInCorrectFormat(appt.getAppointment().getStartHour()));
		sTimeM.setSelectedItem(getTimeInCorrectFormat(appt.getAppointment().getStartMin()));
		
		eTimeH.setSelectedItem(getTimeInCorrectFormat(appt.getAppointment().getEndHour()));
		eTimeM.setSelectedItem(getTimeInCorrectFormat(appt.getAppointment().getEndMin()));
		
		titleField.setText(appt.getTitle());
		
		detailArea.setText(appt.getInfo());
		
		lCB.setSelectedItem(appt.getAppointment().getLocation());
		
		yearSF.setText("" + appt.getAppointment().getStartYear());
		monthSF.setSelectedItem("" + appt.getAppointment().getStartMonth());
		daySF.setText(""+appt.getAppointment().getStartDay());
		
//		yearEF.setText("" + appt.getAppointment().getEndYear());
//		monthEF.setSelectedItem("" + appt.getAppointment().getEndMonth());
//		dayEF.setText(""+appt.getAppointment().getEndDay());
		
		//TODO not all have been implemented
		reminderCB.setSelectedItem("" + appt.getAppointment().getReminderUnit());
		reminderTF.setText(""+appt.getAppointment().getReminderTime());
		boolean apptR;
		if (appt.getAppointment().getReminder() == 0)
			apptR = false;
		else
			apptR = true;
		reminderChB.setSelected(apptR);
		
		//TODO invitation and stuff like that in phrase 2
	}

	public void componentHidden(ComponentEvent e) {
		// TODO I have no idea
	}

	public void componentMoved(ComponentEvent e) {
		// TODO I have no idea
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
		return this.parent.mCurrUser.ID();
	}

	private void allDisableEdit(){
		yearSF.setEditable(false);
//		yearEF.setEditable(false);
		monthSF.setEditable(false);
//		monthEF.setEditable(false);
		daySF.setEditable(false);
//		dayEF.setEditable(false);
		sTimeH.setEditable(false);
		sTimeM.setEditable(false);
		eTimeH.setEditable(false);
		eTimeM.setEditable(false);
		titleField.setEditable(false);
		detailArea.setEditable(false);
	}
}
