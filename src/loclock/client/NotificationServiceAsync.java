package loclock.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NotificationServiceAsync {

	void addNotification(String fromName, String toName,
			AsyncCallback<Void> callback);

	void getNotificationsByUsername(String userName,
			AsyncCallback<String[]> callback);

	void removeNotification(String fromName, String toName,
			AsyncCallback<Void> callback);

}
