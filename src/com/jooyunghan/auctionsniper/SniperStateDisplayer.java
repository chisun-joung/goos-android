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
		showState(SniperStatus.STATUS_LOST);
	}

	@Override
	public void sniperWon() {
		showState(SniperStatus.STATUS_WON);
	}

	@Override
	public void sniperBidding(final SniperSnapshot state) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adapter.sniperStatesChanged(state, SniperStatus.STATUS_BIDDING);
			}
		});
	}

	@Override
	public void sniperWinning() {
		showState(SniperStatus.STATUS_WINNING);
	}

	private void showState(final String state) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adapter.showState(state);
			}
		});
	}
}
