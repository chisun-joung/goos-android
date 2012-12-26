package com.jooyunghan.auctionsniper.test;

import android.app.Activity;
import android.app.Instrumentation;

import com.jooyunghan.auctionsniper.SniperStatus;

public class ApplicationRunner {
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

	public void stop() {
		if (driver != null) {
			driver.getCurrentActivity().finish();
		}
	}

}
