package dk.bot.betfairservice.model;

import java.io.Serializable;
import java.util.List;

/**Represents traded volume at each price on all of the runners in a particular market.
 * 
 * @author korzekwad
 *
 */
public class BFMarketTradedVolume implements Serializable{

	private final int marketId;
	private final List<BFRunnerTradedVolume> runnerTradedVolume;

	public BFMarketTradedVolume(int marketId,List<BFRunnerTradedVolume> runnerTradedVolume) {
		this.marketId = marketId;
		this.runnerTradedVolume = runnerTradedVolume;
	}

	public int getMarketId() {
		return marketId;
	}

	public List<BFRunnerTradedVolume> getRunnerTradedVolume() {
		return runnerTradedVolume;
	}

	@Override
	public String toString() {
		return "BFMarketTradedVolume [marketId=" + marketId + ", runnerTradedVolume=" + runnerTradedVolume + "]";
	}
}
