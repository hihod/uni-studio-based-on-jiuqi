package com.jiuqi.etl.rpa.runtime;

/**
 * 延时信息
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class Delay {
	private String before = "200";
	private String after = "300";

	/**
	 * 获取活动前延时
	 * 
	 * @return 返回活动前延时，毫秒
	 */
	public String getBefore() {
		return before;
	}

	/**
	 * 设置活动前延时
	 * 
	 * @param before 活动前延时，毫秒
	 */
	public void setBefore(String before) {
		this.before = before;
	}

	/**
	 * 获取活动后延时
	 * 
	 * @return 返回活动后延时，毫秒
	 */
	public String getAfter() {
		return after;
	}

	/**
	 * 设置活动后延时
	 * 
	 * @param after 活动后延时，毫秒
	 */
	public void setAfter(String after) {
		this.after = after;
	}

}
