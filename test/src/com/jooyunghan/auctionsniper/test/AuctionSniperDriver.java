package com.jooyunghan.auctionsniper.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.Matcher;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.SystemClock;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.jooyunghan.auctionsniper.MainActivity;
import com.jooyunghan.auctionsniper.R;

public class AuctionSniperDriver extends Solo {
	private int timeout;

	public AuctionSniperDriver(Instrumentation inst, Activity activity,
			int timeout) {
		super(inst, activity);
		this.timeout = timeout;
		assertCurrentActivity("activity not launched", MainActivity.class);
	}

	public void showsSniperStatus(String statusText) {
		new TextViewDriver(this, R.id.status).hasText(equalTo(statusText), timeout);
	}

	static class TextViewDriver {
		private Solo solo;
		private int resId;

		TextViewDriver(Solo solo, int resId) {
			this.solo = solo;
			this.resId = resId;
		}

		public void hasText(Matcher<? super String> matcher, int timeout) {
			final long endTime = SystemClock.uptimeMillis() + timeout;

			TextView view = (TextView) solo.getView(resId);
			String lastText = view.getText().toString();

			while (SystemClock.uptimeMillis() < endTime) {
				lastText = view.getText().toString();
				if (matcher.matches(lastText)) {
					return;
				}
				sleep();
			}

			assertThat(lastText, matcher);
		}

		static private void sleep() {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ignored) {
			}
		}
	}
}
