package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class GroupEventDialog extends JDialog{
	private static final long serialVersionUID = 1L;

	private ApptStorageControllerImpl _controller;
	 
	private DefaultListModel listModel;
	
	private JLabel sUser;
	private JLabel aUser;
	private JLabel aTime;
	private JList userList;
	private JList timeList;
	private JScrollPane listScrollPane;
	private JPanel apptPane;
	private JPanel buttonPane;
	private JComboBox availableUsers;
	private JButton addButton;
	private JButton removeButton;
	private JButton nextButton;
	private JButton finishButton;
	private JLabel dateL;
	private JTextField yearF;
	private JTextField monthF;
	private JTextField dayF;
	private JLabel sTimeL;
	private JTextField sTimeH;
	private JTextField sTimeM;
	private JLabel eTimeL;
	private JTextField eTimeH;
	private JTextField eTimeM;
	private JScrollPane availableTimePane;
	private JPanel centerPanel = new JPanel();
	private FlowLayout flow = new FlowLayout();
	private JTextField titleField;
	private JTextField detailArea;
	private int nextCount = 0;
	

	public GroupEventDialog(ApptStorageControllerImpl controller) {
		_controller = controller;
		this.setLayout(new BorderLayout());
		this.setLocationByPlatform(true);
		this.setSize(700, 300);
		
		//new list model
		listModel = new DefaultListModel();	
		userList = new JList(listModel);	
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		userList.setSelectedIndex(0);	
		userList.setVisibleRowCount(10);		
		
		
		listScrollPane = new JScrollPane(userList);	 
		//select users
		availableUsers = new JComboBox();
		
		//add button
		addButton = new JButton("Add");		
		addButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				//check if there are any duplicated items
			}
		});	
		addButton.setEnabled(true);	
		
		//remove button
		removeButton = new JButton("Remove");	 
		removeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				int index = userList.getSelectedIndex();
				if (index != -1){
					listModel.removeElementAt(index);
				}
			}
		});	
		nextButton = new JButton("Next");	
		finishButton = new JButton("Finish");
		nextButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("nextCount = "+nextCount);
				if(nextCount == 0) {
					addButton.setVisible(false);
					removeButton.setVisible(false);
					//nextButton.setVisible(false);
					//finishButton.setVisible(true);
					listScrollPane.setVisible(false);
					availableUsers.setVisible(false);
					availableTimePane.setVisible(true);
					sUser.setVisible(false);
					dateL.setVisible(true);
					yearF.setVisible(true);
					monthF.setVisible(true);
					dayF.setVisible(true);
					sTimeL.setVisible(true);
					sTimeH.setVisible(true);
					sTimeM.setVisible(true);
					eTimeL.setVisible(true);
					eTimeH.setVisible(true);
					eTimeM.setVisible(true);
					aTime.setVisible(true);
					aUser.setVisible(false);
					nextCount = 1;
				}
				else if(nextCount == 1) {
					nextButton.setVisible(false);
					finishButton.setVisible(true);
					availableTimePane.setVisible(false);
					dateL.setVisible(false);
					yearF.setVisible(false);
					monthF.setVisible(false);
					dayF.setVisible(false);
					sTimeL.setVisible(false);
					sTimeH.setVisible(false);
					sTimeM.setVisible(false);
					eTimeL.setVisible(false);
					eTimeH.setVisible(false);
					eTimeM.setVisible(false);
					aTime.setVisible(false);
				}
			}
		});
		
		//appointment panel
		apptPane = new JPanel();
		sUser = new JLabel("Selected Users : ");
		dateL = new JLabel("Appointment Date");
		yearF = new JTextField(6);
		monthF = new JTextField(4);
		dayF = new JTextField(4);
		sTimeL = new JLabel("Start Time");
		sTimeH = new JTextField(4);
		sTimeM = new JTextField(4);
		eTimeL = new JLabel("End Time");
		eTimeH = new JTextField(4);
		eTimeM = new JTextField(4);
		apptPane.add(sUser);
		apptPane.add(dateL);
		apptPane.add(yearF);
		apptPane.add(monthF);
		apptPane.add(dayF);
		apptPane.add(sTimeL);
		apptPane.add(sTimeH);
		apptPane.add(sTimeM);
		apptPane.add(eTimeL);
		apptPane.add(eTimeH);
		apptPane.add(eTimeM);
		
		//available time panel
		listModel = new DefaultListModel();	
		timeList = new JList(listModel);	
		timeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		timeList.setSelectedIndex(0);	
		timeList.setVisibleRowCount(10);
		availableTimePane = new JScrollPane(timeList);
		
		//button panel
		buttonPane = new JPanel();	
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		aUser = new JLabel("Available Users : ");
		buttonPane.add(aUser); 
		buttonPane.add(availableUsers); 
		buttonPane.add(addButton);			 
		buttonPane.add(removeButton); 
		buttonPane.add(nextButton); 

		buttonPane.add(finishButton); 
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));	
		add(apptPane, BorderLayout.NORTH);
		centerPanel.setLayout(flow);
		aTime = new JLabel("Available Time : ");
		centerPanel.add(aTime);
		centerPanel.add(listScrollPane);
		centerPanel.add(availableTimePane);
		add(centerPanel, BorderLayout.CENTER);
		add(buttonPane, BorderLayout.PAGE_END); 
		this.setVisible(true);
		this.setTitle("Create Group Event");
		//hide components of next page
		dateL.setVisible(false);
		yearF.setVisible(false);
		monthF.setVisible(false);
		dayF.setVisible(false);
		sTimeL.setVisible(false);
		sTimeH.setVisible(false);
		sTimeM.setVisible(false);
		eTimeL.setVisible(false);
		eTimeH.setVisible(false);
		eTimeM.setVisible(false);
		finishButton.setVisible(false);
		availableTimePane.setVisible(false);
		aTime.setVisible(false);
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
		titleField.setText(appt.getTitle());
		detailArea.setText(appt.getInfo());
	}
}
