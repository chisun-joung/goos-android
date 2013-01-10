package com.jooyunghan.auctionsniper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.jivesoftware.smack.XMPPConnection;

import com.jooyunghan.auctionsniper.ui.SniperLauncher;
import com.jooyunghan.auctionsniper.xmpp.XMPPAuctionException;
import com.jooyunghan.auctionsniper.xmpp.XMPPAuctionHouse;

import android.app.Application;
import android.util.Log;

public class ApplicationMain extends Application {
	private final SniperPortfolio sniperPortfolio = new SniperPortfolio();
	private AuctionHouse auctionHouse = null;
	private SniperLauncher sniperLauncher = null;
	private FutureTask<XMPPConnection> connection;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("han", "Application created");
		ConfigureLog4J.configure();
		connection = new FutureTask<XMPPConnection>(new Callable<XMPPConnection>() {
			@Override
			public XMPPConnection call() throws Exception {
				return XMPPAuctionHouse.connect("localhost", "sniper", "sniper");
			}
		});
		Executors.newSingleThreadExecutor().execute(connection);
	}

	@Override
	public void onTerminate() {
		Log.d("han", "Application terminated");
		super.onTerminate();
	}

	public SniperPortfolio getSniperPortfolio() {
		return sniperPortfolio;
	}

	public SniperLauncher getSniperLauncher() {
		if (sniperLauncher != null) {
			return sniperLauncher;
		}
		
		try {
			auctionHouse = new XMPPAuctionHouse(connection.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (XMPPAuctionException e) {
			e.printStackTrace();
		}
		sniperLauncher = new SniperLauncher(auctionHouse, sniperPortfolio);
		return sniperLauncher;
	}

	public void dispose() {
		sniperPortfolio.removeAllSnipers();
	}
}
