#pragma once

class UIAImageLibrary {
private:
	HDC   hScrDC, hMemDC; //��Ļ���ڴ��豸������
	HBITMAP   hBitmap, hOldBitmap;//λͼ���
	int   nX, nY, nX2, nY2; //ѡ����������
	int   nWidth, nHeight; //λͼ��Ⱥ͸߶�
	int   xScrn, yScrn; //��Ļ��Ⱥ͸߶�
public:
	UIAImageLibrary();
	~UIAImageLibrary();
	DWORD doSnapShot(BYTE** result, RECT rect);
};
