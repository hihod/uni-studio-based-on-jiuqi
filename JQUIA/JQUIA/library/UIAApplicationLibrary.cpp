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
		throw string("��Ӧ��ʧ��");

	return GetProcessId(shellInfo.hProcess);
}

LONG64 UIAApplicationLibrary::GetApplicationWindow(LONG64 pId) {
	HWND hWnd = GetHWNDByPorcessID((DWORD)pId);
	if (!hWnd)
		return 0;

	IUIAutomationElement *uiElement;
	HRESULT hResult = uiaObj->GetAutomation()->ElementFromHandle(hWnd, &uiElement);
	if (FAILED(hResult))
		throw string("ͨ�����ھ����ȡԪ��ʧ��");

	return (LONG64)uiElement;
}

void UIAApplicationLibrary::CloseApplication(LONG64 id) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

	UIA_HWND uiaHWnd = 0;
	if (uiElement == NULL) return;
	HRESULT hResult = uiElement->get_CurrentNativeWindowHandle(&uiaHWnd);
	if (FAILED(hResult) || !uiaHWnd)
		throw string("��ȡӦ�õĴ��ھ��ʧ��");

	HWND hWnd = (HWND)uiaHWnd;
	SendMessage(hWnd, WM_CLOSE, 0, 0);
}

void UIAApplicationLibrary::FindProcess(string processName, vector<LONG64> *ids) {
	//TH32CS_SNAPPROCESS��ʾ�������н���
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

		// ͨ�����ھ��ȡ�ý���ID
		DWORD dwTheardID = ::GetWindowThreadProcessId(hwndWindow, &dwPID);
		if (dwTheardID != 0) {
			// �жϺͲ�������Ľ���ID�Ƿ����
			if (dwPID == dwProcessID) {
				// ����ID��ȣ����¼���ھ��
				hwndRet = hwndWindow;
				break;
			}
		}
		// ȡ����һ�����ھ��
		hwndWindow = ::GetNextWindow(hwndWindow, GW_HWNDNEXT);
	}

	// ����ȡ�õĴ��ڣ���һ�������ϲ�Ĵ��ڣ���Ҫͨ��GetParent��ȡ��㴰��
	HWND hwndWindowParent = NULL;

	// ѭ�����Ҹ����ڣ��Ա㱣֤���صľ�������Ĵ��ھ��
	while (hwndRet != NULL) {
		hwndWindowParent = ::GetParent(hwndRet);
		if (hwndWindowParent == NULL)
			break;

		hwndRet = hwndWindowParent;
	}

	// ���ش��ھ��
	return hwndRet;
}