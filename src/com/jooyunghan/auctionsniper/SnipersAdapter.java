package com.jooyunghan.auctionsniper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SnipersAdapter extends BaseAdapter {

	private Context context;
	private String state;
	private SniperSnapshot snapshot;

	public SnipersAdapter(Context context) {
		this.context = context;
		state = context.getString(R.string.status_joining);
		snapshot = new SniperSnapshot("", 0, 0, SniperState.JOINING);
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Object getItem(int position) {
		return state;
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

	public void showState(String state) {
		Log.d("han", "showStatus(" + state + ")");
		this.state = state;
		this.notifyDataSetChanged();
	}

	public void sniperStatesChanged(SniperSnapshot snapshot, String state) {
		Log.d("han", "sniperStatesChanged(" + snapshot + ", " + state + ")");
		this.state = state;
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
		return state;
	}

}
