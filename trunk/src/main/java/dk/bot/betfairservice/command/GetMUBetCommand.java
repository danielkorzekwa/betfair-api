package dk.bot.betfairservice.command;

import java.util.ArrayList;
import java.util.List;

import com.betfair.publicapi.types.exchange.v5.APIRequestHeader;
import com.betfair.publicapi.types.exchange.v5.Bet;
import com.betfair.publicapi.types.exchange.v5.GetBetErrorEnum;
import com.betfair.publicapi.types.exchange.v5.GetBetReq;
import com.betfair.publicapi.types.exchange.v5.GetBetResp;
import com.betfair.publicapi.v3.bfglobalservice.BFGlobalService;
import com.betfair.publicapi.v5.bfexchangeservice.BFExchangeService;

import dk.bot.betfairservice.BFCommand;
import dk.bot.betfairservice.BetFairException;
import dk.bot.betfairservice.model.BFBetCategoryType;
import dk.bot.betfairservice.model.BFBetStatus;
import dk.bot.betfairservice.model.BFBetType;
import dk.bot.betfairservice.model.BFMUBet;

/**
 * This is a command for betfairApi.getBet operation.
 * 
 * From betfair api documentation(docs.developer.betfair.com): The API GetBet
 * service allows you to retrieve information about a particular bet. Each
 * request will retrieve all components of the desired bet.
 * 
 * @author korzekwad
 * 
 */
public class GetMUBetCommand implements BFCommand {

	private final long betId;

	private GetBetResp resp;

	public GetMUBetCommand(long betId) {
		this.betId = betId;
	}

	@Override
	public void execute(BFExchangeService exchangeWebService,
			BFGlobalService globalWebService, String sessionToken) {

		APIRequestHeader requestHeader = new APIRequestHeader();
		requestHeader.setSessionToken(sessionToken);

		GetBetReq req = new GetBetReq();
		req.setHeader(requestHeader);

		req.setHeader(requestHeader);
		req.setBetId(betId);

		resp = exchangeWebService.getBet(req);

	}

	public String getErrorCode() {
		return resp.getHeader().getErrorCode().name();
	}

	public String getSessionToken() {
		return resp.getHeader().getSessionToken();
	}

	public List<BFMUBet> getMUBet() {
		if (resp.getErrorCode().equals(GetBetErrorEnum.OK)
				|| resp.getErrorCode().equals(GetBetErrorEnum.NO_RESULTS)) {

			Bet bet = resp.getBet();
			ArrayList<BFMUBet> betDetails = new ArrayList<BFMUBet>();

			/** Add unmatched bet. */
			if (bet.getRemainingSize() > 0) {
				BFMUBet unmatchedBet = createBetTemplate(bet);
				unmatchedBet.setBetStatus(BFBetStatus.U);
				unmatchedBet.setSize(bet.getRemainingSize());
				unmatchedBet.setPrice(bet.getPrice());

				betDetails.add(unmatchedBet);
			}

			/** Add matched portion of a bet. */
			if (bet.getMatchedSize() > 0) {
				BFMUBet matchedBet = createBetTemplate(bet);
				matchedBet.setBetStatus(BFBetStatus.M);
				matchedBet.setSize(bet.getMatchedSize());
				matchedBet.setPrice(bet.getAvgPrice());

				betDetails.add(matchedBet);
			}
			return betDetails;

		} else {
			throw new BetFairException("getMUBet: "
					+ resp.getErrorCode().name());
		}

	}

	private BFMUBet createBetTemplate(Bet bet) {
		BFMUBet betTemplate = new BFMUBet();
		betTemplate.setBetId(bet.getBetId());

		betTemplate.setBetType(BFBetType.fromValue(bet.getBetType().value()));
		betTemplate.setBetCategoryType(BFBetCategoryType.fromValue(bet
				.getBetCategoryType().value()));

		betTemplate.setMarketId(bet.getMarketId());
		betTemplate.setSelectionId(bet.getSelectionId());

		betTemplate.setPlacedDate(bet.getPlacedDate().toGregorianCalendar()
				.getTime());
		betTemplate.setMatchedDate(bet.getMatchedDate().toGregorianCalendar()
				.getTime());

		return betTemplate;
	}
}
