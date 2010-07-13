package dk.bot.betfairservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math.util.MathUtils;

import com.betfair.publicapi.types.exchange.v5.BetStatusEnum;
import com.betfair.publicapi.types.exchange.v5.MUBet;

import dk.bot.betfairservice.model.BFMarketData;
import dk.bot.betfairservice.model.BFMarketRunner;
import dk.bot.betfairservice.model.BFMarketRunners;
import dk.bot.betfairservice.model.BFRunnerPrice;

/**
 * Helper class to parse BetFair compressed data
 * 
 * @author daniel
 * 
 */
public class BetFairUtil {

	public static final int ROUND_DOWN = 0;
	public static final int ROUND_UP = 1;

	private static final Log log = LogFactory.getLog(BetFairUtil.class.getSimpleName());

	public static List<BFMarketData> parseMarketsData(String marketData) throws BetFairException {

		String[] marketStrings = marketData.split(":");
		List<BFMarketData> markets = new ArrayList<BFMarketData>();

		for (int i = 1; i < marketStrings.length; i++) {
			String[] marketString = marketStrings[i].split("~");

			if (marketString.length != 16) {
				log.warn("getMarkets: wrong market record format:" + marketStrings[i]);
			} else {
				BFMarketData market = new BFMarketData();
				market.setMarketId(Integer.parseInt(marketString[0]));
				market.setMarketName(marketString[1]);
				market.setMarketType(marketString[2]);
				market.setMarketStatus(marketString[3]);
				market.setEventDate(new Date(Long.parseLong(marketString[4])));
				market.setMenuPath(marketString[5]);
				market.setEventHierarchy(marketString[6]);
				market.setBetDelay(marketString[7]);
				market.setExchangeId(Integer.parseInt(marketString[8]));
				market.setCountryCode(marketString[9]);
				market.setLastRefresh(new Date(Long.parseLong(marketString[10])));
				market.setNumberOfRunners(Integer.parseInt(marketString[11]));
				market.setNumberOfWinners(Integer.parseInt(marketString[12]));
				market.setTotalAmountMatched(Double.parseDouble(marketString[13]));

				if (marketString[14].equals("Y")) {
					market.setBsbMarket(true);
				} else {
					market.setBsbMarket(false);
				}

				if (marketString[15].equals("Y")) {
					market.setTurningInPlay(true);
				} else {
					market.setTurningInPlay(false);
				}

				markets.add(market);
			}

		}
		return markets;
	}

	public static BFMarketRunners parseMarketPrices(String marketPrices, Date runnersTimestamp) {
		
		
		String[] marketArray = marketPrices.split(":");

		int marketId = Integer.parseInt(marketArray[0].split("~")[0]);
		int inPlayDelay = Integer.parseInt(marketArray[0].split("~")[1]);
		
		List<BFMarketRunner> runners = new ArrayList<BFMarketRunner>();
		for (int i = 1; i < marketArray.length; i++) {

			String[] runnerArray = marketArray[i].split("\\|");

			String[] runnerDetails = runnerArray[0].split("~", -1);
			
			int selectionId = (Integer.parseInt(runnerDetails[0]));
			double totalAmountMatched = Double.parseDouble(runnerDetails[2]);
			double lastPriceMatched=0;
			double farSP=0,nearSP=0,actualSP=0;
			if (totalAmountMatched > 0 && runnerDetails[3].length() > 0) {
				lastPriceMatched = Double.parseDouble(runnerDetails[3]);
			}

			if (runnerDetails[8].length() > 0 && !runnerDetails[8].equals("NaN") && !runnerDetails[8].equals("-INF") && !runnerDetails[8].equals("INF")) {
				farSP = Double.parseDouble(runnerDetails[8]);
				if(Double.isInfinite(farSP)) {
					farSP = 0;
				}
			}
			if (runnerDetails[9].length() > 0 && !runnerDetails[9].equals("NaN") && !runnerDetails[9].equals("-INF") && !runnerDetails[9].equals("INF")) {
				nearSP = Double.parseDouble(runnerDetails[9]);
				if(Double.isInfinite(nearSP)) {
					nearSP = 0;
				}
			}

			if (runnerDetails[10].length() > 0 && !runnerDetails[10].equals("NaN") && !runnerDetails[10].equals("-INF") && !runnerDetails[10].equals("INF")) {
				actualSP = Double.parseDouble(runnerDetails[10]);
				if(Double.isInfinite(actualSP)) {
					actualSP=0;
				}
			}

			/** prices exists */
			List<BFRunnerPrice> prices = new ArrayList<BFRunnerPrice>();
			if (runnerArray.length == 2) {
				String[] runnerPrices = runnerArray[1].split("~");

				double price=0, totalToBack=0,totalToLay=0;
				for (int j = 0; j < runnerPrices.length; j++) {
					if (((j + 5) % 5) == 0) {
						price = Double.parseDouble(runnerPrices[j]);
					} else if (((j + 4) % 5) == 0) {
						totalToBack = Double.parseDouble(runnerPrices[j]);
					} else if (((j + 3) % 5) == 0) {
						totalToLay = Double.parseDouble(runnerPrices[j]);
					} else if (((j + 1) % 5) == 0) {
						prices.add(new BFRunnerPrice(price,totalToBack,totalToLay));
					}
				}
				

			} 
			BFMarketRunner runner = new BFMarketRunner(selectionId,totalAmountMatched,lastPriceMatched,farSP,nearSP,actualSP,prices);
			runners.add(runner);

		}

		BFMarketRunners marketRunners = new BFMarketRunners(marketId,runners,inPlayDelay,runnersTimestamp);
		return marketRunners;
	}

	/** merge bet's matched transactions and calculate avgPrice */
	public static List<MUBet> mergBets(List<MUBet> bets) {

		Map<Long, MUBet> matchedBets = new HashMap<Long, MUBet>();
		for (MUBet bet : bets) {
			if (bet.getBetStatus() == BetStatusEnum.M) {
				if (matchedBets.get(bet.getBetId()) == null) {
					double sumPrice = 0;
					double amountSize = 0;
					XMLGregorianCalendar matchedDate = bet.getMatchedDate();

					for (MUBet betToMerge : bets) {

						if (betToMerge.getBetStatus() == BetStatusEnum.M && betToMerge.getBetId() == bet.getBetId()) {
							sumPrice = sumPrice + (betToMerge.getSize() * betToMerge.getPrice());
							amountSize = amountSize + betToMerge.getSize();
							if (betToMerge.getMatchedDate().toGregorianCalendar().getTimeInMillis() > matchedDate
									.toGregorianCalendar().getTimeInMillis()) {
								matchedDate = betToMerge.getMatchedDate();
							}
						}
					}

					bet.setPrice(sumPrice / amountSize); // set the avg price
					bet.setSize(amountSize);
					bet.setMatchedDate(matchedDate);
					matchedBets.put(bet.getBetId(), bet);
				}
			}
		}

		List<MUBet> unmatchedBets = new ArrayList<MUBet>();
		for (MUBet bet : bets) {
			if (bet.getBetStatus() == BetStatusEnum.U) {
				unmatchedBets.add(bet);
			}
		}

		ArrayList<MUBet> mergedBets = new ArrayList<MUBet>();
		mergedBets.addAll(unmatchedBets);
		mergedBets.addAll(matchedBets.values());

		return mergedBets;

	}

	/**
	 * Checks if price is validate. If not, it returns the closest correct
	 * value.
	 * 
	 * @param priceRanges -
	 *            Ranges must be closed, 1-2, 2-4, 4-6 and so on
	 * @param price -
	 *            price to be validated
	 * @param roundingMode -
	 *            define price rounding mode.
	 * @return validated price
	 */
	public static double validatePrice(List<PriceRange> priceRanges, double price, int roundingMode) {

		if (priceRanges.size() > 0) {

			// Check if price is out of bounds (below minimum or maximum of
			// priceRanges
			if (price < priceRanges.get(0).getMinimum()) {
				log.warn("Price lower than min value:" + price);
				return priceRanges.get(0).getMinimum();
			} else if (price > priceRanges.get(priceRanges.size() - 1).getMaximum()) {
				log.warn("Price bigger than max value:" + price);
				return priceRanges.get(priceRanges.size() - 1).getMaximum();
			}

			for (PriceRange priceRange : priceRanges) {
				if (price >= priceRange.getMinimum() && price <= priceRange.getMaximum()) {
					// If there is no reminder, it means that price is OK
					double reminder = price - (((priceRange.getIncrRate()*1000) * (int) ((price*1000) / (priceRange.getIncrRate()*1000))))/1000;
					if (reminder == 0) {
						return MathUtils.round(price,2);
					} else {
						if (roundingMode == ROUND_DOWN) {
							return MathUtils.round(price - reminder,2);
						} else if (roundingMode == ROUND_UP) {
							return MathUtils.round(price - reminder + priceRange.getIncrRate(),2);
						} else {
							throw new IllegalArgumentException("Rounding mode not recognized: " + roundingMode + ".");
						}
					}
				}
			}

			throw new IllegalArgumentException("Price range for price not found. Price: " + price + ".");
		} else {
			throw new IllegalArgumentException("Price range list is empty.");
		}
	}

	public static List<Double> getAllPricesForPriceRanges(List<PriceRange> priceRanges) {
		
		List<Double> prices = new ArrayList<Double>();
		
		double lastPrice = -1;
		double currentPrice = 0;
		while (currentPrice > lastPrice) {
			lastPrice = currentPrice;
			currentPrice = BetFairUtil.validatePrice(priceRanges, lastPrice+0.01, BetFairUtil.ROUND_UP);
			prices.add(currentPrice);
		}
		
		return prices;
	}
	
	/**List of valid BetFair bet prices*/
	public static List<PriceRange> getPriceRanges() {
		List<PriceRange> priceRanges = new ArrayList<PriceRange>();
		PriceRange pr1 = new PriceRange(1.01, 2.0, 0.01);
		PriceRange pr2 = new PriceRange(2.0, 3.0, 0.02);
		PriceRange pr3 = new PriceRange(3.0, 4.0, 0.05);
		PriceRange pr4 = new PriceRange(4.0, 6.0, 0.1);
		PriceRange pr5 = new PriceRange(6.0, 10.0, 0.2);
		PriceRange pr6 = new PriceRange(10.0, 20.0, 0.5);
		PriceRange pr7 = new PriceRange(20.0, 30.0, 1.0);
		PriceRange pr8 = new PriceRange(30.0, 50.0, 2.0);
		PriceRange pr9 = new PriceRange(50.0, 100.0, 5.0);
		PriceRange pr10 = new PriceRange(100.0, 1000.0, 10.0);

		priceRanges.add(pr1);
		priceRanges.add(pr2);
		priceRanges.add(pr3);
		priceRanges.add(pr4);
		priceRanges.add(pr5);
		priceRanges.add(pr6);
		priceRanges.add(pr7);
		priceRanges.add(pr8);
		priceRanges.add(pr9);
		priceRanges.add(pr10);

		return priceRanges;
	}

}
