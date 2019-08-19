#pragma once
#include <string>
#include <vector>
#include "utils\Path.h"
#include "library\UIAObj.h"
using namespace std;

class UIAControlLibrary {
private:
	UIAObj *uiaObj;
public:
	UIAControlLibrary(UIAObj *uiaObj);
public:
	void GetRect(LONG64 id, RECT *rect);
	string GetAttributeValue(LONG64 id, string attrName);
	void ScrollIntoView(LONG64 id);
	void SetFocus(LONG64 id);
	bool IsChecked(LONG64 id);
	void SetChecked(LONG64 id, bool checked);
	int GetCheckState(LONG64 id);
	string GetText(LONG64 id);
	void SetText(LONG64 id, string text);
	bool IsPassword(LONG64 id);
	bool IsTable(LONG64 id);
	bool Enable(LONG64 id);
	bool Visible(LONG64 id);
	void ClearSelection(LONG64 id);
	void SelectItems(LONG64 id, vector<string> items);
	string GetPageData(vector<Path *> columns);
};
