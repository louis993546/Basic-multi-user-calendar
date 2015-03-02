package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.locationstorage.LocationStorageController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.xmlfactory.ApptXmlFactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class LocationManager extends JDialog implements ActionListener, ListSelectionListener{
	private ApptStorageControllerImpl apptController;
	private LocationStorageController controller;
	private JList<Location> locationList;
	private DefaultListModel<Location> locationListModel;
	private JScrollPane locationScrollPane;
	private JTextField locationNameInput;
	private JTextField locationCapacityInput;
	private JButton addButton;
	private JButton saveButton;
	private JButton deleteButton;
	private JButton cancelButton;
	
	public LocationManager(ApptStorageControllerImpl apptCon) {
		apptController = apptCon;
		controller = LocationStorageController.getInstance();

		setTitle("Location Manager");
		setSize(500, 300);
		
		Container contentPane;
		contentPane = getContentPane();
		
		locationListModel = new DefaultListModel<Location>();
		locationList = new JList<Location>(locationListModel);
		locationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		locationList.addListSelectionListener(this);
		
		JPanel upper = new JPanel();
		upper.setLayout(new BorderLayout());
		locationScrollPane = new JScrollPane(locationList);
		upper.add(locationScrollPane);
		contentPane.add("North", upper);
		
		JPanel bottom = new JPanel();
		locationNameInput = new JTextField(10);
		locationNameInput.setToolTipText("Please enter a new location");
		locationCapacityInput = new JTextField(5);
		bottom.add(locationNameInput);
		bottom.add(locationCapacityInput);
		
		addButton = new JButton("Add");
		addButton.addActionListener(this);
		bottom.add(addButton);
		
		saveButton = new JButton("Modify");
		saveButton.addActionListener(this);
		bottom.add(saveButton);
		
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(this);
		bottom.add(deleteButton);
		
		cancelButton = new JButton("Close");
		cancelButton.addActionListener(this);
		bottom.add(cancelButton);
		
		contentPane.add("South", bottom);
		
		updateLocationList();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == addButton) {
			boolean error = false;
			
			Location location = new Location(locationNameInput.getText(), Integer.parseInt(locationCapacityInput.getText()));
			
			try {
				controller.manageLocation(location, LocationStorageController.NEW);
			}
			catch(IllegalArgumentException exception) {
				JOptionPane.showMessageDialog(null, exception.toString().substring(exception.toString().indexOf(':') + 1), "Error", JOptionPane.ERROR_MESSAGE);
				error = true;
			}
			
			if(!error) {
				JOptionPane.showMessageDialog(null, "Location added successfully!");
			}
		
			locationNameInput.setText(null);
			updateLocationList();
		}
		else if(e.getSource() == saveButton) {
			Location selectedLocation = locationList.getSelectedValue();
			
			if(selectedLocation == null) {
				JOptionPane.showMessageDialog(null, "No location is selected!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else {
				Object[] options ={ "Yes", "No" };  
				int option = JOptionPane.showOptionDialog(null, "Are you sure to modify " + selectedLocation + "?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); 
				if(option == 0) {
					boolean error = false;
		
					Appt[] CheckAppts = apptController.getApptForLocation(selectedLocation);
					boolean maxed=false;
					for (Appt appts: CheckAppts){
						if (Integer.parseInt(locationCapacityInput.getText())<appts.getAllPeople().size() + 1){
							maxed = true;
							break;
						}
					}
					if (maxed){
						JOptionPane.showMessageDialog(null, "There will be not enough room for people!", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					
					try {
						ApptXmlFactory apptxmlFactory = new ApptXmlFactory();
						apptxmlFactory.updateApptWithLocationName(ApptStorage.apptFile, selectedLocation.getLocationName(), locationNameInput.getText());
						controller.manageLocation(selectedLocation, LocationStorageController.MODIFY, locationNameInput.getText(), Integer.parseInt(locationCapacityInput.getText()));					
					}
					catch(IllegalArgumentException exception) {
						JOptionPane.showMessageDialog(null, exception.toString().substring(exception.toString().indexOf(':') + 1), "Error", JOptionPane.ERROR_MESSAGE);
						error = true;
					}
					

					
					if(!error) {
						JOptionPane.showMessageDialog(null, "Location modified successfully!");
					}
				
					locationNameInput.setText(null);
					updateLocationList();
				}
			}
		}
		else if(e.getSource() == deleteButton) {
			Location selectedLocation = locationList.getSelectedValue();
			
			if(selectedLocation == null) {
				JOptionPane.showMessageDialog(null, "No location is selected!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else {
				Object[] options ={ "Yes", "No" };  
				int option = JOptionPane.showOptionDialog(null, "Are you sure to delete " + selectedLocation + "?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]); 
				if(option == 0) {
					if(apptController.checkApptsHaveLocation(selectedLocation.getLocationName())) {
						
						JOptionPane.showMessageDialog(null, "Location is used by a Appointments!", "Error", JOptionPane.ERROR_MESSAGE);
						JOptionPane.showMessageDialog(null, "Location is used by a Appointments!\nLocation will be deleted after receive the confirmation of the initiators of all concerned events", "Warning", JOptionPane.ERROR_MESSAGE);
						controller.addLocationToToBeDeleteList(selectedLocation);

					}
					else {
						controller.manageLocation(selectedLocation, LocationStorageController.REMOVE);
					}
				}
			}
			
			updateLocationList();
		}
		else if(e.getSource() == cancelButton) {
			setVisible(false); //you can't see me!
			dispose(); //Destroy the JFrame object
		}
	}
	

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		Location selectedLocation = locationList.getSelectedValue();
		if(selectedLocation != null) {
			locationNameInput.setText(selectedLocation.getLocationName());
			locationCapacityInput.setText(new Integer(selectedLocation.getCapacity()).toString());
		}
	}

	private void updateLocationList() {
		locationList.setListData(controller.retrieveLocations());
	}
}
