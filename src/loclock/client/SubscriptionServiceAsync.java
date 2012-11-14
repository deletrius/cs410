package loclock.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import loclock.server.Request;

public interface SubscriptionServiceAsync {
	 void sendInvitation(String senderName, String receiverName, AsyncCallback<Void> async);
	 void acceptInvitation(String senderName, String receiverName, AsyncCallback<Void> async);
	 void rejectInvitation(String senderName, String receiverName, AsyncCallback<Void> async);
	 void getInvitations(String senderName, AsyncCallback<List<String>> async);
	 void getFriends(String senderName, AsyncCallback<List<String>> async);
}
