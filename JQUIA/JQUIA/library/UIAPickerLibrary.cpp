#include "stdafx.h"
#include <iostream>
#include <Windows.h>
#include <WinUser.h>
#include "utils\Logger.h"
#include "UIAPickerLibrary.h"
#include "UIADrawerLibrary.h"
#include "utils\HookUtil.h"
/*------------JQUIACallback----------------*/

jclass callbackObjclass;
HookUtil hookUtil;
LRESULT CALLBACK mouseProc(int nCode, WPARAM wParam, LPARAM lParam);
LRESULT CALLBACK keyboardProc(int nCode, WPARAM wParam, LPARAM lParam);
VOID CALLBACK pickDlgTimerProc(HWND hwnd, UINT uMsg, UINT idEvent, DWORD dwTime);
VOID CALLBACK F2TimerProc(HWND hwnd, UINT uMsg, UINT idEvent, DWORD dwTime);
void pauseHook(int);
void StartDrawTimer();
UINT_PTR TimerID;
UINT_PTR F2TimerID;

extern UIADrawerLibrary *drawerLibrary;

UIAPickerCallback::UIAPickerCallback(JNIEnv *env, jobject callbackObj)
{
	this->env = env;
	this->callbackObj = callbackObj;

	this->mouseMove = env->GetStaticMethodID(callbackObjclass, "onMouseMove", "(II)V");
	this->mouseLeftUp = env->GetStaticMethodID(callbackObjclass, "onMouseLeftUp", "(II)V");
	this->keyEscapeUp = env->GetStaticMethodID(callbackObjclass, "onKeyEscapeUp", "()V");
}

void UIAPickerCallback::onMouseMove(POINT pt)
{
	env->CallStaticVoidMethod(callbackObjclass, mouseMove, pt.x, pt.y);
}

void UIAPickerCallback::onMouseLeftUp(POINT pt)
{
	env->CallStaticVoidMethod(callbackObjclass, mouseLeftUp, pt.x, pt.y);
}

void UIAPickerCallback::onKeyEscapeUp()
{
	env->CallStaticVoidMethod(callbackObjclass, keyEscapeUp);
}

/*------------PickerLibrary----------------*/

JavaVM *gJVM;
JNIEnv *mEnv;
UIAPickerCallback *pickerCallback;
POINT crtPoint;
/*------------PickerDlg----------------*/

HANDLE ben;
HANDLE timeDlgHwnd;
HWND hwnd;
HWND timeHWND;
int DIALOG_WIDTH = 160;
int DIALOG_HEIGHT = 200;

BOOL PointAroundRect(CONST RECT *lprc, POINT pt)
{
	RECT rect;
	rect.top = lprc->top - 5;
	rect.left = lprc->left - 5;
	rect.bottom = lprc->bottom + 5;
	rect.right = lprc->right + 5;
	return PtInRect(&rect, pt);
}

void GetPoint(LPARAM lParam, POINT *point)
{
	point->x = (WORD)LOWORD(lParam);//取低位
	point->y = (WORD)HIWORD(lParam);//取高位

	ClientToScreen(hwnd, point);
	point->x = (WORD)point->x;//避免short类型整数溢出
	point->y = (WORD)point->y;//同上
}

void SetInput_MOUSE_LEFTDOWN()
{
	INPUT input;
	input.type = INPUT_MOUSE;
	input.mi.dwFlags = MOUSEEVENTF_LEFTDOWN;
	SendInput(1, &input, sizeof(INPUT));
	ZeroMemory(&input, sizeof(INPUT));
}

void MoveWindow2_LEFT_TOP()
{
	MoveWindow(hwnd, 0, 0, DIALOG_WIDTH, DIALOG_HEIGHT, FALSE);
}

void MoveWindow2_RIGHT_BOTTOM()
{
	int x = GetSystemMetrics(SM_CXFULLSCREEN) - DIALOG_WIDTH;
	int y = GetSystemMetrics(SM_CYFULLSCREEN) - DIALOG_HEIGHT;
	MoveWindow(hwnd, x, y, DIALOG_WIDTH, DIALOG_HEIGHT, FALSE);
}

void TextOutInfo(POINT point)
{
	HDC hdc = GetDC(hwnd);
	wchar_t cursorPos[32];
	wsprintf(cursorPos, L"位置 = %d X %d      ", (short)point.x, (short)point.y);
	TextOut(hdc, 5, 5, cursorPos, lstrlen(cursorPos));
	wstring f2str = L"F2 = 暂停5秒";
	TextOut(hdc, 5, 25, f2str.c_str(), (int)f2str.size());
	wstring escstr = L"ESC = 退出";
	TextOut(hdc, 5, 45, escstr.c_str(), (int)escstr.size());
	UpdateWindow(hwnd);
	ReleaseDC(hwnd, hdc);
}

bool isInTimer = false;

LRESULT CALLBACK WndProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam)
{
	switch (uMsg)
	{
	case WM_CLOSE:
	{
		UIALog("WM_CLOSE", LV_DEBUG);
		hookUtil.StopHook();
		KillTimer(hwnd, TimerID);
		KillTimer(hwnd, F2TimerID);
		DestroyWindow(hwnd);
		//ReleaseCapture();

		break;
	}
	//窗体销毁
	case WM_DESTROY:
	{
		UIALog("WM_DESTROY", LV_DEBUG);
		hookUtil.StopHook();
		KillTimer(hwnd, TimerID);
		KillTimer(hwnd, F2TimerID);
		PostQuitMessage(0);
		break;
	}
	//默认
	default:
	{
		return DefWindowProc(hwnd, uMsg, wParam, lParam);
	}
	}

	return 0;
}

DWORD WINAPI DlgThreadProc(LPVOID lpParameter)
{
	//将当前线程附加到JVM，以便在当前线程中使用env
	JNIEnv *env;
	gJVM->AttachCurrentThread((void **)&env, NULL);

	//构建回调类
	jobject callbackObj = (jobject)lpParameter;
	pickerCallback = new UIAPickerCallback(env, callbackObj);

	//注册窗体类
	WNDCLASS wndcls;
	wndcls.cbClsExtra = 0;
	wndcls.cbWndExtra = 0;
	wndcls.hbrBackground = (HBRUSH)CreateSolidBrush(RGB(255, 255, 255));
	wndcls.hCursor = LoadCursor(NULL, IDC_CROSS);
	wndcls.hIcon = NULL;
	wndcls.hInstance = (HINSTANCE)ben;
	wndcls.lpfnWndProc = WndProc;
	wndcls.lpszClassName = L"PickerDlg";
	wndcls.lpszMenuName = NULL;
	wndcls.style = CS_HREDRAW | CS_VREDRAW;
	RegisterClass(&wndcls);

	//创建窗体（不出现在Alt+Tab列表、最上层）
	hwnd = CreateWindowExW(WS_EX_TOOLWINDOW | WS_EX_TOPMOST, L"PickerDlg", L"选择器", WS_POPUP | WS_BORDER | WS_THICKFRAME, 0, 0, 0, 0, NULL, NULL, (HINSTANCE)ben, NULL);

	//显示窗体：位置大小等
	SetWindowPos(hwnd, 0, 0, 0, DIALOG_WIDTH, DIALOG_HEIGHT, SWP_NOZORDER);
	ShowWindow(hwnd, SW_SHOWNORMAL);
	StartDrawTimer();
	hookUtil.StartHook(&mouseProc, &keyboardProc);

	//消息循环
	MSG msg;
	while (GetMessage(&msg, NULL, 0, 0))
	{
		TranslateMessage(&msg);
		DispatchMessage(&msg);
	}

	//将当前线程从JVM中剥离，与AttachCurrentThread对应
	gJVM->DetachCurrentThread();

	return 0;
}

void OpenPickerDlg(jobject callbackObj)
{
	ben = GetModuleHandle(NULL);
	HANDLE thread1;
	thread1 = CreateThread(NULL, 0, DlgThreadProc, callbackObj, 0, NULL);
}

HHOOK _mouseHook, _keyboardHook;
DWORD preMouseTimeStamp = 0;
DWORD preKeyTimeStamp = 0;

LRESULT CALLBACK mouseProc(int nCode, WPARAM wParam, LPARAM lParam) {
	UIALog("MOUSEPROC " + to_string((nCode == HC_ACTION && pickerCallback && isInTimer != true)) + "isInTimer="
		+ to_string(isInTimer), LV_DEBUG);
	if (nCode == HC_ACTION && pickerCallback && isInTimer != true) {
		LPMOUSEHOOKSTRUCT mStruct = (LPMOUSEHOOKSTRUCT)lParam;
		if (wParam == WM_MOUSEMOVE) {
			/*
			DWORD crt = GetTickCount();
			if (crt - preMouseTimeStamp > 10) {
				preMouseTimeStamp = crt;
				POINT point = mStruct->pt;
				crtPoint = point;
				RECT rect;
				GetWindowRect(hwnd, &rect);

				//光标在窗体周围
				//挪动窗体，避免对要选择元素产生遮挡
				if (PointAroundRect(&rect, point)) {
					if (rect.left == 0 && rect.top == 0)
						MoveWindow2_RIGHT_BOTTOM();
					else
						MoveWindow2_LEFT_TOP();
				}

				//输出信息：光标位置
				TextOutInfo(point);
				//pickerCallback->onMouseMove(mStruct->pt);
			}*/
		}
		else if (wParam == WM_LBUTTONDOWN)
			return TRUE;
		else if (wParam == WM_LBUTTONUP) {
			pickerCallback->onMouseLeftUp(mStruct->pt);
			hookUtil.StopHook();
			KillTimer(hwnd, TimerID);
			DestroyWindow(hwnd);
			return TRUE;
		}
	}
	return CallNextHookEx(_mouseHook, nCode, wParam, lParam);
}

LRESULT CALLBACK keyboardProc(int nCode, WPARAM wParam, LPARAM lParam) {
	if (nCode == HC_ACTION && wParam == WM_KEYUP && pickerCallback &&isInTimer != true) {
		LPKBDLLHOOKSTRUCT kStruct = (LPKBDLLHOOKSTRUCT)lParam;
		if (kStruct->vkCode == VK_ESCAPE) {
			pickerCallback->onKeyEscapeUp();
			hookUtil.StopHook();
			KillTimer(hwnd, TimerID);
			DestroyWindow(hwnd);
			return TRUE;
		}
		else if (kStruct->vkCode == VK_F2) {
			int x = GetSystemMetrics(SM_CXFULLSCREEN) - 100;
			int y = GetSystemMetrics(SM_CYFULLSCREEN) - 100;
			MoveWindow(hwnd, x, y, 100, 100, FALSE);
			drawerLibrary->EndDraw();
			pauseHook(3);
		}
	}
	return CallNextHookEx(_keyboardHook, nCode, wParam, lParam);
}

void pauseHook(int seconds) {
	UIALog("In pauseHook", LV_DEBUG);
	KillTimer(hwnd, TimerID);
	hookUtil.StopHook();

	isInTimer = true;
	HDC hdc = GetDC(hwnd);
	wstring text = L"                              ";
	TextOutW(hdc, 5, 5, text.c_str(), (int)text.size());
	TextOutW(hdc, 5, 25, text.c_str(), (int)text.size());
	TextOutW(hdc, 5, 45, text.c_str(), (int)text.size());
	UpdateWindow(hwnd);
	F2TimerID = SetTimer(hwnd, 2, 1000, (TIMERPROC)F2TimerProc);
}

void UIAPickerLibrary::StartPick(JNIEnv *env, jobject callbackObj) {
	//初始化全局变量
	mEnv = env;
	mEnv->GetJavaVM(&gJVM);
	callbackObjclass = mEnv->FindClass("com/jiuqi/rpa/lib/picker/UIAPickerCallback");
	//打开拾取对话框
	OpenPickerDlg(callbackObj);
}

void UIAPickerLibrary::EndPick()
{
	hookUtil.StopHook();
	KillTimer(hwnd, TimerID);
	mEnv->DeleteLocalRef(callbackObjclass);
}

void StartDrawTimer() {
	TimerID = SetTimer(hwnd, 1, 500, (TIMERPROC)pickDlgTimerProc);
}
bool isInTimerF = false;
VOID CALLBACK pickDlgTimerProc(HWND hwnd, UINT uMsg, UINT idEvent, DWORD dwTime) {
	if (isInTimerF)return;
	isInTimerF = true;
	POINT point;
	GetCursorPos(&point);
	RECT rect;
	GetWindowRect(hwnd, &rect);

	//光标在窗体周围
	//挪动窗体，避免对要选择元素产生遮挡
	if (PointAroundRect(&rect, point)) {
		if (rect.left == 0 && rect.top == 0)
			MoveWindow2_RIGHT_BOTTOM();
		else
			MoveWindow2_LEFT_TOP();
	}

	//输出信息：光标位置
	TextOutInfo(point);
	//pickerCallback->onMouseMove(mStruct->pt);

	pickerCallback->onMouseMove(point);
	isInTimerF = false;
}
int startCount = 0;
int maxCount = 5;
wstring timeText[6] = { L"          ",L"1",L"2" ,L"3",L"4",L"5" };
VOID CALLBACK F2TimerProc(HWND hwnd, UINT uMsg, UINT idEvent, DWORD dwTime) {
	if (startCount++ > maxCount) {
		isInTimer = false;
		bool iskilled = KillTimer(hwnd, F2TimerID);
		startCount = 0;
		MoveWindow2_LEFT_TOP();

		StartDrawTimer();
		RECT r;
		r.bottom = r.left = r.right = r.top = -1;
		drawerLibrary->StartDraw(r, NULL);
		hookUtil.StartHook(&mouseProc, &keyboardProc);
	}
	else {
		HDC hdc = GetDC(hwnd);
		//设定文字大小和颜色
		LOGFONT logfont; //改变输出字体
		ZeroMemory(&logfont, sizeof(LOGFONT));
		logfont.lfCharSet = GB2312_CHARSET;
		logfont.lfHeight = -50; //设置字体的大小
		HFONT hFont = CreateFontIndirect(&logfont);
		SelectObject(hdc, hFont);  //输出文字的格式
		wstring text = /*L"倒计时" +*/ timeText[maxCount - startCount + 1];
		TextOutW(hdc, 25, 5, text.c_str(), (int)text.size());
		UpdateWindow(hwnd);
		DeleteObject(hFont);
	}
}