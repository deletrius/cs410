package loclock.server;

import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import loclock.client.LocationService;
import loclock.client.NotLoggedInException;
import loclock.client.NotificationService;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class NotificationServiceImpl extends RemoteServiceServlet implements NotificationService {

	private static final Logger LOG = Logger.getLogger(LocationServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	@Override
	public void addNotification(String fromName, String toName)
			throws NotLoggedInException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeNotification(String fromName, String toName)
			throws NotLoggedInException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getNotificationsByUsername(String userName)
			throws NotLoggedInException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void checkLoggedIn() throws NotLoggedInException 
	{
		if (getCurrentUser() == null) {
			throw new NotLoggedInException("Not logged in.");
		}
	}

	private com.google.appengine.api.users.User getCurrentUser() 
	{
		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();
	}

	private PersistenceManager getPersistenceManager() 
	{
		return PMF.getPersistenceManager();
	}

}
