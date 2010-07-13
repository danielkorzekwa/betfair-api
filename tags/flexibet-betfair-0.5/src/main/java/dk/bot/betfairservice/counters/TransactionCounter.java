package dk.bot.betfairservice.counters;

import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Counts betfair transactions for place/edit bet and protects against 'transaction commission'. Max 1000 place/edit bet transactions per hour are allowed.
 * 
 * @author daniel
 * 
 */
public class TransactionCounter {

	private final Log log = LogFactory.getLog(TransactionCounter.class.getSimpleName());

	/** Maximum amount of transactions in an hour. */
	public static final int TX_IN_HOUR_LIMIT = 1000;

	private ReentrantLock lock = new ReentrantLock();
	
	/**Time in hours from epoch*/
	private long currentHour = 0;
	private long lastHour = 0;
	private long maxHour = 0;
	
	/**Amount of transactions in current hour*/
	private int currHourTxNum = 0;
	private int lastHourTxNum = 0;
	private int maxTxNum = 0;
	

	/**
	 * Method blocks for a necessary amount of time if tx limit is
	 * reached.
	 * 
	 * @param requestTime
	 *            Time of sending request to a BetFair server (in milliseconds)
	 * @return Amount of milliseconds method was blocked.
	 * @param wait If true then method waits until next hour if max limit is reached. If false then exception is thrown.
	 */
	public long waitForPermission(long requestTime,boolean wait) {

		lock.lock();
		try {
			long sleepTime = 0;
			long hour = requestTime / (1000l*3600l);
			/**
			 * if it's a new hour then reset/increment txNum and return
			 * without wait.
			 */
			if (hour > currentHour) {
				lastHour=currentHour;
				lastHourTxNum=currHourTxNum;
				currentHour = hour;
				currHourTxNum = 1;
			} else {
				/** Limit is not reached yet - no wait.*/
				if (currHourTxNum + 1 <= TX_IN_HOUR_LIMIT) {
					currHourTxNum = currHourTxNum + 1;
					if (currHourTxNum > maxTxNum) {
						maxHour = currentHour;
						maxTxNum= currHourTxNum;
					}
				}
				/** Wait to the next hour if tx requests limit is reached. */
				else {
					if(wait) {
					sleepTime = ((hour + 1) * (1000l*3600l)) - requestTime;
					sleep(sleepTime);
					lastHour=currentHour;
					lastHourTxNum=currHourTxNum;
					currentHour = hour+1;
					currHourTxNum =  1;
					}
					else {
						throw new IllegalStateException("Max tx limit is reached: " + TX_IN_HOUR_LIMIT);
					}
				}
			}

			return sleepTime;
		} finally {
			lock.unlock();
		}
	}

	/**Returns current state of transaction counter.
	 * 
	 * @return
	 */
	public TransactionCounterState getState() {
		lock.lock();
		try {
			TransactionCounterState stat = new TransactionCounterState();
			stat.setMaxHour(this.maxHour);
			stat.setLastHour(this.lastHour);
			stat.setCurrentHour(this.currentHour);
			
			stat.setMaxTxNum(this.maxTxNum);
			stat.setLastHourTxNum(this.lastHourTxNum);
			stat.setCurrHourTxNum(this.currHourTxNum);
			
			return stat;
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
