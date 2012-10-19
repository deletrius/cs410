package loclock.server;


import java.util.List;

import loclock.client.Account;
import loclock.client.AccountService;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Implementation of log-in functionality
 */
public class AccountServiceImpl extends RemoteServiceServlet implements AccountService {

	/**
	 * Login functionality of the app
	 * @param  requestUri  
	 * @return  account   user account
	 */
	public Account login(String requestUri) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		Account account = new Account();
		if (user != null) {
			account.setLoggedIn(true);
			account.setEmailAddress(user.getEmail());
			account.setNickname(user.getNickname());
			account.setLogoutUrl(userService.createLogoutURL(requestUri));
		} else {
			account.setLoggedIn(false);
			account.setLoginUrl(userService.createLoginURL(requestUri));
		}
		return account;
	}

	
}
