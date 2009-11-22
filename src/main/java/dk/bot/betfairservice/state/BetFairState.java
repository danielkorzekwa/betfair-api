package dk.bot.betfairservice.state;

/**Current state of the betFair service, e.g. session token, amount of bets in last hours
 * 
 * It's a thread safe object.
 * 
 * @author daniel
 *
 */
public interface BetFairState {

	public void setSessionToken(String sessionToken);
	public String getSessionToken();
	
}
