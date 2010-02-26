package dk.bot.betfairservice.command;

import java.util.ArrayList;
import java.util.List;

import com.betfair.publicapi.types.exchange.v5.APIRequestHeader;
import com.betfair.publicapi.types.exchange.v5.GetMarketErrorEnum;
import com.betfair.publicapi.types.exchange.v5.GetMarketReq;
import com.betfair.publicapi.types.exchange.v5.GetMarketResp;
import com.betfair.publicapi.types.exchange.v5.Market;
import com.betfair.publicapi.types.exchange.v5.Runner;
import com.betfair.publicapi.v3.bfglobalservice.BFGlobalService;
import com.betfair.publicapi.v5.bfexchangeservice.BFExchangeService;

import dk.bot.betfairservice.BFCommand;
import dk.bot.betfairservice.BetFairException;
import dk.bot.betfairservice.model.BFMarketDetails;
import dk.bot.betfairservice.model.BFMarketDetailsRunner;

/**
 * Get market details command.
 * 
 * @author daniel
 * 
 */
public class GetMarketDetailsCommand implements BFCommand {

	private GetMarketResp resp;
	private final int marketId;

	public GetMarketDetailsCommand(int marketId) {
		this.marketId = marketId;
	}

	public void execute(BFExchangeService exchangeWebService, BFGlobalService globalWebService, String sessionToken) {

		APIRequestHeader requestHeader = new APIRequestHeader();
		requestHeader.setSessionToken(sessionToken);

		GetMarketReq req = new GetMarketReq();
		req.setHeader(requestHeader);

		req.setMarketId(marketId);

		resp = exchangeWebService.getMarket(req);

	}

	public String getErrorCode() {
		return resp.getHeader().getErrorCode().name();
	}

	public String getSessionToken() {
		return resp.getHeader().getSessionToken();
	}

	public BFMarketDetails getMarketDetails() {

		if (resp.getErrorCode().equals(GetMarketErrorEnum.OK)) {
			Market market = resp.getMarket();
			
			List<BFMarketDetailsRunner> marketRunners = new ArrayList<BFMarketDetailsRunner>();
			for(Runner runner : market.getRunners().getRunner()) {
				marketRunners.add(new BFMarketDetailsRunner(runner.getSelectionId(),runner.getName()));
			}
			
			return new BFMarketDetails(market.getMarketId(),market.getName(),market.getMenuPath(),market.getMarketTime().toGregorianCalendar().getTime(),market.getMarketSuspendTime().toGregorianCalendar().getTime(),market.getNumberOfWinners(),marketRunners);		
		}
		else {
			throw new BetFairException("getMarketDetails: " + resp.getErrorCode().name());
		}

		
	}

}
