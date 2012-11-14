package loclock.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("notification")
public interface NotificationService extends RemoteService {
	public void addNotification(String fromName, String toName, String content, String eventName) throws NotLoggedInException;
	public void addNotificationCalendar(String fromName, String toName, String content, String eventName, Date newStart, Date newEnd) throws NotLoggedInException;
	public void removeNotification(String fromName, String toName) throws NotLoggedInException;
	public List<String> getNotificationsByUsername(String userName) throws NotLoggedInException;
}
