package com.jiuqi.rpa.action.control.hightlight;

import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.Target;

/**
 * 
 * @author liangxiao01
 */
public class HightlightInput implements IActionInput {
	private Target target;
	private int time;
	private String color;
	
	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
