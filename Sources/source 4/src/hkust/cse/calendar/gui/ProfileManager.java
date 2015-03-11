package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserStorageController;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class ProfileManager extends JDialog implements ActionListener{
	private UserStorageController userController;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JPasswordField rePasswordField;
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JTextField emailField;
	private JTextField bDayYearField;
	private JTextField bDayMonthField;
	private JTextField bDayDateField;
	private JButton saveButton;
	private JButton cancelButton;
	private User user;
	
	public ProfileManager(User user) {
		userController = UserStorageController.getInstance();
		this.user = user;

		setTitle("Profile Manager");
		setAlwaysOnTop(true);

		Container contentPane;
		contentPane = getContentPane();

		JPanel top = new JPanel();
		Border loginInfoBorder = new TitledBorder("Login Information");
		top.setBorder(loginInfoBorder);
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		
		JPanel rolePanel = new JPanel();
		rolePanel.add(new JLabel("Role: " + user.getRole() + " user"));
		top.add(rolePanel);
		
		JPanel usernamePanel = new JPanel();
		usernamePanel.add(new JLabel("Username: "));
		usernameField = new JTextField(10);
		usernameField.setText(user.ID());
		usernameField.setEditable(false);
		usernameField.setEnabled(false);
		usernamePanel.add(usernameField);
		top.add(usernamePanel);

		JPanel passwordPanel = new JPanel();
		passwordPanel.add(new JLabel("Password: "));
		passwordField = new JPasswordField(10);
		passwordField.setText(user.Password());
		passwordPanel.add(passwordField);
		top.add(passwordPanel);

		JPanel rePasswordPanel = new JPanel();
		rePasswordPanel.add(new JLabel("Re-type Password: "));
		rePasswordField = new JPasswordField(10);
		rePasswordField.setText(user.Password());
		rePasswordPanel.add(rePasswordField);
		top.add(rePasswordPanel);

		contentPane.add("North", top);

		JPanel personalInfoPanel = new JPanel();
		Border personalInfoBorder = new TitledBorder("Personal Information");
		personalInfoPanel.setBorder(personalInfoBorder);
		personalInfoPanel.setLayout(new BoxLayout(personalInfoPanel, BoxLayout.Y_AXIS));

		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
		namePanel.add(new JLabel("First Name: "));
		firstNameField = new JTextField(10);
		firstNameField.setText(user.getFirstName());
		namePanel.add(firstNameField);
		namePanel.add(new JLabel("Last Name: "));
		lastNameField = new JTextField(10);
		lastNameField.setText(user.getLastName());
		namePanel.add(lastNameField);
		personalInfoPanel.add(namePanel);

		JPanel emailPanel = new JPanel();
		emailPanel.add(new JLabel("Email: "));
		emailField = new JTextField(30);
		emailField.setText(user.getEmail());
		emailPanel.add(emailField);
		personalInfoPanel.add(emailPanel);

		JPanel bDayPanel = new JPanel();
		Border bDayBorder = new TitledBorder("Birthday");
		bDayPanel.setBorder(bDayBorder);
		bDayPanel.setLayout(new BoxLayout(bDayPanel, BoxLayout.X_AXIS));
		bDayPanel.add(new JLabel("YEAR: "));
		bDayYearField = new JTextField(5);
		bDayYearField.setText(new Integer(user.getBirthday().StartTime().getYear() + 1900).toString());
		bDayYearField.setEditable(false);
		bDayYearField.setEnabled(false);
		bDayPanel.add(bDayYearField);
		bDayPanel.add(new JLabel("MONTH: "));
		bDayMonthField = new JTextField(5);
		bDayMonthField.setText(new Integer(user.getBirthday().StartTime().getMonth() + 1).toString());
		bDayMonthField.setEditable(false);
		bDayMonthField.setEnabled(false);
		bDayPanel.add(bDayMonthField);
		bDayPanel.add(new JLabel("DAY: "));
		bDayDateField = new JTextField(5);
		bDayDateField.setText(new Integer(user.getBirthday().StartTime().getDate()).toString());
		bDayDateField.setEditable(false);
		bDayDateField.setEnabled(false);
		bDayPanel.add(bDayDateField);
		personalInfoPanel.add(bDayPanel);

		contentPane.add("Center", personalInfoPanel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		buttonPanel.add(saveButton);

		cancelButton = new JButton("Canel");
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);

		contentPane.add("South", buttonPanel);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == cancelButton) {
			setVisible(false);
			dispose();
		}
		else if(e.getSource() == saveButton){
			boolean succeed = saveButtonResponse();
			if(succeed) {
				JOptionPane.showMessageDialog(this, "Save successfully!");
				setVisible(false);
				dispose();
			}
		}
	}

	private boolean saveButtonResponse() {
		String pw = checkValidPassword(passwordField.getText(), rePasswordField.getText());
		if(pw == null) {
			return false;
		}

		String firstName = checkValidFirstName(firstNameField.getText());
		String lastName = checkValidLastName(lastNameField.getText());
		
		if(firstName == null || lastName == null) {
			return false;
		}

		String email = checkValidEmail(emailField.getText());
		if(email == null) {
			return false;
		}
		
		user.setName(firstName, lastName);
		user.setEmail(email);
		user.setPassword(pw);
		userController.manageUsers(user, UserStorageController.MODIFY);
		
		return true;
	}
	
	private boolean ValidString(String s) {	
		if(s.isEmpty()) {
			return false;
		}
		
		char[] sChar = s.toCharArray();
		for(int i = 0; i < sChar.length; i++)
		{
			int sInt = (int)sChar[i];
			if(sInt < 48 || sInt > 122)
				return false;
			if(sInt > 57 && sInt < 65)
				return false;
			if(sInt > 90 && sInt < 97)
				return false;
		}
		return true;
	}

	private String checkValidPassword(String password, String rePassword) {
		if(password.equals(rePassword)) {
			return password;
		}
		else {
			JOptionPane.showMessageDialog(this, "Passwords do not match", "Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	private String checkValidEmail(String email) {
		Pattern regexPattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher regMatcher	= regexPattern.matcher(email);
		if(regMatcher.matches()){
			return email;
		} else {
			JOptionPane.showMessageDialog(this, "Invalid email address", "Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	private String checkValidFirstName(String firstname) {
		if(firstname.matches("[a-zA-z]+([ '-][a-zA-Z]+)*")) {
			return firstname;
		}
		else {
			JOptionPane.showMessageDialog(this, "Invalid firstname", "Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	private String checkValidLastName(String lastname) {
		if(lastname.matches("[a-zA-z]+([ '-][a-zA-Z]+)*")) {
			return lastname;
		}
		else {
			JOptionPane.showMessageDialog(this, "Invalid lastname", "Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

}
