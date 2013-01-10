package com.jooyunghan.auctionsniper.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
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

	public XMPPAuction(XMPPConnection connection, String auctionJID) {
		AuctionMessageTranslator translator = translatorFor(connection);
		this.chat = connection.getChatManager().createChat(auctionJID,
				translator);
		addAuctionEventListener(chatDisconnectorFor(translator));
	}

	private AuctionEventListener chatDisconnectorFor(
			final AuctionMessageTranslator translator) {
		return new AuctionEventListener() {
			@Override
			public void currentPrice(int price, int increment,
					PriceSource source) { // empty
			}

			@Override
			public void auctionClosed() { // empty
			}

			@Override
			public void auctionFailed() {
				chat.removeMessageListener(translator);
			}
		};
	}

	private AuctionMessageTranslator translatorFor(XMPPConnection connection) {
		return new AuctionMessageTranslator(connection.getUser(),
				auctionEventListeners.announce());
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