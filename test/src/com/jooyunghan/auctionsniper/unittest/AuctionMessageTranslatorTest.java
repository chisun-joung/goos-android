package com.jooyunghan.auctionsniper.unittest;

import junit.framework.TestCase;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jmock.Expectations;
import org.jmock.Mockery;

import com.jooyunghan.auctionsniper.AuctionEventListener;
import com.jooyunghan.auctionsniper.AuctionEventListener.PriceSource;
import com.jooyunghan.auctionsniper.xmpp.AuctionMessageTranslator;
import com.jooyunghan.auctionsniper.xmpp.XMPPFailureReporter;

public class AuctionMessageTranslatorTest extends TestCase {
	public static final Chat UNUSED_CHAT = null;
	private static final String SNIPER_ID = "sniper-id";
	private static final String STRUCTURED_ID_SUFFIX = "@localhost/xxx";
	private final Mockery context = new Mockery();
	private final AuctionEventListener listener = context
			.mock(AuctionEventListener.class);
	private final XMPPFailureReporter failureReporter = context
			.mock(XMPPFailureReporter.class);
	private final AuctionMessageTranslator translator = new AuctionMessageTranslator(
			SNIPER_ID, listener, failureReporter);
	private final AuctionMessageTranslator translator2 = new AuctionMessageTranslator(
			SNIPER_ID + STRUCTURED_ID_SUFFIX, listener,  failureReporter);

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		context.assertIsSatisfied();
	}

	public void testNotifiesAuctionClosedWhenCloseMessageReceived()
			throws Exception {
		context.checking(new Expectations() {
			{
				oneOf(listener).auctionClosed();
			}
		});

		translator.processMessage(UNUSED_CHAT,
				message("SOLVersion: 1.1; Event: CLOSE;"));
	}

	public void testNotifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder() {
		context.checking(new Expectations() {
			{
				oneOf(listener).currentPrice(192, 7,
						PriceSource.FromOtherBidder);
			}
		});

		String priceMessageFromOther = "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;";
		translator.processMessage(UNUSED_CHAT, message(priceMessageFromOther));
	}

	public void testNotifiesBidDetailsWhenCurrentPriceMessageReceivedFromSniper() {
		context.checking(new Expectations() {
			{
				oneOf(listener).currentPrice(192, 7, PriceSource.FromSniper);
			}
		});
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: "
				+ SNIPER_ID + ";");
		translator.processMessage(UNUSED_CHAT, message);
	}

	public void testHandlesStructuredIdFromAuction() {
		context.checking(new Expectations() {
			{
				oneOf(listener).currentPrice(192, 7, PriceSource.FromSniper);
			}
		});
		String messageWithStructuredID = "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192;"
				+ " Increment: 7; Bidder: "
				+ SNIPER_ID
				+ STRUCTURED_ID_SUFFIX
				+ ";";
		translator
				.processMessage(UNUSED_CHAT, message(messageWithStructuredID));
	}

	public void testHandlesStructuredIdFromSniper() {
		context.checking(new Expectations() {
			{
				oneOf(listener).currentPrice(192, 7, PriceSource.FromSniper);
			}
		});
		String messageWithID = "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192;"
				+ " Increment: 7; Bidder: " + SNIPER_ID + ";";
		translator2.processMessage(UNUSED_CHAT, message(messageWithID));
	}

	public void testNotifiesAuctionFailedWhenBadMessageReceived()
			throws Exception {
		String badMessage = "a bad message";
		expectFailureWithMessage(badMessage);
		translator.processMessage(UNUSED_CHAT, message(badMessage));
	}

	public void testNotifiesAuctionFailedWhenEventTypeIsMissing()
			throws Exception {
		String badMessage = "SOLVersion: 1.1; CurrentPrice: 192; Increment: 7; Bidder: "
				+ SNIPER_ID + ";";
		expectFailureWithMessage(badMessage);
		translator.processMessage(UNUSED_CHAT, message(badMessage));
	}

	private void expectFailureWithMessage(final String badMessage) {
		context.checking(new Expectations() {
			{
				oneOf(listener).auctionFailed();
				oneOf(failureReporter).cannotTranslateMessage(with(SNIPER_ID),
						with(badMessage), with(any(Exception.class)));
			}
		});
	}

	private Message message(String badMessage) {
		Message message = new Message();
		message.setBody(badMessage);
		return message;
	}
}
