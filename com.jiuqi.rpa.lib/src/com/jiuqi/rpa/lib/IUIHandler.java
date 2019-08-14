package com.jiuqi.rpa.lib;

/** 
 * UI句柄
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public interface IUIHandler {
	
	/**
	 * 获取句柄标识
	 * @return 返回句柄标识
	 */
	long getId();
	
	/**
	 * 释放句柄
	 */
	void release() throws LibraryException;
}