package hkust.cse.calendar.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AcceptOrNotDialog extends JFrame implements ActionListener{
	private JPanel groupApptFramePanel;
	private JLabel comment1;
	private JLabel comment2;
	private JButton bAcceptGroupInv;
	private JButton bRejectGroupInv;

	public AcceptOrNotDialog() {
		//"user with id "+tmpMB.getUserToBeDeletedID()etedID()+ "will be deleted. Do you accept?");
		this.setTitle("User with id " + " will be deleted. Do you accept?");
		this.setSize(400, 100);
		this.setAlwaysOnTop(true);
		
		groupApptFramePanel = new JPanel();
		//System.out.println("trying to make group invitation dialogue");
		comment1 = new JLabel("This user is in some event created by you in the future");
		//comment2 = new JLabel("Time: " + invite.TimeSpan().toString());
		
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
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
