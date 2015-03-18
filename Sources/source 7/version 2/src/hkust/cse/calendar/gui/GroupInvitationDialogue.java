package hkust.cse.calendar.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.SingletonMediator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.TableModel;

public class GroupInvitationDialogue extends JFrame implements ActionListener{

	private JPanel groupApptFramePanel;
	private JButton bAcceptGroupInv;
	private JButton bRejectGroupInv;
	
	private JLabel comment1;
	private JLabel comment2;
	
	private Appt invitation;
	
	CalGrid parent;
	
	public GroupInvitationDialogue(CalGrid cal, Appt invite) {
		parent = cal;
		invitation = invite;
		
		this.setTitle("Group Invitation from" + invite.getUserName());
		this.setSize(400, 100);
		this.setAlwaysOnTop(true);
		
		groupApptFramePanel = new JPanel();
		System.out.println("trying to make group invitation dialogue");
		comment1 = new JLabel("You have received an invitation from " + invite.getUserName());
		comment2 = new JLabel("Time: " + invite.TimeSpan().toString());
		
		bAcceptGroupInv = new JButton("Accept");
		bAcceptGroupInv.addActionListener(this);
		
		bRejectGroupInv = new JButton("Reject");
		bRejectGroupInv.addActionListener(this);
		
		groupApptFramePanel.add(comment1);
		groupApptFramePanel.add(comment2);
		groupApptFramePanel.add(bAcceptGroupInv);
		groupApptFramePanel.add(bRejectGroupInv);
		
		this.add(groupApptFramePanel);
		pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==bAcceptGroupInv) {
			invitation.addAttendant(SingletonMediator.getBrainInstance().getThisUserName());
			SingletonMediator.getBrainInstance().confirmGroup(invitation);
			//a.show();
			TableModel t = parent.prepareTableModel();
			parent.tableView.setModel(t);
			parent.tableView.repaint();
			this.setVisible(false);
			this.dispose();
		} else if(e.getSource()==bRejectGroupInv) {
			SingletonMediator.getBrainInstance().removeAppt(invitation);
			this.setVisible(false);
			this.dispose();
		}	
	}
}
