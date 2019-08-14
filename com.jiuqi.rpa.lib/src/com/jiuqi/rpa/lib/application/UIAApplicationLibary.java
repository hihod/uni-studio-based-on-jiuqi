package com.jiuqi.rpa.lib.application;

import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.uiadll.JQUIAException;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.ContextProvider;
import com.jiuqi.rpa.lib.LibraryException;

/**
 * UIA应用操作库
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIAApplicationLibary extends ContextProvider {
	/**
	 * 构造器
	 * 
	 * @param context 活动上下文
	 */
	public UIAApplicationLibary(Context context) {
		super(context);
	}

	/**
	 * 开始进程
	 * 
	 * @param applicationPath 应用的程序路径
	 * @param args 启动应用时需要传递的参数集合字符串
	 * @return 返回进程Id
	 * @throws LibraryException
	 */
	public long startProcess(String applicationPath, String args) throws LibraryException {
		try {
			return JQUIA.application_startProcess(applicationPath, args);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	/**
	 * 根据进程Id获取窗口元素
	 * 
	 * @param pId 进程Id
	 * @return 返回应用主窗口元素
	 * @throws LibraryException
	 */
	public long getApplicationWindow(long pId) throws LibraryException {
		try {
			return JQUIA.application_getApplicationWindow(pId);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	/**
	 * 查找进程
	 * 
	 * @param processName 进程名
	 * @return 返回进程标识集合
	 * @throws LibraryException
	 */
	public long[] findProcess(String processName) throws LibraryException {
		try {
			return JQUIA.application_findProcess(processName);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	public void closeApplication(Long id) throws LibraryException {
		try {
			JQUIA.application_closeApplication(id);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public void killProcess(Long id) throws LibraryException {
		try {
			JQUIA.application_killProcess(id);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
}
