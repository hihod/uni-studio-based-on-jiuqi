package com.jiuqi.rpa.action.keyboard.sendhotkey;

import com.jiuqi.rpa.action.Delay;
import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.IDelayable;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.keyboard.HotKeyMode;

/**
 * 
 * @author liangxiao01
 */
public class SendHotKeyInput implements IActionInput, IDelayable {
	private Target target;
	private Delay delay;
	private Boolean attach;
	private HotKeyMode keyMode;
	private int key;
	private int[] maskKeys;
	private Boolean clearBeforeType;

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

	public Boolean getClearBeforeType() {
		return clearBeforeType;
	}

	public void setClearBeforeType(Boolean clearBeforeType) {
		this.clearBeforeType = clearBeforeType;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int[] getMaskKeys() {
		return maskKeys;
	}

	public void setMaskKeys(int[] maskKeys) {
		this.maskKeys = maskKeys;
	}

	public HotKeyMode getKeyMode() {
		return keyMode;
	}

	public void setKeyMode(HotKeyMode keyMode) {
		this.keyMode = keyMode;
	}

	public Boolean getAttach() {
		return attach;
	}

	public void setAttach(Boolean attach) {
		this.attach = attach;
	}

}
