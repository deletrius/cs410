package loclock.client;

	import java.util.List;

	import com.google.gwt.user.client.rpc.RemoteService;
	import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

	@RemoteServiceRelativePath("request")
	public interface RequestService extends RemoteService {
		public void sendInvitation(String senderName, String receiverName);
		public void acceptInvitation(String senderName, String receiverName);
		public void rejectInvitation(String senderName, String receiverName);
		public List<String> getInvitations(String senderName);
	}


