package hkust.cse.calendar.gui;
import javax.swing.*;
import java.awt.event.*;

public class NotificationDialog {
	JFrame frame;
	  public static void main(String[] args){
		  NotificationDialog db = new NotificationDialog();
	  }

	  public NotificationDialog(){
	    frame = new JFrame("You have an appointment after 1 hour");
	    JButton button = new JButton("Click Me To Return");
	    button.addActionListener(new MyAction());
	    frame.add(button);
	    frame.setSize(400, 400);
	    frame.setVisible(true);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  }

	  public class MyAction implements ActionListener{
	    public void actionPerformed(ActionEvent e){
	      JOptionPane.showMessageDialog(frame,"You have an appointment after 1 hour");

	    }
	  }
}
