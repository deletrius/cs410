package loclock.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import loclock.client.LocationService;
import loclock.client.NotLoggedInException;
import loclock.client.NotificationService;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.NoFixedFacet;

public class NotificationServiceImpl extends RemoteServiceServlet implements NotificationService {

	private static final Logger LOG = Logger.getLogger(LocationServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	@Override
	public void addNotification(String fromName, String toName, String content)
			throws NotLoggedInException 
	{
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try 
		{
			pm.makePersistent(new Notification(fromName, toName, content));
		} 
		finally 
		{
			pm.close();
		}
	}
	
	public void addUser(String username) throws NotLoggedInException 
	{
		
		checkLoggedIn();
		
		// Check if the user object already exists in the database. If so, do nothing,
		// if not, go ahead and add the user to the database.
		PersistenceManager pm = getPersistenceManager();
		try{
			pm.getObjectById(new User(getCurrentUser().getEmail()));
		}
		catch (JDOObjectNotFoundException e)
		{
			try 
			{
				pm.makePersistent(new User(getCurrentUser().getEmail()));
			} 
			finally 
			{
				pm.close();
			}
		}
	}

	@Override
	public void removeNotification(String fromName, String toName)
			throws NotLoggedInException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getNotificationsByUsername(String userName)
			throws NotLoggedInException 
	{
		List<Notification> notificationList = new ArrayList<Notification>();
		PersistenceManager pm = getPersistenceManager();
		Query q = pm.newQuery(Notification.class);
		q.declareParameters("String toUserParam");
		q.setFilter("toUser == toUserParam");
		q.setOrdering("content");
		try 
		{
			notificationList = (List<Notification>) q.execute(userName);

			List<String> notificationContents = new ArrayList<String>();

			for (Notification notificationObj : notificationList) 
			{
				notificationContents.add(notificationObj.getContent());
			}

			return notificationContents;
		} finally {
			pm.close();
		}
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
