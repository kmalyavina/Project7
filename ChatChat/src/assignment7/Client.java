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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Client extends Application {

		private User user;
		private static ObjectOutputStream toServer = null;
		private static ObjectInputStream fromServer = null;
		
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
	        	System.out.println("I logged in~~~~");
	        	// status = online
	        	// switch to chatroom fxml
	         }
	       }
	    
		public static void main(String[] args) { launch(args); }
		
		@Override
		public void start(Stage primaryStage) throws Exception{
			primaryStage.setTitle("Chat.Chat");
	        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
	
	        try {
		        Socket socket = new Socket("127.0.0.1", 8000);
		        fromServer = new ObjectInputStream(socket.getInputStream());
		        toServer = new ObjectOutputStream(socket.getOutputStream());
	        } catch (Exception e){
	        	System.out.println(e);
	        	System.out.println("Can't Connect");
	        } 
	        
			Scene scene = new Scene(root);			 
	        primaryStage.setScene(scene);
	        primaryStage.show();
			
		}
}

class User{
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
