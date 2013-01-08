package com.jooyunghan.auctionsniper.test;

import static com.jooyunghan.auctionsniper.ui.SnipersAdapter.textFor;

import org.jivesoftware.smack.XMPPException;

import com.jayway.android.robotium.solo.Solo;
import com.jooyunghan.auctionsniper.SniperState;
import com.jooyunghan.auctionsniper.ui.MainActivity;

public class ApplicationRunner {
	public static final String SNIPER_XMPP_ID = "sniper";
	public static final String SNIPER_XMPP_PASSWORD = "sniper";
	public static final String XMPP_HOSTNAME = "localhost";
	private AuctionSniperDriver driver;
	private MainActivity activity;

	public void startBiddingIn(Solo solo, final FakeAuctionServer... auctions)
			throws Exception {
		startSniper(solo);
		for (FakeAuctionServer auction : auctions) {
			final String itemId = auction.getItemId();
			driver.startBiddingFor(itemId);
			driver.showsSniperState(itemId, 0, 0,
					textFor(activity, SniperState.JOINING));
		}
	}

	private void startSniper(Solo solo, final FakeAuctionServer... auctions)
			throws Exception {
		activity = (MainActivity) solo.getCurrentActivity();
		activity.main(new String[] { XMPP_HOSTNAME, SNIPER_XMPP_ID,
				SNIPER_XMPP_PASSWORD });
		driver = new AuctionSniperDriver(solo, 5000);
	}

	public void showsSniperHasLostAuction() throws InterruptedException {
		driver.showsSniperState(textFor(activity, SniperState.LOST));
	}

	public void showsSniperHasWonAuction(FakeAuctionServer auction,
			int lastPrice) throws InterruptedException {
		driver.showsSniperState(auction.getItemId(), lastPrice, lastPrice,
				textFor(activity, SniperState.WON));
	}

	public void hasShownSniperIsBidding(FakeAuctionServer auction,
			int lastPrice, int lastBid) throws InterruptedException {
		driver.showsSniperState(auction.getItemId(), lastPrice, lastBid,
				textFor(activity, SniperState.BIDDING));
	}

	public void hasShownSniperIsWinning(FakeAuctionServer auction, int lastPrice)
			throws InterruptedException {
		driver.showsSniperState(auction.getItemId(), lastPrice, lastPrice,
				textFor(activity, SniperState.WINNING));
	}

	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}
}
