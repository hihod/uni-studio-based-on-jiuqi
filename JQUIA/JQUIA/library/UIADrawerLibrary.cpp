#include "stdafx.h"
#include <windows.h>
#include <comdef.h>
#include <iostream>
#include "utils\JNIUitls.h"
#include "utils\Logger.h"
#include "library\UIADrawerLibrary.h"

/*------------RectDrawer----------------*/

void RectDrawer::InitDC() {
	hDC = GetDC(NULL);

	lastColor = RGB(255, 0, 0);
	pen = CreatePen(PS_SOLID, 3, lastColor);
	oldPen = (HPEN)SelectObject(hDC, pen);

	brush = (HBRUSH)GetStockObject(NULL_BRUSH);
	oldBrush = (HBRUSH)SelectObject(hDC, brush);
}

void RectDrawer::FreeDC() {
	SelectObject(hDC, oldPen);
	DeleteObject(pen);

	SelectObject(hDC, oldBrush);
	DeleteObject(brush);

	ReleaseDC(0, hDC);
}

void RectDrawer::SetColor(COLORREF color) {
	if (color == lastColor)
		return;

	lastColor = color;
	pen = CreatePen(PS_SOLID, 3, lastColor);
	HPEN tempPen = (HPEN)SelectObject(hDC, pen);
	DeleteObject(tempPen);
}

void RectDrawer::DrawRect(RECT nRect) {
	oRect = { nRect.left, nRect.top, nRect.right, nRect.bottom };
	//UIALog("DrawRect " + RECT2string(oRect), LV_DEBUG);
	Rectangle(hDC, oRect.left, oRect.top, oRect.right, oRect.bottom);
}

void RectDrawer::UnDrawRect() {
	InvalidateRect(0, &oRect, FALSE);
}

/*------------UIADrawerLibrary----------------*/

DWORD WINAPI NewThreadProc(LPVOID lpParameter) {
	UIADrawerLibrary *library = (UIADrawerLibrary *)lpParameter;

	//初始化DC
	library->rDrawer->InitDC();

	//循环重画
	while (library->running) {
		library->rDrawer->DrawRect(library->tRect);
		Sleep(50);
	}

	//释放DC
	library->rDrawer->FreeDC();

	return 0;
}

UIADrawerLibrary::UIADrawerLibrary() {
	rDrawer = new RectDrawer();
	hThread = CreateThread(NULL, 0, NewThreadProc, this, CREATE_SUSPENDED, NULL);
	running = TRUE;
}

UIADrawerLibrary::~UIADrawerLibrary() {
	running = FALSE;
	CloseHandle(hThread);
	delete rDrawer;
}

void UIADrawerLibrary::StartDraw(RECT rect, COLORREF color) {
	rDrawer->UnDrawRect();
	if (rect.top == -1 && rect.bottom == -1 && rect.left == -1 && rect.right == -1) {
	}
	else
		this->tRect = { rect.left, rect.top, rect.right, rect.bottom };

	DWORD r = ResumeThread(hThread);
	if (r == -1)
		throw string("启动线程失败");
}

void UIADrawerLibrary::EndDraw() {
	DWORD r = SuspendThread(hThread);
	if (r == -1)
		throw string("挂起线程失败");

	for (DWORD d = 0; d < r; d++)
		ResumeThread(hThread);

	rDrawer->UnDrawRect();
}