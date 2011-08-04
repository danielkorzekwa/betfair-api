package dk.bot.betfairservice;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import dk.bot.betfairservice.model.LoginResponse;

/**
 * Creates BetFairService object.
 * 
 * @author daniel
 * 
 */
public class DefaultBetFairServiceFactoryBean implements BetFairServiceFactoryBean {

	private BetFairServiceImpl betfairService;

	public void setUser(String user) {
		betfairService.setUser(user);
	}

	public void setPassword(String password) {
		betfairService.setPassword(password);
	}

	public void setProductId(int productId) {
		betfairService.setProductId(productId);
	}

	public DefaultBetFairServiceFactoryBean() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "spring-betfairservice.xml" });
		betfairService = (BetFairServiceImpl) context.getBean("betFairService");
	}

	public LoginResponse login() {
		return betfairService.login();
	}

	public Object getObject() throws Exception {
		return betfairService;
	}

	public Class getObjectType() {
		return BetFairServiceImpl.class;
	}

	public boolean isSingleton() {
		return true;
	}
}