#pragma once

class UIAImageLibrary {
private:
	HDC   hScrDC, hMemDC; //屏幕和内存设备描述表
	HBITMAP   hBitmap, hOldBitmap;//位图句柄
	int   nX, nY, nX2, nY2; //选定区域坐标
	int   nWidth, nHeight; //位图宽度和高度
	int   xScrn, yScrn; //屏幕宽度和高度
public:
	UIAImageLibrary();
	~UIAImageLibrary();
	DWORD doSnapShot(BYTE** result, RECT rect);
};
