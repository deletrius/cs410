package loclock.test;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import loclock.server.Message;


public class MessageTest extends TestCase{
	
	private Date date = new Date();
	private Message message = new Message("fromJohn", "toMary", "msg", date);
	
	@Test
	public void testCalendar() {
		assertEquals("fromJohn", message.getFromUser());
		assertEquals("toMary", message.getToUser());
		assertEquals("msg", message.getMessageBody());
		assertEquals(date, message.getTimeStamp());
	}
	
	@Test
	public void testSetFromUser() {
		message.setFromUser("fromMike");
		assertEquals("fromMike", message.getFromUser());
	}
	
	@Test
	public void testSetToUser() {
		message.setToUser("fromMike");
		assertEquals("toKate", message.getToUser());
	}
	
	@Test
	public void testSetMessage() {
		message.setFromUser("message");
		assertEquals("message", message.getMessageBody());
	}

}
