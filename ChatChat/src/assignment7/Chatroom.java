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

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

//import assignment7.Client.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Chatroom {
	
	//private List<User> members;
	private List<Message> messages;
	private Chatroom currentchat;

	@FXML	
	private Button refreshButton;	
	@FXML	
	private Button sendButton;
	@FXML	
	private TextArea usertext;
	@FXML	
	private TextField chatlog;
    @FXML
    private GridPane chatmessages;
 
    @FXML
    private Label nameLabel;
	
	public void add(Message m){ messages.add(m);}
	
	public List<Message> getMessages() { return messages; }
	
	@FXML
	private void handleRefreshAction(ActionEvent event) throws IOException{
		
				boolean waiting = true;
				Client.toServer.writeObject(1);
				while (waiting) {
					try {
						ArrayList<Message> fullRoom = (ArrayList<Message>) Client.fromServer.readObject();
						for (Message m: fullRoom) {
							System.out.println(m.message);							
						}
						//displayMessages(fullRoom);
						waiting = false;
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
					//	e.printStackTrace();
					}
					catch (ClassCastException e) {
						//Client.toServer.writeObject(1);
					}
				}
		
		
			//usertext.setText("");
			//String pass = password.getText();
	 }
	@FXML
	private void handleSendAction(ActionEvent event) throws IOException{
		   //  Stage stage; 
		    // Parent root;
			String incomingMsg = usertext.getText();
			//chatlog.appendText(incomingMsg);
			System.out.println(incomingMsg);
			Client.toServer.writeObject(incomingMsg);
			ArrayList<Message> fullRoom;
			
			
			
				ImageView icon = new ImageView("file:img/0.png");
				icon.setFitHeight(50);
				icon.setFitWidth(50);
						
				TextArea textarea = new TextArea("testing testing 123");		// get the text contents
				textarea.setEditable(false);
				textarea.setMaxWidth(500);
				textarea.setWrapText(true);

				
				ImageView icon2 = new ImageView("file:img/2.png");
				icon2.setFitHeight(50);
				icon2.setFitWidth(50);
				
				chatmessages.add(icon, 0, 1);								// add it to the grid in the scrollbox 
				chatmessages.add(textarea, 1, 1);								// add it to the grid in the scrollbox 
				TextArea textarea2 = new TextArea("testing testing 123");		// get the text contents
				textarea2.setEditable(false);
				textarea2.setMaxWidth(500);
				textarea2.setWrapText(true);
				
				chatmessages.add(icon2, 0, 2);								// add it to the grid in the scrollbox 
				chatmessages.add(textarea2, 1, 2);								// add it to the grid in the scrollbox 
					

			//usertext.setText("");
			//String pass = password.getText();
	 }
	@FXML
	private void displayMessages(ArrayList<Message> messages){ 								// used when switching between chatrooms
		chatmessages.getChildren().clear();
		for(int k = 0; k < messages.size(); k++){
			Message message = messages.get(k);		// get the message

			ImageView icon = null;
			try{
			icon = new ImageView(message.sender.avatar);	// get the icon	
			} catch (Exception e){
				System.out.println("there was a problem getting the image");
			}
			TextArea textarea = new TextArea(message.message);		// get the text contents

			textarea.setEditable(false);
			
			textarea.setStyle("-fx-text-fill: #eeeeee;");
			if (message.sender.equals("fixme")){						// if the user is me, make my messages look different :D	
				textarea.setStyle("-fx-background-color: #353333");
				textarea.setStyle("-fx-stroke: #A9A9A9");
				textarea.setStyle("-fx-stroke-width: 1.5");
				textarea.setStyle("-fx-stroke-dash-array: 18 9 3 9;");
				textarea.setStyle("-fx-stroke-line-cap: round");

			}
			else 
				textarea.setStyle("-fx-background-color: #A9A9A9");
			
			HBox m = new HBox(10, icon, textarea);						// put them next to each other    		
			chatmessages.add(m, k, 2);								// add it to the grid in the scrollbox 
		}
	}
    @FXML
    private void sendMessage(){ // press enter in usertext or press send button
    	if(usertext.getText() != ""){
	    	Message meow = new Message(Client.currentUser, usertext.getText());
	    	currentchat.add(meow);		// add the message to the current chatroom that's in focus
	    	
	    	ImageView icon = new ImageView(Client.currentUser.avatar);	// get the icon
    		TextArea textarea = new TextArea(usertext.getText());		// get the text contents
			textarea.setEditable(false);
			
			textarea.setStyle("-fx-background-color: #353333");
			textarea.setStyle("-fx-stroke: #A9A9A9");
			textarea.setStyle("-fx-stroke-width: 1.5");
			textarea.setStyle("-fx-stroke-dash-array: 18 9 3 9;");
			textarea.setStyle("-fx-stroke-line-cap: round");
    		
    		
    		HBox m = new HBox(7, icon, textarea);				// put them next to each other
	    	chatmessages.add(m, currentchat.getMessages().size(), 2);	    	
	    	usertext.setText("");
    	}
    }

}



