package loclock.server;


import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import org.apache.commons.collections.CollectionUtils;
import org.datanucleus.exceptions.NucleusObjectNotFoundException;

import loclock.client.MainServices;
import loclock.client.UserLocationService;
import loclock.client.NotLoggedInException;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
//import com.google.gwt.dev.util.Pair;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class UserLocationServiceImpl extends RemoteServiceServlet implements UserLocationService 
{

	private static final Logger LOG = Logger.getLogger(UserLocationServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");

	/* (non-Javadoc)
	 * @see loclock.client.UserLocationService#addUser(java.lang.String)
	 */
	public void addUser(String username) throws NotLoggedInException 
	{

		checkLoggedIn();

		// Check if the user object already exists in the database. If so, do nothing,
		// if not, go ahead and add the user to the database.
		PersistenceManager pm = getPersistenceManager();
		try{
			pm.getObjectById(UserLocation.class,username);
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
		finally {
			pm.close();
		}
	}

	/* (non-Javadoc)
	 * @see loclock.client.UserLocationService#removeUser(java.lang.String)
	 */
	public void removeUser(String username) throws NotLoggedInException 
	{
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			long deleteCount = 0;
			Query q = pm.newQuery(UserLocation.class, "userName == u");
			q.declareParameters("loclock.server.User u");
			List<UserLocation> users = (List<UserLocation>) q.execute(getCurrentUser()
					.getEmail());
			for (UserLocation user : users) {
				if (username.equals(user.getUserName())) {
					deleteCount++;
					pm.deletePersistent(user);
				}
			}
			if (deleteCount != 1) {
				LOG.log(Level.WARNING, "removeUser deleted " + deleteCount
						+ " Users");
			}
		} finally {
			pm.close();
		}
	}

	public String[] getUsers() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<String> usersList = new ArrayList<String>();
		try 
		{
			Query q = pm.newQuery(UserLocation.class);
			//q.declareParameters("loclock.server.User u");
			q.setOrdering("userName");
			List<UserLocation> users = (List<UserLocation>) q.execute();
			for (UserLocation user : users) {
				usersList.add(user.getUserName());
			}
			
			
		} 
		
		finally 
		{
			pm.close();
		}
		
		return (String[]) usersList.toArray(new String[0]);
	}
	
	/* (non-Javadoc)
	 * @see loclock.client.UserLocationService#getUsers()
	 */
	public ArrayList<String> getNearNPublicUsers(String user, int n, double lat, double lon) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		ArrayList<String> usersList = new ArrayList<String>();
		ArrayList<String> resultUserList = new ArrayList<String>();
		
		try 
		{
			Query q = pm.newQuery(UserLocation.class);
			//q.declareParameters("loclock.server.User u");
			q.setOrdering("userName");
			List<UserLocation> users = (List<UserLocation>) q.execute();
			
			for (UserLocation i : users) {
				usersList.add(i.getUserName());
			}

			q.closeAll();
			pm.close();
			
			ArrayList<String> friendList;
			ArrayList<String> types;
			ArrayList<String> usernames=new ArrayList<String>();
			
			pm = getPersistenceManager();
			//Request request = new Request(senderName, receiverName);
			Subscription subscription;

			subscription = pm.getObjectById(Subscription.class,user);			
			
			friendList=subscription.getFriends();	
			types=subscription.getTypes();
			
			for (int i=0;i<friendList.size();i++)
			{
				if (types.get(i).compareTo("friend")==0)
				{
					usernames.add(friendList.get(i));
				}				
			}
			pm.close();
			// filter : public - friends
			usernames.add(user);
			usersList.removeAll(usernames);
			pm=getPersistenceManager();
			
			final ArrayList<Pair> namesWithDistances=new ArrayList<Pair>();
			for (String i:usersList)
			{
				UserLocation userloc=pm.getObjectById(UserLocation.class,i);
								
				double lat1=lat;
				double lon1=lon;
				//Window.alert("Profile Lon: "+result);
				double lat2=Double.parseDouble(userloc.getLatitude());
				double lon2=Double.parseDouble(userloc.getLongitude());
				//Window.alert("User lat:"+lat2+"  User Long: "+lon2);
				int R = 6371; // km
				double dLat = Math.toRadians(lat2-lat1);
				double dLon = Math.toRadians(lon2-lon1);
				double lati1 = Math.toRadians(lat1);
				double lati2 = Math.toRadians(lat2);

				double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
						Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lati1) * Math.cos(lati2); 
				double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
				double distance = R * c;

				
				namesWithDistances.add(new Pair(i, distance));
			}
			Collections.sort(namesWithDistances,new Comparator<Pair>(){

				@Override
				public int compare(Pair arg0,
						Pair arg1) {
					if (arg0.getRight()<arg1.getRight())
						return -1;
					else if (arg0.getRight()>arg1.getRight())
						return 1;
					return 0;
				}});

			for (int i=0;i<n&&i<namesWithDistances.size();i++)
			{
				resultUserList.add(namesWithDistances.get(i).getLeft());
			}
		} 

		finally 
		{
			pm.close();
		}

		return resultUserList;
	}

	private class Pair
	{
		String left;
		Double right;
		public Pair(String left,Double right)
		{
			this.left=left;
			this.right=right;
		}
		public String getLeft()
		{
			return left;
		}
		public Double getRight()
		{
			return right;
		}
	}
	/* (non-Javadoc)
	 * @see loclock.client.UserLocationService#updateUserLatLng(java.lang.String, java.lang.String, java.lang.String, java.util.Date)
	 */
	public void updateUserLatLng(String username, String lat, String lng, Date lastupdate) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			UserLocation user = pm.getObjectById(UserLocation.class, username);
			user.setLatitude(lat);
			user.setLongitude(lng);
			user.setLastUpdate(lastupdate);
		} finally {
			pm.close();
		}
	}

	/* (non-Javadoc)
	 * @see loclock.client.UserLocationService#updateUserImage(java.lang.String, java.lang.String)
	 */
	public void updateUserImage(String username, String url) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			UserLocation user = pm.getObjectById(UserLocation.class, username);
			user.setImage(url);
		} finally {
			pm.close();
		}
	}

	/* (non-Javadoc)
	 * @see loclock.client.UserLocationService#updateUserPrivacy(java.lang.String, java.lang.String)
	 */
	public void updateUserPrivacy(String username, String privacy) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			UserLocation user = pm.getObjectById(UserLocation.class, username);
			user.setPrivacy(privacy);
			pm.makePersistent(user);
		} finally {
			pm.close();
		}
	}

	/* (non-Javadoc)
	 * @see loclock.client.UserLocationService#updateUserFirstName(java.lang.String, java.lang.String)
	 */
	public void updateUserFirstName(String username, String firstName) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			UserLocation user = pm.getObjectById(UserLocation.class, username);
			user.setFirstName(firstName);
		} finally {
			pm.close();
		}
	}

	/* (non-Javadoc)
	 * @see loclock.client.UserLocationService#updateUserLastName(java.lang.String, java.lang.String)
	 */
	public void updateUserLastName(String username, String lastName) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			UserLocation user = pm.getObjectById(UserLocation.class, username);
			user.setLastName(lastName);
		} finally {
			pm.close();
		}
	}

	/* (non-Javadoc)
	 * @see loclock.client.UserLocationService#getUserNameByID(java.lang.String)
	 */
	public String getUserNameByID (String username) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try{
			UserLocation user = pm.getObjectById(UserLocation.class, username);
			return user.getUserName();
		}
		catch (JDOObjectNotFoundException e)
		{
			return null;
		}		
		finally {
			pm.close();
		}
	}

	/* (non-Javadoc)
	 * @see loclock.client.UserLocationService#getUsersAsArrayList(java.lang.String)
	 */
	public List<ArrayList<Object>> getUsersAsArrayList(String userName) throws NotLoggedInException {
		checkLoggedIn(); 
		List<UserLocation> userList = new ArrayList<UserLocation>();
		PersistenceManager pm = getPersistenceManager();
		try {
			//			Query q = pm.newQuery(UserLocation.class);
			Query q = pm.newQuery(UserLocation.class, "userName == u");
			q.declareParameters("String u");
			//q.declareParameters("loclock.server.User u");
			q.setOrdering("userName");
			userList = (List<UserLocation>) q.execute(userName);

			List<ArrayList<Object>> userAsList = new ArrayList<ArrayList<Object>>();

			for (UserLocation userObj : userList) {
				ArrayList<Object> userAttributes = new ArrayList<Object>();
				userAttributes.add(userObj.getUserName());
				userAttributes.add(userObj.getLatitude());
				userAttributes.add(userObj.getLongitude());
				userAttributes.add(userObj.getPrivacy());
				userAttributes.add(userObj.getFirstName());
				userAttributes.add(userObj.getLastName());
				userAsList.add(userAttributes);
			}

			return userAsList;
		} finally {
			pm.close();
		}
	}

	/**
	 * Check if the current user is logged in.
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
	 * Get the current user.
	 * 
	 * @return current user of userService
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
		return PMF.getPersistenceManager();
	}

	/* (non-Javadoc)
	 * @see loclock.client.UserLocationService#getUserLocation(java.lang.String)
	 */
	public ArrayList<String> getUserLocation(String userName) {
		PersistenceManager pm = getPersistenceManager();
		double lat = 0;
		ArrayList<String> result=new ArrayList<String> ();
		try {
			UserLocation userloc=pm.getObjectById(UserLocation.class,userName);
			result.add(userloc.getUserName());
			result.add(userloc.getLatitude());
			result.add(userloc.getLongitude());
			result.add(userloc.getLastUpdate().toString());	    
			result.add(userloc.getImage());
			result.add(userloc.getPrivacy());
		} 
		catch (JDOObjectNotFoundException e)
		{
			return null;
		}
		finally {	    	
			pm.close();
		}
		return result;
	}

}
