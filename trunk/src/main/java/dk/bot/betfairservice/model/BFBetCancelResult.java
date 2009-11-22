package dk.bot.betfairservice.model;


/** Bet cancel result object.
 * 
 * @author daniel
 *
 */
public class BFBetCancelResult {

	/** Amount cancelled by cancel operation. O if status!=OK*/
	final private double sizeCancelled;
	
	/**Amount of original bet matched since placement.*/
	final private double sizeMatched;

	final private BFBetCancelResultEnum status;
	
	public BFBetCancelResult(double sizeCancelled,double sizeMatched, BFBetCancelResultEnum status) {
		this.sizeCancelled = sizeCancelled;
		this.sizeMatched = sizeMatched;
		this.status = status;
	}

	public double getSizeCancelled() {
		return sizeCancelled;
	}

	public double getSizeMatched() {
		return sizeMatched;
	}

	public BFBetCancelResultEnum getStatus() {
		return status;
	}
}
