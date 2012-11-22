package loclock.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import loclock.client.UserLocationService;
import loclock.client.NotLoggedInException;
import loclock.client.NotificationService;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.NoFixedFacet;

public class NotificationServiceImpl extends RemoteServiceServlet implements NotificationService {

	private static final Logger LOG = Logger.getLogger(UserLocationServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	/* (non-Javadoc)
	 * @see loclock.client.NotificationService#addNotification(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addNotification(String fromName, String toName, String content, String eventName, String type)
			throws NotLoggedInException 
	{
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try 
		{
			pm.makePersistent(new Notification(fromName, toName, content, eventName, type));
		} 
		finally 
		{
			pm.close();
		}
	}
	
	/**
	 * @param username
	 * @throws NotLoggedInException
	 */
	public void addUser(String username) throws NotLoggedInException 
	{
		
		checkLoggedIn();
		
		// Check if the user object already exists in the database. If so, do nothing,
		// if not, go ahead and add the user to the database.
		PersistenceManager pm = getPersistenceManager();
		try{
			pm.getObjectById(new UserLocation(getCurrentUser().getEmail()));
		}
		catch (JDOObjectNotFoundException e)
		{
			try 
			{
				pm.makePersistent(new UserLocation(getCurrentUser().getEmail()));
			} 
			finally 
			{
				pm.close();
			}
		}
	}

	/* (non-Javadoc)
	 * @see loclock.client.NotificationService#removeNotification(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeNotification(String notificationId)
			throws NotLoggedInException 
	{
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try
		{
			NotificationCalendar toBeDeleted = pm.getObjectById(NotificationCalendar.class, Long.valueOf(notificationId));
			pm.deletePersistent(toBeDeleted);
		}
		finally
		{			
			pm.close();
		}
	}

	/* (non-Javadoc)
	 * @see loclock.client.NotificationService#getNotificationsByUsername(java.lang.String)
	 */
	@Override
	public List<ArrayList<Object>> getNotificationsByUsername(String userName)
			throws NotLoggedInException 
	{
		List<NotificationCalendar> notificationList = new ArrayList<NotificationCalendar>();
		PersistenceManager pm = getPersistenceManager();
		Query q = pm.newQuery(NotificationCalendar.class);
		q.declareParameters("String toUserParam");
		q.setFilter("toUser == toUserParam");
		q.setOrdering("content");
		try 
		{
			notificationList = (List<NotificationCalendar>) q.execute(userName);

//			List<String> notificationContents = new ArrayList<String>();
//
//			for (Notification notificationObj : notificationList) 
//			{
//				notificationContents.add(notificationObj.getContent());
//			}
//
//			return notificationContents;
			
			List<ArrayList<Object>> notificationAsList = new ArrayList<ArrayList<Object>>();
			
			for (NotificationCalendar notifiyObj : notificationList)
			{
				ArrayList<Object> calendarAttributes = new ArrayList<Object>();
				calendarAttributes.add(notifiyObj.getId().toString());
				calendarAttributes.add(notifiyObj.getEventName());
				calendarAttributes.add(notifiyObj.getContent());
				calendarAttributes.add(notifiyObj.getNewStartDate().toString());
				calendarAttributes.add(notifiyObj.getNewendDate().toString());
				calendarAttributes.add(notifiyObj.getFromUser());
				calendarAttributes.add(notifiyObj.getNotificationCreationDateAsReadable());	
				calendarAttributes.add(notifiyObj.getType());
				notificationAsList.add(calendarAttributes);
				
				
			}
			
			return notificationAsList;
			
			
			
		} finally {
			pm.close();
		}
	}
	
	/**
	 * @throws NotLoggedInException
	 */
	private void checkLoggedIn() throws NotLoggedInException 
	{
		if (getCurrentUser() == null) {
			throw new NotLoggedInException("Not logged in.");
		}
	}

	/**
	 * @return
	 */
	private com.google.appengine.api.users.User getCurrentUser() 
	{
		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();
	}

	/**
	 * @return
	 */
	private PersistenceManager getPersistenceManager() 
	{
		return PMF.getPersistenceManager();
	}

	/* (non-Javadoc)
	 * @see loclock.client.NotificationService#addNotificationCalendar(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public void addNotificationCalendar(String fromName, String toName,
			String content, String eventName, Date newStart, Date newEnd, String type)
			throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		ArrayList<String> friendList;
		ArrayList<String> types;
		ArrayList<String> usernames = new ArrayList<String>();
		Subscription subscription;

		try {
			if (toName.equalsIgnoreCase("broadcast")) {
				subscription = pm.getObjectById(Subscription.class, fromName);
				friendList = subscription.getFriends();
				types = subscription.getTypes();
				int i = 0;
				while (i < friendList.size()) {
					if (types.get(i).compareTo("friend") == 0) {
						usernames.add(friendList.get(i));
					}
					i++;
				}

				for (String friendEmail : usernames) {
					pm.makePersistent(new NotificationCalendar(fromName,
							friendEmail, content, eventName, newStart, newEnd, type));
				}

			} 
			else {
				pm.makePersistent(new NotificationCalendar(fromName, toName,
						content, eventName, newStart, newEnd, type));
			}
			// try {
		} finally {
			pm.close();
		}
	}
	

}
