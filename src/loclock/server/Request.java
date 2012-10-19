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
	
	public Request(String receiverName, String senderName){
	    receiver = receiverName;
	    sender = senderName;
	}
	
	public long getInvitationID(){
		return id;
	}
	
	public String getSender(){
		return sender;
	}
	
	public String getReceiver(){
		return receiver;
	}

}
