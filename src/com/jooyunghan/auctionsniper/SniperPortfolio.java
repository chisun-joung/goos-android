package com.jooyunghan.auctionsniper;

import java.util.ArrayList;
import java.util.List;

import com.jooyunghan.auctionsniper.ui.SniperCollector;
import com.jooyunghan.util.Announcer;

public class SniperPortfolio implements SniperCollector {
	private final List<AuctionSniper> snipers = new ArrayList<AuctionSniper>();
	private final Announcer<PortfolioListener> listeners = Announcer
			.to(PortfolioListener.class);

	public void addPortfolioListener(PortfolioListener listener) {
		for (AuctionSniper sniper: snipers) {
			listener.sniperAdded(sniper);
		}
		listeners.addListener(listener);
	}

	public void removePortfolioListener(PortfolioListener listener) {
		listeners.removeListener(listener);
	}

	@Override
	public void addSniper(AuctionSniper sniper) {
		snipers.add(sniper);
		listeners.announce().sniperAdded(sniper);
	}

	public void removeAllSnipers() {
		snipers.clear();
	}


}
