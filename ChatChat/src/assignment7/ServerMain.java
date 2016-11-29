/* ServerMain.java
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

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class ServerMain extends Application { 
	private Scanner scan;
	
	private static Map<String, User> allusers = new HashMap<>();
	ArrayList<String> userList = new ArrayList<String>();

	
	/*
	 *  format:
	 *  	username nickname password icon.png status friend1 friend2 friend3...
	 * 
	 * 
	 */
	private Map<String, User> buildUsers(){
		scan = null;
		
		try {
			//File test = new File("").getAbsoluteFile();
			
			//System.out.println(test.toString());
			scan = new Scanner (new File("userList.txt"));
		} catch (FileNotFoundException e) { System.out.println("File not found."); }
			
		while(scan.hasNext()){
			userList.add(scan.nextLine());
		}
		
		for(int k = 0; k < userList.size(); k++){
			scan = new Scanner(userList.get(k));
			String username = scan.next();
			String nickname = scan.next();
			String password = scan.next();
			String userIcon = scan.next();
			String status = scan.next();
			Boolean s;
			if(status == "true")
				s = true;
			else
				s = false;
			
			ArrayList<String> friends = new ArrayList<String>();
			while(scan.hasNext()){
				friends.add(scan.next());
			}
			//allusers.put(username, new User(username, nickname, password, userIcon, s, friends));

			User auser = new User(username, nickname, password, userIcon, s, friends);
			System.out.println(auser);
			allusers.put(username, auser);
		}
			
		return allusers;
	}
	
	
	
	// Text area for displaying contents 
	private TextArea ta = new TextArea(); 

	// Number a client 
	private int clientNo = 0; 

	@Override // Override the start method in the Application class 
	public void start(Stage primaryStage) { 
		
		buildUsers();

		
		// Create a scene and place it in the stage 
		Scene scene = new Scene(new ScrollPane(ta), 450, 200); 
		primaryStage.setTitle("MultiThreadServer"); // Set the stage title 
		primaryStage.setScene(scene); // Place the scene in the stage 
		primaryStage.show(); // Display the stage 

		new Thread( () -> { 
			try {  // Create a server socket 
				ServerSocket serverSocket = new ServerSocket(8000); 
				ta.appendText("MultiThreadServer started at " 
						+ new Date() + '\n'); 


				while (true) { 
					// Listen for a new connection request 
					Socket socket = serverSocket.accept(); 

					// Increment clientNo 
					clientNo++; 

					Platform.runLater( () -> { 
						// Display the client number 
						ta.appendText("Starting thread for client " + clientNo +
								" at " + new Date() + '\n'); 

						// Find the client's host name, and IP address 
						InetAddress inetAddress = socket.getInetAddress();
						ta.appendText("Client " + clientNo + "'s host name is "
								+ inetAddress.getHostName() + "\n");
						ta.appendText("Client " + clientNo + "'s IP Address is " 
								+ inetAddress.getHostAddress() + "\n");	}); 


					// Create and start a new thread for the connection
					new Thread(new HandleAClient(socket)).start();
				} 
			} 
			catch(IOException ex) { 
				System.err.println(ex);
			}
		}).start();
	}


	// Define the thread class for handling
	class HandleAClient implements Runnable {
		private Socket socket; // A connected socket
		/** Construct a thread */ 
		public HandleAClient(Socket socket) { 
			this.socket = socket;
		}
		/** Run a thread */
		public void run() { 
			try {				
				// Create data input and output streams
				//DataInputStream inputFromClient = new DataInputStream( socket.getInputStream());
				ObjectOutputStream outputToClient = new ObjectOutputStream( socket.getOutputStream());
				 // BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				ObjectInputStream inputFromClient = new ObjectInputStream( socket.getInputStream());

				//outputToClient.write(1);
				// Continuously serve the client
				String input;
				String lastInput = null;
				while (true) { 
					//scan = new Scanner(inputFromClient.read);
					//String username = scan.next();
					//String password = scan.next();
					User credentials;
					try {
						credentials = (User) inputFromClient.readObject();
						User currentUser = allusers.get(credentials.userName);
						if (currentUser != null && currentUser.password.equals(credentials.password)) {
							System.out.println("Success!");
							outputToClient.writeObject(currentUser);
						} else {
							outputToClient.writeObject(currentUser);
						}
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//System.out.println("Hoping for password: "+ currentUser.password);
				
					
					outputToClient.flush();
					// Receive radius from the client 
					//double radius = inputFromClient.readDouble();
					
					//String internal = input;
					// Compute area
					//double area = radius * radius * Math.PI; 
					// Send area back to the client
					//outputToClient.write(1);

					//outputToClient.flush();
					//if (!input.equals(lastInput)) {
						Platform.runLater(() -> { 
						  	ta.appendText("username from client: " +
						  			 '\n'); 
						});
					//}
					//lastInput = input;
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
/*
public class ServerMain {
	
	protected static Map<String, User> users; // database of users
	protected static List<Chatroom> rooms; 	// chatroom at index 0 is global chat
	protected static TextArea info;			// server status info
	
	static ServerSocket server;
	
	public ServerMain(){
		try {
			int port = 4444;
			info = new TextArea();
			info.setEditable(false);
			info.appendText("<<< - Server established with port " + port + " - >>>\n");
			server = new ServerSocket(port, 100);
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public static void main(String[] args){
		new ServerMain(); // starts up the server
		users = new HashMap<String, User>();
		rooms = new ArrayList<Chatroom>();	
	}
	
}
*/
