package com.jiuqi.rpa.action.control.setcheck;

import com.jiuqi.rpa.action.Delay;
import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.IDelayable;
import com.jiuqi.rpa.action.Target;

/**
 * 
 * @author liangxiao01
 */
public class SetCheckInput implements IActionInput, IDelayable {
	private Target target;
	private Delay delay;
	private Boolean check;
	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public Delay getDelay() {
		return delay;
	}

	public void setDelay(Delay delay) {
		this.delay = delay;
	}

	public Boolean getCheck() {
		return check;
	}

	public void setCheck(Boolean check) {
		this.check = check;
	}

}
