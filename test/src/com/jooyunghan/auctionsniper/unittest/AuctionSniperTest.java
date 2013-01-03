package com.jooyunghan.auctionsniper.unittest;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import junit.framework.TestCase;

import org.mockito.InOrder;

import com.jooyunghan.auctionsniper.Auction;
import com.jooyunghan.auctionsniper.AuctionEventListener.PriceSource;
import com.jooyunghan.auctionsniper.AuctionSniper;
import com.jooyunghan.auctionsniper.SniperListener;
import com.jooyunghan.auctionsniper.SniperSnapshot;

public class AuctionSniperTest extends TestCase {
	private static final String ITEM_ID = "item-id";
	private final Auction auction = mock(Auction.class);
	private final SniperListener sniperListener = mock(SniperListener.class);
	private final AuctionSniper sniper = new AuctionSniper(ITEM_ID, auction, sniperListener);

	public void testReportsLostIfAuctionClosesImmediately() throws Exception {
		sniper.auctionClosed();
		verify(sniperListener, atLeastOnce()).sniperLost();
	}
	
	public void testReportsLostIfAuctionClosesWhenBidding() throws Exception {
		sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
		sniper.auctionClosed();
		InOrder inOrder = inOrder(sniperListener);
		inOrder.verify(sniperListener, atLeastOnce()).sniperBidding(any(SniperSnapshot.class));
		inOrder.verify(sniperListener, atLeastOnce()).sniperLost();
	}
	
	public void testReportsWonIfAuctionClosesWhenWinning() throws Exception {
		sniper.currentPrice(123, 45, PriceSource.FromSniper);
		sniper.auctionClosed();
		InOrder inOrder = inOrder(sniperListener);
		inOrder.verify(sniperListener, atLeastOnce()).sniperWinning();
		inOrder.verify(sniperListener, atLeastOnce()).sniperWon();
	}

	public void testReportsIsWinningWhenCurrentPriceComesFromSniper() throws Exception {
		sniper.currentPrice(123, 45, PriceSource.FromSniper);
		verify(sniperListener).sniperWinning();
	}

	public void testBidsHigherAndReportsBiddingWhenNewPriceArrives()
			throws Exception {
		final int price = 1001;
		final int increment = 25;
		final int bid = price + increment;

		sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);

		verify(sniperListener, atLeastOnce()).sniperBidding(new SniperSnapshot(ITEM_ID, price, bid));
		verify(auction).bid(bid);
	}
}
