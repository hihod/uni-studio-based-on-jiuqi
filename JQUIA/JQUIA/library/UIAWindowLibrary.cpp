#include "stdafx.h"
#include <string>
#include "uiautomation.h"
#include "UIAWindowLibrary.h"

UIAWindowLibrary::UIAWindowLibrary(UIAObj *uiaObj) {
	this->uiaObj = uiaObj;
}

void UIAWindowLibrary::SetWindowState(LONG64 id, int ws) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);
	UIA_HWND uiaHWnd = 0;
	HRESULT hResult = uiElement->get_CurrentNativeWindowHandle(&uiaHWnd);
	if (FAILED(hResult) || !uiaHWnd)
		throw string("获取元素的窗口句柄失败");

	HWND hWnd = (HWND)uiaHWnd;
	ShowWindow(hWnd, ws);
}

void UIAWindowLibrary::MoveWindow_(LONG64 id, RECT rect) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);
	UIA_HWND uiaHWnd = 0;
	HRESULT hResult = uiElement->get_CurrentNativeWindowHandle(&uiaHWnd);
	if (FAILED(hResult) || !uiaHWnd)
		throw string("获取元素的窗口句柄失败");

	HWND hWnd = (HWND)uiaHWnd;
	int x = rect.left;
	int y = rect.top;
	int w = rect.right - rect.left;
	int h = rect.bottom - rect.top;
	MoveWindow(hWnd, x, y, w, h, TRUE);
}

void UIAWindowLibrary::CloseWindow(LONG64 id) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);;
	UIA_HWND uiaHWnd = 0;
	HRESULT hResult = uiElement->get_CurrentNativeWindowHandle(&uiaHWnd);
	if (FAILED(hResult) || !uiaHWnd)
		throw string("获取元素的窗口句柄失败");

	HWND hWnd = (HWND)uiaHWnd;
	SendMessage(hWnd, WM_CLOSE, 0, 0);
}