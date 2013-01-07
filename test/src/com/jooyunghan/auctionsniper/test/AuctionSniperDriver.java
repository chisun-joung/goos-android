package com.jooyunghan.auctionsniper.test;

import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.jooyunghan.auctionsniper.MainActivity;
import com.jooyunghan.auctionsniper.R;
import static junit.framework.Assert.fail;

interface Query<T, V> {
	V query(T t);
}

public class AuctionSniperDriver {
	private int timeout;
	private Solo solo;

	public AuctionSniperDriver(Solo solo, int timeout) {
		this.solo = solo;
		this.timeout = timeout;
		solo.assertCurrentActivity("activity not launched", MainActivity.class);
	}

	public void showsSniperState(String status) {
		hasItemWithText(R.id.list, status);
	}

	public void showsSniperState(String itemId, int lastPrice, int lastBid,
			String statusText) {
		hasItemWithText(R.id.list, itemId,
				String.format("%d/%d", lastPrice, lastBid), statusText);
	}

	private void hasItemWithText(int resId, String... matchStrings) {
		final long endTime = SystemClock.uptimeMillis() + timeout;
		List<String> matchStringList = Arrays.asList(matchStrings);

		while (SystemClock.uptimeMillis() < endTime) {
			ListView list = (ListView) solo.getView(resId);
			ListAdapter adapter = list.getAdapter();
			for (int i = 0; i < adapter.getCount(); i++) {
				View child = adapter.getView(i, null, list);
				List<String> childStrings = getAllText(child);
				if (childStrings.containsAll(matchStringList)) {
					return;
				}
			}
			sleep();
		}

		List<String> rows = new ArrayList<String>();
		ListView list = (ListView) solo.getView(resId);
		ListAdapter adapter = list.getAdapter();
		for (int i = 0; i < adapter.getCount(); i++) {
			View child = adapter.getView(i, null, list);
			rows.add("item " + i + ": "
					+ StringUtils.join(getAllText(child), ", "));
		}
		fail("can't find an item with:\n  " + StringUtils.join(matchStrings, ", ")
				+ "\nbecause\n  " + StringUtils.join(rows, "\n  "));
	}

	private List<String> getAllText(View child) {
		ArrayList<String> text = new ArrayList<String>();
		for (View subView : solo.getViews(child)) {
			if (subView instanceof TextView) {
				text.add(((TextView) subView).getText().toString());
			}
		}
		return text;
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
