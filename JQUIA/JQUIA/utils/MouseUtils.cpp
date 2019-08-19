#include "stdafx.h"
#include "MouseUtils.h"
void SendMouseInput(DWORD dwFlags, long x, long y) {
	/*INPUT input = {};
	input.type = INPUT_MOUSE;
	input.mi.dwFlags = dwFlags;
	SendInput(1, &input, sizeof(INPUT));
	ZeroMemory(&input, sizeof(INPUT));*/
	HDC hdc = GetDC(NULL);                           // �õ���ĻDC
	long client_width = GetDeviceCaps(hdc, HORZRES);      // ��
	long client_height = GetDeviceCaps(hdc, VERTRES);     // ��
	ReleaseDC(NULL, hdc);                            // �ͷ�DC

	double dx = x * (65535.0 / client_width);
	double dy = y * (65535.0 / client_height);//ScreenWidth��ScreenHeight����ʵ�ǵ�ǰ��ʾ���ķֱ��ʣ���÷�����ScreenWidth=Screen.PrimaryScreen.WorkingArea.Width

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