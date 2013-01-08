package com.jooyunghan.auctionsniper.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import com.jooyunghan.auctionsniper.Auction;
import com.jooyunghan.auctionsniper.AuctionEventListener;
import com.jooyunghan.util.Announcer;

import android.util.Log;

public class XMPPAuction implements Auction {
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
	private static final String ITEM_ID_AS_LOGIN = "auction-%s";
	private static final String AUCTION_RESOURCE = "Auction";
	private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/"
			+ AUCTION_RESOURCE;
	private final Chat chat;
	private Announcer<AuctionEventListener> auctionEventListeners = Announcer
			.to(AuctionEventListener.class);

	public XMPPAuction(XMPPConnection connection, String itemId) {
		this.chat = connection.getChatManager().createChat(
				auctionId(itemId, connection), null);
		chat.addMessageListener(new AuctionMessageTranslator(connection
				.getUser(), auctionEventListeners.announce()));
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

	private String auctionId(String itemId, XMPPConnection connection) {
		return String.format(AUCTION_ID_FORMAT, itemId,
				connection.getServiceName());
	}

	@Override
	public void addAuctionEventListener(AuctionEventListener listener) {
		auctionEventListeners.addListener(listener);
	}
}