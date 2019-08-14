package com.jiuqi.rpa.action;

import com.jiuqi.rpa.lib.Context;

/**
 * 活动
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public abstract class Action {
	private IActionInput input;
	private Context context;
	
	/**
	 * 构造器
	 * 
	 * @param input 活动输入
	 */
	public Action(IActionInput input) {
		this.input = input;
	}
	
	/**
	 * 获取活动上下文
	 * 
	 * @return 返回活动上下文
	 */
	protected Context getContext() {
		return context;
	}
	
	/**
	 * 执行
	 * 
	 * @param context 活动上下文
	 * @return 返回活动输出
	 */
	public final IActionOutput run(Context context) throws ActionException {
		this.context = context;
		
		//活动前延时
		if (input instanceof IDelayable) {
			Delay delay = ((IDelayable) input).getDelay();
			try {
				Thread.sleep(delay.getBefore());
			} catch (InterruptedException e) {
				throw new ActionException("活动前延时错误", e);
			}
		}
		
		//活动执行
		IActionOutput output = run();
		
		//活动后延时
		if (input instanceof IDelayable) {
			Delay delay = ((IDelayable) input).getDelay();
			try {
				Thread.sleep(delay.getAfter());
			} catch (InterruptedException e) {
				throw new ActionException("活动后延时错误", e);
			}
		}
		
		//执行结果
		return output;
	}
	
	/**
	 * 执行
	 * 
	 * @return 返回活动输出
	 */
	protected abstract IActionOutput run() throws ActionException;
}
