package loclock.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("user")
public interface UserLocationService extends RemoteService {
	public void addUser(String name) throws NotLoggedInException;
	public void removeUser(String name) throws NotLoggedInException;
	public String[] getUsers() throws NotLoggedInException;
	public void updateUserLatLng(String username, String lat, String lng) throws NotLoggedInException;
	public List<ArrayList<Object>> getUsersAsArrayList() throws NotLoggedInException; 
	public String getUserNameByID(String username) throws NotLoggedInException;
	public Double getUserLatitude(String username) throws NotLoggedInException;
	public Double getUserLongitude(String username) throws NotLoggedInException;
}
