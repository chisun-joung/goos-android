package com.jooyunghan.auctionsniper.test;

import static junit.framework.Assert.assertTrue;
import android.app.Activity;
import android.app.Instrumentation;
import android.util.Log;

import com.jayway.android.robotium.solo.Solo;
import com.jooyunghan.auctionsniper.MainActivity;

public class AuctionSniperDriver extends Solo {

	private int timeOut;

	public AuctionSniperDriver(Instrumentation inst,
			Activity startActivitySync, int timeout) {
		super(inst, startActivitySync);
		this.timeOut = timeout;
		assertCurrentActivity("activity not launched", MainActivity.class);
	}

	public void showsSniperStatus(String statusText) {
		assertTrue(waitForText(statusText, 1, timeOut));
		Log.d("han", "showsSniperStatus:" + statusText);
	}

}
