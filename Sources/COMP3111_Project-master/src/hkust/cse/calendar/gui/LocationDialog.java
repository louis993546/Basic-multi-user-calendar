package hkust.cse.calendar.gui;

import hkust.cse.calendar.locationstorage.LocationStorageController;
import hkust.cse.calendar.unit.Location;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LocationDialog extends JFrame implements ActionListener{
	private JTextField locationName;
	private JTextField locationCapacity;
	private JLabel errorMsg;
	private JButton saveButton;
	private JButton cancelButton;
	private LocationStorageController locationController;
	private Location location;
	
	public LocationDialog(LocationStorageController controller) {
		locationController = controller;
		errorMsg = new JLabel(" ");
		setTitle("Add Location");
		
		
		Container contentPane;
		contentPane = getContentPane();
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		
		JPanel messPanel = new JPanel();
		messPanel.add(new JLabel("Please input the new location"));
		top.add(messPanel);
		
		JPanel namePanel = new JPanel();
		namePanel.add(new JLabel("Location Name:"));
		locationName = new JTextField(15);
		namePanel.add(locationName);
		locationCapacity = new JTextField(15);
		namePanel.add(locationCapacity);
		top.add(namePanel);
		
		JPanel error = new JPanel();
		error.add(errorMsg);
		top.add(error);
		
		contentPane.add("North", top);
		
		JPanel butPanel = new JPanel();
		butPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		saveButton = new JButton("Save location");
		saveButton.addActionListener(this);
		butPanel.add(saveButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		butPanel.add(cancelButton);
		
		contentPane.add("South", butPanel);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == saveButton) {
			boolean error = false;
			location = new Location(locationName.getText(), Integer.parseInt(locationCapacity.getText()));
			
			try {
				locationController.manageLocation(location, locationController.NEW);
			}
			catch(IllegalArgumentException exception) {
				errorMsg.setText(exception.toString().substring(exception.toString().indexOf(':') + 1));
				errorMsg.setForeground(Color.red);
				error = true;
			}
			
			if(!error) {
				JOptionPane.showMessageDialog(null, "Location added successfully!");
				setVisible(false);
			}
		}
		else if(e.getSource() == cancelButton) {
			setVisible(false); //you can't see me!
			dispose(); //Destroy the JFrame object
		}
	}
}
