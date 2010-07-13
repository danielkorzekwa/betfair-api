package dk.bot.betfairservice.command;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.betfair.publicapi.types.exchange.v5.APIRequestHeader;
import com.betfair.publicapi.types.exchange.v5.ArrayOfPlaceBets;
import com.betfair.publicapi.types.exchange.v5.BetCategoryTypeEnum;
import com.betfair.publicapi.types.exchange.v5.BetTypeEnum;
import com.betfair.publicapi.types.exchange.v5.PlaceBets;
import com.betfair.publicapi.types.exchange.v5.PlaceBetsErrorEnum;
import com.betfair.publicapi.types.exchange.v5.PlaceBetsReq;
import com.betfair.publicapi.types.exchange.v5.PlaceBetsResp;
import com.betfair.publicapi.types.exchange.v5.PlaceBetsResult;
import com.betfair.publicapi.types.exchange.v5.PlaceBetsResultEnum;
import com.betfair.publicapi.v3.bfglobalservice.BFGlobalService;
import com.betfair.publicapi.v5.bfexchangeservice.BFExchangeService;

import dk.bot.betfairservice.BFCommand;
import dk.bot.betfairservice.BetFairException;
import dk.bot.betfairservice.BetFairUtil;
import dk.bot.betfairservice.PriceRange;
import dk.bot.betfairservice.model.BFBetType;
import dk.bot.betfairservice.model.BFSPBetPlaceResult;

/**
 * Place SP bet command.
 * 
 * @author daniel
 * 
 */
public class PlaceSPBetCommand implements BFCommand {

	private final Log log = LogFactory.getLog(PlaceSPBetCommand.class.getSimpleName());

	private PlaceBetsResp resp;

	private final int marketId;
	private final int selectionId;
	private final BetTypeEnum betType;

	private List<PriceRange> priceRanges = BetFairUtil.getPriceRanges();

	private double liability;
	private final Double limit;
	private double validatedLimit;

	public PlaceSPBetCommand(int marketId, int selectionId, BFBetType betType, double liability, Double limit) {
		this.marketId = marketId;
		this.selectionId = selectionId;
		this.betType = BetTypeEnum.fromValue(betType.value());
		this.liability = liability;
		this.limit = limit;
	}

	public void execute(BFExchangeService exchangeWebService, BFGlobalService globalWebService, String sessionToken) {

		APIRequestHeader requestHeader = new APIRequestHeader();
		requestHeader.setSessionToken(sessionToken);

		PlaceBetsReq req = new PlaceBetsReq();
		req.setHeader(requestHeader);

		ArrayOfPlaceBets arrayOfBets = new ArrayOfPlaceBets();
		PlaceBets placeBets = new PlaceBets();

		if (limit == null) {
			placeBets.setBetCategoryType(BetCategoryTypeEnum.M);
			validatedLimit=0d;
		} else {
			placeBets.setBetCategoryType(BetCategoryTypeEnum.L);

			if (betType == BetTypeEnum.B) {
				validatedLimit = BetFairUtil.validatePrice(priceRanges, limit, BetFairUtil.ROUND_DOWN);
			} else if (betType == BetTypeEnum.L) {
				validatedLimit = BetFairUtil.validatePrice(priceRanges, limit, BetFairUtil.ROUND_UP);
			} else {
				throw new IllegalArgumentException("Bet type not recognized: " + betType.value() + ".");
			}
		}

		placeBets.setPrice(validatedLimit);
		placeBets.setBspLiability(liability);
		placeBets.setSize(0d);
		placeBets.setBetType(betType);
		placeBets.setMarketId(marketId);
		placeBets.setSelectionId(selectionId);

		arrayOfBets.getPlaceBets().add(placeBets);
		req.setBets(arrayOfBets);

		resp = exchangeWebService.placeBets(req);

	}

	public String getErrorCode() {
		return resp.getHeader().getErrorCode().name();
	}

	public String getSessionToken() {
		return resp.getHeader().getSessionToken();
	}

	public BFSPBetPlaceResult placeBet() {

		if (resp.getErrorCode().equals(PlaceBetsErrorEnum.OK)
				&& resp.getBetResults().getPlaceBetsResult().get(0).getResultCode() == PlaceBetsResultEnum.OK
				&& resp.getBetResults().getPlaceBetsResult().get(0).getBetId() >= 1) {

			PlaceBetsResult placeBetsResult = resp.getBetResults().getPlaceBetsResult().get(0);

			BFSPBetPlaceResult result = new BFSPBetPlaceResult(placeBetsResult.getBetId(), resp.getHeader().getTimestamp()
					.toGregorianCalendar().getTime(), betType.value(), validatedLimit, liability);
			return result;

		} else {

			if (resp.getBetResults().getPlaceBetsResult().size() > 0) {
				String message = "placeSPBet: " + resp.getErrorCode().name() + " (bet error:"
						+ resp.getBetResults().getPlaceBetsResult().get(0).getResultCode() + "), limit:" + limit
						+ ",validated limit: " + validatedLimit + ", liability: " + liability;

				log.error(message);
				throw new BetFairException(message);
			} else {
				String message = "placeSPBet: " + resp.getErrorCode().name();

				log.error(message);
				throw new BetFairException(message);
			}
		}

	}
}
