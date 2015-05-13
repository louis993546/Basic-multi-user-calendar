package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptDB;
import hkust.cse.calendar.apptstorage.LocationDB;
import hkust.cse.calendar.apptstorage.MessageStorage;
import hkust.cse.calendar.unit.MessageBody;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.SortedMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class ModifyLocationDialog extends JFrame implements ActionListener {
	private JList<String> locationList;
	private DefaultListModel<String> locationListModel = new DefaultListModel<String>();
	private JButton deleteButton;
	private JButton modifyButton;
	private JButton exitButton;
	private LocationDB ldb;
	private ApptDB adb;
	private ArrayList<String> locationStringAL = new ArrayList<String>();

	public ModifyLocationDialog() {

		setTitle("Modify Location");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		ldb = LocationDB.getInstance();
		adb = new ApptDB();
		Container contentPane;
		contentPane = getContentPane();
		locationStringAL = ldb.getLocationList();

		// create a new JPanel to hold everything
		JPanel all = new JPanel();
		all.setLayout(new BoxLayout(all, BoxLayout.X_AXIS));

		JPanel listPanel = new JPanel();
		Border lpb = new TitledBorder(null, "Locations:");
		listPanel.setBorder(lpb);
		Box left = Box.createVerticalBox();
		for (String a : locationStringAL) {
			locationListModel.addElement(a + ": " + ldb.getCapacityByName(a));
		}
		locationList = new JList<String>(locationListModel);
		listPanel.add(locationList);
		left.add(listPanel);

		Box right = Box.createVerticalBox();
		modifyButton = new JButton("New");
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
		if (e.getSource() == deleteButton) {

//			int id = ldb.getLocationID(locationList.getSelectedValue().toString());
//			if ((id != 0) || (id != -1)) 
//			{
//				//TODO check if anyone is using it first
//				Appt[] listOfAppt = adb.getApptByLocationName(locationList.getSelectedValue().toString());
//				if (listOfAppt.length > 1)
//				{
//					//ask all those users first
//					//somehow lock this location: set capacity to negative
//					//TODO should not display location with -ve capacity
//				}
//				else
//				{
//					ldb.deleteLocation(id);
//					locationListModel.removeElementAt(locationList.getSelectedIndex());

			int locationID = ldb.getLocationID(locationList.getSelectedValue()
					.toString().split(": ")[0]);
			System.out.println("location id is " + locationID);
			if ((locationID != 0) && (locationID != -1)) {
				SortedMap<Integer, MessageBody> tmpmap = MessageStorage
						.getDeleteLocation();
				
				SortedMap<Integer, LocalDateTime> creatorToLastRelatedEventMap = MessageStorage
						.getCreatorToLastRelatedEventMap(locationID, "location");

				for (int key : creatorToLastRelatedEventMap.keySet()) {
					MessageBody tmpmsgbody2 = new MessageBody(-1, locationID,
							-1,
							creatorToLastRelatedEventMap.get(key), key);
					int insertKey;
					if (!(tmpmap.isEmpty())) {
						insertKey = tmpmap.lastKey() + 1;
					} else {
						insertKey = 1;// start from msgid 1
					}

					tmpmap.put(insertKey, tmpmsgbody2);//
				}

				System.out.println("tmpmap is" + tmpmap);

				if (creatorToLastRelatedEventMap.isEmpty()) {
					ldb.deleteLocation(locationID);
					locationListModel.removeElementAt(locationList
							.getSelectedIndex());

				}
			}
		} else if (e.getSource() == modifyButton) {
			// TODO modify button
			// new GUI
			// load string into that GUI
			// call update method in LocationDB
			// TODO Location needs ID field
			new NewLocationDialog();
		} else if (e.getSource() == exitButton) {
			dispose();
		} else {
			System.out.println("Something's wrong");
		}
	}
}
