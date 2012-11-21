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
	
	public UserLocation() 
	{
		
	}
	
	public UserLocation(String username)
	{
		this();
		this.userName = username;
		this.latitude = "-1.0";
		this.longitude = "-1.0";
		this.lastupdate=new Date();
		this.firstName = "";
		this.lastName = "";
	}
	
	public void setImage(String url)
	{
		profileImageURL=url;
	}
	public String getImage()
	{
		return profileImageURL;
	}
	
	public String getUserName() 
	{
		return this.userName;
	}
	
	public void setUser(String username)
	{
		this.userName = username;
	}
	
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public Date getLastUpdate() {
		return lastupdate;
	}
	
	public void setLastUpdate(Date lastupdate) {
		this.lastupdate = lastupdate;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getPrivacy() {
		return privacy;
	}

	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}	
	
}
