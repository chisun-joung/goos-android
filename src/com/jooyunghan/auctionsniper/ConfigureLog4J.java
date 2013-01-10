package com.jooyunghan.auctionsniper;

import org.apache.log4j.Level;

import com.jooyunghan.auctionsniper.xmpp.XMPPAuctionHouse;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Call {@link #configure()} from your application's activity.
 */
public class ConfigureLog4J {
	public static void configure() {
		final LogConfigurator logConfigurator = new LogConfigurator();

		logConfigurator.setFileName(XMPPAuctionHouse.getLogPath());
		logConfigurator.setRootLevel(Level.DEBUG);
		// Set log level of a specific logger
		logConfigurator.setLevel("org.apache", Level.ERROR);
		logConfigurator.configure();
	}
}
