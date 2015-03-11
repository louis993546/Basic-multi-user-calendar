package hkust.cse.calendar.gui;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.unit.SimpleUser;


public class NewUser  extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HashMap<String, User> userListCopy;
	
	
	private JTextField firstName, lastName, email, userName;
	private JPasswordField confirmPassword,userPassword;
	
	private JButton btnAddUser, btnCancel, btnDeleteUser;
	
	
	public NewUser(HashMap<String, User> userList){
		
		
		try{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}catch(Exception e){
				
			}
		
		
		/*make userListCopy a reference to the main hash map*/
		if(userList != null){
			
			userListCopy = userList;
		}
		else{
			userListCopy = null;
		}
		
		/*give a name to the Dialog Box*/
		this.setTitle("Sign up");
		
		
		
		/*set some restrictions on the dialog box*/
		this.setLayout(new BorderLayout());
		this.setLocationByPlatform(true);
		this.setSize(550, 270);
		this.setResizable(false); // do not allow to resize the dialog box
		this.toFront(); // bring the create a user dialog box in front of the rest diloag boxes
		
		/*make it pretty*/		
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		
		
		/*for the text fields*/
		JPanel contentPaneName = new JPanel();
		contentPaneName.add(new JLabel("First Name (1-15):                                                               "));
		firstName = new JTextField(15);
		contentPaneName.add(firstName);
		mainPanel.add(contentPaneName);
		
		JPanel contentPaneLastName = new JPanel();
		contentPaneLastName.add(new JLabel("Last Name (1-15):                                                               "));
		lastName = new JTextField(15);
		contentPaneLastName.add(lastName);
		mainPanel.add(contentPaneLastName);
		
		JPanel contentPaneEmail = new JPanel();
		contentPaneEmail.add(new JLabel("Email (1-40): "));
		email = new JTextField(40);
		contentPaneEmail.add(email);
		mainPanel.add(contentPaneEmail);
		
		JPanel contentPane1 = new JPanel();
		contentPane1.add(new JLabel("User Name (1-15):                                                               "));
		userName = new JTextField(15);
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
		
		/*for the buttons*/
		JPanel contentPane4 = new JPanel();
		contentPane4.setLayout(new FlowLayout(FlowLayout.CENTER));
		/*add a user*/
		btnAddUser = new JButton("             Add              ");
		btnAddUser.addActionListener(this);
		contentPane4.add(btnAddUser);
		
		/*delete a user*/
		btnDeleteUser = new JButton("            Delete             ");
		btnDeleteUser.addActionListener(this);
		contentPane4.add(btnDeleteUser);
		mainPanel.add(contentPane4);
		
		
		/*cancel your actions*/
		JPanel contentPane5 = new JPanel();
		contentPane5.setLayout(new BorderLayout());
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		contentPane5.add(btnCancel, BorderLayout.NORTH);
		
		mainPanel.add(contentPane5);
		
	
		this.add("North", mainPanel);
		
		
		pack();
		setVisible(true);
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnCancel){
			/*if the cancel button gets pressed, close the dialog*/
			this.setVisible(false);
			dispose();
		}
		else{ 
			if(e.getSource() == btnAddUser){
				
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
				
				
				if(userListCopy.isEmpty() == true){
					userListCopy.put(userName.getText(), new SimpleUser(userName.getText(), userPassword.getText(), firstName.getText(), lastName.getText(), email.getText())); // if the map is empty, just add the new user
					JOptionPane.showMessageDialog(this, "The new user has been created");
					this.setVisible(false);
					dispose();
				}
				
				else{
					if(userListCopy.get(userName.getText()) == null){
						userListCopy.put(userName.getText(), new SimpleUser(userName.getText(), userPassword.getText(), firstName.getText(), lastName.getText(), email.getText())); // if the map does not have such a user, add him/her
						JOptionPane.showMessageDialog(this, "The new user has been created");
						this.setVisible(false);
						dispose();
					}
					else{
						JOptionPane.showMessageDialog(this, "A user with the same user name already exists");
						return;
					}
				}
			}
			else{
				if(e.getSource() == btnDeleteUser){
					
					/*output a new dialog box*/
					
					new DeleteUser(userListCopy);
					
					/*checks if the text fields are not empty*/
					/*boolean emptyField = true;
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
					
					
					tempTest = userPassword.getText();
					sizeOfString = userPassword.getText().length();
					
					for(int i = 0; i < sizeOfString; i++){
						if(tempTest.charAt(i) != ' '){
							emptyField = true;
							break;
						}
						
					}
					
					/*if at least one of the fields is empty, return*/
					/*if(emptyField == false){
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
					/*if(userPassword.getText().equals(confirmPassword.getText()) == false){
						JOptionPane.showMessageDialog(this, "Please check your password");
						return;
					}
					
					/*if the list is empty (I might delete this as root will always exist)*/
					/*if(userListCopy.isEmpty() == true){
						JOptionPane.showMessageDialog(this, "There is no such a user");
						return;
					}
					
					/*I might need to delete up to here*/					
					
					/*else{
						if(userListCopy.get(userName.getText()) == null){
							JOptionPane.showMessageDialog(this, "There is no such a user");
							return;
						}
						else{
							if(userListCopy.get(userName.getText()).Password().equals(userPassword.getText()) == true){
								userListCopy.remove(userName.getText()); // delete the user
								JOptionPane.showMessageDialog(this, "User \"" + userName.getText().toString() + "\" has been deleted");
								return;
							}
							else{
								JOptionPane.showMessageDialog(this, "You are not allowed to delete \"" + userListCopy.get(userName.getText()).ID() + "\"");
								return;
							}
						}
					}			
					*/
					
				}// end of if btnDeleteUser
			}// end of else
			
		}// end of the first else
		
	}// end of ActionListener
	
	

}
