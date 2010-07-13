package dk.bot.betfairservice.command;

import java.util.Date;

import com.betfair.publicapi.types.exchange.v5.APIRequestHeader;
import com.betfair.publicapi.types.exchange.v5.GetCompleteMarketPricesCompressedReq;
import com.betfair.publicapi.types.exchange.v5.GetCompleteMarketPricesCompressedResp;
import com.betfair.publicapi.types.exchange.v5.GetCompleteMarketPricesErrorEnum;
import com.betfair.publicapi.v3.bfglobalservice.BFGlobalService;
import com.betfair.publicapi.v5.bfexchangeservice.BFExchangeService;

import dk.bot.betfairservice.BFCommand;
import dk.bot.betfairservice.BetFairException;
import dk.bot.betfairservice.BetFairUtil;
import dk.bot.betfairservice.model.BFMarketRunners;

/**
 * Get market runners command.
 * 
 * @author daniel
 * 
 */
public class GetMarketRunnersCommand implements BFCommand {

	private GetCompleteMarketPricesCompressedResp resp;
	private final int marketId;

	public GetMarketRunnersCommand(int marketId) {
		this.marketId = marketId;
	}

	public void execute(BFExchangeService exchangeWebService, BFGlobalService globalWebService, String sessionToken) {

		APIRequestHeader requestHeader = new APIRequestHeader();
		requestHeader.setSessionToken(sessionToken);

		GetCompleteMarketPricesCompressedReq req = new GetCompleteMarketPricesCompressedReq();
		req.setHeader(requestHeader);

		req.setMarketId(marketId);

		resp = exchangeWebService.getCompleteMarketPricesCompressed(req);

	}

	public String getErrorCode() {
		return resp.getHeader().getErrorCode().name();
	}

	public String getSessionToken() {
		return resp.getHeader().getSessionToken();
	}

	/**
	 * 
	 * @return null if market is closed/suspended.
	 */
	public BFMarketRunners getMarketRunners() {
		
		if (resp.getErrorCode().equals(GetCompleteMarketPricesErrorEnum.OK)) {
			BFMarketRunners marketRunners = BetFairUtil.parseMarketPrices(resp.getCompleteMarketPrices(),new Date(System.currentTimeMillis()));
			if(marketRunners.getMarketRunners().size()<2) {
				throw new BetFairException("getMarketRunners amount < 2. MarketId: " + marketId + ", runners: " + marketRunners.getMarketRunners().size());
			}
			
			return marketRunners;
		} else if (resp.getErrorCode().equals(GetCompleteMarketPricesErrorEnum.EVENT_CLOSED) || resp.getErrorCode().equals(GetCompleteMarketPricesErrorEnum.EVENT_SUSPENDED)) {
			return null;
		} else {
			throw new BetFairException("getMarketRunners: " + resp.getErrorCode().name());
		}

	}
}
