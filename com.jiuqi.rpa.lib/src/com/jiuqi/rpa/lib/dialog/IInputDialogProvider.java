package com.jiuqi.rpa.lib.dialog;

import com.jiuqi.rpa.lib.LibraryException;

public interface IInputDialogProvider {

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
	public String open(String title, String label, String[] items, boolean isPassword);
	
}
