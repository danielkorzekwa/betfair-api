package dk.bot.betfairservice.model;

import java.io.Serializable;
import java.util.Date;


/**
 * Represents a bet on a betting exchange.
 * 
 * @author daniel
 * 
 */
public class BFMUBet implements Serializable,Comparable<BFMUBet>{

	    private long betId;
	    private BFBetStatus betStatus;
	    private BFBetType betType;
	    private BFBetCategoryType betCategoryType;
	    
	    private int marketId;
	    private int selectionId;
	    
	    private Date placedDate;
	    private Date matchedDate;
	    
	    /**Valid for SP bets only*/
	    private double bspLiability;
	    
	    private double size;
	    private double price;
		public long getBetId() {
			return betId;
		}
		public void setBetId(long betId) {
			this.betId = betId;
		}
		public BFBetStatus getBetStatus() {
			return betStatus;
		}
		public void setBetStatus(BFBetStatus betStatus) {
			this.betStatus = betStatus;
		}
		public BFBetType getBetType() {
			return betType;
		}
		public void setBetType(BFBetType betType) {
			this.betType = betType;
		}
		public int getMarketId() {
			return marketId;
		}
		public void setMarketId(int marketId) {
			this.marketId = marketId;
		}
		public int getSelectionId() {
			return selectionId;
		}
		public void setSelectionId(int selectionId) {
			this.selectionId = selectionId;
		}
		public Date getPlacedDate() {
			return placedDate;
		}
		public void setPlacedDate(Date placedDate) {
			this.placedDate = placedDate;
		}
		public Date getMatchedDate() {
			return matchedDate;
		}
		public void setMatchedDate(Date matchedDate) {
			this.matchedDate = matchedDate;
		}
		public double getSize() {
			return size;
		}
		public void setSize(double size) {
			this.size = size;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
		
		public BFBetCategoryType getBetCategoryType() {
			return betCategoryType;
		}
		public void setBetCategoryType(BFBetCategoryType betCategoryType) {
			this.betCategoryType = betCategoryType;
		}
		
		public double getBspLiability() {
			return bspLiability;
		}
		public void setBspLiability(double bspLiability) {
			this.bspLiability = bspLiability;
		}
		public int compareTo(BFMUBet o) {
			if(getBetStatus()==BFBetStatus.M && o.getBetStatus()==BFBetStatus.M) {
				return o.getMatchedDate().compareTo(getMatchedDate());
			}
			else if(getBetStatus()==BFBetStatus.U && o.getBetStatus()==BFBetStatus.U){
				return o.getPlacedDate().compareTo(getPlacedDate());
			}
			else if(getBetStatus()==BFBetStatus.M && o.getBetStatus()==BFBetStatus.U){
				return -1;
			}
			else if(getBetStatus()==BFBetStatus.U && o.getBetStatus()==BFBetStatus.M){
				return 1;
			}
			else {
				return 0;
			}
			
		}
		@Override
		public String toString() {
			return "BFMUBet [betCategoryType=" + betCategoryType + ", betId=" + betId
					+ ", betStatus=" + betStatus + ", betType=" + betType
					+ ", bspLiability=" + bspLiability + ", marketId=" + marketId
					+ ", matchedDate=" + matchedDate + ", placedDate=" + placedDate
					+ ", price=" + price + ", selectionId=" + selectionId + ", size="
					+ size + "]";
		}
		
		
		
}
