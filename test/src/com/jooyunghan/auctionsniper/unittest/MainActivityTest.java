package com.jooyunghan.auctionsniper.unittest;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import com.jooyunghan.auctionsniper.MainActivity;
import com.jooyunghan.auctionsniper.R;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity activity;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
	}

	public void testStatusShowsInListView() throws Exception {
		ListView list = (ListView) activity.findViewById(R.id.list);
		assertNotNull(list);
	}

}
