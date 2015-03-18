package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserStorageController;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class UserApptManager extends JFrame implements ActionListener, ListSelectionListener {
	private static UserApptManager instance = null;
	
	private CalGrid calGrid;
	
	private UserStorageController userController;
	private JList<User> userList;
	private DefaultListModel<User> userListModel;
	private JScrollPane userScrollPane;
	
	private ApptStorageControllerImpl apptController;
	private JList<Appt> apptList;
	private DefaultListModel<Appt> apptListModel;
	private JScrollPane apptScrollPane;
	
	//private User selectedUser = null;
	public static UserApptManager getInstance() {
		return instance;
	}
	
	public static void updateApptList() {
		UserApptManager.instance.updateApptList(instance.userList.getSelectedValue());
	}
	
	public UserApptManager(CalGrid calGrid, ApptStorageControllerImpl apptController) {
		UserApptManager.instance = this;
		
		userController = UserStorageController.getInstance();
		this.apptController = apptController;
		this.calGrid = calGrid;
		
		/* Act like a destructor */
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				UserApptManager.instance = null;
			}
		});
		
		setTitle("User Appointment Manager");
		
		Container contentPane;
		contentPane = getContentPane();
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		
		JPanel userPanel = new JPanel();
		Border userBorder = new TitledBorder("Users");
		userPanel.setBorder(userBorder);
		
		userListModel = new DefaultListModel<User>();
		userList = new JList<User>(userListModel);
		userList.setListData(userController.retrieveUsers());
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.addListSelectionListener(this);
		userScrollPane = new JScrollPane(userList , JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		userScrollPane.setPreferredSize(new Dimension(350, 500));
		userPanel.add(userScrollPane);
		
		top.add("West", userPanel);
		
		JPanel apptPanel = new JPanel();
		Border apptBorder = new TitledBorder("Appointments");
		apptPanel.setBorder(apptBorder);
		
		apptListModel = new DefaultListModel<Appt>();
		apptList = new JList<Appt>(apptListModel);
		apptList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		apptList.addListSelectionListener(this);
		apptList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JList<Appt> list = (JList<Appt>) e.getSource();
				if(e.getClickCount() == 2) {
					Appt appt = list.getSelectedValue();
					AppScheduler setAppDial = new AppScheduler("Modify", UserApptManager.this.calGrid, appt.getID());
					setAppDial.updateSetApp(appt);
					setAppDial.show();
					setAppDial.setResizable(false);
				}
			}
		});
		apptScrollPane = new JScrollPane(apptList , JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		apptScrollPane.setPreferredSize(new Dimension(350, 500));
		apptPanel.add(apptScrollPane);
		
		top.add("East", apptPanel);
		
		contentPane.add("North", top);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == userList) {
			User selectedUser = userList.getSelectedValue();
			updateApptList(selectedUser);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
	
	public void updateApptList(User user) {
		Appt[] appts = apptController.retrieveAllAppts(user);
		if(appts == null) {
			apptList.setListData(new Appt[0]);
		}
		else {
			apptList.setListData(appts);
		}
	}
}
