package loclock.server;


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

import org.datanucleus.exceptions.NucleusObjectNotFoundException;

import loclock.client.UserLocationService;
import loclock.client.NotLoggedInException;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class UserLocationServiceImpl extends RemoteServiceServlet implements UserLocationService 
{
	
	private static final Logger LOG = Logger.getLogger(UserLocationServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
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
	
	public List<ArrayList<Object>> getUsersAsArrayList() throws NotLoggedInException {
		checkLoggedIn(); 
		List<UserLocation> userList = new ArrayList<UserLocation>();
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(UserLocation.class);
			//q.declareParameters("loclock.server.User u");
			q.setOrdering("userName");
			userList = (List<UserLocation>) q.execute();

			List<ArrayList<Object>> userAsList = new ArrayList<ArrayList<Object>>();

			for (UserLocation userObj : userList) {
				ArrayList<Object> userAttributes = new ArrayList<Object>();
				userAttributes.add(userObj.getUserName());
				userAttributes.add(userObj.getLatitude());
				userAttributes.add(userObj.getLongitude());
				userAsList.add(userAttributes);
			}

			return userAsList;
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

//	public void createUser(String userName,String realName, String age, String height,String weight,String faculty,String email,String yearLevel, String phoneNumber,Date timeRegistered,double lat, double log,LatLng location, String address){
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//    	try{
//			pm.makePersistent(new User(userName,realName,age,height,weight,faculty,email,yearLevel,phoneNumber,timeRegistered,lat,log,location,address));
//		}
//		finally{
//			pm.close();
//		}
//	}
//	public void deleteUser(String name) {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		  Query q = pm.newQuery(User.class);
//		  q.setFilter("schoolName == u");
//		  q.declareParameters("String u");
//		try {
//			  List<User> userSet = (List<User>) q.execute(name);
//		      for(User s : userSet){
//		    	  pm.deletePersistent(s);
//		      }
//		    } finally {
//		    	q.closeAll();
//		      pm.close();
//		    }
//	}
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
//	public Double getUserLatitude(String userName) {
//		PersistenceManager pm = getPersistenceManager();
//		double lat = 0;
//		
//		 Query q = pm.newQuery(UserLocation.class);
//		  q.setFilter("userName == u");
//		  q.declareParameters("String u");
//	    try {
//	    	System.out.println("lat is");
//			  List<UserLocation> users = (List<UserLocation>) q.execute(userName);
//			  for(UserLocation user : users){
//				  lat = (Double)Double.parseDouble(user.getLatitude());
//				 
//			  }
//	    } finally {
//	    	q.closeAll();
//	      pm.close();
//	    }
//		return lat;
//	}

//	public Double getUserLongitude(String userName) {
//		PersistenceManager pm = getPersistenceManager();
//		double log = 0;
//		
//		 Query q = pm.newQuery(UserLocation.class);
//		  q.setFilter("userName == u");
//		  q.declareParameters("String u");
//	    try {
//	    	System.out.println("log is");
//			  List<UserLocation> users = (List<UserLocation>) q.execute(userName);
//			  for(UserLocation user : users){
//				  log = (Double)Double.parseDouble(user.getLongitude());
//				 
//			  }
//	    } finally {
//	    	q.closeAll();
//	      pm.close();
//	    }
//		return log;
//	}
//	
//	public Date getUserLastUpdate(String userName) {
//		PersistenceManager pm = getPersistenceManager();
//		double log = 0;
//		
//		 Query q = pm.newQuery(UserLocation.class);
//		  q.setFilter("userName == u");
//		  q.declareParameters("String u");
//	    try {
//	    	System.out.println("log is");
//			  List<UserLocation> users = (List<UserLocation>) q.execute(userName);
//			  for(UserLocation user : users){
//				  log = (Double)Double.parseDouble(user.getLongitude());
//				 
//			  }
//	    } finally {
//	    	q.closeAll();
//	      pm.close();
//	    }
//		return log;
//	}

//	public List<List<String>> getAllUserInfo(){
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		List<List<String>> userInfos;
//		List<String> userNames;
//		Query q = pm.newQuery(User.class);
//		List<User> users = (List<User>) q.execute();
//		try{
//			userInfos = new ArrayList<List<String>>(users.size());
//			for(User user: users){
//				userNames = new ArrayList<String>();
//				userNames.add(user.getUserName());
//				userNames.add(user.getRealName());
//				userNames.add(user.getHeight());
//				userNames.add(user.getWeight());
//				userNames.add(user.getPhoneNumber());
//				userNames.add(user.getAddress());
//				userNames.add(user.getFaculty());
//				userNames.add(user.getYealLevel());
//				userNames.add(user.getRegisteredTime().toString());
//				userNames.add(String.valueOf(user.getLatitude()));
//				userNames.add(String.valueOf(user.getLongitude()));
//				userNames.add(user.getAddress());
//				userInfos.add(userNames);
//			}
//			return userInfos;
//		}
//		finally{
//			q.closeAll();
//			pm.close();
//		}		
//	}

}
