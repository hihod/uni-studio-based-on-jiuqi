package com.jiuqi.rpa.uia.test;

import java.util.Properties;

import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.tree.UIATreeWalker;
import com.jiuqi.rpa.uiadll.JQUIA;

public class TestUIATreeWalker {

	public static void main(String[] args) {
		JQUIA._initialize();
		try {
			Context context = new Context();
			try {
				UIATreeWalker treeWalker = new UIATreeWalker(context);
				
				//获取根节点
				IUIElement uiElement = treeWalker.getRoot();
				String text = treeWalker.getText(uiElement);
				System.out.println(text);
				
				//获取一级子节点
				IUIElement[] childElements = treeWalker.getChildren(uiElement);
				System.out.print("[");
				for (IUIElement childElement: childElements) {
					text = treeWalker.getText(childElement);
					System.out.print(text + ", ");
				}
				System.out.print("]\n");
				
				//获取父节点
				IUIElement firstChild = childElements[0];
				IUIElement parentElement = treeWalker.getParent(firstChild);
				text = treeWalker.getText(parentElement);
				System.out.println(text);
				
				//获取属性列表
				Properties properties = treeWalker.getProperties(uiElement);
				System.out.println(properties);
				
				//元素控件类型
				String ctrlType = firstChild.getAttributeValue("ControlType");
				System.out.println(ctrlType);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				context.close();
			}
		} finally {
			JQUIA._finalize();
		}
	}

}
