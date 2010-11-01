package dk.bot.betfairservice;

public class BetFairException extends RuntimeException{

	public BetFairException(String message) {
		super(message);
	}
	
	public BetFairException(String message,Throwable t) {
		super(message,t);
	}
	
	public BetFairException(Throwable t) {
		super(t);
	}
}
