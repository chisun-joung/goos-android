package com.jooyunghan.auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final String AUCTION_RESOURCE = "Auction";
	private static final String ITEM_ID_AS_LOGIN = "auction-%s";
	private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/"
			+ AUCTION_RESOURCE;
	private TextView statusText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		statusText = (TextView) findViewById(R.id.status);
		statusText.setText(SniperStatus.STATUS_JOINING);

		new JoinTask().execute("192.168.1.3", "sniper", "sniper", "item-54321");
	}

	class JoinTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			XMPPConnection connection = null;
			try {
				connection = connectTo(params[0], params[1], params[2]);
			} catch (XMPPException e1) {
				e1.printStackTrace();
				Log.e("han", "connection failed");
			}
			if (connection != null) {
				Chat chat = connection.getChatManager().createChat(
						auctionId(params[3], connection),
						new MessageListener() {

							@Override
							public void processMessage(Chat chat,
									Message message) {
								statusText.post(new Runnable() {

									@Override
									public void run() {
										statusText.setText(SniperStatus.STATUS_LOST);
									}
									
								});
							}
						});
				try {
					Message m = new Message(auctionId(params[3], connection));
					m.setBody("text");
					chat.sendMessage(m);
					Log.d("han", auctionId(params[3], connection));
				} catch (XMPPException e) {
					e.printStackTrace();
					Log.e("han", "send failed");
				}
			}

			return null;
		}

		private String auctionId(String itemId, XMPPConnection connection) {
			return String.format(AUCTION_ID_FORMAT, itemId,
					connection.getServiceName());
		}

		private XMPPConnection connectTo(String host, String username,
				String password) throws XMPPException {

			ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(
					host, 5222);
			XMPPConnection connection = new XMPPConnection(
					connectionConfiguration);
			connection.connect();
			connection.login(username, password, AUCTION_RESOURCE);
			return connection;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
