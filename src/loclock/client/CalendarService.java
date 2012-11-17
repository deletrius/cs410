package loclock.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import loclock.server.Calendar;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.smartgwt.client.widgets.calendar.CalendarEvent;
/**
 * 
 * 	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private int eventId;
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
	
	public Calendar(String userName, String eventName, String description,
			Date startDate, Date endDate) {
		super();
		this.userName = userName;
		this.eventName = eventName;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
	}
 *
 */
@RemoteServiceRelativePath("CalendarServlet")
public interface CalendarService extends RemoteService{
	public void saveEvent(String userName, String eventName,String description, Date startDate, Date endDate) throws NotLoggedInException;
	//public List<CalendarEvent> getEvent(String name);
	public List<ArrayList<Object>> getEventByUserName(String userName);
	public void deleteEvent(String userName, String eventName,String description, Date startDate, Date endDate);
}

