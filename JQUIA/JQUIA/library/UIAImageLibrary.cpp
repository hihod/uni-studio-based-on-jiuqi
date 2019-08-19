#include "stdafx.h"
#include "UIAImageLibrary.h"
#include <stdio.h>
UIAImageLibrary::UIAImageLibrary()
{
	//为屏幕创建设备描述表
	hScrDC = CreateDC(L"DISPLAY", NULL, NULL, NULL);
	//为屏幕设备描述表创建兼容的内存设备描述表
	hMemDC = CreateCompatibleDC(hScrDC);   //或者::CreateCompatibleDC(NULL)

	//获得屏幕宽度和高度
	xScrn = GetDeviceCaps(hScrDC, HORZRES);
	yScrn = GetDeviceCaps(hScrDC, VERTRES);
}

UIAImageLibrary::~UIAImageLibrary()
{
	DeleteDC(hScrDC);
	DeleteDC(hMemDC);
}
DWORD getScreenshotBytes(BYTE** result, HBITMAP   hBitmap);
DWORD UIAImageLibrary::doSnapShot(BYTE** result, RECT rect)
{
	//确保选定区域不为空矩形
	if (IsRectEmpty(&rect))
		return   NULL;
	//获得选定区域坐标
	nX = rect.left;
	nY = rect.top;
	nX2 = rect.right;
	nY2 = rect.bottom;

	//确保选定区域是可见的
	if (nX < 0)
		nX = 0;
	if (nY < 0)
		nY = 0;
	if (nX2 > xScrn)
		nX2 = xScrn;
	if (nY2 > yScrn)
		nY2 = yScrn;

	nWidth = nX2 - nX;
	nHeight = nY2 - nY;

	//创建一个与屏幕设备描述表兼容的位图
	hBitmap = CreateCompatibleBitmap(hScrDC, nWidth, nHeight);

	//把新位图选到内存设备描述表中
	hOldBitmap = (HBITMAP)SelectObject(hMemDC, hBitmap);

	//把屏幕设备描述表拷贝到内存设备描述表中
	BitBlt(hMemDC, 0, 0, nWidth, nHeight, hScrDC, nX, nY, SRCCOPY);

	//得到屏幕位图的句柄
	hBitmap = (HBITMAP)SelectObject(hMemDC, hOldBitmap);

	return getScreenshotBytes(result, hBitmap);
}
DWORD copyByteArray(BYTE** result, BYTE* head, DWORD headSize, BYTE* foot, DWORD footSize);
DWORD getScreenshotBytes(BYTE** result, HBITMAP   hBitmap)
{
	//lpFileName   为位图文件名
	WORD   wBitCount;   //位图中每个像素所占字节数

	//定义调色板大小，位图中像素字节大小，位图文件大小，写入文件字节数
	DWORD   dwPaletteSize = 0, dwBmBitsSize, dwDIBSize/*, dwWritten*/;
	BITMAP   Bitmap; //位图属性结构
	BITMAPFILEHEADER   bmfHdr;   //位图文件头结构
	BITMAPINFOHEADER   bi;   //位图信息头结构
//	HANDLE   fh;   //定义文件，分配内存句柄，调色板句柄
	LPSTR   lpbk, lpmem;

	wBitCount = 32;
	//设置位图信息头结构
	GetObject(hBitmap, sizeof(BITMAP), (LPSTR)&Bitmap);
	bi.biSize = sizeof(BITMAPINFOHEADER);
	bi.biWidth = Bitmap.bmWidth;
	bi.biHeight = Bitmap.bmHeight; //为负,正向的位图;为正,倒向的位图
	bi.biPlanes = 1;
	bi.biBitCount = wBitCount;
	bi.biCompression = BI_RGB;
	bi.biSizeImage = 0;
	bi.biXPelsPerMeter = 0;
	bi.biYPelsPerMeter = 0;
	bi.biClrUsed = 0;
	bi.biClrImportant = 0;
	dwBmBitsSize = ((Bitmap.bmWidth*wBitCount + 31) / 32) * 4 * Bitmap.bmHeight;

	//设置位图文件头
	bmfHdr.bfType = 0x4D42;   //   "BM"
	dwDIBSize = sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER) + dwBmBitsSize;
	bmfHdr.bfSize = dwDIBSize;
	bmfHdr.bfReserved1 = 0;
	bmfHdr.bfReserved2 = 0;
	bmfHdr.bfOffBits = (DWORD)sizeof(BITMAPFILEHEADER) + (DWORD)sizeof(BITMAPINFOHEADER);

	//获取位图阵列
	lpmem = new   char[dwBmBitsSize];
	lpbk = (LPSTR)   new   char[dwBmBitsSize];
	GetBitmapBits(hBitmap, dwBmBitsSize, lpmem);//正向的内存图象数据

	//转化为倒向数据(仅在bmHeight为正时需要)
	for (int i = 0; i < Bitmap.bmHeight; i++)
	{
		memcpy(lpbk + Bitmap.bmWidth*i * 4, lpmem + Bitmap.bmWidth*(Bitmap.bmHeight - i - 1) * 4, Bitmap.bmWidth * 4);
	}
	//	return (BYTE*)lpbk;

	BYTE*  t1, *t2;
	int t1Len = 0, t2Len = 0;
	t1Len = copyByteArray((BYTE**)&t1, (BYTE*)&bmfHdr, sizeof(BITMAPFILEHEADER), (BYTE*)&bi, sizeof(BITMAPINFOHEADER));
	t2Len = copyByteArray((BYTE**)&t2, (BYTE*)t1, t1Len, (BYTE*)lpbk, dwBmBitsSize);
	*result = t2;
	//清除
	delete[]lpbk;
	delete[]lpmem;
	delete[]t1;
	return t2Len;
}

DWORD copyByteArray(BYTE** result, BYTE* head, DWORD headSize, BYTE* foot, DWORD footSize) {
	LPSTR   lpbk;
	DWORD totalSize = headSize + footSize;
	lpbk = (LPSTR)   new   char[totalSize + 1];

	memcpy((BYTE *)memcpy(lpbk, head, headSize) + headSize, foot, footSize);
	lpbk[totalSize] = '\0';
	*result = (BYTE*)lpbk;
	return totalSize;
}

int  SaveBitmapToFile(HBITMAP   hBitmap, LPCWSTR   lpFileName)   //hBitmap   为刚才的屏幕位图句柄
{           //lpFileName   为位图文件名
	WORD   wBitCount;   //位图中每个像素所占字节数

	//定义调色板大小，位图中像素字节大小，位图文件大小，写入文件字节数
	DWORD   dwPaletteSize = 0, dwBmBitsSize, dwDIBSize, dwWritten;
	BITMAP   Bitmap; //位图属性结构
	BITMAPFILEHEADER   bmfHdr;   //位图文件头结构
	BITMAPINFOHEADER   bi;   //位图信息头结构
	HANDLE   fh;   //定义文件，分配内存句柄，调色板句柄
	LPSTR   lpbk, lpmem;

	wBitCount = 32;
	//设置位图信息头结构
	GetObject(hBitmap, sizeof(BITMAP), (LPSTR)&Bitmap);
	bi.biSize = sizeof(BITMAPINFOHEADER);
	bi.biWidth = Bitmap.bmWidth;
	bi.biHeight = Bitmap.bmHeight; //为负,正向的位图;为正,倒向的位图
	bi.biPlanes = 1;
	bi.biBitCount = wBitCount;
	bi.biCompression = BI_RGB;
	bi.biSizeImage = 0;
	bi.biXPelsPerMeter = 0;
	bi.biYPelsPerMeter = 0;
	bi.biClrUsed = 0;
	bi.biClrImportant = 0;
	dwBmBitsSize = ((Bitmap.bmWidth*wBitCount + 31) / 32) * 4 * Bitmap.bmHeight;

	//创建位图文件
	fh = CreateFile(lpFileName, GENERIC_WRITE, 0, NULL, CREATE_ALWAYS,
		FILE_ATTRIBUTE_NORMAL | FILE_FLAG_SEQUENTIAL_SCAN, NULL);
	if (fh == INVALID_HANDLE_VALUE)
		return   FALSE;
	//设置位图文件头
	bmfHdr.bfType = 0x4D42;   //   "BM"
	dwDIBSize = sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER) + dwBmBitsSize;
	bmfHdr.bfSize = dwDIBSize;
	bmfHdr.bfReserved1 = 0;
	bmfHdr.bfReserved2 = 0;
	bmfHdr.bfOffBits = (DWORD)sizeof(BITMAPFILEHEADER) + (DWORD)sizeof(BITMAPINFOHEADER);

	//写入位图文件头
	WriteFile(fh, (LPSTR)&bmfHdr, sizeof(BITMAPFILEHEADER), &dwWritten, NULL);
	//写入位图信息头
	WriteFile(fh, (LPSTR)&bi, sizeof(BITMAPINFOHEADER), &dwWritten, NULL);

	//获取位图阵列
	lpmem = new   char[dwBmBitsSize];
	lpbk = (LPSTR)   new   char[dwBmBitsSize];
	GetBitmapBits(hBitmap, dwBmBitsSize, lpmem);//正向的内存图象数据

	//转化为倒向数据(仅在bmHeight为正时需要)
	for (int i = 0; i < Bitmap.bmHeight; i++)
	{
		memcpy(lpbk + Bitmap.bmWidth*i * 4, lpmem + Bitmap.bmWidth*(Bitmap.bmHeight - i - 1) * 4, Bitmap.bmWidth * 4);
	}
	//写位图数据
	WriteFile(fh, lpbk, dwBmBitsSize, &dwWritten, NULL);

	//清除
	delete[]lpbk;
	delete[]lpmem;

	CloseHandle(fh);
	return   TRUE;
}