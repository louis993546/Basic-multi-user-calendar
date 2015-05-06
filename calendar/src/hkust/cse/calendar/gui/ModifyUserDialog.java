
package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.UserDB;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class ModifyUserDialog extends JFrame implements ActionListener {
	private JList<String> UserList;
	private DefaultListModel<String> UserListModel = new DefaultListModel<String>();
	private JButton deleteButton;
	private JButton modifyButton;
	private JButton exitButton;
	private UserDB udb;
	private ArrayList<String> UserStringAL = new ArrayList<String>();

	public ModifyUserDialog() {

		setTitle("Modify User");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		udb = new UserDB();
		Container contentPane;
		contentPane = getContentPane();
		UserStringAL = udb.getUserList();

		// create a new JPanel to hold everything
		JPanel all = new JPanel();
		all.setLayout(new BoxLayout(all, BoxLayout.X_AXIS));

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

		Box right = Box.createVerticalBox();
		modifyButton = new JButton("Modify");
		modifyButton.addActionListener(this);
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(this);
		exitButton = new JButton("Exit");
		exitButton.addActionListener(this);
		right.add(modifyButton);
		right.add(deleteButton);
		right.add(exitButton);

		all.add(left);
		all.add(right);

		contentPane.add("North", all);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public ModifyUserDialog(UserDB udb2) {
		this();
		udb = udb2;
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == deleteButton) 
		{
			int id = udb.getUserUID(UserList.getSelectedValue().toString());
			if ((id != 0) || (id != -1))
			{
				udb.deleteUser(id);
				UserListModel.removeElementAt(UserList.getSelectedIndex());
			}
		} else if (e.getSource() == modifyButton) 
		{
			int uid = udb.getUserUID(UserList.getSelectedValue()
					.toString());
			if ((uid != 0) || (uid != -1))
			{
				//udb.deleteUser(uid);
				UpdateAccountInfoDialog mld = new UpdateAccountInfoDialog(uid);
				dispose();
			}

		} else if (e.getSource() == exitButton) 
		{
			dispose();
		} else 
		{
			System.out.println("Something's wrong");
		}
	}
}
