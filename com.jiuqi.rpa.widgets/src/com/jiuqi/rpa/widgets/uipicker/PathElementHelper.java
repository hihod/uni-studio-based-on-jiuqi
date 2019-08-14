package com.jiuqi.rpa.widgets.uipicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jiuqi.rpa.lib.find.PathAttribute;
import com.jiuqi.rpa.lib.find.PathElement;
/**
 * PathElement��������
 * @author liangxiao01
 * ʵ�����л��ͽڵ����
 */
public class PathElementHelper {
	private static Set<String> windowSet;
	
	private static String UIA_TabItemControlTypeId	= "50019";
	private static String UIA_TreeControlTypeId		= "50023";
	private static String UIA_WindowControlTypeId	= "50032";
	private static String UIA_PaneControlTypeId		= "50033";
	
	private synchronized static final Set<String> getWindowSet() {
		if (windowSet == null) {
			windowSet = new HashSet<>();
			windowSet.add(UIA_TabItemControlTypeId);
			windowSet.add(UIA_TreeControlTypeId);
			windowSet.add(UIA_WindowControlTypeId);
			windowSet.add(UIA_PaneControlTypeId);
		}
		
		return windowSet;
	}
	
	public static List<PathAttribute> getEditableAttribute(Boolean isWeb,PathElement element){
		List<PathAttribute> attrs = new ArrayList<PathAttribute>();
		for (PathAttribute pathAttribute : element.getAttributes()) {
			if(pathAttribute==null){
				continue;
			}
			if("ControlType".equals(pathAttribute.getName()) && !isWeb){
				//����Ӵ�����
			}else{
				attrs.add(pathAttribute);
			}
		}
		return attrs;
	}
	/**
	 * �� PathElement ת������XML�ַ���
	 * @param element
	 * @return
	 */
	public static String toXMLformat(Boolean isWeb,PathElement element){
		List<PathAttribute> attrs = element.getAttributes();
		Map<String, String> pathAttrMap = new HashMap<String, String>();
		String elementType = "";
		for (PathAttribute pathAttribute : attrs) {
			if(pathAttribute==null){
				continue;
			}
			if("ControlType".equals(pathAttribute.getName()) && !isWeb){
				elementType = getWindowSet().contains(pathAttribute.getValue()) ? "window" : "control";
			}else if("tag".equals(pathAttribute.getName()) && isWeb){
				elementType = "webcontrol";
				pathAttrMap.put(pathAttribute.getName(), pathAttribute.getValue());	
			}else{
				if(pathAttribute.isEnable() && (!"".equals(pathAttribute.getName()))){
					pathAttrMap.put(pathAttribute.getName(), pathAttribute.getValue());									
				}
			}
		}
		if("".equals(elementType)&& isWeb){
			elementType ="window";
		}
		return xmlBuilder(elementType,pathAttrMap);
	}
	/**
	 * 
	 * @param elementType �ڵ�����
	 * @param attrMap ���Լ�ֵ��
	 * @return
	 */
	private static String xmlBuilder(String elementType,Map<String, String> attrMap){
		String attrStr = " ";
		for (String key : attrMap.keySet()) {
		    attrStr += key + "=\"" + attrMap.get(key) + "\" ";
		}
		return "<"+elementType + attrStr + " />";
	}
	/**
	 * ��ȡ��ǰPathElement��ControlType PathAttribute
	 * @param pathElement
	 * @return
	 */
	public static PathAttribute getControlType(Boolean isWeb,PathElement pathElement) {
		for (PathAttribute pathAttribute : pathElement.getAttributes()) {
			if(pathAttribute==null){
				continue;
			}
			if("ControlType".equals(pathAttribute.getName())&& !isWeb){
				return pathAttribute;
			}else if("tag".equals(pathAttribute.getName())&& isWeb){
				return null;
			}
		}
		return null;
	}

}
