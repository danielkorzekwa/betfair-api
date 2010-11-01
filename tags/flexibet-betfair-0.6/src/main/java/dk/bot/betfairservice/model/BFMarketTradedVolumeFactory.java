package dk.bot.betfairservice.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates BFMarketTradedVolume from a tradedVolume as an array of delinated string.
 * 
 * @author korzekwad
 * 
 */
public class BFMarketTradedVolumeFactory {

	public static BFMarketTradedVolume create(int marketId, String tradedVolumeString) {
		String[] runnersTradedVolumeText = tradedVolumeString.split(":");
		List<BFRunnerTradedVolume> runnersTradedVolume = new ArrayList<BFRunnerTradedVolume>();
		for (int runnerIndex = 1; runnerIndex < runnersTradedVolumeText.length; runnerIndex++) {
			String[] runnerTradedVolumeText = runnersTradedVolumeText[runnerIndex].split("\\|");
			int selectionId = Integer.parseInt(runnerTradedVolumeText[0].split("~")[0]);

			List<BFPriceTradedVolume> pricesTradedVolume = new ArrayList<BFPriceTradedVolume>();
			for (int priceIndex = 1; priceIndex < runnerTradedVolumeText.length; priceIndex++) {
				String[] priceTradedVolumeText = runnerTradedVolumeText[priceIndex].split("~");
				
				double price = Double.parseDouble(priceTradedVolumeText[0]);
				double priceTradedVolume = Double.parseDouble(priceTradedVolumeText[1]);
				pricesTradedVolume.add(new BFPriceTradedVolume(price,priceTradedVolume));
			}
			runnersTradedVolume.add(new BFRunnerTradedVolume(selectionId, pricesTradedVolume));
		}

		BFMarketTradedVolume marketTradedVolume = new BFMarketTradedVolume(marketId, runnersTradedVolume);
		return marketTradedVolume;

	}
}
