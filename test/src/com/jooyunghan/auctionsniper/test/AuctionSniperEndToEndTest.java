package com.jooyunghan.auctionsniper.test;

import android.test.InstrumentationTestCase;

public class AuctionSniperEndToEndTest extends InstrumentationTestCase {
	private FakeAuctionServer auction;
	private ApplicationRunner application;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		auction = new FakeAuctionServer("item-54321");
		application = new ApplicationRunner(getInstrumentation());
	}

	public void testSniperJoinsAuctionUntilAuctionCloses() throws Exception {
		auction.startSellingItem();
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFromSniper();
		auction.announceClosed();
		application.showsSniperHasLostAuction();
	}

	@Override
	public void tearDown() throws Exception {
		auction.stop();
		application.stop();
		super.tearDown();
	}
}
