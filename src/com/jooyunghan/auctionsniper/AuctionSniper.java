package com.jooyunghan.auctionsniper;

public class AuctionSniper implements AuctionEventListener {

	private SniperListener sniperListener;
	private Auction auction;
	private SniperSnapshot snapshot;

	public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener) {
		this.auction = auction;
		this.sniperListener = sniperListener;
		this.snapshot = SniperSnapshot.join(itemId);
		//sniperListener.sniperStateChanged(snapshot);
	}

	@Override
	public void auctionClosed() {
		snapshot = snapshot.closed();
		sniperListener.sniperStateChanged(snapshot);
	}

	@Override
	public void currentPrice(int price, int increment, PriceSource source) {
		boolean isWinning = source == PriceSource.FromSniper;
		if (isWinning) {
			snapshot = snapshot.winning(price);
		} else {
			final int bid = price + increment;
			auction.bid(bid);
			snapshot = snapshot.bidding(price, bid);
		}
		sniperListener.sniperStateChanged(snapshot);
	}
}
