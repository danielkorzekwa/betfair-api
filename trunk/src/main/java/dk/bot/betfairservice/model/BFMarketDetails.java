package dk.bot.betfairservice.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class BFMarketDetails implements Serializable {

	private final int marketId;
	private final String marketName;
	private final Date marketTime;
	private final Date suspendTime;
	private final String menuPath;
	private final int numOfWinners;
	private final List<BFMarketDetailsRunner> runners;
	
	public BFMarketDetails(int marketId,String marketName,String menuPath,Date marketTime, Date marketSuspendTime, int numOfWinners,List<BFMarketDetailsRunner> marketRunners) {
		this.marketId=marketId;
		this.marketName=marketName;
		this.menuPath=menuPath;
		this.marketTime=marketTime;
		this.suspendTime=marketSuspendTime;
		this.numOfWinners=numOfWinners;
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
	
	public String getMarketName() {
		return marketName;
	}

	public int getNumOfWinners() {
		return numOfWinners;
	}

	@Override
	public String toString() {
		return "BFMarketDetails [marketId=" + marketId + ", marketName=" + marketName + ", marketTime=" + marketTime
				+ ", menuPath=" + menuPath + ", numOfWinners=" + numOfWinners + ", runners=" + runners
				+ ", suspendTime=" + suspendTime + "]";
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
