package loclock.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import loclock.client.SubscriptionService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SubscriptionServiceImpl {
	
	public void addFriend(String friendName, String requesterEmail){
		
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//
//		Query q = pm.newQuery(Subscription.class, "emailAddress == requesterEmail");
//		q.declareParameters("String requesterName");
//		
//		try{
//			List<Subscription> results = (List<Subscription>) q.execute(requesterEmail);
//			Subscription sub = results.get(0);
//			sub.addFriend(friendName);
//		}
//		
//		finally{
//			q.closeAll();
//			pm.close();
//		}
		
	}
	
	public void removeFriend(String friendName, String requesterEmail){
		
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query q = pm.newQuery(Subscription.class, "emailAddress == requesterEmail");
		q.declareParameters("String requesterName");
		
		try{
			List<Subscription> results = (List<Subscription>) q.execute(requesterEmail);
			Subscription sub = results.get(0);
			sub.removeFriend(friendName);
		}
		
		finally{
			q.closeAll();
			pm.close();
		}
		
	}
	
	public String[] getFriends(String requesterEmail){
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Subscription.class, "emailAddress == requesterEmail");
		q.declareParameters("String pName");
		List<String> friendsReturned;
		Subscription sub;
		try {
			List<Subscription> results = (List<Subscription>) q.execute(requesterEmail);
			sub = results.get(0);
			friendsReturned = (List<String>)sub.getFriends();
		}
		finally {
		    q.closeAll();
			pm.close();
		}
		return (String[]) friendsReturned.toArray(new String[0]);

	}

}
