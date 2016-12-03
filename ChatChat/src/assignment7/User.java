/* User.java
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

	protected String userName;		// username(cannot change)(key)
	protected String password;		// password for logging in (changable)
	protected String displayName;	// displayed name (changable)
	protected String avatar;			// icon (changable)
	protected String room = "general";		// Current Chatroom
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
