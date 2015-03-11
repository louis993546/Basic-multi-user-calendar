package hkust.cse.calendar.gui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.user.UserManagement;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class UserList extends JFrame {

	private UserManagement um = UserManagement.getInstance();

	private DefaultListModel listModel;

	private JList list;
	
	private CalGrid parent;
	
	public UserList(CalGrid cal) {
		parent = cal;
		
		setTitle("User List");
		
		this.setLayout(new BorderLayout());
		this.setLocationByPlatform(true);
		this.setSize(300, 200);
		
		//new list model
		listModel = new DefaultListModel();	
		list = new JList(listModel);	
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		list.setSelectedIndex(0);	
		//list.addListSelectionListener(new ListSelectionListener(){});
		list.setVisibleRowCount(5);
		
		JScrollPane listScrollPane = new JScrollPane(list);	
		
		//add button
		JButton addButton = new JButton("Add");		
		addButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});	
		addButton.setEnabled(true);	
		
		//remove button
		JButton removeButton = new JButton("Remove");	
		removeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});	
		
		add(listScrollPane, BorderLayout.CENTER);
		this.setVisible(true);
		
		
		//add context menu
		
        final JPopupMenu menu = new JPopupMenu();
        JMenuItem item = new JMenuItem("Inspect");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            	String ID = listModel.getElementAt(list.getSelectedIndex()).toString();
            	
            	UserSettings info = new UserSettings(um.getUser(ID), true, true);
            	
            }
        });
        menu.add(item);

        item = new JMenuItem("Change");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            	String ID = listModel.getElementAt(list.getSelectedIndex()).toString();
            	
            	UserSettings settings = new UserSettings(um.getUser(ID), true);
            }
        });
        menu.add(item);
        
        item = new JMenuItem("Remove");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String ID = listModel.getElementAt(list.getSelectedIndex()).toString();
    			int n = JOptionPane.showConfirmDialog(null, "Are you sure to remove user \"" + ID + "\"",
    					"Confirm", JOptionPane.YES_NO_OPTION);
    			if (n == JOptionPane.YES_OPTION) {
    				
    				if(!um.removeUser(um.getUser(ID), parent.mCurrUser)) {
    					
    					JOptionPane.showMessageDialog(UserList.this, um.getLastError(),
    							"Error", JOptionPane.ERROR_MESSAGE);
    				}
    				else {
    					
    					listModel.remove(list.getSelectedIndex());
    					
    				}
    			}
    				//System.exit(0);			
            }
        });
        menu.add(item);
        
		list.addMouseListener( new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if ( SwingUtilities.isRightMouseButton(e) )
				{
					int index = getRow(e.getPoint());
                    if (index != -1 && !list.getCellBounds(index, index).contains(e.getPoint())) {
                    	return;
                    }
                    else {
    					list.setSelectedIndex(index);
    					
    					menu.show(list, e.getX(), e.getY());
                    }
                    
				}
			}
		});
		
		loadUsers();
		setLocationRelativeTo(null);
		
	}
	
	private int getRow(Point point)
	{
		return list.locationToIndex(point);
	}
	
	private void loadUsers() {
		for(String user : um.getAllUserIDs()) {
			listModel.addElement(user);
		}
		
		for(String user : um.getAllAdminUserIDs()) {
			listModel.addElement(user);
		}
	}
	
}
