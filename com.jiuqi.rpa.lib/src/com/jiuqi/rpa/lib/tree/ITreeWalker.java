package com.jiuqi.rpa.lib.tree;

import java.util.Properties;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;

/**
 * 树形操作库
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public interface ITreeWalker {

	/**
	 * 获取根UI元素
	 * 
	 * @return 返回根UI元素
	 * @throws LibraryException
	 */
	IUIElement getRoot() throws LibraryException;
	
	/**
	 * 获取子UI元素集合
	 * 
	 * @param parent 父UI元素
	 * @return 返回子UI元素集合 
	 * @throws LibraryException
	 */
	IUIElement[] getChildren(IUIElement parent) throws LibraryException;
	
	
	/**
	 * 获取父UI元素
	 * 
	 * @param child 子UI元素
	 * @return 返回父UI元素
	 * @throws LibraryException
	 */
	IUIElement getParent(IUIElement child) throws LibraryException;
	
	/**
	 * 获取指定UI元素的节点文本
	 * 
	 * @param uiElement UI元素
	 * @return 返回UI元素的节点文本
	 * @throws LibraryException
	 */
	String getText(IUIElement uiElement) throws LibraryException;
	
	/**
	 * 获取指定UI元素的属性列表
	 * 
	 * @param uiElement UI元素
	 * @return 返回UI元素的属性列表
	 * @throws LibraryException
	 */
	Properties getProperties(IUIElement uiElement) throws LibraryException;
}
