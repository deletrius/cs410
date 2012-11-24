package loclock.client;

import java.util.List;

import javax.jdo.PersistenceManager;

import com.google.gwt.user.client.rpc.AsyncCallback;
import loclock.server.Request;
import loclock.server.Subscription;

public interface SubscriptionServiceAsync {
	 void sendInvitation(String senderName, String receiverName, AsyncCallback<Void> async);
	 void acceptInvitation(String senderName, String receiverName, AsyncCallback<Void> async);
	 void rejectInvitation(String senderName, String receiverName, AsyncCallback<Void> async);
	 void removeFriend(String senderName, String receiverName, AsyncCallback<Void> async);
	 void areFriends(String senderName, String receiverName,AsyncCallback<Boolean> async);
	 void getInvitations(String senderName, AsyncCallback<List<String>> async);
	 void getFriends(String senderName, AsyncCallback<List<String>> async);
	 void getFriendsImages(String senderName, AsyncCallback<List<String>> async);
	 void addSubscription(String username, AsyncCallback<Void> async);
}
