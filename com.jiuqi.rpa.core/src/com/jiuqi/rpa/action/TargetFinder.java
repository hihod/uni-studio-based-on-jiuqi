package com.jiuqi.rpa.action;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.find.IFindLibary;
import com.jiuqi.rpa.lib.find.Path;
import com.jiuqi.rpa.lib.find.UIAFindLibrary;
import com.jiuqi.rpa.lib.find.WEBFindLibrary;

/**
 * 目标辅助器
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class TargetFinder {
	
	/**
	 * 根据标识获取句柄
	 * 
	 * @param context 活动上下文
	 * @param target 操作目标
	 * @return 返回指定的句柄
	 * @throws ActionException 
	 */
	public IUIHandler getUIHandler(Context context, Target target) throws ActionException {
		if (target.getElement() != null)
			return target.getElement();
		else if (target.getPath() != null)
			return getUIHandler(context, target.getPath(), target.getTimeout());
		
		throw new ActionException("未设置元素或选择器，无法查找元素。");
	}
	
	/**
	 * 根据路径获取句柄
	 * 
	 * @param context 活动上下文
	 * @param path 路径
	 * @param timeout 超时时间
	 * @return 返回指定的句柄
	 * @throws ActionException 
	 */
	public IUIHandler getUIHandler(Context context, Path path, int timeout) throws ActionException {
		if (path == null)
			throw new ActionException("未设置元素或选择器，无法查找元素。");
		
		TimeOutCallable timeOutCallable = new TimeOutCallable(context, path, timeout);
		try {
			ExecutorService executorService= Executors.newSingleThreadExecutor();
			Future<IUIHandler> future = executorService.submit(timeOutCallable);
			return future.get(timeout, TimeUnit.MILLISECONDS);
		} catch (TimeoutException e) {
			timeOutCallable.stopRunning();
			throw new ActionException("查找元素超时", e);
		} catch (Exception e) {
			timeOutCallable.stopRunning();
			throw new ActionException(e);
		}
	}

	class TimeOutCallable implements Callable<IUIHandler> {
		private static final int INTERVAL = 1000;	//查找间隔
		
		private IFindLibary findLibary;
		private Path path;
		private int timeout;
		private boolean running = true;
		
		public TimeOutCallable(Context context, Path path, int timeout) {
			this.findLibary = path.isWeb() ? new WEBFindLibrary(context) : new UIAFindLibrary(context);
			this.path = path;
			this.timeout = timeout;
		}

		public IUIHandler call() throws Exception {
			int lTime = timeout;
			
			//运行中或未超时，则再次尝试查找
			while (running && lTime >= 0) {
				try {
					IUIHandler uiHandler = findLibary.findFirst(path);
					if (uiHandler != null)
						return uiHandler;
				} catch (Exception e) {
				}
				
				Thread.sleep(INTERVAL);
				lTime -= INTERVAL;
			}
			
			return null;
		}
		
		public void stopRunning() {
			this.running = false;
		}
	}
	
}