package loclock.server;

import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

public class Subscription {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String emailAddress;
	
	@Persistent
	private List<String> friendList;
	
	@Persistent
	private int numFriends;

	public Subscription(){
		
	}
	
	public int getNumFriends(){
		return numFriends;
	}
	
	public String getEmailAddress(){
		return emailAddress;
	}
	
	public void addFriend(String friendName){
		friendList.add(friendName);
	}
	
	public void removeFriend(String friendName){
		friendList.remove(friendName);
	}
	
	public List<String> getFriends(){
		return friendList;
	}

}
