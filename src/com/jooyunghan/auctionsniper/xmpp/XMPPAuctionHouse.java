package com.jooyunghan.auctionsniper.xmpp;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.os.Environment;
import android.util.Log;

import com.jooyunghan.auctionsniper.Auction;
import com.jooyunghan.auctionsniper.AuctionHouse;
import com.jooyunghan.auctionsniper.Item;

public class XMPPAuctionHouse implements AuctionHouse {
	private static final String ITEM_ID_AS_LOGIN = "auction-%s";
	private static final String AUCTION_RESOURCE = "Auction";
	private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/"
			+ AUCTION_RESOURCE;
	public static final String LOG_FILE_NAME = "auction-sniper.log";
	private XMPPConnection connection;
	private XMPPFailureReporter failureReporter;

	public XMPPAuctionHouse(XMPPConnection connection)
			throws XMPPAuctionException {
		this.connection = connection;
		this.failureReporter = new LoggingXMPPFailureReporter(makeLogger());
	}

	@Override
	public Auction auctionFor(Item item) {
		return new XMPPAuction(connection, auctionId(item.identifier,
				connection), failureReporter);
	}

	private Logger makeLogger() throws XMPPAuctionException {
		return Logger.getLogger("AuctionSniper");
	}

	public static String getLogPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/auctionsniper/auction-sniper.log";
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
