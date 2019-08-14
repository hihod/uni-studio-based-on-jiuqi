package com.jiuqi.etl.rpa.toolkits.welcom.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

/**
* @author 作者：houzhiyuan 2019年6月27日 下午2:55:33
*/
public class MessageBoxUtils {
	public static void showTips(String text) {
		MessageBox msg = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK|SWT.ICON_INFORMATION);
		msg.setText("提示");
		msg.setMessage(text);
		msg.open();
	}
	
	public static void showWarnning(String text) {
		MessageBox msg = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK|SWT.ICON_WARNING);
		msg.setText("警告");
		msg.setMessage(text);
		msg.open();
	}
	
	public static void showError(String text) {
		MessageBox msg = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK|SWT.ICON_WARNING);
		msg.setText("错误");
		msg.setMessage(text);
		msg.open();
	}
	
	public static int showOKAndCancel(String text) {
		MessageBox msg = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK|SWT.CANCEL);
		msg.setText("提示");
		msg.setMessage(text);
		return msg.open();
	}
	
	public static int showYesAndNo(String text) {
		MessageBox msg = new MessageBox(Display.getDefault().getActiveShell(), SWT.YES|SWT.NO);
		msg.setText("提示");
		msg.setMessage(text);
		return msg.open();
	}
}
