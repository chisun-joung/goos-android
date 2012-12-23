package com.jooyunghan.auctionsniper.test;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import static java.lang.String.format;

public class FakeAuctionServer {
	private static final String XMPP_HOSTNAME = "192.168.1.3";
	private static final String ITEM_ID_AS_LOGIN = "auction-%s";
	private static final String AUCTION_PASSWORD = "auction";
	private static final String AUCTION_RESOURCE = "Auction";

	private final String itemId;
	private final XMPPConnection connection;
	private Chat currentChat;

	public FakeAuctionServer(String itemId) {
		this.itemId = itemId;
		this.connection = new XMPPConnection(XMPP_HOSTNAME);
	}

	public void startSellingItem() throws XMPPException {
		connection.connect();
		connection.login(format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD,
				AUCTION_RESOURCE);
		connection.getChatManager().addChatListener(new ChatManagerListener() {
			
			@Override
			public void chatCreated(Chat chat, boolean createdLocallay) {
				currentChat = chat;
			}
		});
	}

	public void hasReceivedJoinRequestFromSniper() {
	}

	public void announceClosed() {
	}

	public void stop() {
		// TODO Auto-generated method stub

	}

}
