package loclock.test;

import java.util.Date;

import org.junit.Test;

import junit.framework.TestCase;
import loclock.server.NotificationCalendar;

public class NotificationCalendarTest extends TestCase {
	
	private Date startDate = new Date(101, 11, 3, 15, 43, 52);
	private Date endDate = new Date(101, 11, 5, 15, 43, 52);
	
	private NotificationCalendar calendar = new NotificationCalendar("fromPaul",
			"toKaty", "content", "event", startDate, endDate);
	
	@Test
	public void testNotificationCalendar() {
		assertEquals("fromPaul", calendar.getFromUser());
		assertEquals("toKaty", calendar.getToUser());
		assertEquals("content", calendar.getContent());
		assertEquals(startDate, calendar.getNewStartDate());
		assertEquals(endDate, calendar.getNewendDate());
	}

	@Test
	public void testSetNewStartDate() {
		calendar.setNewendDate(startDate);
		assertEquals(startDate, calendar.getNewStartDate());
	}
	
	@Test
	public void testSetNewEndDate() {
		calendar.setNewendDate(endDate);
		assertEquals(endDate, calendar.getNewendDate());
	}

}
