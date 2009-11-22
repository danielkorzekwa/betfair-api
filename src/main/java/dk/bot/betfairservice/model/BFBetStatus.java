package dk.bot.betfairservice.model;


/** Represents bet status, e.g. matched, unmatched, for details see BetFair betStatus.
 * 
 * @author daniel
 *
 */
public enum BFBetStatus {

	C, L, M, MU, S, U, V;

	public String value() {
		return name();
	}

	public static BFBetStatus fromValue(String v) {
		return valueOf(v);
	}

}
