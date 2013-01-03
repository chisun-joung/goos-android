package com.jooyunghan.auctionsniper;

import android.app.Activity;

public class UIThreadSniperListener implements SniperListener {
	private SniperListener listener;
	private Activity activity;

	public UIThreadSniperListener(Activity activity, SniperListener listener) {
		this.activity = activity;
		this.listener = listener;
	}

	@Override
	public void sniperStateChanged(final SniperSnapshot state) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				listener.sniperStateChanged(state);
			}
		});
	}
}
