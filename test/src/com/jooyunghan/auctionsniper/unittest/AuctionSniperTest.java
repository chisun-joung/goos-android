package com.jooyunghan.auctionsniper.unittest;

import static org.hamcrest.Matchers.equalTo;
import junit.framework.TestCase;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;

import com.jooyunghan.auctionsniper.Auction;
import com.jooyunghan.auctionsniper.AuctionEventListener.PriceSource;
import com.jooyunghan.auctionsniper.AuctionSniper;
import com.jooyunghan.auctionsniper.SniperListener;
import com.jooyunghan.auctionsniper.SniperSnapshot;
import com.jooyunghan.auctionsniper.SniperState;

public class AuctionSniperTest extends TestCase {
	private static final String ITEM_ID = "item-id";
	private final Mockery context = new Mockery();
	private final States sniperState = context.states("sniper");
	private final Auction auction = context.mock(Auction.class);
	private final SniperListener sniperListener = context.mock(SniperListener.class);
	private final AuctionSniper sniper = new AuctionSniper(ITEM_ID, auction,
			sniperListener);

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testReportsLostIfAuctionClosesImmediately() throws Exception {
		context.checking(new Expectations() {{
			atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, 0, 0, SniperState.LOST));
		}});
		sniper.auctionClosed();
		context.assertIsSatisfied();
	}

	// public void testReportsLostIfAuctionClosesWhenBidding() throws Exception
	// {
	// sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
	// sniper.auctionClosed();
	// InOrder inOrder = inOrder(sniperListener);
	// inOrder.verify(sniperListener, atLeastOnce()).sniperStateChanged(
	// any(SniperSnapshot.class));
	// inOrder.verify(sniperListener, atLeastOnce()).sniperStateChanged(new
	// SniperSnapshot(ITEM_ID, 123, 123, SniperState.LOST));
	// }
	//
	// public void testReportsWonIfAuctionClosesWhenWinning() throws Exception {
	// sniper.currentPrice(123, 45, PriceSource.FromSniper);
	// sniper.auctionClosed();
	// InOrder inOrder = inOrder(sniperListener);
	// inOrder.verify(sniperListener, atLeastOnce()).sniperWinning();
	// inOrder.verify(sniperListener, atLeastOnce()).sniperWon();
	// }

	public void testReportsIsWinningWhenCurrentPriceComesFromSniper()
			throws Exception {
		context.checking(new Expectations() {
			{
				ignoring(auction);
				allowing(sniperListener).sniperStateChanged(
						with(aSniperThatIs(SniperState.BIDDING)));
				then(sniperState.is("bidding"));
				atLeast(1).of(sniperListener).sniperStateChanged(
						new SniperSnapshot(ITEM_ID, 135, 135,
								SniperState.WINNING));
				when(sniperState.is("bidding"));
			}

		});
		sniper.currentPrice(123, 12, PriceSource.FromOtherBidder);
		sniper.currentPrice(135, 45, PriceSource.FromSniper);
		context.assertIsSatisfied();
	}

	private Matcher<SniperSnapshot> aSniperThatIs(final SniperState state) {
		return new FeatureMatcher<SniperSnapshot, SniperState>(equalTo(state),
				"sniper that is ", "was") {
			@Override
			protected SniperState featureValueOf(SniperSnapshot actual) {
				return actual.state;
			}
		};
	}

	public void testBidsHigherAndReportsBiddingWhenNewPriceArrives()
			throws Exception {
		final int price = 1001;
		final int increment = 25;
		final int bid = price + increment;
		
		context.checking(new Expectations() {
			{
				one(auction).bid(bid);
				atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, price, bid, SniperState.BIDDING));
			}
		});

		sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);
		context.assertIsSatisfied();
	}
}
