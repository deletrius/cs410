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
	 * @param oldStartDate the old start date of the event
	 * @param newStartDate the new start date of the event
	 * @param oldEndDate the old end date of the event
	 * @param newendDate the new end date of the event
	 */
	public NotificationCalendar(String fromUser, String toUser, 
			String content, String eventName, 
			Date newStartDate, Date newendDate, String type) {
		super(fromUser, toUser, content, eventName, type);
		this.newStartDate = newStartDate;
		this.newendDate = newendDate;
	}

	/**
	 * Get the new start date of the event.
	 * 
	 * @return newStartDate the new start date of the event
	 */
	public Date getNewStartDate() {
		return newStartDate;
	}

	/**
	 * Set for new start date of the event.
	 * 
	 * @param newStartDate the new start date of the event
	 */
	public void setNewStartDate(Date newStartDate) {
		this.newStartDate = newStartDate;
	}

	/**
	 * Get the new end date of the event.
	 * 
	 * @return the new end date of the event
	 */
	public Date getNewendDate() {
		return newendDate;
	}

	/**
	 * Set for new end date of the event.
	 * 
	 * @param newendDate the new end date of the event
	 */
	public void setNewendDate(Date newendDate) {
		this.newendDate = newendDate;
	}
}
