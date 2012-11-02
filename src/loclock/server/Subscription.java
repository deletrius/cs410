package loclock.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType=IdentityType.APPLICATION)
public class Subscription {
	
	@PrimaryKey
	@Persistent//(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String emailAddress;
	
	@Persistent
	private List<String> friendList;
	
	@Persistent
	private int numFriends;
	
	@Persistent 
	private List<String> types;

	public Subscription(String email){
		emailAddress=email;
		friendList=new ArrayList<String> ();
		types=new ArrayList<String> ();
		numFriends=0;
	}
	
	public int getNumFriends(){
		return numFriends;
	}
	
	public String getEmailAddress(){
		return emailAddress;
	}
	
	public void addFriend(String friendName,String type){
		friendList.add(friendName);
		types.add(type);
		numFriends++;
	}
	
	
	public void removeFriend(String friendName){
		friendList.remove(friendName);
	}
	
	public List<String> getFriends(){
		return friendList;
	}

}
