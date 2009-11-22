package dk.bot.betfairservice.state;


/**  It's a thread safe object.
 * 
 * @author daniel
 *
 */
public class BetFairStateImpl implements BetFairState{

	private String sessionToken;
	
	public synchronized void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public synchronized String getSessionToken() {
		return sessionToken;
	}
	
	
}
