package com.jiuqi.rpa.action.dialog.file;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.dialog.UIADialogLibrary;

/**
 * �����Ϣ��
 * 
 * @author liangxiao01
 */
public class SelectFileAction extends Action {
	private SelectFileInput input;

	/**
	 * ������
	 * 
	 * @param input �����
	 */
	public SelectFileAction(SelectFileInput input) {
		super(input);
		this.input = input;
	}

	@Override
	protected IActionOutput run() throws ActionException {
		UIADialogLibrary dialogLibrary = new UIADialogLibrary();
		SelectFileOutput output = new SelectFileOutput(); 
		try {
			output.setFilePath(dialogLibrary.fileDialog(input.getInitPath(), input.getFilters()));
		} catch (LibraryException e) {
			throw new ActionException("��ȡ���Ի�쳣", e);
		}
		return output;
	}

}
