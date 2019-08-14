package com.jiuqi.etl.rpa.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.jiuqi.etl.engine.controlflow.ControlFlowExecutor;
import com.jiuqi.etl.rpa.execute.RPAControlFlowExecuteListener;
import com.jiuqi.rpa.lib.browser.WebBrowserLibary;
import com.jiuqi.rpa.lib.browser.WebBrowserManager;
import com.jiuqi.rpa.lib.dialog.UIADialogLibrary;
import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.widgets.inputdialog.InputDialogProvider;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.jiuqi.etl.rpa.ui"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private RPAControlFlowExecuteListener listener = new RPAControlFlowExecuteListener();

	/**
	 * The constructor
	 */
	public Activator() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		try {
			String RPA_WORKSPACE = "RPA";
			String FILE_SEPARATOR = System.getProperty("file.separator");
			String dirPath = System.getenv().get("APPDATA") + FILE_SEPARATOR + RPA_WORKSPACE;
			
			File dirFile = new File(dirPath);
			if (!dirFile.exists())
				dirFile.mkdir();
			
			File f = new File(dirPath + FILE_SEPARATOR + "RPA_STARTUP.log");
			if (f.exists())
				f.delete();
			
			PrintWriter pw = new PrintWriter(f);
			// 安装JQUIA.dll，以及各浏览器driver
			pw.write("----RPAInstaller.install()-------\n");
			try {
				RPAInstaller.install();
			}catch(Exception e) {
				e.printStackTrace();
			}
			try {
				// JQUIA初始化
				pw.write("----JQUIA._initialize()-------\n");
				JQUIA._initialize();
			} catch (Exception e) {
				e.printStackTrace(pw);
			}
			
			try {
				// 注册控制流执行监听器
				pw.write("----ControlFlowExecutor.addControlFlowExecuteListener(listener)-------\n");
				listener.setWindowMinimizer(new RPAWindowMinimizer());
				ControlFlowExecutor.addControlFlowExecuteListener(listener);

				// 录入框注册
				pw.write("----UIADialogLibrary.setInputDialogProvider(new InputDialogProvider())-------\n");
				UIADialogLibrary.setInputDialogProvider(new InputDialogProvider());
				
				pw.write("----started-------\n");
			} catch (Exception e) {
				e.printStackTrace(pw);
			} finally {
				pw.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		try {
			// 卸载控制流执行监听器
			ControlFlowExecutor.removeControlFlowExecuteListener(listener);
		} catch (Exception e) {
		}
		try {
			// JQUIA终止
			JQUIA._finalize();
		} catch (Exception e) {
		}
		try {
			// 释放所有WebBrowser
			WebBrowserLibary.cancleTimer();
			WebBrowserManager.getInstance().releaseAll();
		} catch (Exception e) {
		}
		super.stop(context);
	}

	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);
		reg.put(IMAGE_FIELD, imageDescriptorFromPlugin(PLUGIN_ID, IMAGE_FIELD));
		reg.put(IMAGE_AUTOMAPPING, imageDescriptorFromPlugin(PLUGIN_ID, IMAGE_AUTOMAPPING));
		reg.put(IMAGE_CANCELMAPPING, imageDescriptorFromPlugin(PLUGIN_ID, IMAGE_CANCELMAPPING));
		reg.put(IMAGE_NORMAL_WIDGET_HEAD, imageDescriptorFromPlugin(PLUGIN_ID, IMAGE_NORMAL_WIDGET_HEAD));
	}

	public static Activator getDefault() {
		return plugin;
	}

	public static final String IMAGE_CANCELMAPPING = "icons/cancelMapping.gif";
	public static final String IMAGE_FIELD = "icons/field.gif";
	public static final String IMAGE_AUTOMAPPING = "icons/autoMapping.gif";
	public static final String IMAGE_NORMAL_WIDGET_HEAD = "icons/normal_widget_head.jpg";

}
