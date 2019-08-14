package com.jiuqi.rpa.action.application.open;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.application.UIAApplicationLibary;
import com.jiuqi.rpa.lib.find.UIAElement;

/**
 * �����Ӧ��
 * 
 * @author liangxiao01
 */
public class OpenApplicationAction extends Action {
	private OpenApplicationInput input;

	/**
	 * ������
	 * 
	 * @param input �����
	 */
	public OpenApplicationAction(OpenApplicationInput input) {
		super(input);
		this.input = input;
	}

	@Override
	protected IActionOutput run() throws ActionException {
		final UIAApplicationLibary library = new UIAApplicationLibary(getContext());
		
		String applicationPath = input.getPath();
		String args = input.getArgs();
		int timeout = input.getTimeout();
		
		long pId = 0;
		try {
			pId = library.startProcess(applicationPath, args);
		} catch (LibraryException e) {
			throw new ActionException("����Ӧ���߳�ʧ��", e);
		}
		
		ExecutorService executorService= Executors.newSingleThreadExecutor();
		TimeOutCallable timeOutCallable = new TimeOutCallable(library, pId, timeout);
		Future<IUIHandler> future = executorService.submit(timeOutCallable);
		try {
			IUIHandler element = future.get(timeout, TimeUnit.MILLISECONDS);
			
			OpenApplicationOutput output = new OpenApplicationOutput();
			output.setElement(element);
			return output;
		} catch (TimeoutException e) {
			timeOutCallable.stopRunning();
			throw new ActionException("��ȡӦ�������ڳ�ʱ", e);
		} catch (Exception e) {
			timeOutCallable.stopRunning();
			throw new ActionException(e);
		}
	}

	class TimeOutCallable implements Callable<IUIHandler> {
		private static final int INTERVAL = 1000;	//���Ҽ��
		
		private UIAApplicationLibary library;
		private long pId;
		private int timeout;
		private boolean running = true;
		
		public TimeOutCallable(UIAApplicationLibary library, long pId, int timeout) {
			this.pId = pId;
			this.library = library;
			this.timeout = timeout;
		}

		@Override
		public IUIHandler call() throws Exception {
			int lTime = timeout;
			
			while (running && lTime > 0) {
				long id = library.getApplicationWindow(pId);
				if (id > 0)
					return new UIAElement(id);
				
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
