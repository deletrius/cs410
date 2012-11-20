package loclock.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import loclock.client.SubscriptionService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SubscriptionServiceImpl extends RemoteServiceServlet implements
SubscriptionService {

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
	public void acceptInvitation(String senderName, String receiverName){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//Request request = new Request(senderName, receiverName);
		Subscription subscription1;
		Subscription subscription2;
		try{
			try{

				subscription1 = pm.getObjectById(Subscription.class,senderName);			
				subscription1.acceptRequest(receiverName);			

			}
			catch (JDOObjectNotFoundException e)
			{
				//Do nothing not supposed to happen			
			}
			
			// update the other side's friend list
			try{
				subscription2 = pm.getObjectById(Subscription.class,receiverName);
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
}
