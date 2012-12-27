package com.jooyunghan.auctionsniper.test;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matchers;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.util.Log;

import com.jooyunghan.auctionsniper.XMPPAuction;

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
		connection.login(String.format(ITEM_ID_AS_LOGIN, itemId),
				AUCTION_PASSWORD, AUCTION_RESOURCE);
		connection.getChatManager().addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean locallyCreated) {
				currentChat = chat;
				chat.addMessageListener(messageListener);
			}
		});
		Log.d("han", "login:" + String.format(ITEM_ID_AS_LOGIN, itemId));
	}

	public void hasReceivedJoinRequestFrom(String sniperId)
			throws InterruptedException {
		receivesAMessageMatching(sniperId, XMPPAuction.JOIN_COMMAND_FORMAT);
	}

	public void hasReceivedBid(int bid, String sniperId)
			throws InterruptedException {
		receivesAMessageMatching(sniperId,
				String.format(XMPPAuction.BID_COMMAND_FORMAT, bid));
	}

	private void receivesAMessageMatching(String sniperId, String format)
			throws InterruptedException {
		messageListener.receivesAMessage(Matchers.equalTo(format));
		assertThat(currentChat.getParticipant(), Matchers.equalTo(sniperId));
	}

	public void announceClosed() throws XMPPException {
		currentChat.sendMessage("SOLVersion: 1.1; Event: CLOSE;");
		Log.d("han", "message sent by fake");
	}

	public void reportPrice(int price, int increment, String bidder)
			throws XMPPException {
		currentChat.sendMessage(String.format("SOLVersion: 1.1; Event: PRICE; "
				+ "CurrentPrice: %d; Increment: %d; Bidder: %s;", price,
				increment, bidder));
	}

	public void stop() {
		// connection.disconnect();
	}

	public String sniperId() {
		return currentChat.getParticipant();
	}
}
