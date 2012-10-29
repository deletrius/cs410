package loclock.client;

import java.util.Date;
import java.util.List;

import loclock.server.Message;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MessageServiceAsync {
	void sendMessage(String fromName, String toName, String messageBody, Date timestamp, AsyncCallback<Void> callback) throws NotLoggedInException;
	void retrieveMessage(String fromName, AsyncCallback<List<String[]>> callback) throws NotLoggedInException;	
}
