package com.jooyunghan.auctionsniper.test;

import android.app.Activity;
import android.app.Instrumentation;

import com.jooyunghan.auctionsniper.Status;

public class ApplicationRunner {

	private AuctionSniperDriver driver;
	
	public ApplicationRunner(Instrumentation inst, Activity activity) {
		driver = new AuctionSniperDriver(inst, activity, 1000);
	}

	public void startBiddingIn(FakeAuctionServer auction) {
		driver.showsSniperStatus(Status.STATUS_JOINING);
	}

	public void showsSniperHasLostAuction() {
		
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
