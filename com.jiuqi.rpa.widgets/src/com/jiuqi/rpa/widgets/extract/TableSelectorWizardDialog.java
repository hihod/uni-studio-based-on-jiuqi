package com.jiuqi.rpa.widgets.extract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jiuqi.etl.model.DataTableField;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.lib.Rect;
import com.jiuqi.rpa.lib.drawer.UIADrawerLibrary;
import com.jiuqi.rpa.lib.find.FindLibraryManager;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.find.Path;
import com.jiuqi.rpa.lib.find.PathAttribute;
import com.jiuqi.rpa.lib.find.PathElement;
import com.jiuqi.rpa.lib.find.WEBElement;
import com.jiuqi.rpa.lib.find.WEBFindLibrary;
import com.jiuqi.rpa.lib.find.WebTableColumnsInfo;
import com.jiuqi.rpa.lib.find.WebTableUtil;
import com.jiuqi.rpa.lib.picker.IUIAPickerListener;
import com.jiuqi.rpa.lib.picker.UIAPickerCallback;
import com.jiuqi.rpa.lib.picker.UIAPickerLibrary;

public class TableSelectorWizardDialog extends WizardDialog {
	private Shell globalShell;
	private Context context;
	private FindLibraryManager findLibraryManager;
	private UIADrawerLibrary uiaDrawerLibrary;
	private UIAPickerLibrary pLibrary;
	private Path root;
	private Path elePath1;
	private Path elePath2;
	private Path rowPath;
	private Path[] ColumnPathArray = new Path[1];
	private List<Path> allColumnPathArray = new ArrayList<Path>();
	private boolean isWebControl = true;
	private boolean isStandardHTMLTable = false;
	private boolean selected = false;
	private boolean isFullTable = false;
	private boolean isError = false;
	private boolean rowValidate = true;
	private IUIElement element1;
	private String text = "", curRowPath = "";//curRowPath是rowPath的JSON String
	private List<ColumnEntity> allColumns = new ArrayList<ColumnEntity>();
	private ArrayList<String> listSub = new ArrayList<String>();
	private ArrayList<ArrayList<String>> columnStrings = new ArrayList<ArrayList<String>>(); 
	private int index = -1;
	private org.eclipse.swt.graphics.Point size,location; 
	
	public TableSelectorWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
		setPageSize(Display.getCurrent().getClientArea().width / 2,Display.getCurrent().getClientArea().height / 2);
	}

	@SuppressWarnings("unchecked")
	@Override
	//重写按键点击处理函数，包括下一步、上一步、完成等
	protected void buttonPressed(int buttonId) {
		
		
		//不是点击“完成”，获取窗口位置和大小，以便保持窗口位置大小不改变
		if(buttonId!=IDialogConstants.FINISH_ID)
		{
			size=getShell().getSize();
			location=getShell().getLocation();
		}
		
		//点击下一步
		if (buttonId == IDialogConstants.NEXT_ID) {

			//当前页是“列编辑”
			if (this.getCurrentPage() instanceof ColumnEditWizardPage) {
				
				//清空两个elepath
				elePath1=null;
				elePath2=null;
				
				//二次配置且不是清除重选，setExtData
				if (((ColumnEditWizardPage) this.getCurrentPage()).getIsReconfig() && !((ColumnEditWizardPage)this.getCurrentPage()).getIsClear()) {
					((ColumnEditWizardPage) this.getCurrentPage()).setExtractedData(((ColumnEditWizardPage) this.getCurrentPage()).getExtractedData());
					((ColumnEditWizardPage) this.getCurrentPage()).setIsReconfig(false);
				}

				//下一页是表格内容展示页面，实时获取表格内容
				if(this.getCurrentPage().getNextPage() instanceof TableDisplayWizardPage)
				{
					//列编辑界面点击下一步时，根据列Path实时获取列展示内容
					columnStrings.clear();
					String r="[[]]";
					IUIElement first;
					FindLibraryManager findLibraryManager=new FindLibraryManager(new Context());
					try {
						first = findLibraryManager.findFirst(rowPath);
						boolean isFullTable = false;
						if(allColumnPathArray != null && !allColumnPathArray.isEmpty()) {
							Path path = allColumnPathArray.get(0);
							if(path==null ||path.getElements() == null ||path.getElements().size()<1) {
								isFullTable = true;
							}
						}
						if(isFullTable) {
								r = ((WEBElement)first).getPageDataByTable();
						}else {
							Path[] paths = allColumnPathArray.toArray(new Path[allColumnPathArray.size()]);
							r=first.getPageData(paths);
						}
					} catch (LibraryException e1) {
						e1.printStackTrace();
					}
							//解析字符串内容
							try {
								JSONArray ja = new JSONArray(r);
								for (int arrayIndex = 0; arrayIndex < ja.length(); arrayIndex++) {
									JSONArray ColumnJa = ja.getJSONArray(arrayIndex);
									for (int ColumnArrayIndex = 0; ColumnArrayIndex < ColumnJa.length(); ColumnArrayIndex++) {
										Object jo = ColumnJa.get(ColumnArrayIndex);
										listSub.add(jo.toString());
									}
									//listSub中存了当前的JSONArray，其中每一个元素是一个String，对应表格中的一格
									columnStrings.add((ArrayList<String>) listSub.clone());
									listSub.clear();
								}
							} catch (JSONException e) {
								e.printStackTrace();
						}
				}
				
				//为了让下一页根据当前信息重绘
				IWizardPage page = this.getCurrentPage().getNextPage();
				if (page.getControl() != null)
					page.dispose();
			}

			//当前页是“列定义”
			if (this.getCurrentPage() instanceof ColumnDefineWizardPage) {
				
				//添加一列
				allColumns.add(new ColumnEntity(((ColumnDefineWizardPage) this.getCurrentPage()).getName(),((ColumnDefineWizardPage) this.getCurrentPage()).getTitle()));
				
				//为了让下一页根据当前信息重绘
				IWizardPage page = this.getCurrentPage().getNextPage();
				if (page.getControl() != null)
					page.dispose();
			}

			//当前页是“标准HTML表格”
			if (this.getCurrentPage() instanceof StandardHTMLTableWizardPage) {
				
				//如果选择了“整表提取”
				if (isFullTable) {
					isFullTable = false;
					try {
						//TODO:换成独立的整表提取
						//根据所选元素获取整个表格内容、列信息和列Path
						String str = ((WEBElement) element1).getPageDataByTable();
						WebTableColumnsInfo cols = ((WEBElement) element1).getTableCols();
						Path[] colsPath = cols.getColsPath();

						//添加列Path
						for (Path path : colsPath) {
							allColumnPathArray.add(path);
						}

						//获取rowPath
						WEBFindLibrary wfl = new WEBFindLibrary(new Context());
						rowPath = wfl.getTableRoot((WEBElement) element1).getPath();
						try {
							//保存rowPath字符串用于比较
							curRowPath = removeEnableFalse(rowPath.toJson().toString());
						} catch (JSONException e1) {
							e1.printStackTrace();
						}

						//添加整表中的每一列，默认名Column0、Column1...
						int i = 0;
						for (String colTitle : cols.getColsTitle()) {
							ColumnEntity ce = new ColumnEntity("Column" + String.valueOf(i),colTitle);
							allColumns.add(ce);
							i++;
						}

						//将表格内容以JSON解析，整个字符串是一个JSONArray，其中每个元素也是JSONArray，添加到columnStrings中
						try {
							JSONArray ja = new JSONArray(str);
							for (int arrayIndex = 0; arrayIndex < ja.length(); arrayIndex++) {
								JSONArray ColumnJa = ja.getJSONArray(arrayIndex);
								for (int ColumnArrayIndex = 0; ColumnArrayIndex < ColumnJa.length(); ColumnArrayIndex++) {
									Object jo = ColumnJa.get(ColumnArrayIndex);
									listSub.add(jo.toString());
								}
								//listSub中存了当前的JSONArray，其中每一个元素是一个String，对应表格中的一格
								columnStrings.add((ArrayList<String>) listSub.clone());
								listSub.clear();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

						//为了让下一页根据当前信息重绘
						IWizardPage page = this.getCurrentPage().getNextPage();
						if (page.getControl() != null)
							page.dispose();

					} catch (LibraryException e) {
						e.printStackTrace();
					}
				}
			}

			//当前页是“欢迎页”（准备选择目标元素）
			if (this.getCurrentPage() instanceof WelcomeWizardPage) {
				
				//下一页是“列定义”（当前是第二个欢迎页）
				if (this.getCurrentPage().getNextPage() instanceof ColumnDefineWizardPage) {
					
					//添加的列合法，即新列的rowPath与已有的一致
					if (rowValidate) {

						//改变WelcomeWizardPage标志，以便Wizard正确选择下一页
						((WelcomeWizardPage) this.getCurrentPage()).setIsRowValidate(true);
						
						//为了让下一页根据当前信息重绘
						IWizardPage page = this.getCurrentPage().getNextPage();
						if (page.getControl() != null)
							page.dispose();
					} else {//该列不合法
						try {
							//结束选取
							endPick();
						} catch (LibraryException e) {
							e.printStackTrace();
						}
						
						//改变WelcomeWizardPage标志，以便Wizard正确选择下一页
						((WelcomeWizardPage) this.getCurrentPage()).setIsRowValidate(false);
					}
				}
				
				//下一页是“标准HTML表格”（当前页是第一个欢迎页）
				else if(this.getCurrentPage().getNextPage() instanceof StandardHTMLTableWizardPage)
				{
					//为了让下一页根据当前信息重绘
					IWizardPage page = this.getCurrentPage().getNextPage();
					if (page.getControl() != null)
						page.dispose();
				}

				//如果没有选择过元素
				if (!selected) {
					// 调用接口开始选择元素
					try {
						startPick();
					} catch (LibraryException e) {
						e.printStackTrace();
					}
				}

				else {
					// 如果返回支持的控件类型，现阶段为网页元素，isWeb
					if (isWebControl)
					{
						
						// 所选元素属于标准HTML表格，isTable
						if (isStandardHTMLTable)
						{
							//下一页为“标准HTML页”
							WelcomeWizardPage welcomePage = (WelcomeWizardPage) this.getCurrentPage();
							welcomePage.setIsStandardHTMLTable(true);
							
						} else { // 否则（不是标准HTML表格），下一页为welcomepage2，直接选择第二个元素指定列
							WelcomeWizardPage welcomePage = (WelcomeWizardPage) this.getCurrentPage();
							welcomePage.setIsStandardHTMLTable(false);
						}
						
						//所选列合法才能到下一页
						if (rowValidate) {
							super.buttonPressed(buttonId);
						}
					}

					//如果返回不支持的控件类型，现阶段为非网页元素
					else {
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								MessageBox dialog = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
								dialog.setText("错误");
								dialog.setMessage("不支持的控件类型");
								dialog.open();
							}
						});
					}
				}
			}

			//其余情况直接调用父类点击下一步方法并更新按钮状态
			else {
				super.buttonPressed(buttonId);
				updateButtons();
			}
		}
		
		// 点击上一步
		else if(buttonId==IDialogConstants.BACK_ID){
			
			//当前页是“欢迎页”且上一页也是“欢迎页”（即当前是第二个欢迎页）
			if (this.getCurrentPage() instanceof WelcomeWizardPage&& this.getCurrentPage().getPreviousPage() instanceof WelcomeWizardPage) {
				
				//清除当前状态，回到第一个欢迎页准备重选
				elePath1 = null;
				rowValidate = true;
				isError = false;
				
				//第一页欢迎页不能再点击上一步
				this.getCurrentPage().getPreviousPage().setPreviousPage(null);
				super.buttonPressed(buttonId);
				updateButtons();
			}
			
			//第一页欢迎页和列编辑页不能上一步
			else if((this.getCurrentPage() instanceof WelcomeWizardPage && (this.getCurrentPage().getNextPage() instanceof StandardHTMLTableWizardPage || this.getCurrentPage().getNextPage() instanceof WelcomeWizardPage))|| this.getCurrentPage() instanceof ColumnEditWizardPage)
			{
				//什么也不做
			}
			
			//其余情况直接调用父类点击上一步方法并更新按钮状态
			else {
				super.buttonPressed(buttonId);
				updateButtons();
			}
		}
		
		//其余情况直接调用父类点击按钮方法（如完成、取消等）
		else {
			super.buttonPressed(buttonId);
		}
		
		//点击的不是完成按钮，保持WizardDialog的shell大小和位置不变
		if(buttonId!=IDialogConstants.FINISH_ID)
		{
			getShell().setLocation(location);
			getShell().setSize(size);
//			getShell().setLocation(Display.getCurrent().getClientArea().width / 2 - getShell().getSize().x/2, Display.getCurrent() .getClientArea().height / 2 - getShell().getSize().y/2); 
//			getShell().setSize(new org.eclipse.swt.graphics.Point(Display.getCurrent().getClientArea().width / 2,(int) (Display.getCurrent().getClientArea().height / 1.5)));
		}
	}


//	/* (non-Javadoc)
//	 * @see org.eclipse.jface.wizard.WizardDialog#setPageSize(int, int)
//	 */
//	@Override
//	public void setPageSize(int width, int height) {
//		// TODO Auto-generated method stub
//		width=Display.getCurrent().getClientArea().width / 2;
//		height=(int) (Display.getCurrent().getClientArea().height / 1.5);
//		super.setPageSize(width, height);
//		
//	}
	
	private void startPick() throws LibraryException {
		
		// 最小化窗口
		globalShell = (Shell) getShell().getParent();
		getShell().setMinimized(true);
		globalShell.setMinimized(true);

		//准备点击操作并开始点击
		this.pLibrary = new UIAPickerLibrary();
		this.context = new Context();
		findLibraryManager = new FindLibraryManager(context);
		this.uiaDrawerLibrary = new UIADrawerLibrary();
		UIAPickerCallback uiaPickerCallback = new UIAPickerCallback();
		UIAPickerCallback.setListener(new UIAPickerListener());
		pLibrary.startPick(uiaPickerCallback);
	}

	private void endPick() throws LibraryException {
		pLibrary.endPick();
		uiaDrawerLibrary.endDraw();
		context.close();
		
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				//恢复窗口
				getShell().setMinimized(false);
				globalShell.setMinimized(false);
			}
		});

	}

	/**
	 * 获取列信息
	 * @return 列信息
	 */
	public List<ColumnEntity> getColumnEntityList() {
		return allColumns;
	}

	/**
	 * 设置列信息
	 * @param 列信息列表
	 */
	public void setColumnEntityList(List<ColumnEntity> ce) {
		allColumns = ce;
	}

	/**
	 * 向列信息列表中添加一列信息
	 * @param 列实体
	 */
	public void addColumnEntity(ColumnEntity column) {
		allColumns.add(column);
	}

	/**
	 * 从列信息列表中删除一列信息
	 * @param 列实体column
	 */
	public void deleteColumnEntity(ColumnEntity column) {
		allColumns.remove(column);
	}

	/**
	 * 获取列展示内容
	 * @return 列展示内容二维列表columnStrings
	 */
	public ArrayList<ArrayList<String>> getColumnStrings() {
		return columnStrings;
	}

	/**
	 * 设置列展示内容
	 * @param 列展示内容二维列表
	 */
	public void setColumnStrings(ArrayList<ArrayList<String>> cs) {
		columnStrings = cs;
	}

	/**
	 * 获取列Path列表
	 * @return 列Path列表allColumnPathArray
	 */
	public List<Path> getAllColumnPath() {
		return allColumnPathArray;
	}

	/**
	 * 设置列Path列表
	 * @param 列Path列表
	 */
	public void setAllColumnPath(List<Path> allColumnPathList) {
		allColumnPathArray = allColumnPathList;
	}

	/**
	 * 获取xml展示文本，即将rowPath、列信息、列Path转换为xml文本并返回文本内容
	 * @return xml文本字符串text
	 */
	public String getxmlText() {

		//没有列，返回空串
		text = "";
		if (allColumnPathArray.size() == 0) {
			curRowPath = "";
			return text;
		}

		text += "<extract>\n\n";
		
		//rowPath
		text += "\t<row>\n";
		text += PathHelper.toXMLformat(rowPath);
		text += "\t</row>\n\n";

		//列信息及列Path
		for (int i = 0 ; i < allColumnPathArray.size() ; i++) {
			Path path = allColumnPathArray.get(i);
			int pathIndex= i;
			ColumnEntity columnEntity = allColumns.get(pathIndex);
			text += "\t<column name=\"" + columnEntity.getName() + "\" title=\""+ columnEntity.getTitle()+"\"" + ">\n";
			if(path!=null) {
				text += PathHelper.toXMLformat(path);
			}
			
			text += "\t</column>\n\n";
		}
		
		text += "</extract>";
		
		return text;
	}

	/**
	 * 设置index，重选列时记录位置用
	 * @param i
	 */
	public void setIndex(int i) {
		index = i;
		}

	/**
	 * 设置isFullTable标志
	 * @param flag
	 */
	public void setFullTable(boolean flag) {
		isFullTable = flag;
	}

	/**
	 * 设置当前rowPath字符串
	 * @param curRowPath
	 */
	public void setCurRowPath(String curRowPath) {
		this.curRowPath = curRowPath;
	}

	/**
	 * 设置rowPath
	 * @param rp
	 */
	public void setRowPath(Path rp) {
		rowPath = rp;
	}

//	public static String Json2XML(String jsonStr) {
//		JSONObject jo;
//		try {
//			jo = new JSONObject(jsonStr);
//			return XML.toString(jo);
//		} catch (JSONException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		return "";
//	}

	/**
	 * 移除Path中enable为false的项
	 * @param jsonStr
	 * @return
	 */
	public String removeEnableFalse(String jsonStr) {
		String str = jsonStr;

		try {
			JSONObject jo = new JSONObject(str);
			JSONArray ja = jo.getJSONArray("elements");
			for (int i = 0; i < ja.length(); i++) {

				JSONObject jsonobject = ja.getJSONObject(i);
				JSONArray jsonarray = jsonobject.getJSONArray("attributes");
				for (int j = 0; j < jsonarray.length(); j++) {
					if (jsonarray.getJSONObject(j).get("enable").toString() == "false") {

						jsonarray.remove(j);
						j--;

					}
				}
				if (jsonarray.length() == 0) {
					ja.remove(i);
					i--;
				}
			}

			return jo.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}


	/**
	 * 实现元素选择监听器
	 *
	 */
	class UIAPickerListener implements IUIAPickerListener {
		private Rect lastRect = new Rect();

		public void onMouseMove(int x, int y) {
			try {
				synchronized (this) {
					
					//画框
					Rect rect = findLibraryManager.getRect(new Point(x, y));
					if (rect.equals(lastRect))
						return;

					uiaDrawerLibrary.startDraw(rect, "#ff0000");
					lastRect = rect;
				}
			} catch (LibraryException e) {
				e.printStackTrace();
			}
		}

		@SuppressWarnings("unchecked")
		public void onMouseLeftUp(int x, int y) {
			
			try {
				//获取鼠标所在位置的元素
				IUIElement uiElement;
				uiElement = findLibraryManager.get(new Point(x, y));

				//获取元素Path信息
				Path path = uiElement.getPath();
				
				//有元素的Path，认为选择成功，否则认为没有选到，do nothing
				if (path != null) {
					selected = true;
				}

				//标识是否为支持的控件，现阶段为web元素
				if (!path.isWeb()) {
					isWebControl = false;
				}else {
					isWebControl = true;
				}

				//如果是支持的控件
				if (isWebControl) {
					
					//若第一个元素变量为空，则当前所选元素为第一个元素，若isTable，可选提取整表或指定列，否则提取指定列，将选择第二个元素
					if (elePath1 == null) {
						elePath1 = path;
						
						//获取当前元素
						WEBFindLibrary wfl = new WEBFindLibrary(context);
						element1 = wfl.findFirst(elePath1);
						
						//如果当前元素属于标准HTML表格
						if (element1.isTable()) {
							
							//如果当前没有列信息，则认为是第一次选择，进入标准HTML表格页，否则为添加列或重选列，即使该元素属于标准HTML表格，也要选择指定列
							if (allColumns.size() == 0) {
								isStandardHTMLTable = true;
							}
							
						}else {//当前元素不属于标准HTML表格
							isStandardHTMLTable = false;
						}
					} else {//第一个元素不空，当前元素为第二个元素
						elePath2 = path;
						
						
						try {
							
							//根据两个同列元素的Path获取列Path
							ColumnPathArray[0] = WebTableUtil.calcColPath(elePath1, elePath2);
							path = ColumnPathArray[0];
							
						} catch (Exception e) {// 如果elepath2不是标准控件，清除elepath2，弹出错误提示，直接返回
							elePath2 = null;
							selected = false;
							endPick();
							e.printStackTrace();
						}
						
						WEBFindLibrary wfl = new WEBFindLibrary(context);

						try {
							
							//根据两个元素的Path获取TableRoot的Path
							root = WebTableUtil.getTableRoot(elePath1, elePath2);
							IUIElement first = wfl.findFirst(root);

							//如果不是只有一列的重选，且有新增列，需要比对rowpath来判断当前列是否与之前列同属于一个表格
							if (allColumnPathArray.size() > 0)
							{
							
								//只有一列的重选，不需要比对，直接更新rowPath
								if(allColumnPathArray.size()==1&&index!=-1)
								{
									rowValidate = true;
									rowPath = root;
									curRowPath = removeEnableFalse(root.toJson().toString());
								}
								
								//不是只有一列的重选，需要比对rowPath
								else 
								{
									//如果当前rowPath与之前的rowPath相同，即新列合法
									Path newPath=new Path();
									Path nowPath=new Path();
									newPath.fromJson(new JSONObject(removeEnableFalse(root.toJson().toString())));
									nowPath.fromJson(new JSONObject(curRowPath));
									if (ComparePath(newPath, nowPath)) {
										
										// 新列合法
										rowValidate = true;
										
										//不是重选再添加列信息，否则do nothing，因为重选的话列信息还存在
										if(index==-1)
										{
											allColumnPathArray.add(path);
										}
									} else {// 新列不合法，弹窗提示
										rowValidate = false;
										endPick();
										Display.getDefault().asyncExec(new Runnable() {
											@Override
											public void run() {
												MessageBox dialog = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
												dialog.setText("错误");
												dialog.setMessage("新选列与之前列不属于同一表格");
												dialog.open();
												rowValidate = true;
											}
										});
									}
								}
							}

							// 当前没有列信息，因此所选列为第一列，不需要比对，直接添加列信息，并记录rowPath
							else {
								allColumnPathArray.add(path);
								rowValidate = true;
								rowPath = root;
								curRowPath = removeEnableFalse(root.toJson().toString());
							}

							//新列合法
							if (rowValidate) {
								
								//获取该列的展示内容（ColumnPathArray里只有当前列的Path一个元素）
								String r = first.getPageData(ColumnPathArray);

								//解析列展示内容[["xxx","yyy","zzz"...]]
								JSONArray ja = new JSONArray(r);
								ja=ja.getJSONArray(0);
								for (int i = 0; i < ja.length(); i++) {
									listSub.add(ja.get(i).toString());
								}

								//index==-1说明当前不是重选的列，否则index会被赋值>=0
								if (index == -1) {
									
									// 添加的列
									columnStrings.add((ArrayList<String>) listSub.clone());
									
								} else {
									
									// 重选的列，列展示内容和列Path先插入再删除，列信息不变
									columnStrings.add(index, (ArrayList<String>) listSub.clone());
									columnStrings.remove(index + 1);
									allColumnPathArray.add(index,path);
									allColumnPathArray.remove(index+1);
									index = -1;
								}

								//添加完列，清空临时变量
								listSub.clear();
								elePath1 = null;
								elePath2 = null;
							}
						} catch (Exception e) {//两元素不属于同一列
							selected = false;
							endPick();
							isError = true;
							Display.getDefault().asyncExec(new Runnable() {
								@Override
								public void run() {
									MessageBox dialog = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
									dialog.setText("错误");
									dialog.setMessage("两元素不属于同一列");
									dialog.open();
								}
							});
						}
					}
					
					//如果选择时没有出错（两元素同列、控件类型支持、新增列合法等）
					if (!isError) {
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								
								//实际点击下一步，跳转到下一页面
								buttonPressed(IDialogConstants.NEXT_ID);
								selected = false;
							}
						});
					} else {//选择时出错，不实际点击下一步，将isError恢复为false
						isError = false;
					}
				} else {//控件类型不支持
					endPick();
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							MessageBox dialog = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
							dialog.setText("错误");
							dialog.setMessage("不支持的控件类型");
							dialog.open();
						}
					});
					
					//已选择变为false，控件类型恢复默认为支持
					selected = false;
					isWebControl = true;
				}
				endPick();
			} catch (LibraryException e) {
				
				//已选择变为false，控件类型恢复默认为支持
				selected = false;
				isWebControl = true;
				
				try {
					endPick();
				} catch (LibraryException e1) {
					e1.printStackTrace();
				}
				
				e.printStackTrace();
			} 
			
//			catch (JSONException e) {
//				//已选择变为false，控件类型恢复默认为支持
//				selected = false;
//				isWebControl = true;
//				
//				try {
//					endPick();
//				} catch (LibraryException e1) {
//					e1.printStackTrace();
//				}
//				
//				e.printStackTrace();
//			}
		}

		public void onKeyEscapeUp() {
			try {
				endPick();
			} catch (LibraryException e) {
				e.printStackTrace();
			}
		}
	}

	public Path getRowPath() {
		return rowPath;
	}

	/**
	 * 获取所有列Path
	 * @return 列Path列表
	 */
	public ArrayList<Path> getColumnsPathList() {
		return (ArrayList<Path>) allColumnPathArray;
	}

	/**
	 * 获取DataTableField类型列信息
	 * @return ArrayList<DataTableField> ColumnsList
	 */
	public ArrayList<DataTableField> getColumnsList() {
		
		//将所有ColumnEntity列信息添加到ColumnsList里
		ArrayList<DataTableField> ColumnsList = new ArrayList<DataTableField>();
		for (ColumnEntity Column : allColumns) {
			DataTableField dtf = new DataTableField();
			dtf.setName(Column.getName());
			dtf.setTitle(Column.getTitle());
			ColumnsList.add(dtf);
		}
		return ColumnsList;
	}
	
	/**
	 * 判断两个Path是否内容等价，忽略element顺序及个数（包含关系返回true）
	 */
	public boolean ComparePath(Path path1,Path path2) {
		
		Path tempPath;
		//path1长度<=path2
		if(path1.getElements().size()>path2.getElements().size())
		{
			tempPath=path1;
			path1=path2;
			path2=tempPath;
		}
		
		for(int i=0;i<path1.getElements().size();i++)
		{
			for(int j=0;j<path2.getElements().size();j++)
			{
				if(ComparePathElement(path1.getElements().get(i), path2.getElements().get(j)))
				{
					break;
				}
				
				if(j==path2.getElements().size()-1)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 判断两个pathElement是否等价，忽略Attribute顺序
	 * @param pathElement1
	 * @param pathElement2
	 * @return
	 */
	public boolean ComparePathElement(PathElement pathElement1,PathElement pathElement2)
	{
		for(int i=0;i<pathElement1.getAttributes().size();i++)
		{
			for(int j=0;j<pathElement2.getAttributes().size();j++)
			{
				if(CompareAttribute(pathElement1.getAttributes().get(i), pathElement2.getAttributes().get(j)))
				{
					break;
				}
				
				if(j==pathElement2.getAttributes().size()-1)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 判断两个PathAttribute是否等价，同WEBTableUtil
	 * @param pa1
	 * @param pa2
	 * @return
	 */
	private  boolean CompareAttribute(PathAttribute pa1, PathAttribute pa2) {
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardDialog#updateButtons()
	 */
	@Override
	public void updateButtons() {
		// TODO Auto-generated method stub
		super.updateButtons();
		if(getCurrentPage() instanceof ColumnEditWizardPage || (getCurrentPage() instanceof WelcomeWizardPage&&(getCurrentPage().getNextPage() instanceof WelcomeWizardPage)||getCurrentPage().getNextPage() instanceof StandardHTMLTableWizardPage) ){
			getButton(IDialogConstants.BACK_ID).setEnabled(false);
		}

	}
	
}
