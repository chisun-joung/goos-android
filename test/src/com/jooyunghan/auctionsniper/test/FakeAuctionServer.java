package com.jooyunghan.auctionsniper.test;

import static java.lang.String.format;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.util.Log;

public class FakeAuctionServer {
	private static final String XMPP_HOSTNAME = "localhost";
	private static final String ITEM_ID_AS_LOGIN = "auction-%s";
	private static final String AUCTION_PASSWORD = "auction";
	private static final String AUCTION_RESOURCE = "Auction";

	private final String itemId;
	private final XMPPConnection connection;
	private Chat currentChat;
	protected final SingleMessageListener messageListener = new SingleMessageListener();

	public FakeAuctionServer(String itemId) {
		this.itemId = itemId;

		ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(
				XMPP_HOSTNAME, 5222);
		this.connection = new XMPPConnection(connectionConfiguration);
	}

	public void startSellingItem() throws XMPPException {
		connection.connect();
		connection.login(format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD,
				AUCTION_RESOURCE);
		connection.getChatManager().addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean locallyCreated) {
				Log.d("han", "chat created");
				currentChat = chat;
				chat.addMessageListener(messageListener);
			}
		});
		Log.d("han", "login:" + format(ITEM_ID_AS_LOGIN, itemId));
	}

	public void hasReceivedJoinRequestFromSniper() throws InterruptedException {
		messageListener.receivesAMessage();
	}

	public void announceClosed() throws XMPPException {
		currentChat.sendMessage(new Message());
		Log.d("han", "message sent by fake");
	}

	public void stop() {
		// connection.disconnect();
	}
}
