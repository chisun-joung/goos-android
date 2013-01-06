package com.jooyunghan.auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends Activity {
	private static final int ARG_HOSTNAME = 0;
	private static final int ARG_USERNAME = 1;
	private static final int ARG_PASSWORD = 2;
	private static final String ITEM_ID_AS_LOGIN = "auction-%s";
	private static final String AUCTION_RESOURCE = "Auction";
	private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/"
			+ AUCTION_RESOURCE;
	private ListView list;
	private Chat notToBeGCd;
	private SnipersAdapter adapter;
	public XMPPConnection connection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		list = (ListView) findViewById(R.id.list);
		adapter = new SnipersAdapter(this);
		list.setAdapter(adapter);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		if (connection != null && connection.isConnected()) {
//			connection.disconnect();
//			connection = null;
//			Log.d("han", "disconnected");
//		}
	}

	private void joinAuction(XMPPConnection connection, String itemId) {
		final Chat chat = connection.getChatManager().createChat(
				auctionId(itemId, connection), null);
		this.notToBeGCd = chat;

		Auction auction = new XMPPAuction(chat);
		chat.addMessageListener(new AuctionMessageTranslator(connection
				.getUser(), new AuctionSniper(itemId, auction,
				new UIThreadSniperListener(this, adapter))));
		auction.join();
	}

	private String auctionId(String itemId, XMPPConnection connection) {
		return String.format(AUCTION_ID_FORMAT, itemId,
				connection.getServiceName());
	}

	private XMPPConnection connectTo(String host, String username,
			String password) throws XMPPException {
		ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(
				host, 5222);
		XMPPConnection connection = new XMPPConnection(connectionConfiguration);
		connection.connect();
		Log.d("han", "connect:" + username);
		connection.login(username, password, AUCTION_RESOURCE);
		Log.d("han", "login:" + username);
		return connection;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_join) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class JoinTask extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {
			try {
				connection = connectTo(params[ARG_HOSTNAME],
						params[ARG_USERNAME], params[ARG_PASSWORD]);
				for (int i = 3; i < params.length; i++) {
					joinAuction(connection, params[i]);
				}
			} catch (XMPPException e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}

	// test-only method (called from test)
	// should run on UI thread
	public void main(String[] arguments) {
		new JoinTask().execute(arguments);
	}
}
