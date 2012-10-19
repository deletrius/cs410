package loclock.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import loclock.server.Request;

public interface RequestServiceAsync {
	 void sendInvitation(String senderName, String receiverName, AsyncCallback<Void> async);
	 void acceptInvitation(String senderName, String receiverName, AsyncCallback<Void> async);
	 void rejectInvitation(String senderName, String receiverName, AsyncCallback<Void> async);
	 void getInvitations(String senderName, AsyncCallback<List<String>> async);
}
