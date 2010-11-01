package dk.bot.betfairservice.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.betfair.publicapi.types.exchange.v5.APIRequestHeader;
import com.betfair.publicapi.types.exchange.v5.ArrayOfCancelBets;
import com.betfair.publicapi.types.exchange.v5.CancelBets;
import com.betfair.publicapi.types.exchange.v5.CancelBetsErrorEnum;
import com.betfair.publicapi.types.exchange.v5.CancelBetsReq;
import com.betfair.publicapi.types.exchange.v5.CancelBetsResp;
import com.betfair.publicapi.types.exchange.v5.CancelBetsResult;
import com.betfair.publicapi.types.exchange.v5.CancelBetsResultEnum;
import com.betfair.publicapi.v3.bfglobalservice.BFGlobalService;
import com.betfair.publicapi.v5.bfexchangeservice.BFExchangeService;

import dk.bot.betfairservice.BFCommand;
import dk.bot.betfairservice.BetFairException;
import dk.bot.betfairservice.model.BFBetCancelResult;
import dk.bot.betfairservice.model.BFBetCancelResultEnum;

/**
 * Cancel bet command.
 * 
 * @author daniel
 * 
 */
public class CancelBetCommand implements BFCommand {

	private final Log log = LogFactory.getLog(CancelBetCommand.class.getSimpleName());

	private CancelBetsResp resp;
	private final long betId;

	public CancelBetCommand(long betId) {
		this.betId = betId;
	}

	public void execute(BFExchangeService exchangeWebService, BFGlobalService globalWebService, String sessionToken) {

		APIRequestHeader requestHeader = new APIRequestHeader();
		requestHeader.setSessionToken(sessionToken);

		CancelBetsReq req = new CancelBetsReq();
		req.setHeader(requestHeader);

		ArrayOfCancelBets arrayOfCancelBets = new ArrayOfCancelBets();
		CancelBets cancelBets = new CancelBets();
		cancelBets.setBetId(betId);
		arrayOfCancelBets.getCancelBets().add(cancelBets);
		req.setBets(arrayOfCancelBets);

		resp = exchangeWebService.cancelBets(req);

	}

	public String getErrorCode() {
		return resp.getHeader().getErrorCode().name();
	}

	public String getSessionToken() {
		return resp.getHeader().getSessionToken();
	}

	public BFBetCancelResult cancelBet() {

		if (resp.getErrorCode().equals(CancelBetsErrorEnum.OK)
				&& (resp.getBetResults().getCancelBetsResult().get(0).getResultCode() == CancelBetsResultEnum.OK
						|| resp.getBetResults().getCancelBetsResult().get(0).getResultCode() == CancelBetsResultEnum.REMAINING_CANCELLED || resp
						.getBetResults().getCancelBetsResult().get(0).getResultCode() == CancelBetsResultEnum.TAKEN_OR_LAPSED)) {

			CancelBetsResult cancelBetsResult = resp.getBetResults().getCancelBetsResult().get(0);

			BFBetCancelResultEnum status;
			if (resp.getBetResults().getCancelBetsResult().get(0).getResultCode() == CancelBetsResultEnum.TAKEN_OR_LAPSED) {
				status = BFBetCancelResultEnum.TAKEN_OR_LAPSED;
			} else {
				status = BFBetCancelResultEnum.OK;
			}
			return new BFBetCancelResult(cancelBetsResult.getSizeCancelled(), cancelBetsResult.getSizeMatched(), status);

		} else {
			String message = "cancelBet: " + resp.getErrorCode().name() + ", bet result: "
					+ resp.getBetResults().getCancelBetsResult().get(0).getResultCode() + ",betId: " + betId;
			log.error(message);
			throw new BetFairException(message);
		}

	}
}
