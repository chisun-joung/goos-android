package com.jooyunghan.auctionsniper;

import com.jooyunghan.util.Announcer;

public class AuctionSniper implements AuctionEventListener {

	private Announcer<SniperListener> sniperListeners = Announcer
			.to(SniperListener.class);
	private final Auction auction;
	private final Item item;
	private SniperSnapshot snapshot;

	public AuctionSniper(Item item, Auction auction) {
		this.item = item;
		this.auction = auction;
		this.snapshot = SniperSnapshot.joining(item.identifier);
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
			if (item.allowsBid(bid)) {
				auction.bid(bid);
				snapshot = snapshot.bidding(price, bid);
			} else {
				snapshot = snapshot.losing(price);
			}
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

	public Item getItem() {
		return item;
	}
}
