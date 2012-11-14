package loclock.server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
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
	
	/**
	 * 
	 */
	public Notification() {
		Date date = new Date();
		notificationCreationDate = date;
	}
	
	/**
	 * @param fromUser
	 * @param toUser
	 * @param content
	 */
	public Notification(String fromUser, String toUser, String content, String eventName) {
		this();
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.content = content;
		this.eventName = eventName;
	}

	/**
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return
	 */
	public String getFromUser() {
		return fromUser;
	}

	/**
	 * @param fromUser
	 */
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	/**
	 * @return
	 */
	public String getToUser() {
		return toUser;
	}

	/**
	 * @param toUser
	 */
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	/**
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * @param eventName
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * @return
	 */
	public Date getNotificationCreationDate() {
		return notificationCreationDate;
	}
	
	/**
	 * @return
	 */
	public String getNotificationCreationDateAsReadable(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return dateFormat.format(notificationCreationDate);
	}

	/**
	 * @param notificationCreationDate
	 */
	public void setNotificationCreationDate(Date notificationCreationDate) {
		this.notificationCreationDate = notificationCreationDate;
	}
}
