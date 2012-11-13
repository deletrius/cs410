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
	
	
	public void receiveRequest(String friendName){
		if (findFriend(friendName)!=-1)
			return;
		friendList.add(friendName);
		types.add("request");		
	}
	
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
	
	public int findFriend(String friendName)
	{
		for(int i=0;i<friendList.size();i++)
		{
			if (friendList.get(i).compareTo(friendName)==0)
				return i;
		}
		return -1;
	}
	
	
	public void removeFriend(String friendName){
		friendList.remove(friendName);
	}
	
	public ArrayList<String> getFriends(){
		return friendList;
	}
	public ArrayList<String> getTypes(){
		return types;
	}

}
