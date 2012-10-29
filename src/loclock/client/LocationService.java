package loclock.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("user")
public interface LocationService extends RemoteService {
	public void addUser(String name) throws NotLoggedInException;
	public void removeUser(String name) throws NotLoggedInException;
	public String[] getUsers() throws NotLoggedInException;
	public void updateUserLatLng(String username, String lat, String lng) throws NotLoggedInException;
	public List<ArrayList<Object>> getUsersAsArrayList() throws NotLoggedInException; 
	public String getUserByID(String username) throws NotLoggedInException;
}
