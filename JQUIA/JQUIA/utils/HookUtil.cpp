#include "stdafx.h"
#include "HookUtil.h"
#include <iostream>
#include<string>
using namespace  std;

HookUtil::HookUtil()
{
}

HookUtil::~HookUtil()
{
	StopHook();
}

void HookUtil::MsgLoop()
{
	MSG message;
	while (msgLooping != 0 && GetMessage(&message, NULL, 0, 0)) {
		TranslateMessage(&message);
		DispatchMessage(&message);
	}
}

void HookUtil::StartHook(HOOKPROC mouseProc, HOOKPROC keyProc)
{
	StopHook();
	HINSTANCE hInst = 0;
	_mouseHook = SetWindowsHookEx(WH_MOUSE_LL, mouseProc, hInst, 0);
	_keyboardHook = SetWindowsHookEx(WH_KEYBOARD_LL, keyProc, hInst, 0);
	msgLooping = TRUE;
	MsgLoop();
}

void HookUtil::StopHook()
{
	bool mUnhooked = UnhookWindowsHookEx(_mouseHook);
	_mouseHook = NULL;

	bool kUnhooked = UnhookWindowsHookEx(_keyboardHook);
	_keyboardHook = NULL;

	cout << "UnhookMouse :" + to_string(mUnhooked) + "\tUnhookKeyboard£º" + to_string(kUnhooked) << endl;
	msgLooping = FALSE;
}