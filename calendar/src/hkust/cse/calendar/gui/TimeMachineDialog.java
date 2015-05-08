package hkust.cse.calendar.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
	private JComboBox<String> monthF;
	private String[] monthS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
	private JTextField dayF;
	private JTextField timeHF;
	private JTextField timeMF;
	private JTextField timeSF;
	
	private JButton btnModify;
	private CalGrid cal;
	
	private Timestamp today=null;//??
	//private GregorianCalendar today;
	int date[] = new int [3];
	int time[] = new int [3];
	
	public TimeMachineDialog(CalGrid grid){
		cal = grid;
		
		today=cal.timeMachine.getTMTimestamp();//??
		date[0] = today.getYear()+ 1900;
		date[1] = today.getMonth() + 1 - 1;
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
		//monthF = new JTextField(2);
		//monthL = new JLabel("MONTH: ");
		//pStart.add(monthL);
		monthF = new JComboBox<String>(monthS);
		
		dayF = new JTextField(2);
		timeHF = new JTextField(2);
		timeMF = new JTextField(2);
		timeSF = new JTextField(2);
		
		yearF.setText(Integer.toString(date[0]));
		monthF.setSelectedIndex(date[1]);
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
		date[1] = monthF.getSelectedIndex()+1;//0~11 -> 1~12
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
			if(modifyButtonResponse()==true){
				setVisible(false);
				dispose();
			}
		}
		
		
	}
	private boolean modifyButtonResponse(){
		int[] jumpDate = getValidDate(yearF,monthF,dayF);
		int[] jumpTime =getValidTime(timeHF,timeMF,timeSF);
		if ((jumpDate==null) || (jumpTime==null)){
			return false;//input again
		}
		System.out.println("input for"+jumpDate[0]+"/"+jumpDate[1]+"/"+jumpDate[2]+" "+ 
				jumpTime[0]+":"+jumpTime[1]+":"+jumpTime[2]);
		cal.timeMachine.setTimeMachine(jumpDate[0], jumpDate[1]  , jumpDate[2], 
				jumpTime[0], jumpTime[1], jumpTime[2]);
		cal.UpdateCal();
		cal.updateAppList();
		cal.updateReminderCheckerApptlist();
		cal.updateDB();
		return true; //ok
		
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

	private int[] getValidTime(JTextField h, JTextField m, JTextField s	) {
		int hr=Utility.getNumber(h.getText());
		int min=Utility.getNumber(m.getText());
		int sec=Utility.getNumber(s.getText());
		if( !(0<=hr && hr<24) || !(0<=min && min<60) || !(0<=sec && sec<60) ){
			JOptionPane.showMessageDialog(this,
					"Invalid Time", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		int[] result=new int[3];
		result[0]=hr;
		result[1]=min;
		result[2]=sec;
		return result;
	}
}
