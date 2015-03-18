package hkust.cse.calendar.gui;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.ListSelectionModel;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.user.UserManagement;

import java.awt.BorderLayout;
import java.sql.Timestamp;
import java.util.LinkedList;

public class AvailableTimeListDialog extends JFrame{
	public TimeSpan firstTime;
	private Appt currAppt;
	private ApptStorageControllerImpl _controller;
	private JList list;
	private DefaultListModel listModel;
	private Timestamp newts;
	private Timestamp newts2;
	
	public AvailableTimeListDialog(Appt a, ApptStorageControllerImpl controller, Timestamp ts){
		currAppt = a;
		_controller = controller;
		setTitle("Avalable Time List ( Start time with 15 minutes interval within a day )");

		this.setLayout(new BorderLayout());
		this.setLocationByPlatform(true);
		this.setSize(550,450);
		listModel = new DefaultListModel(); 
		list = new JList(listModel); 
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		list.setSelectedIndex(0); 
		list.setVisibleRowCount(5); 
		JScrollPane listScrollPane = new JScrollPane(list); 
		JPanel buttonPane = new JPanel(); 
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5)); 
		add(listScrollPane, BorderLayout.CENTER); 
		add(buttonPane, BorderLayout.PAGE_END); 
		this.setVisible(true);
		
		Timestamp start_day_time = new Timestamp(ts.getYear(), ts.getMonth(), ts.getDate(), 8, 0, 0, 0);
		Timestamp end_day_time = new Timestamp(ts.getYear(), ts.getMonth(), ts.getDate(), 18, 0, 0, 0);
		newts = new Timestamp(ts.getYear(), ts.getMonth(), ts.getDate(), ts.getHours(), ts.getMinutes(), ts.getSeconds(), 0);
		
		if(newts.before(start_day_time)){
			newts=new Timestamp(ts.getYear(), ts.getMonth(), ts.getDate(), 8, 0, 0, 0);
			newts2=new Timestamp(ts.getYear(), ts.getMonth(), ts.getDate(), 8, 15, 0, 0);
		}
			else
				newts2 = new Timestamp(ts.getYear(), ts.getMonth(), ts.getDate(),ts.getHours(), ts.getMinutes()+15, 0, 0);

		newts=AdjustTS(newts);
		newts2=AdjustTS(newts2);
		LinkedList<TimeSpan> aTime = new LinkedList<TimeSpan>();
		
		while(aTime.size()==0){
				while(newts.equals(end_day_time) == false && newts.before(end_day_time)){
					boolean noTimeConflict = true;	
					boolean noLocationConflict = true;
					TimeSpan tspan = new TimeSpan(newts, newts2);	
					
					UserManagement um = UserManagement.getInstance();			
						for(int i=0; i<currAppt.getWaitingList().size(); i++) {			
							for(int j =0; j<_controller.RetrieveAppts(um.getUser(currAppt.getWaitingList().get(i)), tspan).length; j++)			
								if(_controller.RetrieveAppts(um.getUser(currAppt.getWaitingList().get(i)), tspan)[j].getID()!=currAppt.getID())			
									noTimeConflict = false;			
						}	
						for(int i=0; i<currAppt.getAttendList().size(); i++) {						
							for(int j =0; j<_controller.RetrieveAppts(um.getUser(currAppt.getAttendList().get(i)), tspan).length; j++)			
								if(_controller.RetrieveAppts(um.getUser(currAppt.getAttendList().get(i)), tspan)[j].getID()!=currAppt.getID())			
									noTimeConflict = false;		
						}
			
						Appt[] retriedAppts2 = _controller.RetrieveAppt(currAppt.getLocation(), tspan);
						//System.out.println(retriedAppts2.length);
						//System.out.println(currAppt.getLocation());
						//System.out.println(tspan.StartTime());

						for(int i=0; i<retriedAppts2.length; i++){
							if(retriedAppts2[i].getID()!=currAppt.getID())
								noLocationConflict = false;
							//System.out.println(retriedAppts2.length);
							//System.out.println(currAppt.getLocation());
							//System.out.println(currAppt.TimeSpan().StartTime());
						}
									
						
						if( noTimeConflict && noLocationConflict){			
							aTime.add(tspan);
						}
					newts = new Timestamp(newts.getYear(), newts.getMonth(), newts.getDate(),newts.getHours(), newts.getMinutes()+15, 0, 0);			
					newts2 = new Timestamp(newts2.getYear(), newts2.getMonth(), newts2.getDate(),newts2.getHours(), newts2.getMinutes()+15, 0, 0);			
					
				}
				if (aTime.size()==0){
					newts = new Timestamp(newts.getYear(), newts.getMonth(), newts.getDate()+1,8, 0, 0, 0);			
					newts2 = new Timestamp(newts2.getYear(), newts2.getMonth(), newts2.getDate()+1,8, 15, 0, 0);	
					end_day_time = new Timestamp(end_day_time.getYear(), end_day_time.getMonth(), end_day_time.getDate()+1, 18, 0, 0, 0);
					
				}
		}
		
		for (int i = 0; i < aTime.size(); i++)  
			listModel.addElement(aTime.get(i).StartTime());
		
		firstTime= aTime.getFirst();
		
		}
	
	public Timestamp AdjustTS(Timestamp ts){
		if(ts.getMinutes()%15!=0){
			if(ts.getMinutes()>45){
				ts = new Timestamp(ts.getYear(), ts.getMonth(), ts.getDate(), ts.getHours()+1, 0, 0, 0);
			}
			else {
				ts = new Timestamp(ts.getYear(), ts.getMonth(), ts.getDate(), ts.getHours(), (ts.getMinutes()/15 +1)*15, 0, 0);
			}
		}
		return ts;
	}
}
