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
	private String[] timeHS = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
			"12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
	private String[] timeMS = {"00", "15", "30", "45"};
	private String[] reminderS = {"Minute(s)", "Hour(s)", "Day(s)", "Week(s)", "Month(s)", "Year(s)", "Decade(s)"};
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

	private JTextArea detailArea;

	private JSplitPane pDes;
	JPanel detailPanel;
	
	private Appt tempAppt;
	private int saveOrModify = 0;

//	private JTextField attendField;
//	private JTextField rejectField;
//	private JTextField waitingField;
	private int selectedApptId = -1;

	private LocationDB ldb;
	private ApptDB adb;

	private void commonConstructor(String title, CalGrid cal) {
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

		//End JPanel
		JPanel pEnd = new JPanel();
		Border endBorder = new TitledBorder(null, "END TIME");
		pEnd.setBorder(endBorder);
		yearL = new JLabel("YEAR: ");
		pEnd.add(yearL);
		yearEF = new JTextField(6);
		pEnd.add(yearEF);
		monthL = new JLabel("MONTH: ");
		pEnd.add(monthL);
		monthEF = new JComboBox<String>(monthS);
		pEnd.add(monthEF);
		dayL = new JLabel("DAY: ");
		pEnd.add(dayL);
		dayEF = new JTextField(4);
		pEnd.add(dayEF);
		eTimeHL = new JLabel("Hour");
		pEnd.add(eTimeHL);
		eTimeH = new JComboBox<String>(timeHS);
		pEnd.add(eTimeH);
		eTimeML = new JLabel("Minute");
		pEnd.add(eTimeML);
		eTimeM = new JComboBox<String>(timeMS);
		pEnd.add(eTimeM);

		//Location Panel
		JPanel lPanel = new JPanel();
		Border lBorder = new TitledBorder(null, "Location");
		lPanel.setBorder(lBorder);
		lCB = new JComboBox(locationAL.toArray());
		lPanel.add(lCB);

		//Repeat Panel
		JPanel rPanel = new JPanel();
		Border rBorder = new TitledBorder(null, "Repeat(Optional)");
		rPanel.setBorder(rBorder);
		JCheckBox rChB = new JCheckBox("On");
		rPanel.add(rChB);
		JTextField rTF = new JTextField(5);
		rPanel.add(rTF);
		JComboBox rCB = new JComboBox(repeatS);
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

	AppScheduler(String title, CalGrid cal, int selectedApptId) {
		
		this.selectedApptId = selectedApptId;
		commonConstructor(title, cal);
	}

	AppScheduler(String title, CalGrid cal) {
		commonConstructor(title, cal);
	}
	
	AppScheduler(String title, CalGrid cal, Appt appt)
	{
		tempAppt = appt;
		saveOrModify=1;
		commonConstructor(title, cal);
		//TODO put info of appt into each field
		sTimeH.setSelectedItem("0" + appt.getAppointment().getStartHour());
		sTimeM.setSelectedItem("0" + appt.getAppointment().getStartMin());
		titleField.setText(appt.getTitle());
		yearEF.setText("" + appt.getAppointment().getEndYear());
		yearSF.setText("" + appt.getAppointment().getStartYear());
	}

	public void actionPerformed(ActionEvent e) {
		// distinguish which button is clicked and continue with require function
		if (e.getSource() == CancelBut) {
			setVisible(false);
			dispose();
		} else if (e.getSource() == saveBut) {
			try {
				saveButtonResponse();
				setVisible(false);
				dispose();
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
		date[0] = Utility.getNumber(a.getText());
		date[1] = Utility.getNumber(b.getSelectedItem().toString());
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

		date[2] = Utility.getNumber(d.getText());
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

	private int getTime(JComboBox h, JComboBox eTimeM2) {

		int hour = Utility.getNumber(h.getSelectedItem().toString());
		if (hour == -1)
			return -1;
		int minute = Utility.getNumber(eTimeM2.getSelectedItem().toString());
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

		if (!sTimeM.getSelectedItem().toString().equals("0") && !sTimeM.getSelectedItem().toString().equals("15")
				&& !sTimeM.getSelectedItem().toString().equals("30") && !sTimeM.getSelectedItem().toString().equals("45")
			|| !eTimeM.getSelectedItem().toString().equals("0") && !eTimeM.getSelectedItem().toString().equals("15")
			&& !eTimeM.getSelectedItem().toString().equals("30") && !eTimeM.getSelectedItem().toString().equals("45")){
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

	private void saveButtonResponse() throws SQLException {
		// TODO unfinished save button

		int[] startDate = getValidDate(yearSF, monthSF, daySF);
		int[] endDate = getValidDate(yearEF, monthEF, dayEF);
		//TODO get time using getValidTimeInterval()
		int shr = Integer.parseInt(sTimeH.getSelectedItem().toString());
		int smin = Integer.parseInt(sTimeM.getSelectedItem().toString());
		int ehr = Integer.parseInt(eTimeH.getSelectedItem().toString());
		int emin = Integer.parseInt(eTimeM.getSelectedItem().toString());
		//check if end date earlier then start date
		String title = titleField.getText().trim();
		String description = detailArea.getText();
		String location = lCB.getSelectedItem().toString();

		//currently it provide 3 empty linkedlist
		LinkedList<String> temp = new LinkedList<String>();
		Appointment newAppt = new Appointment(title, description, location, shr, smin, startDate[0], startDate[1], startDate[2], ehr, emin, endDate[0], endDate[1], endDate[2], 0, 0, 0, temp, temp, temp, 12);
		adb = new ApptDB();
		if (saveOrModify == 0)
		{
			adb.addAppt(newAppt);
		}
		else
		{
			adb.modifyAppt(tempAppt.getID(), newAppt);
		}
		JOptionPane.showMessageDialog(null, "Saved.",
				"Exit", JOptionPane.YES_NO_OPTION);
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
		// TODO I have no idea
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
		yearEF.setEditable(false);
		monthSF.setEditable(false);
		monthEF.setEditable(false);
		daySF.setEditable(false);
		dayEF.setEditable(false);
		sTimeH.setEditable(false);
		sTimeM.setEditable(false);
		eTimeH.setEditable(false);
		eTimeM.setEditable(false);
		titleField.setEditable(false);
		detailArea.setEditable(false);
	}
}
