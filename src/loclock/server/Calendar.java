package loclock.server;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;






//@PersistenceCapable
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Calendar {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long eventId;
	@Persistent
	private String userName;
	@Persistent
	private String eventName;
	@Persistent
	private String description;
	@Persistent
	private Date startDate;
	@Persistent
	private Date endDate;
	@Persistent
	private boolean canEdit;
	
	public Calendar(){
		
	}
	
public Calendar(String userName, String eventName, String description,
			Date startDate, Date endDate) {
		super();
		this.userName = userName;
		this.eventName = eventName;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
	}


public Calendar(String userName, String eventName, String description,
		Date startDate, Date endDate, boolean canEdit) {
	super();
	this.userName = userName;
	this.eventName = eventName;
	this.description = description;
	this.startDate = startDate;
	this.endDate = endDate;
	this.canEdit = canEdit;
}

public String getUserName() {
	return userName;
}

public void setUserName(String userName) {
	this.userName = userName;
}

public String getEventName() {
	return eventName;
}

public void setEventName(String eventName) {
	this.eventName = eventName;
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

public Date getStartDate() {
	return startDate;
}

public void setStartDate(Date startDate) {
	this.startDate = startDate;
}

public Date getEndDate() {
	return endDate;
}

public void setEndDate(Date endDate) {
	this.endDate = endDate;
}

public boolean isCanEdit() {
	return canEdit;
}

public void setCanEdit(boolean canEdit) {
	this.canEdit = canEdit;
}








}
