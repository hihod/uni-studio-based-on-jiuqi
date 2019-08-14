package com.jiuqi.rpa.action.control.selectmultipleitems;

import com.jiuqi.rpa.action.Delay;
import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.IDelayable;
import com.jiuqi.rpa.action.Target;

/**
 * 单选条目
 * @author liangxiao01
 */
public class SelectMultipleItemsInput implements IActionInput,IDelayable {
	private Target target;
	private Delay delay;
	private String[] multipleItems;
	private Boolean clearOrigin;
	
	
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

	public Boolean getClearOrigin() {
		return clearOrigin;
	}

	public void setClearOrigin(Boolean clearOrigin) {
		this.clearOrigin = clearOrigin;
	}

	public String[] getMultipleItems() {
		return multipleItems;
	}

	public void setMultipleItems(String[] multipleItems) {
		this.multipleItems = multipleItems;
	}

}
