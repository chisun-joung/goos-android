package com.jooyunghan.auctionsniper.unittest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import android.database.DataSetObserver;
import android.test.AndroidTestCase;

import com.jooyunghan.auctionsniper.SniperSnapshot;
import com.jooyunghan.auctionsniper.SniperState;
import com.jooyunghan.auctionsniper.SniperStatus;
import com.jooyunghan.auctionsniper.SnipersAdapter;

public class SnipersAdapterTest extends AndroidTestCase {
	private static final String ITEM_ID = "item-id";
	private DataSetObserver observer = mock(DataSetObserver.class);

	public void testSetsSniperValuesProperly() throws Exception {
		SnipersAdapter adapter = new SnipersAdapter(getContext());
		adapter.registerDataSetObserver(observer);
		
		adapter.sniperStatesChanged(new SniperSnapshot(ITEM_ID, 555, 666, SniperState.BIDDING), SniperStatus.STATUS_BIDDING);
		
		verify(observer).onChanged();
		assertEquals("item-id", adapter.getItemId());
		assertEquals("555/666", adapter.getDetail());
		assertEquals(SniperStatus.STATUS_BIDDING, adapter.getState());
	}
}
