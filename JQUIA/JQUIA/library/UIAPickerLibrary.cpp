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
	point->x = (WORD)LOWORD(lParam);//ȡ��λ
	point->y = (WORD)HIWORD(lParam);//ȡ��λ

	ClientToScreen(hwnd, point);
	point->x = (WORD)point->x;//����short�����������
	point->y = (WORD)point->y;//ͬ��
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
	wsprintf(cursorPos, L"λ�� = %d X %d      ", (short)point.x, (short)point.y);
	TextOut(hdc, 5, 5, cursorPos, lstrlen(cursorPos));
	wstring f2str = L"F2 = ��ͣ5��";
	TextOut(hdc, 5, 25, f2str.c_str(), (int)f2str.size());
	wstring escstr = L"ESC = �˳�";
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
	//��������
	case WM_DESTROY:
	{
		UIALog("WM_DESTROY", LV_DEBUG);
		hookUtil.StopHook();
		KillTimer(hwnd, TimerID);
		KillTimer(hwnd, F2TimerID);
		PostQuitMessage(0);
		break;
	}
	//Ĭ��
	default:
	{
		return DefWindowProc(hwnd, uMsg, wParam, lParam);
	}
	}

	return 0;
}

DWORD WINAPI DlgThreadProc(LPVOID lpParameter)
{
	//����ǰ�̸߳��ӵ�JVM���Ա��ڵ�ǰ�߳���ʹ��env
	JNIEnv *env;
	gJVM->AttachCurrentThread((void **)&env, NULL);

	//�����ص���
	jobject callbackObj = (jobject)lpParameter;
	pickerCallback = new UIAPickerCallback(env, callbackObj);

	//ע�ᴰ����
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

	//�������壨��������Alt+Tab�б����ϲ㣩
	hwnd = CreateWindowExW(WS_EX_TOOLWINDOW | WS_EX_TOPMOST, L"PickerDlg", L"ѡ����", WS_POPUP | WS_BORDER | WS_THICKFRAME, 0, 0, 0, 0, NULL, NULL, (HINSTANCE)ben, NULL);

	//��ʾ���壺λ�ô�С��
	SetWindowPos(hwnd, 0, 0, 0, DIALOG_WIDTH, DIALOG_HEIGHT, SWP_NOZORDER);
	ShowWindow(hwnd, SW_SHOWNORMAL);
	StartDrawTimer();
	hookUtil.StartHook(&mouseProc, &keyboardProc);

	//��Ϣѭ��
	MSG msg;
	while (GetMessage(&msg, NULL, 0, 0))
	{
		TranslateMessage(&msg);
		DispatchMessage(&msg);
	}

	//����ǰ�̴߳�JVM�а��룬��AttachCurrentThread��Ӧ
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

				//����ڴ�����Χ
				//Ų�����壬�����Ҫѡ��Ԫ�ز����ڵ�
				if (PointAroundRect(&rect, point)) {
					if (rect.left == 0 && rect.top == 0)
						MoveWindow2_RIGHT_BOTTOM();
					else
						MoveWindow2_LEFT_TOP();
				}

				//�����Ϣ�����λ��
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
	//��ʼ��ȫ�ֱ���
	mEnv = env;
	mEnv->GetJavaVM(&gJVM);
	callbackObjclass = mEnv->FindClass("com/jiuqi/rpa/lib/picker/UIAPickerCallback");
	//��ʰȡ�Ի���
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

	//����ڴ�����Χ
	//Ų�����壬�����Ҫѡ��Ԫ�ز����ڵ�
	if (PointAroundRect(&rect, point)) {
		if (rect.left == 0 && rect.top == 0)
			MoveWindow2_RIGHT_BOTTOM();
		else
			MoveWindow2_LEFT_TOP();
	}

	//�����Ϣ�����λ��
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
		//�趨���ִ�С����ɫ
		LOGFONT logfont; //�ı��������
		ZeroMemory(&logfont, sizeof(LOGFONT));
		logfont.lfCharSet = GB2312_CHARSET;
		logfont.lfHeight = -50; //��������Ĵ�С
		HFONT hFont = CreateFontIndirect(&logfont);
		SelectObject(hdc, hFont);  //������ֵĸ�ʽ
		wstring text = /*L"����ʱ" +*/ timeText[maxCount - startCount + 1];
		TextOutW(hdc, 25, 5, text.c_str(), (int)text.size());
		UpdateWindow(hwnd);
		DeleteObject(hFont);
	}
}