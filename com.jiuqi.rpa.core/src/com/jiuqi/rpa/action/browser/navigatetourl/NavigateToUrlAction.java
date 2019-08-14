package com.jiuqi.rpa.action.browser.navigatetourl;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.lib.browser.UIBrowser;

/**
 * 浏览器跳转活动
 * 
 * @author wangshanyu
 */
public class NavigateToUrlAction extends Action {
	private NavigateToUrlInput input;
	/**
	 * 活动输入
	 * 
	 * @param input
	 */
	public NavigateToUrlAction(NavigateToUrlInput input) {
		super(input);
		this.input = input;
	}

	@Override
	protected IActionOutput run() throws ActionException {
		try {
			UIBrowser browser = input.getBrowser();
			try {
				if (input.getUrl() != null && !input.getUrl().isEmpty()) {
					browser.navigateTo(input.getUrl());
				}
			} catch (Exception e) {
				throw new ActionException("跳转到指定URL:【" + input.getUrl() + "】时发生错误！"+e.getMessage(), e);
			}
		} catch (Exception e) {
			throw new ActionException("打开浏览器活动异常"+e.getMessage(), e);
		}
		return null;
	}

}
