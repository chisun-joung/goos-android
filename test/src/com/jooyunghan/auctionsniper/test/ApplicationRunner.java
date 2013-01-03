package com.jooyunghan.auctionsniper.test;

import com.jayway.android.robotium.solo.Solo;
import com.jooyunghan.auctionsniper.SniperStatus;

public class ApplicationRunner {
	private AuctionSniperDriver driver;
	private String itemId;

	public ApplicationRunner() {
	}

	public void startBiddingIn(FakeAuctionServer auction, Solo solo) {
		itemId = auction.getItemId();
		solo.clickOnMenuItem("Join");
		driver = new AuctionSniperDriver(solo, 1000);
		driver.showsSniperStatus(SniperStatus.STATUS_JOINING);
	}

	public void showsSniperHasLostAuction() {
		driver.showsSniperStatus(SniperStatus.STATUS_LOST);
	}

	public void showsSniperHasWonAuction(int lastPrice) {
		driver.showsSniperStatus(SniperStatus.STATUS_WON);
	}

	public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
		driver.showsSniperStatus(itemId, lastPrice, lastBid,
				SniperStatus.STATUS_BIDDING);
	}

	public void hasShownSniperIsWinning(int lastPrice) {
		driver.showsSniperStatus(SniperStatus.STATUS_WINNING);
	}

	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}
}
