package loclock.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import loclock.client.NotLoggedInException;
import loclock.client.SubscriptionService;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SubscriptionServiceImpl extends RemoteServiceServlet implements
SubscriptionService {

	/* (non-Javadoc)
	 * @see loclock.client.SubscriptionService#sendInvitation(java.lang.String, java.lang.String)
	 */
	public void sendInvitation(String senderName, String receiverName) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//Request request = new Request(senderName, receiverName);
		Subscription subscription;
		try{
			subscription = pm.getObjectById(Subscription.class,receiverName);			
			subscription.receiveRequest(senderName);			
			
		}
		catch (JDOObjectNotFoundException e)
		{
			subscription= new Subscription(receiverName);
			subscription.receiveRequest(senderName);
			pm.makePersistent(subscription);			
		}
		finally{			
			pm.close();
		}
	}
	
	// two way update
	/* (non-Javadoc)
	 * @see loclock.client.SubscriptionService#acceptInvitation(java.lang.String, java.lang.String)
	 */
	public void acceptInvitation(String senderName, String receiverName){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//Request request = new Request(senderName, receiverName);
		Subscription subscription1;
		Subscription subscription2;
		try{
			try{

				subscription1 = pm.getObjectById(Subscription.class,senderName);			
				subscription1.acceptRequest(receiverName);			

				subscription2 = pm.getObjectById(Subscription.class,receiverName);
				
				if (!subscription2.getEmailAddress().equals(subscription1.getEmailAddress()))
					subscription2.acceptRequest(senderName);	
				pm.makePersistent(subscription2);	
			}
			catch (JDOObjectNotFoundException e)
			{
				subscription2= new Subscription(receiverName);
				subscription2.receiveRequest(senderName);
				subscription2.acceptRequest(senderName);	
				pm.makePersistent(subscription2);			
			}
						
		}
		finally{			
			pm.close();
		}
	}

	/* (non-Javadoc)
	 * @see loclock.client.SubscriptionService#rejectInvitation(java.lang.String, java.lang.String)
	 */
	public void rejectInvitation(String senderName, String receiverName){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//Request request = new Request(senderName, receiverName);
		Subscription subscription;
		try{
			subscription = pm.getObjectById(Subscription.class,senderName);			
			subscription.rejectRequest(receiverName);			
			
		}
//		catch (JDOObjectNotFoundException e)
//		{
//			//Do nothing not supposed to happen			
//		}
		finally{			
			pm.close();
		}
	}

	/* (non-Javadoc)
	 * @see loclock.client.SubscriptionService#removeFriend(java.lang.String, java.lang.String)
	 */
	public void removeFriend(String senderName, String receiverName){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//Request request = new Request(senderName, receiverName);
		Subscription subscription1;
		Subscription subscription2;
		try{
			subscription1 = pm.getObjectById(Subscription.class,senderName);			
			subscription1.removeFriend(receiverName);
			
			subscription2 = pm.getObjectById(Subscription.class, receiverName);
			subscription2.removeFriend(senderName);
			
		}
		catch (JDOObjectNotFoundException e)
		{
			//Do nothing means already deleted		
		}
		finally{			
			pm.close();
		}
	}
	
	/* (non-Javadoc)
	 * @see loclock.client.SubscriptionService#areFriends(java.lang.String, java.lang.String)
	 */
	public boolean areFriends(String senderName, String receiverName)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
	    // because it is a two way subscription, its sufficient to just check one subscription list
		boolean result=false;
		Subscription subscription1;
		try{
			subscription1 = pm.getObjectById(Subscription.class,senderName);			
			result=(-1!=subscription1.findFriend(receiverName));			
		}
		catch (JDOObjectNotFoundException e)
		{
			//Do nothing means already deleted		
		}
		finally{			
			pm.close();
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see loclock.client.SubscriptionService#getInvitations(java.lang.String)
	 */
	public List<String> getInvitations(String user){
		ArrayList<String> friendList;
		ArrayList<String> types;
		ArrayList<String> usernames=new ArrayList<String>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//Request request = new Request(senderName, receiverName);
	    Subscription subscription;
		try{
			subscription = pm.getObjectById(Subscription.class,user);			
			friendList=subscription.getFriends();	
			types=subscription.getTypes();
			int i=0;
			while (i<friendList.size())
			{
				if (types.get(i).compareTo("request")==0)
				{
					usernames.add(friendList.get(i));
				}
				i++;
			}
		}
		catch (JDOObjectNotFoundException e)
		{
			//Do nothing not supposed to happen			
		}
		finally{			
			pm.close();
		}
		return usernames;
	}
	
	/* (non-Javadoc)
	 * @see loclock.client.SubscriptionService#getFriends(java.lang.String)
	 */
	public List<String> getFriends(String user){
		ArrayList<String> friendList;
		ArrayList<String> types;
		ArrayList<String> usernames=new ArrayList<String>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//Request request = new Request(senderName, receiverName);
	    Subscription subscription;
		try{
			subscription = pm.getObjectById(Subscription.class,user);			
			friendList=subscription.getFriends();	
			types=subscription.getTypes();
			int i=0;
			while (i<friendList.size())
			{
				if (types.get(i).compareTo("friend")==0)
				{
					usernames.add(friendList.get(i));
				}
				i++;
			}
		}
		catch (JDOObjectNotFoundException e)
		{
			//Do nothing not supposed to happen			
		}
		finally{			
			pm.close();
		}
		return usernames;
	}
	
	/* (non-Javadoc)
	 * @see loclock.client.SubscriptionService#getFriendsImages(java.lang.String)
	 */
	public List<String> getFriendsImages(String user){
		ArrayList<String> friendList;
		ArrayList<String> types;
		ArrayList<String> pics=new ArrayList<String>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//Request request = new Request(senderName, receiverName);
	    Subscription subscription;
		try{
			subscription = pm.getObjectById(Subscription.class,user);			
			friendList=subscription.getFriends();	
			types=subscription.getTypes();
			int i=0;
			while (i<friendList.size())
			{
				if (types.get(i).compareTo("friend")==0)
				{
					UserLocation userLocation=pm.getObjectById(UserLocation.class,friendList.get(i));
					pics.add(userLocation.getImage());
				}
				i++;
			}
		}
		catch (JDOObjectNotFoundException e)
		{
			//Do nothing not supposed to happen			
		}
		finally{			
			pm.close();
		}
		return pics;
	}
	
	/* (non-Javadoc)
	 * @see loclock.client.SubscriptionService#addSubscription(java.lang.String)
	 */
	public void addSubscription(String username) throws NotLoggedInException
	{
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try 
		{
			pm.getObjectById(Subscription.class, username);
		}
		catch (JDOObjectNotFoundException e)
		{
			try 
			{
				pm.makePersistent(new Subscription(username));
				System.out.println("new subscription object was created.");
			} 
			finally 
			{
				pm.close();
			}
		}
		finally 
		{
			pm.close();
		}
	}

	/**
	 * Check if user is logged in.
	 * 
	 * @throws NotLoggedInException
	 */
	private void checkLoggedIn() throws NotLoggedInException 
	{
		if (getCurrentUser() == null) {
			throw new NotLoggedInException("Not logged in.");
		}
	}

	/**
	 * Get the current user of the user service.
	 * 
	 * @return the current user of the user service
	 */
	private com.google.appengine.api.users.User getCurrentUser() 
	{
		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();
	}

	/**
	 * Get the persistence manager.
	 * 
	 * @return persistence manager
	 */
	private PersistenceManager getPersistenceManager() 
	{
		return PMF.get().getPersistenceManager();
	}
}
