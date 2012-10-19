package loclock.client;


import java.io.Serializable;

/**
 * Account represents an account with the account information.
 **/

public class Account implements Serializable {
	
	private boolean loggedIn = false;
	private String loginUrl;
	private String logoutUrl;
	private String emailAddress;
	private String nickname;
	
	/**
	 * Returns whether user is logged in (false on default)
	 * @return true  if user is logged in
	 */
	public boolean isLoggedIn(){
		return loggedIn;
	}

	/**
	 * Set user to logged in
	 * @param loggedIn  set isLoggedIn to false
	 */
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	/**
	 * Gets the login  URL
	 * @return loginUrl   URL of the login page
	 */
	public String getLoginUrl() {
		return loginUrl;
	}

	/**
	 * Sets the login URL
	 * @param loginUrl  URL of the login page
	 */
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	/**
	 * Gets the logout URL
	 * @return logoutUrl   URL of the logout page
	 */
	public String getLogoutUrl() {
		return logoutUrl;
	}

	/**
	 * Sets the logout URL
	 * @param logoutUrl   URL of the logout page
	 */
	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	/**
	 * Gets the user's email address
	 * @return emailAddress   the user's email address
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Sets the user's email address
	 * @param emailAddress  the user's email address
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * Gets the user's nickname
	 * @return nickname  user's nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Sets the user's nickname
	 * @param nickname  user's nickname
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
