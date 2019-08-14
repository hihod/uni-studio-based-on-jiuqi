package com.jiuqi.etl.app.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import com.jiuqi.etl.app.ApplicationPlugin;
import com.jiuqi.etl.ui.view.server.ServerView;

/**
 * 类名：新建Action <p>
 * 描述：用于创建新建菜单和工具栏按钮。
 */
public class NewAction extends Action implements ActionFactory.IWorkbenchAction
{
	public static final String ID = "com.jiuqi.etl.new";
	
	private static final String IMG 		= "icons/action/new.gif";
	private static final String IMG_DISABLE = "icons/action/new_disable.gif";
	NewAction newAction = null;
	private IWorkbenchWindow window;
	private MenuManager menuMgr;
	private Menu parentMenu;
	public NewAction(IWorkbenchWindow workbenchWindow)
	{
		super("", AS_DROP_DOWN_MENU);
		this.window = workbenchWindow;
		setId("new");
		setActionDefinitionId(ID);
		setText("新建(&N)");
		setToolTipText("新建");
		setImageDescriptor(ApplicationPlugin.imageDescriptorFromPlugin(IMG));
		setDisabledImageDescriptor(ApplicationPlugin.imageDescriptorFromPlugin(IMG_DISABLE));
		newAction = this;
		menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				updateMenuMgr();
			}
		});
		setMenuCreator(new IMenuCreator() {
			
			//工具栏上支持新建
			public Menu getMenu(Control parent){
				return menuMgr.createContextMenu(parent);
			}
			
			//菜单栏上的支持
			public Menu getMenu(Menu parent){
				parentMenu = new Menu(parent);
				parentMenu.addMenuListener(new MenuListener() {
					
					public void menuShown(MenuEvent e) {
						updateMenuMgr();
						updateMenu();
					}
					
					public void menuHidden(MenuEvent e) {
					}
				});
				return parentMenu;
			}
			
			public void dispose(){
			}
		});
	}
	
	/**
	 * 更新工具栏
	 */
	private void updateMenuMgr(){
		menuMgr.removeAll();
		ServerView view = (ServerView) window.getActivePage().findView(ServerView.VIEW_ID);
		if (view != null) {
			for (IAction action : view.getNewActions()) {
				menuMgr.add(action);
			}
		}
	}
	
	
	/**
	 * 更新菜单
	 */
	private void updateMenu(){
		for(MenuItem item : parentMenu.getItems()){
			item.dispose();
		}
		IContributionItem[] items = menuMgr.getItems();
		for (int i = 0; i < items.length; i++){
			IContributionItem item = items[i];
			IContributionItem newItem = item;
			if (item instanceof ActionContributionItem){
				newItem = new ActionContributionItem(((ActionContributionItem) item)
				        .getAction());
			}
			newItem.fill(parentMenu, -1);
		}
	}
	
	
	public void dispose()
	{
		if (window == null)
			return;
		window = null;
		if (menuMgr != null)
		{
			menuMgr.dispose();
			menuMgr = null;
		}
	}
}
