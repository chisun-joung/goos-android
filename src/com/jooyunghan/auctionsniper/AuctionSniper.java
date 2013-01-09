package com.jooyunghan.auctionsniper;

import com.jooyunghan.util.Announcer;

public class AuctionSniper implements AuctionEventListener {

	private Announcer<SniperListener> sniperListeners = Announcer
			.to(SniperListener.class);
	private Auction auction;
	private SniperSnapshot snapshot;

	public AuctionSniper(String itemId, Auction auction) {
		this.auction = auction;
		this.snapshot = SniperSnapshot.joining(itemId);
	}

	@Override
	public void auctionClosed() {
		snapshot = snapshot.closed();
		notifyChanges();
	}

	@Override
	public void currentPrice(int price, int increment, PriceSource source) {
		switch (source) {
		case FromSniper:
			snapshot = snapshot.winning(price);
			break;
		case FromOtherBidder:
			final int bid = price + increment;
			auction.bid(bid);
			snapshot = snapshot.bidding(price, bid);
			break;
		}
		notifyChanges();
	}

	public SniperSnapshot getSnapshot() {
		return snapshot;
	}

	public String getItemId() {
		return snapshot.itemId;
	}

	private void notifyChanges() {
		sniperListeners.announce().sniperStateChanged(snapshot);
	}

	public void addSniperListener(SniperListener listener) {
		sniperListeners.addListener(listener);
	}
}
