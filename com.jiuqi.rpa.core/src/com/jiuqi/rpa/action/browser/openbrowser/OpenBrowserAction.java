package com.jiuqi.rpa.action.browser.openbrowser;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.browser.UIBrowser;
import com.jiuqi.rpa.lib.browser.WebBrowserLibary;

/**
 * 打开浏览器活动
 * 
 * @author wangshanyu
 */
public class OpenBrowserAction extends Action {
	private OpenBrowserInput input;
	private OpenBrowserOutput output;
	/**
	 * 活动输入
	 * 
	 * @param input
	 */
	public OpenBrowserAction(OpenBrowserInput input) {
		super(input);
		this.input = input;
		this.output = new OpenBrowserOutput();
	}

	@Override
	protected IActionOutput run() throws ActionException {
		Context context = getContext();
		try {
			UIBrowser browser = WebBrowserLibary.open(input.getBrowserType(),input.getUrl());
			context.add(browser);
			this.output.setBrowser(browser);
			return this.output;
		} catch (Exception e) {
			throw new ActionException("打开浏览器活动异常"+e.getMessage(), e);
		}
	}

}
