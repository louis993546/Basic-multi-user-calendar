package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptDB;
import hkust.cse.calendar.apptstorage.LocationDB;
import hkust.cse.calendar.unit.Appointment;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class ApptListDialog extends JFrame implements ActionListener{
	
	JList<String> apptList;
	ArrayList<Appointment> aal= new ArrayList<Appointment>();
	DefaultListModel<String> aall = new DefaultListModel<String>();
	String op = "";
	
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
		
		//Load data
		ApptDB adb = new ApptDB();
		aal = adb.getAppointmentList();
		int aalSize = aal.size();
		for (int i = 0; i<aalSize; i++)
		{
//			System.out.println("Size = " + aalSize);
			op = op + "Title: ";
			op = op + aal.get(i).getTitle();
			aall.addElement(op);
			op = "";
			op = op + "Location: ";
			op = op + aal.get(i).getLocation();
			aall.addElement(op);
			op = "";
			aall.addElement("<html><br></html>");
		}
		apptList = new JList<String>(aall);
		all.add(apptList);
		
		contentPane.add("North", all);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
