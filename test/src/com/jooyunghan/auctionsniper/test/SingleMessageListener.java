package com.jooyunghan.auctionsniper.test;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;

import java.util.concurrent.ArrayBlockingQueue;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import android.util.Log;

public class SingleMessageListener implements MessageListener {
	private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(
			1);

	@Override
	public void processMessage(Chat chat, Message message) {
		Log.d("han", "message received: " + message.getBody());
		messages.add(message);
	}

	public void receivesAMessage(Matcher<? super String> messageMatcher)
			throws InterruptedException {
		final Message message = messages.poll(5, SECONDS);
		assertThat(message, hasBody(messageMatcher));
	}

	private Matcher<? super Message> hasBody(
			Matcher<? super String> messageMatcher) {
		return new FeatureMatcher<Message, String>(messageMatcher, "message with body of", "was") {
			@Override
			protected String featureValueOf(Message actual) {
				return actual.getBody();
			}
		};
	}
}
