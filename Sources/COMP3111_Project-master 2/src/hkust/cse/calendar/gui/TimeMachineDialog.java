package hkust.cse.calendar.gui;

import hkust.cse.calendar.system.TimeMachine;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TimeMachineDialog extends JFrame implements ActionListener,ChangeListener{
	private JLabel selectmsg;
	private JRadioButton defaulttime;
	private JRadioButton customtime;
	private ButtonGroup timegp;
	
	private JSpinner yearSpinner;
	private JSpinner monthSpinner;
	private JSpinner dateSpinner;
	private JSpinner hourSpinner;
	private JSpinner minuteSpinner;
	private JSpinner secondSpinner;
	private Timestamp timestamp;
	private JButton saveBut;
	private CalGrid parent;
	
	private Timestamp today;
	int date[] = new int [3];
	int time[] = new int [3];
	
	public TimeMachineDialog(CalGrid cal) {
		setAlwaysOnTop(true);
		setTitle("Change Time Setting");
		parent = cal;
		Container contentPane;
		contentPane = getContentPane();
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		
		JPanel selectPanel = new JPanel();
		Border selectBorder = new TitledBorder(null, "SELECT TIME SETTING");
		selectPanel.setBorder(selectBorder);
		
		selectmsg = new JLabel("Please select the time setting   ");
		selectPanel.add(selectmsg);
		
		defaulttime = new JRadioButton("USE SYSTEM TIME   ");
		customtime = new JRadioButton("USE CUSTOM TIME   ");
		defaulttime.setActionCommand("default");
		customtime.setActionCommand("custom");
		timegp = new ButtonGroup();
		timegp.add(defaulttime);
		timegp.add(customtime);
		selectPanel.add(defaulttime);
		selectPanel.add(customtime);
		defaulttime.setSelected(true);
		defaulttime.addActionListener(this);
		customtime.addActionListener(this);
		top.add(selectPanel, BorderLayout.NORTH);
		
		// panel
		JPanel timePanel = new JPanel();
		Border timeBorder = new TitledBorder(null, "TIME MACHINE");
		timePanel.setBorder(timeBorder);
		timestamp = TimeMachine.getInstance().getNowTimestamp();

		yearSpinner = new JSpinner(new SpinnerNumberModel (timestamp.getYear()+1900,1900,2100,1));
		monthSpinner = new JSpinner(new SpinnerNumberModel (timestamp.getMonth()+1,1,12,1));
		dateSpinner = new JSpinner(new SpinnerNumberModel (timestamp.getDate(),1,31,1));
		hourSpinner = new JSpinner(new SpinnerNumberModel (timestamp.getHours(),0,23,1));
		minuteSpinner = new JSpinner(new SpinnerNumberModel (timestamp.getMinutes(),0,59,1));
		secondSpinner = new JSpinner(new SpinnerNumberModel (timestamp.getSeconds(),0,59,1));

		yearSpinner.addChangeListener(this);
		monthSpinner.addChangeListener(this);
		dateSpinner.addChangeListener(this);
		hourSpinner.addChangeListener(this);
		minuteSpinner.addChangeListener(this);
		secondSpinner.addChangeListener(this);
		
		JLabel yearLabel = new JLabel("YEAR: ");
		JLabel monthLabel = new JLabel("MONTH: ");
		JLabel dateLabel = new JLabel("DAY: ");
		JLabel hourLabel = new JLabel("HOUR: ");
		JLabel minuteLabel = new JLabel("MINUTE: ");
		JLabel secondLabel = new JLabel("SECOND: ");
		
		// disable all spinner because we default using the system time
		setAllTextFieldEnabled(false);
		setAllEnabled(false);

		timePanel.add(yearLabel);
		timePanel.add(yearSpinner);
		timePanel.add(monthLabel);
		timePanel.add(monthSpinner);
		timePanel.add(dateLabel);
		timePanel.add(dateSpinner);
		timePanel.add(hourLabel);
		timePanel.add(hourSpinner);
		timePanel.add(minuteLabel);
		timePanel.add(minuteSpinner);
		timePanel.add(secondLabel);
		timePanel.add(secondSpinner);

		top.add(timePanel, BorderLayout.SOUTH);
		
		contentPane.add("North", top);
		
		JPanel savePanel = new JPanel();
		saveBut = new JButton("Save Time");
		saveBut.addActionListener(this);
		savePanel.add(saveBut);
		contentPane.add("South", savePanel);
		
		setResizable(false);
		pack();
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub		
		if (e.getSource() == saveBut) {
			boolean succeed = saveButResponse(timegp.getSelection().getActionCommand());
			if (succeed) {
				setVisible(false);
				dispose();
			}
		} 
		
		if (defaulttime.isSelected()) {

			// set spinner to the current time of time machine
			today = new Timestamp(System.currentTimeMillis());
			date[0] = today.getYear() + 1900;
			date[1] = today.getMonth();
			date[2] = today.getDate();
			time[0] = today.getHours();
			time[1] = today.getMinutes();
			time[2] = today.getSeconds();
			
			yearSpinner.setValue(date[0]);
			monthSpinner.setValue(date[1]);
			dateSpinner.setValue(date[2]);
			hourSpinner.setValue(time[0]);
			minuteSpinner.setValue(time[1]);
			secondSpinner.setValue(time[2]);

			// disable the spinner
			setAllEnabled(false);
			setAllTextFieldEnabled(false);


		} else if (customtime.isSelected()) {
			// enable the spinner
			setAllEnabled(true);
			setAllTextFieldEnabled(true);
		}
	}
	
	private int[] getDate() {

		int[] date = new int[3];
		date[0] = (Integer) yearSpinner.getValue();
		date[1] = (Integer) monthSpinner.getValue();
		date[2] = (Integer) dateSpinner.getValue();

		return date;
	}

	private int[] getTime(){
		int[] time = new int[3];
		time[0] = (Integer) hourSpinner.getValue();
		time[1] = (Integer) minuteSpinner.getValue();
		time[2] = (Integer) secondSpinner.getValue();
		return time;

	}

	private boolean saveButResponse(String tmsetting){

		if (tmsetting == "custom") { 
			// custom time
			int date[] = getDate();
			int time[] = getTime();
			TimeMachine timeMachine = TimeMachine.getInstance();
			timeMachine.setTimeMachine(date[0], date[1] - 1, date[2], time[0], time[1], time[2]);

			return true;
		} else {
			
			// system time
			today = new Timestamp(System.currentTimeMillis());
			date[0] = today.getYear() + 1900;
			date[1] = today.getMonth();
			date[2] = today.getDate();
			time[0] = today.getHours();
			time[1] = today.getMinutes();
			time[2] = today.getSeconds();
//			System.out.println(date[0] + " " + date[1] +  " " +date[2] +  " " +time[0] +  " " +time[1] +  " " +time[2]);

			TimeMachine timeMachine = TimeMachine.getInstance();
			timeMachine.setTimeMachine(date[0], date[1], date[2], time[0], time[1], time[2]);

		}
		return true;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		
		if((JSpinner)e.getSource()==yearSpinner || (JSpinner)e.getSource()==monthSpinner){
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR,(Integer) yearSpinner.getValue());
			c.set(Calendar.MONTH,(Integer) monthSpinner.getValue()-1);
			dateSpinner.setModel(new SpinnerNumberModel (1,1,c.getActualMaximum(Calendar.DATE),1));
		}
	}
	
	private void setAllTextFieldEnabled(boolean c){
		((JSpinner.DefaultEditor) yearSpinner.getEditor()).getTextField().setEditable(c);
		((JSpinner.DefaultEditor) monthSpinner.getEditor()).getTextField().setEditable(c);
		((JSpinner.DefaultEditor) dateSpinner.getEditor()).getTextField().setEditable(c);
		((JSpinner.DefaultEditor) hourSpinner.getEditor()).getTextField().setEditable(c);
		((JSpinner.DefaultEditor) minuteSpinner.getEditor()).getTextField().setEditable(c);
		((JSpinner.DefaultEditor) secondSpinner.getEditor()).getTextField().setEditable(c);
	}

	private void setAllEnabled(boolean c){
		yearSpinner.setEnabled(c);
		monthSpinner.setEnabled(c);
		dateSpinner.setEnabled(c);
		hourSpinner.setEnabled(c);
		minuteSpinner.setEnabled(c);
		secondSpinner.setEnabled(c);
	}
}