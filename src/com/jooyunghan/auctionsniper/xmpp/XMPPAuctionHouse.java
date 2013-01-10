package com.jooyunghan.auctionsniper.xmpp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.content.Context;
import android.util.Log;

import com.jooyunghan.auctionsniper.Auction;
import com.jooyunghan.auctionsniper.AuctionHouse;
import com.jooyunghan.auctionsniper.Item;

public class XMPPAuctionHouse implements AuctionHouse {
	private static final String LOGGER_NAME = "AuctionSniperLogger";
	private static final String ITEM_ID_AS_LOGIN = "auction-%s";
	private static final String AUCTION_RESOURCE = "Auction";
	private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/"
			+ AUCTION_RESOURCE;
	public static final String LOG_FILE_NAME = "auction-sniper.log";
	private XMPPConnection connection;
	private XMPPFailureReporter failureReporter;
	private Context context;

	public XMPPAuctionHouse(Context context, XMPPConnection connection)
			throws XMPPAuctionException {
		this.context = context;
		this.connection = connection;
		this.failureReporter = new LoggingXMPPFailureReporter(makeLogger());
	}

	@Override
	public Auction auctionFor(Item item) {
		return new XMPPAuction(connection, auctionId(item.identifier,
				connection), failureReporter);
	}

	private Logger makeLogger() throws XMPPAuctionException {
		Logger logger = Logger.getLogger(LOGGER_NAME);
		logger.setUseParentHandlers(false);
		logger.addHandler(simpleFileHandler());
		return logger;
	}

	private Handler simpleFileHandler() throws XMPPAuctionException {
		try {
			FileOutputStream output = context.openFileOutput(LOG_FILE_NAME,
					Context.MODE_WORLD_READABLE);
			return new StreamHandler(output, new SimpleFormatter());
		} catch (IOException e) {
			Log.d("han", "could not create logger");
			throw new XMPPAuctionException(
					"Could not create logger FileHandler "
							+ getFullPath(LOG_FILE_NAME), e);
		}
	}

	private String getFullPath(String logFileName) {
		return logFileName;
	}

	private String auctionId(String itemId, XMPPConnection connection) {
		return String.format(AUCTION_ID_FORMAT, itemId,
				connection.getServiceName());
	}

	public static XMPPConnection connect(String host, String username,
			String password) throws XMPPException {
		ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(
				host, 5222);
		XMPPConnection connection = new XMPPConnection(connectionConfiguration);
		connection.connect();
		Log.d("han", "connect:" + username);
		connection.login(username, password, AUCTION_RESOURCE);
		Log.d("han", "login:" + username);
		return connection;
	}

}
