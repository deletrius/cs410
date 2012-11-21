package loclock.server;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/*
 * Calendar notification class extends the notification class
 * so that we can add more specific attributes and methods.
 * */
//@PersistenceCapable(identityType = IdentityType.APPLICATION)
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class NotificationCalendar extends Notification{
	// Modified new start time/date
	@Persistent
	private Date newStartDate;
	
	// Modified new end time/date
	@Persistent
	private Date newendDate;

	/**
	 * Constructor for new calendar notification object.
	 * 
	 * @param oldStartDate
	 * @param newStartDate
	 * @param oldEndDate
	 * @param newendDate
	 */
	public NotificationCalendar(String fromUser, String toUser, 
			String content, String eventName, 
			Date newStartDate, Date newendDate) {
		super(fromUser, toUser, content, eventName);
		this.newStartDate = newStartDate;
		this.newendDate = newendDate;
	}

	/**
	 * Getter for new start date.
	 * 
	 * @return
	 */
	public Date getNewStartDate() {
		return newStartDate;
	}

	/**
	 * Setter for new start date.
	 * 
	 * @param newStartDate
	 */
	public void setNewStartDate(Date newStartDate) {
		this.newStartDate = newStartDate;
	}

	/**
	 * Getter for new end date.
	 * 
	 * @return
	 */
	public Date getNewendDate() {
		return newendDate;
	}

	/**
	 * Setter for new end date.
	 * 
	 * @param newendDate
	 */
	public void setNewendDate(Date newendDate) {
		this.newendDate = newendDate;
	}
}
