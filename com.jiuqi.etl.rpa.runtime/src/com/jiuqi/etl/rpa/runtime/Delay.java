package com.jiuqi.etl.rpa.runtime;

/**
 * ��ʱ��Ϣ
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class Delay {
	private String before = "200";
	private String after = "300";

	/**
	 * ��ȡ�ǰ��ʱ
	 * 
	 * @return ���ػǰ��ʱ������
	 */
	public String getBefore() {
		return before;
	}

	/**
	 * ���ûǰ��ʱ
	 * 
	 * @param before �ǰ��ʱ������
	 */
	public void setBefore(String before) {
		this.before = before;
	}

	/**
	 * ��ȡ�����ʱ
	 * 
	 * @return ���ػ����ʱ������
	 */
	public String getAfter() {
		return after;
	}

	/**
	 * ���û����ʱ
	 * 
	 * @param after �����ʱ������
	 */
	public void setAfter(String after) {
		this.after = after;
	}

}
