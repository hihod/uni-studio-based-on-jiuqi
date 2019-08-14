package com.jiuqi.rpa.lib.find;

import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Rect;

/**
 * UI元素
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public interface IUIElement extends IUIHandler {

	/**
	 * 获取元素外框
	 * 
	 * @return 返回UI元素外框
	 * @throws LibraryException
	 */
	Rect getRect() throws LibraryException;

	/**
	 * 获取元素路径
	 * 
	 * @return 返回元素路径
	 * @throws LibraryException
	 */
	Path getPath() throws LibraryException;

	/**
	 * 滚动到可见
	 * 
	 * @throws LibraryException
	 */
	void scrollIntoView() throws LibraryException;
	
	/**
	 * 设置焦点
	 * 
	 * @throws LibraryException
	 */
	void setFocus() throws LibraryException;

	/**
	 * 元素是够被勾选
	 * 
	 * @return 返回元素是否被勾选
	 * @throws LibraryException
	 */
	boolean isChecked() throws LibraryException;

	/**
	 * 设置元素勾选
	 * 
	 * @param checked 是否勾选
	 * @throws LibraryException
	 */
	void setChecked(boolean checked) throws LibraryException;

	/**
	 * 设置元素选中状态
	 * 
	 * @see CheckState
	 * @return 返回选中状态
	 * @throws LibraryException
	 */
	int getCheckState() throws LibraryException;

	/**
	 * 获取元素文本
	 * 
	 * @return 返回元素文本
	 * @throws LibraryException
	 */
	String getText() throws LibraryException;

	/**
	 * 设置元素文本
	 * 
	 * @param text 要设置到元素的文本
	 * @throws LibraryException
	 */
	void setText(String text) throws LibraryException;

	/**
	 * 元素是否为密码控件
	 * 
	 * @return 返回是否为密码控件
	 * @throws LibraryException
	 */
	boolean isPassword() throws LibraryException;

	/**
	 * 清空当前的选中状态
	 * 
	 * @throws LibraryException
	 */
	void clearSelection() throws LibraryException;

	/**
	 * 选中指定的条目
	 * 
	 * @param items 要选中的条目集合
	 * @throws LibraryException
	 */
	void selectItems(String[] items) throws LibraryException;

	/**
	 * 清除元素文本
	 * 
	 * @throws LibraryException
	 */
	void clearText() throws LibraryException;

	/**
	 * 模拟录入文本
	 * 
	 * @param text 要录入的文本
	 * @throws LibraryException
	 */
	void simulateTypeText(String text) throws LibraryException;

	/**
	 * 发送热键
	 * 
	 * @param hotkey 热键
	 * @param maskkeys 组合键
	 * @throws LibraryException
	 */
	void sendHotkey(int hotkey, int[] maskkeys) throws LibraryException;

	/**
	 * 获取属性值
	 * 
	 * @param attrName 属性名称
	 * @return 返回属性值
	 * @throws LibraryException
	 */
	String getAttributeValue(String attrName) throws LibraryException;
	
	/**
	 * 判断元素是否可用
	 * 
	 * @return 返回元素是否可用
	 * @throws LibraryException
	 */
	boolean enable() throws LibraryException;
	
	/**
	 * 判断元素是否可见
	 * 
	 * @return 返回元素是否可见
	 * @throws LibraryException
	 */
	boolean visible() throws LibraryException;
	
	/**
	 * 是否标准Table
	 * 
	 * @return 返回是否标准Table
	 * @throws LibraryException
	 */
	boolean isTable() throws LibraryException;
	
	/**
	 * 获取当页数据
	 * 
	 * @param columns 列元素路径集合
	 * @return 返回当页数据
	 * @throws LibraryException
	 */
	String getPageData(Path[] columns) throws LibraryException;
}
