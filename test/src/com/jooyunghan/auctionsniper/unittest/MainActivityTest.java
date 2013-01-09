package com.jooyunghan.auctionsniper.unittest;

import static org.hamcrest.Matchers.equalTo;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.jooyunghan.auctionsniper.UserRequestListener;
import com.jooyunghan.auctionsniper.test.AuctionSniperDriver;
import com.jooyunghan.auctionsniper.ui.MainActivity;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity activity;
	private Solo solo;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		solo = new Solo(getInstrumentation(), activity);
	}

	public void testMakesUserRequestWhenJoinButtonClicked() throws Exception {
		AuctionSniperDriver driver = new AuctionSniperDriver(solo, 100);
		final ValueMatcherProbe<String> buttonProbe = new ValueMatcherProbe<String>(
				equalTo("an item-id"), "join request for");

		activity.setUserRequestListener(new UserRequestListener() {
			@Override
			public void joinAuction(String itemId) {
				buttonProbe.setReceivedValue(itemId);
			}
		});
		driver.startBiddingFor("an item-id");
		driver.check(buttonProbe);
	}
}
