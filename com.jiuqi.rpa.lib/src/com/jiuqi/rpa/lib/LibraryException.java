package com.jiuqi.rpa.lib;

/**
 * ²Ù×÷¿âÒì³£
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class LibraryException extends Exception {
	private static final long serialVersionUID = -8656064080070875032L;

	public LibraryException(String message) {
		super(message);
	}
	
	public LibraryException(Throwable cause) {
		super(cause);
	}
	
	public LibraryException(String message, Throwable cause) {
		super(message, cause);
	}
}
