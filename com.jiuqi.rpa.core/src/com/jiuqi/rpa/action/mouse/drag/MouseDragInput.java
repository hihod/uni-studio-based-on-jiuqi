package com.jiuqi.rpa.action.mouse.drag;

import com.jiuqi.rpa.action.CursorPosition;
import com.jiuqi.rpa.action.Delay;
import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.IDelayable;
import com.jiuqi.rpa.action.Target;

/**
 * 活动输入：鼠标Drag
 * 
 * @author liangxiao01
 */
public class MouseDragInput implements IActionInput, IDelayable {
	private Target startTarget;
	private Target endTarget;
	private Delay delay;
	private CursorPosition startCursorPosition;
	private CursorPosition endCursorPosition;
	private int[] maskKeys = {};

	public Delay getDelay() {
		return delay;
	}

	public void setDelay(Delay delay) {
		this.delay = delay;
	}

	public Target getStartTarget() {
		return startTarget;
	}

	public void setStartTarget(Target startTarget) {
		this.startTarget = startTarget;
	}

	public Target getEndTarget() {
		return endTarget;
	}

	public void setEndTarget(Target endTarget) {
		this.endTarget = endTarget;
	}

	public CursorPosition getStartCursorPosition() {
		return startCursorPosition;
	}

	public void setStartCursorPosition(CursorPosition startCursorPosition) {
		this.startCursorPosition = startCursorPosition;
	}

	public CursorPosition getEndCursorPosition() {
		return endCursorPosition;
	}

	public void setEndCursorPosition(CursorPosition endCursorPosition) {
		this.endCursorPosition = endCursorPosition;
	}

	public int[] getMaskKeys() {
		return maskKeys;
	}

	public void setMaskKeys(int[] maskKeys) {
		this.maskKeys = maskKeys;
	}

}
