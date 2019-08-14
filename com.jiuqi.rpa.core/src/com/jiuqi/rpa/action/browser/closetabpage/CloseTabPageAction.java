package com.jiuqi.rpa.action.browser.closetabpage;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.lib.browser.UIBrowser;

/**
 * 关闭浏览器页签活动
 * 
 * @author wangshanyu
 */
public class CloseTabPageAction extends Action {
	private CloseTabPageInput input;
	/**
	 * 活动输入
	 * 
	 * @param input
	 */
	public CloseTabPageAction(CloseTabPageInput input) {
		super(input);
		this.input = input;
	}

	@Override
	protected IActionOutput run() throws ActionException {
		try {
			UIBrowser browser =input.getBrowser();
			try {
				browser.close();
			} catch (Exception e) {
				throw new ActionException("浏览器页面关闭失败"+e.getMessage(), e);
			}
			return null;
		} catch (Exception e) {
			throw new ActionException("浏览器页面关闭活动异常"+e.getMessage(), e);
		}
	}

}
