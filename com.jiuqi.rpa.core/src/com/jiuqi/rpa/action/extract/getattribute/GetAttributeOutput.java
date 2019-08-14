package com.jiuqi.rpa.action.extract.getattribute;

import com.jiuqi.rpa.action.IActionOutput;
/**
 * 
 * @author liangxiao01
 */
public class GetAttributeOutput implements IActionOutput {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String element) {
		this.value = element;
	}
	

}
