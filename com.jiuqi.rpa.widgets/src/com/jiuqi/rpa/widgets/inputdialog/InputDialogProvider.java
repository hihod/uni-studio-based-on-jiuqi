package com.jiuqi.rpa.widgets.inputdialog;

import com.jiuqi.rpa.lib.dialog.IInputDialogProvider;

public class InputDialogProvider implements IInputDialogProvider {

	@Override
	public String open(String title, String label, String[] items, boolean isPassword) {
		return InputDialogHelper.open(title, label, isPassword, items);
	}

}
