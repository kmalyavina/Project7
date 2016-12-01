/* Chatroom.java
 * EE422C Project 7 submission by
 * Katya Malyavina
 * ym5356
 * 16465
 * Brian Sutherland
 * bcs2433
 * 16445
 * Slip days used: 1
 * Fall 2016
 * GitHub Repository: https://github.com/kmalyavina/Project7
 */

package assignment7;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
//import assignment7.Client.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class Chatroom {
	
	//private List<User> members;
	private List<Message> messages;
	private Chatroom currentchat;
	private static boolean time = false;

	@FXML
	static Button refreshButton;	
	@FXML	
	private Button sendButton;
	@FXML	
	private TextArea usertext;
	@FXML	
	private TextField chatlog;
    @FXML
    private GridPane chatmessages;
    @FXML
    private GridPane roomlist;
    @FXML
    protected void initialize() {
    	try {
			handleRefreshAction(null);
			time = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	new Timer().scheduleAtFixedRate(new TimerTask() {   
    
	    	 @Override
	         public void run() {
	             Platform.runLater(() -> {
	 				try {
						handleRefreshAction(null);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

	             });
	           
	    
	        // Here comes your void to refresh the whole application.

	    }
	}, 2000, 2000);
        };
	
	public void add(Message m){ messages.add(m);}
	
	public List<Message> getMessages() { return messages; }
	
    @FXML
    private Label nameLabel;
    @FXML
    private ImageView userIcon;
    
    static void timeRefresh() {
    	if (time) {
		 IntStream.range(0, 1).forEach(
                 i -> Chatroom.refreshButton.fire()
         );
    	}
    }

	@FXML
	 void handleRefreshAction(ActionEvent event) throws IOException{
		
						Client.toServer.writeObject("+REFRESH+");

						try {
							//Object fullRoom =  Client.fromServer.readObject();
							Integer c = 1;
							chatmessages.getChildren().clear();

							while (true) {
								Message m = (Message) Client.fromServer.readObject();
								if (m.message.equals("+END+")) {break;}

						
								//System.out.println(m.message+" at row:"+c);
								String url = "file:img/" + m.sender.avatar;
								
								Image img  = new Image(url);
								ImageView icon = new ImageView(url);		// replace the path with user icon
								//System.out.println(img.toString());
								icon.setImage(img);
								icon.setFitHeight(50);
								icon.setFitWidth(50);
								TextArea textmess = new TextArea(m.message);		// get the text contents
								textmess.setEditable(false);
								textmess.setPrefHeight(50);
								textmess.setMaxWidth(500);
								textmess.setWrapText(true);
								
								chatmessages.add(icon, 0, c);
								chatmessages.add(textmess, 1, c);
								
							
								c++;
																// add it to the grid in the scrollbox 
									

								
							}
							//Client.toServer.writeObject(true);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

	 }
	
	public void loadChatroom(){
		nameLabel.setText("Hello, " + Client.currentUser.displayName + "!");	
		String url = "file:img/" + Client.currentUser.avatar;
		Image icon = new Image(url);		// replace the path with user icon
		//System.out.println(Client.currentUser.avatar); //

		userIcon.setImage(icon); //

		
	}
	
	@FXML
	private void handleSendAction(ActionEvent event) throws IOException{	
			loadChatroom();
			// Stage stage; 
		    // Parent root;
			String incomingMsg = usertext.getText();
			//chatlog.appendText(incomingMsg);
		//	System.out.println(incomingMsg);
			Client.toServer.writeObject(incomingMsg);
			Client.toServer.flush();

			ArrayList<Message> fullRoom;
	
				handleRefreshAction(null);
			/*	for(int i = 0; i < 9; i++){
					ImageView icon = new ImageView("file:img/" + i + ".png");		// replace the path with user icon
					icon.setFitHeight(50);
					icon.setFitWidth(50);
					TextArea textmess = new TextArea("message #"+ i + "-------------------");		// get the text contents
					textmess.setEditable(false);
					textmess.setMinHeight(50);
					textmess.setMaxWidth(500);
					textmess.setWrapText(true);
					
					chatmessages.add(icon, 0, i+1);
					chatmessages.add(textmess, 1, i+1);
					
				}*/

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

}



