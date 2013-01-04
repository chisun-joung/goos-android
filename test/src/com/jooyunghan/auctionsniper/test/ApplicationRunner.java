package com.jooyunghan.auctionsniper.test;

import com.jayway.android.robotium.solo.Solo;

public class ApplicationRunner {
	public static final String SNIPER_XMPP_ID = "sniper";
	private AuctionSniperDriver driver;
	private String itemId;

	public ApplicationRunner() {
	}

	public void startBiddingIn(FakeAuctionServer auction, Solo solo) {
		itemId = auction.getItemId();
		solo.clickOnMenuItem("Join");
		driver = new AuctionSniperDriver(solo, 1000);
		driver.showsSniperState("Joining");
	}

	public void showsSniperHasLostAuction() {
		driver.showsSniperState("Lost");
	}

	public void showsSniperHasWonAuction(int lastPrice) {
		driver.showsSniperState(itemId, lastPrice, lastPrice, "Won");
	}

	public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
		driver.showsSniperState(itemId, lastPrice, lastBid, "Bidding");
	}

	public void hasShownSniperIsWinning(int lastPrice) {
		driver.showsSniperState(itemId, lastPrice, lastPrice, "Winning");
	}

	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}
}
