package com.jiuqi.rpa.action;

import com.jiuqi.rpa.lib.Context;

/**
 * �
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public abstract class Action {
	private IActionInput input;
	private Context context;
	
	/**
	 * ������
	 * 
	 * @param input �����
	 */
	public Action(IActionInput input) {
		this.input = input;
	}
	
	/**
	 * ��ȡ�������
	 * 
	 * @return ���ػ������
	 */
	protected Context getContext() {
		return context;
	}
	
	/**
	 * ִ��
	 * 
	 * @param context �������
	 * @return ���ػ���
	 */
	public final IActionOutput run(Context context) throws ActionException {
		this.context = context;
		
		//�ǰ��ʱ
		if (input instanceof IDelayable) {
			Delay delay = ((IDelayable) input).getDelay();
			try {
				Thread.sleep(delay.getBefore());
			} catch (InterruptedException e) {
				throw new ActionException("�ǰ��ʱ����", e);
			}
		}
		
		//�ִ��
		IActionOutput output = run();
		
		//�����ʱ
		if (input instanceof IDelayable) {
			Delay delay = ((IDelayable) input).getDelay();
			try {
				Thread.sleep(delay.getAfter());
			} catch (InterruptedException e) {
				throw new ActionException("�����ʱ����", e);
			}
		}
		
		//ִ�н��
		return output;
	}
	
	/**
	 * ִ��
	 * 
	 * @return ���ػ���
	 */
	protected abstract IActionOutput run() throws ActionException;
}
