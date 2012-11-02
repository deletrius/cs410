package loclock.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import loclock.client.RequestService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RequestServiceImpl extends RemoteServiceServlet implements
RequestService {

	public void sendInvitation(String senderName, String receiverName) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//Request request = new Request(senderName, receiverName);
		Subscription subscription;
		try{
			subscription = (Subscription)pm.getObjectById(receiverName);
			subscription.addFriend(senderName,"request");			
		
		}
		catch (JDOObjectNotFoundException e)
		{
			subscription= new Subscription(receiverName);
			subscription.addFriend(senderName,"request");
			pm.makePersistent(subscription);			
		}
		finally{
			pm.close();
		}
	}

	public void acceptInvitation(String senderName, String receiverName){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			Query q = pm.newQuery(User.class, "userName == receiverName");
			q.declareParameters("String receiverName");
			List<User> users = (List<User>) q.execute(receiverName);
			User user = users.get(0);
			//user.addUser(senderName);
			q.closeAll();
			Query qq = pm.newQuery(Request.class, "receiver == sender");
			qq.declareParameters("String userName");
			List<Request> requests = (List<Request>) qq.execute(receiverName);
			for(Request req : requests){
				if(req.getSender().equals(receiverName)){
					pm.deletePersistent(req);
				}

			}
			qq.closeAll();
		}
		finally{
			pm.close();
		}

	}

	public void rejectInvitation(String senderName, String receiverName){
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try{
			Query q = pm.newQuery(Request.class, "senderName == receiverName");
			q.declareParameters("String receiverName");
			List<Request> requests = (List<Request>) q.execute(receiverName);
			for(Request req : requests){
				if(req.getSender().equals(receiverName)){
					pm.deletePersistent(req);
				}
			}
			Query qq = pm.newQuery(User.class, " == pName");
			qq.declareParameters("String pName");
			List<User> users = (List<User>) qq.execute(senderName);
			for(User user : users){
				//user.inviteRejected(receiverName);

			}

			qq.closeAll();
			q.closeAll();
		}
		finally{
			pm.close();
		}
	}




	public List<String> getInvitations(String receiverName){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<String> userNames = new ArrayList<String>();
		try {
			Query q = pm.newQuery(Request.class, "receiver == receiverName");
			q.declareParameters("String userName");
			List<Request> aRequests = (List<Request>) q.execute(receiverName);
			for (Request invit : aRequests) {
				userNames.add(invit.getSender());
			}
			q.closeAll();
		} finally {
			pm.close();
		}
		return userNames;
	}
}
