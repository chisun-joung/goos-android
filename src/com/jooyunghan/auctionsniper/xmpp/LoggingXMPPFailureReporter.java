package com.jooyunghan.auctionsniper.xmpp;

import org.apache.log4j.Logger;

import android.util.Log;


public class LoggingXMPPFailureReporter implements XMPPFailureReporter {

	private final Logger logger;

	public LoggingXMPPFailureReporter(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void cannotTranslateMessage(String auctionId, String failedMessage,
			Exception exception) {
		Log.d("han", "write log");
		logger.error("<" + auctionId + "> " + "Could not translate message \""
				+ failedMessage + "\" " + "because \"" + exception + "\"");
	}

}
