/* Client.java
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


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;

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
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class Client extends Application {

		private User user;

		static ObjectOutputStream toServer = null;
		static ObjectInputStream fromServer = null;
		static User currentUser;

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
	    private Button selecticon;
	    @FXML
	    private Button iconselected;
	    @FXML
	    private ImageView chatchatIcon;
	    
	    static String selectedicon;
	    
	
	    protected void initialize() {
	    	
	    	/*new Timer().scheduleAtFixedRate(new TimerTask() {   
			    public void run() {
			    	try {
						//handleRefreshAction(null);
			    		 Chatroom.timeRefresh();
			    		System.out.println("Refresh Please!");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        // Here comes your void to refresh the whole application.

			    }
			}, 2000, 2000);*/
	    }
	    @FXML
	    private void handleLinkAction(ActionEvent event) throws IOException{
	        Stage stage = null; 
	        Parent root = null;
	        if(event.getSource()==registerLink){
	           stage=(Stage) registerLink.getScene().getWindow();
	           root = FXMLLoader.load(getClass().getResource("Register.fxml"));
	         }
	        if(event.getSource()==loginLink){ 
	          stage=(Stage) loginLink.getScene().getWindow();
	          root = FXMLLoader.load(getClass().getResource("Login.fxml"));
	         }
	         Scene scene = new Scene(root);
	         stage.setScene(scene);
	         stage.show();
	       }
	    
	    @SuppressWarnings("deprecation")
		@FXML
	    private void handleButtonAction(ActionEvent event) throws IOException{
	    	
	    	 if(event.getSource() == selecticon){	    		 
	    		 Stage stage = new Stage();
			     Parent root = FXMLLoader.load(getClass().getResource("IconSelect.fxml"));			        
	    		 stage.setScene(new Scene(root));
	    		 stage.initModality(Modality.APPLICATION_MODAL);
	    		 stage.initOwner(selecticon.getScene().getWindow());
	    		 stage.showAndWait();	    		 
	    	 }
	    		 
    		 if(event.getSource() == iconselected){
 	        	Stage stage = (Stage) iconselected.getScene().getWindow();
 	        	stage.close();
 	        	
 	        	//selecticon.setGraphic();
 	        	
    		 }
	    		 
	        if(event.getSource() == registerButton){
	           // do registration stuff
	        	// check to make sure the username is not already in use
	        	ImageView img = (ImageView) selecticon.getGraphic();
	        	String fullpath = img.getImage().impl_getUrl();		// YES, I KNOW IT'S NOT GOOD PRACTICE, BUT I NEED IT Q-Q

	        	int index = fullpath.lastIndexOf("/");
	        	String fileName = fullpath.substring(index + 1);

	        	System.out.println(fileName);
	        	
	        	user = new User(username.getText(), nickname.getText(), password.getText(), fileName);
	        	
	        	toServer.writeObject(user);
	        	toServer.flush(); 
	        	
				 try {
					currentUser  = (User) fromServer.readObject();
				} catch (ClassNotFoundException e) { e.printStackTrace(); }
				 
	        	Stage stage = (Stage) registerButton.getScene().getWindow();
		        Parent root = FXMLLoader.load(getClass().getResource("CRoom.fxml"));			        
		        Scene scene = new Scene(root);
		        stage.setScene(scene);
		        stage.show();
	         }
	        if(event.getSource() == loginButton){ 
	          // do login stuff
	        	//System.out.println(username.getText());
	        	
	        	//toServer.writeBytes(username.getText()+ ' ' + password.getText() +'\n'); 
				user = new User(username.getText(),"", password.getText(), "");

	        	toServer.writeObject(user);
	        	toServer.flush(); 
	        	
				 try {
					currentUser  = (User) fromServer.readObject();
				} catch (ClassNotFoundException e) { e.printStackTrace(); }
				if (currentUser == null) {
		        	System.out.println("Login failed!");

				} else {
					
				    Stage stage=(Stage) loginButton.getScene().getWindow();
			        Parent root = FXMLLoader.load(getClass().getResource("CRoom.fxml"));			        
			        Scene scene = new Scene(root);
			        stage.setScene(scene);
			        stage.show();
					//System.out.println("I logged in~~~~");

				}
				
				//currentchat = allchats.get(0);	// default in global chat

	         }
	        
	    }
	    
		public static void main(String[] args) { launch(args); }
		
		public static void note() {
			Client.toServer.notify();
		}
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
		     //   Socket socket = new Socket("10.145.177.101", 8000);

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

