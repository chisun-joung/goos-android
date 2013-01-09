package com.jooyunghan.auctionsniper.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import com.jooyunghan.auctionsniper.Auction;
import com.jooyunghan.auctionsniper.AuctionEventListener;
import com.jooyunghan.auctionsniper.Item;
import com.jooyunghan.auctionsniper.xmpp.XMPPAuctionHouse;

public class XMPPAuctionHouseTest extends TestCase {
	private XMPPAuctionHouse auctionHouse;
	private final FakeAuctionServer server = new FakeAuctionServer("item-54321");

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		auctionHouse = new XMPPAuctionHouse(ApplicationRunner.XMPP_HOSTNAME,
				ApplicationRunner.SNIPER_XMPP_ID,
				ApplicationRunner.SNIPER_XMPP_PASSWORD);
	}

	public void testReceivesEventsFromAuctionServerAfterJoining()
			throws Exception {
		server.startSellingItem();
		Auction auction = auctionHouse.auctionFor(new Item(server.getItemId()));

		CountDownLatch auctionWasClosed = new CountDownLatch(1);
		auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));

		auction.join();
		server.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		server.announceClosed();

		assertTrue("should have been closed.",
				auctionWasClosed.await(3, TimeUnit.SECONDS));
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
