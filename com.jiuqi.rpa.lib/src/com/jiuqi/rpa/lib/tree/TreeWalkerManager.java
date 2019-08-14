package com.jiuqi.rpa.lib.tree;

import java.util.Properties;

import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.find.WEBElement;

/**
 * Ԫ�����ι�����
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class TreeWalkerManager {
	private Context context;
	private UIATreeWalker uiaWalker;

	/**
	 * ������
	 * 
	 * @param context �������
	 */
	public TreeWalkerManager(Context context) {
		this.context = context;
		this.uiaWalker = new UIATreeWalker(context);
	}
	
	public IUIElement getUIARoot() throws LibraryException {
		return uiaWalker.getRoot();
	}

	private ITreeWalker getTreeWalker(IUIElement uiElement) {
		if (uiElement instanceof WEBElement) {
			WEBElement webElement = (WEBElement) uiElement;
			return new WEBTreeWalker(webElement.getBrowserId(), context);
		} else
			return uiaWalker;
	}
	
	/**
	 * ��ȡ��UIԪ�ؼ���
	 * 
	 * @param parent ��UIԪ��
	 * @return ������UIԪ�ؼ��� 
	 * @throws LibraryException
	 */
	public IUIElement[] getChildren(IUIElement parent) throws LibraryException {	
		return getTreeWalker(parent).getChildren(parent);
	}
	
	
	/**
	 * ��ȡ��UIԪ��
	 * 
	 * @param child ��UIԪ��
	 * @return ���ظ�UIԪ��
	 * @throws LibraryException
	 */
	public IUIElement getParent(IUIElement child) throws LibraryException {
		return getTreeWalker(child).getParent(child);
	}
	
	/**
	 * ��ȡָ��UIԪ�صĽڵ��ı�
	 * 
	 * @param uiElement UIԪ��
	 * @return ����UIԪ�صĽڵ��ı�
	 * @throws LibraryException
	 */
	public String getText(IUIElement uiElement) throws LibraryException {
		return getTreeWalker(uiElement).getText(uiElement);
	}
	
	/**
	 * ��ȡָ��UIԪ�ص������б�
	 * 
	 * @param uiElement UIԪ��
	 * @return ����UIԪ�ص������б�
	 * @throws LibraryException
	 */
	public Properties getProperties(IUIElement uiElement) throws LibraryException {
		return getTreeWalker(uiElement).getProperties(uiElement);
	}
	
}
