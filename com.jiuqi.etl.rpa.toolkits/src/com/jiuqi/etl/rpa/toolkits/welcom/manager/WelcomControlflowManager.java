package com.jiuqi.etl.rpa.toolkits.welcom.manager;

import com.jiuqi.etl.rpa.toolkits.template.Template;
import com.jiuqi.etl.rpa.toolkits.template.TemplateManager;
import com.jiuqi.etl.rpa.toolkits.welcom.bean.ExampleBean;
import com.jiuqi.etl.rpa.toolkits.welcom.bean.ProcessTemplateBean;
import com.jiuqi.etl.rpa.toolkits.welcom.editor.WelcomEditor;
import com.jiuqi.etl.storage.IControlFlowStorage;
import com.jiuqi.etl.storage.ISchemeStorage;
import com.jiuqi.etl.storage.IStorageProvider;
import com.jiuqi.etl.storage.StorageProviderManager;
import com.jiuqi.etl.ui.editor.RecentEditorItem;
import com.jiuqi.etl.ui.view.server.EditAction;
import com.jiuqi.etl.ui.view.server.ServerView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.presentations.IStackPresentationSite;

import com.jiuqi.etl.engine.IExecutor;
import com.jiuqi.etl.engine.IFinishListener;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.bean.ControlFlowBean;
import com.jiuqi.etl.model.bean.ControlFlowFolderBean;
import com.jiuqi.etl.model.bean.ControlFlowWrapper;
import com.jiuqi.etl.model.bean.SchemeBean;
import com.jiuqi.bi.util.Guid;
import com.jiuqi.bi.util.OrderGenerator;
import com.jiuqi.etl.engine.ETLEngineManager;

/**
* @author ���ߣ�houzhiyuan 2019��6��26�� ����10:03:45
*/
public class WelcomControlflowManager {
	
	private static final String CONTROL_FLOW_PREFIX = "������_";
	
	/**
	 * ͨ������ִ�п�����
	 * @param exampleBean
	 * @throws Exception
	 */
	public static void execControlFlow(ExampleBean exampleBean) throws Exception{
		//1.etl������С��
		final Shell shell = Display.getDefault().getActiveShell();
		shell.setMinimized(true);
		//ִ�п�����
		ControlFlowModel controlFlowModel = new ControlFlowModel();
		ByteArrayInputStream bis = new ByteArrayInputStream(exampleBean.getControlFlowContent());
		try {
			controlFlowModel.load(bis);
		} finally {
			bis.close();
		}
		final IExecutor exec = ETLEngineManager.getExecutor(controlFlowModel);
		exec.addFinishListener(new IFinishListener() {
			public void flowFinish(Env env) {
				//�������
				//shell.setMinimized(false);
			}
			
		});
		Map<String, String> taskParam = new HashMap<String, String>();
		
		final Thread t = exec.start(taskParam);
		new Thread(new Runnable() {
			public void run() {
				try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				exec.getEnv().close();
			}
		}).start();;
	}
	/**
	 * ����ʷ��¼�еĿ�����
	 * @param item
	 * @throws Exception
	 */
	public static void openRecentEditor(RecentEditorItem item) throws Exception{
		//�򿪷�����ͼ��������ӭҳ�滹ԭ
		restoredWelcomState();
		ServerView serverView = getServerView();
		serverView.openControlflowEditor(item);
	}
	/**
	 * ��ӿ�����ģ�嵽����������
	 * @param templateBean
	 * @param flowBean
	 * @throws Exception
	 */
	public static void addToControlFlow(ProcessTemplateBean templateBean,
			ControlFlowBean flowBean, List<ControlFlowFolderBean> selectFolderPath) throws Exception{
		if(templateBean == null || flowBean == null) {
			return;
		}
		IStorageProvider provider = StorageProviderManager.getInstance().getStorageProvider();
		IControlFlowStorage controlFlowStorage = provider.getControlFlowStorage(flowBean.getSchemeGuid());
		
		ControlFlowModel controlFlowModel = new ControlFlowModel();
		TemplateManager templateManager = new TemplateManager();
		Template template = templateManager.get(templateBean.getId());
		controlFlowModel.load(new ByteArrayInputStream(template.getData()));//?
		
		//����������bean����
		String guid = Guid.newGuid();
		flowBean.setGuid(guid);
		flowBean.setName(CONTROL_FLOW_PREFIX + OrderGenerator.newOrder());
		flowBean.setModifiedTime(System.currentTimeMillis());
		controlFlowStorage.addControlFlow(flowBean);
		
		//model��ֵ
		controlFlowModel.setGuid(flowBean.getGuid());
		controlFlowModel.setSchemeGuid(flowBean.getGuid());
		controlFlowModel.setName(flowBean.getName());
		controlFlowModel.setTitle(flowBean.getTitle());
		controlFlowModel.setModifiedTime(flowBean.getModifiedTime());
		controlFlowStorage.setControlFlowData(guid, controlFlowModel);//???
		
		ControlFlowWrapper wrapper = new ControlFlowWrapper(flowBean);
		wrapper.setModel(controlFlowModel);
		
		//�򿪷�����ͼ��������ӭҳ�滹ԭ
		restoredWelcomState();
		ServerView serverView = getServerView();
		
		//�ڿ������������ӽڵ㣬����λ,���ݽڵ�·�����ж�λ
		/*TreeViewer treeViewer = serverView.getTreeViewer();
		//ˢ��һ��������֤չ��ʱ�����������ڵ��ܹ�����
		treeViewer.refresh();*/
		locateToControlFlow(serverView, selectFolderPath, wrapper);
		
	}
	
	public static void newControlFlow(ControlFlowBean flowBean, List<ControlFlowFolderBean> selectFolderPath) throws Exception{
		if(flowBean == null) {
			return;
		}
		IStorageProvider provider = StorageProviderManager.getInstance().getStorageProvider();
		IControlFlowStorage controlFlowStorage = provider.getControlFlowStorage(flowBean.getSchemeGuid());
		
		//����������bean����
		String guid = Guid.newGuid();
		flowBean.setGuid(guid);
		flowBean.setName(CONTROL_FLOW_PREFIX + OrderGenerator.newOrder());
		flowBean.setModifiedTime(System.currentTimeMillis());
		controlFlowStorage.addControlFlow(flowBean);
		
		//����Model����
		ControlFlowModel controlFlowModel = new ControlFlowModel();
		controlFlowModel.setGuid(flowBean.getGuid());
		controlFlowModel.setSchemeGuid(flowBean.getGuid());
		controlFlowModel.setName(flowBean.getName());
		controlFlowModel.setTitle(flowBean.getTitle());
		controlFlowModel.setModifiedTime(flowBean.getModifiedTime());
		
		ControlFlowWrapper wrapper = new ControlFlowWrapper(flowBean);
		wrapper.setModel(controlFlowModel);
		
		//�򿪷�����ͼ��������ӭҳ�滹ԭ
		restoredWelcomState();
		ServerView serverView = getServerView();
		
		//�ڿ������������ӽڵ㣬��ѡ�� ,���ݽڵ�·�����ж�λ
		/*TreeViewer treeViewer = serverView.getTreeViewer();*/
		locateToControlFlow(serverView, selectFolderPath, wrapper);
		
		//�򿪿�����
		IWorkbenchWindow window = serverView.getSite().getWorkbenchWindow();
		new EditAction(window, wrapper).run();
	}
	
	/**
	 * ��λ���������ڵ�
	 * @param treeViewer
	 * @param selectFolderPath
	 * @param wrapper
	 * @throws Exception
	 */
	public static void locateToControlFlow(ServerView serverView, 
			List<ControlFlowFolderBean>selectFolderPath, ControlFlowWrapper wrapper) throws Exception {
		ControlFlowBean flowBean = wrapper.getBean();
		IStorageProvider provider = StorageProviderManager.getInstance().getStorageProvider();
		IControlFlowStorage controlFlowStorage = provider.getControlFlowStorage(flowBean.getSchemeGuid());
		ISchemeStorage schemeStorage = provider.getSchemeStorage(null);
		SchemeBean schemeBean = schemeStorage.getScheme(flowBean.getSchemeGuid());
		if(schemeBean == null) {
			return;
		}
		//ѡ��Ŀ������ڵ�
		List<Object> segmentList = new ArrayList<Object>();
		segmentList.add(schemeStorage);
		segmentList.add(schemeBean);
		segmentList.add(controlFlowStorage);
		if(selectFolderPath != null && selectFolderPath.size() > 0) {
			segmentList.addAll(selectFolderPath);
		}
		segmentList.add(wrapper);
		
//		serverView.locateToControlFlowEditor(segmentList);
		
	}
	/**
	 * ��ȡ������������������ͼ���������򴴽�
	 * @return
	 * @throws Exception
	 */
	public static ServerView getServerView() throws Exception{
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IViewPart viewPart  = page.findView(ServerView.VIEW_ID);
		if(viewPart == null) {
			//����ͼ
			viewPart = page.showView(ServerView.VIEW_ID);
		}
		//�򿪿�����
		//page.showView(ServerView.VIEW_ID);
		ServerView serverView = (ServerView)viewPart;
		return serverView;
	}
	/**
	 * ��ԭ��ӭҳ�棬�������򷵻�
	 */
	public static void restoredWelcomState() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		//���һ�ӭҳ��
		IEditorInput editor = new WelcomEditor();
		IEditorPart find = page.findEditor(editor);
		if(find == null) {
			return;
		}
		//��ԭ��ӭҳ��
		IWorkbenchPartReference ref = page.getActivePartReference();
		page.setPartState(ref, IStackPresentationSite.STATE_RESTORED);
	}
}
