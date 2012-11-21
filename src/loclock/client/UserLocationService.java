package loclock.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;

import loclock.server.UserLocation;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("userlocation")
public interface UserLocationService extends RemoteService {
	public void addUser(String name) throws NotLoggedInException;
	public void removeUser(String name) throws NotLoggedInException;
	public String[] getUsers() throws NotLoggedInException;
	public void updateUserLatLng(String username, String lat, String lng, Date lastupdate) throws NotLoggedInException;
	public void updateUserImage(String username, String url) throws NotLoggedInException;
	public List<ArrayList<Object>> getUsersAsArrayList(String userName) throws NotLoggedInException; 
	public String getUserNameByID(String username) throws NotLoggedInException;
//	public Double getUserLatitude(String username) throws NotLoggedInException;
//	public Double getUserLongitude(String username) throws NotLoggedInException;
	public ArrayList<String> getUserLocation(String username) throws NotLoggedInException;
	
	public void updateUserPrivacy(String username, String privacy) throws NotLoggedInException;
	
	public void updateUserFirstName(String username, String firstName) throws NotLoggedInException;
	
	public void updateUserLastName(String username, String lastName) throws NotLoggedInException;
}
