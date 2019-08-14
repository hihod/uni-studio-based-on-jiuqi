package com.jiuqi.rpa.lib.find;

import java.util.List;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.lib.Rect;

/**
 * 查找操作库
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public interface IFindLibary {
	/**
	 * 根据指定位置获取UI元素
	 * 
	 * @param point 指定的位置 
	 * @return 返回UI元素对象
	 * @throws LibraryException
	 */
	IUIElement get(Point point) throws LibraryException;
	
	/**
	 * 根据指定位置获取对应元素的方框
	 * 
	 * @param point 指定的位置
	 * @return 返回UI元素的方框
	 * @throws LibraryException
	 */
	Rect getRect(Point point) throws LibraryException;
	
	/**
	 * 判断指定路径的UI元素是否存在
	 * 
	 * @param path 路径
	 * @return 存在返回true，否则返回false
	 * @throws LibraryException
	 */
	boolean exists(Path path) throws LibraryException;
	
	/**
	 * 获取指定路径的元素，如果存在多个，返回第一个
	 * 
	 * @param path 路径
	 * @return 返回UI元素对象
	 * @throws LibraryException
	 */
	IUIElement findFirst(Path path) throws LibraryException;
	
	/**
	 * 获取指定路径的元素，如果存在多个，返回第一个
	 * 
	 * @param path 路径
	 * @param runtimeIdList 运行时标识列表
	 * @return 返回UI元素对象
	 * @throws LibraryException
	 */
	IUIElement findFirst(Path path, List<String> runtimeIdList) throws LibraryException;
	
	/**
	 * 获取指定路径的元素集合
	 * 
	 * @param path 路径
	 * @return 返回UI元素对象集合
	 * @throws LibraryException
	 */
	IUIElement[] findAll(Path path) throws LibraryException;
	
}
