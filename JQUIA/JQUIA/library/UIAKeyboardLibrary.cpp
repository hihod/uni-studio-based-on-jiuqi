#include "stdafx.h"
#include <cstringt.h>
#include <comdef.h>
#include "UIAKeyboardLibrary.h"
#include "utils\KeyboardUtils.h"

UIAKeyboardLibrary::UIAKeyboardLibrary(UIAObj *uiaObj) {
	this->uiaObj = uiaObj;
}

void UIAKeyboardLibrary::ClearText(LONG64 id) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

	IUnknown *uiaPattern = NULL;
	HRESULT hResult = uiElement->GetCurrentPattern(UIA_ValuePatternId, &uiaPattern);
	if (FAILED(hResult) || !uiaPattern)
		throw string("获取Pattern失败：UIA_ValuePatternId");

	IUIAutomationValuePattern *uiaValuePattern = (IUIAutomationValuePattern *)uiaPattern;
	uiaValuePattern->SetValue(_com_util::ConvertStringToBSTR(""));

	uiaValuePattern->Release();
	uiaValuePattern = NULL;
}

void UIAKeyboardLibrary::TypeText(string text) {
	short vk = 0;
	bool shift = FALSE;

	//按字符执行录入
	USES_CONVERSION;
	wchar_t *data = A2W(text.data());

	int len = static_cast<int>(wcslen(data));
	for (int i = 0; i < len; i++) {
		//ascii字符
		if (data[i] >= 0 && data[i] < 256) {
			vk = VkKeyScanW(data[i]);
			if (vk != -1) {
				if (vk < 0)
					vk = ~vk + 0x1;

				shift = vk >> 8 & 0x1;
				if (GetKeyState(VK_CAPITAL) & 0x1) {
					if (data[i] >= 'a' && data[i] <= 'z' || data[i] >= 'A' && data[i] <= 'Z')
						shift = !shift;
				}
				SendAscii(vk & 0xFF, shift);
			}
			//unicode字符
			else
				SendUnicode(data[i]);
		}
		//unicode字符 测
		else
			SendUnicode(data[i]);
	}
}

void UIAKeyboardLibrary::SendHotKey(int hotKey, vector<INT> maskKeys) {
	//按下组合键
	for (int i = 0; i < maskKeys.size(); i++)
		SendKeyboardInput(maskKeys[i], 0);

	//点击热键
	SendKeyboardInput(hotKey, 0);
	SendKeyboardInput(hotKey, KEYEVENTF_KEYUP);

	//抬起组合键
	for (int i = 0; i < maskKeys.size(); i++)
		SendKeyboardInput(maskKeys[i], KEYEVENTF_KEYUP);
}

void UIAKeyboardLibrary::SendMessageHotkey(LONG64 id, int hotKey, vector<INT> maskKeys) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

	UIA_HWND hwnd;
	HRESULT hResult = uiElement->get_CurrentNativeWindowHandle(&hwnd);
	if (FAILED(hResult) || !hwnd)
		throw string("获取NativeWindowHandle失败");

	//按下组合键
	for (int i = 0; i < maskKeys.size(); i++)
		SendKeyboardInput(maskKeys[i], 0);

	PostMessage((HWND)hwnd, WM_SYSKEYDOWN, hotKey, 0);
	PostMessage((HWND)hwnd, WM_SYSKEYUP, hotKey, 0);

	//抬起组合键
	for (int i = 0; i < maskKeys.size(); i++)
		SendKeyboardInput(maskKeys[i], KEYEVENTF_KEYUP);
}

void SendAscii(wchar_t data, BOOL shift) {
	INPUT input[2];
	memset(input, 0, 2 * sizeof(INPUT));

	if (shift) {
		//按下SHIFT
		input[0].type = INPUT_KEYBOARD;
		input[0].ki.wVk = VK_SHIFT;
		SendInput(1, input, sizeof(INPUT));
	}

	input[0].type = INPUT_KEYBOARD;
	input[0].ki.wVk = data;

	input[1].type = INPUT_KEYBOARD;
	input[1].ki.wVk = data;
	input[1].ki.dwFlags = KEYEVENTF_KEYUP;

	SendInput(2, input, sizeof(INPUT));

	if (shift) {
		//弹起SHIFT
		input[0].type = INPUT_KEYBOARD;
		input[0].ki.wVk = VK_SHIFT;
		input[0].ki.dwFlags = KEYEVENTF_KEYUP;
		SendInput(1, input, sizeof(INPUT));
	}
}

void SendUnicode(wchar_t data) {
	INPUT input[2];
	memset(input, 0, 2 * sizeof(INPUT));

	input[0].type = INPUT_KEYBOARD;
	input[0].ki.wVk = 0;
	input[0].ki.wScan = data;
	input[0].ki.dwFlags = KEYEVENTF_UNICODE;

	input[1].type = INPUT_KEYBOARD;
	input[1].ki.wVk = 0;
	input[1].ki.wScan = data;
	input[1].ki.dwFlags = KEYEVENTF_KEYUP | KEYEVENTF_UNICODE;

	SendInput(2, input, sizeof(INPUT));
}