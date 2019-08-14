package com.jiuqi.rpa.action.browser.closetabpage;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.lib.browser.UIBrowser;

/**
 * �ر������ҳǩ�
 * 
 * @author wangshanyu
 */
public class CloseTabPageAction extends Action {
	private CloseTabPageInput input;
	/**
	 * �����
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
				throw new ActionException("�����ҳ��ر�ʧ��"+e.getMessage(), e);
			}
			return null;
		} catch (Exception e) {
			throw new ActionException("�����ҳ��رջ�쳣"+e.getMessage(), e);
		}
	}

}
