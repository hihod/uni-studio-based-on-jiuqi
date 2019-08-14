package com.jiuqi.rpa.action.control.selectItem;

import java.io.UnsupportedEncodingException;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;

/**
 * �����ѡ��Ŀ
 * 
 * @author liangxiao01
 */
public class SelectItemAction extends Action {
	private SelectItemInput input;

	/**
	 * ������
	 * 
	 * @param input �����
	 */
	public SelectItemAction(SelectItemInput input) {
		super(input);
		this.input = input;
	}

	@Override
	protected IActionOutput run() throws ActionException {
		Target target = input.getTarget();
		IUIHandler uiHandler = (IUIElement) new TargetFinder().getUIHandler(getContext(), target);
		if (uiHandler == null)
			throw new ActionException("δ�ҵ�����Ŀ��");
		if (!(uiHandler instanceof IUIElement))
			throw new ActionException("����Ŀ�겻���ô˲���");
		IUIElement uiElement = (IUIElement) uiHandler;
		try {
			uiElement.scrollIntoView();
			try {
				uiElement.selectItems(new String[]{new String(input.getItem().getBytes(),"GBK")});
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} catch (LibraryException e) {
			throw new ActionException("��ѡ��Ŀ��쳣", e);
		}
		return null;
		
	}

}
