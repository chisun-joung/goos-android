package com.jooyunghan.auctionsniper.unittest;

import android.database.DataSetObserver;
import android.test.AndroidTestCase;

import com.jooyunghan.auctionsniper.SniperSnapshot;
import com.jooyunghan.auctionsniper.SniperState;
import com.jooyunghan.auctionsniper.SnipersAdapter;

public class SnipersAdapterTest extends AndroidTestCase {
	private static final String ITEM_ID = "item-id";
	protected boolean onChangedCalled = false;
	
	private DataSetObserver observer = new DataSetObserver() {
		@Override
		public void onChanged() {
			onChangedCalled    = true;
		}
	};

	public void testSetsSniperValuesProperly() throws Exception {
		SnipersAdapter adapter = new SnipersAdapter(getContext());
		adapter.registerDataSetObserver(observer);
		
		adapter.sniperStateChanged(new SniperSnapshot(ITEM_ID, 555, 666, SniperState.BIDDING));
		
		assertTrue("onChanged should be called", onChangedCalled);
		assertEquals("item-id", adapter.getItemId());
		assertEquals("555/666", adapter.getDetail());
		assertEquals("Bidding", adapter.getState());
	}
}
