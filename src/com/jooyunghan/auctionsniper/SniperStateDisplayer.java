package com.jooyunghan.auctionsniper;

import android.app.Activity;
import android.widget.TextView;

public class SniperStateDisplayer implements SniperListener {
	private TextView statusText;
	private Activity activity;

	public SniperStateDisplayer(Activity activity) {
		this.activity = activity;
		statusText = (TextView) activity.findViewById(R.id.status);
	}

	@Override
	public void sniperLost() {
		showStatus(SniperStatus.STATUS_LOST);
	}

	@Override
	public void sniperBidding() {
		showStatus(SniperStatus.STATUS_BIDDING);
	}

	private void showStatus(final String status) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				statusText.setText(status);
			}
		});
	}
}
