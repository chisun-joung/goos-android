package com.jooyunghan.auctionsniper.test;

import android.test.AndroidTestCase;

public class AuctionSniperEndToEndTest extends AndroidTestCase {
	private final FakeAuctionServer auction = new FakeAuctionServer(
			"item-54321");
	private final ApplicationRunner application = new ApplicationRunner();

	public void testSniperJoinsAuctionUntilAuctionCloses() throws Exception {
		auction.startSellingItem();
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFromSniper();
		auction.announceClosed();
		application.showsSniperHasLostAuction();
	}

	// Additional cleanup
	public void setup() {
		auction.stop();
		application.stop();
	}
}
