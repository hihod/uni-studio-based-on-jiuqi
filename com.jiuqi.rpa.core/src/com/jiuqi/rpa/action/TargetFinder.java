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
 * Ŀ�긨����
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class TargetFinder {
	
	/**
	 * ���ݱ�ʶ��ȡ���
	 * 
	 * @param context �������
	 * @param target ����Ŀ��
	 * @return ����ָ���ľ��
	 * @throws ActionException 
	 */
	public IUIHandler getUIHandler(Context context, Target target) throws ActionException {
		if (target.getElement() != null)
			return target.getElement();
		else if (target.getPath() != null)
			return getUIHandler(context, target.getPath(), target.getTimeout());
		
		throw new ActionException("δ����Ԫ�ػ�ѡ�������޷�����Ԫ�ء�");
	}
	
	/**
	 * ����·����ȡ���
	 * 
	 * @param context �������
	 * @param path ·��
	 * @param timeout ��ʱʱ��
	 * @return ����ָ���ľ��
	 * @throws ActionException 
	 */
	public IUIHandler getUIHandler(Context context, Path path, int timeout) throws ActionException {
		if (path == null)
			throw new ActionException("δ����Ԫ�ػ�ѡ�������޷�����Ԫ�ء�");
		
		TimeOutCallable timeOutCallable = new TimeOutCallable(context, path, timeout);
		try {
			ExecutorService executorService= Executors.newSingleThreadExecutor();
			Future<IUIHandler> future = executorService.submit(timeOutCallable);
			return future.get(timeout, TimeUnit.MILLISECONDS);
		} catch (TimeoutException e) {
			timeOutCallable.stopRunning();
			throw new ActionException("����Ԫ�س�ʱ", e);
		} catch (Exception e) {
			timeOutCallable.stopRunning();
			throw new ActionException(e);
		}
	}

	class TimeOutCallable implements Callable<IUIHandler> {
		private static final int INTERVAL = 1000;	//���Ҽ��
		
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
			
			//�����л�δ��ʱ�����ٴγ��Բ���
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