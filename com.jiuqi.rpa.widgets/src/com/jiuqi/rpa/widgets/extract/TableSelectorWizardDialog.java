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
	private String text = "", curRowPath = "";//curRowPath��rowPath��JSON String
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
	//��д���������������������һ������һ������ɵ�
	protected void buttonPressed(int buttonId) {
		
		
		//���ǵ������ɡ�����ȡ����λ�úʹ�С���Ա㱣�ִ���λ�ô�С���ı�
		if(buttonId!=IDialogConstants.FINISH_ID)
		{
			size=getShell().getSize();
			location=getShell().getLocation();
		}
		
		//�����һ��
		if (buttonId == IDialogConstants.NEXT_ID) {

			//��ǰҳ�ǡ��б༭��
			if (this.getCurrentPage() instanceof ColumnEditWizardPage) {
				
				//�������elepath
				elePath1=null;
				elePath2=null;
				
				//���������Ҳ��������ѡ��setExtData
				if (((ColumnEditWizardPage) this.getCurrentPage()).getIsReconfig() && !((ColumnEditWizardPage)this.getCurrentPage()).getIsClear()) {
					((ColumnEditWizardPage) this.getCurrentPage()).setExtractedData(((ColumnEditWizardPage) this.getCurrentPage()).getExtractedData());
					((ColumnEditWizardPage) this.getCurrentPage()).setIsReconfig(false);
				}

				//��һҳ�Ǳ������չʾҳ�棬ʵʱ��ȡ�������
				if(this.getCurrentPage().getNextPage() instanceof TableDisplayWizardPage)
				{
					//�б༭��������һ��ʱ��������Pathʵʱ��ȡ��չʾ����
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
							//�����ַ�������
							try {
								JSONArray ja = new JSONArray(r);
								for (int arrayIndex = 0; arrayIndex < ja.length(); arrayIndex++) {
									JSONArray ColumnJa = ja.getJSONArray(arrayIndex);
									for (int ColumnArrayIndex = 0; ColumnArrayIndex < ColumnJa.length(); ColumnArrayIndex++) {
										Object jo = ColumnJa.get(ColumnArrayIndex);
										listSub.add(jo.toString());
									}
									//listSub�д��˵�ǰ��JSONArray������ÿһ��Ԫ����һ��String����Ӧ����е�һ��
									columnStrings.add((ArrayList<String>) listSub.clone());
									listSub.clear();
								}
							} catch (JSONException e) {
								e.printStackTrace();
						}
				}
				
				//Ϊ������һҳ���ݵ�ǰ��Ϣ�ػ�
				IWizardPage page = this.getCurrentPage().getNextPage();
				if (page.getControl() != null)
					page.dispose();
			}

			//��ǰҳ�ǡ��ж��塱
			if (this.getCurrentPage() instanceof ColumnDefineWizardPage) {
				
				//���һ��
				allColumns.add(new ColumnEntity(((ColumnDefineWizardPage) this.getCurrentPage()).getName(),((ColumnDefineWizardPage) this.getCurrentPage()).getTitle()));
				
				//Ϊ������һҳ���ݵ�ǰ��Ϣ�ػ�
				IWizardPage page = this.getCurrentPage().getNextPage();
				if (page.getControl() != null)
					page.dispose();
			}

			//��ǰҳ�ǡ���׼HTML���
			if (this.getCurrentPage() instanceof StandardHTMLTableWizardPage) {
				
				//���ѡ���ˡ�������ȡ��
				if (isFullTable) {
					isFullTable = false;
					try {
						//TODO:���ɶ�����������ȡ
						//������ѡԪ�ػ�ȡ����������ݡ�����Ϣ����Path
						String str = ((WEBElement) element1).getPageDataByTable();
						WebTableColumnsInfo cols = ((WEBElement) element1).getTableCols();
						Path[] colsPath = cols.getColsPath();

						//�����Path
						for (Path path : colsPath) {
							allColumnPathArray.add(path);
						}

						//��ȡrowPath
						WEBFindLibrary wfl = new WEBFindLibrary(new Context());
						rowPath = wfl.getTableRoot((WEBElement) element1).getPath();
						try {
							//����rowPath�ַ������ڱȽ�
							curRowPath = removeEnableFalse(rowPath.toJson().toString());
						} catch (JSONException e1) {
							e1.printStackTrace();
						}

						//��������е�ÿһ�У�Ĭ����Column0��Column1...
						int i = 0;
						for (String colTitle : cols.getColsTitle()) {
							ColumnEntity ce = new ColumnEntity("Column" + String.valueOf(i),colTitle);
							allColumns.add(ce);
							i++;
						}

						//�����������JSON�����������ַ�����һ��JSONArray������ÿ��Ԫ��Ҳ��JSONArray����ӵ�columnStrings��
						try {
							JSONArray ja = new JSONArray(str);
							for (int arrayIndex = 0; arrayIndex < ja.length(); arrayIndex++) {
								JSONArray ColumnJa = ja.getJSONArray(arrayIndex);
								for (int ColumnArrayIndex = 0; ColumnArrayIndex < ColumnJa.length(); ColumnArrayIndex++) {
									Object jo = ColumnJa.get(ColumnArrayIndex);
									listSub.add(jo.toString());
								}
								//listSub�д��˵�ǰ��JSONArray������ÿһ��Ԫ����һ��String����Ӧ����е�һ��
								columnStrings.add((ArrayList<String>) listSub.clone());
								listSub.clear();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

						//Ϊ������һҳ���ݵ�ǰ��Ϣ�ػ�
						IWizardPage page = this.getCurrentPage().getNextPage();
						if (page.getControl() != null)
							page.dispose();

					} catch (LibraryException e) {
						e.printStackTrace();
					}
				}
			}

			//��ǰҳ�ǡ���ӭҳ����׼��ѡ��Ŀ��Ԫ�أ�
			if (this.getCurrentPage() instanceof WelcomeWizardPage) {
				
				//��һҳ�ǡ��ж��塱����ǰ�ǵڶ�����ӭҳ��
				if (this.getCurrentPage().getNextPage() instanceof ColumnDefineWizardPage) {
					
					//��ӵ��кϷ��������е�rowPath�����е�һ��
					if (rowValidate) {

						//�ı�WelcomeWizardPage��־���Ա�Wizard��ȷѡ����һҳ
						((WelcomeWizardPage) this.getCurrentPage()).setIsRowValidate(true);
						
						//Ϊ������һҳ���ݵ�ǰ��Ϣ�ػ�
						IWizardPage page = this.getCurrentPage().getNextPage();
						if (page.getControl() != null)
							page.dispose();
					} else {//���в��Ϸ�
						try {
							//����ѡȡ
							endPick();
						} catch (LibraryException e) {
							e.printStackTrace();
						}
						
						//�ı�WelcomeWizardPage��־���Ա�Wizard��ȷѡ����һҳ
						((WelcomeWizardPage) this.getCurrentPage()).setIsRowValidate(false);
					}
				}
				
				//��һҳ�ǡ���׼HTML��񡱣���ǰҳ�ǵ�һ����ӭҳ��
				else if(this.getCurrentPage().getNextPage() instanceof StandardHTMLTableWizardPage)
				{
					//Ϊ������һҳ���ݵ�ǰ��Ϣ�ػ�
					IWizardPage page = this.getCurrentPage().getNextPage();
					if (page.getControl() != null)
						page.dispose();
				}

				//���û��ѡ���Ԫ��
				if (!selected) {
					// ���ýӿڿ�ʼѡ��Ԫ��
					try {
						startPick();
					} catch (LibraryException e) {
						e.printStackTrace();
					}
				}

				else {
					// �������֧�ֵĿؼ����ͣ��ֽ׶�Ϊ��ҳԪ�أ�isWeb
					if (isWebControl)
					{
						
						// ��ѡԪ�����ڱ�׼HTML���isTable
						if (isStandardHTMLTable)
						{
							//��һҳΪ����׼HTMLҳ��
							WelcomeWizardPage welcomePage = (WelcomeWizardPage) this.getCurrentPage();
							welcomePage.setIsStandardHTMLTable(true);
							
						} else { // ���򣨲��Ǳ�׼HTML��񣩣���һҳΪwelcomepage2��ֱ��ѡ��ڶ���Ԫ��ָ����
							WelcomeWizardPage welcomePage = (WelcomeWizardPage) this.getCurrentPage();
							welcomePage.setIsStandardHTMLTable(false);
						}
						
						//��ѡ�кϷ����ܵ���һҳ
						if (rowValidate) {
							super.buttonPressed(buttonId);
						}
					}

					//������ز�֧�ֵĿؼ����ͣ��ֽ׶�Ϊ����ҳԪ��
					else {
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								MessageBox dialog = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
								dialog.setText("����");
								dialog.setMessage("��֧�ֵĿؼ�����");
								dialog.open();
							}
						});
					}
				}
			}

			//�������ֱ�ӵ��ø�������һ�����������°�ť״̬
			else {
				super.buttonPressed(buttonId);
				updateButtons();
			}
		}
		
		// �����һ��
		else if(buttonId==IDialogConstants.BACK_ID){
			
			//��ǰҳ�ǡ���ӭҳ������һҳҲ�ǡ���ӭҳ��������ǰ�ǵڶ�����ӭҳ��
			if (this.getCurrentPage() instanceof WelcomeWizardPage&& this.getCurrentPage().getPreviousPage() instanceof WelcomeWizardPage) {
				
				//�����ǰ״̬���ص���һ����ӭҳ׼����ѡ
				elePath1 = null;
				rowValidate = true;
				isError = false;
				
				//��һҳ��ӭҳ�����ٵ����һ��
				this.getCurrentPage().getPreviousPage().setPreviousPage(null);
				super.buttonPressed(buttonId);
				updateButtons();
			}
			
			//��һҳ��ӭҳ���б༭ҳ������һ��
			else if((this.getCurrentPage() instanceof WelcomeWizardPage && (this.getCurrentPage().getNextPage() instanceof StandardHTMLTableWizardPage || this.getCurrentPage().getNextPage() instanceof WelcomeWizardPage))|| this.getCurrentPage() instanceof ColumnEditWizardPage)
			{
				//ʲôҲ����
			}
			
			//�������ֱ�ӵ��ø�������һ�����������°�ť״̬
			else {
				super.buttonPressed(buttonId);
				updateButtons();
			}
		}
		
		//�������ֱ�ӵ��ø�������ť����������ɡ�ȡ���ȣ�
		else {
			super.buttonPressed(buttonId);
		}
		
		//����Ĳ�����ɰ�ť������WizardDialog��shell��С��λ�ò���
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
		
		// ��С������
		globalShell = (Shell) getShell().getParent();
		getShell().setMinimized(true);
		globalShell.setMinimized(true);

		//׼�������������ʼ���
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
				//�ָ�����
				getShell().setMinimized(false);
				globalShell.setMinimized(false);
			}
		});

	}

	/**
	 * ��ȡ����Ϣ
	 * @return ����Ϣ
	 */
	public List<ColumnEntity> getColumnEntityList() {
		return allColumns;
	}

	/**
	 * ��������Ϣ
	 * @param ����Ϣ�б�
	 */
	public void setColumnEntityList(List<ColumnEntity> ce) {
		allColumns = ce;
	}

	/**
	 * ������Ϣ�б������һ����Ϣ
	 * @param ��ʵ��
	 */
	public void addColumnEntity(ColumnEntity column) {
		allColumns.add(column);
	}

	/**
	 * ������Ϣ�б���ɾ��һ����Ϣ
	 * @param ��ʵ��column
	 */
	public void deleteColumnEntity(ColumnEntity column) {
		allColumns.remove(column);
	}

	/**
	 * ��ȡ��չʾ����
	 * @return ��չʾ���ݶ�ά�б�columnStrings
	 */
	public ArrayList<ArrayList<String>> getColumnStrings() {
		return columnStrings;
	}

	/**
	 * ������չʾ����
	 * @param ��չʾ���ݶ�ά�б�
	 */
	public void setColumnStrings(ArrayList<ArrayList<String>> cs) {
		columnStrings = cs;
	}

	/**
	 * ��ȡ��Path�б�
	 * @return ��Path�б�allColumnPathArray
	 */
	public List<Path> getAllColumnPath() {
		return allColumnPathArray;
	}

	/**
	 * ������Path�б�
	 * @param ��Path�б�
	 */
	public void setAllColumnPath(List<Path> allColumnPathList) {
		allColumnPathArray = allColumnPathList;
	}

	/**
	 * ��ȡxmlչʾ�ı�������rowPath������Ϣ����Pathת��Ϊxml�ı��������ı�����
	 * @return xml�ı��ַ���text
	 */
	public String getxmlText() {

		//û���У����ؿմ�
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

		//����Ϣ����Path
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
	 * ����index����ѡ��ʱ��¼λ����
	 * @param i
	 */
	public void setIndex(int i) {
		index = i;
		}

	/**
	 * ����isFullTable��־
	 * @param flag
	 */
	public void setFullTable(boolean flag) {
		isFullTable = flag;
	}

	/**
	 * ���õ�ǰrowPath�ַ���
	 * @param curRowPath
	 */
	public void setCurRowPath(String curRowPath) {
		this.curRowPath = curRowPath;
	}

	/**
	 * ����rowPath
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
	 * �Ƴ�Path��enableΪfalse����
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
	 * ʵ��Ԫ��ѡ�������
	 *
	 */
	class UIAPickerListener implements IUIAPickerListener {
		private Rect lastRect = new Rect();

		public void onMouseMove(int x, int y) {
			try {
				synchronized (this) {
					
					//����
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
				//��ȡ�������λ�õ�Ԫ��
				IUIElement uiElement;
				uiElement = findLibraryManager.get(new Point(x, y));

				//��ȡԪ��Path��Ϣ
				Path path = uiElement.getPath();
				
				//��Ԫ�ص�Path����Ϊѡ��ɹ���������Ϊû��ѡ����do nothing
				if (path != null) {
					selected = true;
				}

				//��ʶ�Ƿ�Ϊ֧�ֵĿؼ����ֽ׶�ΪwebԪ��
				if (!path.isWeb()) {
					isWebControl = false;
				}else {
					isWebControl = true;
				}

				//�����֧�ֵĿؼ�
				if (isWebControl) {
					
					//����һ��Ԫ�ر���Ϊ�գ���ǰ��ѡԪ��Ϊ��һ��Ԫ�أ���isTable����ѡ��ȡ�����ָ���У�������ȡָ���У���ѡ��ڶ���Ԫ��
					if (elePath1 == null) {
						elePath1 = path;
						
						//��ȡ��ǰԪ��
						WEBFindLibrary wfl = new WEBFindLibrary(context);
						element1 = wfl.findFirst(elePath1);
						
						//�����ǰԪ�����ڱ�׼HTML���
						if (element1.isTable()) {
							
							//�����ǰû������Ϣ������Ϊ�ǵ�һ��ѡ�񣬽����׼HTML���ҳ������Ϊ����л���ѡ�У���ʹ��Ԫ�����ڱ�׼HTML���ҲҪѡ��ָ����
							if (allColumns.size() == 0) {
								isStandardHTMLTable = true;
							}
							
						}else {//��ǰԪ�ز����ڱ�׼HTML���
							isStandardHTMLTable = false;
						}
					} else {//��һ��Ԫ�ز��գ���ǰԪ��Ϊ�ڶ���Ԫ��
						elePath2 = path;
						
						
						try {
							
							//��������ͬ��Ԫ�ص�Path��ȡ��Path
							ColumnPathArray[0] = WebTableUtil.calcColPath(elePath1, elePath2);
							path = ColumnPathArray[0];
							
						} catch (Exception e) {// ���elepath2���Ǳ�׼�ؼ������elepath2������������ʾ��ֱ�ӷ���
							elePath2 = null;
							selected = false;
							endPick();
							e.printStackTrace();
						}
						
						WEBFindLibrary wfl = new WEBFindLibrary(context);

						try {
							
							//��������Ԫ�ص�Path��ȡTableRoot��Path
							root = WebTableUtil.getTableRoot(elePath1, elePath2);
							IUIElement first = wfl.findFirst(root);

							//�������ֻ��һ�е���ѡ�����������У���Ҫ�ȶ�rowpath���жϵ�ǰ���Ƿ���֮ǰ��ͬ����һ�����
							if (allColumnPathArray.size() > 0)
							{
							
								//ֻ��һ�е���ѡ������Ҫ�ȶԣ�ֱ�Ӹ���rowPath
								if(allColumnPathArray.size()==1&&index!=-1)
								{
									rowValidate = true;
									rowPath = root;
									curRowPath = removeEnableFalse(root.toJson().toString());
								}
								
								//����ֻ��һ�е���ѡ����Ҫ�ȶ�rowPath
								else 
								{
									//�����ǰrowPath��֮ǰ��rowPath��ͬ�������кϷ�
									Path newPath=new Path();
									Path nowPath=new Path();
									newPath.fromJson(new JSONObject(removeEnableFalse(root.toJson().toString())));
									nowPath.fromJson(new JSONObject(curRowPath));
									if (ComparePath(newPath, nowPath)) {
										
										// ���кϷ�
										rowValidate = true;
										
										//������ѡ���������Ϣ������do nothing����Ϊ��ѡ�Ļ�����Ϣ������
										if(index==-1)
										{
											allColumnPathArray.add(path);
										}
									} else {// ���в��Ϸ���������ʾ
										rowValidate = false;
										endPick();
										Display.getDefault().asyncExec(new Runnable() {
											@Override
											public void run() {
												MessageBox dialog = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
												dialog.setText("����");
												dialog.setMessage("��ѡ����֮ǰ�в�����ͬһ���");
												dialog.open();
												rowValidate = true;
											}
										});
									}
								}
							}

							// ��ǰû������Ϣ�������ѡ��Ϊ��һ�У�����Ҫ�ȶԣ�ֱ���������Ϣ������¼rowPath
							else {
								allColumnPathArray.add(path);
								rowValidate = true;
								rowPath = root;
								curRowPath = removeEnableFalse(root.toJson().toString());
							}

							//���кϷ�
							if (rowValidate) {
								
								//��ȡ���е�չʾ���ݣ�ColumnPathArray��ֻ�е�ǰ�е�Pathһ��Ԫ�أ�
								String r = first.getPageData(ColumnPathArray);

								//������չʾ����[["xxx","yyy","zzz"...]]
								JSONArray ja = new JSONArray(r);
								ja=ja.getJSONArray(0);
								for (int i = 0; i < ja.length(); i++) {
									listSub.add(ja.get(i).toString());
								}

								//index==-1˵����ǰ������ѡ���У�����index�ᱻ��ֵ>=0
								if (index == -1) {
									
									// ��ӵ���
									columnStrings.add((ArrayList<String>) listSub.clone());
									
								} else {
									
									// ��ѡ���У���չʾ���ݺ���Path�Ȳ�����ɾ��������Ϣ����
									columnStrings.add(index, (ArrayList<String>) listSub.clone());
									columnStrings.remove(index + 1);
									allColumnPathArray.add(index,path);
									allColumnPathArray.remove(index+1);
									index = -1;
								}

								//������У������ʱ����
								listSub.clear();
								elePath1 = null;
								elePath2 = null;
							}
						} catch (Exception e) {//��Ԫ�ز�����ͬһ��
							selected = false;
							endPick();
							isError = true;
							Display.getDefault().asyncExec(new Runnable() {
								@Override
								public void run() {
									MessageBox dialog = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
									dialog.setText("����");
									dialog.setMessage("��Ԫ�ز�����ͬһ��");
									dialog.open();
								}
							});
						}
					}
					
					//���ѡ��ʱû�г�����Ԫ��ͬ�С��ؼ�����֧�֡������кϷ��ȣ�
					if (!isError) {
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								
								//ʵ�ʵ����һ������ת����һҳ��
								buttonPressed(IDialogConstants.NEXT_ID);
								selected = false;
							}
						});
					} else {//ѡ��ʱ������ʵ�ʵ����һ������isError�ָ�Ϊfalse
						isError = false;
					}
				} else {//�ؼ����Ͳ�֧��
					endPick();
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							MessageBox dialog = new MessageBox(getShell(), SWT.OK | SWT.ICON_ERROR);
							dialog.setText("����");
							dialog.setMessage("��֧�ֵĿؼ�����");
							dialog.open();
						}
					});
					
					//��ѡ���Ϊfalse���ؼ����ͻָ�Ĭ��Ϊ֧��
					selected = false;
					isWebControl = true;
				}
				endPick();
			} catch (LibraryException e) {
				
				//��ѡ���Ϊfalse���ؼ����ͻָ�Ĭ��Ϊ֧��
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
//				//��ѡ���Ϊfalse���ؼ����ͻָ�Ĭ��Ϊ֧��
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
	 * ��ȡ������Path
	 * @return ��Path�б�
	 */
	public ArrayList<Path> getColumnsPathList() {
		return (ArrayList<Path>) allColumnPathArray;
	}

	/**
	 * ��ȡDataTableField��������Ϣ
	 * @return ArrayList<DataTableField> ColumnsList
	 */
	public ArrayList<DataTableField> getColumnsList() {
		
		//������ColumnEntity����Ϣ��ӵ�ColumnsList��
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
	 * �ж�����Path�Ƿ����ݵȼۣ�����element˳�򼰸�����������ϵ����true��
	 */
	public boolean ComparePath(Path path1,Path path2) {
		
		Path tempPath;
		//path1����<=path2
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
	 * �ж�����pathElement�Ƿ�ȼۣ�����Attribute˳��
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
	 * �ж�����PathAttribute�Ƿ�ȼۣ�ͬWEBTableUtil
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
