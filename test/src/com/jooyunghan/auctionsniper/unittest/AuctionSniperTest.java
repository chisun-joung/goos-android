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
import com.jooyunghan.auctionsniper.Item;
import com.jooyunghan.auctionsniper.SniperListener;
import com.jooyunghan.auctionsniper.SniperSnapshot;
import com.jooyunghan.auctionsniper.SniperState;

public class AuctionSniperTest extends TestCase {
	private static final Item ITEM = new Item("item-id", 1234);
	private final Mockery context = new Mockery();
	private final States sniperState = context.states("sniper");
	private final Auction auction = context.mock(Auction.class);
	private final SniperListener sniperListener = context
			.mock(SniperListener.class);
	private final AuctionSniper sniper = new AuctionSniper(ITEM, auction);

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		sniper.addSniperListener(sniperListener);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testReportsLostIfAuctionClosesImmediately() throws Exception {
		context.checking(new Expectations() {
			{
				atLeast(1).of(sniperListener).sniperStateChanged(
						new SniperSnapshot(ITEM.identifier, 0, 0,
								SniperState.LOST));
			}
		});
		sniper.auctionClosed();
		context.assertIsSatisfied();
	}

	public void testReportsLostIfAuctionClosesWhenBidding() throws Exception {
		allowingSniperBidding();
		context.checking(new Expectations() {
			{
				ignoring(auction);
				atLeast(1).of(sniperListener).sniperStateChanged(
						new SniperSnapshot(ITEM.identifier, 123, 123 + 45,
								SniperState.LOST));
				when(sniperState.is("bidding"));
			}
		});
		sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
		sniper.auctionClosed();
	}

	public void testReportsWonIfAuctionClosesWhenWinning() throws Exception {
		context.checking(new Expectations() {
			{
				ignoring(auction);
				allowing(sniperListener).sniperStateChanged(
						with(aSniperThatIs(SniperState.WINNING)));
				then(sniperState.is("winning"));
				atLeast(1).of(sniperListener).sniperStateChanged(
						new SniperSnapshot(ITEM.identifier, 123, 123,
								SniperState.WON));
				when(sniperState.is("winning"));
			}
		});
		sniper.currentPrice(123, 45, PriceSource.FromSniper);
		sniper.auctionClosed();
	}

	public void testReportsIsWinningWhenCurrentPriceComesFromSniper()
			throws Exception {
		allowingSniperBidding();
		context.checking(new Expectations() {
			{
				ignoring(auction);
				atLeast(1).of(sniperListener).sniperStateChanged(
						new SniperSnapshot(ITEM.identifier, 135, 135,
								SniperState.WINNING));
				when(sniperState.is("bidding"));
			}

		});
		sniper.currentPrice(123, 12, PriceSource.FromOtherBidder);
		sniper.currentPrice(135, 45, PriceSource.FromSniper);
		context.assertIsSatisfied();
	}

	public void testBidsHigherAndReportsBiddingWhenNewPriceArrives()
			throws Exception {
		final int price = 1001;
		final int increment = 25;
		final int bid = price + increment;

		context.checking(new Expectations() {
			{
				allowing(auction).bid(bid);
				atLeast(1).of(sniperListener).sniperStateChanged(
						new SniperSnapshot(ITEM.identifier, price, bid,
								SniperState.BIDDING));
			}
		});

		sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);
		context.assertIsSatisfied();
	}

	public void testDoesNotBidAndReportsLosingIfSubsequentPriceIsAboveStopPrice()
			throws Exception {
		allowingSniperBidding();
		context.checking(new Expectations() {
			{
				int bid = 123 + 45;
				allowing(auction).bid(bid);
				atLeast(1).of(sniperListener).sniperStateChanged(
						new SniperSnapshot(ITEM.identifier, 2345, bid,
								SniperState.LOSING));
				when(sniperState.is("bidding"));
			}
		});
		sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
		sniper.currentPrice(2345, 67, PriceSource.FromOtherBidder);
	}

	public void testDoesNotBidAndReportsLosingIfFirstPriceIsAboveStopPrice()
			throws Exception {
		context.checking(new Expectations() {
			{
				atLeast(1).of(sniperListener).sniperStateChanged(
						new SniperSnapshot(ITEM.identifier, 2345, 0,
								SniperState.LOSING));
			}
		});
		sniper.currentPrice(2345, 10, PriceSource.FromOtherBidder);
	}

	public void testReportsLostIfAuctionClosesWhenLosing() throws Exception {
		context.checking(new Expectations() {
			{
				allowing(sniperListener).sniperStateChanged(
						with(aSniperThatIs(SniperState.LOSING)));
				then(sniperState.is("losing"));
				atLeast(1).of(sniperListener).sniperStateChanged(
						with(aSniperThatIs(SniperState.LOST)));
				when(sniperState.is("losing"));
			}
		});
		sniper.currentPrice(2345, 10, PriceSource.FromOtherBidder);
		sniper.auctionClosed();
	}

	private void allowingSniperBidding() {
		context.checking(new Expectations() {
			{
				allowing(sniperListener).sniperStateChanged(
						with(aSniperThatIs(SniperState.BIDDING)));
				then(sniperState.is("bidding"));
			}
		});
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
}
