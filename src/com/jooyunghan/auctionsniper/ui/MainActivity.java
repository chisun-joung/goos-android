package com.jooyunghan.auctionsniper.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jooyunghan.auctionsniper.R;
import com.jooyunghan.auctionsniper.SniperPortfolio;
import com.jooyunghan.auctionsniper.UserRequestListener;
import com.jooyunghan.auctionsniper.xmpp.XMPPAuctionHouse;
import com.jooyunghan.util.Announcer;

public class MainActivity extends Activity {
	private static final int ARG_HOSTNAME = 0;
	private static final int ARG_USERNAME = 1;
	private static final int ARG_PASSWORD = 2;
	private SnipersAdapter snipers;
	private final Announcer<UserRequestListener> userRequests = Announcer
			.to(UserRequestListener.class);
	private final SniperPortfolio portfolio = new SniperPortfolio();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		snipers = new SnipersAdapter(this);
		portfolio.addPortfolioListener(snipers);
		final ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(snipers);

		final TextView itemIdField = (TextView) findViewById(R.id.item_id_text);
		final Button button = (Button) findViewById(R.id.bid_button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userRequests.announce().joinAuction(
						itemIdField.getText().toString());
			}
		});
	}

	// test-only method (called from test)
	public void main(final String[] args) throws Exception {
		final XMPPAuctionHouse auctionHouse = new XMPPAuctionHouse(
				args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
		addUserRequestListener(new SniperLaucher(auctionHouse, portfolio));
	}

	public void addUserRequestListener(UserRequestListener userRequestListener) {
		userRequests.addListener(userRequestListener);
	}
}
