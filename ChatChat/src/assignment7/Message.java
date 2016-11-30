package assignment7;
import java.io.Serializable;


public class Message implements Serializable {
	User sender;
	String message;
	
		public Message(User s, String m){
			this.sender = s;
			this.message = m;
		}


}
