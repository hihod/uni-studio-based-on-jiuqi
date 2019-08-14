package com.jiuqi.rpa.action.control.selectItem;

import com.jiuqi.rpa.action.Delay;
import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.IDelayable;
import com.jiuqi.rpa.action.Target;

/**
 * 单选条目
 * @author liangxiao01
 */
public class SelectItemInput implements IActionInput,IDelayable {
	private Target target;
	private Delay delay;
	private String item;
	
	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public Delay getDelay() {
		return delay;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public void setDelay(Delay delay) {
		this.delay = delay;
	}


}
