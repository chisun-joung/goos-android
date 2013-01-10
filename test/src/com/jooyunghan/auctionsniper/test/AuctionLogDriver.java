package com.jooyunghan.auctionsniper.test;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.IOException;

import org.hamcrest.Matcher;

import android.util.Log;

import com.google.dexmaker.dx.util.FileUtils;
import com.jooyunghan.auctionsniper.xmpp.XMPPAuctionHouse;

public class AuctionLogDriver {

	public void hasEntry(Matcher<String> matcher) throws IOException {
		Log.d("han", "has entry? " + matcher);
		assertThat(
				new String(FileUtils.readFile(XMPPAuctionHouse.getLogPath())),
				matcher);
	}

	public void clearLog() {
		Log.d("han", "clear log");
//		new File(XMPPAuctionHouse.getLogPath()).delete();
	}
}
