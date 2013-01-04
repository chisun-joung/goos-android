package com.jooyunghan.auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import android.util.Log;

public class XMPPAuction implements Auction {
	private final Chat chat;
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";

	public XMPPAuction(Chat chat) {
		this.chat = chat;
	}

	@Override
	public void bid(int price) {
		sendMessage(String.format(BID_COMMAND_FORMAT, price));
	}

	@Override
	public void join() {
		sendMessage(JOIN_COMMAND_FORMAT);
	}

	private void sendMessage(String message) {
		try {
			Log.d("han", "send message: " + message);
			chat.sendMessage(message);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
}