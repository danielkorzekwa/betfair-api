package dk.bot.betfairservice.command;

import com.betfair.publicapi.types.exchange.v5.APIRequestHeader;
import com.betfair.publicapi.types.exchange.v5.GetMarketTradedVolumeCompressedErrorEnum;
import com.betfair.publicapi.types.exchange.v5.GetMarketTradedVolumeCompressedReq;
import com.betfair.publicapi.types.exchange.v5.GetMarketTradedVolumeCompressedResp;
import com.betfair.publicapi.v3.bfglobalservice.BFGlobalService;
import com.betfair.publicapi.v5.bfexchangeservice.BFExchangeService;

import dk.bot.betfairservice.BFCommand;
import dk.bot.betfairservice.BetFairException;
import dk.bot.betfairservice.model.BFMarketTradedVolume;
import dk.bot.betfairservice.model.BFMarketTradedVolumeFactory;

/** Returns traded volume at each price on all of the runners in a particular market.
 * 
 * @author korzekwad
 *
 */
public class GetMarketTradedVolumeCommand implements BFCommand {

	private GetMarketTradedVolumeCompressedResp resp;
	private final int marketId;

	public GetMarketTradedVolumeCommand(int marketId) {
		this.marketId = marketId;
	}

	public void execute(BFExchangeService exchangeWebService, BFGlobalService globalWebService, String sessionToken) {

		APIRequestHeader requestHeader = new APIRequestHeader();
		requestHeader.setSessionToken(sessionToken);

		GetMarketTradedVolumeCompressedReq req = new GetMarketTradedVolumeCompressedReq();
		req.setHeader(requestHeader);

		req.setMarketId(marketId);
		
		resp = exchangeWebService.getMarketTradedVolumeCompressed(req);

	}

	public String getErrorCode() {
		return resp.getHeader().getErrorCode().name();
	}

	public String getSessionToken() {
		return resp.getHeader().getSessionToken();
	}

	public BFMarketTradedVolume getMarketTradedVolume() {

		if (resp.getErrorCode().equals(GetMarketTradedVolumeCompressedErrorEnum.OK)) {
			BFMarketTradedVolume marketTradedVolume = BFMarketTradedVolumeFactory.create(marketId,resp.getTradedVolume());
			return marketTradedVolume;
			
		}
		else {
			throw new BetFairException("getMarketTradedVolume: " + resp.getErrorCode().name());
		}		
	}
}
