package dk.bot.betfairservice.command;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.betfair.publicapi.types.exchange.v5.APIRequestHeader;
import com.betfair.publicapi.types.exchange.v5.ArrayOfPlaceBets;
import com.betfair.publicapi.types.exchange.v5.BetCategoryTypeEnum;
import com.betfair.publicapi.types.exchange.v5.BetPersistenceTypeEnum;
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
import dk.bot.betfairservice.model.BFBetPlaceResult;
import dk.bot.betfairservice.model.BFBetType;

/**
 * Place bet command.
 * 
 * @author daniel
 * 
 */
public class PlaceBetCommand implements BFCommand {

	private final Log log = LogFactory.getLog(PlaceBetCommand.class.getSimpleName());

	private PlaceBetsResp resp;

	private final int marketId;
	private final int selectionId;
	private final BetTypeEnum betType;
	private final double price;
	private double size;
	
	private double validatedPrice;
	private List<PriceRange> priceRanges = BetFairUtil.getPriceRanges();

	public PlaceBetCommand(int marketId, int selectionId, BFBetType betType, double price, double size) {
		this.marketId = marketId;
		this.selectionId = selectionId;
		this.betType = BetTypeEnum.fromValue(betType.value());
		this.price = price;
		this.size = size;
	}

	public void execute(BFExchangeService exchangeWebService, BFGlobalService globalWebService, String sessionToken) {

		APIRequestHeader requestHeader = new APIRequestHeader();
		requestHeader.setSessionToken(sessionToken);

		PlaceBetsReq req = new PlaceBetsReq();
		req.setHeader(requestHeader);

		ArrayOfPlaceBets arrayOfBets = new ArrayOfPlaceBets();

		PlaceBets placeBets = new PlaceBets();
		if (betType == BetTypeEnum.B) {
			validatedPrice = BetFairUtil.validatePrice(priceRanges, price, BetFairUtil.ROUND_DOWN);
		} else if (betType == BetTypeEnum.L) {
			validatedPrice = BetFairUtil.validatePrice(priceRanges, price, BetFairUtil.ROUND_UP);
		} else {
			throw new IllegalArgumentException("Bet type not recognized: " + betType.value() + ".");
		}

		placeBets.setBspLiability(0d);
		placeBets.setPrice(validatedPrice);
		placeBets.setSize(size);
		placeBets.setBetCategoryType(BetCategoryTypeEnum.E);
		placeBets.setBetPersistenceType(BetPersistenceTypeEnum.NONE);
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

	public BFBetPlaceResult placeBet() {

		if (resp.getErrorCode().equals(PlaceBetsErrorEnum.OK)
				&& resp.getBetResults().getPlaceBetsResult().get(0).getResultCode() == PlaceBetsResultEnum.OK
				&& resp.getBetResults().getPlaceBetsResult().get(0).getBetId() >= 1) {

			PlaceBetsResult placeBetsResult = resp.getBetResults().getPlaceBetsResult().get(0);

			BFBetPlaceResult result = new BFBetPlaceResult(placeBetsResult.getBetId(), resp.getHeader().getTimestamp()
					.toGregorianCalendar().getTime(), betType.value(), validatedPrice, size, placeBetsResult
					.getAveragePriceMatched(), placeBetsResult.getSizeMatched());
			return result;

		} else {

			if (resp.getBetResults()!=null && resp.getBetResults().getPlaceBetsResult().size() > 0) {
				String message = "placeBet: " + resp.getErrorCode().name() + " (bet error:"
						+ resp.getBetResults().getPlaceBetsResult().get(0).getResultCode() + "), price:" + price
						+ ",validated price: " + validatedPrice + ", size: " + size;

				log.error(message);
				throw new BetFairException(message);
			} else {
				String message = "placeBet: " + resp.getErrorCode().name();

				log.error(message);
				throw new BetFairException(message);
			}
		}

	}
}
