package dk.bot.betfairservice.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * BetFair API bean for market runners.
 * 
 * @author daniel
 * 
 */
public class BFMarketRunners implements Serializable {

	private final int marketId;
	private final List<BFMarketRunner> marketRunners;
	private final int inPlayDelay;
	
	/** The time at which the response was received from the betfair server. */
	private final Date timestamp;

	public BFMarketRunners(int marketId,List<BFMarketRunner> marketRunners,int inPlayDelay,Date timestamp) {
		this.marketId=marketId;
		this.marketRunners=marketRunners;
		this.inPlayDelay=inPlayDelay;
		this.timestamp=timestamp;
	}

	public int getMarketId() {
		return marketId;
	}
	public List<BFMarketRunner> getMarketRunners() {
		return marketRunners;
	}
	public int getInPlayDelay() {
		return inPlayDelay;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	
	/**
	 * 
	 * @param selectionId
	 * @return null if runner doesn't exist for selectionId
	 */
	public BFMarketRunner getMarketRunner(int selectionId) {
		for(BFMarketRunner runner : marketRunners) {
			if(runner.getSelectionId()==selectionId) {
				return runner;
			}
		}
		return null;
	}
	
	/** Returns amount of all offers to back and lay.*/
	public double getTotalToBet() {
		double total=0;
		
		for (BFMarketRunner runner : marketRunners) {
			total = total + runner.getTotalToBet();
		}
		return total;
	}
}
