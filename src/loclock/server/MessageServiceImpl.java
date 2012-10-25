package loclock.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import loclock.client.MessageService;
import loclock.client.NotLoggedInException;
import loclock.client.NotificationService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MessageServiceImpl extends RemoteServiceServlet implements MessageService{

	private static final Logger LOG = Logger.getLogger(LocationServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	@Override
	public void sendMessage(String fromName, String toName, String messageBody,
			Date timestamp) throws NotLoggedInException {
		getPersistenceManager().makePersistent(new Message(fromName, toName, messageBody, timestamp));
		
	}

	@Override
	public List<String[]> retrieveMessage(String fromName) throws NotLoggedInException {
		PersistenceManager pm=PMF.getPersistenceManager();
		Query q = pm.newQuery(Message.class);
		 q.setFilter("toUser == u");
		 q.declareParameters("String u");
		 List<Message> incomingMessages = (List<Message>) q.execute(fromName);
		 // message schema: fromUser toUser messageBody timestamp 
		 List<String[]> result=new ArrayList<String[]>();
		 
		 for (Message i: incomingMessages)
		 {
			 String[] messageToStringArray=new String[4];
			 messageToStringArray[0]=i.getFromUser();
			 messageToStringArray[1]=i.getToUser();
			 messageToStringArray[2]=i.getMessageBody();
			 messageToStringArray[3]=i.getTimeStamp().toString();
			 result.add(messageToStringArray);
			 pm.deletePersistent(i);
		 }
		 pm.close();
		 
		 
		return result;
	}

	private PersistenceManager getPersistenceManager() 
	{
		return PMF.getPersistenceManager();
	}
}
