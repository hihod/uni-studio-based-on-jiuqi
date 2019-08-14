package com.jiuqi.rpa.action.find.waitforvanish;

import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.Target;

/**
 * 
 * @author liangxiao01
 */
public class WaitForVanishInput implements IActionInput {
	
	private Target target;

	private boolean waitNotActive;
	private boolean waitNotVisible;

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public boolean isWaitNotActive() {
		return waitNotActive;
	}

	public void setWaitNotActive(boolean waitNotActive) {
		this.waitNotActive = waitNotActive;
	}

	public boolean isWaitNotVisible() {
		return waitNotVisible;
	}

	public void setWaitNotVisible(boolean waitNotVisible) {
		this.waitNotVisible = waitNotVisible;
	}

}
