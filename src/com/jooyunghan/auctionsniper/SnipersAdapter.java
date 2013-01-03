package com.jooyunghan.auctionsniper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SnipersAdapter extends BaseAdapter implements SniperListener {

	private Context context;
	private SniperSnapshot snapshot;

	public SnipersAdapter(Context context) {
		this.context = context;
		snapshot = new SniperSnapshot("", 0, 0, SniperState.JOINING);
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Object getItem(int position) {
		return snapshot;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item, parent, false);
		}
		setField(convertView, R.id.text_item_id, getItemId());
		setField(convertView, R.id.text_detail, getDetail());
		setField(convertView, R.id.text_status, getState());

		return convertView;
	}

	private void setField(View view, int resId, String fieldValue) {
		TextView tv = (TextView) view.findViewById(resId);
		tv.setText(fieldValue);
	}

	@Override
	public void sniperStateChanged(SniperSnapshot snapshot) {
		Log.d("han", "sniperStatesChanged(" + snapshot + ")");
		this.snapshot = snapshot;
		this.notifyDataSetChanged();
	}

	public String getItemId() {
		return snapshot.itemId;
	}

	public String getDetail() {
		return String.format("%d/%d", snapshot.lastPrice, snapshot.lastBid);
	}

	public String getState() {
		switch (snapshot.state) {
		case JOINING: 
			return context.getString(R.string.status_joining);
		case BIDDING: 
			return context.getString(R.string.status_bidding);
		case WINNING: 
			return context.getString(R.string.status_winning);
		case LOST: 
			return context.getString(R.string.status_lost);
		case WON: 
			return context.getString(R.string.status_won);
		}
		throw new IllegalArgumentException("Unknown state:" + snapshot.state);
	}

}
