package hkust.cse.calendar.gui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import hkust.cse.calendar.unit.Appt;
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

public class BookmarkList extends JFrame {

	private UserManagement um = UserManagement.getInstance();

	private DefaultListModel listModel;

	private JList list;
	
	private CalGrid parent;
	
	public BookmarkList(final CalGrid cal) {
		parent = cal;
		
		setTitle("Appointment Bookmark List");
		
		this.setLayout(new BorderLayout());
		this.setLocationByPlatform(true);
		this.setSize(300, 200);
		
		//new list model
		listModel = new DefaultListModel();	
		list = new JList(listModel);	
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		list.setSelectedIndex(0);	
		list.setVisibleRowCount(5);
		
		JScrollPane listScrollPane = new JScrollPane(list);	
		
		
		add(listScrollPane, BorderLayout.CENTER);
		this.setVisible(true);
		
		
		//add context menu
		
		final JPopupMenu menu = new JPopupMenu();
		JMenuItem item = new JMenuItem("Details");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Appt appt = (Appt)listModel.getElementAt(list.getSelectedIndex());
				
				DetailsDialog info = new DetailsDialog(appt, "Appointment Details");

				info.setVisible(true);
			}
		});
		menu.add(item);

		item = new JMenuItem("Navigate");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Appt appt = (Appt)listModel.getElementAt(list.getSelectedIndex());
				
				cal.navigate(appt);
			}
		});
		menu.add(item);
		
		item = new JMenuItem("Remove from List");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Appt appt = (Appt)listModel.getElementAt(list.getSelectedIndex());
				
				cal.mCurrUser.RemoveBookmark(appt);
				
				loadBookmarks();
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
		
		loadBookmarks();
		setLocationRelativeTo(null);
		
	}
	
	private int getRow(Point point)
	{
		return list.locationToIndex(point);
	}
	
	private void loadBookmarks() {
		listModel.removeAllElements();
		for(Appt appt : parent.mCurrUser.getBookmark()) {
			listModel.addElement(appt);
		}
	}
	
}
