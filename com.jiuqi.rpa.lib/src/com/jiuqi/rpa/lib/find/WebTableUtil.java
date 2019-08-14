package com.jiuqi.rpa.lib.find;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WebTableUtil {
	public static Path getTableRoot(Path p1, Path p2) {
		List<PathElement> p1list = p1.getElements();
		List<PathElement> p2list = p2.getElements();
		Path root = new Path();
		root.setWeb(true);
		for (int i = 0; i < p1list.size() && i < p2list.size(); i++) {
			if (comparePathElement(p1list.get(i), p2list.get(i))) {
				root.getElements().add(p1list.get(i));
			}else {
				break;
			}
		}
		return root;
	}

	public static Path calcColPath(Path p1, Path p2) throws Exception {
		List<PathElement> list1 = p1.getElements();
		List<PathElement> list2 = p2.getElements();
		if (list1.size() != list2.size()) {
			throw new Exception("两元素不属于同一列元素");
		}
		Path root = getTableRoot(p1, p2);
		int rootSize = root.getElements().size();
		Path col = new Path();
		col.setWeb(true);
		col.getElements().add(caclRowEle(list1.get(rootSize), list2.get(rootSize)));
		for (int i = root.getElements().size() + 1; i < p1.getElements().size(); i++) {
			PathElement tpe1 = list1.get(i);
			PathElement tpe2 = list2.get(i);
			PathElement t = getSamePart(tpe1, tpe2);
			t.setEnable(true);
			col.getElements().add(t);
		}
		return col;
	}

	private static PathElement caclRowEle(PathElement pe1, PathElement pe2) {
		PathElement result = getSamePart(pe1, pe2);
		result.setEnable(true);
		List<PathAttribute> attrlist = result.getAttributes();
		Iterator<PathAttribute> iterator = attrlist.iterator();
		while (iterator.hasNext()) {
			PathAttribute t = iterator.next();
			t.setEnable(true);
			String attrName = t.getName();
			if (attrName.equalsIgnoreCase("id") || attrName.equalsIgnoreCase("index"))
				iterator.remove();
		}
		PathAttribute indexAttr = new PathAttribute();
		indexAttr.setEnable(false);
		indexAttr.setName("index");
		indexAttr.setValue("0");
		attrlist.add(indexAttr);
		return result;
	}

	private static PathElement getSamePart(PathElement pe1, PathElement pe2) {
		Map<String, PathAttribute> pmap1 = elementToAttrMap(pe1);
		Map<String, PathAttribute> pmap2 = elementToAttrMap(pe2);
		PathElement pe = new PathElement();

		for (String key : pmap1.keySet()) {
			if (pmap2.containsKey(key)) {
				PathAttribute class1 = pmap1.get(key);
				PathAttribute class2 = pmap2.get(key);
				if (key.equalsIgnoreCase("class")) {
					PathAttribute tclass = new PathAttribute();
					List<String> pa1Value = Arrays.asList(class1.getValue().toLowerCase().split("\\s+"));
					List<String> pa2Value = Arrays.asList(class2.getValue().toLowerCase().split("\\s+"));
					tclass.setEnable(class1.isEnable() || class2.isEnable());
					tclass.setName("class");
					for (String i : pa1Value) {
						if (pa2Value.indexOf(i) != -1) {
							tclass.setValue((tclass.getValue()==null?"":(tclass.getValue()+ "* "))+ i);
						}
					}
					if(tclass.getValue() == null || tclass.getValue().isEmpty()) continue;
					pe.getAttributes().add(tclass);
					continue;
				} else if (class1.getValue().equalsIgnoreCase(class2.getValue())) {
					pe.getAttributes().add(class1);
				}
			}
		}
		return pe;
	}

	private static boolean comparePathElement(PathElement pe1, PathElement pe2) {
		List<PathAttribute> attributes = pe1.getAttributes();
		List<PathAttribute> attributes2 = pe2.getAttributes();
		for (int i = 0; i < attributes.size() && i < attributes2.size(); i++) {
			if (!compareAttribute(attributes.get(i), attributes2.get(i)))
				return false;
		}
		return true;
	}

	private static boolean compareAttribute(PathAttribute pa1, PathAttribute pa2) {
		if (!pa1.getName().equalsIgnoreCase(pa2.getName()))
			return false;
		if (pa1.getName().equals("class")) {
			List<String> pa1Value = Arrays.asList(pa1.getValue().toLowerCase().split("\\s+"));
			List<String> pa2Value = Arrays.asList(pa2.getValue().toLowerCase().split("\\s+"));
			for (String i : pa1Value) {
				if (pa2Value.indexOf(i) != -1)
					return true;
			}
		}
		if (pa1.getValue().equalsIgnoreCase(pa2.getValue()))
			return true;
		return false;
	}

	private static Map<String, PathAttribute> elementToAttrMap(PathElement pe) {
		List<PathAttribute> attributes = pe.getAttributes();
		Map<String, PathAttribute> attrMap = new HashMap<String, PathAttribute>();
		for (int i = 0; i < attributes.size(); i++) {
			PathAttribute pathAttribute = attributes.get(i);
			attrMap.put(pathAttribute.getName(), pathAttribute);
		}
		return attrMap;
	}

	
/**
 * <em>只用于表格只有一行的情况</em>
 * @param item
 * @return
 * @throws Exception
 */
	public static Path getTableRootByOneTableItem(Path item) throws Exception {
		Path root = new Path();
		root.setWeb(true);
		List<PathElement> list = root.getElements();
		boolean flag = false;
		for (PathElement ele : item.getElements()) {
			if (isDomTable(ele)) {
				list.add(ele);
				flag = true;
				break;
			}
			list.add(ele);
		}
		if (flag)
			return root;
		else
			throw new Exception("此元素父级不包含table标签");
	}
/**
 * <em>只用于表格只有一行的情况</em>
 * @param ele
 * @return
 */
	public static boolean isDomTable(PathElement ele) {
		List<PathAttribute> attributes = ele.getAttributes();
		for(PathAttribute attr : attributes) {
			if(attr.getName().equalsIgnoreCase("tag") && attr.getValue().equalsIgnoreCase("table"))
				return true;
		}
		return false;
	}
	/**
	 * <em>只用于表格只有一行的情况</em>
	 * @param root
	 * @param col
	 * @return
	 * @throws Exception
	 */
	public static Path calcColPathByTableRoot(Path root,Path col) throws Exception {
		Path colPath = new Path();
		colPath.setWeb(true);
		List<PathElement> colpathlist = col.getElements();
		List<PathElement> rootpathlist = root.getElements();

		if (rootpathlist.size() > colpathlist.size())
			throw new Exception("元素路径比table路径短，请检查提供的元素与表格");
		for (int i = 0; i < colpathlist.size(); i++) {
			PathElement crtColElement = colpathlist.get(i);
			if (i < rootpathlist.size()) {
				PathElement crtRootElement = colpathlist.get(i);
				if (crtRootElement != crtColElement)
					throw new Exception("###############元素不在表格内：###################\n"
							+ crtRootElement.toJson().toString() + "\n" + crtColElement.toJson().toString()
							+ "\n##################################################\\");
				continue;
			} 
			colPath.getElements().add(crtColElement);
		}
		return colPath;
	}
}
