package com.jooyunghan.auctionsniper.testtest;

import android.test.AndroidTestCase;

import com.jooyunghan.auctionsniper.test.FakeAuctionServer;

public class FakeTest extends AndroidTestCase {
	public void testFakeCanConnectToServer() throws Exception {
		FakeAuctionServer server = new FakeAuctionServer("item-54321");
		server.startSellingItem();
		server.stop();
	}

//	public void testConnectToGtalk() throws Exception {
//		ConnectionConfiguration config = new ConnectionConfiguration(
//				"talk.google.com", 5222, "gtalk.com");
//		XMPPConnection connection = new XMPPConnection(config);
//		connection.connect();
//		connection.login("jooyung.han@gmail.com", "Rnfjrl00");
//		Chat chat = connection.getChatManager().createChat("hyejoung.seo@gmail.com", null);
//		chat.sendMessage("Hello there");
//		connection.disconnect();
//	}
}
