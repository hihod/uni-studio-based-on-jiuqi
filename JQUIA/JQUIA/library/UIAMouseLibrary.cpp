#include "stdafx.h"
#include "UIAMouseLibrary.h"
#include "utils\MouseUtils.h"
#include "utils\KeyboardUtils.h"

const int MOUSE_CLICKTYPE_SINGLE = 0;
const int MOUSE_CLICKTYPE_DOUBLE = 1;
const int MOUSE_CLICKTYPE_DOWN = 2;
const int MOUSE_CLICKTYPE_UP = 3;

UIAMouseLibrary::UIAMouseLibrary(UIAObj *uiaObj) {
	this->uiaObj = uiaObj;
}

void UIAMouseLibrary::MouseMove(POINT pt) {
	SendMouseInput(MOUSEEVENTF_ABSOLUTE | MOUSEEVENTF_MOVE, pt.x, pt.y);
}

void UIAMouseLibrary::MouseClick(POINT pt, int clickType, int mouseKey, vector<INT> maskKeys) {
	//按下组合键
	for (int i = 0; i < maskKeys.size(); i++)
		SendKeyboardInput(maskKeys[i], 0);
	long x = pt.x;
	long y = pt.y;
	//单击
	if (clickType == MOUSE_CLICKTYPE_SINGLE) {
		if (mouseKey == VK_LBUTTON) {
			SendMouseInput(MOUSEEVENTF_LEFTDOWN | MOUSEEVENTF_LEFTUP, x, y);
			//SendMouseInput(MOUSEEVENTF_LEFTUP);
		}
		else if (mouseKey == VK_RBUTTON) {
			SendMouseInput(MOUSEEVENTF_RIGHTDOWN | MOUSEEVENTF_RIGHTUP, x, y);
			//SendMouseInput(MOUSEEVENTF_RIGHTUP);
		}
	}
	//双击
	else if (clickType == MOUSE_CLICKTYPE_DOUBLE) {
		if (mouseKey == VK_LBUTTON) {
			SendMouseInput(MOUSEEVENTF_LEFTDOWN | MOUSEEVENTF_LEFTUP, x, y);
			//SendMouseInput(MOUSEEVENTF_LEFTUP);
			Sleep(100);
			SendMouseInput(MOUSEEVENTF_LEFTDOWN | MOUSEEVENTF_LEFTUP, x, y);
			//SendMouseInput(MOUSEEVENTF_LEFTUP);
		}
		else if (mouseKey == VK_RBUTTON) {
			SendMouseInput(MOUSEEVENTF_RIGHTDOWN | MOUSEEVENTF_RIGHTUP, x, y);
			//SendMouseInput(MOUSEEVENTF_RIGHTUP);
			Sleep(100);
			SendMouseInput(MOUSEEVENTF_RIGHTDOWN | MOUSEEVENTF_RIGHTUP, x, y);
			//SendMouseInput(MOUSEEVENTF_RIGHTUP);
		}
	}
	//按下
	else if (clickType == MOUSE_CLICKTYPE_DOWN) {
		if (mouseKey == VK_LBUTTON)
			SendMouseInput(MOUSEEVENTF_LEFTDOWN, x, y);
		else if (mouseKey == VK_RBUTTON)
			SendMouseInput(MOUSEEVENTF_RIGHTDOWN, x, y);
	}
	//抬起
	else if (clickType == MOUSE_CLICKTYPE_UP) {
		if (mouseKey == VK_LBUTTON)
			SendMouseInput(MOUSEEVENTF_LEFTUP, x, y);
		else if (mouseKey == VK_RBUTTON)
			SendMouseInput(MOUSEEVENTF_RIGHTUP, x, y);
	}
	//未知
	else {
		char msg[64];
		sprintf_s(msg, 64, "未知的点击类型：%d", clickType);
		throw string(msg);
	}

	//抬起组合键
	for (int i = 0; i < maskKeys.size(); i++)
		SendKeyboardInput(maskKeys[i], KEYEVENTF_KEYUP);
}

void UIAMouseLibrary::SimulateClick(LONG64 id) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

	IUIAutomationInvokePattern *uiaInvokePattern;
	HRESULT hResult = uiElement->GetCurrentPatternAs(UIA_InvokePatternId, IID_PPV_ARGS(&uiaInvokePattern));
	if (FAILED(hResult) || !uiaInvokePattern)
		throw string("获取Pattern失败：UIA_InvokePatternId");

	hResult = uiaInvokePattern->Invoke();
	if (FAILED(hResult))
		throw string("执行Invoke失败");

	uiaInvokePattern->Release();
	uiaInvokePattern = NULL;
}

void UIAMouseLibrary::MessageClick(LONG64 id, POINT pt, int clickType, int mouseKey, vector<INT> maskKeys) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

	UIA_HWND uiaHWnd = 0;
	HRESULT hResult = uiElement->get_CurrentNativeWindowHandle(&uiaHWnd);
	if (FAILED(hResult) || !uiaHWnd)
		throw string("获取元素窗体句柄失败");

	HWND hWnd = (HWND)uiaHWnd;
	WPARAM wParam = 0;
	LPARAM lParam = pt.y << 16 | pt.x;	//LPARAM格式：y << 16) | x

	//单击
	if (clickType == MOUSE_CLICKTYPE_SINGLE) {
		if (mouseKey == VK_LBUTTON) {
			wParam = 1;
			PostMessage(hWnd, WM_LBUTTONDOWN, wParam, lParam);
			wParam = 0;
			PostMessage(hWnd, WM_LBUTTONUP, wParam, lParam);
		}
		else if (mouseKey == VK_RBUTTON) {
			SendMessage(hWnd, WM_RBUTTONDOWN, wParam, lParam);
			SendMessage(hWnd, WM_RBUTTONUP, wParam, lParam);
		}
	}
	//双击
	else if (clickType == MOUSE_CLICKTYPE_DOUBLE) {
		if (mouseKey == VK_LBUTTON)
			SendMessage(hWnd, WM_LBUTTONDBLCLK, wParam, lParam);
		else if (mouseKey == VK_RBUTTON)
			SendMessage(hWnd, WM_RBUTTONDBLCLK, wParam, lParam);
	}
	//按下
	else if (clickType == MOUSE_CLICKTYPE_DOWN) {
		if (mouseKey == VK_LBUTTON)
			SendMessage(hWnd, WM_LBUTTONDOWN, wParam, lParam);
		else if (mouseKey == VK_RBUTTON)
			SendMessage(hWnd, WM_RBUTTONDOWN, wParam, lParam);
	}
	//抬起
	else if (clickType == MOUSE_CLICKTYPE_UP) {
		if (mouseKey == VK_LBUTTON)
			SendMessage(hWnd, WM_LBUTTONUP, wParam, lParam);
		else if (mouseKey == VK_RBUTTON)
			SendMessage(hWnd, WM_RBUTTONUP, wParam, lParam);
	}
	//未知
	else {
		char msg[64];
		sprintf_s(msg, 64, "未知的点击类型：%d", clickType);
		throw string(msg);
	}
}