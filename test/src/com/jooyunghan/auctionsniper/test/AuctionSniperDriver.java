package com.jooyunghan.auctionsniper.test;

import static junit.framework.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.jooyunghan.auctionsniper.MainActivity;
import com.jooyunghan.auctionsniper.R;

public class AuctionSniperDriver {
	private int timeout;
	private Solo solo;

	public AuctionSniperDriver(Solo solo, int timeout) {
		this.solo = solo;
		this.timeout = timeout;
		solo.assertCurrentActivity("activity not launched", MainActivity.class);
	}

	public void startBiddingFor(String itemId) {
		solo.enterText(itemIdField(), itemId);
		solo.clickOnView(bidButton());
	}

	private View bidButton() {
		return solo.getView(R.id.bid_button);
	}

	private EditText itemIdField() {
		return (EditText) solo.getView(R.id.item_id_text);
	}

	public void showsSniperState(String status) throws InterruptedException {
		hasItemWithText(R.id.list, status);
	}

	public void showsSniperState(String itemId, int lastPrice, int lastBid,
			String statusText) throws InterruptedException {
		hasItemWithText(R.id.list, itemId,
				String.format("%d/%d", lastPrice, lastBid), statusText);
	}

	private void hasItemWithText(int resId, String... matchStrings) throws InterruptedException {
		final long endTime = SystemClock.uptimeMillis() + timeout;
		List<String> matchStringList = Arrays.asList(matchStrings);
		ListView list = (ListView) solo.getView(resId);
		while (SystemClock.uptimeMillis() < endTime) {
			ListAdapter adapter = list.getAdapter();
			for (int i = 0; i < adapter.getCount(); i++) {
				List<String> childStrings = getAllItemText(list, i);
				if (childStrings.containsAll(matchStringList)) {
					return;
				}
			}
			
			sleep();
		}

		List<String> rows = new ArrayList<String>();
		ListAdapter adapter = list.getAdapter();
		for (int i = 0; i < adapter.getCount(); i++) {
			rows.add("item " + i + ": "
					+ StringUtils.join(getAllItemText(list, i), ", "));
		}
		fail("can't find an item with:\n  "
				+ StringUtils.join(matchStrings, ", ") + "\nbecause\n  "
				+ StringUtils.join(rows, "\n  "));
	}

	private List<String> getAllItemText(final ListView list, final int pos) throws InterruptedException {
		final SynchronousQueue<List<String>> queue = new SynchronousQueue<List<String>>();
		
		Activity activity = solo.getCurrentActivity();
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ArrayList<String> text = new ArrayList<String>();
				ListAdapter adapter = list.getAdapter();
				View child = adapter.getView(pos, null, list);
				for (View subView : solo.getViews(child)) {
					if (subView instanceof TextView) {
						text.add(((TextView) subView).getText().toString());
					}
				}
				try {
					queue.put(text);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		return queue.take();
	}

	static private void sleep() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException ignored) {
		}
	}

	public void dispose() {
		Log.d("han", "dispose");
	}
}
