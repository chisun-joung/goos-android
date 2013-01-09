package com.jooyunghan.auctionsniper.unittest;

import android.database.DataSetObserver;
import android.test.ActivityInstrumentationTestCase2;

import com.jooyunghan.auctionsniper.Defect;
import com.jooyunghan.auctionsniper.SniperSnapshot;
import com.jooyunghan.auctionsniper.ui.MainActivity;
import com.jooyunghan.auctionsniper.ui.SnipersAdapter;

public class SnipersAdapterTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private boolean onChangedCalled = false;
	private SnipersAdapter model;
	private DataSetObserver observer = new DataSetObserver() {
		@Override
		public void onChanged() {
			onChangedCalled = true;
		}
	};

	public SnipersAdapterTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		model = new SnipersAdapter(getActivity());
		model.registerDataSetObserver(observer);
	}

	public void testSetsSniperValuesProperly() throws Exception {
		SniperSnapshot joining = SniperSnapshot.joining("item id");
		SniperSnapshot bidding = joining.bidding(555, 666);

		model.addSniper(joining);
		onChangedCalled = false; // checking-true is not relevant to this test

		model.sniperStateChanged(bidding);
		assertTrue("onChanged should be called", onChangedCalled);
		assertEquals(1, model.getCount());
		assertEquals(bidding, model.getItem(0));
	}

	public void testNotifiesObserverWhenAddingASniper() throws Exception {
		SniperSnapshot joining = SniperSnapshot.joining("item123");

		assertEquals(0, model.getCount());

		model.addSniper(joining);

		assertTrue("onChanged should be called", onChangedCalled);
		assertEquals(1, model.getCount());
		assertEquals(joining, model.getItem(0));
	}

	public void testHoldsSnipersInAdditionOrder() throws Exception {
		SniperSnapshot item0 = SniperSnapshot.joining("item 0");
		SniperSnapshot item1 = SniperSnapshot.joining("item 1");
		model.addSniper(item0);
		model.addSniper(item1);
		assertEquals(item0, model.getItem(0));
		assertEquals(item1, model.getItem(1));
	}

	public void testUpdatesCorrectItemForSniper() throws Exception {
		SniperSnapshot item0 = SniperSnapshot.joining("item 0");
		SniperSnapshot item1 = SniperSnapshot.joining("item 1");
		SniperSnapshot bidding1 = item1.bidding(10, 20);
		model.addSniper(item0);
		model.addSniper(item1);
		model.sniperStateChanged(bidding1);
		assertEquals(item0, model.getItem(0));
		assertEquals(bidding1, model.getItem(1));
	}

	public void testThrowsDefectIfNoExistingSniperForAnUpdate()
			throws Exception {
		SniperSnapshot item0 = SniperSnapshot.joining("item 0");
		SniperSnapshot item1 = SniperSnapshot.joining("item 1");
		SniperSnapshot bidding1 = item1.bidding(10, 20);
		model.addSniper(item0);
		try {
			model.sniperStateChanged(bidding1);
			fail("Should throw Defect");
		} catch (Defect d) {
		}
	}
}
