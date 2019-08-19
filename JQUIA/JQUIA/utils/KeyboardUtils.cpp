#include "stdafx.h"
#include "KeyboardUtils.h"

void SendKeyboardInput(int key, DWORD dwFlags) {
	INPUT input;
	input.type = INPUT_KEYBOARD;
	input.ki.wScan = 0;
	input.ki.time = 0;
	input.ki.dwExtraInfo = 0;
	input.ki.wVk = key;
	input.ki.dwFlags = dwFlags;
	SendInput(1, &input, sizeof(INPUT));
	ZeroMemory(&input, sizeof(INPUT));
}