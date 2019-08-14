package com.jiuqi.rpa.action.browser.openbrowser;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.browser.UIBrowser;
import com.jiuqi.rpa.lib.browser.WebBrowserLibary;

/**
 * ��������
 * 
 * @author wangshanyu
 */
public class OpenBrowserAction extends Action {
	private OpenBrowserInput input;
	private OpenBrowserOutput output;
	/**
	 * �����
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
			throw new ActionException("���������쳣"+e.getMessage(), e);
		}
	}

}
