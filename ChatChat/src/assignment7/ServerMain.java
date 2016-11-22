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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import assignment7.Client.User;

import javafx.scene.control.*;

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
