package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.UserDB;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
	 * | |            |   	  |			    | |
	 * | |            |   	  |			    | |
	 * | |            | |---| |			    | |
	 * | |            | | > | |			    | |
	 * | |            | |---| |			    | |
	 * | |            |       |			    | |
	 * | |            |       |			    | |
	 * | |            |   	  |			    | |
	 * | |            |   	  |			    | |
	 * | |------------|    	  |-------------| |
	 * |    								  |
	 * |    			    	     |------| |
	 * |    					     |      | |
	 * |    			    	     |------| |
	 * |--------------------------------------|
	 */
	
	private UserDB udb;
	private ArrayList<String> UserStringAL = new ArrayList<String>();
	private DefaultListModel<String> UserListModel = new DefaultListModel<String>();
	private DefaultListModel<String> InvitingListModel = new DefaultListModel<String>();
	private JList<String> UserList;
	private JList<String> InvitingList;
	private JButton confirmButton;
	private JButton cancelButton;
	private JButton addButton;
	private AppScheduler parent;
	
	public InviteDialog(AppScheduler p)
	{
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
		
		JPanel listPanel2 = new JPanel();
		Border lbp2 = new TitledBorder(null, "Inviting:");
		listPanel2.setBorder(lbp2);
		InvitingList = new JList<String>(InvitingListModel);
		listPanel2.add(InvitingList);
		Box right = Box.createVerticalBox();
		right.add(listPanel2);
		
		Box top = Box.createHorizontalBox();
		top.add(left);
		top.add(middle);
		top.add(right);
		
		Box bottom = Box.createHorizontalBox();
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(this);
		bottom.add(cancelButton);
		bottom.add(confirmButton);
		
		all.add(top);
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
			//TODO store the list to parent.(some where)
			//turn InvitingList into an array of uids
			ArrayList<Integer> InvitingListUID = new ArrayList<Integer>();
			int ilmSize = InvitingListModel.getSize();
			for (int i = 0; i<ilmSize; i++)
			{
				parent.addToInvitingList(udb.getUserUID(InvitingListModel.get(i).toString()));
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
			//TODO move things from UserList to InvitingList
			InvitingListModel.addElement(UserList.getSelectedValue().toString());
			UserListModel.removeElementAt(UserList.getSelectedIndex());
			this.pack();
		}
		
	}
	
}
