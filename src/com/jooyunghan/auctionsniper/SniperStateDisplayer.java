package com.jooyunghan.auctionsniper;

import android.app.Activity;

public class SniperStateDisplayer implements SniperListener {
	private SnipersAdapter adapter;
	private Activity activity;

	public SniperStateDisplayer(Activity activity, SnipersAdapter adapter) {
		this.activity = activity;
		this.adapter = adapter;
	}

	@Override
	public void sniperStateChanged(final SniperSnapshot state) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adapter.sniperStateChanged(state);
			}
		});
	}
}
