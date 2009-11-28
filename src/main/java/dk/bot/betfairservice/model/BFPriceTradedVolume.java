package dk.bot.betfairservice.model;

import java.io.Serializable;

/**
 * Represents traded volume for the given price on the given runner in a particular market.
 * 
 * @author korzekwad
 * 
 */
public class BFPriceTradedVolume implements Serializable{

	private final double price;
	private final double tradedVolume;

	/**
	 * 
	 * @param price
	 * @param tradedVolume
	 *            The total amount matched for the given price
	 */
	public BFPriceTradedVolume(double price, double tradedVolume) {
		this.price = price;
		this.tradedVolume = tradedVolume;
	}

	public double getPrice() {
		return price;
	}

	public double getTradedVolume() {
		return tradedVolume;
	}

	@Override
	public String toString() {
		return "BFPriceTradedVolume [price=" + price + ", tradedVolume=" + tradedVolume + "]";
	}
}
