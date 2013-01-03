package com.jooyunghan.auctionsniper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SnipersAdapter extends BaseAdapter {

	private Context context;
	private String status;

	public SnipersAdapter(Context context) {
		this.context = context;
		status = context.getString(R.string.status_joining);
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
		// TextView textViewItemId = (TextView)
		// convertView.findViewById(R.id.text_item_id);
		TextView textViewStatus = (TextView) convertView
				.findViewById(R.id.text_status);
		// TextView textViewDetail = (TextView)
		// convertView.findViewById(R.id.text_detail);

		textViewStatus.setText(status);
		return convertView;
	}

	public void showStatus(String status) {
		this.status = status;
		this.notifyDataSetChanged();
	}

}
