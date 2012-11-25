package loclock.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import loclock.server.Calendar;
import loclock.server.PMF;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.smartgwt.client.widgets.calendar.CalendarEvent;

@RemoteServiceRelativePath("CalendarServlet")
public interface CalendarService extends RemoteService{
	
	/**
	 * Save an event to the user calendar.
	 * 
	 * @param userName the user name of the account
	 * @param eventName the name of the event
	 * @param description the description of the event
	 * @param startDate the start time of the event
	 * @param endDate the end time of the event
	 * @throws NotLoggedInException exception thrown if user no longer logged in
	 */
	public void saveEvent(String userName, String eventName,String description, Date startDate, Date endDate) throws NotLoggedInException;
	
	/**
	 * Get all the events belonged to a user
	 * 
	 * @param userName the user name of the account
	 * @return the list of events belong to the user
	 */	
	public List<ArrayList<Object>> getEventByUserName(String userName);
	
	/**
	 * Delete an event from the calendar.
	 * 
	 * @param userName the user name of the account
	 * @param eventName the name of the event
	 * @param description the description of the event
	 * @param startDate the start time of the event
	 * @param endDate the end time of the event
	 */
	public void deleteEvent(String userName, String eventName,String description, Date startDate, Date endDate);
	
	/**
	 * @param userName the user name of the account
	 * @param eventName the name of the event
	 * @param description the description of the event
	 * @param startDate the start time of the event
	 * @param endDate the end time of the event
	 * @return 1 if duplicate, 0 if not duplicate
	 */
	public String checkDuplicate(String userName, String eventName,String description, Date startDate, Date endDate);
	
	/**
	 * Get the list of events taking place on the current day.
	 * 
	 * @param userName the user name of the account
	 * @return the list of event for the current date
	 */
	public List<ArrayList<Object>> getCalendarEventsForTodayByUsername(String userName);
	
	/**
	 * Check if the passed hour argument is within the time range
	 * 
	 * @param hour the specified time for checking
	 * @param amPm am or pm
	 * @param start start time of the event
	 * @param end end time of the event
	 * @return true if it is within the range, false if not
	 */
	public boolean isWithinRange(String hour, String amPm, Date start, Date end);
}

