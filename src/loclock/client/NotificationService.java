package loclock.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("notification")
public interface NotificationService extends RemoteService {
	public void addNotification(String fromName, String toName) throws NotLoggedInException;
	public void removeNotification(String fromName, String toName) throws NotLoggedInException;
	public String[] getNotificationsByUsername(String userName) throws NotLoggedInException;
}
