#include "stdafx.h"
#include "MouseUtils.h"
void SendMouseInput(DWORD dwFlags, long x, long y) {
	/*INPUT input = {};
	input.type = INPUT_MOUSE;
	input.mi.dwFlags = dwFlags;
	SendInput(1, &input, sizeof(INPUT));
	ZeroMemory(&input, sizeof(INPUT));*/
	HDC hdc = GetDC(NULL);                           // 得到屏幕DC
	long client_width = GetDeviceCaps(hdc, HORZRES);      // 宽
	long client_height = GetDeviceCaps(hdc, VERTRES);     // 高
	ReleaseDC(NULL, hdc);                            // 释放DC

	double dx = x * (65535.0 / client_width);
	double dy = y * (65535.0 / client_height);//ScreenWidth和ScreenHeight，其实是当前显示器的分辨率，获得方法是ScreenWidth=Screen.PrimaryScreen.WorkingArea.Width

	MOUSEINPUT *myMinput = new MOUSEINPUT();
	memset(myMinput, 0, sizeof(myMinput));
	myMinput->dx = (long)dx;
	myMinput->dy = (long)dy;
	myMinput->mouseData = 0;
	myMinput->dwFlags = dwFlags;

	myMinput->time = 0;
	INPUT myInput[1];
	memset(myInput, 0, sizeof(myInput));
	myInput[0].type = 0;
	myInput[0].mi = *myMinput;
	SendInput(1, myInput, sizeof(INPUT));
}