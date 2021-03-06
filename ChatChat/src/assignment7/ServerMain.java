/* ServerMain.java
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

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.function.BiConsumer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class ServerMain extends Application { 
	private Scanner scan;
	
	private static Map<String, User> allusers = new HashMap<>();
	ArrayList<Chatroom> allchats = new ArrayList<Chatroom>();
	private static Map<String, ArrayList<Message>> messages = new HashMap<>();
	ServerSocket serverSocket;
	

	private Map<String, User> buildUsers(){
		ArrayList<String> userList = new ArrayList<String>();
		scan = null;
		
		try {
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
			//System.out.println(auser);
			allusers.put(username, auser);
		}
			
		return allusers;
	}
	
	@Override
	public void stop(){
	    try {
			serverSocket.close();
		} catch (IOException e) {e.printStackTrace();}
	}
	
	// Text area for displaying contents 
	private TextArea ta = new TextArea(); 

	// Number a client 
	private int clientNo = 0; 

	@Override // Override the start method in the Application class 
	public void start(Stage primaryStage) { 
		
		buildUsers();

	
		messages.put("general", new ArrayList<Message>());
		// Create a scene and place it in the stage 
		Scene scene = new Scene(new ScrollPane(ta), 450, 200); 
		primaryStage.setTitle("MultiThreadServer"); // Set the stage title 
		primaryStage.setScene(scene); // Place the scene in the stage 
		primaryStage.show(); // Display the stage 
		new Thread( () -> { 
			try {  // Create a server socket 
				serverSocket = new ServerSocket(8000); 
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
				boolean logged = false;
				User credentials = null;

				while (logged == false) { 
			
					try {
						credentials = (User) inputFromClient.readObject();
						User currentUser = allusers.get(credentials.userName);
						if (currentUser != null && currentUser.password.equals(credentials.password)) {
							//System.out.println("Success!");
							credentials = currentUser;
							outputToClient.writeObject(currentUser);
							allusers.get(credentials.userName).status = true;

							logged = true;
						} else {
							outputToClient.writeObject(currentUser);
						}
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//System.out.println("Hoping for password: "+ currentUser.password);
				
					
					outputToClient.flush();
					
				}
				while (true) { 
					String room = allusers.get(credentials.userName).room;
			

			
					try {	
						String in = (String) inputFromClient.readObject();
						if (in.endsWith("+ROOMSWITCH+")) {
							//in.endsWith(suffix)
							String[] bits = in.split("+");
							allusers.get(credentials.userName).room = bits[0];
							
						} else if (in.equals("+REFRESH+")) {
							//System.out.println("Refreshing!");
							
							ArrayList<Message> fullRoom = messages.get(room);
							Integer c=1;
							if (fullRoom == null) {
								outputToClient.writeObject(null);

							} else {
							for (Message m: (ArrayList<Message>)fullRoom) {
								outputToClient.writeObject(m);
								//System.out.println(m.message+" at row:"+c++);
							}
							}
							outputToClient.writeObject(new Message(credentials,"+REFEND+"));

								
						} else if (in.equals("+USERLIST+")) {
							//System.out.println("Refreshing!");
							
							//ArrayList<Message> fullRoom = messages.get(room);
							Integer c=1;
							//outputToClient.writeObject(allusers);

							if (allusers == null) {
								outputToClient.writeObject(null);

							} else {
							//String k;
							//User v;
							for (Entry<String, User> u: allusers.entrySet()) {
								//System.out.println(u.getValue().status);

								outputToClient.writeObject(u.getValue());
								//System.out.println(m.message+" at row:"+c++);
							}
							}
							outputToClient.writeObject(new User("+USEREND+","","",""));


						} else{
						synchronized (messages) {
							Message msg = new Message(credentials,in);
							//System.out.println(credentials.avatar);
							//System.out.println(credentials.userName + ": "+ in);
							synchronized (allusers) {
								allusers.get(credentials.userName).status = true;
							}
							ta.appendText(credentials.userName + ": "+ in + '\n'); 
							ArrayList<Message> fullRoom = messages.get(room);
							if (fullRoom == null) {
								fullRoom = new ArrayList<Message>();
							}
						//	System.out.println("Adding: "+in);

							fullRoom.add(msg);
							messages.put(room, fullRoom);
						}
						}
						//outputToClient.flush();

						//outputToClient.writeObject(fullRoom);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					} catch (ClassCastException e) {
						
						}

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

