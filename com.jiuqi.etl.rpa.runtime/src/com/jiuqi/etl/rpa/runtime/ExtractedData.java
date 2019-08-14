package com.jiuqi.etl.rpa.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.jiuqi.etl.DataType;
import com.jiuqi.etl.model.DataTableField;
import com.jiuqi.rpa.lib.find.Path;
import com.jiuqi.rpa.lib.find.PathAttribute;
import com.jiuqi.rpa.lib.find.PathElement;

public class ExtractedData {

	private static ArrayList<DataTableField> ColumnsList = new ArrayList<DataTableField>();
	private static Path rowPath = new Path();
	private static ArrayList<Path> ColumnsPath = new ArrayList<Path>();
	private static String text;
	public ExtractedData(){
		
	}
	public ExtractedData(String xmlStr) {
		
				try {
					calInfo(xmlStr);
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	}

	/**
	 * ���� column �ڵ�Ԫ��
	 * 
	 * @param xmlStr
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws UnsupportedEncodingException 
	 */
	public static ArrayList<Path> calColumnsPath(String xmlStr) throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException {
		ArrayList<Path> paths = new ArrayList<Path>();

//		try {
			// ��ȡ jaxp ����
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			// ��ȡ������
			DocumentBuilder builder = factory.newDocumentBuilder();
			// �ý��������� xml �ĵ� ---> Document
			Document document = builder.parse(new ByteArrayInputStream(xmlStr.getBytes("UTF-8")));
			// ��ȡ���� column �ڵ�
			NodeList columnList = document.getElementsByTagName("column");
			for (int i = 0; i < columnList.getLength(); i++) {
				Node columnNode = columnList.item(i);
				// ��õ�ǰ column ������ element ����
				NodeList rowElementList = columnNode.getChildNodes();
				// ���� row ���ϣ���װ PathElement �� List
				List<PathElement> pathElements = new ArrayList<PathElement>();
				for (int j = 0; j < rowElementList.getLength(); j++) {
					Node elementNode = rowElementList.item(j);
					NamedNodeMap namedNodeMap = elementNode.getAttributes();
					// �������Լ�ֵ�ԣ���װ PathAttribute �� List
					List<PathAttribute> pathAttributes = new ArrayList<PathAttribute>();
					for (int k = 0; k < namedNodeMap.getLength(); k++) {
						Node attributeNode = namedNodeMap.item(k);
						// ��װһ�� PathAttribute
						PathAttribute attribute = new PathAttribute();
						attribute.setEnable(true);
						attribute.setName(attributeNode.getNodeName());
						attribute.setValue(attributeNode.getNodeValue());
						// List++
						pathAttributes.add(attribute);
					}
					// ��װһ�� PathElement
					PathElement element = new PathElement();
					element.setEnable(true);
					// element.setAttributes(pathAttributes);
					for (PathAttribute pathAttr : pathAttributes) {
						element.getAttributes().add(pathAttr);
					}
					// List++
					pathElements.add(element);
				}
				Path path = new Path();
				path.setWeb(true);
				// path.setElements(pathElements);
				for (PathElement pathEle : pathElements) {
					path.getElements().add(pathEle);
				}
				paths.add(path);
			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return paths;
	}

	/**
	 * ���� row �ڵ�Ԫ��
	 * 
	 * @param xmlStr
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws UnsupportedEncodingException 
	 */
	public static Path calRowPath(String xmlStr) throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException {
		Path path = new Path();

//		try {
			// ��ȡ jaxp ����
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			// ��ȡ������
			DocumentBuilder builder = factory.newDocumentBuilder();
			// �ý��������� xml �ĵ� ---> Document
			Document document = builder.parse(new ByteArrayInputStream(xmlStr.getBytes("UTF-8")));
			// ��� row ������ element ����
			NodeList rowElementList = document.getElementsByTagName("row").item(0).getChildNodes();
			// ���� row ���ϣ���װ PathElement �� List
			List<PathElement> pathElements = new ArrayList<PathElement>();
			for (int i = 0; i < rowElementList.getLength(); i++) {
				Node elementNode = rowElementList.item(i);
				NamedNodeMap namedNodeMap = elementNode.getAttributes();
				// �������Լ�ֵ�ԣ���װ PathAttribute �� List
				List<PathAttribute> pathAttributes = new ArrayList<PathAttribute>();
				for (int j = 0; j < namedNodeMap.getLength(); j++) {
					Node attributeNode = namedNodeMap.item(j);
					// ��װһ�� PathAttribute
					PathAttribute attribute = new PathAttribute();
					attribute.setEnable(true);
					attribute.setName(attributeNode.getNodeName());
					attribute.setValue(attributeNode.getNodeValue());
					// List++
					pathAttributes.add(attribute);
				}
				// ��װһ�� PathElement
				PathElement element = new PathElement();
				element.setEnable(true);
				for (PathAttribute pathAttr : pathAttributes) {
					element.getAttributes().add(pathAttr);
				}

				// List++
				pathElements.add(element);
			}
			path.setWeb(true);

			for (PathElement pathEle : pathElements) {
				path.getElements().add(pathEle);
			}

//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return path;
	}

	public static ArrayList<DataTableField> calColumnsList(String xmlStr) throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException {
		ArrayList<DataTableField> fieldList = new ArrayList<DataTableField>();

//		try {
			// ��ȡ jaxp ����
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			// ��ȡ������
			DocumentBuilder builder = factory.newDocumentBuilder();
			// �ý��������� xml �ĵ� ---> Document
			Document document = builder.parse(new ByteArrayInputStream(xmlStr.getBytes("UTF-8")));
			// ������� column ����
			NodeList columnList = document.getElementsByTagName("column");
			// ���� column ����
			for (int i = 0; i < columnList.getLength(); i++) {
				// ͨ���±��ø��� column
				Element columnElement = (Element) columnList.item(i);
				// ��ȡ��װ��ÿ�� column �ĸ�������
				DataTableField datatablefield = new DataTableField();
				datatablefield.setName(columnElement.getAttribute("name"));
				datatablefield.setTitle(columnElement.getAttribute("title"));
				datatablefield.setType(DataType.STRING);
				// List++
				fieldList.add(datatablefield);
			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return fieldList;
	}

	public ArrayList<DataTableField> getColumnsList() {
		return ColumnsList;
	}

	public Path getRowPath() {
		return rowPath;
	}

	public ArrayList<Path> getColumnsPath() {
		return ColumnsPath;
	}

	public String getText() {
		return text;
	}

	/**
	 * ����ȫ��Ϣ����xml�ı�
	 * 
	 * @param ColumnsList
	 * @param rowPath
	 * @param ColumnsPath
	 * @return xmlText
	 */
	public static String getXMLText(ArrayList<DataTableField> ColumnsList, Path rowPath, ArrayList<Path> ColumnsPath) {
		String xmlText = "";
		xmlText += "<extract>\n\n";
		xmlText += "\t<row>\n";
		xmlText += toXMLformat(rowPath);
		xmlText += "\t</row>\n\n";

		for(int i = 0; i < ColumnsPath.size();i++) {
			Path path = ColumnsPath.get(i);
			xmlText += "\t<column name=\"" + ColumnsList.get(i).getName() + "\" title=\""
					+ ColumnsList.get(i).getTitle() + "\"" + ">\n";
			if(path != null)
			xmlText += toXMLformat(path);
			xmlText += "\t</column>\n\n";
		}

		xmlText += "</extract>";
		return xmlText;
	}

	public static String toXMLformat(Path path) {

		String result = "";

		List<PathElement> pathElements = path.getElements();
		for (PathElement pathElement : pathElements) {
			result += "\t\t<element ";
			List<PathAttribute> pathAttributes = pathElement.getAttributes();
			Map<String, String> pathAttrMap = new HashMap<String, String>();
			for (PathAttribute pathAttribute : pathAttributes) {
				if (pathAttribute.isEnable())
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
			attrStr += key + "=\""
					+ pathAttrMap.get(key).replace((CharSequence) "&", (CharSequence) "&amp;")
							.replace((CharSequence) "<", (CharSequence) "&lt;")
							.replace((CharSequence) ">", (CharSequence) "&gt;")
							.replace((CharSequence) "\"", (CharSequence) "&quot;")
							.replace((CharSequence) "'", (CharSequence) "&apos;")
					+ "\" ";
		}
		return attrStr;
	}
	
	public static void calInfo(String xmlStr) throws UnsupportedEncodingException, ParserConfigurationException, SAXException, IOException {
		text = xmlStr;

		// ȥ���Ʊ��������
		Pattern p = Pattern.compile("\t|\r|\n");
		Matcher m = p.matcher(xmlStr);
		xmlStr = m.replaceAll("");

		// �ֱ��������
		ColumnsList = calColumnsList(xmlStr);
		rowPath = calRowPath(xmlStr);
		ColumnsPath = calColumnsPath(xmlStr);
	}
	
	/**
	 * ����ȫ��Ϣ����JSONObject
	 * @param allColumns
	 * @param rowPath
	 * @param allColumnPathArray
	 * @return JSONObject
	 */
	public static JSONObject getJSONObject(ArrayList<DataTableField> allColumns, Path rowPath, List<Path> allColumnPathArray)
	{
	
		JSONObject jsonObjectExtract=new JSONObject();
		if(allColumnPathArray.size()==0)
		{
			return jsonObjectExtract;
		}
		try {
			jsonObjectExtract.put("extract", new JSONObject());
			//rowPath
			((JSONObject) jsonObjectExtract.get("extract")).put("row", rowPath.toJson());
			
			//columns
			((JSONObject) jsonObjectExtract.get("extract")).put("column", new JSONArray());
		    for(	DataTableField column:allColumns)
		    {
		    	Path colPath=allColumnPathArray.get(allColumns.indexOf(column));
		    	((JSONArray)((JSONObject) jsonObjectExtract.get("extract")).get("column")).put(colPath.toJson().put("name", column.getName()).put("title", column.getTitle()));
		    }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(jsonObjectExtract.toString());
		return jsonObjectExtract;
	}
	
	/**
	 * ��JSONObject�ָ���ȫ��Ϣ
	 * @param jsonObject
	 */
	public void calInfo(JSONObject jsonObject)
	{
		//rowPath
		rowPath=new Path();
		try {
			rowPath.fromJson(jsonObject.getJSONObject("extract").getJSONObject("row"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		//ColumnsPath
		ColumnsPath.clear();
		JSONArray ja=new JSONArray();
		try {
			ja = jsonObject.getJSONObject("extract").optJSONArray("column");
			for(int i=0;i<ja.length();i++)
			{
				Path column=new Path();
				column.fromJson(ja.getJSONObject(i));
				ColumnsPath.add(column);
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		//ColumnsList
		ColumnsList.clear();
		for(int i=0;i<ja.length();i++)
		{
			DataTableField dtField=new DataTableField();
			try {
				dtField.setName(ja.getJSONObject(i).getString("name"));
				dtField.setTitle(ja.getJSONObject(i).getString("title"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			dtField.setType(DataType.STRING);
			ColumnsList.add(dtField);
		}
		
		//xmlText
		text=getXMLText(ColumnsList, rowPath, ColumnsPath);
	}
	
}
