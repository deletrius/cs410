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
		
		/**
		 * Basic class constructor of Message.
		 */
		public Message() {
			
		}
		
		/**
		 * Class constructor of Message.
		 * 
		 * @param fromUser the name of the message sender
		 * @param toUser the name of the message receiver
		 * @param messageBody the content of the message
		 * @param timestamp the time when the message is sent
		 */
		public Message(String fromUser, String toUser, String messageBody, Date timestamp) {
			this();			
			this.fromUser = fromUser;
			this.toUser = toUser;
			this.messageBody = messageBody;
			this.timestamp = timestamp;
		}

		/**
		 * Get the unique identifier of the message.
		 * 
		 * @return id the ID of the message
		 */
		public Long getId() {
			return id;
		}

		/**
		 * Get the name of the message sender.
		 * 
		 * @return from User the name of the message sender
		 */
		public String getFromUser() {
			return fromUser;
		}

		/**
		 * Set the name of the message sender.
		 * 
		 * @param fromUser the name of the message sender
		 */
		public void setFromUser(String fromUser) {
			this.fromUser = fromUser;
		}
		
		/**
		 * Get the name of the message receiver.
		 * 
		 * @return toUser the name of the message receiver
		 */
		public String getToUser() {
			return toUser;
		}

		/**
		 * Set the name of the message receiver.
		 * 
		 * @param toUser the name of the message receiver
		 */
		public void setToUser(String toUser) {
			this.toUser = toUser;
		}
		
		/**
		 * Get the message body.
		 * 
		 * @return messageBody the body of the message
		 */
		public String getMessageBody() {
			return this.messageBody;
		}

		/**
		 * Get the time when the message is sent.
		 * 
		 * @return the time when the message is sent
		 */
		public Date getTimeStamp()	{
			return this.timestamp;
		}
	}



