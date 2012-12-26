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
import android.widget.TextView;

public class MainActivity extends Activity implements AuctionEventListener {

	private static final String ITEM_ID_AS_LOGIN = "auction-%s";
	private static final String AUCTION_RESOURCE = "Auction";
	private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/"
			+ AUCTION_RESOURCE;
	private TextView statusText;
	public Chat notToBeGCd;
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		statusText = (TextView) findViewById(R.id.status);
		statusText.setText(SniperStatus.STATUS_JOINING);

		new JoinTask().execute("localhost", "sniper", "sniper", "item-54321");
	}

	class JoinTask extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {
			try {
				joinAuction(connectTo(params[0], params[1], params[2]),
						params[3]);
			} catch (XMPPException e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}

	private void joinAuction(XMPPConnection connection, String itemId)
			throws XMPPException {
		Chat chat = connection.getChatManager().createChat(
				auctionId(itemId, connection),
				new AuctionMessageTranslator(this));
		Log.d("han", "chat created with " + chat.getParticipant());
		this.notToBeGCd = chat;
		chat.sendMessage(JOIN_COMMAND_FORMAT);
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
		connection.login(username, password, AUCTION_RESOURCE);
		Log.d("han", "login:" + username);
		return connection;
	}

	private void showStatus(String status) {
		statusText.setText(status);
		Log.d("han", "showStatus:" + status);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void auctionClosed() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showStatus(SniperStatus.STATUS_LOST);
			}
		});
	}
}
