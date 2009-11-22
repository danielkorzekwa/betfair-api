package dk.bot.betfairservice;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.betfair.publicapi.types.exchange.v5.GetAccountFundsResp;
import com.betfair.publicapi.types.global.v3.EventType;

import dk.bot.betfairservice.model.BFAccountStatementItem;
import dk.bot.betfairservice.model.BFBetCancelResult;
import dk.bot.betfairservice.model.BFBetPlaceResult;
import dk.bot.betfairservice.model.BFBetStatus;
import dk.bot.betfairservice.model.BFBetType;
import dk.bot.betfairservice.model.BFMUBet;
import dk.bot.betfairservice.model.BFMarketData;
import dk.bot.betfairservice.model.BFMarketDetails;
import dk.bot.betfairservice.model.BFMarketRunners;
import dk.bot.betfairservice.model.BFSPBetPlaceResult;
import dk.bot.betfairservice.model.LoginResponse;

/**
 * BetFair API adapter to connect BetFair server and execute operations such a getMarkets, placeBet, etc.
 * 
 * @author daniel
 * 
 */
public interface BetFairService {

	public LoginResponse login();

	public List<EventType> getEventTypes();

	/**
	 * 
	 * @param fromDate
	 * @param toDate
	 * @param eventTypeIds
	 *            If not empty then markets for given event types are returned, e.g. for soccer: 1.
	 * 
	 * 
	 * @throws BetFairException
	 */
	public List<BFMarketData> getMarkets(Date fromDate, Date toDate, Set<Integer> eventTypeIds);

	/**
	 * 
	 * @param marketId
	 * @return null if market is closed/suspended.
	 */
	public BFMarketRunners getMarketRunners(int marketId);

	/**
	 * Returns current bets for a given BetStatus
	 * 
	 * @param betStatus
	 * @return
	 * @throws BetFairException
	 */
	public List<BFMUBet> getMUBets(BFBetStatus betStatus);
	
	/**
	 * Returns current bets for a given BetStatus and marketId
	 * 
	 * @param betStatus
	 * @return
	 * @throws BetFairException
	 */
	public List<BFMUBet> getMUBets(BFBetStatus betStatus, int marketId);

	public BFMarketDetails getMarketDetails(int marketId) throws BetFairException;

	public BFBetCancelResult cancelBet(long betId) throws BetFairException;

	/**
	 * 
	 * @param marketId
	 * @param selectionId
	 * @param betType
	 * @param price
	 * @param size
	 * @param checkTxLimit
	 *            If true then tx counter is incremented and limit is checked. If limit is reached then exception is
	 *            thrown. If checkTxLimit is false then tx counter is not incremented and tx limit is not checked.
	 * @return
	 * @throws BetFairException
	 */
	public BFBetPlaceResult placeBet(int marketId, int selectionId, BFBetType betType, double price, double size,
			boolean checkTxLimit) throws BetFairException;

	/**
	 * 
	 * @param marketId
	 * @param selectionId
	 * @param betType
	 * @param liability
	 *            This is the maximum amount of money you want to risk for a BSP bet. The minimum amount for a back bet
	 *            is £2. The minimum amount for a lay bet is £10.
	 * @param limit
	 *            if not null then SP Limit on Price bet is placed
	 * @return
	 * @throws BetFairException
	 */
	public BFSPBetPlaceResult placeSPBet(int marketId, int selectionId, BFBetType betType, double liability,
			Double limit) throws BetFairException;

	/** Get account statement for UK wallet */
	public List<BFAccountStatementItem> getAccountStatement(Date startDate, Date endDate, int recordCount);

	public GetAccountFundsResp getAccountFunds();
	
	/** Returns BetFairService usage statistics. */
	public BetFairServiceInfo getBetFairServiceInfo();

}
