package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptDB;
import hkust.cse.calendar.apptstorage.UserDB;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeInterval;
import hkust.cse.calendar.unit.TimeSpan;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class InviteDialog extends JFrame implements ActionListener
{
	/*
	 * |--------------------------------------|
	 * |    								  |
	 * | |------------|       |-------------| |
	 * | |            |   	  |			    | |
	 * | |            |   	  |			    | |
	 * | |            | |---| |			    | |
	 * | |            | | > | |			    | |
	 * | |            | |---| |			    | |
	 * | |  		  |		  |			    | |
	 * | |            | |---| |			    | |
	 * | |            | | < | |			    | |
	 * | |            | |---| |			    | |
	 * | |            |       |			    | |
	 * | |            |   	  |			    | |
	 * | |            |   	  |			    | |
	 * | |------------|    	  |-------------| |
	 * |    								  |
	 * | |------------|   					  |
	 * | |  Calculate |				TF        |
	 * | |------------|						  |
	 * |    								  |
	 * |    								  |
	 * |    								  |
	 * |    				Common time		  |
	 * |    								  |
	 * |    			    	     |------| |
	 * |    					     |      | |
	 * |    			    	     |------| |
	 * |--------------------------------------|
	 */
	
	private UserDB udb;
	private ArrayList<String> UserStringAL = new ArrayList<String>();
	private ArrayList<String> InvitingStringAL = new ArrayList<String>();
	private DefaultListModel<String> UserListModel = new DefaultListModel<String>();
	private DefaultListModel<String> InvitingListModel = new DefaultListModel<String>();
	private JList<String> UserList;
	private JList<String> InvitingList;
	private JButton confirmButton;
	private JButton cofirmWithSelectedTimeButton;
	private JButton cancelButton;
	private JButton addButton;
	private JButton removeButton;
	private JButton calculateButton;
	private JTextField timeSpanTF;
	private JTextArea commonTimeTA;
	private AppScheduler parent;
	private ApptDB adb;
	
	public InviteDialog(AppScheduler p)
	{
		adb = new ApptDB();
		this.parent = p;
		this.setSize(600, 300);
		
		setTitle("Invite other people");
		addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e) 
			{
				dispose();
			}
		});
		udb = new UserDB();
		Container contentPane;
		contentPane = getContentPane();
		UserStringAL = udb.getUserList();
		UserStringAL.remove(parent.getCurrentUserEmail());
		
		InvitingStringAL = parent.getInvitingAL();
		
		for (String s:InvitingStringAL)
		{
			UserStringAL.remove(s);
		}

		// create a new JPanel to hold everything
		JPanel all = new JPanel();
		all.setLayout(new BoxLayout(all, BoxLayout.Y_AXIS));

		JPanel listPanel = new JPanel();
		Border lpb = new TitledBorder(null, "Users:");
		listPanel.setBorder(lpb);
		Box left = Box.createVerticalBox();
		for (String a : UserStringAL) {
			UserListModel.addElement(a);
		}
		UserList = new JList<String>(UserListModel);
		listPanel.add(UserList);
		left.add(listPanel);
		
		Box middle = Box.createVerticalBox();
		addButton = new JButton(">");
		addButton.addActionListener(this);
		middle.add(addButton);
		
		removeButton = new JButton("<");
		removeButton.addActionListener(this);
		middle.add(removeButton);
		
		JPanel listPanel2 = new JPanel();
		Border lbp2 = new TitledBorder(null, "Inviting:");
		listPanel2.setBorder(lbp2);
		for (String a:InvitingStringAL)
		{
			InvitingListModel.addElement(a);
		}
		InvitingList = new JList<String>(InvitingListModel);
		listPanel2.add(InvitingList);
		Box right = Box.createVerticalBox();
		right.add(listPanel2);
		
		Box top = Box.createHorizontalBox();
		top.add(left);
		top.add(middle);
		top.add(right);
		
		Box center = Box.createVerticalBox();
		Box centerA = Box.createHorizontalBox();
		Box centerB = Box.createHorizontalBox();
		timeSpanTF = new JTextField(5);
		commonTimeTA = new JTextArea(3,30);
		calculateButton = new JButton("Calculate common time slot");
		calculateButton.addActionListener(this);
		JLabel minLabel = new JLabel("Duration of your appointment(minutes): ");
		centerA.add(minLabel);
		centerA.add(timeSpanTF);
		centerA.add(calculateButton);
		centerB.add(commonTimeTA);
		center.add(centerA);
		center.add(centerB);
		
		Box bottom = Box.createHorizontalBox();
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		cofirmWithSelectedTimeButton = new JButton("Confirm with the selected time slot");
		cofirmWithSelectedTimeButton.addActionListener(this);
		confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(this);
		bottom.add(cancelButton);
		bottom.add(confirmButton);
		
		all.add(top);
		all.add(center);
		all.add(bottom);

		contentPane.add("North", all);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == confirmButton)
		{
			int ilmSize = InvitingListModel.getSize();
			parent.resetInvitingList();
			for (int i = 0; i<ilmSize; i++)
			{
//				System.out.println("Checking: " + InvitingListModel.get(i).toString());
				boolean success = parent.addToInvitingList(udb.getUserUID(InvitingListModel.get(i).toString()));
				if (success == false)
				{
					JOptionPane.showMessageDialog(this, "You have already add this user.");
				}
			}
			JOptionPane.showMessageDialog(this, "Please continue creating appointment.");
			setVisible(false);
			dispose();
		}
		else if (e.getSource() == cancelButton)
		{
			if (JOptionPane.showConfirmDialog(this, "Dispost all data?", "Confirmation", JOptionPane.YES_NO_OPTION) == 0)
			{
				setVisible(false);
				dispose();
			}
		}
		else if (e.getSource() == addButton)
		{
			InvitingListModel.addElement(UserList.getSelectedValue().toString());
			UserListModel.removeElementAt(UserList.getSelectedIndex());
			this.pack();
		}
		else if (e.getSource() == removeButton)
		{
			System.out.println(InvitingList.getSelectedValue().toString());
			UserListModel.addElement(InvitingList.getSelectedValue().toString());
			InvitingListModel.removeElementAt(InvitingList.getSelectedIndex());
			this.pack();
		}
		else if (e.getSource() == calculateButton)
		{
			//TODO construct timeinterval for those in the list
			Timestamp startTS = parent.getCurrentTime();
			Timestamp endTS = new Timestamp(0);
			endTS.setYear(2099-1900);
			endTS.setMonth(11);
			endTS.setDate(31);
			endTS.setMinutes(59);
			endTS.setSeconds(59);
			TimeSpan newTS = new TimeSpan(startTS, endTS);
			
			//get TimeInterval of creator
			
			Appt[] cretorAA = adb.getFutureApptWithUser(parent.getCurrentUserUID());
			System.out.println("parent.getCurrentUserUID(): " + parent.getCurrentUserUID());
//			System.out.println("adb.getFutureApptWithUser(parent.getCurrentUserUID()): " + adb.getFutureApptWithUser(parent.getCurrentUserUID()));
			
			TimeInterval ti = new TimeInterval();
			
			System.out.println("cretorAA.length: " + cretorAA.length);
			if (cretorAA.length > 0)
			{
				ti.setTimeInterval(cretorAA);
			}
				
			int ilmSize = InvitingListModel.getSize();
			System.out.println("ilmSize: " + ilmSize);
			for (int i = 0; i < ilmSize; i++)
			{
				if (adb.getFutureApptWithUser(udb.getUserUID(InvitingListModel.get(i).toString())).length > 0)
				{
					Appt[] userAA = adb.getFutureApptWithUser(udb.getUserUID(InvitingListModel.get(i).toString()));
					ti.setTimeInterval(userAA);
				}
			}
			System.out.println(ti);
		}
		else if (e.getSource() == cofirmWithSelectedTimeButton)
		{
			//TODO
			
		}
		else
		{
			System.out.println("Something wrong in actionPerformed of InviteDialog.");
		}
	}
	
}
