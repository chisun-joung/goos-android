package com.jooyunghan.auctionsniper.test;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.jooyunghan.auctionsniper.MainActivity;

public class AuctionSniperEndToEndTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private FakeAuctionServer auction = new FakeAuctionServer("item-54321");
	private ApplicationRunner application = new ApplicationRunner();
	private Solo solo;

	public AuctionSniperEndToEndTest() {
		super(MainActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.solo = new Solo(getInstrumentation(), getActivity());
	}

	public void testSniperJoinsAuctionUntilAuctionCloses() throws Exception {
		auction.startSellingItem();
		application.startBiddingIn(auction, solo);
		auction.hasReceivedJoinRequestFrom(auction.sniperId());
		auction.announceClosed();
		application.showsSniperHasLostAuction();
	}

	public void testSniperMakesAHigherBidButLoses() throws Exception {
		auction.startSellingItem();

		application.startBiddingIn(auction, solo);
		auction.hasReceivedJoinRequestFrom(auction.sniperId());

		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding(1000, 1098);

		auction.hasReceivedBid(1098, auction.sniperId());

		auction.announceClosed();
		application.showsSniperHasLostAuction();
	}

	public void testSniperWinsAnAuctionByBiddingHigher() throws Exception {
		auction.startSellingItem();

		application.startBiddingIn(auction, solo);
		auction.hasReceivedJoinRequestFrom(auction.sniperId());

		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding(1000, 1098);

		auction.hasReceivedBid(1098, auction.sniperId());

		auction.reportPrice(1098, 97, auction.sniperId());
		application.hasShownSniperIsWinning(1098);

		auction.announceClosed();
		application.showsSniperHasWonAuction(1098);
	}

	@Override
	public void tearDown() throws Exception {
		auction.stop();
		application.stop();
		super.tearDown();
	}
}
