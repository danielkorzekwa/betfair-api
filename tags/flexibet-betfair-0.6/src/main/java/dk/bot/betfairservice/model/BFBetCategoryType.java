package dk.bot.betfairservice.model;



/**
 * Represents Exchange or SP bet
 * 
 * @author daniel
 * 
 */
public enum BFBetCategoryType {

	E, L, M, NONE;

	public String value() {
		return name();
	}

	public static BFBetCategoryType fromValue(String v) {
		return valueOf(v);
	}

}
