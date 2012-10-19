package loclock.client;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import loclock.client.Account;
/**
 * Interface that specifies the AccountService (login) functionality to be implemented on the server side 
 **/

@RemoteServiceRelativePath("Account")
public interface AccountService extends RemoteService {
	/**
	 * Login using the account
	 * @param requestUri  address to redirect user
	 * @return Account    account that user logins with
	 **/
  public Account login(String requestUri);
}