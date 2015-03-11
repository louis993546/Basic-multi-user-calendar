package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.SingletonMediator;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.unit.TimeSpan;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class UserSelectDialog extends JFrame implements ActionListener{

	//private User[]  availableUsers;
	private JCheckBox selectUser[];
	private JButton confirm;
	private AppScheduler parent1;
	private CalGrid parent2;
	private Appt thisAppointment;

	
	private String location;
	
	//private ArrayList<String> listModel;
	private String[] listModelArr;
	private JList list;
	
	private TimeSpan[] availableTimes;
	
	public UserSelectDialog(AppScheduler p1, CalGrid p2, Appt appt, String l) {
		this.toFront();
		this.repaint();
		
		this.setLayout(new BorderLayout());
		this.setLocationByPlatform(true);
		this.setSize(400, 203);
		//this.setResizable(true); // do not allow to resize the dialog box

		
		//listModel = new ArrayList<String>();
		listModelArr = new String[6];
		listModelArr[0] = "Choose own time";
		list = new JList(listModelArr);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.setFixedCellHeight(20);
		
		JScrollPane scrollPane = new JScrollPane(list);
		
		location = l;
		parent1 = p1;
		parent2 = p2;
		thisAppointment = appt;
		setTitle("Select Users");
		JPanel contentPane = new JPanel();
		System.out.println(location);
		//availableUsers = new User[LoginDialog.userList.size()-1];
		selectUser = new JCheckBox[SingletonMediator.getBrainInstance().getAllUsers().size()-1];
		
		//String arr[] = new String[LoginDialog.userList.size()];
		Iterator it = SingletonMediator.getBrainInstance().getAllUsers().entrySet().iterator();
		int i=0;
		while(it.hasNext()){
			 Map.Entry tempUser = (Map.Entry)it.next();
			 if(!parent2.mCurrUser.ID().equals(((User) tempUser.getValue()).ID())) {
				 selectUser[i]=new JCheckBox(((User) tempUser.getValue()).ID());
				 selectUser[i].addActionListener(this);
				 i++;
			 }
			 
		}
		confirm = new JButton("Confirm");
		confirm.addActionListener(this);
		
		for(int j=0; j<(SingletonMediator.getBrainInstance().getAllUsers().size()-1); j++) {
			contentPane.add(selectUser[j]);
		}
		//contentPane.add(scrollPane, BorderLayout.CENTER);
		this.add(contentPane, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(confirm, BorderLayout.AFTER_LAST_LINE);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		ArrayList<String> usersSelected = new ArrayList<String>();

		if(e.getID() == 1001){
			int tempUsersNum = 0;
			Location tempCheck = hkust.cse.calendar.unit.SingletonMediator.getBrainInstance().getLocationFromString(parent1.locField.getSelectedItem().toString());
		
			for(int selIndex=0; selIndex<selectUser.length; selIndex++){
												
					if(selectUser[selIndex].isSelected()) {
						tempUsersNum++;
					
						if((tempUsersNum + 1)<=tempCheck.getCapacity()) {
							usersSelected.add(selectUser[selIndex].getText());
							
						} else {
							//selectUsersNum = 0;
							JOptionPane.showMessageDialog(this, "Number of users is too big");
							selectUser[selIndex].setSelected(false);
							//list.remove();
							thisAppointment.removeWaiting(selectUser[selIndex].getText());
							//selectUsersNum = usersSelected.size();
							break;
						}
						//System.out.println(usersSelected.get(j));
					}
				System.out.println("********" + " " + usersSelected.size());
		
		
				}	
		}
		
		
		
		
		
		if(e.getSource() == confirm) {
			
			for(int i=0; i<selectUser.length; i++) {
				if(selectUser[i].isSelected()) {
						thisAppointment.addWaiting(selectUser[i].getText());	
						//System.out.println(list.getSelectedValue().toString());
						try {
							String year = list.getSelectedValue().toString().split("-")[0];
							String month = list.getSelectedValue().toString().split("-")[1];
							String day = list.getSelectedValue().toString().split("-")[2].split(" ")[0];
							if(day.split("")[0].equals("0")) {
								day = day.split("")[1];
							}
							String hour1 = list.getSelectedValue().toString().split(" ")[1].split(":")[0];
							if(hour1.split("")[0].equals("0")) {
								hour1 = hour1.split("")[1];
							}
							String minutes1 = list.getSelectedValue().toString().split(" ")[1].split(":")[1];
							if(minutes1.split("")[0].equals("0")) {
								minutes1 = minutes1.split("")[1];
							}
							String hour2 = list.getSelectedValue().toString().split(" ")[2].split(":")[0];
							if(hour2.split("")[0].equals("0")) {
								hour2 = hour2.split("")[1];
							}
							String minutes2 = list.getSelectedValue().toString().split(" ")[2].split(":")[1];
							if(minutes2.split("")[0].equals("0")) {
								minutes2 = minutes2.split("")[1];
							}
							
							System.out.println(year);
							System.out.println(month);
							System.out.println(day);
							System.out.println(hour1);
							System.out.println(minutes1);
							System.out.println(hour2);
							System.out.println(minutes2);
							
							parent1.setNewApptTimeSpan(year, month, day, hour1, minutes1, hour2, minutes2);
						} catch (Exception e1) {
							parent1.setNewApptTimeSpan("", "", "", "", "", "", "");
						}
					}
					
				
			}
			this.setVisible(false);
			this.dispose();
		}

				availableTimes = SingletonMediator.getBrainInstance().checkGroupTimeSpans(usersSelected, location);
				if(availableTimes!=null) {
					//int sizeOfTimes = availableTimes.length;
					for(int j = 1; j < 6; j++){
						listModelArr[j] = availableTimes[j-1].toString();
					}
				} else {
					for(int j=1; j<6; j++) {
						listModelArr[j] = "";
					}
				}
				
				this.repaint();
			}
		}
	
	

