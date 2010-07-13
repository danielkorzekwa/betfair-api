package dk.bot.betfairservice.model;

/**Type of bet: back or lay
 * 
 * @author daniel
 *
 */
public enum BFBetType {

	B, L;

	public String value() {
		return name();
	}

	public static BFBetType fromValue(String v) {
		return valueOf(v);
	}
}
