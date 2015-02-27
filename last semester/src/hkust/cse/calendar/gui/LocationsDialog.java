package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.apptstorage.ApptStorageMemImpl;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LocationsDialog extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private ApptStorageControllerImpl _controller;
	
	private DefaultListModel<Object> listModel;
	private JList<Object> list;
	private JTextField locNameText;
	private JButton _add;
	private JButton _delete;
	
	public LocationsDialog(ApptStorageControllerImpl controller){
		_controller =controller;
		this.setLayout(new BorderLayout());
		this.setLocationByPlatform(true);
		this.setSize(300,200);
		
		listModel = new DefaultListModel<Object>();
		initialize();
		
		list = new JList<Object>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.add(new JScrollPane(list),BorderLayout.NORTH);
		
		locNameText= new JTextField(10);
		this.add(locNameText,BorderLayout.WEST);
		
		_add = new JButton("add");
		this.add(_add,BorderLayout.CENTER);
		_add.addActionListener(new add_Action());
		
		_delete = new JButton("delete");
		this.add(_delete,BorderLayout.EAST);
		_delete.addActionListener(new delete_Action());
		
		list.setSelectedIndex(0);
		list.addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				
				
				}
			}
		);
	}
	
	private void initialize() {
		Location[] locations = _controller.getLocationList();
		if(locations == null)
			locations = new Location[0];
		
		int size = locations.length;
		for(int i = 0;i<size;i++){
			listModel.addElement(_controller.getLocationList()[i].getName());;
		}
	}

	public class add_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			String temps = locNameText.getText().trim();
			if (!temps.equals("")&& !listModel.contains(temps)){
				listModel.addElement(temps);
				locNameText.setText("");
				locNameText.grabFocus();
				UpdateLocationList();
			} else {
				locNameText.grabFocus();
			}
		}
	}
	
	public class delete_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (listModel.getSize() != 0){
				if(list.getSelectedIndex() != -1){
					listModel.removeElementAt(list.getSelectedIndex());
					locNameText.grabFocus();
					UpdateLocationList();
				}
			}
		}
	}
	
	public void UpdateLocationList() {
		int length = listModel.size();
		Location[] temp = new Location[length];
		for(int i = 0;i<length;i++){
			temp[i] = new Location(listModel.elementAt(i).toString());
			temp[i].setName(listModel.elementAt(i).toString());
		}
		_controller.setLocationList(temp);
		_controller.SaveLocationList(temp);
		//check getlocationlist validity
//		for(int i = 0;i<length;i++){
//			listModel.addElement(_controller.getLocationList()[i]);;
//		}
	}
}