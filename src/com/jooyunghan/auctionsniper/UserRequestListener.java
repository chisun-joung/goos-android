package com.jooyunghan.auctionsniper;

import java.util.EventListener;

public interface UserRequestListener extends EventListener {
	void joinAuction(String itemId);
}
