package dk.bot.betfairservice;

public class PriceRange {

	private double minimum;
	private double maximum;
	private double incrRate;
	
	public PriceRange(double minimum, double maximum, double incrRate) {
		this.minimum = minimum;
		this.maximum = maximum;
		this.incrRate = incrRate;		
	}
	public double getMinimum() {
		return minimum;
	}
	public void setMinimum(double minimum) {
		this.minimum = minimum;
	}
	public double getMaximum() {
		return maximum;
	}
	public void setMaximum(double maximum) {
		this.maximum = maximum;
	}
	public double getIncrRate() {
		return incrRate;
	}
	public void setIncrRate(double incrRate) {
		this.incrRate = incrRate;
	}	

}
