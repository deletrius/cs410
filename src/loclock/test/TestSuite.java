package loclock.test;

import com.google.gwt.junit.tools.GWTTestSuite;

import junit.framework.Test;
import junit.framework.TestCase;


public class TestSuite extends GWTTestSuite{
	
	public static Test suite(){
		TestSuite suite = new TestSuite();
		suite.addTestSuite(CalendarTest.class);
		suite.addTestSuite(MessageTest.class);
		suite.addTestSuite(NotificationCalendarTest.class);
		suite.addTestSuite(NotificationTest.class);
		suite.addTestSuite(RequestTest.class);
		suite.addTestSuite(SubscriptionTest.class);
		suite.addTestSuite(UserLocationTest.class);
		return suite;
	}

}
