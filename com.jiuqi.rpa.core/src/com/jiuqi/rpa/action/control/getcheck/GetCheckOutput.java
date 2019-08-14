package com.jiuqi.rpa.action.control.getcheck;

import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.control.CheckState;

public class GetCheckOutput implements IActionOutput {

	private Boolean check;
	private CheckState checkState;
	
	public Boolean getCheck() {
		return check;
	}

	public void setCheck(Boolean check) {
		this.check = check;
	}

	public CheckState getCheckState() {
		return checkState;
	}

	public void setCheckState(CheckState checkState) {
		this.checkState = checkState;
	}
}
