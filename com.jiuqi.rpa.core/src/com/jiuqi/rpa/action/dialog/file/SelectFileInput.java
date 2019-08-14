package com.jiuqi.rpa.action.dialog.file;

import com.jiuqi.rpa.action.IActionInput;

public class SelectFileInput implements IActionInput {
	private String[] filters;
	private String initPath;

	public String[] getFilters() {
		return filters;
	}

	public void setFilters(String[] filters) {
		this.filters = filters;
	}

	public String getInitPath() {
		return initPath;
	}

	public void setInitPath(String initPath) {
		this.initPath = initPath;
	}

}
