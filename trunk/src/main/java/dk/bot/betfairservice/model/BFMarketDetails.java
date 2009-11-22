package dk.bot.betfairservice.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class BFMarketDetails implements Serializable {

	private final int marketId;
	private final Date marketTime;
	private final Date suspendTime;
	private final String menuPath;
	private final List<BFMarketDetailsRunner> runners;
	
	public BFMarketDetails(int marketId,String menuPath,Date marketTime, Date marketSuspendTime, List<BFMarketDetailsRunner> marketRunners) {
		this.marketId=marketId;
		this.menuPath=menuPath;
		this.marketTime=marketTime;
		this.suspendTime=marketSuspendTime;
		this.runners = marketRunners;
	}
	
	public int getMarketId() {
		return marketId;
	}
	
	public String getMenuPath() {
		return menuPath;
	}

	public Date getMarketTime() {
		return marketTime;
	}

	public Date getMarketSuspendTime() {
		return suspendTime;
	}

	public List<BFMarketDetailsRunner> getRunners() {
		return runners;
	}
	
	@Override
	public String toString() {
		StringBuffer text = new StringBuffer();

		for (int i = 0; i < runners.size(); i++) {
			text.append(runners.get(i).getSelectionName());
			if (i < runners.size() - 1) {
				text.append(":");
			}
		}

		return text.toString();
	}
	
	/**
	 * 
	 * @param selectionId
	 * @return Null if runner not found for selectionId
	 */
	public String getSelectionName(int selectionId) {
		for(BFMarketDetailsRunner runner: runners) {
			if(selectionId == runner.getSelectionId()) {
				return runner.getSelectionName();
			}
		}
		return null;
	}

}
