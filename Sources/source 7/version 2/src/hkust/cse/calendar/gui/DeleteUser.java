package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.User;

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
import javax.swing.JTextField;


public class DeleteUser extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HashMap<String, User> userListCopy;
	
	
	private JTextField  userName;
	private JPasswordField confirmPassword, userPassword;
	
	private JButton btnCancel, btnDeleteUser;
	
	
	public DeleteUser(HashMap<String, User> userList){
		
		/*make userListCopy a reference to the main hash map*/
		if(userList != null){
			
			userListCopy = userList;
		}
		else{
			userListCopy = null;
		}
		
		/*give a name to the Dialog Box*/
		this.setTitle("Delete");
		
		
		/*set some restrictions on the dialog box*/
		this.setLayout(new BorderLayout());
		this.setLocationByPlatform(true);
		this.setSize(300, 150);
		this.setResizable(false); // do not allow to resize the dialog box
		this.toFront(); // bring the delete dialog box in front of the rest of dialog boxes
		
		/*make it pretty*/		
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		/*for text fields*/
		JPanel panelName = new JPanel();
		panelName.add(new JLabel("User Name: "));
		userName = new JTextField(15);
		panelName.add(userName);
		mainPanel.add(panelName);
		
		
		JPanel panelPass = new JPanel();
		panelPass.add(new JLabel("Password:  "));
		userPassword = new JPasswordField(15);
		panelPass.add(userPassword);
		mainPanel.add(panelPass);
		
		
		JPanel panelConfirm = new JPanel();
		panelConfirm.add(new JLabel("Confirm:      "));
		confirmPassword = new JPasswordField(15);
		panelConfirm.add(confirmPassword);
		mainPanel.add(panelConfirm);
		
		
		
		/*panel for the buttons*/
		JPanel panelBtn = new JPanel();
		panelBtn.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		/*delete a user*/
		btnDeleteUser = new JButton("     Delete      ");
		btnDeleteUser.addActionListener(this);
		panelBtn.add(btnDeleteUser);
		
		/*cancel*/
		btnCancel = new JButton("     Cancel       ");
		btnCancel.addActionListener(this);
		panelBtn.add(btnCancel);
		
		mainPanel.add(panelBtn);
		
		
		this.add("North", mainPanel);
		
		this.setVisible(true);
		
	}
	
		
	
	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		
		if(event.getSource() == btnCancel){
			/*if the cancel button gets pressed, close the dialog*/
			this.setVisible(false);
			dispose();
		}
		else{
			if(event.getSource() == btnDeleteUser){
				
				
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
				
				
				tempTest = userPassword.getText();
				sizeOfString = userPassword.getText().length();
				
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
				
				
				
				/*if the root is tried to be deleted, do not allow to do that*/
				if(userName.getText().equals("root") == true){
					JOptionPane.showMessageDialog(this, "The \"root\" user is not allowed to be deleted");
					return;
				}
				
				/*if the list is empty (I might delete this as root will always exist)*/
				if(userListCopy.isEmpty() == true){
					JOptionPane.showMessageDialog(this, "There is no such a user");
					return;
				}
				
				/*I might need to delete up to here*/					
				
				else{
					if(userListCopy.get(userName.getText()) == null){
						JOptionPane.showMessageDialog(this, "There is no such a user");
						return;
					}
					else{
						if(userListCopy.get(userName.getText()).Password().equals(userPassword.getText()) == true){
							/*first, delete all the user’s appointments. The SingletonMediator class hadnles this issues*/
							hkust.cse.calendar.unit.SingletonMediator.getBrainInstance().deleteAppointments(userName.getText());
							
							/*after having deleted all appointments, delete the user*/
							userListCopy.remove(userName.getText()); // delete the user
							JOptionPane.showMessageDialog(this, "User \"" + userName.getText().toString() + "\" has been deleted");
							this.setVisible(false);
							dispose();
							return;
						}
						else{
							JOptionPane.showMessageDialog(this, "You are not allowed to delete \"" + userListCopy.get(userName.getText()).ID() + "\"");
							return;
						}
					}
				}
				
				
			}
		}
		
	}
	

}
