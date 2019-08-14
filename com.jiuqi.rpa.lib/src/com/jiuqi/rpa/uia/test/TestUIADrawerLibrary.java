package com.jiuqi.rpa.uia.test;

import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.lib.drawer.UIADrawerLibrary;
import com.jiuqi.rpa.lib.find.UIAFindLibrary;
import com.jiuqi.rpa.lib.find.UIARect;
import com.jiuqi.rpa.uiadll.JQUIA;

public class TestUIADrawerLibrary {

	public static void main(String[] args) {
		JQUIA._initialize();
		try {
			Context context = new Context();
			try {
				UIAFindLibrary findLibary = new UIAFindLibrary(context);
				UIADrawerLibrary drawerLibrary = new UIADrawerLibrary();
				
				Point point = new Point(500, 500);
				UIARect rect = findLibary.getRect(point);
				System.out.println("获取点对应元素Rect：" + rect);
				
				drawerLibrary.startDraw(rect, "#ff0000");
			} catch (LibraryException e) {
				e.printStackTrace();
			} finally {
				context.close();
			}
		} finally {
//			JQUIA.getInstance().finalize();
		}
	}

}
