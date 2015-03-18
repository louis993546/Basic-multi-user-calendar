package hkust.cse.calendar.gui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Notification extends JDialog {
	private JButton closeButton;
	
	public Notification(String message, String location) {
		setTitle("Reminder");
		Container contentPane;
		contentPane = getContentPane();
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		
		JPanel msgPanel = new JPanel();
		msgPanel.add(new JLabel(message));
		top.add(msgPanel);
		
		JPanel locationPanel = new JPanel();
		locationPanel.add(new JLabel("Location: " + location));
		top.add(locationPanel);
		
		JPanel buttonPanel = new JPanel();
		closeButton = new JButton("Ok");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setVisible(false);
				dispose();
			}
		});
		buttonPanel.add(closeButton);
		setLocationRelativeTo(null);
		top.add(buttonPanel);
		
		contentPane.add(top);
		
		pack();
		setVisible(true);
	}
}
