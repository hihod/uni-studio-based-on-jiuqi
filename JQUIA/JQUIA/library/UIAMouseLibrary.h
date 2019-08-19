#pragma once
#include <vector>
#include "UIAObj.h"
using namespace std;

class UIAMouseLibrary {
private:
	UIAObj *uiaObj;
public:
	UIAMouseLibrary(UIAObj *uiaObj);
public:
	void MouseMove(POINT pt);
	void MouseClick(POINT pt, int clickType, int mouseKey, vector<INT> maskKeys);
	void SimulateClick(LONG64 id);
	void MessageClick(LONG64 id, POINT pt, int clickType, int mouseKey, vector<INT> maskKeys);
};