package com.jiuqi.rpa.lib;

import java.util.Map;

import java.util.HashMap;

/**
 * 活动上下文
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class Context {
	private Map<Long, IUIHandler> map = new HashMap<Long, IUIHandler>();
	private IInterruptable interruptable;

	/**
	 * 构造器
	 */
	public Context() {
	}
	
	/**
	 * 构造器
	 * 
	 * @param interruptable 可中断对象
	 */
	public Context(IInterruptable interruptable) {
		this.interruptable = interruptable;
	}
	
	/**
	 * 将UI句柄添加到上下文
	 * 
	 * @param uiHandler
	 * @return 返回添加的UI句柄对象
	 */
	public IUIHandler add(IUIHandler uiHandler) {
		map.put(uiHandler.getId(), uiHandler);
		return uiHandler;
	}
	
	/**
	 * 获取指定id的UI句柄
	 * 
	 * @param id UI句柄标识  
	 * @return 返回UI句柄
	 */
	public IUIHandler get(long id) {
		return map.get(id);
	}
	
	/**
	 * 释放指定标识的UI句柄
	 * 
	 * @param id UI句柄标识
	 */
	public void release(long id) {
		IUIHandler uiHandler = get(id);
		if (uiHandler != null) {
			try {
				uiHandler.release();
			} catch (LibraryException e) {
				e.printStackTrace();
			}
			
			map.remove(id);
		}
	}
	
	/**
	 * 释放指定标识的UI句柄集合
	 * 
	 * @param ids UI句柄标识集合
	 */
	public void release(long[] ids) {
		//统一释放ID列表
		for (long id: ids) {
			IUIHandler uiHandler = get(id);
			if (uiHandler == null)
				continue;
			
			try {
				uiHandler.release();
			} catch (LibraryException e) {
			}
		}

		//移除ID列表
		for (long id: ids)
			map.remove(id);
	}
	
	/**
	 * 关闭上下文
	 */
	public void close() {
		int i = 0;
		long[] ids = new long[map.keySet().size()];
		for (Long id: map.keySet())
			ids[i++] = id;
		
		release(ids);
	}
	
	public boolean isInterrupted() {
		if (interruptable != null)
			return interruptable.isInterrupted();
		
		return false;
	}
	
}
