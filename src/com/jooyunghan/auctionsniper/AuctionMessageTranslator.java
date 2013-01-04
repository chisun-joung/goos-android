package com.jooyunghan.auctionsniper;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import android.util.Log;

import com.jooyunghan.auctionsniper.AuctionEventListener.PriceSource;

public class AuctionMessageTranslator implements MessageListener {

	private String sniperId;
	private AuctionEventListener listener;

	public AuctionMessageTranslator(String sniperId,
			AuctionEventListener listener) {
		this.sniperId = sniperId;
		this.listener = listener;
	}

	@Override
	public void processMessage(Chat chat, Message message) {
		Log.d("han",
				sniperId + " got message from server: " + message.getBody());
		AuctionEvent event = AuctionEvent.from(message);
		String type = event.type();
		if ("CLOSE".equals(type)) {
			listener.auctionClosed();
		} else if ("PRICE".equals(type)) {
			listener.currentPrice(event.currentPrice(), event.increment(),
					event.isFrom(sniperId));
		}
	}

	private static class AuctionEvent {
		private Map<String, String> fields = new HashMap<String, String>();

		public String type() {
			return get("Event");
		}

		public int currentPrice() {
			return getInt("CurrentPrice");
		}

		public int increment() {
			return getInt("Increment");
		}

		private String bidder() {
			return get("Bidder");
		}

		public PriceSource isFrom(String sniperId) {
			if (sameId(sniperId, bidder()))
				return PriceSource.FromSniper;
			else
				return PriceSource.FromOtherBidder;
		}

		private boolean sameId(String sniperId, String bidder) {
			return unstructure(sniperId).equals(unstructure(bidder));
		}

		private String unstructure(String string) {
			return string.split("@")[0];
		}

		private String get(String fieldName) {
			return fields.get(fieldName);
		}

		private int getInt(String fieldName) {
			return Integer.parseInt(get(fieldName));
		}

		static AuctionEvent from(Message message) {
			AuctionEvent event = new AuctionEvent();
			for (String field : fieldsIn(message)) {
				event.addField(field);
			}
			return event;
		}

		private void addField(String field) {
			String[] pair = field.split(":");
			fields.put(pair[0].trim(), pair[1].trim());
		}

		private static String[] fieldsIn(Message message) {
			return message.getBody().split(";");
		}

	}
}
