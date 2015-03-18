package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.apptstorage.ApptStorageMemImpl;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Save;
import hkust.cse.calendar.unit.Userlist;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class InviteDialog extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private ApptStorageControllerImpl _controller;
	
	private DefaultListModel<Object> listModel;
	private JList<Object> list;
	private JTextField userNameText;
	private JButton _add;
	private JButton _delete;
	public LinkedList<String> userlist;
	public AppScheduler parent;
	
	public InviteDialog(String title, ApptStorageControllerImpl controller){
		
		setTitle(title);
		
		if(this.getTitle().equals("invite")){
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					parent.setinvitedUserlist(userlist);
					parent.setVisible(true);
					
				}
			});
		}
		
		_controller =controller;
		this.setLayout(new BorderLayout());
		this.setLocationByPlatform(true);
		this.setSize(300,200);
		this.setName("Invite users");
		
		listModel = new DefaultListModel<Object>();
		initialize();
		
		list = new JList<Object>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.add(new JScrollPane(list),BorderLayout.NORTH);
		
		userNameText= new JTextField(10);
		this.add(userNameText,BorderLayout.WEST);
		
		_add = new JButton("add");
		this.add(_add,BorderLayout.CENTER);
		_add.addActionListener(new add_Action());
		
		_delete = new JButton("delete");
		this.add(_delete,BorderLayout.EAST);
		_delete.addActionListener(new delete_Action());
		
		list.setSelectedIndex(0);
		list.addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				
				
				}
			}
		);
	}
	
	private void initialize() {
		//UpdateInvitedUserList();
		int length = listModel.size();
		LinkedList<String> temp = new LinkedList<String>();
		for(int i = 0;i<length;i++){
			temp.push(new String(listModel.elementAt(i).toString()));
		}
	}

	public class add_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			addButtonResponse();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}
	
	public class delete_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (listModel.getSize() != 0){
				if(list.getSelectedIndex() != -1){
					listModel.removeElementAt(list.getSelectedIndex());
					userNameText.grabFocus();
					UpdateInvitedUserList();
				}
			}
		}
	}
	
	private void addButtonResponse(){
		String temps = userNameText.getText().trim();
		if (checkuser(temps)){
			if (!temps.equals("")&& !listModel.contains(temps)){
				listModel.addElement(temps);
				userNameText.setText("");
				userNameText.grabFocus();
				UpdateInvitedUserList();
			} else {
				userNameText.grabFocus();
			}
		}else if (!checkuser(temps)){
			JOptionPane.showMessageDialog(this,  "The name you input is not an exsisting User. Please input a proper User",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void UpdateInvitedUserList() {
		int length = listModel.size();
		LinkedList<String> temp = new LinkedList<String>();
		for(int i = 0;i<length;i++){
			temp.add(new String(listModel.elementAt(i).toString()));
		}
		userlist = temp;
		//this._controller.setinvitedUserList(temp);
		System.out.println(temp.getFirst());
		parent.setinvitedUserlist(temp);
		
	}
	
	public LinkedList<String> getUserlist(){
		return userlist;
	}
	
	public void setParent(AppScheduler parent) {
		this.parent = parent;
	}
	
	public boolean checkuser(String id){
		Save s=_controller.getSave();
		List<Userlist> list=new ArrayList<Userlist>();
		list=s.getApptlist();
		if(!id.equals(_controller.getDefaultUser().getID())){
			for(Userlist a:list){
				if(a.getuser().getID().equals(id))return true;
			}
		}
		return false;
	}
	
}