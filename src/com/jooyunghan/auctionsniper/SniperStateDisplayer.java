package com.jooyunghan.auctionsniper;

public class SniperStateDisplayer implements SniperListener {
	private SnipersAdapter adapter;

	public SniperStateDisplayer(SnipersAdapter adapter) {
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
	public void sniperBidding() {
		showStatus(SniperStatus.STATUS_BIDDING);
	}

	@Override
	public void sniperWinning() {
		showStatus(SniperStatus.STATUS_WINNING);
	}

	private void showStatus(final String status) {
		adapter.showStatus(status);
	}
}
