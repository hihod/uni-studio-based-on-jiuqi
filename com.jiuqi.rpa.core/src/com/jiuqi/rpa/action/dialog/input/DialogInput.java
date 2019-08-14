package com.jiuqi.rpa.action.dialog.input;

import com.jiuqi.rpa.action.IActionInput;

public class DialogInput implements IActionInput {

	private String inputTitle;
	private String inputLabel;
	private Boolean password;
	private String[] options;
	public String getInputTitle() {
		return inputTitle;
	}

	public void setInputTitle(String inputTitle) {
		this.inputTitle = inputTitle;
	}

	public String getInputLabel() {
		return inputLabel;
	}

	public void setInputLabel(String inputLabel) {
		this.inputLabel = inputLabel;
	}

	public Boolean isPassword() {
		return password;
	}

	public void setPassword(Boolean password) {
		this.password = password;
	}

	public String[] getOptions() {
		return options;
	}

	public void setOptions(String[] options) {
		this.options = options;
	}
	
}
