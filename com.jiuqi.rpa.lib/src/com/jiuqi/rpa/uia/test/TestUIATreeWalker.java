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
				
				//��ȡ���ڵ�
				IUIElement uiElement = treeWalker.getRoot();
				String text = treeWalker.getText(uiElement);
				System.out.println(text);
				
				//��ȡһ���ӽڵ�
				IUIElement[] childElements = treeWalker.getChildren(uiElement);
				System.out.print("[");
				for (IUIElement childElement: childElements) {
					text = treeWalker.getText(childElement);
					System.out.print(text + ", ");
				}
				System.out.print("]\n");
				
				//��ȡ���ڵ�
				IUIElement firstChild = childElements[0];
				IUIElement parentElement = treeWalker.getParent(firstChild);
				text = treeWalker.getText(parentElement);
				System.out.println(text);
				
				//��ȡ�����б�
				Properties properties = treeWalker.getProperties(uiElement);
				System.out.println(properties);
				
				//Ԫ�ؿؼ�����
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
