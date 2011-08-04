package dk.bot.betfairservice.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * BetFair API bean for runner price.
 * 
 * @author daniel
 * 
 */
public class BFRunnerPrice implements Serializable{

	private final double price;

	private final double totalToBack;
	
	private final double totalToLay;

	public BFRunnerPrice(double price,double totalToBack,double totalToLay) {
		this.price=price;
		this.totalToBack=totalToBack;
		this.totalToLay=totalToLay;
	}

	public double getPrice() {
		return price;
	}

	public double getTotalToBack() {
		return totalToBack;
	}

	public double getTotalToLay() {
		return totalToLay;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this).toString();
	}
}
