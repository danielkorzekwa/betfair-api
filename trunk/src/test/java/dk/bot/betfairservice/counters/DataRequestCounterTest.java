package dk.bot.betfairservice.counters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DataRequestCounterTest {

	private DataRequestCounter counter = new DataRequestCounter();

	/** Wait to the next second. Place 20 requests with weight 1, then place next request(weight=5) in the same second.*/
	@Test
	public void testWaitForPermission() {

		for (int i = 501; i <= 520; i++) {
			assertEquals(0, counter.waitForPermission(i, 1));
		}
		assertEquals(20, counter.getCurrSecRequestsNum());
		assertEquals(0, counter.getLastSecRequestsNum());
		assertEquals(20,counter.getMaxRequestsPerSecond());
		
		long waitingTime = counter.waitForPermission(600,1);
		assertTrue("Waiting time for next request must be 400",waitingTime==400);
		assertEquals(1, counter.getCurrSecRequestsNum());
		assertEquals(20, counter.getLastSecRequestsNum());
		assertEquals(20,counter.getMaxRequestsPerSecond());
	}
	
	/** Wait to the next second. Place 16 requests with weight 1, then place next request (weight=5) in the same second.*/	
	@Test
	public void testWaitForPermission2() {

		
		for (int i = 501; i <= 516; i++) {
			assertEquals(0, counter.waitForPermission(i, 1));
		}
		assertEquals(16, counter.getCurrSecRequestsNum());
		assertEquals(0, counter.getLastSecRequestsNum());
		assertEquals(16,counter.getMaxRequestsPerSecond());
		
		long waitingTime = counter.waitForPermission(600,5);
		assertTrue("Waiting time for next request must be 400",waitingTime==400);
		assertEquals(5, counter.getCurrSecRequestsNum());
		assertEquals(16, counter.getLastSecRequestsNum());
		assertEquals(16,counter.getMaxRequestsPerSecond());
	}
	
	/** Don't wait - go to next second. Place 16 requests with weight 1, then place next request (weight=5) in next second.*/		
	@Test
	public void testWaitForPermission3() {

		for (int i = 501; i <= 516; i++) {
			assertEquals(0, counter.waitForPermission(i, 1));
		}
		assertEquals(16, counter.getCurrSecRequestsNum());
		assertEquals(0, counter.getLastSecRequestsNum());
		assertEquals(16,counter.getMaxRequestsPerSecond());
		
		long waitingTime = counter.waitForPermission(1600,5);
		assertTrue("Waiting time for next request must be 0",waitingTime==0);
		assertEquals(5, counter.getCurrSecRequestsNum());
		assertEquals(16, counter.getLastSecRequestsNum());
		assertEquals(16,counter.getMaxRequestsPerSecond());
	}
	
	/** Don't wait - weight - 0. Place 16 requests with weight 1, then place next request (weight=0) in the same second.*/		
	
	@Test
	public void testWaitForPermission4() {

		for (int i = 501; i <= 516; i++) {
			assertEquals(0, counter.waitForPermission(i, 1));
		}
		assertEquals(16, counter.getCurrSecRequestsNum());
		assertEquals(0, counter.getLastSecRequestsNum());
		assertEquals(16,counter.getMaxRequestsPerSecond());
		
		long waitingTime = counter.waitForPermission(600,0);
		assertTrue("Waiting time for next request must be 0",waitingTime==0);
		assertEquals(16, counter.getCurrSecRequestsNum());
		assertEquals(0, counter.getLastSecRequestsNum());
		assertEquals(16,counter.getMaxRequestsPerSecond());
	}
}
