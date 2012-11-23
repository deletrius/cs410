package loclock.server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Inheritance(strategy=InheritanceStrategy.SUBCLASS_TABLE)
public class Notification {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String fromUser;
	
	@Persistent
	private String toUser;
	
	@Persistent
	private String content;
	
	@Persistent
	private String eventName;
	
	@Persistent
	private Date notificationCreationDate;
	
	@Persistent
	private Integer hasBeenPublishedToUser;
	
	@Persistent
	private String type;
	
	/**
	 * Class constructor of Notification
	 * 
	 */
	public Notification() {
		Date date = new Date();
		notificationCreationDate = date;
		hasBeenPublishedToUser = 0;
	}
	
	/**
	 * Class constructor of Notification
	 * 
	 * @param fromUser the name of of the user who owns the notification
	 * @param toUser the name of the user who reads the notification
	 * @param content the content of the notification
	 */
	public Notification(String fromUser, String toUser, String content, String eventName, String type) {
		this();
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.content = content;
		this.eventName = eventName;
		this.type = type;
	}

	/**
	 * Get the identifier of the request
	 * 
	 * @return id the id of the notification
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Get the username of the owner of the notification content.
	 * 
	 * @return fromUser the name of the owner of the notification content
	 */
	public String getFromUser() {
		return fromUser;
	}

	/**
	 * Set the owner of the notification content.
	 * 
	 * @param fromUser the name of the notification content owner
	 */
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	/**
	 * Get the username of the notification reader.
	 * 
	 * @return the name of the notification reader
	 */
	public String getToUser() {
		return toUser;
	}

	/**
	 * Set the reader of the notification content.
	 * 
	 * @param toUser the name of the reader of the notification content
	 */
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	/**
	 * Get the content of the notification
	 * 
	 * @return the content of the notification
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Set the content of the notification.
	 * 
	 * @param content the content of the notification
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Get the name of the event.
	 * 
	 * @return eventName the name of the event
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * Set the name of the event.
	 * 
	 * @param eventName
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * Get the date of the notification creation.
	 * 
	 * @return the date of creation
	 */
	public Date getNotificationCreationDate() {
		return notificationCreationDate;
	}
	
	/**
	 * Get the date of the notification creation in date format.
	 * 
	 * @return the date of notification in the date format
	 */
	public String getNotificationCreationDateAsReadable(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return dateFormat.format(notificationCreationDate);
	}

	/**
	 * Set the creation date of the notification.
	 * 
	 * @param notificationCreationDate the creation date of the notification
	 */
	public void setNotificationCreationDate(Date notificationCreationDate) {
		this.notificationCreationDate = notificationCreationDate;
	}

	/**
	 * Get the type of the notification.
	 * 
	 * @return type the type of the notification
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set the type of the notification.
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
}
