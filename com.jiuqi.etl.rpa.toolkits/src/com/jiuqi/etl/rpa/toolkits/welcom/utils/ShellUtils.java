package com.jiuqi.etl.rpa.toolkits.welcom.utils;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
* @author ���ߣ�houzhiyuan 2019��6��27�� ����1:58:27
*/
public class ShellUtils {
	/**
	 * ��shell������ʾ����Ļ�м�
	 * @param display
	 * @param shell
	 */
	public static void centerShell(Display display, Shell shell) {
		Rectangle displayBounds = display.getPrimaryMonitor().getBounds();
		Rectangle shellBounds = shell.getBounds();
		int x = displayBounds.x + (displayBounds.width - shellBounds.width) >> 1;
		int y = displayBounds.y + (displayBounds.height - shellBounds.height) >> 1;
		shell.setLocation(x, y);
	}

}
