package com.jiuqi.rpa.action.dialog.input;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.dialog.UIADialogLibrary;

/**
 * ���¼���
 * 
 * @author liangxiao01
 */
public class DialogInputAction extends Action {
	private DialogInput input;

	/**
	 * ������
	 * 
	 * @param input �����
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
			throw new ActionException("��ȡ���Ի�쳣", e);
		}
		return output;
	}

}
