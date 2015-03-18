package hkust.cse.calendar.gui;

//package hkust.cse.calendar.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.SingletonMediator;
import hkust.cse.calendar.unit.SingletonMediator.NotifyInitiator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.TableModel;

public class DeleteUserConfirm extends JFrame implements ActionListener{

	private JPanel groupApptFramePanel;
	private JButton bAcceptDeletion;
	private JButton bRejectDeletion;
	
	private JLabel comment1;
	private JLabel comment2;
	
	private NotifyInitiator notifyIn;
	//private NotifyInitiator location;
	
	CalGrid parent;
	
	public DeleteUserConfirm(CalGrid cal, NotifyInitiator n) {
		parent = cal;
		

		this.setSize(400, 100);
		this.setAlwaysOnTop(true);
		
		notifyIn = n;
		if(notifyIn.deleteLocation==null) {
			this.setTitle("Delete " + notifyIn.deleteUser);
			comment1 = new JLabel("Admin is deleting " + notifyIn.deleteUser + ".");
			comment2 = new JLabel("Do you confirm this deletion?");
		} else {
			this.setTitle("Delete " + notifyIn.deleteLocation);
			comment1 = new JLabel("Admin is deleting " + notifyIn.deleteLocation + ".");
			comment2 = new JLabel("Do you confirm this deletion?");
		}
		
		
		groupApptFramePanel = new JPanel();
		System.out.println("trying to make group invitation dialogue");
		
		
		bAcceptDeletion = new JButton("Accept");
		bAcceptDeletion.addActionListener(this);
		
		bRejectDeletion = new JButton("Reject");
		bRejectDeletion.addActionListener(this);
		
		groupApptFramePanel.add(comment1);
		groupApptFramePanel.add(comment2);
		groupApptFramePanel.add(bAcceptDeletion);
		groupApptFramePanel.add(bRejectDeletion);
		
		this.add(groupApptFramePanel);
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==bAcceptDeletion) {
			notifyIn.confirmed = 1;
			if(notifyIn.deleteLocation!=null) {
				SingletonMediator.getBrainInstance().checkDeleteLocation();
			}else {
				SingletonMediator.getBrainInstance().checkDeleteUser();
			}
			//a.show();
			/*TableModel t = parent.prepareTableModel();
			parent.tableView.setModel(t);
			parent.tableView.repaint();*/
			this.setVisible(false);
			this.dispose();
		} else if(e.getSource()==bRejectDeletion) {
			notifyIn.confirmed = 0;
			if(notifyIn.deleteLocation!=null) {
				SingletonMediator.getBrainInstance().checkDeleteLocation();
			}else {
				SingletonMediator.getBrainInstance().checkDeleteUser();
			}
			this.setVisible(false);
			this.dispose();
		}
	}
}