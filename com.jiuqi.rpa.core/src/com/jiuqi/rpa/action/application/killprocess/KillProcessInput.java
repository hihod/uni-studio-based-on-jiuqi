package com.jiuqi.rpa.action.application.killprocess;

import com.jiuqi.rpa.action.IActionInput;

public class KillProcessInput implements IActionInput {
	private String processName;

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}


}
