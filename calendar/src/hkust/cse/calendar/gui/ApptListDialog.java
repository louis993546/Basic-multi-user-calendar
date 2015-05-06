package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptDB;
import hkust.cse.calendar.unit.Appointment;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class ApptListDialog extends JFrame implements ActionListener{
	
	JList<String> apptList;
	ArrayList<Appointment> aal= new ArrayList<Appointment>();
	DefaultListModel<String> aall = new DefaultListModel<String>();
	JButton delete = new JButton("Delete");
	String op = "";
	ApptDB adb = new ApptDB();
	
	public ApptListDialog()
	{
		setTitle("Appointment List");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		Container contentPane;
		contentPane = getContentPane();
		
		//UI
		JPanel all = new JPanel();		//A JPanel that holds everything
		Border allBorder = new TitledBorder(null, "List of appointments:");
		all.setBorder(allBorder);		//Add border text
		
		JPanel buttons = new JPanel();
		delete.addActionListener(this);
		buttons.add(delete);
		
		//Load data
		ApptDB adb = new ApptDB();
		aal = adb.getAppointmentList();
		int aalSize = aal.size();
		for (int i = 0; i<aalSize; i++)
		{
//			op = op + "Title: ";
			op = op + aal.get(i).getTitle();
//			op = op + "Location: ";
//			op = op + aal.get(i).getLocation();
			aall.addElement(op);
			op = "";
		}
		apptList = new JList<String>(aall);
		all.add(apptList);
		
		contentPane.add("West", all);
		contentPane.add("East", buttons);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == delete)
		{
			//TODO this is not safe!!!
			//multiple appointments can have the same name!!
			int id = adb.getApptIDByTitle(apptList.getSelectedValue().toString());
			if ((id != 0) || (id != -1)) {
				adb.deleteAppt(id);
				aall.removeElementAt(apptList.getSelectedIndex());
			}
		}
		else
		{
			
		}
	}
}
