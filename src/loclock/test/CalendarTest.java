package loclock.test;

import java.util.Date;

import org.junit.Test;

import junit.framework.TestCase;
import loclock.server.Calendar;

public class CalendarTest extends TestCase{
	
	private Date startDate = new Date(101, 11, 3, 15, 43, 52);
	private Date endDate = new Date(101, 11, 5, 15, 43, 52);
	
	
	Calendar calendar1 = new Calendar("user1", "bbq", "description1",
			startDate, endDate);
	
	Calendar calendar2 = new Calendar("user2", "birthday", "description2",
			startDate, endDate, false);
	
	@Test
	public void testCalendar() {
		
		assertEquals("user1", calendar1.getUserName());
		assertEquals("bbq", calendar1.getEventName());
		assertEquals("description1", calendar1.getDescription());
		assertEquals(startDate, calendar1.getStartDate());
		assertEquals(endDate, calendar1.getEndDate());
		
		assertEquals("user2", calendar2.getUserName());
		assertEquals("birthday", calendar2.getEventName());
		assertEquals("description2", calendar2.getDescription());
		assertEquals(startDate, calendar2.getStartDate());
		assertEquals(endDate, calendar2.getEndDate());
		assertEquals(false, calendar2.isCanEdit());
	}
	
	@Test
	public void testSetUserName() {
		calendar1.setUserName("Bryan");
		assertEquals("Bryan", calendar1.getUserName());
	}
	
	@Test
	public void testSetEventName() {
		calendar1.setEventName("friday night");
		assertEquals("friday night", calendar1.getEventName());
	}
	
	@Test
	public void testSetDescription() {
		calendar1.setDescription("desc");
		assertEquals("desc", calendar1.getDescription());
	}
	
	private Date startDate2 = new Date(111, 5, 3, 15, 43, 52);
	private Date endDate2 = new Date(111, 5, 5, 2, 43, 52);
	
	@Test
	public void testStartDate() {
		calendar1.setStartDate(startDate2);
		assertEquals(startDate2, calendar1.getStartDate());
	}
	
	@Test
	public void testEndDate() {
		calendar1.setEndDate(endDate2);
		assertEquals(endDate2, calendar1.getEndDate());
	}
	
	@Test
	public void testEdit() {
		calendar1.setCanEdit(true);
		assertEquals(true, calendar1.isCanEdit());
		calendar2.setCanEdit(false);
		assertEquals(false, calendar2.isCanEdit());
	}

}
