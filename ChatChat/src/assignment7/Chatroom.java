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

import java.sql.Timestamp;
import java.util.List;

import assignment7.Client.User;

public class Chatroom {
	
	private List<User> members;
	private List<Message> messages;
	//private int index; // like a chatroom id; used in servermain
	
	public class Message {
		User sender;
		String message;
		Timestamp timestamp;
		
		/**
		 * Message constructor
		 * Called when client presses enter to send a message to a chatroom
		 * 
		 * @param s sender
		 * @param m message
		 * @param time timestamp
		 */
		
		public Message(User s, String m, Timestamp time){
			sender = s;
			message = m;
			timestamp = time;
		}
	}	
	
}
