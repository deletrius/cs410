package loclock.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("message")
public interface MessageService extends RemoteService{	
		public void sendMessage(String fromName, String toName, String messageBody, Date timestamp) throws NotLoggedInException;
		public void retrieveMessage(String fromName) throws NotLoggedInException;	
}
