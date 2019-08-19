#pragma once
#include "library\UIAObj.h"
using namespace std;

class UIAWindowLibrary {
private:
	UIAObj *uiaObj;
public:
	UIAWindowLibrary(UIAObj *uiaObj);
public:
	void SetWindowState(LONG64 id, int ws);
	void MoveWindow_(LONG64 id, RECT rect);
	void CloseWindow(LONG64 id);
};
