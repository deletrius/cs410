package loclock.test;

import org.junit.Test;

import junit.framework.TestCase;
import loclock.server.UserLocation;

public class UserLocationTest extends TestCase {
	
	private UserLocation userLocation = new UserLocation("Andrew");
	
	@Test
	public void testUserName() {
		assertEquals("Andrew", userLocation.getUserName());
		userLocation.setUser("John");
		assertEquals("John", userLocation.getUserName());
	}
	
	@Test
	public void testSetImage() {
		userLocation.setImage("www.google.ca/image");
		assertEquals("www.google.ca/image", userLocation.getImage());
	}
	
	@Test
	public void testSetPosition() {
		userLocation.setLongitude("122.2");
		userLocation.setLatitude("231.5");
		assertEquals("122.2", userLocation.getLongitude());
		assertEquals("231.5", userLocation.getLatitude());
	}
	
	@Test
	public void testSetName() {
		userLocation.setFirstName("Michael");
		userLocation.setLastName("Jordan");
		assertEquals("Michael", userLocation.getFirstName());
		assertEquals("Jordan", userLocation.getLastName());
	}
	
	@Test
	public void testSetPrivacy() {
		userLocation.setPrivacy("private");
		assertEquals("private", userLocation.getPrivacy());
		userLocation.setPrivacy("public");
		assertEquals("public", userLocation.getPrivacy());
	}

}
