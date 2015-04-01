package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.TimeMachine;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TimeMachineDialog extends JFrame implements ActionListener{
	
	private JLabel yearL;
	private JLabel monthL;
	private JLabel dayL;
	private JLabel timeHL;
	private JLabel timeML;
	private JLabel timeSL;
	
	private JTextField yearF;
	private JTextField monthF;
	private JTextField dayF;
	private JTextField timeHF;
	private JTextField timeMF;
	private JTextField timeSF;
	
	private JButton btnModify;
	private CalGrid cal;
	
	private Timestamp today;
	//private GregorianCalendar today;
	int date[] = new int [3];
	int time[] = new int [3];
	
	public TimeMachineDialog(CalGrid grid){
		cal = grid;
		
		//today = TimeMachine.getInstance().getMToday();
		today = TimeMachine.getInstance().getMTimestamp();
		//date[0] = today.get(Calendar.YEAR);
		//date[1] = today.get(today.MONTH) + 1;
		//date[2] = today.get(today.DAY_OF_MONTH);
		//time[0] = 0;
		//time[1] = 0;
		//time[2] = 0;
		date[0] = today.getYear() + 1900;
		date[1] = today.getMonth() + 1;
		date[2] = today.getDate();
		time[0] = today.getHours();
		time[1] = today.getMinutes();
		time[2] = today.getSeconds();
		
		setTitle("Modify the clock");
		Container contentPane;
		contentPane = getContentPane();
		
		JPanel tmd = new JPanel();
		tmd.setLayout(new BoxLayout(tmd, BoxLayout.Y_AXIS));
		
		JPanel topPanel = new JPanel();
		JLabel title = new JLabel("Please Modify The Clock!");
		
		JPanel mainPanel = new JPanel();
		yearL = new JLabel("Year: ");
		monthL = new JLabel("Month: ");
		dayL = new JLabel("Day: ");
		timeHL = new JLabel("Hour: ");
		timeML = new JLabel("Minute: ");
		timeSL = new JLabel("Second: ");
		
		yearF = new JTextField(4);
		monthF = new JTextField(2);
		dayF = new JTextField(2);
		timeHF = new JTextField(2);
		timeMF = new JTextField(2);
		timeSF = new JTextField(2);
		
		yearF.setText(Integer.toString(date[0]));
		monthF.setText(Integer.toString(date[1]));
		dayF.setText(Integer.toString(date[2]));
		timeHF.setText(Integer.toString(time[0]));
		timeMF.setText(Integer.toString(time[1]));
		timeSF.setText(Integer.toString(time[2]));
		
		
		mainPanel.add(yearL);
		mainPanel.add(yearF);
		mainPanel.add(monthL);
		mainPanel.add(monthF);
		mainPanel.add(dayL);
		mainPanel.add(dayF);
		mainPanel.add(timeHL);
		mainPanel.add(timeHF);
		mainPanel.add(timeML);
		mainPanel.add(timeMF);
		mainPanel.add(timeSL);
		mainPanel.add(timeSF);
		
		JPanel btnPanel = new JPanel();
		btnModify = new JButton("Modify");
		btnModify.addActionListener(this);
		btnPanel.add(btnModify);
		
		tmd.add(topPanel, BorderLayout.NORTH);
		tmd.add(mainPanel, BorderLayout.CENTER);
		tmd.add(btnPanel, BorderLayout.SOUTH);
		
		contentPane.add("Center", tmd);
	
		pack();
		setVisible(true);
	}
	
	public int[] getDate() {
		int[] date = new int[3];
		date[0] = Integer.parseInt(yearF.getText());
		date[1] = Integer.parseInt(monthF.getText());
		date[2] = Integer.parseInt(dayF.getText());
		
		return date;
	}

	public int[] getTime() {
		int[] time = new int[3];
		time[0] = Integer.parseInt(timeHF.getText());
		time[1] = Integer.parseInt(timeMF.getText());
		time[2] = Integer.parseInt(timeSF.getText());
		
		return time;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == btnModify){
			modifyButtonResponse();
			setVisible(false);
		}
		
		
	}
	private void modifyButtonResponse(){
		int date[] = getDate();
		int time[] = getTime();

		TimeMachine timeMachine = TimeMachine.getInstance();
		timeMachine.setTimeMachine(date[0], date[1] - 1 , date[2], time[0], time[1], time[2]);
		cal.UpdateCal();
		
	}

}
