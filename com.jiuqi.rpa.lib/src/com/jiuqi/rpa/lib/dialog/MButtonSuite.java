package com.jiuqi.rpa.lib.dialog;

/**
 * 消息对话框按钮组枚举
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public enum MButtonSuite {
	OK(0x00000000L, "确定"),
	OKCANCEL(0x00000001L, "确定/取消"),
	ABORTRETRYIGNORE(0x00000002L, "放弃/重试/跳过"),
	YESNOCANCEL(0x00000003L, "是/否/取消"),
	YESNO(0x00000004L, "是/否"),
	RETRYCANCEL(0x00000005L, "重试/取消");
	
	private long value;
	private String title;
	
	private MButtonSuite(long value, String title) {
		this.value = value;
		this.title = title;
	}
	
	public long value() {
		return value;
	}
	
	public String title() {
		return title;
	}
}
