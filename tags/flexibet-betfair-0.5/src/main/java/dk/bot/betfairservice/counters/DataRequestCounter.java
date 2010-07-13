package dk.bot.betfairservice.counters;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Counts data requests and protects against 'data request commission'. Max 20
 * requests are allowed in a second for weight =1 or 4 requests with weight=5.
 * 
 * It's a thread safe object.
 */
public class DataRequestCounter {

	private final Log log = LogFactory.getLog(DataRequestCounter.class.getSimpleName());

	/** Maximum amount of data requests in a second. */
	private static final int DATA_REQUESTS_LIMIT = 20;

	private ReentrantLock lock = new ReentrantLock();
	
	/**Time from epoch in seconds.*/
	private long currentSecond = 0;
	
	/**Amount of requests (weighted) in a current second.*/
	private int currSecDataRequests = 0;
	private int lastSecDataRequests = 0;
	private int maxDataRequests = 0;

	/**
	 * Method blocks for a necessary amount of time if data request limit is
	 * reached.
	 * 
	 * @param requestTime
	 *            Time of sending request to a BetFair server (in milliseconds)
	 * @param weight
	 *            Weight of a request, See class level comments.
	 * @return Amount of milliseconds method was blocked.
	 */
	public long waitForPermission(long requestTime, int weight) {

		lock.lock();
		try {
			long sleepTime = 0;
			long second = requestTime / 1000l;
			/**
			 * if it's a new second then reset/increment dataRequests and return
			 * without wait.
			 */
			if (second > currentSecond) {
				currentSecond = second;
				lastSecDataRequests=currSecDataRequests;
				currSecDataRequests = weight;
			} else {
				/** Limit is not reached yet - no wait.*/
				if (currSecDataRequests + weight <= DATA_REQUESTS_LIMIT) {
					currSecDataRequests = currSecDataRequests + weight;
					if (currSecDataRequests > maxDataRequests) {
						maxDataRequests = currSecDataRequests;
					}
				}
				/** Wait to the next second if data requests limit is reached. */
				else {
					sleepTime = ((second + 1) * 1000) - requestTime;
					sleep(sleepTime);
					currentSecond = second + 1;
					lastSecDataRequests=currSecDataRequests;
					currSecDataRequests = weight;
				}
			}

			return sleepTime;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Returns max num of requests per second.
	 * 
	 * @return
	 */
	public int getMaxRequestsPerSecond() {
		lock.lock();
		try {
			return maxDataRequests;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Returns num of requests in current second.
	 * 
	 * @return
	 */
	public int getCurrSecRequestsNum() {
		lock.lock();
		try {
			return currSecDataRequests;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Returns num of requests in last second.
	 * 
	 * @return
	 */
	public int getLastSecRequestsNum() {
		lock.lock();
		try {
			return lastSecDataRequests;
		} finally {
			lock.unlock();
		}
	}

	private void sleep(long timeInMillis) {
		try {
			Thread.sleep(timeInMillis);
		} catch (InterruptedException e) {
			log.error("Thread sleep error.", e);
		}
	}
}
