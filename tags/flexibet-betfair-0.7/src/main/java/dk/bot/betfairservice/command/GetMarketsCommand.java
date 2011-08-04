package dk.bot.betfairservice.command;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.betfair.publicapi.types.exchange.v5.APIRequestHeader;
import com.betfair.publicapi.types.exchange.v5.ArrayOfInt;
import com.betfair.publicapi.types.exchange.v5.GetAllMarketsErrorEnum;
import com.betfair.publicapi.types.exchange.v5.GetAllMarketsReq;
import com.betfair.publicapi.types.exchange.v5.GetAllMarketsResp;
import com.betfair.publicapi.v3.bfglobalservice.BFGlobalService;
import com.betfair.publicapi.v5.bfexchangeservice.BFExchangeService;

import dk.bot.betfairservice.BFCommand;
import dk.bot.betfairservice.BetFairException;
import dk.bot.betfairservice.BetFairUtil;
import dk.bot.betfairservice.model.BFMarketData;

/**
 * Get markets command.
 * 
 * @author daniel
 * 
 */
public class GetMarketsCommand implements BFCommand {

	private GetAllMarketsResp resp;

	private final Date fromDate;
	private final Date toDate;
	private final Set<Integer> eventTypeIds;
	

	public GetMarketsCommand(Date fromDate, Date toDate,Set<Integer> eventTypeIds) {
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.eventTypeIds = eventTypeIds;
	}

	public void execute(BFExchangeService exchangeWebService, BFGlobalService globalWebService, String sessionToken) {

		APIRequestHeader requestHeader = new APIRequestHeader();
		requestHeader.setSessionToken(sessionToken);

		GetAllMarketsReq req = new GetAllMarketsReq();
		req.setHeader(requestHeader);

		try {
			GregorianCalendar calFrom = (GregorianCalendar) GregorianCalendar.getInstance();
			calFrom.setTime(fromDate);
			GregorianCalendar calTo = (GregorianCalendar) GregorianCalendar.getInstance();
			calTo.setTime(toDate);

			XMLGregorianCalendar xmlCalFrom = DatatypeFactory.newInstance().newXMLGregorianCalendar(calFrom);
			req.setFromDate(xmlCalFrom);

			XMLGregorianCalendar xmlCalTo = DatatypeFactory.newInstance().newXMLGregorianCalendar(calTo);
			req.setToDate(xmlCalTo);
		} catch (DatatypeConfigurationException e) {
			throw new BetFairException(e.getMessage());

		}

		if (eventTypeIds!=null && eventTypeIds.size() > 0) {

			ArrayOfInt eventTypeIdsArray = new ArrayOfInt();
			for (Integer eventTypeId : eventTypeIds) {
				eventTypeIdsArray.getInt().add(eventTypeId);
			}
			req.setEventTypeIds(eventTypeIdsArray);
		}

		resp = exchangeWebService.getAllMarkets(req);

	}

	public String getErrorCode() {
		return resp.getHeader().getErrorCode().name();
	}

	public String getSessionToken() {
		return resp.getHeader().getSessionToken();
	}

	public List<BFMarketData> getMarkets() {
		if (resp.getErrorCode().equals(GetAllMarketsErrorEnum.OK)) {
			List<BFMarketData> markets = BetFairUtil.parseMarketsData(resp.getMarketData());

			return markets;
		} else {
			throw new BetFairException("getMarkets: " + resp.getErrorCode().name());
		}
	}
}
