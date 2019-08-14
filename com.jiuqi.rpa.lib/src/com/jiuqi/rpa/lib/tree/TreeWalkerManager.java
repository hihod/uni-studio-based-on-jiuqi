package com.jiuqi.rpa.lib.tree;

import java.util.Properties;

import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.find.WEBElement;

/**
 * 元素树形管理器
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class TreeWalkerManager {
	private Context context;
	private UIATreeWalker uiaWalker;

	/**
	 * 构造器
	 * 
	 * @param context 活动上下文
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
	 * 获取子UI元素集合
	 * 
	 * @param parent 父UI元素
	 * @return 返回子UI元素集合 
	 * @throws LibraryException
	 */
	public IUIElement[] getChildren(IUIElement parent) throws LibraryException {	
		return getTreeWalker(parent).getChildren(parent);
	}
	
	
	/**
	 * 获取父UI元素
	 * 
	 * @param child 子UI元素
	 * @return 返回父UI元素
	 * @throws LibraryException
	 */
	public IUIElement getParent(IUIElement child) throws LibraryException {
		return getTreeWalker(child).getParent(child);
	}
	
	/**
	 * 获取指定UI元素的节点文本
	 * 
	 * @param uiElement UI元素
	 * @return 返回UI元素的节点文本
	 * @throws LibraryException
	 */
	public String getText(IUIElement uiElement) throws LibraryException {
		return getTreeWalker(uiElement).getText(uiElement);
	}
	
	/**
	 * 获取指定UI元素的属性列表
	 * 
	 * @param uiElement UI元素
	 * @return 返回UI元素的属性列表
	 * @throws LibraryException
	 */
	public Properties getProperties(IUIElement uiElement) throws LibraryException {
		return getTreeWalker(uiElement).getProperties(uiElement);
	}
	
}
