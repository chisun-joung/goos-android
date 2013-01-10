package com.jooyunghan.auctionsniper;

import java.util.EventListener;

public interface AuctionEventListener extends EventListener {
	enum PriceSource {
		FromSniper, FromOtherBidder
	}
	void currentPrice(int price, int increment, PriceSource source);
	void auctionClosed();
	void auctionFailed();
}
