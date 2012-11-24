package loclock.server;

import java.io.Serializable;
import java.util.List;
import java.util.Queue;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

public class Request implements Serializable {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String sender;
	
	@Persistent
	private String receiver;
	
	/**
	 * Class constructor of Request.
	 * 
	 * @param receiverName the name of the user being requested
	 * @param senderName the name of the request sender
	 */
	public Request(String receiverName, String senderName){
	    receiver = receiverName;
	    sender = senderName;
	}
	
	/**
	 * Get the identifier of the invitation.
	 * 
	 * @return the unique ID of the request
	 */
	public long getInvitationID(){
		return id;
	}
	
	/**
	 * Get the name of the sender of the request.
	 * 
	 * @return sender the sender of the request
	 */
	public String getSender(){
		return sender;
	}
	
	/**
	 * Get the name of the receiver of the request.
	 * 
	 * @return receiver the name of the receiver of the request
	 */
	public String getReceiver(){
		return receiver;
	}

}
