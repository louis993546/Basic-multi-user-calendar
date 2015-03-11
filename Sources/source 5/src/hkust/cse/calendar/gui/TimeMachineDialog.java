package hkust.cse.calendar.gui;

import hkust.cse.calendar.listener.TimeMachineListener;
import hkust.cse.calendar.unit.TimeMachine;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.sql.Timestamp;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class TimeMachineDialog extends JFrame implements ActionListener, TimeMachineListener {

	private JButton btnStartTime;
	private JButton btnStopTime;
	private JButton btnResume;
	private JButton btnRewind;
	private JButton btnReset;
	private TimeMachine machine;
	
	//start date params
	private JLabel sYearL;
	private JTextField sYearF;
	private JLabel sMonthL;
	private JTextField sMonthF;
	private JLabel sDayL;
	private JTextField sDayF;
	
	//end date params
	private JLabel eYearL;
	private JTextField eYearF;
	private JLabel eMonthL;
	private JTextField eMonthF;
	private JLabel eDayL;
	private JTextField eDayF;
	
	//start time params
	private JLabel sTimeHL;
	private JTextField sTimeH;
	private JLabel sTimeML;
	private JTextField sTimeM;
	
	//end time params
	private JLabel eTimeHL;
	private JTextField eTimeH;
	private JLabel eTimeML;
	private JTextField eTimeM;
	
	//time delay
	private JLabel dTimeHL;
	private JTextField dTimeH;
	private JLabel dTimeML;
	private JTextField dTimeM;
	
	//current time
	private JLabel cTimeL;
	
	public TimeMachineDialog(TimeMachine machine) {

		this.machine = machine;
		this.machine.addElpasedListener(this);
		
		setTitle("Time Machine");
		
		Container contentPane;
		contentPane = getContentPane();
		
		//start date panel
		JPanel psDate = new JPanel();
		Border sDateBorder = new TitledBorder(null, "Start Date");
		psDate.setBorder(sDateBorder);

		//label and textfield in start date
		sYearL = new JLabel("YEAR: ");
		psDate.add(sYearL);
		sYearF = new JTextField(6);
		psDate.add(sYearF);
		sMonthL = new JLabel("MONTH: ");
		psDate.add(sMonthL);
		sMonthF = new JTextField(4);
		psDate.add(sMonthF);
		sDayL = new JLabel("DAY: ");
		psDate.add(sDayL);
		sDayF = new JTextField(4);
		psDate.add(sDayF);

		//end date panel
		JPanel peDate = new JPanel();
		Border eDateBorder = new TitledBorder(null, "End Date");
		peDate.setBorder(eDateBorder);

		//label and textfield in end date
		eYearL = new JLabel("YEAR: ");
		peDate.add(eYearL);
		eYearF = new JTextField(6);
		peDate.add(eYearF);
		eMonthL = new JLabel("MONTH: ");
		peDate.add(eMonthL);
		eMonthF = new JTextField(4);
		peDate.add(eMonthF);
		eDayL = new JLabel("DAY: ");
		peDate.add(eDayL);
		eDayF = new JTextField(4);
		peDate.add(eDayF);

		//start time panel
		JPanel psTime = new JPanel();
		Border stimeBorder = new TitledBorder(null, "Start Time");
		psTime.setBorder(stimeBorder);
		
		//label and textfield in start time
		sTimeHL = new JLabel("Hour");
		psTime.add(sTimeHL);
		sTimeH = new JTextField(4);
		psTime.add(sTimeH);
		sTimeML = new JLabel("Minute");
		psTime.add(sTimeML);
		sTimeM = new JTextField(4);
		psTime.add(sTimeM);

		//end time panel
		JPanel peTime = new JPanel();
		Border etimeBorder = new TitledBorder(null, "End Time");
		peTime.setBorder(etimeBorder);
		
		//label and textfield in end time
		eTimeHL = new JLabel("Hour");
		peTime.add(eTimeHL);
		eTimeH = new JTextField(4);
		peTime.add(eTimeH);
		eTimeML = new JLabel("Minute");
		peTime.add(eTimeML);
		eTimeM = new JTextField(4);
		peTime.add(eTimeM);
		
		//time panel for start time and end time
		JPanel pTime = new JPanel();
		pTime.setLayout(new BorderLayout());
		pTime.add("West", psTime);
		pTime.add("East", peTime);
		
		//delay time panel
		JPanel pdTime = new JPanel();
		Border dtimeBorder = new TitledBorder(null, "Delay Time");
		pdTime.setBorder(dtimeBorder);
		dTimeHL = new JLabel("Hour");
		pdTime.add(dTimeHL);
		dTimeH = new JTextField(4);
		pdTime.add(dTimeH);
		dTimeML = new JLabel("Minute");
		pdTime.add(dTimeML);
		dTimeM = new JTextField(4);
		pdTime.add(dTimeM);
		
		//current time label
		cTimeL = new JLabel("");
		
		
		//time panel for delay time
		JPanel pdsTime = new JPanel();
		pdsTime.setLayout(new BorderLayout());
		pdsTime.add("West", pdTime);
		pdsTime.add("East", cTimeL);

		//top panel
		JPanel pTop = new JPanel();
		pTop.setLayout(new BorderLayout());
		pTop.setBorder(new BevelBorder(BevelBorder.RAISED));
		pTop.add(psDate, BorderLayout.NORTH);
		pTop.add(peDate, BorderLayout.SOUTH);
		
		//bottom panel
		JPanel pBottom = new JPanel();
		pBottom.setLayout(new BorderLayout());
		pBottom.setBorder(new BevelBorder(BevelBorder.RAISED));
		pBottom.add(pTime, BorderLayout.NORTH);
		pBottom.add(pdsTime, BorderLayout.SOUTH);

		//window panel
		JPanel pWindows = new JPanel();
		pWindows.setLayout(new BorderLayout());
		pWindows.setBorder(new BevelBorder(BevelBorder.RAISED));
		pWindows.add(pTop, BorderLayout.NORTH);
		pWindows.add(pBottom, BorderLayout.SOUTH);
		
		//add top and bottom to contentPane
		contentPane.add("North", pWindows);
		
		// start & stop button
		JPanel butPanel = new JPanel();
		butPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		btnStartTime = new JButton("Start");
		btnStartTime.addActionListener(this);
		butPanel.add(btnStartTime);

		btnResume = new JButton("Resume");
		btnResume.addActionListener(this);
		butPanel.add(btnResume);

		btnStopTime = new JButton("Stop");
		btnStopTime.addActionListener(this);
		butPanel.add(btnStopTime);
		
		btnRewind = new JButton("Rewind");
		btnRewind.addActionListener(this);
		butPanel.add(btnRewind);
	
		btnReset = new JButton("Reset");
		btnReset.addActionListener(this);
		butPanel.add(btnReset);

		contentPane.add("South", butPanel);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		if(machine.IsStart())
			setEnable(false);
		else
			setEnable(true);
		
		sYearF.setText("2014");
		sMonthF.setText("10");
		sDayF.setText("27");
		sTimeH.setText("8");
		sTimeM.setText("0");
		
		eYearF.setText("2014");
		eMonthF.setText("11");
		eDayF.setText("10");
		eTimeH.setText("0");
		eTimeM.setText("0");
		
		dTimeH.setText("0");
		dTimeM.setText("15");
	}
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource() == btnStartTime) {
 			try {
 				
 				machine.changeStartTime(new Timestamp(Integer.parseInt(sYearF.getText()) - 1900, Integer.parseInt(sMonthF.getText()) - 1,Integer.parseInt( sDayF.getText()), Integer.parseInt(sTimeH.getText()), Integer.parseInt(sTimeM.getText()), 0, 0));
 				machine.changeEndTime(new Timestamp(Integer.parseInt(eYearF.getText()) - 1900, Integer.parseInt(eMonthF.getText()) - 1, Integer.parseInt(eDayF.getText()), Integer.parseInt(eTimeH.getText()), Integer.parseInt(eTimeM.getText()), 0, 0));
 				machine.changeTimeDelay((Integer.parseInt(dTimeH.getText()) * 60 +  Integer.parseInt(dTimeM.getText())) * 60000);
 				
 				machine.start();
 				setEnable(false);
 				setDelayEnable(false);
 				btnResume.setEnabled(false);
 				btnRewind.setEnabled(false);
 				btnReset.setEnabled(false);
 			}
 			catch(NumberFormatException e) {
 				JOptionPane.showMessageDialog(null,
 	 				    "Please enter valid input!",
 	 				    "Warning",
 	 				    JOptionPane.WARNING_MESSAGE);
 			}	
		}
		else if(arg0.getSource() == btnStopTime) {
			machine.stop();
			setEnable(false);
			setDelayEnable(true);
			btnStopTime.setEnabled(false);
			btnResume.setEnabled(true);
			btnRewind.setEnabled(true);
			btnReset.setEnabled(true);
			
		}
		else if(arg0.getSource() == btnResume) {
			machine.changeTimeDelay((Integer.parseInt(dTimeH.getText()) * 60 +  Integer.parseInt(dTimeM.getText())) * 60000);
			machine.resume();
			setEnable(false);
			setDelayEnable(false);
			btnResume.setEnabled(false);
			btnRewind.setEnabled(false);
			btnReset.setEnabled(false);
		}
		else if(arg0.getSource() == btnReset) {
			cTimeL.setText("");
			machine.stop();
			machine.reset();
			setEnable(true);
		}
		else if(arg0.getSource() == btnRewind) {
			machine.changeTimeDelay((Integer.parseInt(dTimeH.getText()) * 60 +  Integer.parseInt(dTimeM.getText())) * 60000);
			machine.rewind();
			setEnable(false);
			setDelayEnable(false);
			btnResume.setEnabled(false);
			btnRewind.setEnabled(false);
			btnReset.setEnabled(false);
		}
	}
	
	public void timeElapsed(TimeMachine sender) {
		cTimeL.setText("Time: " + sender.toString());
	}
	
	public void timeStopped(TimeMachine sender) {
		//cTimeL.setText("");
		setEnable(true);
	}
	
	private void setEnable(Boolean b) {
		btnStopTime.setEnabled(!b);
		btnResume.setEnabled(!b);
		btnReset.setEnabled(!b);
		btnRewind.setEnabled(!b);
		btnStartTime.setEnabled(b);
		sYearL.setEnabled(b);
		sYearF.setEnabled(b);
		sMonthL.setEnabled(b);
		sMonthF.setEnabled(b);
		sDayL.setEnabled(b);
		sDayF.setEnabled(b);
		eYearL.setEnabled(b);
		eYearF.setEnabled(b);
		eMonthL.setEnabled(b);
		eMonthF.setEnabled(b);
		eDayL.setEnabled(b);
		eDayF.setEnabled(b);
		sTimeHL.setEnabled(b);
		sTimeH.setEnabled(b);
		sTimeML.setEnabled(b);
		sTimeM.setEnabled(b);
		eTimeHL.setEnabled(b);
		eTimeH.setEnabled(b);
		eTimeML.setEnabled(b);
		eTimeM.setEnabled(b);
	}
	
	private void setDelayEnable(Boolean b) {
		dTimeHL.setEnabled(b);
		dTimeH.setEnabled(b);
		dTimeML.setEnabled(b);
		dTimeM.setEnabled(b);
	}
}
