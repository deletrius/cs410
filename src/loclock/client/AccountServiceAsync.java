package loclock.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import loclock.client.Account;
/**
 * Part of RPC structure that provides the bridge between the server and client side to allowed asynchronous calls
 **/

public interface AccountServiceAsync {
	/**
	 * @param requestUri  address to redirect user
	 * @param async       specifying the object type to be later returned by asynchronous callback
	 */
  public void login(String requestUri, AsyncCallback<Account> async);
}