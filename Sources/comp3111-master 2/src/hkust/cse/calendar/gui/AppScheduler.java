package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.gui.LocationsDialog;
import hkust.cse.calendar.unit.user.UserManagement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
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
	
	private JLabel sEmailL;
	private JCheckBox sEmailCB;
	private JLabel sSmsL;
	private JCheckBox sSmsCB;

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
	private boolean sendEmail = false;
	private boolean sendSms = false;

	private JTextArea detailArea;

	private JComboBox locField;
	private JComboBox freField;
	private JCheckBox remField;
	private JTextField rTimeH;
	private JTextField rTimeM;
	private JCheckBox gEvent;
	private JButton addUser;
	private JButton availableTime;
	
	private JSplitPane pDes;
	JPanel detailPanel;

  	private JTextField attendField;
  	private JTextField rejectField;
  	private JTextField waitingField;
	private int selectedApptId = -1;
	
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
		
		JLabel frequencyL = new JLabel("FREQUENCY");
		String[] frequencyType = {"One-time", "Daily", "Weekly", "Monthly"};
		freField = new JComboBox(frequencyType);
		peTime.add(frequencyL);
		peTime.add(freField);
		sEmailL = new JLabel("Send Email");
		peTime.add(sEmailL);
		sEmailCB = new JCheckBox();
		sEmailCB.addItemListener(new ItemListener() {
		      public void itemStateChanged(ItemEvent e) {
		    	  if(sEmailCB.isSelected()){
		    		  	sendEmail = true;
		  			}
		  		else{
		  				sendEmail = false;
		  			}
		        }
		      });
		peTime.add(sEmailCB);
		sSmsL = new JLabel("Send SMS");
		peTime.add(sSmsL);
		sSmsCB = new JCheckBox();
		sSmsCB.addItemListener(new ItemListener() {
		      public void itemStateChanged(ItemEvent e) {
		    	  if(sSmsCB.isSelected()){
		    		  	sendSms = true;
		  			}
		  		else{
		  				sendSms = false;
		  			}
		        }
		      });
		peTime.add(sSmsCB);
		
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
		if(locations == null){
			locations = new Location[0];
		}
		JLabel locationL = new JLabel("LOCATION");
		locField = new JComboBox(locations);
		titleAndTextPanel.add(locationL);
		titleAndTextPanel.add(locField);
		
		remField = new JCheckBox("REMINDER");
		titleAndTextPanel.add(remField);
		rTimeH = new JTextField(4);
		rTimeM = new JTextField(4);
		titleAndTextPanel.add(rTimeH);
		titleAndTextPanel.add(rTimeM);
		rTimeH.disable();
		rTimeM.disable();
		remField.addItemListener(new ItemListener() {
		      public void itemStateChanged(ItemEvent e) {
		    	  if(remField.isSelected()){
		  			rTimeH.enable();
		  			rTimeM.enable();
		  			}
		  		else{
		  			rTimeH.disable();
		  			rTimeM.disable();
		  			}
		        }
		      });
		gEvent = new JCheckBox("Group Event");
		titleAndTextPanel.add(gEvent);
		addUser = new JButton("Add Participants");
		//titleAndTextPanel.add(addUser);
		addUser.addActionListener(this);
		availableTime = new JButton("Available Time List");
		titleAndTextPanel.add(availableTime);
		addUser.setEnabled(false);
		availableTime.setEnabled(true);
		availableTime.addActionListener(this);
		gEvent.addItemListener(new ItemListener() {
		      public void itemStateChanged(ItemEvent e) {
		    	  if(gEvent.isSelected()){
		    		  	isJoint = true;
		    		  	inviteBut.setVisible(true);
			  			//addUser.setEnabled(true);
		    		  	//if(NewAppt.getWaitingList().size() !=0)
		    		  	//	availableTime.setEnabled(true);
		    		  	//else
		    		  	//	availableTime.setEnabled(false);
		  			}
		  		else{
		  				isJoint = false;
		  				inviteBut.setVisible(false);
			  			//addUser.setEnabled(false);
			  			availableTime.setEnabled(false);
		  			}
		        }
		      });
		detailPanel = new JPanel();
		detailPanel.setLayout(new BorderLayout());
		Border detailBorder = new TitledBorder(null, "Appointment Description");
		detailPanel.setBorder(detailBorder);
		detailArea = new JTextArea(20, 30);

		detailArea.setEditable(true);
		JScrollPane detailScroll = new JScrollPane(detailArea);
		detailPanel.add(detailScroll);

		pDes = new JSplitPane(JSplitPane.VERTICAL_SPLIT, titleAndTextPanel,
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
  		inviteBut.setVisible(false);
		
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
		} else if (e.getSource() == saveBut) {
			saveButtonResponse();

		} else if (e.getSource() == rejectBut){
			if (JOptionPane.showConfirmDialog(this, "Reject this joint appointment?", "Confirmation", JOptionPane.YES_NO_OPTION) == 0){
				NewAppt.addReject(getCurrentUser());
				NewAppt.getAttendList().remove(getCurrentUser());
				NewAppt.getWaitingList().remove(getCurrentUser());
				this.setVisible(false);
				dispose();
			}
		} else if (e.getSource() == inviteBut){
			//show dialog to add/remove participant
			AddUserDialog addUserD = new AddUserDialog(NewAppt, parent.controller);
		} else if (e.getSource() == availableTime){
			NewAppt.setLocation((Location)locField.getSelectedItem());
			AvailableTimeListDialog aTimeD = new AvailableTimeListDialog(NewAppt, parent.controller, parent.timeMachine.getCurrentTime()); 
			yearF.setText(Integer.toString(aTimeD.firstTime.StartTime().getYear()+1900));
			monthF.setText(Integer.toString(aTimeD.firstTime.StartTime().getMonth()+1));
			dayF.setText(Integer.toString(aTimeD.firstTime.StartTime().getDate()));
			sTimeH.setText(Integer.toString(aTimeD.firstTime.StartTime().getHours()));
			sTimeM.setText(Integer.toString(aTimeD.firstTime.StartTime().getMinutes()));
			eTimeH.setText(Integer.toString(aTimeD.firstTime.EndTime().getHours()));
			eTimeM.setText(Integer.toString(aTimeD.firstTime.EndTime().getMinutes()));
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

	private int getTime(JTextField h, JTextField min) {

		int hour = Utility.getNumber(h.getText());
		if (hour == -1)
			return -1;
		int minute = Utility.getNumber(min.getText());
		if (minute == -1)
			return -1;

		return (hour * 60 + minute);

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
	
	public void modifyAppt() {
		int[] validDate = getValidDate();
		int[] validTime = getValidTimeInterval();
		TimeSpan apptTimeSpan = new TimeSpan(CreateTimeStamp(validDate, validTime[0]), CreateTimeStamp(validDate, validTime[1]));
		
		if(NewAppt.TimeSpan().StartTime().equals(apptTimeSpan.StartTime()) && NewAppt.TimeSpan().EndTime().equals(apptTimeSpan.EndTime())) return;
		
		//if(NewAppt.TimeSpan().StartTime().before(apptTimeSpan.StartTime()) && NewAppt.TimeSpan().EndTime().after(apptTimeSpan.StartTime())) return;
		//if(apptTimeSpan.Overlap(NewAppt.TimeSpan())) return;
		
		saveButtonResponse();
	}
	
	//drag appt
	public void setStartTime(int hour, int min) {
		sTimeH.setText(Integer.toString(hour));
		sTimeM.setText(Integer.toString(min));
		
		TimeSpan interval = NewAppt.TimeSpan();
		
		int hroffset = hour + (int)Math.floor(interval.TimeLength() / 60);
		int minoffset = min + (interval.TimeLength() / 15 % 4) * 15;
		
		if(minoffset >= 60) {
			minoffset -= 60;
			hroffset += 1;
		}
		
		eTimeH.setText(Integer.toString(hroffset));
		eTimeM.setText(Integer.toString(minoffset));
		
	}
	
	//resize appt
	public void setEndTime(int hour, int min) {
		eTimeH.setText(Integer.toString(hour + (min == 45 ? 1 : 0)));
		eTimeM.setText(Integer.toString(min + 15 == 60 ? 0 : min + 15));
	}

	private void saveButtonResponse() {
		// Fix Me!
		// Save the appointment to the hard disk
		boolean rTimeValid=true;
		boolean noTimeConflict = true;
		boolean noLocationConflict = true;
		NewAppt.setTitle(titleField.getText());
		NewAppt.setInfo(detailArea.getText());
		NewAppt.setReminder(remField.isSelected());
		NewAppt.setJoint(isJoint);
		NewAppt.setLocation((Location)locField.getSelectedItem());
		NewAppt.setSendEmail(sEmailCB.isSelected());
		NewAppt.setSendSms(sSmsCB.isSelected());
		if(remField.isSelected()) {
			if(Utility.getNumber(rTimeH.getText())<=24 && Utility.getNumber(rTimeH.getText())>=0 && Utility.getNumber(rTimeM.getText())<=59 && Utility.getNumber(rTimeM.getText())>=0) {
				NewAppt.setReminderTime(Utility.getNumber(rTimeH.getText()), Utility.getNumber(rTimeM.getText()));
				rTimeValid = true;
			}
			else {
				rTimeValid = false;
				NewAppt.setReminderTime(0, 0);
				JOptionPane.showMessageDialog(this, "Invalid Time For Reminder !",
						"Input Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else
			rTimeValid = true;
		switch(freField.getSelectedIndex()){
			case 0:
				NewAppt.setFrequency(Appt.SINGLE);
				break;
			case 1:
				NewAppt.setFrequency(Appt.DAILY);
				break;
			case 2:
				NewAppt.setFrequency(Appt.WEEKLY);
				break;
			case 3:
				NewAppt.setFrequency(Appt.MONTHLY);
		}
		//check of valid date and time
		int[] validDate = getValidDate();
		int[] validTime = getValidTimeInterval();
		TimeSpan apptTimeSpan = new TimeSpan(CreateTimeStamp(validDate, validTime[0]), CreateTimeStamp(validDate, validTime[1]));
		NewAppt.setTimeSpan(apptTimeSpan);
		Appt[] retrivedAppts = parent.controller.RetrieveAppts(apptTimeSpan, NewAppt.getFrequency());
		//check initiator time conflict
		for(int i=0; i<retrivedAppts.length; i++) {
			if(/*retrivedAppts[i].IsScheduled() &&*/ retrivedAppts[i].getAttendList().contains(getCurrentUser()) && retrivedAppts[i].getID()!=NewAppt.getID())
				noTimeConflict = false;
		}
		//check other attendants' time conflict
		UserManagement um = UserManagement.getInstance();
		for(int i=0; i<NewAppt.getWaitingList().size(); i++) {
			//if(parent.controller.RetrieveAppts(um.getUser(NewAppt.getWaitingList().get(i)), apptTimeSpan).length!=0)
				for(int j =0; j<parent.controller.RetrieveAppts(um.getUser(NewAppt.getWaitingList().get(i)), apptTimeSpan).length; j++)
						if(parent.controller.RetrieveAppts(um.getUser(NewAppt.getWaitingList().get(i)), apptTimeSpan)[j].getID()!=NewAppt.getID())
								noTimeConflict = false;
		}
		Appt[] retriedAppts2 = parent.controller.RetrieveAppt(NewAppt.getLocation(), NewAppt.TimeSpan());
		for(int i=0; i<retriedAppts2.length; i++)
			if(/*retriedAppts2[i].IsScheduled() &&*/ retriedAppts2[i].getID()!=NewAppt.getID())
				noLocationConflict = false;
		//check if the appointment is overlapped with other appointments
		//if(!((retrivedAppts.length==0) || (retrivedAppts.length==1 && retrivedAppts[0].getID()==NewAppt.getID()))) {
		if(!noTimeConflict) {
			JOptionPane.showMessageDialog(this, "Overlap with other appointments !",
					"Input Error", JOptionPane.ERROR_MESSAGE);
		}
		if(!noLocationConflict) {
			JOptionPane.showMessageDialog(this, "Overlap with other appointments in that location!",
					"Input Error", JOptionPane.ERROR_MESSAGE);
		}
		//if(rTimeValid==true && (validDate!=null) && (validTime!=null) && ((retrivedAppts.length==0) || (retrivedAppts.length==1 && retrivedAppts[0].getID()==NewAppt.getID()))) {
		if(rTimeValid==true && noTimeConflict && noLocationConflict) {
			if(this.getTitle().equals("New")) {
				if(NewAppt.isJoint() && NewAppt.getWaitingList().size()==0)
					JOptionPane.showMessageDialog(this, "Please Select Participants For Group Event !",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				else {
					parent.controller.ManageAppt(NewAppt, ApptStorageControllerImpl.NEW);
					this.setVisible(false);
				}
			}
			if(this.getTitle().equals("Modify")) {
				if(NewAppt.TimeSpan().StartTime().before(parent.timeMachine.getCurrentTime()))
					JOptionPane.showMessageDialog(this, "Cannot Modify Past Events !",
							"Modify", JOptionPane.ERROR_MESSAGE);
				else {
					//add all people in attend list to waiting list except initiator
					for(int i = 1; i<NewAppt.getAttendList().size(); i++) {
						NewAppt.getWaitingList().add(NewAppt.getAttendList().get(i));
						NewAppt.getAttendList().remove(i);
					}
					//add all people in reject list to waiting list
					for(int i = 0; i<NewAppt.getRejectList().size(); i++) {
						NewAppt.getWaitingList().add(NewAppt.getRejectList().get(i));
						NewAppt.getRejectList().remove(i);
					}
					parent.controller.ManageAppt(NewAppt, ApptStorageControllerImpl.MODIFY);
					this.setVisible(false);
				}
			}
			if(this.getTitle().equals("Join Appointment Content Change") || this.getTitle().equals("Join Appointment Invitation")) {
				//move current user from waiting list to attend list after pressed accept button
				NewAppt.addAttendant(getCurrentUser());
				NewAppt.getWaitingList().remove(getCurrentUser());
				parent.controller.ManageAppt(NewAppt, ApptStorageControllerImpl.MODIFY);
				if(NewAppt.getWaitingList().size()==0 && NewAppt.getRejectList().size()==0)
					NewAppt.setScheduled(true);
				else
					NewAppt.setScheduled(false);
				this.setVisible(false);
			}
		}
		if(NewAppt.getAttendList().size()==0)
			NewAppt.addAttendant(getCurrentUser());
		if(isJoint == false)
			NewAppt.setScheduled(true);
		else {
			//joint appointment is scheduled only when no one is in waiting and reject list
			if(NewAppt.getWaitingList().size() == 0 && NewAppt.getRejectList().size() == 0)
				NewAppt.setScheduled(true);
			else
				NewAppt.setScheduled(false);
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
		Calendar sCal = Calendar.getInstance();
		sCal.setTime(new Date(appt.TimeSpan().StartTime().getTime()));
		Calendar eCal = Calendar.getInstance();
		eCal.setTime(new Date(appt.TimeSpan().EndTime().getTime()));
		yearF.setText(Integer.toString(sCal.get(Calendar.YEAR)));
		monthF.setText(Integer.toString(sCal.get(Calendar.MONTH)+1));
		dayF.setText(Integer.toString(sCal.get(Calendar.DAY_OF_MONTH)));
		sTimeH.setText(Integer.toString(sCal.get(Calendar.HOUR_OF_DAY)));
		sTimeM.setText(Integer.toString(sCal.get(Calendar.MINUTE)));
		eTimeH.setText(Integer.toString(eCal.get(Calendar.HOUR_OF_DAY)));
		eTimeM.setText(Integer.toString(eCal.get(Calendar.MINUTE)));
		freField.setSelectedIndex(appt.getFrequency()-1);
		titleField.setText(appt.getTitle());
		detailArea.setText(appt.getInfo());
		remField.setSelected(appt.needReminder());
		rTimeH.setText(Integer.toString(appt.getReminderTime().getHours()));
		rTimeM.setText(Integer.toString(appt.getReminderTime().getMinutes()));
		NewAppt=appt;
		isJoint = appt.isJoint();
		gEvent.setSelected(isJoint);
		sEmailCB.setSelected(appt.sendEmail());
		sSmsCB.setSelected(appt.sendSms());
		if(appt.getID()!=0) {
			//cannot add/remove participant in modify
			locField.setSelectedItem(NewAppt.getLocation());
			gEvent.setEnabled(false);
			availableTime.setEnabled(true);
			inviteBut.setVisible(false);
		}
		if(appt.getAttendList().size()==0)
			appt.addAttendant(getCurrentUser());
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
		return this.parent.mCurrUser.ID();
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
		locField.setEnabled(false);
		freField.setEnabled(false);
		remField.setEnabled(false);
		rTimeH.setEditable(false);
		rTimeM.setEditable(false);
		gEvent.setEnabled(false);
		addUser.setEnabled(false);
		availableTime.setEnabled(false);
	}
}

