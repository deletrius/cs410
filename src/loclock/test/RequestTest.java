package loclock.test;

import org.junit.Test;

import junit.framework.TestCase;
import loclock.server.Request;

public class RequestTest extends TestCase {
	
	private Request request = new Request("receiverJohn", "senderMary");
	
	@Test
	public void testRequest() {
		assertEquals("receiverJohn", request.getReceiver());
		assertEquals("senderMary", request.getSender());
	}

}
