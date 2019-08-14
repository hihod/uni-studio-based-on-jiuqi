package com.jiuqi.rpa.action.dialog.input;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.dialog.UIADialogLibrary;

/**
 * 活动：录入框
 * 
 * @author liangxiao01
 */
public class DialogInputAction extends Action {
	private DialogInput input;

	/**
	 * 构造器
	 * 
	 * @param input 活动输入
	 */
	public DialogInputAction(DialogInput input) {
		super(input);
		this.input = input;
	}

	@Override
	protected IActionOutput run() throws ActionException {
		UIADialogLibrary dialogLibrary = new UIADialogLibrary();
		DialogInputOutput output = new DialogInputOutput(); 
		try {
			output.setResult(dialogLibrary.inputDialog(input.getInputTitle(), input.getInputLabel(), input.getOptions(), input.isPassword()));
		} catch (LibraryException e) {
			throw new ActionException("获取属性活动异常", e);
		}
		return output;
	}

}
