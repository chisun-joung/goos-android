package com.jooyunghan.auctionsniper.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import android.util.Log;

import com.jooyunghan.auctionsniper.Auction;
import com.jooyunghan.auctionsniper.AuctionEventListener;
import com.jooyunghan.util.Announcer;

public class XMPPAuction implements Auction {
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";

	private final Chat chat;
	private Announcer<AuctionEventListener> auctionEventListeners = Announcer
			.to(AuctionEventListener.class);

	public XMPPAuction(Chat chat, String connectionId) {
		this.chat = chat;
		chat.addMessageListener(new AuctionMessageTranslator(connectionId,
				auctionEventListeners.announce()));
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
			Log.d("han", "sending message: " + message);
			chat.sendMessage(message);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addAuctionEventListener(AuctionEventListener listener) {
		auctionEventListeners.addListener(listener);
	}
}