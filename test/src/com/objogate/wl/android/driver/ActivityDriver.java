package com.objogate.wl.android.driver;

import android.view.View;

import com.jayway.android.robotium.solo.Solo;
import com.objogate.wl.Probe;
import com.objogate.wl.Prober;
import com.objogate.wl.android.UIPollingProber;

public class ActivityDriver {

	protected Solo solo;
	protected Prober prober;

	public ActivityDriver(Solo solo, int timeoutMillis) {
		this.solo = solo;
		this.prober = new UIPollingProber(solo, timeoutMillis, 100);
	}

	public void check(Probe probe) {
		prober.check(probe);
	}

	protected View getView(int resId) {
		return solo.getView(resId);
	}
}