package com.jiuqi.etl.rpa.toolkits.welcom.utils;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
* @author 作者：houzhiyuan 2019年6月27日 下午1:58:27
*/
public class ShellUtils {
	/**
	 * 将shell窗口显示在屏幕中间
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
