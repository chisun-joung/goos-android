package com.jooyunghan.auctionsniper;

public interface SniperListener {
	void sniperBidding(SniperSnapshot sniperState);
	void sniperWinning();
	void sniperLost();
	void sniperWon();
}
