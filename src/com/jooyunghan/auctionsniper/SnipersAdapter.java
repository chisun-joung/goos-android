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
	private String status;
	private SniperState state;

	public SnipersAdapter(Context context) {
		this.context = context;
		status = context.getString(R.string.status_joining);
		state = new SniperState("", 0, 0);
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Object getItem(int position) {
		return status;
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
		setField(convertView, R.id.text_status, getStatus());

		return convertView;
	}

	private void setField(View view, int resId, String fieldValue) {
		TextView tv = (TextView) view.findViewById(resId);
		tv.setText(fieldValue);
	}

	public void showStatus(String status) {
		Log.d("han", "showStatus(" + status + ")");
		this.status = status;
		this.notifyDataSetChanged();
	}

	public void sniperStatusChanged(SniperState state, String status) {
		Log.d("han", "sniperStatusChanged(" + state + ", " + status + ")");
		this.status = status;
		this.state = state;
		this.notifyDataSetChanged();
	}

	public String getItemId() {
		return state.itemId;
	}

	public String getDetail() {
		return String.format("%d/%d", state.lastPrice, state.lastBid);
	}

	public String getStatus() {
		return status;
	}

}
