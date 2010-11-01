package dk.bot.betfairservice.model;

import java.io.Serializable;

public class BFMarketDetailsRunner implements Serializable {

	private final int selectionId;
	private final String selectionName;

	public BFMarketDetailsRunner(int selectionId, String selectionName) {
		this.selectionId = selectionId;
		this.selectionName = selectionName;
	}

	public int getSelectionId() {
		return selectionId;
	}

	public String getSelectionName() {
		return selectionName;
	}

	@Override
	public String toString() {
		return "BFMarketDetailsRunner [selectionId=" + selectionId + ", selectionName=" + selectionName + "]";
	}

}
