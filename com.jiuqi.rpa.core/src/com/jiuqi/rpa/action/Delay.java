package com.jiuqi.rpa.action;

/**
 * ��ʱ��Ϣ
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class Delay {
	private int before = 200;
	private int after = 300;

	/**
	 * ��ȡ�ǰ��ʱ
	 * 
	 * @return ���ػǰ��ʱ������
	 */
	public int getBefore() {
		return before;
	}

	/**
	 * ���ûǰ��ʱ
	 * 
	 * @param before �ǰ��ʱ������
	 */
	public void setBefore(int before) {
		this.before = before;
	}

	/**
	 * ��ȡ�����ʱ
	 * 
	 * @return ���ػ����ʱ������
	 */
	public int getAfter() {
		return after;
	}

	/**
	 * ���û����ʱ
	 * 
	 * @param after �����ʱ������
	 */
	public void setAfter(int after) {
		this.after = after;
	}

}
