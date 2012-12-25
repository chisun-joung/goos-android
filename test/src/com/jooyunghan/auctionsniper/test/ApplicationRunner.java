package com.jooyunghan.auctionsniper.test;

import android.app.Activity;
import android.app.Instrumentation;

import com.jooyunghan.auctionsniper.SniperStatus;

public class ApplicationRunner {

	private AuctionSniperDriver driver;
	
	public ApplicationRunner(Instrumentation inst, Activity activity) {
		driver = new AuctionSniperDriver(inst, activity, 1000);
	}

	public void startBiddingIn(FakeAuctionServer auction) {
		driver.showsSniperStatus(SniperStatus.STATUS_JOINING);
	}

	public void showsSniperHasLostAuction() {
		driver.showsSniperStatus(SniperStatus.STATUS_LOST);
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
