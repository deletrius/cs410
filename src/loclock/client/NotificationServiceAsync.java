package loclock.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NotificationServiceAsync {

	void addNotification(String fromName, String toName, String content, String eventName,
			AsyncCallback<Void> callback);

	void getNotificationsByUsername(String userName,
			AsyncCallback<List<ArrayList<Object>>> callback);

	void removeNotification(String fromName, String toName,
			AsyncCallback<Void> callback);

	void addNotificationCalendar(String fromName, String toName,
			String content, String eventName, Date newStart,
			Date newEnd, AsyncCallback<Void> callback);

}
