package com.jiuqi.rpa.lib.browser;

import java.util.List;
import java.util.TimerTask;

import org.openqa.selenium.remote.RemoteWebDriver;

public class RemoteDriverTimerTask extends TimerTask {

	public void run() {
		RemoteDriverManager manager = RemoteDriverManager.getInstance();
		List<RemoteWebDriver> list = manager.getDriverList();
		for(RemoteWebDriver driver :list) {
			manager.isDriverReachable(driver);
		}
	}
}
