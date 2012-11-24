package loclock.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

	private static final Logger LOG = Logger.getLogger(UserLocationServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	/* (non-Javadoc)
	 * @see loclock.client.MessageService#sendMessage(java.lang.String, java.lang.String, java.lang.String, java.util.Date)
	 */
	@Override
	public void sendMessage(String fromName, String toName, String messageBody,
			Date timestamp) throws NotLoggedInException {
		
		PersistenceManager pm=getPersistenceManager();
		pm.makePersistent(new Message(fromName, toName, messageBody, timestamp));
		pm.close();
		
	}

	/* (non-Javadoc)
	 * @see loclock.client.MessageService#retrieveMessage(java.lang.String)
	 */
	@Override
	public List<String[]> retrieveMessage(String fromName) throws NotLoggedInException {
		//PersistenceManager pm=PMF.getPersistenceManager();
		PersistenceManager pm=getPersistenceManager();
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
			 messageToStringArray[3]=Long.toString(i.getTimeStamp().getTime());
			 
			 result.add(messageToStringArray);
			 
			 //pm.currentTransaction().begin();
			 pm.deletePersistent(i);
			 //pm.currentTransaction().commit();
			 
		 }
		 Collections.sort(result,new Comparator<String[]> (){

			@Override
			public int compare(String[] arg0, String[] arg1) {
				if(Long.parseLong(arg0[3])<Long.parseLong(arg1[3]))
					return -1;
				else if (Long.parseLong(arg0[3])>Long.parseLong(arg1[3]))
					return 1;
				else 
					return 0;
			}});
		 
		 pm.close();
		 
		 
		return result;
	}

	/**
	 * Get the persistence manager.
	 * 
	 * @return the persistence manager
	 */
	private PersistenceManager getPersistenceManager() 
	{
		return PMF.getPersistenceManager();
	}
}
