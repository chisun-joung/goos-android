package com.jooyunghan.auctionsniper;

import java.util.EventListener;

public interface SniperListener extends EventListener {
	void sniperStateChanged(SniperSnapshot sniperState);
}
