package com.jooyunghan.auctionsniper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.jooyunghan.auctionsniper.ui.SniperLauncher;
import com.jooyunghan.auctionsniper.xmpp.XMPPAuctionHouse;

import android.app.Application;
import android.util.Log;

public class ApplicationMain extends Application {
	private final SniperPortfolio sniperPortfolio = new SniperPortfolio();
	private AuctionHouse auctionHouse = null;
	private SniperLauncher sniperLauncher = null;
	private FutureTask<AuctionHouse> connection;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("han", "Application created");
		connection = new FutureTask<AuctionHouse>(new Callable<AuctionHouse>() {
			@Override
			public AuctionHouse call() throws Exception {
				return new XMPPAuctionHouse("localhost", "sniper", "sniper");
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
			auctionHouse = connection.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		sniperLauncher = new SniperLauncher(auctionHouse, sniperPortfolio);
		return sniperLauncher;
	}

	public void dispose() {
		sniperPortfolio.removeAllSnipers();
	}
}
