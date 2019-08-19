package com.jiuqi.rpa.lib.find;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.lib.Rect;
import com.jiuqi.rpa.lib.mouse.MouseClickType;
import com.jiuqi.rpa.lib.window.WindowState;
import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.uiadll.JQUIAException;

/**
 * UIAԪ��
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIAElement implements IUIElement {
	private long id;
	
	/**
	 * ������
	 * 
	 * @param id UIԪ�ر�ʶ
	 */
	public UIAElement(long id) {
		this.id = id;
	}
	
	public long getId() {
		return id;
	}
	
	public void release() throws LibraryException {
		try {
			JQUIA.releaseById(id);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	/**
	 * lpy
	 */
	public UIARect getRect() throws LibraryException {
		try {
			UIARect rect = new UIARect();
			JQUIA.control_getRect(id, rect);
			return rect;
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	/**
	 * lpy
	 */
	public UIARect getRect(DataInputStream inputStream, DataOutputStream outputStream) throws LibraryException {
		throw new LibraryException("�ͻ��� UIAElement ��������ò����� getRect ����");
	}
	
	/**
	 * @author lpy
	 */
	public Path getPath() throws LibraryException {
		try {
			String pathJsonStr = JQUIA.find_getPath(id);
			JSONObject pathJson = new JSONObject(pathJsonStr);
			
			Path path = new Path();
			path.fromJson(pathJson);
			return path;
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		} catch (JSONException e) {
			throw new LibraryException("·��תjsonʧ��", e);
		}
	}
	
	/**
	 * @author lpy
	 */
	public Path getPath(DataInputStream inputStream, DataOutputStream outputStream) throws LibraryException {
		throw new LibraryException("�ͻ��� UIAElement ��������ò����� getPath ����");
	}
	
	public void scrollIntoView() throws LibraryException {
		try {
			JQUIA.control_ScrollIntoView(id);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	public void setFocus() throws LibraryException {
		try {
			JQUIA.control_setFocus(id);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	public boolean isChecked() throws LibraryException {
		try {
			return JQUIA.control_isChecked(id);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public void setChecked(boolean checked) throws LibraryException {
		try {
			JQUIA.control_setChecked(id, checked);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public int getCheckState() throws LibraryException {
		try {
			return JQUIA.control_getCheckState(id);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public String getText() throws LibraryException {
		try {
			return JQUIA.control_getText(id);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public void setText(String text) throws LibraryException {
		try {
			JQUIA.control_setText(id, text);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public boolean isPassword() throws LibraryException {
		try {
			return JQUIA.control_isPassword(id);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public void clearSelection() throws LibraryException {
		try {
			JQUIA.control_clearSelection(id);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public void selectItems(String[] items) throws LibraryException {
		try {
			JQUIA.control_selectItems(id, items);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public void clearText() throws LibraryException {
		try {
			JQUIA.keyboard_clearText(id);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public void simulateTypeText(String text) throws LibraryException {
		try {
			JQUIA.keyboard_simulateTypeText(id, text);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public void sendHotkey(int hotkey, int[] maskkeys) throws LibraryException {
		try {
			setFocus();
			JQUIA.keyboard_sendHotKey(hotkey, maskkeys);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public String getAttributeValue(String attrName) throws LibraryException {
		try {
			return JQUIA.control_getAttributeValue(id, attrName);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public boolean enable() throws LibraryException {
		try {
			return JQUIA.control_enable(id);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public boolean visible() throws LibraryException {
		try {
			return JQUIA.control_visible(id);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	
	public boolean isTable() throws LibraryException {
		try {
			return JQUIA.control_isTable(id);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public String getPageData(Path[] columns) throws LibraryException {
		try {
			String[] pathJsonStrs = new String[columns.length]; 
			for (int i = 0; i < columns.length; i++)
				pathJsonStrs[i] = columns[i].toJson().toString();
			
			return JQUIA.control_getPageData(id, pathJsonStrs);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		} catch (JSONException e) {
			throw new LibraryException("·��תjsonʧ��", e);
		}
	}
	
	/**
	 * ģ����
	 * 
	 * @param point ����ĵ�
	 * @param clickType �������
	 * @param mousekey ����ֵ
	 * @param maskkeys ��ϼ�
	 * @throws LibraryException
	 */
	public void simulateClick() throws LibraryException {
		try {
			JQUIA.mouse_simulateClick(id);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	/**
	 * ��Ϣ���
	 * 
	 * @param point ����ĵ�
	 * @param clickType �������
	 * @param mousekey ����ֵ
	 * @param maskkeys ��ϼ�
	 * @throws LibraryException
	 */
	public void messageClick(Point point, MouseClickType clickType, int mousekey, int[] maskkeys) throws LibraryException {
		try {
			JQUIA.mouse_messageClick(id, point, clickType.value(), mousekey, maskkeys);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	/**
	 * ������Ϣ�ȼ�
	 * 
	 * @param hotkey �ȼ���ֵ
	 * @param maskkeys ��Ͻ�
	 * @throws LibraryException
	 */
	public void sendMessageHotkey(int hotkey, int[] maskkeys) throws LibraryException {
		try {
			JQUIA.keyboard_sendMessageHotkey(id, hotkey, maskkeys);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	/**
	 * ���ô���״̬
	 * 
	 * @param ws ����״̬
	 * @see WindowState
	 * @throws LibraryException
	 */
	public void sendWindowState(WindowState ws) throws LibraryException {
		try {
			JQUIA.window_setWindowState(id, ws.value());
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	/**
	 * �ƶ�����
	 * 
	 * @param rect Ŀ��λ�����С
	 * @throws LibraryException
	 * @see Rect
	 */
	public void moveWindow(Rect rect) throws LibraryException {
		try {
			JQUIA.window_moveWindow(id, rect);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	/**
	 * �رմ���
	 * @throws LibraryException
	 */
	public void closeWindow() throws LibraryException {
		try {
			JQUIA.window_closeWindow(id);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
}
