package dk.bot.betfairservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.betfair.publicapi.types.exchange.v5.GetAccountFundsResp;
import com.betfair.publicapi.types.global.v3.EventType;

import dk.bot.betfairservice.model.BFAccountStatementItem;
import dk.bot.betfairservice.model.BFBetCategoryType;
import dk.bot.betfairservice.model.BFBetPlaceResult;
import dk.bot.betfairservice.model.BFBetStatus;
import dk.bot.betfairservice.model.BFBetType;
import dk.bot.betfairservice.model.BFMUBet;
import dk.bot.betfairservice.model.BFMarketData;
import dk.bot.betfairservice.model.BFMarketDetails;
import dk.bot.betfairservice.model.BFMarketRunners;
import dk.bot.betfairservice.model.BFMarketTradedVolume;
import dk.bot.betfairservice.model.BFPriceTradedVolume;
import dk.bot.betfairservice.model.BFRunnerTradedVolume;
import dk.bot.betfairservice.model.BFSPBetPlaceResult;

public class BetFairServiceImplIntegrationTest {

	private static BetFairServiceImpl betFairService;

	@BeforeClass
	public static void setUp() throws BetFairException {
		System.setProperty("bfProductId", "82");
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "betfairbeans.xml" });
		betFairService = (BetFairServiceImpl) context.getBean("betFairService");
		betFairService.login();
	}

	@Test
	public void testGetEventTypes() throws BetFairException {

		List<EventType> eventTypes = betFairService.getEventTypes();

		for (EventType eventType : eventTypes) {
			System.out.println(eventType.getName() + ":" + eventType.getId());
		}
		assertTrue(eventTypes.size() > 0);
	}

	@Test
	public void testGetMarkets() throws BetFairException {
		long now = System.currentTimeMillis();

		List<BFMarketData> markets = betFairService.getMarkets(new Date(now), new Date(now + 1000l * 3600l * 24l * 7l),
				new HashSet<Integer>());

		assertEquals(true, markets.size() > 0);

		int count = 0;
		for (BFMarketData market : markets) {
			if (market.getEventHierarchy().startsWith("/1/") && market.getMarketType().equals("O")
					&& market.getMarketName().endsWith("Match Odds")) {
				System.out.println(market.getMenuPath() + ":" + market.getEventHierarchy() + ":"
						+ market.getEventDate() + ":" + market.getMarketName() + ":" + market.getMarketType() + ":"
						+ market.getTotalAmountMatched());
				count++;
			}
		}
		System.out.println("Total: " + count);
	}

	@Test
	public void testGetMarketDetails() {
		BFMarketRunners horseRaceRunners = getSPHorseRaceRunners(true);
		if (horseRaceRunners == null) {
			fail("Can't run test - market not found");
		}
		BFMarketDetails marketDetails = betFairService.getMarketDetails(horseRaceRunners.getMarketId());
		assertEquals(true, marketDetails.getRunners().size() > 0);
	}

	@Test
	public void testGetMarketTradedVolume() {
		BFMarketRunners horseRaceRunners = getSPHorseRaceRunners(true);
		if (horseRaceRunners == null) {
			fail("Can't run test - market not found");
		}
		BFMarketTradedVolume marketTradedVolume = betFairService.getMarketTradedVolume(horseRaceRunners.getMarketId());

		assertEquals(horseRaceRunners.getMarketId(), marketTradedVolume.getMarketId());
		assertTrue("No runners on a market", marketTradedVolume.getRunnerTradedVolume().size() > 0);

		double totalTradedVolume = 0;
		for (BFRunnerTradedVolume runnerTradedVolume : marketTradedVolume.getRunnerTradedVolume()) {
			assertTrue("SelectionId should be bigger than 0.", runnerTradedVolume.getSelectionId() > 0);
			for (BFPriceTradedVolume priceTradedVolume : runnerTradedVolume.getPriceTradedVolume()) {
				assertTrue("Price must be >=1.01 and <=1000", priceTradedVolume.getPrice() > 1.01
						&& priceTradedVolume.getPrice() <= 1000);
				assertTrue("Traded volume must be >=0", priceTradedVolume.getTradedVolume() >= 0);
				totalTradedVolume += priceTradedVolume.getTradedVolume();
			}
		}

		assertTrue("Total traded volume for market is 0", totalTradedVolume > 0);

	}

	@Test
	public void testGetAccountStatement() throws BetFairException {

		List<BFAccountStatementItem> statementItems = betFairService.getAccountStatement(new Date(System
				.currentTimeMillis()
				- (long) 1000 * 3600 * 24 * 1), new Date(System.currentTimeMillis()), Integer.MAX_VALUE);

		System.out.println("\nAccount items: " + statementItems.size());
		for (BFAccountStatementItem item : statementItems) {
			if (item.getMarketId() > 0) {
				assertTrue(item.getSelectionId() > 0);
				assertTrue(item.getBetId() > 0);
			}
		}
	}

	@Test
	public void testGetAccountFunds() {
		GetAccountFundsResp accountFunds = betFairService.getAccountFunds();
		System.out.println("Balance: " + accountFunds.getBalance());
		System.out.println("Avail balance: " + accountFunds.getAvailBalance());
	}

	@Test
	public void testGetMarketRunners() {

		BFMarketRunners horseRaceRunners = getSPHorseRaceRunners(true);
		if (horseRaceRunners == null) {
			fail("No market found, so can't get market runners");
		}

		assertTrue("Num of runners < 2", horseRaceRunners.getMarketRunners().size() > 1);
	}

	@Test
	public void testPlaceBetLayOnExchange() {
		/** Get market to place bet on */
		BFMarketRunners runners = getSPHorseRaceRunners(true);

		int marketId = runners.getMarketId();
		int selectionId = runners.getMarketRunners().get(0).getSelectionId();
		BFBetPlaceResult betPlaceResult = betFairService.placeBet(marketId, selectionId, BFBetType.L, 1.01, 2, true);

		assertTrue("PlaceBet error: betId=" + betPlaceResult.getBetId(), betPlaceResult.getBetId() > 0);

		betFairService.cancelBet(betPlaceResult.getBetId());
	}

	/** Place SP bet limit on close */
	@Test
	public void testPlaceBetSPBackLoC() {
		/** Get market to place bet on */
		BFMarketRunners runners = getSPHorseRaceRunners(true);

		int marketId = runners.getMarketId();
		int selectionId = runners.getMarketRunners().get(0).getSelectionId();
		BFSPBetPlaceResult betPlaceResult = betFairService.placeSPBet(marketId, selectionId, BFBetType.B, 2, 1000d);

		assertTrue("PlaceSPBet error: betId=" + betPlaceResult.getBetId(), betPlaceResult.getBetId() > 0);
	}

	/**
	 * Get horse race market which supports SB bets
	 * 
	 * @return null if not market found
	 */
	private BFMarketRunners getSPHorseRaceRunners(boolean turningInPlay) {
		long now = System.currentTimeMillis();

		List<BFMarketData> markets = betFairService.getMarkets(new Date(now + 1000l * 3600l), new Date(now + 1000l
				* 3600l * 24l * 7l), new HashSet<Integer>());

		for (BFMarketData market : markets) {
			if (market.isBsbMarket() && market.isTurningInPlay() == turningInPlay) {
				BFMarketRunners marketRunners = betFairService.getMarketRunners(market.getMarketId());
				return marketRunners;
			}
		}

		return null; // no market found

	}

	@Test
	public void testGetMUBets() {
		betFairService.getMUBets(BFBetStatus.MU);
	}

	@Test
	public void testGetMUBetsForMarketId() {
		long now = System.currentTimeMillis();
		List<BFMarketData> markets = betFairService.getMarkets(new Date(now + 1000l * 3600l), new Date(now + 1000l
				* 3600l * 24l * 7l), new HashSet<Integer>());

		betFairService.getMUBets(BFBetStatus.MU, markets.get(0).getMarketId());
	}

	@Test
	public void testGetMUBetsCheckSPBet() {
		/** Get market to place bet on */
		BFMarketRunners runners = getSPHorseRaceRunners(true);

		int marketId = runners.getMarketId();
		int selectionId = runners.getMarketRunners().get(0).getSelectionId();
		BFSPBetPlaceResult betPlaceResult = betFairService.placeSPBet(marketId, selectionId, BFBetType.B, 2, 1000d);

		assertTrue("PlaceSPBet error: betId=" + betPlaceResult.getBetId(), betPlaceResult.getBetId() > 0);

		List<BFMUBet> bets = betFairService.getMUBets(BFBetStatus.MU);
		for (BFMUBet bet : bets) {
			if (bet.getBetId() == betPlaceResult.getBetId()) {
				assertEquals(BFBetCategoryType.L, bet.getBetCategoryType());
				assertEquals(2, bet.getBspLiability(), 0);
				assertEquals(1000, bet.getPrice(), 0);
				return;
			}
		}
		fail("Placed bet not found.");
	}
}
