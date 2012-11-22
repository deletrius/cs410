package loclock.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

//import org.objectweb.asm.Type;

@PersistenceCapable(identityType=IdentityType.APPLICATION)
public class Subscription {
	
	@PrimaryKey
	@Persistent//(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String emailAddress;
	
	@Persistent(defaultFetchGroup="true")
	private ArrayList<String> friendList;
	
	@Persistent(defaultFetchGroup="true")
	private int numFriends;
	
	@Persistent(defaultFetchGroup="true")
	private ArrayList<String> types;

	/**
	 * The class constructor of Subscription
	 * 
	 * @param email the email address of the user
	 */
	public Subscription(String email){
		emailAddress=email;
		friendList=new ArrayList<String> ();
		types=new ArrayList<String> ();
		numFriends=0;
	}
	
	/**
	 * Get the number of friends a user has.
	 * 
	 * @return numFriends the number of friends of the user
	 */
	public int getNumFriends(){
		return numFriends;
	}
	
	/**
	 * Get the email address of the user.
	 * 
	 * @return
	 */
	public String getEmailAddress(){
		return emailAddress;
	}
	
	
	/**
	 * Add the friend requester to a user's friend list but keep
	 * the requester's status as "request"
	 * 
	 * @param friendName the name of the friend requester
	 */
	public void receiveRequest(String friendName){
		if (findFriend(friendName)!=-1)
			return;
		friendList.add(friendName);
		types.add("request");		
	}
	
	/**
	 * Accept the friend request and become friends with each other,
	 * add the requeter to the list of friends and change the status 
	 * of the requeter to "friend".
	 * 
	 * @param friendName the name of the friend requester
	 */
	public void acceptRequest(String friendName){
		int index=findFriend(friendName);
		if (index==-1)
		{
			friendList.add(friendName);
			types.add("friend");
		}			
		else if (types.get(index).compareTo("request")==0)
			types.set(index,"friend");	
		numFriends++;
	}
	
	/**
	 * Reject the friend request and remove the requester from the
	 * user's friend list.
	 * 
	 * @param friendName the name of the friend requester
	 */
	public void rejectRequest(String friendName){
		int index=findFriend(friendName);
		if (index==-1)
			return;
		if (types.get(index).compareTo("request")==0)
		{
			friendList.remove(index);
			types.remove(index);	
		}
		
	}
	
	/**
	 * Find a friend among a user's friend list.
	 * 
	 * @param friendName the name of the friend to be found
	 * @return the index of the friend in the list of friends. -1 if
	 * the friend does not exist.
	 */
	public int findFriend(String friendName)
	{
		for(int i=0;i<friendList.size();i++)
		{
			if (friendList.get(i).compareTo(friendName)==0)
				return i;
		}
		return -1;
	}
	
	
	/**
	 * Remove a friend from a user's friend list.
	 * 
	 * @param friendName the name of the friend to be removed from the friend list
	 */
	public void removeFriend(String friendName){
		int index=findFriend(friendName);
		friendList.remove(index);
		types.remove(index);	
		numFriends--;		
	}
	
	/**
	 * Get all of the friends in the user's friend list.
	 * 
	 * @return the whole list of the friends of a user
	 */
	public ArrayList<String> getFriends(){
		return friendList;
	}
	
	/**
	 * Get the all the individual user types in a user's friend list.
	 * 
	 * @return the whole list of the user types of a user
	 */
	public ArrayList<String> getTypes(){
		return types;
	}

}
