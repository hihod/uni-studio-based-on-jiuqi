package com.jiuqi.rpa.lib.find;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class WebTableColumnsInfo {
	List<WebTableColumnInfo> cols = new ArrayList<WebTableColumnInfo>();

	public WebTableColumnsInfo() {
	}

	public WebTableColumnsInfo fromJSON(String jsonStr) {

		try {
			JSONArray ja = new JSONArray(jsonStr);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.optJSONObject(i);
				JSONArray nodes = jo.optJSONArray("rowNodes");
				// col Title
				System.out.print(jo.optString("colTitle") + "\t");

				WebTableColumnInfo wtci = new WebTableColumnInfo();
				wtci.setTitle(jo.optString("colTitle"));

				// col path
				Path colPath = new Path();
				if(nodes.length() == 1){
					Path firstPath = new Path();
					JSONArray elements = new JSONArray(nodes.optString(0));
					JSONObject pathobj = new JSONObject();
					pathobj.put("elements", elements);
					pathobj.put("isWeb", true);
					firstPath.fromJson(pathobj);
					Path root = WebTableUtil.getTableRootByOneTableItem(firstPath);
					colPath = WebTableUtil.calcColPathByTableRoot(root, firstPath);
					wtci.setPath(colPath);
				}else if(nodes.length() > 1){
					Path firstPath = new Path();
					JSONArray elements = new JSONArray(nodes.optString(0));
					JSONObject pathobj = new JSONObject();
					pathobj.put("elements", elements);
					pathobj.put("isWeb", true);
					firstPath.fromJson(pathobj);
					
					Path secondPath = new Path();
					JSONArray elements2 = new JSONArray(nodes.optString(1));
					JSONObject pathobj2 = new JSONObject();
					pathobj2.put("elements", elements2);
					pathobj2.put("isWeb", true);
					secondPath.fromJson(pathobj2);
					colPath = WebTableUtil.calcColPath(firstPath, secondPath);
					wtci.setPath(colPath);
				}
				this.cols.add(wtci);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public Path[] getColsPath() {
		List<Path> list = new ArrayList<Path>();
		for (int i = 0; i < cols.size(); i++) {
			list.add(cols.get(i).getPath());
		}
		return list.toArray(new Path[0]);
	}

	public String[] getColsTitle() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < cols.size(); i++) {
			list.add(cols.get(i).getTitle());
		}
		return list.toArray(new String[0]);
	}

}

class WebTableColumnInfo {
	private String title;
	private Path path ;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the path
	 */
	public Path getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(Path path) {
		this.path = path;
	}

}
