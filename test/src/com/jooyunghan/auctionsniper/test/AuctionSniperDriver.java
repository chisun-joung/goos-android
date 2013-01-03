package com.jooyunghan.auctionsniper.test;

import static junit.framework.Assert.assertTrue;

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

	public void showsSniperState(String itemId, int lastPrice, int lastBid, String statusText) {
		assertTrue(solo.waitForText(itemId, 1, timeout));
		assertTrue(solo.waitForText(String.format("%d/%d", lastPrice, lastBid), 1, timeout));
		assertTrue(solo.waitForText(statusText, 1, timeout));
	}

	public void showsSniperState(String status) {
		assertTrue(solo.waitForText(status, 1, timeout));
	}

	public void dispose() {
		solo.finishOpenedActivities();
	}

}
