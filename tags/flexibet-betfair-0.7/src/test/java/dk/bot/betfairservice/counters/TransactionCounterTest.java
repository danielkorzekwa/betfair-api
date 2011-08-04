package dk.bot.betfairservice.counters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TransactionCounterTest {

	private TransactionCounter counter = new TransactionCounter();

	/** Wait to the next hour. Place 1000 requests, then place next request in the same hour. */
	@Test
	public void testWaitForPermission() {

		for (int i = (1000 * 3600) - (TransactionCounter.TX_IN_HOUR_LIMIT + 199); i <= (1000 * 3600) - 200; i++) {
			assertEquals(0, counter.waitForPermission(i, true));
			assertEquals(i - ((1000 * 3600) - (TransactionCounter.TX_IN_HOUR_LIMIT + 200)), counter.getState().getCurrHourTxNum());
		}
		assertEquals(TransactionCounter.TX_IN_HOUR_LIMIT, counter.getState().getCurrHourTxNum());
		assertEquals(0, counter.getState().getLastHourTxNum());
		assertEquals(TransactionCounter.TX_IN_HOUR_LIMIT, counter.getState().getMaxTxNum());

		long waitingTime = counter.waitForPermission((1000 * 3600) - 199, true);
		assertEquals(199, waitingTime);
		assertEquals(1, counter.getState().getCurrHourTxNum());
		assertEquals(TransactionCounter.TX_IN_HOUR_LIMIT, counter.getState().getLastHourTxNum());
		assertEquals(TransactionCounter.TX_IN_HOUR_LIMIT, counter.getState().getMaxTxNum());
	}

	/** Throw exception when limit is reached. Place 1000 requests, then place next request in the same hour. */
	@Test
	public void testWaitForPermissionThrowException() {

		for (int i = (1000 * 3600) - (TransactionCounter.TX_IN_HOUR_LIMIT + 199); i <= (1000 * 3600) - 200; i++) {
			assertEquals(0, counter.waitForPermission(i, true));
			assertEquals(i - ((1000 * 3600) - (TransactionCounter.TX_IN_HOUR_LIMIT + 200)), counter.getState().getCurrHourTxNum());
		}
		assertEquals(TransactionCounter.TX_IN_HOUR_LIMIT, counter.getState().getCurrHourTxNum());
		assertEquals(0, counter.getState().getLastHourTxNum());
		assertEquals(TransactionCounter.TX_IN_HOUR_LIMIT, counter.getState().getMaxTxNum());

		try {
			counter.waitForPermission((1000 * 3600) - 199, false);
			fail("Exception not thrown when limit was reached.");
		} catch (IllegalStateException e) {

		}
		assertEquals(TransactionCounter.TX_IN_HOUR_LIMIT, counter.getState().getCurrHourTxNum());
		assertEquals(0, counter.getState().getLastHourTxNum());
		assertEquals(TransactionCounter.TX_IN_HOUR_LIMIT, counter.getState().getMaxTxNum());
	}

	/** Don't wait - go to next hour. Place 500 requests, then place next request in next hour. */
	@Test
	public void testWaitForPermission3() {

		for (int i = 501; i <= 1500; i++) {
			assertEquals(0, counter.waitForPermission(i, true));
			assertEquals(i - 500, counter.getState().getCurrHourTxNum());
		}
		assertEquals(1000, counter.getState().getCurrHourTxNum());
		assertEquals(0, counter.getState().getLastHourTxNum());
		assertEquals(1000, counter.getState().getMaxTxNum());

		long waitingTime = counter.waitForPermission(1000 * 3600 + 100, true);
		assertTrue("Waiting time for next request must be 0", waitingTime == 0);
		assertEquals(1, counter.getState().getCurrHourTxNum());
		assertEquals(1000, counter.getState().getLastHourTxNum());
		assertEquals(1000, counter.getState().getMaxTxNum());
	}

}
