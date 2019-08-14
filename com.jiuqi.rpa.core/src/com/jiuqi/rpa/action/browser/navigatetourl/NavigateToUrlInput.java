package com.jiuqi.rpa.action.browser.navigatetourl;


import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.lib.browser.UIBrowser;

/**
 * 浏览器跳转活动输入
 * 
 * @author wangshanyu
 */
public class NavigateToUrlInput implements IActionInput {
	private UIBrowser browser;
	private String url;
	
	public UIBrowser getBrowser() {
		return browser;
	}
	public void setBrowser(UIBrowser browser) {
		this.browser = browser;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
