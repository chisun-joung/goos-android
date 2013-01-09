package com.jooyunghan.auctionsniper.unittest;

import junit.framework.TestCase;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;

import com.jooyunghan.auctionsniper.Auction;
import com.jooyunghan.auctionsniper.AuctionHouse;
import com.jooyunghan.auctionsniper.AuctionSniper;
import com.jooyunghan.auctionsniper.ui.SniperCollector;
import com.jooyunghan.auctionsniper.ui.SniperLaucher;

public class SniperLaucherTest extends TestCase {
	private final Mockery context = new Mockery();
	private final Auction auction = context.mock(Auction.class);
	private final AuctionHouse auctionHouse = context.mock(AuctionHouse.class);
	private final SniperCollector collector = context
			.mock(SniperCollector.class);
	protected final States auctionStates = context.states("auction state")
			.startsAs("not joined");

	private SniperLaucher launcher = new SniperLaucher(auctionHouse, collector);

	public void testAddsNewSniperToCollectorAndThenJoinsAuction()
			throws Exception {
		final String itemId = "item 123";
		context.checking(new Expectations() {
			{
				allowing(auctionHouse).auctionFor(itemId);
				will(returnValue(auction));
				
				oneOf(auction).addAuctionEventListener(with(sniperForItem(itemId)));
				when(auctionStates.is("not joined"));
				oneOf(collector).addSniper(with(sniperForItem(itemId)));
				when(auctionStates.is("not joined"));
				
				one(auction).join();
				then(auctionStates.is("joined"));
			}
		});
		launcher.joinAuction(itemId);
	}

	protected Matcher<AuctionSniper> sniperForItem(final String itemId) {
		return new BaseMatcher<AuctionSniper>() {

			@Override
			public boolean matches(Object item) {
				AuctionSniper sniper = (AuctionSniper) item;
				return sniper.getItemId().equals(itemId);
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("sniper doesn't handle " + itemId);
			}
		};
	}
}
