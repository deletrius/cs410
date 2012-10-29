package loclock.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NotificationServiceAsync {

	void addNotification(String fromName, String toName, String content,
			AsyncCallback<Void> callback);

	void getNotificationsByUsername(String userName,
			AsyncCallback<List<String>> callback);

	void removeNotification(String fromName, String toName,
			AsyncCallback<Void> callback);

}
