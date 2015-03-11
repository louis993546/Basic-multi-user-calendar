package hkust.cse.calendar.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

public class Clock extends JFrame {
	private CalGrid parent;
	private Timer timer;
	
	public Clock(CalGrid cal) {
		parent = cal;
		
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				parent.updateDisplay();
			}
		});
		timer.start();
	}
}
