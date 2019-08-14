package com.jiuqi.rpa.action.mouse.click;

import com.jiuqi.rpa.action.CursorPosition;
import com.jiuqi.rpa.action.Delay;
import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.IDelayable;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.VirtualKey;
import com.jiuqi.rpa.action.mouse.ClickMode;
import com.jiuqi.rpa.lib.mouse.MouseClickType;

/**
 * 活动输入：鼠标单击
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class MouseClickInput implements IActionInput, IDelayable {
	private Target target;
	private Delay delay;
	private CursorPosition cursorPosition;
	private MouseClickType clickType;
	private int mouseKey = VirtualKey.VK_LBUTTON;
	private ClickMode clickMode = ClickMode.NORMAL_CLICK;
	private int[] maskKeys = {};

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

	public CursorPosition getCursorPosition() {
		return cursorPosition;
	}

	public void setCursorPosition(CursorPosition cursorPosition) {
		this.cursorPosition = cursorPosition;
	}

	public MouseClickType getClickType() {
		return clickType;
	}

	public void setClickType(MouseClickType clickType) {
		this.clickType = clickType;
	}

	public int getMouseKey() {
		return mouseKey;
	}

	public void setMouseKey(int mouseKey) {
		this.mouseKey = mouseKey;
	}

	public int[] getMaskKeys() {
		return maskKeys;
	}

	public void setMaskKeys(int[] maskKeys) {
		this.maskKeys = maskKeys;
	}

	public ClickMode getClickMode() {
		return clickMode;
	}

	public void setClickMode(ClickMode clickMode) {
		this.clickMode = clickMode;
	}

}
