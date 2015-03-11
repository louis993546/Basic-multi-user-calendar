package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.LocationDB;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class ModifyLocationDialog extends JFrame implements ActionListener{
	private JList locationList;
	private DefaultListModel locationListModel = new DefaultListModel();
	private JButton deleteButton;
	private JButton modifyButton;
	private JButton exitButton;
	private LocationDB ldb;
	private String[] testStringArray = {"test1", "test2"};
	
	public ModifyLocationDialog() {
		
		setTitle("Modify Location");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		Container contentPane;
		contentPane = getContentPane();

		//create a new JPanel to hold everything
		JPanel all = new JPanel();
		all.setLayout(new BoxLayout(all, BoxLayout.X_AXIS));
		
		Box left = Box.createVerticalBox();
		for (String a:testStringArray) {
			locationListModel.addElement(a);
		}
		locationList = new JList(locationListModel);
		left.add(locationList);
//		JTextField test = new JTextField("Testing");
//		left.add(test);
		
		Box right = Box.createVerticalBox();
		modifyButton = new JButton("Modify");
		deleteButton = new JButton("Delete");
		exitButton = new JButton("Exit");
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

	public ModifyLocationDialog(LocationDB ldb2) {
		this();
		ldb = ldb2;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
