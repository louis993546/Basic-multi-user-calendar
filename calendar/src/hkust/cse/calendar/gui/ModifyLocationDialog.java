package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.LocationDB;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class ModifyLocationDialog extends JFrame implements ActionListener{
	private JList<String> locationList;
	private DefaultListModel<String> locationListModel = new DefaultListModel<String>();
	private JButton deleteButton;
	private JButton modifyButton;
	private JButton exitButton;
	private LocationDB ldb;
	private ArrayList<String> locationStringAL = new ArrayList<String>();
	
	public ModifyLocationDialog() {
		
		setTitle("Modify Location");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		ldb = new LocationDB();
		Container contentPane;
		contentPane = getContentPane();
		locationStringAL = ldb.getLocationList();

		//create a new JPanel to hold everything
		JPanel all = new JPanel();
		all.setLayout(new BoxLayout(all, BoxLayout.X_AXIS));
		
		JPanel listPanel = new JPanel();
		Border lpb = new TitledBorder(null, "Locations:");
		listPanel.setBorder(lpb);
		Box left = Box.createVerticalBox();
		for (String a:locationStringAL) {
			locationListModel.addElement(a);
		}
		locationList = new JList<String>(locationListModel);
		listPanel.add(locationList);
		left.add(listPanel);
		
		Box right = Box.createVerticalBox();
		modifyButton = new JButton("Modify");
		modifyButton.addActionListener(this);
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(this);
		exitButton = new JButton("Exit");
		exitButton.addActionListener(this);
		right.add(modifyButton);
		right.add(deleteButton);
		right.add(exitButton);
		
		all.add(left);
		all.add(right);
		
		contentPane.add("North", all);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public ModifyLocationDialog(LocationDB ldb2) {
		this();
		ldb = ldb2;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO modify and exit button still useless
		if (e.getSource() == deleteButton)
		{
			int id = ldb.getLocationID(locationList.getSelectedValue().toString());
			if ((id != 0) || (id != -1)) {
				ldb.deleteLocation(id);
				locationListModel.removeElementAt(locationList.getSelectedIndex());
				System.out.println("Done");
			}
		}
		else if (e.getSource() == modifyButton)
		{
			
		}
		else if (e.getSource() == exitButton)
		{
			
		}
		else
		{
			System.out.println("Something's wrong");
		}
	}
}
