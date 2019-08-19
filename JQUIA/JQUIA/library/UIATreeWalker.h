#pragma once
#include <string>
#include <vector>
#include "library\UIAObj.h"
using namespace std;

class UIATreeWalker {
private:
	UIAObj *uiaObj;
public:
	UIATreeWalker(UIAObj *uiaObj);
public:
	LONG64 GetRoot();
	void GetChildren(LONG64 id, vector<LONG64> *children);
	LONG64 GetParent(LONG64 id);
	string GetText(LONG64 id);
	void GetProperties(LONG64 id, map<string, string> *properties);
};
