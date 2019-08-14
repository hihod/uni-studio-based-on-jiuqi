package com.jiuqi.rpa.action.browser.openbrowser;

import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.lib.browser.UIBrowser;
/**
 * 打开浏览器活动输出
 * @author wangshanyu
 */
public class OpenBrowserOutput implements IActionOutput {
	private UIBrowser browser ;
	
	public UIBrowser getBrowser() {
		return browser;
	}
	public void setBrowser(UIBrowser browser) {
		this.browser = browser;
	}
}
