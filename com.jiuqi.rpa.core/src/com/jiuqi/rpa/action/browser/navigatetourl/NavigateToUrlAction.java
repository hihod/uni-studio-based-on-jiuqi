package com.jiuqi.rpa.action.browser.navigatetourl;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.lib.browser.UIBrowser;

/**
 * �������ת�
 * 
 * @author wangshanyu
 */
public class NavigateToUrlAction extends Action {
	private NavigateToUrlInput input;
	/**
	 * �����
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
				throw new ActionException("��ת��ָ��URL:��" + input.getUrl() + "��ʱ��������"+e.getMessage(), e);
			}
		} catch (Exception e) {
			throw new ActionException("���������쳣"+e.getMessage(), e);
		}
		return null;
	}

}
