package loclock.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("subscription")
public interface SubscriptionService {
		public void addFriend(String friendName, String requesterEmail);
		public void removeFriend(String friendName, String requesterEmail);
		public String[] getFriends(String requesterEmail);
	}
