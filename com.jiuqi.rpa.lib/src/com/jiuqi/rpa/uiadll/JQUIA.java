package com.jiuqi.rpa.uiadll;

import java.util.List;

import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.lib.Rect;
import com.jiuqi.rpa.lib.find.BrowserInfo;
import com.jiuqi.rpa.lib.find.UIARect;
import com.jiuqi.rpa.lib.picker.UIAPickerCallback;

/**
 * JNI��������
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class JQUIA {
	static {
		String rpaRoamingPath = (String) System.getenv().get("APPDATA") + System.getProperty("file.separator") + "RPA";
		System.load(rpaRoamingPath + System.getProperty("file.separator") + "JQUIA.dll");
	}

	/**
	 * ��ʼ����
	 */
	public static final native void _initialize();
	
	/**
	 * ��ֹ����
	 */
	public static final native void _finalize();
	
	/**
	 * �ͷ�ָ����ʶ��UI����
	 * 
	 * @param id UI�����ʶ
	 * @throws JQUIAException
	 */
	public static final native void releaseById(long id) throws JQUIAException;
	
	/**
	 * �ͷ�ָ����ʶ��UI���󼯺�
	 * 
	 * @param ids UI�����ʶ����
	 * @throws JQUIAException
	 */
	public static final native void releaseByIds(long[] ids) throws JQUIAException;
	
	
	
	/*--------------------------------------------------------------------------------*
	 *----- ���� start -----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * ����ָ�����ȡUIԪ�ر�ʶ
	 * 
	 * @param point ָ���ĵ�
	 * @return ����UIԪ�ر�ʶ
	 * @throws JQUIAException
	 */
	public static final native long find_get(Point point) throws JQUIAException;
	
	/**
	 * ����ָ�����ȡ����
	 * 
	 * @param point ָ���ĵ�
	 * @return ���ش����ʶ
	 * @throws JQUIAException
	 */
	public static final native long find_getWindow(Point point) throws JQUIAException;
	
	/**
	 * ����·����ȡ����
	 * 
	 * @param path ����������Ϣ��·��
	 * @return ���ش����ʶ
	 * @throws JQUIAException
	 */
	public static final native long find_getWindowByPath(String path) throws JQUIAException;
	
	/**
	 * ����Ԫ�ػ�ȡ����
	 * 
	 * @param id ĳ������Ԫ��id
	 * @return ���ش����ʶ
	 * @throws JQUIAException
	 */
	public static final native long find_getWindowByElement(long id) throws JQUIAException;
	
	/**
	 * �����������Ϣ��ȡ����
	 * 
	 * @param info ����URL��title��type���������Ϣ
	 * @return ���ش����ʶ
	 * @throws JQUIAException
	 */
	public static final native long find_getWindowByBrowserInfo(BrowserInfo info) throws JQUIAException;
	/**
	 * ��ȡָ����UIԪ�ص����
	 * 
	 * @param point ָ���ĵ�
	 * @param rect ������Ϣ����Ϊ��������
	 * @throws JQUIAException
	 */
	public static final native void find_getRect(Point point, UIARect rect) throws JQUIAException;
	
	/**
	 * ��ȡָ���㴰������
	 * 
	 * @param point ָ���ĵ�
	 * @param rect ������Ϣ����Ϊ��������
	 * @throws JQUIAException
	 */
	public static final native void find_getWindowRect(Point point, UIARect rect) throws JQUIAException;
	
	/**
	 * ��ȡָ����Ϣ��������ͻ������
	 * 
	 * @param browserInfo ָ����Ϣ
	 * @param rect ������Ϣ����Ϊ��������
	 * @throws JQUIAException
	 */
	public static final native void find_getBrowserCientRect(BrowserInfo browserInfo, UIARect rect) throws JQUIAException;
	
	/**
	 * �ж�ָ��·����Ԫ���Ƿ����
	 * 
	 * @param path ·��
	 * @return ���ڷ���true�����򷵻�false
	 * @throws JQUIAException
	 */
	public static final native boolean find_exists(String path) throws JQUIAException;
	
	/**
	 * ��ȡָ��Ԫ�ص�·��
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @return ����UIԪ�ص�·��
	 * @throws JQUIAException
	 */
	public static final native String find_getPath(long id) throws JQUIAException;
	
	/**
	 * ��ȡָ����Ԫ�ص��������Ϣ
	 * 
	 * @param point ָ���ĵ�
	 * @param browserInfo �������Ϣ
	 * @throws JQUIAException
	 */
	public static final native void find_getBrowserInfo(Point point, BrowserInfo browserInfo) throws JQUIAException;
	
	/**
	 * ��·����ȡ��һ������������UIԪ��
	 * 
	 * @param path ·��
	 * @return �����ҵ���UIԪ��
	 * @throws JQUIAException
	 */
	public static final native long find_findFirst(String path) throws JQUIAException;
	
	/**
	 * ��·����ȡ��һ������������UIԪ��
	 * 
	 * @param path ·��
	 * @param runtimeIdList ����ʱ��ʶ�б�
	 * @return �����ҵ���UIԪ��
	 * @throws JQUIAException
	 */
	public static final native long find_findFirstEx(String path, List<String> runtimeIdList) throws JQUIAException;
	
	/**
	 * ��·���������з���������UIԪ��
	 * 
	 * @param path ·��
	 * @return �����ҵ���UIԪ�ؼ���
	 * @throws JQUIAException
	 */
	public static final native long[] find_findAll(String path) throws JQUIAException;
	
	/*----- ���� end -------------------------------------------------------------------*/

	
	
	/*--------------------------------------------------------------------------------*
	 *----- ���� start -----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * ��ȡ���ڵ�UIԪ��
	 * 
	 * @return ���ظ��ڵ�UIԪ��
	 * @throws JQUIAException
	 */
	public static final native long tree_getRoot() throws JQUIAException;
	
	/**
	 * ��ȡָ��UIԪ�ص���UIԪ�ؼ���
	 * 
	 * @param id UIԪ�صı�ʶ��
	 * @return ������UIԪ�ؼ���
	 * @throws JQUIAException
	 */
	public static final native long[] tree_getChildren(long id) throws JQUIAException;
	
	/**
	 * ��ȡָ��UIԪ�صĸ�UIԪ��
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @return ���ظ�UIԪ��
	 * @throws JQUIAException
	 */
	public static final native long tree_getParent(long id) throws JQUIAException;
	
	/**
	 * ��ȡָ��UIԪ�ص��ı�
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @return ����UIԪ�ص��ı�
	 * @throws JQUIAException
	 */
	public static final native String tree_getText(long id) throws JQUIAException;
	
	/**
	 * ��ȡָ��UIԪ�ص����Լ���
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @return ����UIԪ�ص����Լ���
	 * @throws JQUIAException
	 */
	public static final native String tree_getProperties(long id) throws JQUIAException;

	/*----- ���� end -------------------------------------------------------------------*/
	


	/*--------------------------------------------------------------------------------*
	 *----- �ؼ� start -----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * ��ȡָ��UIԪ�ص����
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @param rect ������Ϣ����Ϊ��������
	 * @throws JQUIAException
	 */
	public static final native void control_getRect(long id, Rect rect) throws JQUIAException;
	
	/**
	 * ��ȡָ��UIԪ�ص�����ֵ
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @param attrName ������
	 * @return ��������ֵ
	 * @throws JQUIAException
	 */
	public static final native String control_getAttributeValue(long id, String attrName) throws JQUIAException;
	
	/**
	 * �������ɼ�����
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @throws JQUIAException
	 */
	public static final native void control_ScrollIntoView(long id) throws JQUIAException;
	
	/**
	 * ��ָ��UIԪ�������ý���
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @throws JQUIAException
	 */
	public static final native void control_setFocus(long id) throws JQUIAException;
	
	/**
	 * ָ��UIԪ���Ƿ��ѹ�ѡ
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @return �����Ƿ��ѹ�ѡ
	 * @throws JQUIAException
	 */
	public static final native boolean control_isChecked(long id) throws JQUIAException;
	
	/**
	 * ��ѡ��ȡ����ѡ
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @param checked �Ƿ�ѡ
	 * @return �����Ƿ��ѹ�ѡ
	 * @throws JQUIAException
	 */
	public static final native void control_setChecked(long id, boolean checked) throws JQUIAException;
	
	/**
	 * ��ȡָ��UIԪ�صĹ�ѡ״̬
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @return ���ع�ѡ״̬
	 * @throws JQUIAException
	 * @see CheckState
	 */
	public static final native int control_getCheckState(long id) throws JQUIAException;
	
	/**
	 * ��ȡָ��UIԪ�ص��ı�
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @return ����UIԪ�ص��ı�
	 * @throws JQUIAException
	 */
	public static final native String control_getText(long id) throws JQUIAException;
	
	/**
	 * Ϊָ��UIԪ�������ı�
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @param text Ҫ���õ��ı�
	 * @throws JQUIAException
	 */
	public static final native void control_setText(long id, String text) throws JQUIAException;
	
	/**
	 * �ж�ָ��UIԪ���Ƿ�Ϊ����ؼ�
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @return ����ؼ�����true�����򷵻�false
	 * @throws JQUIAException
	 */
	public static final native boolean control_isPassword(long id) throws JQUIAException;
	
	/**
	 * �ж�ָ��UIԪ���Ƿ����
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @return ���÷���true�����򷵻�false
	 * @throws JQUIAException
	 */
	public static final native boolean control_enable(long id) throws JQUIAException;
	
	/**
	 * �ж�ָ��UIԪ���Ƿ�ɼ�
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @return �ɼ�����true�����򷵻�false
	 * @throws JQUIAException
	 */
	public static final native boolean control_visible(long id) throws JQUIAException;
	
	/**
	 * �ж�ָ��UIԪ���Ƿ�Ϊ���ؼ�
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @return ���ؼ�����true�����򷵻�false
	 * @throws JQUIAException
	 */
	public static final native boolean control_isTable(long id) throws JQUIAException;
	
	/**
	 * ���ָ��UIԪ�ص�ѡ��״̬
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @throws JQUIAException
	 */
	public static final native void control_clearSelection(long id) throws JQUIAException;
	
	/**
	 * Ϊָ��UIԪ��ѡ��ָ����Ŀ����
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @param items ��Ŀ����
	 * @throws JQUIAException
	 */
	public static final native void control_selectItems(long id, String[] items) throws JQUIAException;
	
	/**
	 * ��ȡ��ҳ����
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @param columns ��·��
	 * @return ���ص�ҳ����
	 * @throws JQUIAException
	 */
	public static final native String control_getPageData(long id, String[] columns) throws JQUIAException;
	
	/*----- �ؼ� end -------------------------------------------------------------------*/
	

	
	/*--------------------------------------------------------------------------------*
	 *----- Ӧ�� start -----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * ��Ӧ��
	 * 
	 * @param applicationPath Ӧ��·��
	 * @param args ���������ַ���
	 * @return ����Ӧ�õĽ���id
	 * @throws JQUIAException
	 */
	public static final native long application_startProcess(String applicationPath, String args) throws JQUIAException;
	
	/**
	 * ���ݽ���Id��ȡ����Ԫ��
	 * 
	 * @param pId ����Id
	 * @return ����Ӧ��������Ԫ��
	 * @throws JQUIAException
	 */
	public static final native long application_getApplicationWindow(long pId) throws JQUIAException;
	
	/**
	 * �ر�Ӧ��
	 * 
	 * @param id Ӧ�õ������ڱ�ʶ
	 * @throws JQUIAException
	 */
	public static final native void application_closeApplication(long id) throws JQUIAException;
	
	/**
	 * �������������߳�
	 * 
	 * @param processName ������
	 * @return �����ҵ����߳�ID����
	 * @throws JQUIAException
	 */
	public static final native long[] application_findProcess(String processName) throws JQUIAException;
	
	/**
	 * ���ݽ���ID��������
	 * 
	 * @param id ����ID
	 * @throws JQUIAException
	 */
	public static final native void application_killProcess(long id) throws JQUIAException;
	
	/*----- Ӧ�� end -------------------------------------------------------------------*/
	
	
	
	/*--------------------------------------------------------------------------------*
	 *----- ��� start -----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * ����ƶ�
	 * 
	 * @param point �ƶ����ĵ�
	 * @throws JQUIAException
	 */
	public static final native void mouse_mouseMove(Point point) throws JQUIAException;
	
	/**
	 * �����
	 * 
	 * @param point ������ĵ� 
	 * @param clickType �������
	 * @param mousekey ����ֵ
	 * @param maskkeys ��ϼ�
	 * @throws JQUIAException
	 */
	public static final native void mouse_mouseClick(Point point, int clickType, int mousekey, int[] maskkeys) throws JQUIAException;
	
	/**
	 * ģ����
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @throws JQUIAException
	 */
	public static final native void mouse_simulateClick(long id) throws JQUIAException;
	
	/**
	 * ��Ϣ���
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @param point ������ĵ�
	 * @param clickType �������
	 * @param mousekey ����ֵ
	 * @param maskkeys ��ϼ�
	 * @throws JQUIAException
	 */
	public static final native void mouse_messageClick(long id, Point point, int clickType, int mousekey, int[] maskkeys) throws JQUIAException;
	
	/*----- ��� end -------------------------------------------------------------------*/
	

	
	/*--------------------------------------------------------------------------------*
	 *----- ���� start -----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * ���ָ��UIԪ�ص��ı�
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @throws JQUIAException
	 */
	public static final native void keyboard_clearText(long id) throws JQUIAException;
	
	/**
	 * ��ָ��UIԪ����¼���ı�
	 * 
	 * @param text Ҫ¼����ı�
	 * @throws JQUIAException
	 */
	public static final native void keyboard_typeText(String text) throws JQUIAException;
	
	/**
	 * ģ�������ı�
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @param text Ҫ¼����ı�
	 * @throws JQUIAException
	 */
	public static final native void keyboard_simulateTypeText(long id, String text) throws JQUIAException;
	
	/**
	 * �����ȼ�
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @param hotkey �ȼ���ֵ
	 * @param maskkeys ��ϼ�
	 * @throws JQUIAException
	 */
	public static final native void keyboard_sendHotKey(int hotkey, int[] maskkeys) throws JQUIAException;
	
	/**
	 * ������Ϣ�ȼ�
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @param hotkey �ȼ���ֵ
	 * @param maskkeys ��ϼ�
	 * @throws JQUIAException
	 */
	public static final native void keyboard_sendMessageHotkey(long id, int hotkey, int[] maskkeys) throws JQUIAException;

	/*----- ���� end -------------------------------------------------------------------*/
	
	

	/*--------------------------------------------------------------------------------*
	 *----- ���� start -----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * ���ô���״̬
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @param ws ����״̬
	 * @throws JQUIAException
	 */
	public static final native void window_setWindowState(long id, int ws) throws JQUIAException;
	
	/**
	 * �ƶ�����
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @param rect Ŀ��λ�����С
	 * @throws JQUIAException
	 */
	public static final native void window_moveWindow(long id, Rect rect) throws JQUIAException;
	
	/**
	 * �رմ���
	 * 
	 * @param id UIԪ�صı�ʶ
	 * @throws JQUIAException
	 */
	public static final native void window_closeWindow(long id) throws JQUIAException;

	/*----- ���� end -------------------------------------------------------------------*/
	

	
	/*--------------------------------------------------------------------------------*
	 *----- �Ի��� start ----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * ¼��Ի���
	 * 
	 * @param title ����
	 * @param label ��ǩ
	 * @param items ��Ŀ
	 * @param isPassword �Ƿ�����
	 * @return ����¼���ֵ
	 * @throws JQUIAException
	 */
	public static final native String dialog_inputDialog(String title, String label, String[] items, boolean isPassword) throws JQUIAException;
	
	/**
	 * ��Ϣ�Ի���
	 * 
	 * @param title ����
	 * @param message ��Ϣ
	 * @param btnSuite ��ť���
	 * @return ���ص����ť���ı�
	 * @throws JQUIAException
	 */
	public static final native String dialog_messageDialog(String title, String message, long btnSuite) throws JQUIAException;
	
	/**
	 * �ļ��Ի���
	 * 
	 * @param initialDir Ĭ��·��
	 * @param filters ����������
	 * @return ����ѡ����ļ�·��
	 * @throws JQUIAException
	 */
	public static final native String dialog_fileDialog(String initialDir, String filters) throws JQUIAException;
	
	/**
	 * Ŀ¼�Ի���
	 * 
	 * @param initialDir Ĭ��·��
	 * @return ����Ŀ¼·��
	 * @throws JQUIAException
	 */
	public static final native String dialog_directoryDialog(String initialDir) throws JQUIAException;
	
	/*----- �Ի��� end -------------------------------------------------------------------*/
	
	
	
	/*--------------------------------------------------------------------------------*
	 *----- ͼƬ start -----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * ִ�н���
	 * 
	 * @param rect �������򷽿�
	 * @throws JQUIAException
	 */
	public static final native byte[] image_doScreenShot(Rect rect) throws JQUIAException;

	/*----- ͼƬ end -------------------------------------------------------------------*/
	

	
	/*--------------------------------------------------------------------------------*
	 *----- ���� start -----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * ��ʼ����
	 * 
	 * @param rect ����
	 * @param color ����ı߿�ɫ����ʽ#FFFFFF
	 * @throws JQUIAException
	 */
	public static final native void drawer_startDraw(Rect rect, String color) throws JQUIAException;
	
	/**
	 * ��������
	 * 
	 * @throws JQUIAException
	 */
	public static final native void drawer_endDraw() throws JQUIAException;
	
	/*----- ���� end -------------------------------------------------------------------*/
	
	

	/*--------------------------------------------------------------------------------*
	 *----- ѡ���� start ----------------------------------------------------------------*
	 *--------------------------------------------------------------------------------*/
	
	/**
	 * ��ʼѡȡ
	 * 
	 * @param callback �ص�����
	 * @throws JQUIAException
	 */
	public static final native void picker_startPick(UIAPickerCallback callback) throws JQUIAException;
	
	/**
	 * ����ѡȡ
	 * @throws JQUIAException
	 */
	public static final native void picker_endPick() throws JQUIAException;

	
	/*----- ѡ���� end -------------------------------------------------------------------*/
}
