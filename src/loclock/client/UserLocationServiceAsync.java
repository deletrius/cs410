package loclock.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserLocationServiceAsync {

	void addUser(String name, AsyncCallback<Void> callback);

	void removeUser(String name, AsyncCallback<Void> callback);

	void getUsers(AsyncCallback<String[]> callback);
	
	void updateUserLatLng(String username, String lat, String lng,
			AsyncCallback<Void> callback);
	
	void getUsersAsArrayList(AsyncCallback<List<ArrayList<Object>>> callback);

	void getUserNameByID(String username, AsyncCallback<String> callback);
	
	void getUserLatitude(String userName, AsyncCallback<Double> callback);

	void getUserLongitude(String userName, AsyncCallback<Double> callback);

}
