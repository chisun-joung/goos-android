package com.jooyunghan.auctionsniper.test;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;
import com.jooyunghan.auctionsniper.ApplicationMain;
import com.jooyunghan.auctionsniper.R;
import com.jooyunghan.auctionsniper.ui.MainActivity;
import com.objogate.wl.android.driver.AndroidDriver;
import com.objogate.wl.android.driver.ListViewDriver;

public class AuctionSniperDriver extends AndroidDriver<Activity> {
	public AuctionSniperDriver(Solo solo, int timeout) {
		super(solo, timeout);
		solo.assertCurrentActivity("activity not launched", MainActivity.class);
	}

	public void startBiddingFor(String itemId) {
		solo.clearEditText(itemIdField());
		solo.clearEditText(stopPriceField());
		solo.enterText(itemIdField(), itemId);
		solo.clickOnView(bidButton());
	}

	public void startBiddingFor(String itemId, int stopPrice) {
		solo.clearEditText(itemIdField());
		solo.clearEditText(stopPriceField());
		solo.enterText(itemIdField(), itemId);
		solo.enterText(stopPriceField(), String.valueOf(stopPrice));
		solo.clickOnView(bidButton());
	}

	private EditText stopPriceField() {
		return (EditText) getView(R.id.stop_price_text);
	}

	private View bidButton() {
		return getView(R.id.bid_button);
	}

	private EditText itemIdField() {
		return (EditText) getView(R.id.item_id_text);
	}

	public void showsSniperState(String itemId, String statusText)
			throws InterruptedException {
		new ListViewDriver(this, R.id.list).hasItem(containsAllStrings(itemId,
				statusText));
	}

	public void showsSniperState(String itemId, int lastPrice, int lastBid,
			String statusText) throws InterruptedException {
		new ListViewDriver(this, R.id.list).hasItem(containsAllStrings(itemId,
				String.format("%d/%d", lastPrice, lastBid), statusText));
	}

	public void dispose() {
		Log.d("han", "dispose");
		Activity activity = solo.getCurrentActivity();
		ApplicationMain main = (ApplicationMain) activity.getApplication();
		main.dispose();
	}

}
