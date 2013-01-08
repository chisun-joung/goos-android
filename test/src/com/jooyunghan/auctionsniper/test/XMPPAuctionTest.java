package com.jooyunghan.auctionsniper.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;

import com.jooyunghan.auctionsniper.Auction;
import com.jooyunghan.auctionsniper.AuctionEventListener;
import com.jooyunghan.auctionsniper.ui.MainActivity;
import com.jooyunghan.auctionsniper.xmpp.XMPPAuction;

public class XMPPAuctionTest extends TestCase {
	private XMPPConnection connection;
	private final FakeAuctionServer server = new FakeAuctionServer("item-54321");

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		connection = MainActivity.connectTo(ApplicationRunner.XMPP_HOSTNAME,
				ApplicationRunner.SNIPER_XMPP_ID,
				ApplicationRunner.SNIPER_XMPP_PASSWORD);
	}

	public void testReceivesEventsFromAuctionServerAfterJoining()
			throws Exception {
		CountDownLatch auctionWasClosed = new CountDownLatch(1);
		server.startSellingItem();

		Auction auction = new XMPPAuction(connection, server.getItemId());
		auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));

		auction.join();
		server.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		server.announceClosed();

		assertTrue("should have been closed.",
				auctionWasClosed.await(2, TimeUnit.SECONDS));
	}

	private AuctionEventListener auctionClosedListener(
			final CountDownLatch auctionWasClosed) {
		return new AuctionEventListener() {
			@Override
			public void auctionClosed() {
				auctionWasClosed.countDown();
			}

			@Override
			public void currentPrice(int price, int increment,
					PriceSource source) {
				// not implemented
			}

		};
	}

}
