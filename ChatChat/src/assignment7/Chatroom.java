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
		
						Client.toServer.writeObject("+REFRESH+");
						//Client.toServer.flush();

						ArrayList<Message> fullRoom;
						try {
							fullRoom = (ArrayList<Message>) Client.fromServer.readObject();
							for (Message m: fullRoom) {
								System.out.println(m.message);
								
							}
							//Client.toServer.writeObject(true);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

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

				for(int i = 0; i < 9; i++){
					ImageView iconn = new ImageView("file:img/" + i + ".png");
					iconn.setFitHeight(50);
					iconn.setFitWidth(50);
					TextArea textmess = new TextArea(i + "-------------------MEOW MEOW MEOW lorem ipsum delor.....");		// get the text contents
					textmess.setEditable(false);
					textmess.setMaxWidth(500);
					textmess.setWrapText(true);
					
					chatmessages.add(iconn, 0, i+1);
					chatmessages.add(textmess, 1, i+1);
					
				}
					


			Client.toServer.flush();
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



