package dk.bot.betfairservice.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * BetFair API bean for market runner.
 * 
 * @author daniel
 * 
 */
public class BFMarketRunner implements Serializable{

	private final int selectionId;
	private final double totalAmountMatched;
	private final double lastPriceMatched;
	private final double farSP;
	private final double nearSP;
	private final double actualSP;
	private final List<BFRunnerPrice> prices;

	public BFMarketRunner(int selectionId,double totalAmountMatched,double lastPriceMatched,double farSP,double nearSP,double actualSP,List<BFRunnerPrice> prices) {
		this.selectionId = selectionId;
		this.totalAmountMatched = totalAmountMatched;
		this.lastPriceMatched = lastPriceMatched;
		this.farSP = farSP;
		this.nearSP = nearSP;
		this.actualSP = actualSP;
		this.prices = prices;
	}

	public int getSelectionId() {
		return selectionId;
	}

	public double getTotalAmountMatched() {
		return totalAmountMatched;
	}

	public double getLastPriceMatched() {
		return lastPriceMatched;
	}

	public double getFarSP() {
		return farSP;
	}

	public double getNearSP() {
		return nearSP;
	}

	public double getActualSP() {
		return actualSP;
	}

	public List<BFRunnerPrice> getPrices() {
		return prices;
	}
	
	public double getTotalToBack() {
		double total=0;
		
		for (BFRunnerPrice price : prices) {
			total = total + price.getTotalToBack();
		}
		return total;
	}
	
	public double getTotalToLay() {
		double total=0;
		
		for (BFRunnerPrice price : prices) {
			total = total + price.getTotalToLay();
		}
		return total;
	}
	
	/** Returns amount of all offers to back and lay.*/
	public double getTotalToBet() {
		double total=0;
		
		for (BFRunnerPrice price : prices) {
			total = total + price.getTotalToLay() + price.getTotalToBack();
		}
		return total;
	}
	
	public double getTotalOnPriceToBack() {
		double bestPrice = 1.01d;
		double total = 0;

		for (BFRunnerPrice price : prices) {
			if (price.getTotalToBack() >= 2 && price.getPrice() > 0) {
				if (price.getPrice() >= bestPrice) {
					bestPrice = price.getPrice();
					total = price.getTotalToBack();
				}
			}
		}
		return total;
	}

	public double getPriceToBack() {
		double bestPrice = 1.01d;

		if (prices != null) {
			for (BFRunnerPrice price : prices) {
				if (price.getTotalToBack() >= 2 && price.getPrice() > 0) {
					if (price.getPrice() >= bestPrice) {
						bestPrice = price.getPrice();
					}
				}
			}
		}

		return bestPrice;
	}

	public double getTotalOnPriceToLay() {
		double bestPrice = 1000;
		double total = 0;

		for (BFRunnerPrice price : prices) {
			if (price.getTotalToLay() >= 2 && price.getPrice() > 0) {
				if (price.getPrice() <= bestPrice) {
					bestPrice = price.getPrice();
					total = price.getTotalToLay();
				}
			}
		}
		return total;
	}

	public double getPriceToLay() {
		double bestPrice = 1000;

		if (prices != null) {
			for (BFRunnerPrice price : prices) {
				if (price.getTotalToLay() >= 2 && price.getPrice() > 0) {
					if (price.getPrice() <= bestPrice) {
						bestPrice = price.getPrice();
					}
				}
			}
		}
		return bestPrice;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this).toString();
	}
}
