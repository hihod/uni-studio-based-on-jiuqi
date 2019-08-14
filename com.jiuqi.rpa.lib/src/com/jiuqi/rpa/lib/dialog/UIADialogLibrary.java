package com.jiuqi.rpa.lib.dialog;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.uiadll.JQUIAException;

/**
 * UIA对话框操作库
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIADialogLibrary {
	private static IInputDialogProvider inputDialogProvider;
	
	public static void setInputDialogProvider(IInputDialogProvider inputDialogProvider) {
		UIADialogLibrary.inputDialogProvider = inputDialogProvider;
	}
	
	/**
	 * 打开录入对话框
	 * 
	 * @param title 标题
	 * @param label 标签
	 * @param items 选项集合
	 * @param isPassword 是否密码
	 * @return 返回录入的值
	 * @throws LibraryException
	 */
	public String inputDialog(String title, String label, String[] items, boolean isPassword) throws LibraryException {
		if (inputDialogProvider == null)
			throw new LibraryException("未注册输入对话框提供器");
		
		return inputDialogProvider.open(title, label, items, isPassword);
			
		/*
		try {
			return JQUIA.dialog_inputDialog(title, label, items, isPassword);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
		*/
	}
	
	/**
	 * 打开消息对话框
	 * 
	 * @param title 标题
	 * @param message 标签
	 * @param btnSuite 按钮组
	 * @return
	 * @throws LibraryException
	 */
	public String messageDialog(String title, String message, long btnSuite) throws LibraryException {
		try {
			return JQUIA.dialog_messageDialog(title, message, btnSuite);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	/**
	 * 打开文件对话框
	 * 
	 * @param initialDir 默认目录
	 * @param filters 文件过滤器列表，首个为默认过滤器
	 * @return 返回文件路径
	 * @throws LibraryException
	 */
	public String fileDialog(String initialDir, String[] filters) throws LibraryException {
		try {
			StringBuffer buffer = new StringBuffer();
			for (String filter: filters)
				buffer.append(filter);
			return JQUIA.dialog_fileDialog(initialDir, buffer.toString());
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	/**
	 * 目录对话框
	 * 
	 * @param initialDir 默认目录
	 * @return 返回目录路径
	 * @throws JQUIAException
	 */
	public String directoryDialog(String initialDir) throws LibraryException {
		try {
			return JQUIA.dialog_directoryDialog(initialDir);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
}
