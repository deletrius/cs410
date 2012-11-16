package loclock.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NotificationServiceAsync {

	/**
	 * @param fromName
	 * @param toName
	 * @param content
	 * @param eventName
	 * @param callback
	 */
	void addNotification(String fromName, String toName, String content, String eventName,
			AsyncCallback<Void> callback);

	/**
	 * @param userName
	 * @param callback
	 */
	void getNotificationsByUsername(String userName,
			AsyncCallback<List<ArrayList<Object>>> callback);

	/**
	 * @param fromName
	 * @param toName
	 * @param callback
	 */
	void removeNotification(String notificationId,
			AsyncCallback<Void> callback);

	/**
	 * @param fromName
	 * @param toName
	 * @param content
	 * @param eventName
	 * @param newStart
	 * @param newEnd
	 * @param callback
	 */
	void addNotificationCalendar(String fromName, String toName,
			String content, String eventName, Date newStart,
			Date newEnd, AsyncCallback<Void> callback);

}
