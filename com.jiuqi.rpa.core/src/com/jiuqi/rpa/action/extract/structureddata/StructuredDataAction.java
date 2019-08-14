package com.jiuqi.rpa.action.extract.structureddata;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.jiuqi.bi.dataset.Column;
import com.jiuqi.bi.dataset.DataRow;
import com.jiuqi.bi.dataset.MemoryDataSet;
import com.jiuqi.bi.dataset.Metadata;
import com.jiuqi.etl.model.DataTableField;
import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.action.VirtualKey;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.FindLibraryManager;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.find.Path;
import com.jiuqi.rpa.lib.find.UIAElement;
import com.jiuqi.rpa.lib.find.WEBElement;
import com.jiuqi.rpa.lib.mouse.MouseClickType;

/**
 * 活动：结构化数据
 * 
 * @author liangxiao01
 */
public class StructuredDataAction extends Action {
	private StructuredDataInput input;

	/**
	 * 构造器
	 * 
	 * @param input 活动输入
	 */
	public StructuredDataAction(StructuredDataInput input) {
		super(input);
		this.input = input;
		
	}

	@Override
	protected IActionOutput run() throws ActionException {
		StructuredDataOutput output = new StructuredDataOutput();
		boolean hasNextLink = false;
		Target nextLinkTarget = input.getNextLinkSelector();
		FindLibraryManager findManager = new FindLibraryManager(getContext());
	
		MemoryDataSet<DataTableField> dataset = new MemoryDataSet<DataTableField>(DataTableField.class);
		Metadata<DataTableField> metadata = dataset.getMetadata();
		for (DataTableField f : input.getColumnsList()) {
			Column<DataTableField> column = new Column<DataTableField>(f.getName(), f.getType().value());
			column.setTitle(f.getTitle());
			column.setInfo(f);
			metadata.addColumn(column);
		}
		//执行循环抓取分页数据
		do {
			try{
				//获取当前分页Path
				IUIElement tableElement = findManager.findFirst(input.getRowPath());
				//获取所有列Path
				ArrayList<Path> columnsPath = input.getColumnsPath();
				boolean isFullTable = false;
				if(columnsPath != null && !columnsPath.isEmpty()) {
					Path path = columnsPath.get(0);
					if(path==null ||path.getElements() == null ||path.getElements().size()<1) {
						isFullTable = true;
					}
				}
				String tableDataString;
				if(isFullTable) {
					tableDataString = ((WEBElement)tableElement).getPageDataByTable();
				}else {
					Path[] paths = columnsPath.toArray(new Path[columnsPath.size()]);
					tableDataString=tableElement.getPageData(paths);
				}

				JSONArray array2D = new JSONArray(tableDataString);
				int columnCount = array2D.length();
				int rowCount = array2D.getJSONArray(0).length();
				for (int i = 0; i < rowCount; i++) {
					DataRow dataRow = dataset.add();
					for (int j = 0; j < columnCount; j++) {
						dataRow.setString(j, array2D.getJSONArray(j).getString(i));
					}
				}
				//判断数量限制
				if(dataset.size()>=input.getMaxNumberOfResult())
					break;
				//存在下一页
				//处理tableDataString
				if(nextLinkTarget.getPath()!=null){
					hasNextLink = findManager.exists(nextLinkTarget.getPath());
					//点击
					if(hasNextLink){
						IUIHandler uiHandler =  new TargetFinder().getUIHandler(getContext(), nextLinkTarget);
						if(uiHandler == null)
							break;
						IUIElement element = (IUIElement) uiHandler;
						if(!element.enable())
							break;
						if(element instanceof UIAElement){
							((UIAElement) element).simulateClick();
						}else if(element instanceof WEBElement){
							((WEBElement) element).simulateClick(MouseClickType.SINGLE, VirtualKey.VK_LBUTTON);
						}
						try {
							Thread.sleep(input.getDelayBetweenPagesMS());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (LibraryException e) {
				e.printStackTrace();
				break;
			} catch (ActionException e) {
				e.printStackTrace();
				break;
			} catch (JSONException e) {
				e.printStackTrace();
				break;
			}
		} while (hasNextLink);
		output.setDataTable(dataset);
		return output;
	}

}
