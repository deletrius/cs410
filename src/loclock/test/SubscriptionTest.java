package loclock.test;

import org.junit.Test;

import junit.framework.TestCase;
import loclock.server.Subscription;


public class SubscriptionTest extends TestCase{
	
	private Subscription subscription = new Subscription("test@example.com");
	
	@Test
	public void testSubsription() {
		assertEquals("test@example.com", subscription.getEmailAddress());
	}

	@Test
	public void testReceiveRequest(){
		subscription.receiveRequest("Lebron James");
		subscription.receiveRequest("Dwayne Wade");
		subscription.receiveRequest("Tyson Chandeller");
		assertEquals(subscription.findFriend("Lebron James"), 0);
		assertEquals(subscription.getTypes().get(0), "request");
		assertEquals(subscription.findFriend("Dwayne Wade"), 1);
		assertEquals(subscription.getTypes().get(1), "request");
		assertEquals(subscription.findFriend("Tyson Chandeller"), 2);
		assertEquals(subscription.getTypes().get(2), "request");
		assertEquals(subscription.findFriend("Kobe Bryant"), -1);
		assertEquals(subscription.getNumFriends(), 0);
	}
	
	@Test
	public void testAcceptRequest(){
		subscription.acceptRequest("Lebron James");
		assertEquals(subscription.getNumFriends(), 1);
		subscription.acceptRequest("Dwayne Wade");
		assertEquals(subscription.getNumFriends(), 2);
		subscription.receiveRequest("Tyson Chandeller");
		assertEquals(subscription.getNumFriends(), 2);
		subscription.acceptRequest("Kobe Bryant");
		assertEquals(subscription.getNumFriends(), 3);
		assertEquals(subscription.findFriend("Lebron James"), 0);
		assertEquals(subscription.getTypes().get(0), "friend");
		assertEquals(subscription.findFriend("Dwayne Wade"), 1);
		assertEquals(subscription.getTypes().get(1), "friend");
		assertEquals(subscription.findFriend("Tyson Chandeller"), 2);
		assertEquals(subscription.getTypes().get(2), "request");
		assertEquals(subscription.findFriend("Kobe Bryant"), 3);
		assertEquals(subscription.getTypes().get(3), "friend");
		assertEquals(subscription.getNumFriends(), 3);
	}
	
	@Test
	public void testRejectRequest(){
		subscription.receiveRequest("Lebron James");
		subscription.receiveRequest("Dwayne Wade");
		assertEquals(subscription.findFriend("Lebron James"), 0);
		assertEquals(subscription.getTypes().get(0), "request");
		assertEquals(subscription.findFriend("Dwayne Wade"), 1);
		assertEquals(subscription.getTypes().get(0), "request");
		subscription.rejectRequest("Dwayne Wade");
		assertEquals(subscription.findFriend("Dwayne Wade"), -1);
	}
	
	@Test
	public void testremoveFriend(){
		subscription.acceptRequest("Lebron James");
		subscription.acceptRequest("Dwayne Wade");
		assertEquals(subscription.findFriend("Lebron James"), 0);
		assertEquals(subscription.getTypes().get(0), "friend");
		assertEquals(subscription.findFriend("Dwayne Wade"), 1);
		assertEquals(subscription.getTypes().get(1), "friend");
		subscription.removeFriend("Lebron James");
		assertEquals(subscription.findFriend("Lebron James"), -1);
		assertEquals(subscription.findFriend("Dwayne Wade"), 0);
		assertEquals(subscription.getNumFriends(), 1);
	}
	
	

}
