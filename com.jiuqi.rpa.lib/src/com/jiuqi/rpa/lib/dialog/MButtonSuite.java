package com.jiuqi.rpa.lib.dialog;

/**
 * ��Ϣ�Ի���ť��ö��
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public enum MButtonSuite {
	OK(0x00000000L, "ȷ��"),
	OKCANCEL(0x00000001L, "ȷ��/ȡ��"),
	ABORTRETRYIGNORE(0x00000002L, "����/����/����"),
	YESNOCANCEL(0x00000003L, "��/��/ȡ��"),
	YESNO(0x00000004L, "��/��"),
	RETRYCANCEL(0x00000005L, "����/ȡ��");
	
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
