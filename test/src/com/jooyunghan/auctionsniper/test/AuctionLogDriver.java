package com.jooyunghan.auctionsniper.test;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

import org.hamcrest.Matcher;

import com.jooyunghan.auctionsniper.xmpp.XMPPAuctionHouse;

import android.content.Context;

public class AuctionLogDriver {
	private final File logFile;
	private Context context;

	public AuctionLogDriver(Context context) {
		this.context = context;
		logFile = context.getFileStreamPath(XMPPAuctionHouse.LOG_FILE_NAME);
	}

	public void hasEntry(Matcher<String> matcher) throws IOException {
		FileInputStream input = context.openFileInput(XMPPAuctionHouse.LOG_FILE_NAME);
		byte[] data = new byte[input.available()];
		while (input.read(data) != -1) {
			;
		}
		input.close();
		assertThat(new String(data), matcher);
	}

	public void clearLog() {
		logFile.delete();
		LogManager.getLogManager().reset();
	}
}
