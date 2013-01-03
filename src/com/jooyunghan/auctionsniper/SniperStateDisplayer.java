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
	public void sniperLost() {
		showStatus(SniperStatus.STATUS_LOST);
	}

	@Override
	public void sniperWon() {
		showStatus(SniperStatus.STATUS_WON);
	}

	@Override
	public void sniperBidding(final SniperState state) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adapter.sniperStatusChanged(state, SniperStatus.STATUS_BIDDING);
			}
		});
	}

	@Override
	public void sniperWinning() {
		showStatus(SniperStatus.STATUS_WINNING);
	}

	private void showStatus(final String status) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adapter.showStatus(status);
			}
		});
	}
}
