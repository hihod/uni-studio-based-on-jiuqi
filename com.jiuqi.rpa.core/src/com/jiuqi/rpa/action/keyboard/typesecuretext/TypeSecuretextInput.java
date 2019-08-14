package com.jiuqi.rpa.action.keyboard.typesecuretext;

import com.jiuqi.rpa.action.Delay;
import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.IDelayable;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.keyboard.TypeMode;

/**
 * 活动输入：录入密文
 * 
 * @author liangxiao01
 */
public class TypeSecuretextInput implements IActionInput, IDelayable {
	private Target target;
	private Boolean attach;
	private TypeMode typeMode;
	private Delay delay;
	private String expression;
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

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
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
