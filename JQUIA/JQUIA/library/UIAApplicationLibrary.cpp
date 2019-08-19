#include "stdafx.h"
#include "shellapi.h"
#include "comutil.h"
#include <iostream>
#include <algorithm>
#include "tlhelp32.h"
#include "uiautomation.h"
#include "utils\JNIUitls.h"
#include "UIAApplicationLibrary.h"

UIAApplicationLibrary::UIAApplicationLibrary(UIAObj *uiaObj) {
	this->uiaObj = uiaObj;
}

LONG64 UIAApplicationLibrary::StartProcess(string applicationPath, string args) {
	SHELLEXECUTEINFO shellInfo;
	memset(&shellInfo, 0, sizeof(shellInfo));
	shellInfo.cbSize = sizeof(shellInfo);
	shellInfo.hwnd = NULL;
	shellInfo.lpVerb = _com_util::ConvertStringToBSTR("open");
	shellInfo.lpFile = _com_util::ConvertStringToBSTR(applicationPath.data());
	shellInfo.lpParameters = _com_util::ConvertStringToBSTR(args.data());
	shellInfo.nShow = SW_SHOWNORMAL;
	shellInfo.fMask = SEE_MASK_NOCLOSEPROCESS;

	CoInitializeEx(NULL, COINIT_APARTMENTTHREADED | COINIT_DISABLE_OLE1DDE);
	if (!ShellExecuteEx(&shellInfo))
		throw string("打开应用失败");

	return GetProcessId(shellInfo.hProcess);
}

LONG64 UIAApplicationLibrary::GetApplicationWindow(LONG64 pId) {
	HWND hWnd = GetHWNDByPorcessID((DWORD)pId);
	if (!hWnd)
		return 0;

	IUIAutomationElement *uiElement;
	HRESULT hResult = uiaObj->GetAutomation()->ElementFromHandle(hWnd, &uiElement);
	if (FAILED(hResult))
		throw string("通过窗口句柄获取元素失败");

	return (LONG64)uiElement;
}

void UIAApplicationLibrary::CloseApplication(LONG64 id) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

	UIA_HWND uiaHWnd = 0;
	if (uiElement == NULL) return;
	HRESULT hResult = uiElement->get_CurrentNativeWindowHandle(&uiaHWnd);
	if (FAILED(hResult) || !uiaHWnd)
		throw string("获取应用的窗口句柄失败");

	HWND hWnd = (HWND)uiaHWnd;
	SendMessage(hWnd, WM_CLOSE, 0, 0);
}

void UIAApplicationLibrary::FindProcess(string processName, vector<LONG64> *ids) {
	//TH32CS_SNAPPROCESS表示快照所有进程
	HANDLE hSnapShot = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);
	PROCESSENTRY32 pe;
	pe.dwSize = sizeof(PROCESSENTRY32);
	if (!Process32First(hSnapShot, &pe))
		return;

	transform(processName.begin(), processName.end(), processName.begin(), tolower);
	while (Process32Next(hSnapShot, &pe)) {
		string scTmp = wstring2string(pe.szExeFile);
		transform(scTmp.begin(), scTmp.end(), scTmp.begin(), tolower);

		bool equal = !scTmp.compare(processName);
		if (equal) {
			DWORD dwProcessID = pe.th32ProcessID;
			ids->insert(ids->end(), dwProcessID);
		}
	}
}

void UIAApplicationLibrary::KillProcess(LONG64 id) {
	DWORD dwProcessId = (DWORD)id;
	HANDLE hProcess = OpenProcess(PROCESS_TERMINATE, FALSE, dwProcessId);
	TerminateProcess(hProcess, 0);
	CloseHandle(hProcess);
}

HWND GetHWNDByPorcessID(DWORD dwProcessID) {
	DWORD dwPID = 0;
	HWND hwndRet = NULL;

	HWND hwndWindow = GetTopWindow(0);
	while (hwndWindow) {
		dwPID = 0;

		// 通过窗口句柄取得进程ID
		DWORD dwTheardID = ::GetWindowThreadProcessId(hwndWindow, &dwPID);
		if (dwTheardID != 0) {
			// 判断和参数传入的进程ID是否相等
			if (dwPID == dwProcessID) {
				// 进程ID相等，则记录窗口句柄
				hwndRet = hwndWindow;
				break;
			}
		}
		// 取得下一个窗口句柄
		hwndWindow = ::GetNextWindow(hwndWindow, GW_HWNDNEXT);
	}

	// 上面取得的窗口，不一定是最上层的窗口，需要通过GetParent获取最顶层窗口
	HWND hwndWindowParent = NULL;

	// 循环查找父窗口，以便保证返回的句柄是最顶层的窗口句柄
	while (hwndRet != NULL) {
		hwndWindowParent = ::GetParent(hwndRet);
		if (hwndWindowParent == NULL)
			break;

		hwndRet = hwndWindowParent;
	}

	// 返回窗口句柄
	return hwndRet;
}