package dk.bot.betfairservice;

import java.io.Serializable;

import dk.bot.betfairservice.counters.TransactionCounterState;

/**
 * Data model for BetFairService usage statistics.
 * 
 * @author daniel
 * 
 */
public class BetFairServiceInfo implements Serializable {

	private TransactionCounterState txCounterState;
	
	/** max num of data requests (weighted) per second. */
	private int maxDataRequestPerSecond;

	/** num of data requests (weighted) in last second. */
	private int lastSecDataRequest;

	

	public int getMaxDataRequestPerSecond() {
		return maxDataRequestPerSecond;
	}

	public void setMaxDataRequestPerSecond(int maxDataRequestPerSecond) {
		this.maxDataRequestPerSecond = maxDataRequestPerSecond;
	}

	public int getLastSecDataRequest() {
		return lastSecDataRequest;
	}

	public void setLastSecDataRequest(int lastSecDataRequest) {
		this.lastSecDataRequest = lastSecDataRequest;
	}

	public TransactionCounterState getTxCounterState() {
		return txCounterState;
	}

	public void setTxCounterState(TransactionCounterState txCounterState) {
		this.txCounterState = txCounterState;
	}
}
