package com.jiuqi.rpa.action.control.settext;

import com.jiuqi.rpa.action.Delay;
import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.IDelayable;
import com.jiuqi.rpa.action.Target;

/**
 * 
 * @author liangxiao01
 */
public class SetTextInput implements IActionInput, IDelayable {
	private Target target;
	private Delay delay;
	private String text;
	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public Delay getDelay() {
		return this.delay;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setDelay(Delay delay) {
		this.delay = delay;
	}


}
