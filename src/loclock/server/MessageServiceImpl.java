package loclock.server;

import java.util.Date;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

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
	public void retrieveMessage(String fromName) throws NotLoggedInException {
		// TODO Auto-generated method stub
		
	}

	private PersistenceManager getPersistenceManager() 
	{
		return PMF.getPersistenceManager();
	}
}
