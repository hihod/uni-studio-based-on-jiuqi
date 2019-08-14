package com.jiuqi.rpa.action.browser.openbrowser;


import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.lib.browser.WebBrowserType;

/**
 * 打开浏览器活动输入
 * 
 * @author wangshanyu
 */
public class OpenBrowserInput implements IActionInput {
	private WebBrowserType browserType;
	private String url;
	
	public WebBrowserType getBrowserType() {
		return browserType;
	}
	public void setBrowserType(WebBrowserType browserType) {
		this.browserType = browserType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
