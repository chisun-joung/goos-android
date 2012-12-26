package com.jooyunghan.auctionsniper.test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import android.app.Activity;
import android.app.Instrumentation;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.jooyunghan.auctionsniper.MainActivity;
import com.jooyunghan.auctionsniper.R;

public class AuctionSniperDriver extends Solo {
	private int timeout;

	public AuctionSniperDriver(Instrumentation inst, Activity activity, int timeout) {
		super(inst, activity);
		this.timeout = timeout;
		assertCurrentActivity("activity not launched", MainActivity.class);
	}

	public void showsSniperStatus(String statusText) {
		waitForText(R.id.status, statusText);
		Log.d("han", "showsSniperStatus:" + statusText);
	}

	private void waitForText(int resId, String statusText) {
		final long endTime = SystemClock.uptimeMillis() + timeout;

		TextView view = (TextView) getView(resId);
		String lastText = view.getText().toString();

		while (SystemClock.uptimeMillis() < endTime) {
			lastText = view.getText().toString();
			if (lastText.equals(statusText)) {
				return;
			}
			sleep();
		}
		
		assertThat(lastText, equalTo(statusText));
	}

	public void sleep() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException ignored) {}
	}
}
