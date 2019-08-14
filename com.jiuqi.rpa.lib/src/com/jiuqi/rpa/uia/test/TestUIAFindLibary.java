package com.jiuqi.rpa.uia.test;

import org.json.JSONException;

import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.lib.Rect;
import com.jiuqi.rpa.lib.find.BrowserInfo;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.find.Path;
import com.jiuqi.rpa.lib.find.UIAFindLibrary;
import com.jiuqi.rpa.uiadll.JQUIA;

public class TestUIAFindLibary {

	public static void main(String[] args) {
		Context context = new Context();
		try {
			JQUIA._initialize();
			try {
				UIAFindLibrary findLibary = new UIAFindLibrary(context);
				
				Point point = new Point(200, 200);
				IUIElement uiElement = findLibary.get(point);
				String text = uiElement.getText();
				System.out.println("根据点获取元素：" + text);
				
				Rect rect = uiElement.getRect();
				System.out.println("获取元素Rect：" + rect);

				Path path = uiElement.getPath();
				System.out.println("获取元素Path：" + path.toJson());

				rect = findLibary.getRect(point);
				System.out.println("获取点对应元素Rect：" + rect);
				
				BrowserInfo browserInfo = findLibary.getBrowserInfo(point);
				System.out.println("获取点对应浏览器信息：" + browserInfo);
				
				rect = findLibary.getRect(browserInfo);
				System.out.println("获取浏览器信息对应客户区偏移：" + rect);
				
				boolean existed = findLibary.exists(path);
				System.out.println("判断元素是否存在：" + existed);
			} finally {
				JQUIA._finalize();
			}
		} catch (LibraryException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			context.close();
		}
	}

}
