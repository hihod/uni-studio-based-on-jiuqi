package com.jiuqi.rpa.action.keyboard.typetext;

import com.jiuqi.rpa.action.Delay;
import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.IDelayable;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.keyboard.TypeMode;

/**
 * 活动输入：录入文本
 * 
 * @author liangxiao01
 */
public class TypetextInput implements IActionInput, IDelayable {
	private Target target;
	private TypeMode typeMode;
	private Delay delay;
	private Boolean attach;
	private String text;
	private Boolean clearBeforeType;
	private int delayBetweenKeys;

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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getClearBeforeType() {
		return clearBeforeType;
	}

	public void setClearBeforeType(Boolean clearBeforeType) {
		this.clearBeforeType = clearBeforeType;
	}

	public int getDelayBetweenKeys() {
		return delayBetweenKeys;
	}

	public void setDelayBetweenKeys(int delayBetweenKeys) {
		this.delayBetweenKeys = delayBetweenKeys;
	}

	public TypeMode getTypeMode() {
		return typeMode;
	}

	public void setTypeMode(TypeMode typeMode) {
		this.typeMode = typeMode;
	}

	public Boolean getAttach() {
		return attach;
	}

	public void setAttach(Boolean attach) {
		this.attach = attach;
	}

}
