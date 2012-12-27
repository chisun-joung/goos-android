package com.jooyunghan.auctionsniper.unittest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import junit.framework.TestCase;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;

import com.jooyunghan.auctionsniper.AuctionEventListener;
import com.jooyunghan.auctionsniper.AuctionEventListener.PriceSource;
import com.jooyunghan.auctionsniper.AuctionMessageTranslator;

public class AuctionMessageTranslatorTest extends TestCase {
	public static final Chat UNUSED_CHAT = null;
	private static final String SNIPER_ID = "sniper-id";
	private final AuctionEventListener listener = mock(AuctionEventListener.class);
	private final AuctionMessageTranslator translator = new AuctionMessageTranslator(
			SNIPER_ID, listener);

	public void testNotifiesAuctionClosedWhenCloseMessageReceived()
			throws Exception {
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: CLOSE;");
		translator.processMessage(UNUSED_CHAT, message);
		verify(listener).auctionClosed();
	}

	public void testNotifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder() {
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;");
		translator.processMessage(UNUSED_CHAT, message);
		verify(listener).currentPrice(192, 7, PriceSource.FromOtherBidder);
	}

	public void testNotifiesBidDetailsWhenCurrentPriceMessageReceivedFromSniper() {
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: "
				+ SNIPER_ID + ";");
		translator.processMessage(UNUSED_CHAT, message);
		verify(listener).currentPrice(192, 7, PriceSource.FromSniper);
	}
}
