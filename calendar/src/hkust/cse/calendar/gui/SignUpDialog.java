package hkust.cse.calendar.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SignUpDialog extends JFrame implements ActionListener{

	private JFrame frame;
	private JTextField userName;
	private JPasswordField password1;
	private JPasswordField password2;
	private JTextField email;
	
	public SignUpDialog () {
		frame.setVisible(true);
	}
	
	public void start() {
		frame.setSize(400, 300);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
