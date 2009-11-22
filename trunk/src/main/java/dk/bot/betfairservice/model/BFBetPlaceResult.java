package dk.bot.betfairservice.model;

import java.util.Date;

/**
 * Result object of bet placement operation
 * 
 * @author daniel
 * 
 */
public class BFBetPlaceResult {

	private final long betId;
	private final Date betDate;
	private final double price;
	private final double size;
	private final double avgPriceMatched;
	private final double sizeMatched;
	private final String betType;

	public BFBetPlaceResult(long betId, Date betDate, String betType, double price, double size, double avgPriceMatched,
			double sizeMatched) {
		this.betId = betId;
		this.betDate = betDate;
		this.betType = betType;
		this.price = price;
		this.size = size;
		this.avgPriceMatched = avgPriceMatched;
		this.sizeMatched = sizeMatched;
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

	public double getPrice() {
		return price;
	}

	public double getSize() {
		return size;
	}

	public double getAvgPriceMatched() {
		return avgPriceMatched;
	}

	public double getSizeMatched() {
		return sizeMatched;
	}

}
