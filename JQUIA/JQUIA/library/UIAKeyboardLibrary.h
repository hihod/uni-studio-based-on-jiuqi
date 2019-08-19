#pragma once
#include <string>
#include "UIAObj.h"
using namespace std;

class UIAKeyboardLibrary {
private:
	UIAObj *uiaObj;
public:
	UIAKeyboardLibrary(UIAObj *uiaObj);
public:
	void ClearText(LONG64 id);
	void TypeText(string text);
	void SendHotKey(int hotKey, vector<INT> maskKeys);
	void SendMessageHotkey(LONG64 id, int hotKey, vector<INT> maskKeys);
};

void SendUnicode(wchar_t data);
void SendAscii(wchar_t data, BOOL shift);