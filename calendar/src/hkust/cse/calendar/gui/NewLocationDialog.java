package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.LocationDB;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NewLocationDialog extends JFrame implements ActionListener {
	// GUI for adding new location
	private JTextField capapcityTF;
	private JTextField locationTF; // TextField for location
	private JButton createB; // Button for confirm
	private JButton cancelB; // Button for cancel
	private LocationDB ldb;

	public NewLocationDialog() {
		setTitle("New Location");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		Container contentPane;
		contentPane = getContentPane();

		// create a new JPanel to hold everything
		JPanel sud = new JPanel();
		sud.setLayout(new BoxLayout(sud, BoxLayout.Y_AXIS));

		// messPanel contains message to be displayed
		JPanel messPanel = new JPanel();
		messPanel.add(new JLabel("New Location:"));
		sud.add(messPanel);

		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		locationTF = new JTextField(15);
		namePanel.add(locationTF);
		messPanel.add(new JLabel("Capacity:"));
		capapcityTF = new JTextField(15);
		namePanel.add(capapcityTF);
		sud.add(namePanel);

		contentPane.add("North", sud);

		JPanel butPanel = new JPanel();
		cancelB = new JButton("Cancel");
		cancelB.addActionListener(this);
		butPanel.add(cancelB);
		createB = new JButton("Create");
		createB.addActionListener(this);
		butPanel.add(createB);

		contentPane.add("South", butPanel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public NewLocationDialog(LocationDB l) {
		this();
		ldb = l;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancelB) {
			int n = JOptionPane.showConfirmDialog(null, "Discard all changes?",
					"Confirm", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION)
				dispose();
		} else if (e.getSource() == createB) {
			try {
				ldb.addLocation(locationTF.getText(),
						Integer.parseInt(capapcityTF.getText()));
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

	}

}
