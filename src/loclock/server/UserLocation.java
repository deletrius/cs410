package loclock.server;


import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdentityType;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UserLocation {
	
	@PrimaryKey
	@Persistent
	private String userName;
	
	// Have to use string else I get the error:
	// Type 'java.lang.Double' was not included in the set of types which can be serialized by this SerializationPolicy or its Class object could not be loaded. For security purposes, this type will not be serialized.: instance = 49.248523
	@Persistent
	private String latitude;

	@Persistent
	private String longitude;

	@Persistent(defaultFetchGroup="true")
	private Date lastupdate;
	
	@Persistent
	private String profileImageURL="http://images.wikia.com/civilization/images/c/cb/Yao-ming-meme.jpg";
	
	@Persistent
	private String privacy;
	
	@Persistent
	private String firstName;
	
	@Persistent
	private String lastName;
	
	/**
	 * Basic class constructor of UserLocation.
	 */
	public UserLocation() 
	{
		
	}
	
	/**
	 * Class constructor of UserLocation.
	 * 
	 * @param username the username of the account
	 */
	public UserLocation(String username)
	{
		this();
		this.userName = username;
		this.latitude = "-1.0";
		this.longitude = "-1.0";
		this.lastupdate=new Date();
		this.privacy = "private";
		this.firstName = "";
		this.lastName = "";
	}
	
	/**
	 * Set the picture of the profile.
	 * 
	 * @param url the url of the picture
	 */
	public void setImage(String url)
	{
		profileImageURL=url;
	}
	
	/**
	 * Get the URL of the profile picture of the user.
	 * 
	 * @return the url of the profile picture of the user
	 */
	public String getImage()
	{
		return profileImageURL;
	}
	
	/**
	 * Get the username of the account.
	 * 
	 * @return the username of the account
	 */
	public String getUserName() 
	{
		return this.userName;
	}
	
	/**
	 * Set the username of the user.
	 * 
	 * @param username the username of the account
	 */
	public void setUser(String username)
	{
		this.userName = username;
	}
	
	/**
	 * Get the latitude of the user's location.
	 * 
	 * @return the latitude of the user's location
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * Set the latitude of the user's location.
	 * 
	 * @param latitude
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Get the time when the last update took place.
	 * 
	 * @return the time of the last update
	 */
	public Date getLastUpdate() {
		return lastupdate;
	}
	
	/**
	 * Set the time when last update took place.
	 * 
	 * @param lastupdate the date and time input of the last update
	 */
	public void setLastUpdate(Date lastupdate) {
		this.lastupdate = lastupdate;
	}

	/**
	 * Get the longitude of the user's location.
	 * 
	 * @return longtitude longitude of the user's location
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * Set the longitude of the user's location
	 * 
	 * @param longitude of the user's location
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * Get the privacy status of the user account.
	 * 
	 * @return privacy the privacy status of the user account
	 */
	public String getPrivacy() {
		return privacy;
	}

	/**
	 * Set the privacy status of the user account.
	 * 
	 * @param privacy of the user account
	 */
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}

	/**
	 * Get the first name of the user.
	 * 
	 * @return firstName the first name of the user
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Set the first name of the user in the system.
	 * 
	 * @param firstName the first name of the user
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Get the last name of the user.
	 * 
	 * @return lastName the first name of the user
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Set the last name of the user in the system.
	 * 
	 * @param lastName the last name of the user
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}	
	
}
