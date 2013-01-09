package com.jooyunghan.auctionsniper.test;

import static junit.framework.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.StringDescription;

import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.jooyunghan.auctionsniper.ApplicationMain;
import com.jooyunghan.auctionsniper.R;
import com.jooyunghan.auctionsniper.ui.MainActivity;
import com.objogate.wl.Probe;
import com.objogate.wl.internal.Timeout;

public class AuctionSniperDriver {
	private int timeoutMillis;
	private int pollDelayMillis;
	private Solo solo;

	public AuctionSniperDriver(Solo solo, int timeout) {
		this.solo = solo;
		this.timeoutMillis = timeout;
		this.pollDelayMillis = 100;
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
		return (EditText) solo.getView(R.id.stop_price_text);
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

	public void showsSniperState(String itemId, String statusText)
			throws InterruptedException {
		hasItemWithText(R.id.list, itemId, statusText);
	}

	public void showsSniperState(String itemId, int lastPrice, int lastBid,
			String statusText) throws InterruptedException {
		hasItemWithText(R.id.list, itemId,
				String.format("%d/%d", lastPrice, lastBid), statusText);
	}

	private void hasItemWithText(int resId, String... matchStrings)
			throws InterruptedException {
		final long endTime = SystemClock.uptimeMillis() + timeoutMillis;
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
			waitFor(pollDelayMillis);
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

	private List<String> getAllItemText(final ListView list, final int pos)
			throws InterruptedException {
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

	static private void waitFor(int delayInMillis) {
		try {
			Thread.sleep(delayInMillis);
		} catch (InterruptedException ignored) {
		}
	}

	public void dispose() {
		Log.d("han", "dispose");
		Activity activity = solo.getCurrentActivity();
		ApplicationMain main = (ApplicationMain) activity.getApplication();
		main.dispose();
	}

	public void check(Probe probe) {
		if (!poll(probe)) {
			throw new AssertionError(describeFailureOf(probe));
		}
	}

	protected String describeFailureOf(Probe probe) {
		StringDescription description = new StringDescription();

		description.appendText("\nTried to find:\n    ");
		probe.describeTo(description);
		description.appendText("\nbut:\n    ");
		probe.describeFailureTo(description);

		return description.toString();
	}

	private boolean poll(Probe probe) {
		Timeout timeout = new Timeout(this.timeoutMillis);

		for (;;) {
			runProbe(probe);

			if (probe.isSatisfied()) {
				return true;
			} else if (timeout.hasTimedOut()) {
				return false;
			} else {
				waitFor(pollDelayMillis);
			}
		}
	}

	protected void runProbe(final Probe probe) {
		try {
			invokeAndWait(new Runnable() {
				@Override
				public void run() {
					probe.probe();
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void invokeAndWait(final Runnable runnable)
			throws InterruptedException {
		final SynchronousQueue<Boolean> queue = new SynchronousQueue<Boolean>();

		Activity activity = solo.getCurrentActivity();
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				runnable.run();
				try {
					queue.put(true);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		queue.take();
	}


}
