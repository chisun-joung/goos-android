package com.jooyunghan.auctionsniper.test;

import com.jayway.android.robotium.solo.Solo;
import com.jooyunghan.auctionsniper.MainActivity;

public class AuctionSniperDriver {
	private int timeout;
	private Solo solo;

	public AuctionSniperDriver(Solo solo, int timeout) {
		this.solo = solo;
		this.timeout = timeout;
		solo.assertCurrentActivity("activity not launched", MainActivity.class);
	}

	public void showsSniperStatus(String statusText) {
		solo.waitForText(statusText, 1, timeout);
	}

	public void dispose() {
		solo.finishOpenedActivities();
	}
}
