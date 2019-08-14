package com.jiuqi.rpa.action.application.open;

import com.jiuqi.rpa.action.IActionInput;

public class OpenApplicationInput implements IActionInput {

	private String path;
	private String args;
	private int timeout;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public String getArgs() {
		return args;
	}
	public void setArgs(String args) {
		this.args = args;
	}
	
}
