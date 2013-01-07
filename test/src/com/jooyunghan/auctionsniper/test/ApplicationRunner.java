package com.jooyunghan.auctionsniper.test;

import static com.jooyunghan.auctionsniper.SnipersAdapter.textFor;

import com.jayway.android.robotium.solo.Solo;
import com.jooyunghan.auctionsniper.MainActivity;
import com.jooyunghan.auctionsniper.SniperState;

public class ApplicationRunner {
	public static final String SNIPER_XMPP_ID = "sniper";
	public static final String SNIPER_XMPP_PASSWORD = "sniper";
	public static final String XMPP_HOSTNAME = "localhost";
	private AuctionSniperDriver driver;

	public void startBiddingIn(Solo solo, final FakeAuctionServer... auctions)
			throws Exception {
		final MainActivity activity = (MainActivity) solo.getCurrentActivity();
		activity.main(arguments(auctions));
		driver = new AuctionSniperDriver(solo, 5000);
		for (FakeAuctionServer auction : auctions) {
			driver.showsSniperState(auction.getItemId(), 0, 0,
					textFor(activity, SniperState.JOINING));
		}
	}

	public void showsSniperHasLostAuction() throws InterruptedException {
		driver.showsSniperState("Lost");
	}

	public void showsSniperHasWonAuction(FakeAuctionServer auction,
			int lastPrice) throws InterruptedException {
		driver.showsSniperState(auction.getItemId(), lastPrice, lastPrice,
				"Won");
	}

	public void hasShownSniperIsBidding(FakeAuctionServer auction,
			int lastPrice, int lastBid) throws InterruptedException {
		driver.showsSniperState(auction.getItemId(), lastPrice, lastBid,
				"Bidding");
	}

	public void hasShownSniperIsWinning(FakeAuctionServer auction, int lastPrice)
			throws InterruptedException {
		driver.showsSniperState(auction.getItemId(), lastPrice, lastPrice,
				"Winning");
	}

	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}

	private static String[] arguments(FakeAuctionServer[] auctions) {
		String[] arguments = new String[auctions.length + 3];
		arguments[0] = XMPP_HOSTNAME;
		arguments[1] = SNIPER_XMPP_ID;
		arguments[2] = SNIPER_XMPP_PASSWORD;
		for (int i = 0; i < auctions.length; i++) {
			arguments[i + 3] = auctions[i].getItemId();
		}
		return arguments;
	}
}
