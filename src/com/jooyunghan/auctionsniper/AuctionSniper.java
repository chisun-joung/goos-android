package com.jooyunghan.auctionsniper;

public class AuctionSniper implements AuctionEventListener {

	private SniperListener sniperListener;
	private Auction auction;
	private boolean isWinning = false;
	private String itemId;

	public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener) {
		this.itemId = itemId;
		this.auction = auction;
		this.sniperListener = sniperListener;
	}

	@Override
	public void auctionClosed() {
		if (isWinning ) {
			sniperListener.sniperWon();
		} else {
			sniperListener.sniperLost();
		}
	}

	@Override
	public void currentPrice(int price, int increment, PriceSource source) {
		isWinning = source == PriceSource.FromSniper;
		if (isWinning) {
			sniperListener.sniperWinning();
		} else {
			final int bid = price + increment;
			auction.bid(bid);
			sniperListener.sniperBidding(new SniperSnapshot(itemId, price, bid, SniperState.BIDDING));
		}
	}
}
