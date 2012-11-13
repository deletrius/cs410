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
		Subscription subscription;
		try{
			try{

				subscription = pm.getObjectById(Subscription.class,senderName);			
				subscription.acceptRequest(receiverName);			

			}
			catch (JDOObjectNotFoundException e)
			{
				//Do nothing not supposed to happen			
			}
			// update the other side's friend list
			try{

				subscription = pm.getObjectById(Subscription.class,receiverName);			
						

			}
			catch (JDOObjectNotFoundException e)
			{
				subscription= new Subscription(receiverName);
				subscription.receiveRequest(senderName);
				subscription.acceptRequest(senderName);	
				pm.makePersistent(subscription);			
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
}
