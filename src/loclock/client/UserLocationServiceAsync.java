package loclock.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserLocationServiceAsync {

	void addUser(String name, AsyncCallback<Void> callback);

	void removeUser(String name, AsyncCallback<Void> callback);

	void getUsers(AsyncCallback<String[]> callback);
	
	void updateUserLatLng(String username, String lat, String lng, Date lastupdate,
			AsyncCallback<Void> callback);
	
	void updateUserImage(String username, String url,AsyncCallback<Void> callback);
	
	void getUsersAsArrayList(String userName, AsyncCallback<List<ArrayList<Object>>> callback);

	void getUserNameByID(String username, AsyncCallback<String> callback);
	
	//void getUserLatitude(String userName, AsyncCallback<Double> callback);

	//void getUserLongitude(String userName, AsyncCallback<Double> callback);
	void getUserLocation(String userName,AsyncCallback<ArrayList<String>> callback);

	void updateUserPrivacy(String username, String privacy,
			AsyncCallback<Void> callback);

	void updateUserFirstName(String username, String firstName,
			AsyncCallback<Void> callback);

	void updateUserLastName(String username, String lastName,
			AsyncCallback<Void> callback);
	
	
}
