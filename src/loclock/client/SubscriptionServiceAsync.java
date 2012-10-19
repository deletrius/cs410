package loclock.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SubscriptionServiceAsync {
	 void addFriend(String friendName, String requesterEmail, AsyncCallback<Void> async);
	 void removeFriend(String friendName, String requesterEmail, AsyncCallback<Void> async);
	 void getFriends(String requesterEmail, AsyncCallback<String[]> async);
}
