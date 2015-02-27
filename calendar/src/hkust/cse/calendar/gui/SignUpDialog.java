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

public class SignUpDialog extends JFrame implements ActionListener{

	private JTextField usernameTF;		//TextField for username
	private JPasswordField password1PF;	//PasswordField for password2
	private JPasswordField password2PF;	//PasswordField for password2
	private JTextField emailTF;			//TextField for email
	private JButton signupB;			//Button for confirm signup
	private JButton cancelB;			//Button for cancel signup
	
	//constructor: construct the GUI
	public SignUpDialog ()  
	{
		setTitle("Sign up");	
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		Container contentPane;
		contentPane = getContentPane();
		
		//create a new JPanel to hold everything
		JPanel sud = new JPanel();
		sud.setLayout(new BoxLayout(sud, BoxLayout.Y_AXIS));
		
		//messPanel contains message to be displayed
		JPanel messPanel = new JPanel();
		messPanel.add(new JLabel("Create account to login"));
		sud.add(messPanel);
		
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		namePanel.add(new JLabel("User Name:"));
		usernameTF = new JTextField(15);
		namePanel.add(usernameTF);
		sud.add(namePanel);
		
		JPanel pwPanel1 = new JPanel();
		pwPanel1.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pwPanel1.add(new JLabel("Password:  "));
		password1PF = new JPasswordField(15);
		pwPanel1.add(password1PF);
		sud.add(pwPanel1);
		
		JPanel pwPanel2 = new JPanel();
		pwPanel2.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pwPanel2.add(new JLabel("Type password again:  "));
		password2PF = new JPasswordField(15);
		pwPanel2.add(password2PF);
		sud.add(pwPanel2);
		
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
		//define all button clicks
		if (e.getSource() == signupB) //signup button
		{
			//get all textfield
			//check if username is available
			//check if the 2 passwords are the same
			//?
		}
		else if (e.getSource() == cancelB) //cancel button
		{
			//close this and go back
		}
		
	}
}
