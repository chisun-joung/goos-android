package com.jooyunghan.auctionsniper.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jooyunghan.auctionsniper.AuctionSniper;
import com.jooyunghan.auctionsniper.Defect;
import com.jooyunghan.auctionsniper.PortfolioListener;
import com.jooyunghan.auctionsniper.R;
import com.jooyunghan.auctionsniper.SniperListener;
import com.jooyunghan.auctionsniper.SniperSnapshot;
import com.jooyunghan.auctionsniper.SniperState;
import com.jooyunghan.auctionsniper.UIThreadSniperListener;

public class SnipersAdapter extends BaseAdapter implements SniperListener,
		PortfolioListener {
	private Activity context;
	private List<SniperSnapshot> snapshots = new ArrayList<SniperSnapshot>();

	public SnipersAdapter(Activity context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return snapshots.size();
	}

	@Override
	public Object getItem(int position) {
		return snapshots.get(position);
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
		SniperSnapshot snapshot = snapshots.get(position);
		setField(convertView, R.id.text_item_id, snapshot.itemId);
		setField(convertView, R.id.text_detail, getDetailText(snapshot));
		setField(convertView, R.id.text_status, getStateText(snapshot));
		return convertView;
	}

	private void setField(View view, int resId, String fieldValue) {
		TextView tv = (TextView) view.findViewById(resId);
		tv.setText(fieldValue);
	}

	private String getDetailText(SniperSnapshot snapshot) {
		return String.format("%d/%d", snapshot.lastPrice, snapshot.lastBid);
	}

	public String getStateText(SniperSnapshot snapshot) {
		return textFor(context, snapshot.state);
	}

	public static String textFor(Context context, SniperState state) {
		switch (state) {
		case JOINING:
			return context.getString(R.string.status_joining);
		case BIDDING:
			return context.getString(R.string.status_bidding);
		case WINNING:
			return context.getString(R.string.status_winning);
		case LOSING:
			return context.getString(R.string.status_losing);
		case LOST:
			return context.getString(R.string.status_lost);
		case WON:
			return context.getString(R.string.status_won);
		case FAILED:
			return context.getString(R.string.status_failed);
		}
		throw new IllegalArgumentException("Unknown state:" + state);
	}

	@Override
	public void sniperStateChanged(SniperSnapshot newSnapshot) {
		int row = rowMatching(newSnapshot);
		snapshots.set(row, newSnapshot);
		notifyDataSetChanged();
	}

	@Override
	public void sniperAdded(AuctionSniper sniper) {
		addSniperSnapshot(sniper.getSnapshot());
		sniper.addSniperListener(new UIThreadSniperListener(context, this));
	}

	private int rowMatching(SniperSnapshot newSnapshot) {
		for (int i = 0; i < snapshots.size(); i++) {
			SniperSnapshot snapshot = snapshots.get(i);
			if (snapshot.isForSameItemAs(newSnapshot)) { // avoid feature envy
				return i;
			}
		}
		throw new Defect("Cannot find match for " + newSnapshot);
	}

	public void addSniperSnapshot(SniperSnapshot snapshot) {
		snapshots.add(snapshot);
		notifyDataSetChanged();
	}

}
