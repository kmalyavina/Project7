/* Client.java
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

import java.io.BufferedReader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Client extends Application {

		private User user;
		 static ObjectOutputStream toServer = null;
		 static ObjectInputStream fromServer = null;
		private Chatroom currentchat;

	    @FXML
	    private Hyperlink registerLink;
	    @FXML
	    private Hyperlink loginLink;
	    @FXML
	    private TextField username;
	    @FXML
	    private PasswordField password;
	    @FXML
	    private Button loginButton;
	    @FXML 
	    private Button registerButton;
	    @FXML
	    private TextField nickname;
	    @FXML
	    private Label error;
	    @FXML
	    private Button selectAvatar;
	    @FXML
	    private ImageView chatchatIcon;
	    
	    
	    // chatroom
	    @FXML
	    private GridPane chatmessages;
	    @FXML
	    private TextArea usertext;
	    @FXML
	    private Button sendButton;
	   
	    
	    
	    @FXML
	    private void displayMessages(){ 								// used when switching between chatrooms
	    	chatmessages.getChildren().clear();
	    	for(int k = 0; k < currentchat.getMessages().size(); k++){
	    		Message message = currentchat.getMessages().get(k);		// get the message
	    		ImageView icon = new ImageView(message.sender.avatar);	// get the icon
	    		TextArea textarea = new TextArea(message.message);					// get the text contents
    			textarea.setEditable(false);
    			
	    		textarea.setStyle("-fx-text-fill: #eeeeee;");
	    		if (message.sender.equals(user)){	
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
		    	Message meow = new Message(user, usertext.getText());
		    	currentchat.add(meow);		// add the message to the current chatroom that's in focus
		    	
		    	ImageView icon = new ImageView(user.avatar);	// get the icon
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
	    
	    @FXML
	    private void handleLinkAction(ActionEvent event) throws IOException{
	        Stage stage = null; 
	        Parent root = null;
	        if(event.getSource()==registerLink){
	           //get reference to the button's stage         
	           stage=(Stage) registerLink.getScene().getWindow();
	           //load up OTHER FXML document
	           root = FXMLLoader.load(getClass().getResource("Register.fxml"));
	         }
	        if(event.getSource()==loginLink){ 
	          stage=(Stage) loginLink.getScene().getWindow();
	          root = FXMLLoader.load(getClass().getResource("Login.fxml"));
	         }
	        //create a new scene with root and set the stage
	         Scene scene = new Scene(root);
	         stage.setScene(scene);
	         stage.show();
	       }
	    
	    @FXML
	    private void handleButtonAction(ActionEvent event) throws IOException{
	        if(event.getSource() == registerButton){
	           // do registration stuff
	        	System.out.println("I registered!!!!!");
	         }
	        if(event.getSource() == loginButton){ 
	          // do login stuff
	        	System.out.println(username.getText());
	        	
	        	//toServer.writeBytes(username.getText()+ ' ' + password.getText() +'\n'); 
				User me = new User(username.getText(),"", password.getText(), "");

	        	toServer.writeObject(me);
	        	toServer.flush(); 
				 try {
					me  = (User) fromServer.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (me == null) {
		        	System.out.println("Login failed!");

				} else {
				     Stage stage=(Stage) loginButton.getScene().getWindow();
			         Parent root = FXMLLoader.load(getClass().getResource("CRoom.fxml"));
			        /*try {
						Chatroom.start(stage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
			        //create a new scene with root and set the stage
			        Scene scene = new Scene(root);
			         stage.setScene(scene);
			         stage.show();
					System.out.println("I logged in~~~~");


				}
				//currentchat = allchats.get(0);	// default in global chat


	         }
	       }
	    
		public static void main(String[] args) { launch(args); }
		
		@Override
		public void start(Stage primaryStage) throws Exception{
			primaryStage.setTitle("Chat.Chat");
	        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
	    	Scene scene = new Scene(root);			 
	        primaryStage.setScene(scene);
	        primaryStage.show();
	
	        try {
				@SuppressWarnings("resource")
		        Socket socket = new Socket("127.0.0.1", 8000);
		       fromServer = new ObjectInputStream(socket.getInputStream());
			       toServer = new ObjectOutputStream(socket.getOutputStream());

				 //BufferedReader fromServer  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				//new DataInputStream(socket.getInputStream()); 
				//toServer = new DataOutputStream(socket.getOutputStream()); 

		        //socket.close();
		        //toServer.flush();

	        } catch (Exception e){
	        	System.out.println(e);
	        	System.out.println("Can't Connect");
	        }	
			
		}
}


class User implements Serializable {
	protected String userName;		// username(cannot change)(key)
	protected String password;		// password for logging in (changable)
	protected String displayName;	// displayed name (changable)
	protected String avatar;			// icon (changable)
	protected List<String> friends;	// list of friends
	protected boolean status;		// online/offline
	
	// constructor when have all info
	public User(String name, String nickname, String pass, String img, Boolean s, ArrayList<String> fl){
		userName = name;
		password = pass;
		displayName = nickname;
		avatar = img;
		friends = fl;
		status = s;
	}
	
	// constructor for NEW user
	public User(String name,String nickname, String pass, String img){
		userName = name;
		password = pass;
		displayName = nickname;
		avatar = img;
		friends = new ArrayList<String>();
		status = true;
	}
	
	public String toString(){
		return userName + " - " + displayName + " - " + password + " - " + avatar + " - " + status;
		
	}
}
