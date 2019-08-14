package com.jiuqi.rpa.action.dialog.dictionary;

import com.jiuqi.rpa.action.IActionInput;

public class SelectDirectoryInput implements IActionInput {

	private String initPath;

	public String getInitPath() {
		return initPath;
	}

	public void setInitPath(String initPath) {
		this.initPath = initPath;
	}
}
