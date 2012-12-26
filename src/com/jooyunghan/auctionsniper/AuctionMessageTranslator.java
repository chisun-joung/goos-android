package com.jooyunghan.auctionsniper;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

public class AuctionMessageTranslator implements MessageListener {

	private AuctionEventListener listener;

	public AuctionMessageTranslator(AuctionEventListener listener) {
		this.listener = listener;
	}

	@Override
	public void processMessage(Chat chat, Message message) {
		Map<String, String> event = unpackEventFrom(message);
		String type = event.get("Event");
		if ("CLOSE".equals(type)) {
			listener.auctionClosed();
		} else if ("PRICE".equals(type)) {
			final int price = Integer.parseInt(event.get("CurrentPrice"));
			final int increment = Integer.parseInt(event.get("Increment"));
			listener.currentPrice(price, increment);
		}
	}

	private Map<String, String> unpackEventFrom(Message message) {
		HashMap<String, String> event = new HashMap<String, String>();
		for (String element: message.getBody().split(";")) {
			String[] pairs = element.split(":");
			event.put(pairs[0].trim(), pairs[1].trim());
		}
		return event;
	}

}
