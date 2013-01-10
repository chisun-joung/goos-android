package com.jooyunghan.auctionsniper.unittest;

import static org.hamcrest.Matchers.equalTo;
import junit.framework.TestCase;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;

import com.jooyunghan.auctionsniper.Auction;
import com.jooyunghan.auctionsniper.AuctionHouse;
import com.jooyunghan.auctionsniper.AuctionSniper;
import com.jooyunghan.auctionsniper.Item;
import com.jooyunghan.auctionsniper.ui.SniperCollector;
import com.jooyunghan.auctionsniper.ui.SniperLauncher;

public class SniperLauncherTest extends TestCase {
	private final Mockery context = new Mockery();
	private final Auction auction = context.mock(Auction.class);
	private final AuctionHouse auctionHouse = context.mock(AuctionHouse.class);
	private final SniperCollector collector = context
			.mock(SniperCollector.class);
	protected final States auctionStates = context.states("auction state")
			.startsAs("not joined");

	private SniperLauncher launcher = new SniperLauncher(auctionHouse,
			collector);

	@Override
	protected void tearDown() throws Exception {
		context.assertIsSatisfied();
		super.tearDown();
	}

	public void testAddsNewSniperToCollectorAndThenJoinsAuction()
			throws Exception {
		final Item item = new Item("item 123", 123);
		context.checking(new Expectations() {
			{
				allowing(auctionHouse).auctionFor(item);
				will(returnValue(auction));

				oneOf(auction).addAuctionEventListener(
						with(sniperForItem(item)));
				when(auctionStates.is("not joined"));
				oneOf(collector).addSniper(with(sniperForItem(item)));
				when(auctionStates.is("not joined"));

				one(auction).join();
				then(auctionStates.is("joined"));
			}
		});
		launcher.joinAuction(item);
	}

	protected Matcher<AuctionSniper> sniperForItem(final Item item) {
		return new FeatureMatcher<AuctionSniper, Item>(equalTo(item),
				"sniper for", "was") {

			@Override
			protected Item featureValueOf(AuctionSniper actual) {
				return actual.getItem();
			}
		};
	}
}
