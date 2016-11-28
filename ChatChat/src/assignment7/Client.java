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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Client extends Application {
	
	public class User{
		protected String userName;		// username(cannot change)(key)
		protected String password;		// password for logging in (changable)
		protected String displayName;	// displayed name (changable)
		protected Image avatar;			// icon (changable)
		protected List<User> friends;	// list of friends
		protected boolean status;		// online/offline
		
		public User(String name, String pass, String nickname, Image img){
			userName = name;
			password = pass;
			displayName = nickname;
			avatar = img;
			friends = new ArrayList<User>();
			status = true;
		}
	}

	private static User user;
	private static ObjectOutputStream output;
	private static ObjectInputStream input;
	private static Socket connection;

	
	// ALLLL THE UI STUFF HERE :D
	// *******************************************************************************************
	private VBox ui; // temp
	
	@FXML
	private static TextField username;
	@FXML
	private static PasswordField password;	// pressing enter in this field = trigger login button action
	@FXML
	private static Button login;
	@FXML
	private static Hyperlink registerLink; // initial register button
	@FXML
	private static Hyperlink loginLink;
	@FXML
	private static Button register;
	@FXML	
	private static TextField nickname = new TextField();
	@FXML
	private static Button selectAvatar;
	//private Image avatar;
	
	
	private TextField userText;
	
	
	// *******************************************************************************************
	
	//**** Note: maybe we need to establish connection to server when program is opened so client can
	//**** 		 have access to the user database
	@FXML
	private static void login(){ // when login button is pressed
		String uName = username.getText();
		String pass = password.getText();
		if(ServerMain.users.containsKey(uName)){				// check if the usernamee exists in list of users
			if(ServerMain.users.get(uName).password == pass){	// check if pass matches username
				user = ServerMain.users.get(uName);
				try {
					connect(); 									// establish a connection to server  
				} catch (IOException e) { e.printStackTrace(); }
			}
			else {
				// your password is incorrect! 
				// clear fields
				//username.setText("");
				password.setText("");
			}
		}
		else {
			// there is no user with this name! Register?
		}
	}
	
	@FXML
	 private void handleLinkAction(ActionEvent event) throws IOException{
	     Stage stage; 
	     Parent root;
	     if(event.getSource()==registerLink){
	        //get reference to the button's stage         
	        stage=(Stage) registerLink.getScene().getWindow();
	        //load up OTHER FXML document
	  root = FXMLLoader.load(getClass().getResource("Register.fxml"));
	      }
	     else{
	       stage=(Stage) loginLink.getScene().getWindow();
	       root = FXMLLoader.load(getClass().getResource("Login.fxml"));
	      }
	     //create a new scene with root and set the stage
	      Scene scene = new Scene(root);
	      stage.setScene(scene);
	      stage.show();
	    }
	
	private void closeConnection() {
		try{
			user.status = false;
			ServerMain.info.appendText(user.userName + " has disconnected from the server. \n");
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioException){ ioException.printStackTrace(); }
	}

	private static void connect() throws IOException{
		connection = ServerMain.server.accept();
		user.status = true;
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
	}
	
	public static void main(String[] args) { launch(args); }
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		// set up all UI elements
        primaryStage.setTitle("Chat.Chat");
        Parent loginPage = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Parent registerPage = FXMLLoader.load(getClass().getResource("Register.fxml"));
        Parent iconSelection = FXMLLoader.load(getClass().getResource("IconSelect.fxml"));
        
        // Login ------------------------------------------------------------------------------
        
        	// username
        	// password
        	// login button
        	// register button
        
        // Register ---------------------------------------------------------------------------
        	// icon select button
        	// username
        	// nickname
        	// password
        	// complete registration button
        	// cancel button
        
        // Chatroom UI ------------------------------------------------------------------------
        	// list of users
        	// chatroom in view/focus
        
       
		Scene scene = new Scene(ui, ui.getPrefWidth(), ui.getPrefHeight());			 
        primaryStage.setScene(scene);
        primaryStage.show();
		
	}
	
	
}

