package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.xmlfactory.ApptXmlFactory;

import java.awt.BorderLayout;
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
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ReplyJointApptPanel extends JDialog implements ActionListener, ListSelectionListener {
	private JButton acceptButton, rejectButton, detailButton, closeButton;
	private JList<Appt> apptList;
	private DefaultListModel<Appt> apptListModel; 
	private JScrollPane apptScrollPane;
	private CalGrid parent;
	
	public ReplyJointApptPanel(CalGrid parent) {
		this.parent = parent;
		
		setTitle("Reply to Joint Appointment");
		
		Container contentPane;
		contentPane = getContentPane();
		
		apptListModel = new DefaultListModel<Appt>();
		apptList = new JList<Appt>(apptListModel);
		apptList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		apptList.addListSelectionListener(this);
		
		JPanel upper = new JPanel();
		upper.setLayout(new BorderLayout());
		apptScrollPane = new JScrollPane(apptList);
		upper.add(apptScrollPane);
		contentPane.add("North", upper);
		
		JPanel bottom = new JPanel();
		
		acceptButton = new JButton("Accept");
		acceptButton.addActionListener(this);
		bottom.add(acceptButton);
		
		rejectButton = new JButton("Reject");
		rejectButton.addActionListener(this);
		bottom.add(rejectButton);
		
		detailButton = new JButton("Detail");
		detailButton.addActionListener(this);
		bottom.add(detailButton);
		
		closeButton = new JButton("Close");
		closeButton.addActionListener(this);
		bottom.add(closeButton);
		
		contentPane.add("South", bottom);
		
		setApptList();
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == closeButton) {
			setVisible(false);
			dispose();
		}
		else if(checkApptSelected()){
			Appt selectedAppt = apptList.getSelectedValue();
			
			if(e.getSource() == acceptButton) {
				if (JOptionPane.showConfirmDialog(this, "Accept this joint appointment?", "Confirmation", JOptionPane.YES_NO_OPTION) == 0){
					selectedAppt.addAttendant(this.parent.getCurrUser());
					selectedAppt.getRejectList().remove(this.parent.getCurrUser().ID());
					selectedAppt.getWaitingList().remove(this.parent.getCurrUser().ID());
					
					ApptXmlFactory apptxmlfactory = new ApptXmlFactory();
					Appt[] appts = this.parent.controller.RetrieveAppts(selectedAppt.getOwner(), selectedAppt.getJoinID());
					for(Appt appt : appts){
						apptxmlfactory.removeApptFromXml("appt.xml", appt, selectedAppt.getOwner().ID());
						apptxmlfactory.saveApptToXml("appt.xml", appt, selectedAppt.getOwner().ID());					
					}
				}
			}
			else if(e.getSource() == rejectButton) {
				if (JOptionPane.showConfirmDialog(this, "Reject this joint appointment?", "Confirmation", JOptionPane.YES_NO_OPTION) == 0){				
					selectedAppt.addReject(this.parent.getCurrUser());
					selectedAppt.getAttendList().remove(this.parent.getCurrUser().ID());
					selectedAppt.getWaitingList().remove(this.parent.getCurrUser().ID());
					
					ApptXmlFactory apptxmlfactory = new ApptXmlFactory();
					Appt[] appts = this.parent.controller.RetrieveAppts(selectedAppt.getOwner(), selectedAppt.getJoinID());
					for(Appt appt : appts){
						apptxmlfactory.removeApptFromXml("appt.xml", appt, selectedAppt.getOwner().ID());
						apptxmlfactory.saveApptToXml("appt.xml", appt, selectedAppt.getOwner().ID());
					}
				}
			}
			else if(e.getSource() == detailButton) {
				DetailsDialog detailPanel = new DetailsDialog(selectedAppt, selectedAppt.toString());
				detailPanel.setVisible(true);
			}
			
			setApptList();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

	private void setApptList() {
		Appt[] appts = getJointAppt();
		if(appts == null) {
			apptList.setListData(new Appt[0]);
		}
		else {
			apptList.setListData(appts);
		}
	}
	
	private Appt[] getJointAppt() {
		Appt[] JointApptsInWaitList = this.parent.controller.RetrieveJointApptsInWaitlist();
		return JointApptsInWaitList.length == 0 ? null : JointApptsInWaitList;
	}
	
	private boolean checkApptSelected() {
		if(apptList.getSelectedValue() != null) {
			return true;
		}
		else {
			JOptionPane.showMessageDialog(null, "No Appointment is selected", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
}
