package loclock.test;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import loclock.server.Notification;

public class NotificationTest extends TestCase {
	
	private Notification notification = new Notification("fromJohn", "toMary", "content1", "bbq");
	private Notification notificationWithoutParameter = new Notification();	
	private Date now = new Date();	
	
	@Test
	public void testNotification() {
		assertEquals("fromJohn", notification.getFromUser());
		assertEquals("toMary", notification.getToUser());
		assertEquals("content1", notification.getContent());
		assertEquals("bbq", notification.getEventName());
	}
	
	@Test
	public void testNotificationWithoutParameter() {
		assertEquals(now, notificationWithoutParameter.getNotificationCreationDate());
	}
	
	@Test
	public void testSetDate() {
		Date date = new Date();
		notificationWithoutParameter.setNotificationCreationDate(date);
		assertEquals(date, notificationWithoutParameter.getNotificationCreationDate());
	}
	
	@Test
	public void testSetFromUser() {
		notification.setFromUser("fromKing");
		assertEquals("fromKing", notification.getFromUser());
	}
	
	@Test
	public void testSetToUser() {
		notification.setToUser("toQueen");
		assertEquals("toQueen", notification.getToUser());
	}
	
	@Test
	public void testSetEvent() {
		notification.setEventName("party");
		assertEquals("party", notification.getEventName());
	}
	
	@Test
	public void testSetContent() {
		notification.setContent("texts");
		assertEquals("texts", notification.getContent());
	}

}
