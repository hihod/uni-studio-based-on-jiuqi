package com.jiuqi.rpa.widgets.extract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuqi.rpa.lib.find.Path;
import com.jiuqi.rpa.lib.find.PathAttribute;
import com.jiuqi.rpa.lib.find.PathElement;

public class PathHelper {

	/**
	 * 将Path转换成xml文本
	 * @param path
	 * @return
	 */
	public static String toXMLformat(Path path) {
		
		String result = "";
		
		List<PathElement> pathElements = path.getElements();
		for(PathElement pathElement : pathElements) {
			result += "\t\t<element ";
			List<PathAttribute> pathAttributes = pathElement.getAttributes();
			Map<String, String> pathAttrMap = new HashMap<String, String>();
			for(PathAttribute pathAttribute : pathAttributes) {
				if(pathAttribute.isEnable())
				pathAttrMap.put(pathAttribute.getName(), pathAttribute.getValue());
			}
			result += xmlBuilder(pathAttrMap);
			result += "/>\n";
		}

		return result;
	}
	
	private static String xmlBuilder(Map<String, String> pathAttrMap) {
		String attrStr = " ";
		for (String key : pathAttrMap.keySet()) {
			String attrValue = encodehtml(pathAttrMap.get(key));
		    attrStr += key + "=\"" + attrValue+ "\" ";
		}
		return attrStr;
	}

	private static String encodehtml(String str) {
		if(str == null || str.isEmpty()) return "";
		return str.replace((CharSequence)"&", (CharSequence)"&amp;").replace((CharSequence)"<", (CharSequence)"&lt;").replace((CharSequence)">", (CharSequence)"&gt;").replace((CharSequence)"\"", (CharSequence)"&quot;").replace((CharSequence)"'", (CharSequence)"&apos;");
	}
}
