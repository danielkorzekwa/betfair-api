package dk.bot.betfairservice;

import com.betfair.publicapi.v3.bfglobalservice.BFGlobalService;
import com.betfair.publicapi.v5.bfexchangeservice.BFExchangeService;

public interface BFCommand {

	
	public void execute(BFExchangeService exchangeWebService,BFGlobalService globalWebService, String sessionToken);
	
	/**
	 * 
	 * @return OK when command executed successfully
	 */
	public String getErrorCode();
	
	public String getSessionToken();
	
}
