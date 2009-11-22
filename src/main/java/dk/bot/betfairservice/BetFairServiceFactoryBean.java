package dk.bot.betfairservice;

import org.springframework.beans.factory.FactoryBean;

import dk.bot.betfairservice.model.LoginResponse;

/**
 * Creates BetFairService object.
 * 
 * @author daniel
 * 
 */
public interface BetFairServiceFactoryBean extends FactoryBean{
	
	public void setUser(String user);

	public void setPassword(String password);

	public void setProductId(int productId);
	
	public LoginResponse login();

}
