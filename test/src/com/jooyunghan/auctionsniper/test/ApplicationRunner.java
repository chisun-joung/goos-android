package com.jooyunghan.auctionsniper.test;

import com.jayway.android.robotium.solo.Solo;
import com.jooyunghan.auctionsniper.SniperStatus;

public class ApplicationRunner {
	private AuctionSniperDriver driver;

	public ApplicationRunner() {
	}

	public void startBiddingIn(FakeAuctionServer auction, Solo solo) {
		solo.clickOnMenuItem("Join");
		driver = new AuctionSniperDriver(solo, 1000);
		driver.showsSniperStatus(SniperStatus.STATUS_JOINING);
	}

	public void showsSniperHasLostAuction() {
		driver.showsSniperStatus(SniperStatus.STATUS_LOST);
	}

	public void showsSniperHasWonAuction() {
		driver.showsSniperStatus(SniperStatus.STATUS_WON);
	}

	public void hasShownSniperIsBidding() {
		driver.showsSniperStatus(SniperStatus.STATUS_BIDDING);
	}

	public void hasShownSniperIsWinning() {
		driver.showsSniperStatus(SniperStatus.STATUS_WINNING);
	}

	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}
}
