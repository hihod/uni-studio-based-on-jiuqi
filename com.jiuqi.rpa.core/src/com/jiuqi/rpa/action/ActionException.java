package com.jiuqi.rpa.action;

/**
 * ªÓ∂Øø‚“Ï≥£
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class ActionException extends Exception {
	private static final long serialVersionUID = 6410803868397174801L;

	public ActionException(String message) {
		super(message);
	}
	
	public ActionException(Throwable cause) {
		super(cause);
	}
	
	public ActionException(String message, Throwable cause) {
		super(message, cause);
	}
}
