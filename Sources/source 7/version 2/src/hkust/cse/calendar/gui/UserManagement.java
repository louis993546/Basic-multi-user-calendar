package hkust.cse.calendar.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import hkust.cse.calendar.unit.SingletonMediator;
import hkust.cse.calendar.unit.User;;

public class UserManagement extends JFrame implements ActionListener{
	
	
	private User userNewInfo;
	
	private String userKey;
	private JTextField firstName, lastName, email, userName;
	private JTextArea otherInfo;
	private JPasswordField confirmPassword,userPassword;
	
	private JButton btnUpdate, btnCancel;
	
	public UserManagement(User changeUser){
				
		/*give a name to the Dialog Box*/
		this.setTitle("Manage user�s information");
		
		
		
		/*set some restrictions on the dialog box*/
		this.setLayout(new BorderLayout());
		this.setLocationByPlatform(true);
		this.setSize(550, 500);
		this.setResizable(false); // do not allow to resize the dialog box
		
		/*make it pretty*/		
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		
		
		/*for the text fields*/
		JPanel contentPaneName = new JPanel();
		contentPaneName.add(new JLabel("First Name (1-15):                                                               "));
		firstName = new JTextField(15);
		firstName.setText(changeUser.getFirstName());
		contentPaneName.add(firstName);
		mainPanel.add(contentPaneName);
		
		JPanel contentPaneLastName = new JPanel();
		contentPaneLastName.add(new JLabel("Last Name (1-15):                                                               "));
		lastName = new JTextField(15);
		lastName.setText(changeUser.getLastName());
		contentPaneLastName.add(lastName);
		mainPanel.add(contentPaneLastName);
		
		JPanel contentPaneEmail = new JPanel();
		contentPaneEmail.add(new JLabel("Email (1-40): "));
		email = new JTextField(40);
		email.setText(changeUser.getEmail());
		contentPaneEmail.add(email);
		mainPanel.add(contentPaneEmail);
		
		JPanel contentPane1 = new JPanel();
		contentPane1.add(new JLabel("User Name (1-15):                                                               "));
		userName = new JTextField(15);
		userName.setText(changeUser.ID());
		contentPane1.add(userName);
		mainPanel.add(contentPane1);
		
		JPanel contentPane2 = new JPanel();
		contentPane2.add(new JLabel("Password (5-15):                                                                 "));
		userPassword = new JPasswordField(15);
		contentPane2.add(userPassword);
		mainPanel.add(contentPane2);
		
		JPanel contentPane3 = new JPanel();
		contentPane3.add(new JLabel("Confirm (5-15):                                                                     "));
		confirmPassword = new JPasswordField(15);
		contentPane3.add(confirmPassword);
		mainPanel.add(contentPane3);
		
		
		
		/*a large panel for the extra information*/
		JPanel contentPane4 = new JPanel();
		contentPane4.setLayout(new BorderLayout());
		Border contentPane4Border = new TitledBorder(null, "Other information: ");
		contentPane4.setBorder(contentPane4Border);
		otherInfo = new JTextArea(14, 10);
		otherInfo.setEditable(true);
		otherInfo.setText(changeUser.getOtherInformation());
		JScrollPane detailScroll = new JScrollPane(otherInfo);
		contentPane4.add(detailScroll);
		contentPane4.add(otherInfo, BorderLayout.AFTER_LAST_LINE);
		mainPanel.add(contentPane4);
		
		
		/*for the buttons*/
		JPanel contentPane5 = new JPanel();
		contentPane5.setLayout(new FlowLayout(FlowLayout.CENTER));
		/*add a user*/
		btnUpdate = new JButton("                  Update                   ");
		btnUpdate.addActionListener(this);
		contentPane5.add(btnUpdate);
		
		/*cancel your actions*/
		btnCancel = new JButton("                  Cancel                   ");
		btnCancel.addActionListener(this);
		contentPane5.add(btnCancel);
		
		mainPanel.add(contentPane5);
		
	
		this.add("North", mainPanel);
		
		
		userNewInfo = changeUser;
		userKey = changeUser.ID();
		
		pack();
		this.setVisible(true);
		
		
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnCancel){
			/*if the cancel button gets pressed, do nothing*/
			this.setVisible(false);
			dispose();
			return;
		}
		else{
			if(e.getSource() == btnUpdate){
				
				
				/*checks if the text fields are not empty*/
				boolean emptyField = true;
				String tempTest = userName.getText();
				int sizeOfString = userName.getText().length();
				
				for(int i = 0; i < sizeOfString; i++){
					if(tempTest.charAt(i) != ' '){
						emptyField = false;
						break;
					}
					
				}
				
				if(emptyField == true){
					JOptionPane.showMessageDialog(this, "Please fill in all the text fields");
					return;
				}
				
				
				
				
				tempTest = firstName.getText();
				sizeOfString = firstName.getText().length();
				
				for(int i = 0; i < sizeOfString; i++){
					if(tempTest.charAt(i) != ' '){
						emptyField = true;
						break;
					}
					
				}
				
				
				/*if at least one of the fields is empty, return*/
				if(emptyField == false){
					JOptionPane.showMessageDialog(this, "Please fill in all the text fields");
					return;
				}
				
				
				
				tempTest = lastName.getText();
				sizeOfString = lastName.getText().length();
				
				for(int i = 0; i < sizeOfString; i++){
					if(tempTest.charAt(i) != ' '){
						emptyField = false;
						break;
					}
					
				}
				
				
				/*if at least one of the fields is empty, return*/
				if(emptyField == true){
					JOptionPane.showMessageDialog(this, "Please fill in all the text fields");
					return;
				}
				
				
				tempTest = email.getText();
				sizeOfString = email.getText().length();
				
				for(int i = 0; i < sizeOfString; i++){
					if(tempTest.charAt(i) != ' '){
						emptyField = true;
						break;
					}
					
				}
				
				
				/*if at least one of the fields is empty, return*/
				if(emptyField == false){
					JOptionPane.showMessageDialog(this, "Please fill in all the text fields");
					return;
				}
				
				
				
				tempTest = userPassword.getText();
				sizeOfString = userPassword.getText().length();
				
				for(int i = 0; i < sizeOfString; i++){
					if(tempTest.charAt(i) != ' '){
						emptyField = false;
						break;
					}
					
				}
				
				
				/*if at least one of the fields is empty, return*/
				if(emptyField == true){
					JOptionPane.showMessageDialog(this, "Please fill in all the text fields");
					return;
				}
				
				
					
				int counter = 0;
				for(int i = 0; i < sizeOfString; i++){
					if(tempTest.charAt(i) != ' '){
						counter++;
					}
					
				}
				
				if( (5 > counter) || counter > 15){
					JOptionPane.showMessageDialog(this, "The password must be from 5 to 15 characters long");
					return;
				}
				
				/*if two the fields do not match, notice that*/
				if(userPassword.getText().equals(confirmPassword.getText()) == false){
					JOptionPane.showMessageDialog(this, "Please check your password");
					return;
				}
				
				/*if a user with the new name already exists, do not allow to change the particulars of the user*/
				if((SingletonMediator.getBrainInstance().getAllUsers().get(userName.getText()) != null) && (userKey.equals(userName.getText()) == false)){
					JOptionPane.showMessageDialog(this, "A user with the same user name already exists"); // notify the user
					return;
				}
				
				/*update the information of the user*/
				userNewInfo.setFirstName(firstName.getText()); // update the first name
				userNewInfo.setLastName(lastName.getText()); // update the last name
				userNewInfo.setID(userName.getText()); // update the user name
				userNewInfo.Password(userPassword.getText().toString()); // update the password
				userNewInfo.setEmail(email.getText()); // update the email address
				userNewInfo.setOtherInfmormation(otherInfo.getText()); // update the extra information field
				
				if(userKey.equals(userName.getText()) == true){

					JOptionPane.showMessageDialog(this, "The information has been updated"); // notify the user
					this.setVisible(false); // hide the dialog box
					dispose();
				}
				else{
					SingletonMediator.getBrainInstance().getAllUsers().remove(userKey);
					SingletonMediator.getBrainInstance().getAllUsers().put(userName.getText(), userNewInfo);
					JOptionPane.showMessageDialog(this, "The information has been updated"); // notify the user
					this.setVisible(false); // hide the dialog box
					dispose();
				}
			}
		
		}		
		
	}
	
}
