package loclock.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LocationServiceAsync {

	void addUser(String name, AsyncCallback<Void> callback);

	void removeUser(String name, AsyncCallback<Void> callback);

	void getUsers(AsyncCallback<String[]> callback);
	
	void updateUserLatLng(String username, String lat, String lng,
			AsyncCallback<Void> callback);
	
	void getUsersAsArrayList(AsyncCallback<List<ArrayList<Object>>> callback);

}
