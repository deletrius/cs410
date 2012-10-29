package loclock.server;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType=IdentityType.APPLICATION)
public class Message {

		@PrimaryKey
		@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
		private Long id;
		
		@Persistent
		private String fromUser;
		
		@Persistent
		private String toUser;
		
		@Persistent
		private String messageBody;
		
		@Persistent
		private Date timestamp;
		
		public Message() {
			
		}
		
		public Message(String fromUser, String toUser, String messageBody, Date timestamp) {
			this();			
			this.fromUser = fromUser;
			this.toUser = toUser;
			this.messageBody = messageBody;
			this.timestamp = timestamp;
		}

		public Long getId() {
			return id;
		}

		public String getFromUser() {
			return fromUser;
		}

		public void setFromUser(String fromUser) {
			this.fromUser = fromUser;
		}
		
		public String getToUser() {
			return toUser;
		}

		public void setToUser(String toUser) {
			this.toUser = toUser;
		}
		
		public String getMessageBody() {
			return this.messageBody;
		}

		public Date getTimeStamp()	{
			return this.timestamp;
		}
	}



