package com.jiuqi.rpa.action.dialog.message;

import com.jiuqi.rpa.action.IActionInput;

public class DialogMessageInput implements IActionInput {

	private String inputTitle;
	private String inputMsg;
	private Long buttonSuite;
	public String getInputTitle() {
		return inputTitle;
	}

	public void setInputTitle(String inputTitle) {
		this.inputTitle = inputTitle;
	}


	public Long getButtonSuite() {
		return buttonSuite;
	}

	public void setButtonSuite(Long buttonSuite) {
		this.buttonSuite = buttonSuite;
	}

	public String getInputMsg() {
		return inputMsg;
	}

	public void setInputMsg(String inputMsg) {
		this.inputMsg = inputMsg;
	}
	
}
