package com.jooyunghan.auctionsniper.ui;

import com.jooyunghan.auctionsniper.Auction;
import com.jooyunghan.auctionsniper.AuctionHouse;
import com.jooyunghan.auctionsniper.AuctionSniper;
import com.jooyunghan.auctionsniper.Item;
import com.jooyunghan.auctionsniper.UserRequestListener;

public final class SniperLauncher implements UserRequestListener {
	private final AuctionHouse auctionHouse;
	private final SniperCollector collector;

	public SniperLauncher(AuctionHouse auctionHouse, SniperCollector collector) {
		this.auctionHouse = auctionHouse;
		this.collector = collector;
	}

	@Override
	public void joinAuction(Item item) {
		Auction auction = auctionHouse.auctionFor(item);
		AuctionSniper sniper = new AuctionSniper(item, auction);
		auction.addAuctionEventListener(sniper);
		collector.addSniper(sniper);
		auction.join();
	}
}