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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	private static ObjectOutputStream toServer = null;
	private static ObjectInputStream fromServer = null;

	
	// ALLLL THE UI STUFF HERE :D
	// *******************************************************************************************
	@FXML
	private static ImageView chatchatIcon;
	@FXML
	private static TextField username;
	@FXML
	private static PasswordField password;	// pressing enter in this field = trigger login button action
	@FXML
	private static Button loginButton;
	@FXML
	private static Hyperlink registerLink; 
	@FXML
	private static Hyperlink loginLink;
	@FXML
	private static Button registerButton;
	@FXML	
	private static TextField nickname;
	@FXML
	private static Button selectAvatar;

	
	
	//private TextField userText; // chat text
	
	
	// *******************************************************************************************
	
	@FXML
	private static void login(){ // when login button is pressed
		String uName = username.getText();
		String pass = password.getText();
		if(ServerMain.users.containsKey(uName)){				// check if the usernamee exists in list of users
			if(ServerMain.users.get(uName).password == pass){	// check if pass matches username
				user = ServerMain.users.get(uName);
				
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
	     if(event.getSource() == registerLink){
	        stage = (Stage) registerLink.getScene().getWindow();
	        root = FXMLLoader.load(getClass().getClassLoader().getResource("assignment7\\Register.fxml"));
	      }
	     else{
	       stage = (Stage) loginLink.getScene().getWindow();
	       root = FXMLLoader.load(getClass().getClassLoader().getResource("assignment7\\Login.fxml"));
	      }

	     Scene scene = new Scene(root);
	     stage.setScene(scene);
	     stage.show();
	    }
	
	private void closeConnection() {
		try{
			user.status = false;
			ServerMain.info.appendText(user.userName + " has disconnected from the server. \n");
			fromServer.close();
			toServer.close();
		}catch(IOException ioException){ ioException.printStackTrace(); }
	}

	
	public static void main(String[] args) { launch(args); }
	
	@Override
	public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Chat.Chat");
        Parent loginPage = FXMLLoader.load(getClass().getClassLoader().getResource("assignment7\\Login.fxml"));
        
//        registerLink.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent e) {
//    	        try {
//        	        Stage stage = (Stage) registerLink.getScene().getWindow();
//					Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("assignment7\\Register.fxml"));
//				    Scene scene = new Scene(root);
//				    stage.setScene(scene);
//				    stage.show();
//				} catch (IOException e1) {e1.printStackTrace();}        
//            }
//        });
//        
//        loginLink.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent e) {
//    	        try {
//        	        Stage stage = (Stage) loginLink.getScene().getWindow();
//					Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("assignment7\\Login.fxml"));
//				    Scene scene = new Scene(root);
//				    stage.setScene(scene);
//				    stage.show();
//				} catch (IOException e1) {e1.printStackTrace();}        
//            }
//        });
       
        try {
	        Socket socket = new Socket("127.0.0.1", 8000);
	        fromServer = new ObjectInputStream(socket.getInputStream());
	        toServer = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e){
        	System.out.println(e);
        	System.out.println("Can't Connect");
        } 
       
		Scene scene = new Scene(loginPage,850,560);			 
        primaryStage.setScene(scene);
        primaryStage.show();
		
	}
	
	
}

