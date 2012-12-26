package com.jooyunghan.auctionsniper.unittest;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import junit.framework.TestCase;

import com.jooyunghan.auctionsniper.Auction;
import com.jooyunghan.auctionsniper.AuctionSniper;
import com.jooyunghan.auctionsniper.SniperListener;

public class AuctionSniperTest extends TestCase {
	private final Auction auction = mock(Auction.class);
	private final SniperListener sniperListener = mock(SniperListener.class);
	private final AuctionSniper sniper = new AuctionSniper(sniperListener);

	public void testReportsLostWhenAuctionCloses() throws Exception {
		sniper.auctionClosed();
		verify(sniperListener).sniperLost();
	}

	public void testBidsHigherAndReportsBiddingWhenNewPriceArrives()
			throws Exception {
		final int price = 1001;
		final int increment = 25;

		sniper.currentPrice(price, increment);

		verify(sniperListener, atLeastOnce()).sniperBidding();
		verify(auction).bid(price + increment);
	}
}
