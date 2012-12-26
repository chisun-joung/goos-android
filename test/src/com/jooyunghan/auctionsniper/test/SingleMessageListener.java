package com.jooyunghan.auctionsniper.test;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.concurrent.ArrayBlockingQueue;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import android.util.Log;

public class SingleMessageListener implements MessageListener {
	private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(
			1);

	@Override
	public void processMessage(Chat chat, Message message) {
		Log.d("han", "message received by fake");
		messages.add(message);
	}

	public void receivesAMessage() throws InterruptedException {
		Log.d("han", "check message");
		assertThat("Message", messages.poll(5, SECONDS), is(notNullValue()));
	}
}
