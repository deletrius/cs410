package loclock.server;


import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdentityType;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UserLocation {

//	@PrimaryKey
//	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
//	private Long id;

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
	}

//	public Long getId() 
//	{
//		return this.id;
//	}
	
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
	
	
//	@Persistent
//	private String realName;
//	@Persistent
//	private String age;
//	@Persistent
//	private String height;
//	@Persistent
//	private String weight;
//	@Persistent
//	private String faculty;
//	@Persistent
//	private String email;
//	@Persistent
//	private String yearLevel;
//	@Persistent
//	private String phoneNumber;
//	@Persistent
//	private Date timeReistered;
//	@Persistent
//	private double latitude;
//	@Persistent
//	private double longitude;
//	@Persistent
//	private LatLng location;
//	@Persistent
//	private String address;
//	@Persistent
//	private List<String> userSet = new ArrayList<String>();
//	@Persistent
//	private CalendarEvent appointment;
//	@Persistent
//	private List<CalendarEvent> appointments = new ArrayList<CalendarEvent>();
///**
// * COnstructor 
// * @param userName
// * @param age
// * @param height
// * @param weight
// * @param faculty
// * @param email
// * @param yearLevel
// * @param phoneNumber
// * @param tiemRegistered
// * @param latitude
// * @param longitude
// * @param location
// * @param address
// */
//
//
//	public User(String userName,String realName, String age, String height,String weight,String faculty,String email,String yearLevel, String phoneNumber,Date timeRegistered,double lat, double log,LatLng location, String address){
//		this.userName = userName;
//		this.realName = realName;
//		this.age = age;
//		this.height = height;
//		this.weight = weight;
//		this.faculty = faculty;
//		this.email = email;
//		this.yearLevel = yearLevel;
//		this.phoneNumber = phoneNumber;
//		this.timeReistered = timeRegistered;
//		this.latitude = lat;
//		this.longitude = log;
//		this.location = location;
//		this.address = address;
//	}
//	
//	public User(){
//		
//	}
//	
//	/**
//	 * Gets the user name of the user
//	 * @return user name
//	 */
//	public String getUserName(){
//		return this.userName;
//	}
//	
//	/**
//	 * Gets the real name of the user
//	 * @return real name
//	 */
//	public String getRealName(){
//		return this.realName;
//	}
//	
//	/**
//	 * Gets the age of the user
//	 * @return age
//	 */
//	public String getAge(){
//		return this.age;
//	}
//	
//	
//	/**
//	 * Gets the height of the user
//	 * @return height
//	 */
//	public String getHeight(){
//		return this.height;
//	}
//	
//	/**
//	 * Gets the weight of the user
//	 * @return weight
//	 */
//	public String getWeight(){
//		return this.weight;
//	}
//	
//	/**
//	 * Gets the faculty of the user
//	 * @return faculty
//	 */
//	public String getFaculty(){
//		return this.faculty;
//	}
//	
//	/**
//	 * Gets the email of the user
//	 * @return email
//	 */
//	public String getEmail(){
//		return this.email;
//	}
//	
//	/**
//	 * Gets the yearLevel of the user
//	 * @return yearLevel
//	 */
//	public String getYealLevel(){
//		return this.yearLevel;
//	}
//	
//	/**
//	 * Gets the phoneNumber of the user
//	 * @return phoneNumber
//	 */
//	public String getPhoneNumber(){
//		return this.phoneNumber;
//	}
//	/**
//	 * Gets the registered time of the user
//	 * @return registered time
//	 */
//	public Date getRegisteredTime(){
//		return this.timeReistered;
//	}
//
//	/**
//	 * Gets the latitude of the user
//	 * @return latitude
//	 */
//	public double getLatitude(){
//		return this.latitude;
//	}
//	/**
//	 * Gets the longitude of the user
//	 * @return longitude
//	 */
//	public double getLongitude(){
//		return this.longitude;
//	}
//	/**
//	 * Gets the location of the user
//	 * @return location (lat log)
//	 */
//	public LatLng getLocation(){
//		return this.location;
//	}
//	/**
//	 * Sets the address of the user
//	 */
//	public String getAddress(){
//		return this.address;
//	}
//	/**
//	 * Gets all users in a list
//	 * @return  list of users
//	 */
//	public List<String> getUserSet(){
//		return this.userSet;
//	}
//	
//	/**
//	 * Gets all users in a list
//	 * @return  list of users
//	 */
//	public void addUser(String uName){
//		userSet.add(uName);
//	}
//	
//	public void setUserName(String userName){
//		this.userName = userName;
//	}
//	
//	/**
//	 * Sets the real name of the user
//	 */
//	public void setRealName(String realname){
//		this.realName= realname;
//	}
//	
//	/**
//	 * sets the age of the user
//	 */
//	public void setAge(String age){
//		this.age= age;
//	}
//	
//	
//	/**
//	 * sets the height of the user
//	 */
//	public void setHeight(String height){
//		this.height=height;
//	}
//	
//	/**
//	 * sets the weight of the user
//	 */
//	public void setWeight(String weight){
//		this.weight= weight;
//	}
//	
//	/**
//	 * sets the faculty of the user
//	 */
//	public void setFaculty(String faculty){
//		this.faculty=faculty;
//	}
//	
//	/**
//	 * sets the email of the user
//	 */
//	public void setEmail(String email){
//		this.email= email;
//	}
//	
//	/**
//	 * sets the yearLevel of the user
//	 */
//	public void setYealLevel(String level){
//		this.yearLevel=level;
//	}
//	
//	/**
//	 * sets the phoneNumber of the user
//	 */
//	public void setPhoneNumber(String number){
//		this.phoneNumber=number;
//	}
//	/**
//	 * sets the registered time of the user
//	 */
//	public void setRegisteredTime(Date date){
//		this.timeReistered = date;
//	}
//
//	/**
//	 * sets the latitude of the user
//	 */
//	public void setLatitude(double lat){
//		this.latitude = lat;
//	}
//	/**
//	 * sets the longitude of the user
//	 */
//	public void setLongitude(double lon){
//		this.longitude=lon;
//	}
//	/**
//	 * sets the location of the user
//	 */
//	public void setLocation(LatLng loc){
//		this.location=loc;
//	}
//	/**
//	 * Gets the address of the user
//	 * @return address
//	 */
//	public void setAddress(String addr){
//		this.address=addr;
//	}
	
}
