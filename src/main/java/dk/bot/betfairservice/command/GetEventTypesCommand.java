package dk.bot.betfairservice.command;

import java.util.List;

import com.betfair.publicapi.types.global.v3.APIRequestHeader;
import com.betfair.publicapi.types.global.v3.EventType;
import com.betfair.publicapi.types.global.v3.GetEventTypesReq;
import com.betfair.publicapi.types.global.v3.GetEventTypesResp;
import com.betfair.publicapi.types.global.v3.GetEventsErrorEnum;
import com.betfair.publicapi.v3.bfglobalservice.BFGlobalService;
import com.betfair.publicapi.v5.bfexchangeservice.BFExchangeService;

import dk.bot.betfairservice.BFCommand;
import dk.bot.betfairservice.BetFairException;

/** Get event types command.
 * 
 * @author daniel
 *
 */
public class GetEventTypesCommand implements BFCommand {

	private GetEventTypesResp resp;
	
	public void execute(BFExchangeService exchangeWebService, BFGlobalService globalWebService, String sessionToken) {
	
		APIRequestHeader requestHeader = new APIRequestHeader();
		requestHeader.setSessionToken(sessionToken);

		GetEventTypesReq req = new GetEventTypesReq();
		req.setHeader(requestHeader);

		resp = globalWebService.getAllEventTypes(req);
	}

	public String getErrorCode() {
		return resp.getHeader().getErrorCode().name();
	}

	public String getSessionToken() {
		return resp.getHeader().getSessionToken();
	}
	
	public List<EventType> getEventTypes() {
		if (resp.getErrorCode().equals(GetEventsErrorEnum.OK)) {
			return resp.getEventTypeItems().getEventType();
		} else {
			throw new BetFairException("getEventTypes: " + resp.getErrorCode().name());
		}
	}

}
