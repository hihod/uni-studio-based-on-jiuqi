#pragma once
#include <string>
#include <vector>
#include "library\UIAObj.h"
using namespace std;

class UIAApplicationLibrary {
private:
	UIAObj *uiaObj;
public:
	UIAApplicationLibrary(UIAObj *uiaObj);
public:
	LONG64 StartProcess(string applicationPath, string args);
	LONG64 GetApplicationWindow(LONG64 pId);
	void CloseApplication(LONG64 id);
	void FindProcess(string processName, vector<LONG64> *ids);
	void KillProcess(LONG64 id);
};

HWND GetHWNDByPorcessID(DWORD dwProcessID);