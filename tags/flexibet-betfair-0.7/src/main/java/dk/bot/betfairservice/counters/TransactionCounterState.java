package dk.bot.betfairservice.counters;

import java.io.Serializable;
import java.util.Date;

/** Represents current state of transaction counter.
 * 
 * @author daniel
 *
 */
public class TransactionCounterState implements Serializable{

	/**Time in hours from epoch*/
	private long currentHour = 0;
	private long lastHour = 0;
	private long maxHour = 0;
	
	/**Amount of transactions in current hour*/
	private int currHourTxNum = 0;
	private int lastHourTxNum = 0;
	private int maxTxNum = 0;
	
	public long getCurrentHour() {
		return currentHour;
	}
	public void setCurrentHour(long currentHour) {
		this.currentHour = currentHour;
	}
	public long getLastHour() {
		return lastHour;
	}
	public void setLastHour(long lastHour) {
		this.lastHour = lastHour;
	}
	public long getMaxHour() {
		return maxHour;
	}
	public void setMaxHour(long maxHour) {
		this.maxHour = maxHour;
	}
	public int getCurrHourTxNum() {
		return currHourTxNum;
	}
	public void setCurrHourTxNum(int currHourTxNum) {
		this.currHourTxNum = currHourTxNum;
	}
	public int getLastHourTxNum() {
		return lastHourTxNum;
	}
	public void setLastHourTxNum(int lastHourTxNum) {
		this.lastHourTxNum = lastHourTxNum;
	}
	public int getMaxTxNum() {
		return maxTxNum;
	}
	public void setMaxTxNum(int maxTxNum) {
		this.maxTxNum = maxTxNum;
	}
	
	
	/**Convinience methods*/
	public Date getMaxHourAsDate() {
		return new Date(maxHour*1000*3600);
	}
	public Date getLastHourAsDate() {
		return new Date(lastHour*1000*3600);
	}
	public Date getCurrentHourAsDate() {
		return new Date(currentHour*1000*3600);
	}
	
}
