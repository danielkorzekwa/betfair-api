package dk.bot.betfairservice.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.betfair.publicapi.types.exchange.v5.APIRequestHeader;
import com.betfair.publicapi.types.exchange.v5.AccountStatementIncludeEnum;
import com.betfair.publicapi.types.exchange.v5.AccountStatementItem;
import com.betfair.publicapi.types.exchange.v5.GetAccountStatementErrorEnum;
import com.betfair.publicapi.types.exchange.v5.GetAccountStatementReq;
import com.betfair.publicapi.types.exchange.v5.GetAccountStatementResp;
import com.betfair.publicapi.v3.bfglobalservice.BFGlobalService;
import com.betfair.publicapi.v5.bfexchangeservice.BFExchangeService;

import dk.bot.betfairservice.BFCommand;
import dk.bot.betfairservice.BetFairException;
import dk.bot.betfairservice.model.BFAccountStatementItem;

/**
 * Get account statement command.
 * 
 * @author daniel
 * 
 */
public class GetAccountStatementCommand implements BFCommand {

	private final Log log = LogFactory.getLog(GetAccountStatementCommand.class.getSimpleName());

	private GetAccountStatementResp resp;
	private final Date startDate;
	private final Date endDate;
	private final int recordCount;

	public GetAccountStatementCommand(Date startDate, Date endDate, int recordCount) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.recordCount = recordCount;
	}

	public void execute(BFExchangeService exchangeWebService, BFGlobalService globalWebService, String sessionToken) {

		APIRequestHeader requestHeader = new APIRequestHeader();
		requestHeader.setSessionToken(sessionToken);

		GetAccountStatementReq req = new GetAccountStatementReq();
		req.setHeader(requestHeader);

		try {
			GregorianCalendar calFrom = (GregorianCalendar) GregorianCalendar.getInstance();
			calFrom.setTime(startDate);
			GregorianCalendar calTo = (GregorianCalendar) GregorianCalendar.getInstance();
			calTo.setTime(endDate);

			XMLGregorianCalendar xmlCalFrom = DatatypeFactory.newInstance().newXMLGregorianCalendar(calFrom);
			req.setStartDate(xmlCalFrom);
			XMLGregorianCalendar xmlCalTo = DatatypeFactory.newInstance().newXMLGregorianCalendar(calTo);
			req.setEndDate(xmlCalTo);

		} catch (DatatypeConfigurationException e) {
			throw new BetFairException(e.getMessage());

		}

		req.setStartRecord(0);
		req.setRecordCount(recordCount);
		req.setItemsIncluded(AccountStatementIncludeEnum.ALL);

		resp = exchangeWebService.getAccountStatement(req);
	}

	public String getErrorCode() {
		return resp.getHeader().getErrorCode().name();
	}

	public String getSessionToken() {
		return resp.getHeader().getSessionToken();
	}

	public List<BFAccountStatementItem> getAccountStatementItems() {
		if (resp.getErrorCode().equals(GetAccountStatementErrorEnum.OK)) {
			List<BFAccountStatementItem> items = new ArrayList<BFAccountStatementItem>();

			for (int i = 0; i < resp.getItems().getAccountStatementItem().size(); i++) {
				AccountStatementItem betFairItem = resp.getItems().getAccountStatementItem().get(i);

				BFAccountStatementItem item = new BFAccountStatementItem();

				/** if selectionId=0 then it's a commision */
				if (betFairItem.getSelectionId() > 0) {
					item.setSelectionId(betFairItem.getSelectionId());
					item.setSelectionName(betFairItem.getSelectionName());
				}
				/** found selectionId for commission item */
				else if (betFairItem.getBetId() > 0) {
					boolean found = false;
					for (int j = i + 1; j < resp.getItems().getAccountStatementItem().size(); j++) {
						AccountStatementItem nextItem = resp.getItems().getAccountStatementItem().get(j);

						if (betFairItem.getBetId() == nextItem.getBetId() && nextItem.getSelectionId() > 0) {
							item.setSelectionId(nextItem.getSelectionId());
							item.setSelectionName(nextItem.getSelectionName());
							item.setCommission(true);
							found = true;
							break;
						}
					}

					if (!found) {
						log.warn("AccountStatementItem not found for commission item with betId:"
								+ betFairItem.getBetId() + ",amount:" + betFairItem.getAmount() + ",settled date: "
								+ betFairItem.getSettledDate() + ",market type:" + betFairItem.getMarketType());
						continue; // do not add this account item - it's probably multiples
					}

				}

				item.setMarketId(betFairItem.getEventId());
				item.setMarketName(betFairItem.getMarketName());
				item.setMarketType(betFairItem.getMarketType().name());
				item.setFullMarketName(betFairItem.getFullMarketName());

				item.setBetId(betFairItem.getBetId());

				item.setEventTypeId(betFairItem.getEventTypeId());
				item.setBetCategoryType(betFairItem.getBetCategoryType().name());
				item.setBetType(betFairItem.getBetType().name());
				item.setBetSize(betFairItem.getBetSize());
				item.setAvgPrice(betFairItem.getAvgPrice());

				item.setPlacedDate(betFairItem.getPlacedDate().toGregorianCalendar().getTime());
				item.setStartDate(betFairItem.getStartDate().toGregorianCalendar().getTime());
				item.setSettledDate(betFairItem.getSettledDate().toGregorianCalendar().getTime());

				item.setAmount(betFairItem.getAmount());

				items.add(item);

			}

			return items;
		} else {
			throw new BetFairException("getAccountStatementItems: " + resp.getErrorCode().name());
		}
	}

}
