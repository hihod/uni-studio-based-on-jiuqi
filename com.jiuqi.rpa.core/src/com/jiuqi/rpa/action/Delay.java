package com.jiuqi.rpa.action;

/**
 * 延时信息
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class Delay {
	private int before = 200;
	private int after = 300;

	/**
	 * 获取活动前延时
	 * 
	 * @return 返回活动前延时，毫秒
	 */
	public int getBefore() {
		return before;
	}

	/**
	 * 设置活动前延时
	 * 
	 * @param before 活动前延时，毫秒
	 */
	public void setBefore(int before) {
		this.before = before;
	}

	/**
	 * 获取活动后延时
	 * 
	 * @return 返回活动后延时，毫秒
	 */
	public int getAfter() {
		return after;
	}

	/**
	 * 设置活动后延时
	 * 
	 * @param after 活动后延时，毫秒
	 */
	public void setAfter(int after) {
		this.after = after;
	}

}
