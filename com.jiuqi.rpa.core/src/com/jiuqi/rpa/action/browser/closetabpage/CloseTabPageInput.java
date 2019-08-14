package com.jiuqi.rpa.action.browser.closetabpage;


import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.lib.browser.UIBrowser;

/**
 * πÿ±’‰Ø¿¿∆˜“≥«©ªÓ∂Ø ‰»Î
 * 
 * @author wangshanyu
 */
public class CloseTabPageInput implements IActionInput {
	private UIBrowser browser = null;
	
	public UIBrowser getBrowser() {
		return browser;
	}
	public void setBrowser(UIBrowser browser) {
		this.browser = browser;
	}
}
