package com.jooyunghan.auctionsniper.unittest;

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

public class AuctionSniperTest extends TestCase {
	private final Auction auction = mock(Auction.class);
	private final SniperListener sniperListener = mock(SniperListener.class);
	private final AuctionSniper sniper = new AuctionSniper(auction, sniperListener);

	public void testReportsLostIfAuctionClosesImmediately() throws Exception {
		sniper.auctionClosed();
		verify(sniperListener, atLeastOnce()).sniperLost();
	}
	
	public void testReportsLostIfAuctionClosesWhenBidding() throws Exception {
		sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
		sniper.auctionClosed();
		InOrder inOrder = inOrder(sniperListener);
		inOrder.verify(sniperListener, atLeastOnce()).sniperBidding();
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

		sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);

		verify(sniperListener, atLeastOnce()).sniperBidding();
		verify(auction).bid(price + increment);
	}
}
