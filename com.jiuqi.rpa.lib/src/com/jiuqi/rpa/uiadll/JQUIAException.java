package com.jiuqi.rpa.uiadll;

/**
 * JQUIA异常：用于包装从jquia.dll中抛出的异常
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class JQUIAException extends Exception {
	private static final long serialVersionUID = -5025813203240717646L;
	
	public JQUIAException() {
		super();
	}
	
	public JQUIAException(String message) {
		super(message);
	}
}
