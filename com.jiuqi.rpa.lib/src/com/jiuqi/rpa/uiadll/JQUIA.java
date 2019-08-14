package com.jiuqi.rpa.uiadll;

import java.util.List;

import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.lib.Rect;
import com.jiuqi.rpa.lib.find.BrowserInfo;
import com.jiuqi.rpa.lib.find.UIARect;
import com.jiuqi.rpa.lib.picker.UIAPickerCallback;

/**
 * JNI方法声明
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class JQUIA {
	static {
		String rpaRoamingPath = (String) System.getenv().get("APPDATA") + System.getProperty("file.separator") + "RPA";
		System.load(rpaRoamingPath + System.getProperty("file.separator") + "JQUIA.dll");
	}

	/**
	 * 初始处理
	 */
	public static final native void _initialize();
	
	/**
	 * 终止处理
	 */
	public static final native void _finalize();
	
	/**
	 * 释放指定标识的UI对象
	 * 
	 * @param id UI对象标识
	 * @throws JQUIAException
	 */
	public static final native void releaseById(long id) throws JQUIAException;
	
	/**
	 * 释放指定标识的UI对象集合
	 * 
	 * @param ids UI对象标识集合
	 * @throws JQUIAException
	 */
	public static final native void releaseByIds(long[] ids) throws JQUIAException;
	
	
	
	/*--------------------------------------------------------------------------------*
	 *----- 查找 start -----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * 根据指定点获取UI元素标识
	 * 
	 * @param point 指定的点
	 * @return 返回UI元素标识
	 * @throws JQUIAException
	 */
	public static final native long find_get(Point point) throws JQUIAException;
	
	/**
	 * 根据指定点获取窗体
	 * 
	 * @param point 指定的点
	 * @return 返回窗体标识
	 * @throws JQUIAException
	 */
	public static final native long find_getWindow(Point point) throws JQUIAException;
	
	/**
	 * 根据路径获取窗体
	 * 
	 * @param path 包含窗体信息的路径
	 * @return 返回窗体标识
	 * @throws JQUIAException
	 */
	public static final native long find_getWindowByPath(String path) throws JQUIAException;
	
	/**
	 * 根据元素获取窗体
	 * 
	 * @param id 某窗体内元素id
	 * @return 返回窗体标识
	 * @throws JQUIAException
	 */
	public static final native long find_getWindowByElement(long id) throws JQUIAException;
	
	/**
	 * 根据浏览器信息获取窗体
	 * 
	 * @param info 包含URL、title和type的浏览器信息
	 * @return 返回窗体标识
	 * @throws JQUIAException
	 */
	public static final native long find_getWindowByBrowserInfo(BrowserInfo info) throws JQUIAException;
	/**
	 * 获取指定点UI元素的外框
	 * 
	 * @param point 指定的点
	 * @param rect 方框信息，此为传出参数
	 * @throws JQUIAException
	 */
	public static final native void find_getRect(Point point, UIARect rect) throws JQUIAException;
	
	/**
	 * 获取指定点窗体的外框
	 * 
	 * @param point 指定的点
	 * @param rect 方框信息，此为传出参数
	 * @throws JQUIAException
	 */
	public static final native void find_getWindowRect(Point point, UIARect rect) throws JQUIAException;
	
	/**
	 * 获取指定信息的浏览器客户区外框
	 * 
	 * @param browserInfo 指定信息
	 * @param rect 方框信息，此为传出参数
	 * @throws JQUIAException
	 */
	public static final native void find_getBrowserCientRect(BrowserInfo browserInfo, UIARect rect) throws JQUIAException;
	
	/**
	 * 判断指定路径的元素是否存在
	 * 
	 * @param path 路径
	 * @return 存在返回true，否则返回false
	 * @throws JQUIAException
	 */
	public static final native boolean find_exists(String path) throws JQUIAException;
	
	/**
	 * 获取指定元素的路径
	 * 
	 * @param id UI元素的标识
	 * @return 返回UI元素的路径
	 * @throws JQUIAException
	 */
	public static final native String find_getPath(long id) throws JQUIAException;
	
	/**
	 * 获取指定点元素的浏览器信息
	 * 
	 * @param point 指定的点
	 * @param browserInfo 浏览器信息
	 * @throws JQUIAException
	 */
	public static final native void find_getBrowserInfo(Point point, BrowserInfo browserInfo) throws JQUIAException;
	
	/**
	 * 按路径获取第一个符合条件的UI元素
	 * 
	 * @param path 路径
	 * @return 返回找到的UI元素
	 * @throws JQUIAException
	 */
	public static final native long find_findFirst(String path) throws JQUIAException;
	
	/**
	 * 按路径获取第一个符合条件的UI元素
	 * 
	 * @param path 路径
	 * @param runtimeIdList 运行时标识列表
	 * @return 返回找到的UI元素
	 * @throws JQUIAException
	 */
	public static final native long find_findFirstEx(String path, List<String> runtimeIdList) throws JQUIAException;
	
	/**
	 * 按路径查找所有符合条件的UI元素
	 * 
	 * @param path 路径
	 * @return 返回找到的UI元素集合
	 * @throws JQUIAException
	 */
	public static final native long[] find_findAll(String path) throws JQUIAException;
	
	/*----- 查找 end -------------------------------------------------------------------*/

	
	
	/*--------------------------------------------------------------------------------*
	 *----- 树形 start -----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * 获取根节点UI元素
	 * 
	 * @return 返回根节点UI元素
	 * @throws JQUIAException
	 */
	public static final native long tree_getRoot() throws JQUIAException;
	
	/**
	 * 获取指定UI元素的子UI元素集合
	 * 
	 * @param id UI元素的标识素
	 * @return 返回子UI元素集合
	 * @throws JQUIAException
	 */
	public static final native long[] tree_getChildren(long id) throws JQUIAException;
	
	/**
	 * 获取指定UI元素的父UI元素
	 * 
	 * @param id UI元素的标识
	 * @return 返回父UI元素
	 * @throws JQUIAException
	 */
	public static final native long tree_getParent(long id) throws JQUIAException;
	
	/**
	 * 获取指定UI元素的文本
	 * 
	 * @param id UI元素的标识
	 * @return 返回UI元素的文本
	 * @throws JQUIAException
	 */
	public static final native String tree_getText(long id) throws JQUIAException;
	
	/**
	 * 获取指定UI元素的属性集合
	 * 
	 * @param id UI元素的标识
	 * @return 返回UI元素的属性集合
	 * @throws JQUIAException
	 */
	public static final native String tree_getProperties(long id) throws JQUIAException;

	/*----- 树形 end -------------------------------------------------------------------*/
	


	/*--------------------------------------------------------------------------------*
	 *----- 控件 start -----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * 获取指定UI元素的外框
	 * 
	 * @param id UI元素的标识
	 * @param rect 方框信息，此为传出参数
	 * @throws JQUIAException
	 */
	public static final native void control_getRect(long id, Rect rect) throws JQUIAException;
	
	/**
	 * 获取指定UI元素的属性值
	 * 
	 * @param id UI元素的标识
	 * @param attrName 属性名
	 * @return 返回属性值
	 * @throws JQUIAException
	 */
	public static final native String control_getAttributeValue(long id, String attrName) throws JQUIAException;
	
	/**
	 * 滚动到可见区域
	 * 
	 * @param id UI元素的标识
	 * @throws JQUIAException
	 */
	public static final native void control_ScrollIntoView(long id) throws JQUIAException;
	
	/**
	 * 在指定UI元素上设置焦点
	 * 
	 * @param id UI元素的标识
	 * @throws JQUIAException
	 */
	public static final native void control_setFocus(long id) throws JQUIAException;
	
	/**
	 * 指定UI元素是否已勾选
	 * 
	 * @param id UI元素的标识
	 * @return 返回是否已勾选
	 * @throws JQUIAException
	 */
	public static final native boolean control_isChecked(long id) throws JQUIAException;
	
	/**
	 * 勾选或取消勾选
	 * 
	 * @param id UI元素的标识
	 * @param checked 是否勾选
	 * @return 返回是否已勾选
	 * @throws JQUIAException
	 */
	public static final native void control_setChecked(long id, boolean checked) throws JQUIAException;
	
	/**
	 * 获取指定UI元素的勾选状态
	 * 
	 * @param id UI元素的标识
	 * @return 返回勾选状态
	 * @throws JQUIAException
	 * @see CheckState
	 */
	public static final native int control_getCheckState(long id) throws JQUIAException;
	
	/**
	 * 获取指定UI元素的文本
	 * 
	 * @param id UI元素的标识
	 * @return 返回UI元素的文本
	 * @throws JQUIAException
	 */
	public static final native String control_getText(long id) throws JQUIAException;
	
	/**
	 * 为指定UI元素设置文本
	 * 
	 * @param id UI元素的标识
	 * @param text 要设置的文本
	 * @throws JQUIAException
	 */
	public static final native void control_setText(long id, String text) throws JQUIAException;
	
	/**
	 * 判断指定UI元素是否为密码控件
	 * 
	 * @param id UI元素的标识
	 * @return 密码控件返回true，否则返回false
	 * @throws JQUIAException
	 */
	public static final native boolean control_isPassword(long id) throws JQUIAException;
	
	/**
	 * 判断指定UI元素是否可用
	 * 
	 * @param id UI元素的标识
	 * @return 可用返回true，否则返回false
	 * @throws JQUIAException
	 */
	public static final native boolean control_enable(long id) throws JQUIAException;
	
	/**
	 * 判断指定UI元素是否可见
	 * 
	 * @param id UI元素的标识
	 * @return 可见返回true，否则返回false
	 * @throws JQUIAException
	 */
	public static final native boolean control_visible(long id) throws JQUIAException;
	
	/**
	 * 判断指定UI元素是否为表格控件
	 * 
	 * @param id UI元素的标识
	 * @return 表格控件返回true，否则返回false
	 * @throws JQUIAException
	 */
	public static final native boolean control_isTable(long id) throws JQUIAException;
	
	/**
	 * 清除指定UI元素的选中状态
	 * 
	 * @param id UI元素的标识
	 * @throws JQUIAException
	 */
	public static final native void control_clearSelection(long id) throws JQUIAException;
	
	/**
	 * 为指定UI元素选中指定条目集合
	 * 
	 * @param id UI元素的标识
	 * @param items 条目集合
	 * @throws JQUIAException
	 */
	public static final native void control_selectItems(long id, String[] items) throws JQUIAException;
	
	/**
	 * 获取当页数据
	 * 
	 * @param id UI元素的标识
	 * @param columns 列路径
	 * @return 返回当页数据
	 * @throws JQUIAException
	 */
	public static final native String control_getPageData(long id, String[] columns) throws JQUIAException;
	
	/*----- 控件 end -------------------------------------------------------------------*/
	

	
	/*--------------------------------------------------------------------------------*
	 *----- 应用 start -----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * 打开应用
	 * 
	 * @param applicationPath 应用路径
	 * @param args 参数集合字符串
	 * @return 返回应用的进程id
	 * @throws JQUIAException
	 */
	public static final native long application_startProcess(String applicationPath, String args) throws JQUIAException;
	
	/**
	 * 根据进程Id获取窗口元素
	 * 
	 * @param pId 进程Id
	 * @return 返回应用主窗口元素
	 * @throws JQUIAException
	 */
	public static final native long application_getApplicationWindow(long pId) throws JQUIAException;
	
	/**
	 * 关闭应用
	 * 
	 * @param id 应用的主窗口标识
	 * @throws JQUIAException
	 */
	public static final native void application_closeApplication(long id) throws JQUIAException;
	
	/**
	 * 按进程名查找线程
	 * 
	 * @param processName 进程名
	 * @return 返回找到的线程ID集合
	 * @throws JQUIAException
	 */
	public static final native long[] application_findProcess(String processName) throws JQUIAException;
	
	/**
	 * 根据进程ID结束进程
	 * 
	 * @param id 进程ID
	 * @throws JQUIAException
	 */
	public static final native void application_killProcess(long id) throws JQUIAException;
	
	/*----- 应用 end -------------------------------------------------------------------*/
	
	
	
	/*--------------------------------------------------------------------------------*
	 *----- 鼠标 start -----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * 鼠标移动
	 * 
	 * @param point 移动到的点
	 * @throws JQUIAException
	 */
	public static final native void mouse_mouseMove(Point point) throws JQUIAException;
	
	/**
	 * 鼠标点击
	 * 
	 * @param point 鼠标点击的点 
	 * @param clickType 点击类型
	 * @param mousekey 鼠标键值
	 * @param maskkeys 组合键
	 * @throws JQUIAException
	 */
	public static final native void mouse_mouseClick(Point point, int clickType, int mousekey, int[] maskkeys) throws JQUIAException;
	
	/**
	 * 模拟点击
	 * 
	 * @param id UI元素的标识
	 * @throws JQUIAException
	 */
	public static final native void mouse_simulateClick(long id) throws JQUIAException;
	
	/**
	 * 消息点击
	 * 
	 * @param id UI元素的标识
	 * @param point 鼠标点击的点
	 * @param clickType 点击类型
	 * @param mousekey 鼠标键值
	 * @param maskkeys 组合键
	 * @throws JQUIAException
	 */
	public static final native void mouse_messageClick(long id, Point point, int clickType, int mousekey, int[] maskkeys) throws JQUIAException;
	
	/*----- 鼠标 end -------------------------------------------------------------------*/
	

	
	/*--------------------------------------------------------------------------------*
	 *----- 键盘 start -----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * 清空指定UI元素的文本
	 * 
	 * @param id UI元素的标识
	 * @throws JQUIAException
	 */
	public static final native void keyboard_clearText(long id) throws JQUIAException;
	
	/**
	 * 在指定UI元素上录入文本
	 * 
	 * @param text 要录入的文本
	 * @throws JQUIAException
	 */
	public static final native void keyboard_typeText(String text) throws JQUIAException;
	
	/**
	 * 模拟输入文本
	 * 
	 * @param id UI元素的标识
	 * @param text 要录入的文本
	 * @throws JQUIAException
	 */
	public static final native void keyboard_simulateTypeText(long id, String text) throws JQUIAException;
	
	/**
	 * 发送热键
	 * 
	 * @param id UI元素的标识
	 * @param hotkey 热键键值
	 * @param maskkeys 组合键
	 * @throws JQUIAException
	 */
	public static final native void keyboard_sendHotKey(int hotkey, int[] maskkeys) throws JQUIAException;
	
	/**
	 * 发送消息热键
	 * 
	 * @param id UI元素的标识
	 * @param hotkey 热键键值
	 * @param maskkeys 组合键
	 * @throws JQUIAException
	 */
	public static final native void keyboard_sendMessageHotkey(long id, int hotkey, int[] maskkeys) throws JQUIAException;

	/*----- 键盘 end -------------------------------------------------------------------*/
	
	

	/*--------------------------------------------------------------------------------*
	 *----- 窗体 start -----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * 设置窗体状态
	 * 
	 * @param id UI元素的标识
	 * @param ws 窗体状态
	 * @throws JQUIAException
	 */
	public static final native void window_setWindowState(long id, int ws) throws JQUIAException;
	
	/**
	 * 移动窗体
	 * 
	 * @param id UI元素的标识
	 * @param rect 目标位置与大小
	 * @throws JQUIAException
	 */
	public static final native void window_moveWindow(long id, Rect rect) throws JQUIAException;
	
	/**
	 * 关闭窗体
	 * 
	 * @param id UI元素的标识
	 * @throws JQUIAException
	 */
	public static final native void window_closeWindow(long id) throws JQUIAException;

	/*----- 窗体 end -------------------------------------------------------------------*/
	

	
	/*--------------------------------------------------------------------------------*
	 *----- 对话框 start ----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * 录入对话框
	 * 
	 * @param title 标题
	 * @param label 标签
	 * @param items 条目
	 * @param isPassword 是否密码
	 * @return 返回录入的值
	 * @throws JQUIAException
	 */
	public static final native String dialog_inputDialog(String title, String label, String[] items, boolean isPassword) throws JQUIAException;
	
	/**
	 * 消息对话框
	 * 
	 * @param title 标题
	 * @param message 消息
	 * @param btnSuite 按钮组合
	 * @return 返回点击按钮的文本
	 * @throws JQUIAException
	 */
	public static final native String dialog_messageDialog(String title, String message, long btnSuite) throws JQUIAException;
	
	/**
	 * 文件对话框
	 * 
	 * @param initialDir 默认路径
	 * @param filters 过滤器集合
	 * @return 返回选择的文件路径
	 * @throws JQUIAException
	 */
	public static final native String dialog_fileDialog(String initialDir, String filters) throws JQUIAException;
	
	/**
	 * 目录对话框
	 * 
	 * @param initialDir 默认路径
	 * @return 返回目录路径
	 * @throws JQUIAException
	 */
	public static final native String dialog_directoryDialog(String initialDir) throws JQUIAException;
	
	/*----- 对话框 end -------------------------------------------------------------------*/
	
	
	
	/*--------------------------------------------------------------------------------*
	 *----- 图片 start -----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * 执行截屏
	 * 
	 * @param rect 截屏区域方框
	 * @throws JQUIAException
	 */
	public static final native byte[] image_doScreenShot(Rect rect) throws JQUIAException;

	/*----- 图片 end -------------------------------------------------------------------*/
	

	
	/*--------------------------------------------------------------------------------*
	 *----- 画笔 start -----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * 开始画框
	 * 
	 * @param rect 方框
	 * @param color 方框的边框色，格式#FFFFFF
	 * @throws JQUIAException
	 */
	public static final native void drawer_startDraw(Rect rect, String color) throws JQUIAException;
	
	/**
	 * 结束画框
	 * 
	 * @throws JQUIAException
	 */
	public static final native void drawer_endDraw() throws JQUIAException;
	
	/*----- 画笔 end -------------------------------------------------------------------*/
	
	

	/*--------------------------------------------------------------------------------*
	 *----- 选择器 start ----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * 开始选取
	 * 
	 * @param callback 回调对象
	 * @throws JQUIAException
	 */
	public static final native void picker_startPick(UIAPickerCallback callback) throws JQUIAException;
	
	/**
	 * 结束选取
	 * @throws JQUIAException
	 */
	public static final native void picker_endPick() throws JQUIAException;

	
	/*----- 选择器 end -------------------------------------------------------------------*/
}
