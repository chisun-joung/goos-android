package com.jooyunghan.auctionsniper.test;

import android.app.Activity;
import android.app.Instrumentation;

import com.jooyunghan.auctionsniper.SniperStatus;

public class ApplicationRunner {
	public static final String SNIPER_XMPP_ID = "sniper";
	private AuctionSniperDriver driver;

	public ApplicationRunner() {
	}

	public void startBiddingIn(FakeAuctionServer auction,
			Instrumentation instrumentation, Activity activity) {
		driver = new AuctionSniperDriver(instrumentation, activity, 1000);
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
			driver.getCurrentActivity().finish();
		}
	}



}
