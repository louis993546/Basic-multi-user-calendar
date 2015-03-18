package hkust.cse.calendar.gui;

import javax.swing.DefaultListModel; 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import hkust.cse.calendar.unit.Location;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import hkust.cse.calendar.apptstorage.*;
import hkust.cse.calendar.unit.*;


public class LocationsDialog extends JFrame implements ActionListener {
	

	/*this variable is for the default user*/
	private static final long serialVersionUID = 1L;
	
	/*to manipulate the GUI*/
	private ApptStorageControllerImpl _controller;

	private DefaultListModel<Location> listModel;
	private JList<Location> list;
	private JTextField locNameText;
	
	/*added by me*/
	private JButton btnAddLocation, btnRemoveLocation;
	//private JScrollPane scroll;
	/*up to here*/
	
	public LocationsDialog(ApptStorageControllerImpl controller){
		_controller = controller;
		
		/*added by me*/
		setTitle("Location Dialog"); // set the name for the dialog
		JPanel contentPane = new JPanel();
		
		locNameText = new JTextField(20); // 20 characters title of a location
		locNameText.setText("");
			
		
		btnAddLocation = new JButton("Add");
		btnAddLocation.addActionListener(this); // to make the Add button respond to actions
		btnRemoveLocation = new JButton("Remove");
		btnRemoveLocation.addActionListener(this); // to make the Remove button respond to actions
		
		
		/*up to here*/
		
		
		this.setLayout(new BorderLayout());
		this.setLocationByPlatform(true);
		this.setSize(400, 210);
		this.setResizable(false); // do not allow to resize the dialog box
		
		listModel = new DefaultListModel<Location>();
		/*initialise the listModel if controller is not null*/
		//System.out.println(controller.getLocationList());
		if(controller.getLocationList() != null){
			//System.out.println(controller.getLocationList().length);
			int sizeOfController = controller.getLocationList().length;
			/*loop for deep copying*/
			for(int i = 0; i < sizeOfController; i++){
				listModel.add(i,_controller.getLocationList()[i]);
			}
		}
		
		//Location tempArray[] = new Location[sizeOfController]; 
		//tempArray = _controller.getLocationList(); // copy temporary
		/*loop for deep copying*/
		/*for(int i = 0; i < sizeOfController; i++){
			listModel.add(i,_controller.getLocationList()[i]);
		}*/
		
		
		list = new JList<Location>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		
		/*the size of one row, it makes the JList visible*/
		list.setVisibleRowCount(7);
		list.setFixedCellHeight(20);
		list.setFixedCellWidth(200);
		
//		list.addListSelectionListener(new ListSelectionListener(){
/*
		/*output the names of the locations, not the objects themselves */
/*		@Override
		public void valueChanged(ListSelectionEvent e) {
			// TODO Auto-generated method stub
			 
		}
		
	});*/
		
		/*this panel is used for the list*/
		JScrollPane scrollPane = new JScrollPane(list);
		this.add(scrollPane, BorderLayout.NORTH);
		/*added by me*/
		
		/*the panel is only for the buttons and  the text field*/
		contentPane.add(locNameText);
		contentPane.add(btnAddLocation);
		contentPane.add(btnRemoveLocation);
		this.add(contentPane,BorderLayout.AFTER_LAST_LINE);
		setVisible(true);
		/*up to here*/
		

	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnAddLocation){ // if the Add button gets pressed
			boolean lockEmpty = false;
			int sizeOfText = locNameText.getText().length();
			/*checks whether the locaNameText field is not empty*/
			for(int i = 0; i < sizeOfText; i++){
				if(locNameText.getText().charAt(i) != ' '){
					lockEmpty = true;
				}
			}
			if(lockEmpty){ // if the text field is not empty, create a location object
						/*if a location with the same name already exists in the list, do not add it*/
						int sizeOfList = listModel.getSize();
						boolean addAvailable = true; // to keep track of availability of adding the new location
						for(int j = 0; j < sizeOfList; j++){
							if(listModel.getElementAt(j).getName().equals(locNameText.getText())){
								addAvailable = false;
								break;
							}
						}
						if(addAvailable){
							listModel.addElement(new Location(locNameText.getText())); // create a location object and add to the listModel
							/*temporary array is used to copy all the new locations to the _controller array*/
							Location tempArray[] = new Location [listModel.size()];
							listModel.copyInto(tempArray); // deep copying
							_controller.setLocationList(tempArray); // reassign the values of the location array
						}
			}
		
		}
		else{ if(e.getSource() == btnRemoveLocation){
				if(listModel.getSize() > 0 ){
					int delIndex = list.getSelectedIndex();
					if(delIndex >= 0){
						listModel.remove(delIndex); // remove an item from the DefaultListModel
						/*temporary array is used to copy all the new locations to the _controller array*/
						Location tempArray[] = new Location [listModel.size()];
						listModel.copyInto(tempArray); // deep copying
						_controller.setLocationList(tempArray); // reassign the values of the location array
					}
						
					
				}
			  }
			
		}
	}
}
