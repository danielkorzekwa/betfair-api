package dk.bot.betfairservice.model;

import java.util.Date;

/**
 * Result object of sp bet placement operation
 * 
 * @author daniel
 * 
 */
public class BFSPBetPlaceResult {

	private final long betId;
	private final Date betDate;
	
	/**0 if limit not set.*/
	private final double limit;
	private final double liability;
	private final String betType;

	public BFSPBetPlaceResult(long betId, Date betDate, String betType, double limit, double liability) {
		this.betId = betId;
		this.betDate = betDate;
		this.betType = betType;
		this.limit = limit;
		this.liability = liability;
	}

	public long getBetId() {
		return betId;
	}

	public Date getBetDate() {
		return betDate;
	}

	public String getBetType() {
		return betType;
	}

	public double getLimit() {
		return limit;
	}

	public double getLiability() {
		return liability;
	}

}
