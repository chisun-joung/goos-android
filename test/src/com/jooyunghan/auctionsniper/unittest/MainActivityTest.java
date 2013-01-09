package com.jooyunghan.auctionsniper.unittest;

import static org.hamcrest.Matchers.equalTo;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.jooyunghan.auctionsniper.Item;
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
		final ValueMatcherProbe<Item> buttonProbe = new ValueMatcherProbe<Item>(
				equalTo(new Item("an item-id", 789)), "item request for");

		activity.setUserRequestListener(new UserRequestListener() {
			@Override
			public void joinAuction(Item item) {
				buttonProbe.setReceivedValue(item);
			}
		});
		driver.startBiddingFor("an item-id", 789);
		driver.check(buttonProbe);
	}
}
