package dk.bot.betfairservice.command;

import java.util.ArrayList;
import java.util.List;

import com.betfair.publicapi.types.exchange.v5.APIRequestHeader;
import com.betfair.publicapi.types.exchange.v5.BetStatusEnum;
import com.betfair.publicapi.types.exchange.v5.BetsOrderByEnum;
import com.betfair.publicapi.types.exchange.v5.GetMUBetsErrorEnum;
import com.betfair.publicapi.types.exchange.v5.GetMUBetsReq;
import com.betfair.publicapi.types.exchange.v5.GetMUBetsResp;
import com.betfair.publicapi.types.exchange.v5.SortOrderEnum;
import com.betfair.publicapi.v3.bfglobalservice.BFGlobalService;
import com.betfair.publicapi.v5.bfexchangeservice.BFExchangeService;

import dk.bot.betfairservice.BFCommand;
import dk.bot.betfairservice.BetFairException;
import dk.bot.betfairservice.BetFairUtil;
import dk.bot.betfairservice.model.BFBetCategoryType;
import dk.bot.betfairservice.model.BFBetStatus;
import dk.bot.betfairservice.model.BFBetType;
import dk.bot.betfairservice.model.BFMUBet;

/**
 * Get mu bets command.
 * 
 * @author daniel
 * 
 */
public class GetMUBetsCommand implements BFCommand {

	private GetMUBetsResp resp;
	private final BetStatusEnum betStatus;
	private final Integer marketId;

	public GetMUBetsCommand(BFBetStatus betStatus, Integer marketId) {
		this.marketId = marketId;
		this.betStatus = BetStatusEnum.valueOf(betStatus.value());
	}

	public void execute(BFExchangeService exchangeWebService, BFGlobalService globalWebService, String sessionToken) {

		APIRequestHeader requestHeader = new APIRequestHeader();
		requestHeader.setSessionToken(sessionToken);

		GetMUBetsReq req = new GetMUBetsReq();
		req.setHeader(requestHeader);

		req.setHeader(requestHeader);
		req.setBetStatus(betStatus);
		req.setOrderBy(BetsOrderByEnum.NONE);
		req.setSortOrder(SortOrderEnum.ASC);
		req.setRecordCount(5000);
		req.setStartRecord(0);
		if (marketId != null) {
			req.setMarketId(marketId);
		}

		resp = exchangeWebService.getMUBets(req);

	}

	public String getErrorCode() {
		return resp.getHeader().getErrorCode().name();
	}

	public String getSessionToken() {
		return resp.getHeader().getSessionToken();
	}

	public List<BFMUBet> getMUBets() {

		if (resp.getErrorCode().equals(GetMUBetsErrorEnum.OK)
				|| resp.getErrorCode().equals(GetMUBetsErrorEnum.NO_RESULTS)) {

			if (resp.getBets() != null) {

				List<com.betfair.publicapi.types.exchange.v5.MUBet> mergedBets = BetFairUtil.mergBets(resp.getBets()
						.getMUBet());

				return convertBets(mergedBets);

			} else {
				return new ArrayList<BFMUBet>();
			}

		} else {
			throw new BetFairException("getMUBets: " + resp.getErrorCode().name());
		}
	}

	/** Convert CXF generated MUBet to own MUBet model */
	private List<BFMUBet> convertBets(List<com.betfair.publicapi.types.exchange.v5.MUBet> betFairbets) {

		List<BFMUBet> muBets = new ArrayList<BFMUBet>(betFairbets.size());

		for (com.betfair.publicapi.types.exchange.v5.MUBet betFairBet : betFairbets) {
			BFMUBet bet = new BFMUBet();
			bet.setBetId(betFairBet.getBetId());
			bet.setBetStatus(BFBetStatus.fromValue(betFairBet.getBetStatus().value()));
			bet.setBetType(BFBetType.fromValue(betFairBet.getBetType().value()));
			bet.setBetCategoryType(BFBetCategoryType.fromValue(betFairBet.getBetCategoryType().value()));

			bet.setMarketId(betFairBet.getMarketId());
			bet.setSelectionId(betFairBet.getSelectionId());

			bet.setPlacedDate(betFairBet.getPlacedDate().toGregorianCalendar().getTime());
			bet.setMatchedDate(betFairBet.getMatchedDate().toGregorianCalendar().getTime());

			bet.setBspLiability(betFairBet.getBspLiability());
			bet.setSize(betFairBet.getSize());
			bet.setPrice(betFairBet.getPrice());

			muBets.add(bet);
		}

		return muBets;

	}

}
