package com.jiuqi.rpa.action.find.exists;

import com.jiuqi.rpa.action.IActionOutput;

public class ExistsOutput implements IActionOutput {
	private Boolean exist = false;

	public Boolean getExist() {
		return exist;
	}

	public void setExist(Boolean exist) {
		this.exist = exist;
	}
	
}
