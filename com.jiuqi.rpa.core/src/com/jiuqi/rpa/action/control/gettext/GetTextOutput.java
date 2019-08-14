package com.jiuqi.rpa.action.control.gettext;

import com.jiuqi.rpa.action.IActionOutput;

public class GetTextOutput implements IActionOutput {
	private String text;
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
