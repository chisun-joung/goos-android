package com.jooyunghan.auctionsniper.test;

import com.jooyunghan.auctionsniper.ApplicationMain;

import android.app.Instrumentation;
import android.test.InstrumentationTestCase;

public class ApplicationMainTest extends InstrumentationTestCase {
	public void testInitialized() throws Exception {
		Instrumentation inst = getInstrumentation();
		ApplicationMain main = (ApplicationMain) inst.getTargetContext()
				.getApplicationContext();
		assertNotNull(main);
		assertNotNull(main.getSniperLauncher());
		assertNotNull(main.getSniperPortfolio());
	}
}
