package com.jooyunghan.auctionsniper.test;

import com.jooyunghan.auctionsniper.MainActivity;

import android.test.ActivityInstrumentationTestCase2;

public class AuctionSniperEndToEndTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private FakeAuctionServer auction = new FakeAuctionServer("item-54321");
	private ApplicationRunner application = new ApplicationRunner();

	public AuctionSniperEndToEndTest() {
		super(MainActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
	}

	public void testSniperJoinsAuctionUntilAuctionCloses() throws Exception {
		auction.startSellingItem();
		application
				.startBiddingIn(auction, getInstrumentation(), getActivity());
		auction.hasReceivedJoinRequestFrom(auction.sniperId());
		auction.announceClosed();
		application.showsSniperHasLostAuction();
	}

	public void testSniperMakesAHigherBidButLoses() throws Exception {
		auction.startSellingItem();

		application
				.startBiddingIn(auction, getInstrumentation(), getActivity());
		auction.hasReceivedJoinRequestFrom(auction.sniperId());

		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding();

		auction.hasReceivedBid(1098, auction.sniperId());

		auction.announceClosed();
		application.showsSniperHasLostAuction();
	}

	public void testSniperWinsAnAuctionByBiddingHigher() throws Exception {
		auction.startSellingItem();
		application
				.startBiddingIn(auction, getInstrumentation(), getActivity());
		auction.hasReceivedJoinRequestFrom(auction.sniperId());

		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding();

		auction.hasReceivedBid(1098, auction.sniperId());

		auction.reportPrice(1098, 97, auction.sniperId());
		application.hasShownSniperIsWinning();

		auction.announceClosed();
		application.showsSniperHasWonAuction();
	}

	@Override
	public void tearDown() throws Exception {
		auction.stop();
		application.stop();
		super.tearDown();
	}
}
