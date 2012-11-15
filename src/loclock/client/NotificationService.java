package loclock.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("notification")
public interface NotificationService extends RemoteService {
	/**
	 * @param fromName
	 * @param toName
	 * @param content
	 * @param eventName
	 * @throws NotLoggedInException
	 */
	public void addNotification(String fromName, String toName, String content, String eventName) throws NotLoggedInException;
	
	/**
	 * Store a calendar modified event to be sent to friends of user.
	 * 
	 * @param fromName the user of the modified event
	 * @param toName the friend to send the notification to or use 'broadcast' to send to all friends
	 * @param content the content of the notification
	 * @param eventName the name of the event
	 * @param newStart the start time of the modified event
	 * @param newEnd the end time of the modified event
	 * @throws NotLoggedInException if the user is not currently logged in
	 */
	public void addNotificationCalendar(String fromName, String toName, String content, String eventName, Date newStart, Date newEnd) throws NotLoggedInException;
	/**
	 * @param fromName
	 * @param toName
	 * @throws NotLoggedInException
	 */
	public void removeNotification(String fromName, String toName) throws NotLoggedInException;
	/**
	 * @param userName
	 * @return
	 * @throws NotLoggedInException
	 */
	public List<ArrayList<Object>> getNotificationsByUsername(String userName) throws NotLoggedInException;
}
