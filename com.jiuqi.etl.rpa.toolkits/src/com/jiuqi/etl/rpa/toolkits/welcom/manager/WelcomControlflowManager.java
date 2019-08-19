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
* @author 作者：houzhiyuan 2019年6月26日 上午10:03:45
*/
public class WelcomControlflowManager {
	
	private static final String CONTROL_FLOW_PREFIX = "控制流_";
	
	/**
	 * 通过案例执行控制流
	 * @param exampleBean
	 * @throws Exception
	 */
	public static void execControlFlow(ExampleBean exampleBean) throws Exception{
		//1.etl窗口最小化
		final Shell shell = Display.getDefault().getActiveShell();
		shell.setMinimized(true);
		//执行控制流
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
				//窗口最大化
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
	 * 打开历史记录中的控制流
	 * @param item
	 * @throws Exception
	 */
	public static void openRecentEditor(RecentEditorItem item) throws Exception{
		//打开方案视图，并将欢迎页面还原
		restoredWelcomState();
		ServerView serverView = getServerView();
		serverView.openControlflowEditor(item);
	}
	/**
	 * 添加控制流模板到控制流树下
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
		
		//创建控制流bean对象
		String guid = Guid.newGuid();
		flowBean.setGuid(guid);
		flowBean.setName(CONTROL_FLOW_PREFIX + OrderGenerator.newOrder());
		flowBean.setModifiedTime(System.currentTimeMillis());
		controlFlowStorage.addControlFlow(flowBean);
		
		//model赋值
		controlFlowModel.setGuid(flowBean.getGuid());
		controlFlowModel.setSchemeGuid(flowBean.getGuid());
		controlFlowModel.setName(flowBean.getName());
		controlFlowModel.setTitle(flowBean.getTitle());
		controlFlowModel.setModifiedTime(flowBean.getModifiedTime());
		controlFlowStorage.setControlFlowData(guid, controlFlowModel);//???
		
		ControlFlowWrapper wrapper = new ControlFlowWrapper(flowBean);
		wrapper.setModel(controlFlowModel);
		
		//打开方案视图，并将欢迎页面还原
		restoredWelcomState();
		ServerView serverView = getServerView();
		
		//在控制流树上增加节点，并定位,根据节点路径进行定位
		/*TreeViewer treeViewer = serverView.getTreeViewer();
		//刷新一下树，保证展开时，新增的树节点能够加载
		treeViewer.refresh();*/
		locateToControlFlow(serverView, selectFolderPath, wrapper);
		
	}
	
	public static void newControlFlow(ControlFlowBean flowBean, List<ControlFlowFolderBean> selectFolderPath) throws Exception{
		if(flowBean == null) {
			return;
		}
		IStorageProvider provider = StorageProviderManager.getInstance().getStorageProvider();
		IControlFlowStorage controlFlowStorage = provider.getControlFlowStorage(flowBean.getSchemeGuid());
		
		//创建控制流bean对象
		String guid = Guid.newGuid();
		flowBean.setGuid(guid);
		flowBean.setName(CONTROL_FLOW_PREFIX + OrderGenerator.newOrder());
		flowBean.setModifiedTime(System.currentTimeMillis());
		controlFlowStorage.addControlFlow(flowBean);
		
		//创建Model对象
		ControlFlowModel controlFlowModel = new ControlFlowModel();
		controlFlowModel.setGuid(flowBean.getGuid());
		controlFlowModel.setSchemeGuid(flowBean.getGuid());
		controlFlowModel.setName(flowBean.getName());
		controlFlowModel.setTitle(flowBean.getTitle());
		controlFlowModel.setModifiedTime(flowBean.getModifiedTime());
		
		ControlFlowWrapper wrapper = new ControlFlowWrapper(flowBean);
		wrapper.setModel(controlFlowModel);
		
		//打开方案视图，并将欢迎页面还原
		restoredWelcomState();
		ServerView serverView = getServerView();
		
		//在控制流树上增加节点，并选中 ,根据节点路径进行定位
		/*TreeViewer treeViewer = serverView.getTreeViewer();*/
		locateToControlFlow(serverView, selectFolderPath, wrapper);
		
		//打开控制流
		IWorkbenchWindow window = serverView.getSite().getWorkbenchWindow();
		new EditAction(window, wrapper).run();
	}
	
	/**
	 * 定位到控制流节点
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
		//选择的控制流节点
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
	 * 获取服务器（方案树）视图，不存在则创建
	 * @return
	 * @throws Exception
	 */
	public static ServerView getServerView() throws Exception{
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IViewPart viewPart  = page.findView(ServerView.VIEW_ID);
		if(viewPart == null) {
			//打开视图
			viewPart = page.showView(ServerView.VIEW_ID);
		}
		//打开控制流
		//page.showView(ServerView.VIEW_ID);
		ServerView serverView = (ServerView)viewPart;
		return serverView;
	}
	/**
	 * 还原欢迎页面，不存在则返回
	 */
	public static void restoredWelcomState() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		//查找欢迎页面
		IEditorInput editor = new WelcomEditor();
		IEditorPart find = page.findEditor(editor);
		if(find == null) {
			return;
		}
		//还原欢迎页面
		IWorkbenchPartReference ref = page.getActivePartReference();
		page.setPartState(ref, IStackPresentationSite.STATE_RESTORED);
	}
}
