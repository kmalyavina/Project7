/* Chatroom.java
 * EE422C Project 7 submission by
 * Katya Malyavina
 * ym5356
 * 16465
 * Brian Sutherland
 * bcs2433
 * 16445
 * Slip days used: 0
 * Fall 2016
 * GitHub Repository: https://github.com/kmalyavina/Project7
 */

package assignment7;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

//import assignment7.Client.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Chatroom {
	
	private List<User> members;
	private List<Message> messages;
	
	
	@FXML	
	private Button sendButton;
	@FXML	
	private TextArea usertext;
	@FXML	
	private TextField chatlog;
	
	public void add(Message m){ messages.add(m);}
	
	public List<Message> getMessages() { return messages; }

	@FXML
	private void handleSendAction(ActionEvent event) throws IOException{
		   //  Stage stage; 
		    // Parent root;
			String incomingMsg = usertext.getText();
			System.out.println(incomingMsg);
			Client.toServer.writeObject(incomingMsg);
			usertext.setText("");
			//String pass = password.getText();
	 }

}

class Message {
	User sender;
	String message;
	//Timestamp timestamp;
	
	/**
	 * Message constructor
	 * Called when client presses enter to send a message to a chatroom
	 * 
	 * @param s sender
	 * @param m message
	 * @param time timestamp
	 */
	
	public Message(User s, String m){
		sender = s;
		message = m;
	}
}
