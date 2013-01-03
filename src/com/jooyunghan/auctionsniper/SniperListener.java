package com.jooyunghan.auctionsniper;

public interface SniperListener {
	void sniperBidding(SniperState sniperState);
	void sniperWinning();
	void sniperLost();
	void sniperWon();
}
