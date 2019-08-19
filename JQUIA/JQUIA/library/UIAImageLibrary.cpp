#include "stdafx.h"
#include "UIAImageLibrary.h"
#include <stdio.h>
UIAImageLibrary::UIAImageLibrary()
{
	//Ϊ��Ļ�����豸������
	hScrDC = CreateDC(L"DISPLAY", NULL, NULL, NULL);
	//Ϊ��Ļ�豸�����������ݵ��ڴ��豸������
	hMemDC = CreateCompatibleDC(hScrDC);   //����::CreateCompatibleDC(NULL)

	//�����Ļ��Ⱥ͸߶�
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
	//ȷ��ѡ������Ϊ�վ���
	if (IsRectEmpty(&rect))
		return   NULL;
	//���ѡ����������
	nX = rect.left;
	nY = rect.top;
	nX2 = rect.right;
	nY2 = rect.bottom;

	//ȷ��ѡ�������ǿɼ���
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

	//����һ������Ļ�豸��������ݵ�λͼ
	hBitmap = CreateCompatibleBitmap(hScrDC, nWidth, nHeight);

	//����λͼѡ���ڴ��豸��������
	hOldBitmap = (HBITMAP)SelectObject(hMemDC, hBitmap);

	//����Ļ�豸�����������ڴ��豸��������
	BitBlt(hMemDC, 0, 0, nWidth, nHeight, hScrDC, nX, nY, SRCCOPY);

	//�õ���Ļλͼ�ľ��
	hBitmap = (HBITMAP)SelectObject(hMemDC, hOldBitmap);

	return getScreenshotBytes(result, hBitmap);
}
DWORD copyByteArray(BYTE** result, BYTE* head, DWORD headSize, BYTE* foot, DWORD footSize);
DWORD getScreenshotBytes(BYTE** result, HBITMAP   hBitmap)
{
	//lpFileName   Ϊλͼ�ļ���
	WORD   wBitCount;   //λͼ��ÿ��������ռ�ֽ���

	//�����ɫ���С��λͼ�������ֽڴ�С��λͼ�ļ���С��д���ļ��ֽ���
	DWORD   dwPaletteSize = 0, dwBmBitsSize, dwDIBSize/*, dwWritten*/;
	BITMAP   Bitmap; //λͼ���Խṹ
	BITMAPFILEHEADER   bmfHdr;   //λͼ�ļ�ͷ�ṹ
	BITMAPINFOHEADER   bi;   //λͼ��Ϣͷ�ṹ
//	HANDLE   fh;   //�����ļ��������ڴ�������ɫ����
	LPSTR   lpbk, lpmem;

	wBitCount = 32;
	//����λͼ��Ϣͷ�ṹ
	GetObject(hBitmap, sizeof(BITMAP), (LPSTR)&Bitmap);
	bi.biSize = sizeof(BITMAPINFOHEADER);
	bi.biWidth = Bitmap.bmWidth;
	bi.biHeight = Bitmap.bmHeight; //Ϊ��,�����λͼ;Ϊ��,�����λͼ
	bi.biPlanes = 1;
	bi.biBitCount = wBitCount;
	bi.biCompression = BI_RGB;
	bi.biSizeImage = 0;
	bi.biXPelsPerMeter = 0;
	bi.biYPelsPerMeter = 0;
	bi.biClrUsed = 0;
	bi.biClrImportant = 0;
	dwBmBitsSize = ((Bitmap.bmWidth*wBitCount + 31) / 32) * 4 * Bitmap.bmHeight;

	//����λͼ�ļ�ͷ
	bmfHdr.bfType = 0x4D42;   //   "BM"
	dwDIBSize = sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER) + dwBmBitsSize;
	bmfHdr.bfSize = dwDIBSize;
	bmfHdr.bfReserved1 = 0;
	bmfHdr.bfReserved2 = 0;
	bmfHdr.bfOffBits = (DWORD)sizeof(BITMAPFILEHEADER) + (DWORD)sizeof(BITMAPINFOHEADER);

	//��ȡλͼ����
	lpmem = new   char[dwBmBitsSize];
	lpbk = (LPSTR)   new   char[dwBmBitsSize];
	GetBitmapBits(hBitmap, dwBmBitsSize, lpmem);//������ڴ�ͼ������

	//ת��Ϊ��������(����bmHeightΪ��ʱ��Ҫ)
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
	//���
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

int  SaveBitmapToFile(HBITMAP   hBitmap, LPCWSTR   lpFileName)   //hBitmap   Ϊ�ղŵ���Ļλͼ���
{           //lpFileName   Ϊλͼ�ļ���
	WORD   wBitCount;   //λͼ��ÿ��������ռ�ֽ���

	//�����ɫ���С��λͼ�������ֽڴ�С��λͼ�ļ���С��д���ļ��ֽ���
	DWORD   dwPaletteSize = 0, dwBmBitsSize, dwDIBSize, dwWritten;
	BITMAP   Bitmap; //λͼ���Խṹ
	BITMAPFILEHEADER   bmfHdr;   //λͼ�ļ�ͷ�ṹ
	BITMAPINFOHEADER   bi;   //λͼ��Ϣͷ�ṹ
	HANDLE   fh;   //�����ļ��������ڴ�������ɫ����
	LPSTR   lpbk, lpmem;

	wBitCount = 32;
	//����λͼ��Ϣͷ�ṹ
	GetObject(hBitmap, sizeof(BITMAP), (LPSTR)&Bitmap);
	bi.biSize = sizeof(BITMAPINFOHEADER);
	bi.biWidth = Bitmap.bmWidth;
	bi.biHeight = Bitmap.bmHeight; //Ϊ��,�����λͼ;Ϊ��,�����λͼ
	bi.biPlanes = 1;
	bi.biBitCount = wBitCount;
	bi.biCompression = BI_RGB;
	bi.biSizeImage = 0;
	bi.biXPelsPerMeter = 0;
	bi.biYPelsPerMeter = 0;
	bi.biClrUsed = 0;
	bi.biClrImportant = 0;
	dwBmBitsSize = ((Bitmap.bmWidth*wBitCount + 31) / 32) * 4 * Bitmap.bmHeight;

	//����λͼ�ļ�
	fh = CreateFile(lpFileName, GENERIC_WRITE, 0, NULL, CREATE_ALWAYS,
		FILE_ATTRIBUTE_NORMAL | FILE_FLAG_SEQUENTIAL_SCAN, NULL);
	if (fh == INVALID_HANDLE_VALUE)
		return   FALSE;
	//����λͼ�ļ�ͷ
	bmfHdr.bfType = 0x4D42;   //   "BM"
	dwDIBSize = sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER) + dwBmBitsSize;
	bmfHdr.bfSize = dwDIBSize;
	bmfHdr.bfReserved1 = 0;
	bmfHdr.bfReserved2 = 0;
	bmfHdr.bfOffBits = (DWORD)sizeof(BITMAPFILEHEADER) + (DWORD)sizeof(BITMAPINFOHEADER);

	//д��λͼ�ļ�ͷ
	WriteFile(fh, (LPSTR)&bmfHdr, sizeof(BITMAPFILEHEADER), &dwWritten, NULL);
	//д��λͼ��Ϣͷ
	WriteFile(fh, (LPSTR)&bi, sizeof(BITMAPINFOHEADER), &dwWritten, NULL);

	//��ȡλͼ����
	lpmem = new   char[dwBmBitsSize];
	lpbk = (LPSTR)   new   char[dwBmBitsSize];
	GetBitmapBits(hBitmap, dwBmBitsSize, lpmem);//������ڴ�ͼ������

	//ת��Ϊ��������(����bmHeightΪ��ʱ��Ҫ)
	for (int i = 0; i < Bitmap.bmHeight; i++)
	{
		memcpy(lpbk + Bitmap.bmWidth*i * 4, lpmem + Bitmap.bmWidth*(Bitmap.bmHeight - i - 1) * 4, Bitmap.bmWidth * 4);
	}
	//дλͼ����
	WriteFile(fh, lpbk, dwBmBitsSize, &dwWritten, NULL);

	//���
	delete[]lpbk;
	delete[]lpmem;

	CloseHandle(fh);
	return   TRUE;
}