package com.jooyunghan.auctionsniper.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jooyunghan.auctionsniper.ApplicationMain;
import com.jooyunghan.auctionsniper.R;
import com.jooyunghan.auctionsniper.SniperPortfolio;
import com.jooyunghan.auctionsniper.UserRequestListener;

public class MainActivity extends Activity {
	private SnipersAdapter snipers;
	private UserRequestListener userRequestListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		snipers = new SnipersAdapter(this);
		final ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(snipers);

		final TextView itemIdField = (TextView) findViewById(R.id.item_id_text);
		final Button button = (Button) findViewById(R.id.bid_button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (userRequestListener != null) {
					userRequestListener.joinAuction(itemIdField.getText()
							.toString());
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		final SniperPortfolio portfolio = ((ApplicationMain) getApplication())
				.getSniperPortfolio();
		portfolio.addPortfolioListener(snipers);

		final SniperLauncher sniperLauncher = ((ApplicationMain) getApplication())
				.getSniperLauncher();
		setUserRequestListener(sniperLauncher);
	}

	@Override
	protected void onPause() {
		final SniperPortfolio portfolio = ((ApplicationMain) getApplication())
				.getSniperPortfolio();
		portfolio.removePortfolioListener(snipers);

		super.onPause();
	}

	public void setUserRequestListener(UserRequestListener userRequestListener) {
		this.userRequestListener = userRequestListener;
	}

}
