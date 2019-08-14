package com.jiuqi.rpa.action.dialog.dictionary;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.dialog.UIADialogLibrary;

/**
 * 活动：消息框
 * 
 * @author liangxiao01
 */
public class SelectDirectoryAction extends Action {
	SelectDirectoryInput input;
	/**
	 * 构造器
	 * 
	 * @param input 活动输入
	 */
	public SelectDirectoryAction(SelectDirectoryInput input) {
		super(input);
		this.input = input;
	}

	@Override
	protected IActionOutput run() throws ActionException {
		UIADialogLibrary dialogLibrary = new UIADialogLibrary();
		SelectDirectoryOutput output = new SelectDirectoryOutput(); 
		try {
			output.setFilePath(dialogLibrary.directoryDialog(input.getInitPath()));
		} catch (LibraryException e) {
			throw new ActionException("获取属性活动异常", e);
		}
		return output;
	}

}
