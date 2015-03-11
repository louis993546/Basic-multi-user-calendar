package hkust.cse.calendar.gui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LocationDialog extends JFrame implements ActionListener{
	//GUI for adding new location
	private JTextField locationTF;		//TextField for location
	private JButton signupB;			//Button for confirm
	private JButton cancelB;			//Button for cancel
	
	public LocationDialog() {	
		setTitle("New Location");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		Container contentPane;
		contentPane = getContentPane();

		//create a new JPanel to hold everything
		JPanel sud = new JPanel();
		sud.setLayout(new BoxLayout(sud, BoxLayout.Y_AXIS));

		//messPanel contains message to be displayed
		JPanel messPanel = new JPanel();
		messPanel.add(new JLabel("New Location:"));
		sud.add(messPanel);

		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		locationTF = new JTextField(15);
		namePanel.add(locationTF);
		sud.add(namePanel);
		
		contentPane.add("North", sud);

		JPanel butPanel = new JPanel();
		cancelB = new JButton("Cancel");
		cancelB.addActionListener(this);
		butPanel.add(cancelB);
		signupB = new JButton("Create");
		signupB.addActionListener(this);
		butPanel.add(signupB);

		contentPane.add("South", butPanel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
