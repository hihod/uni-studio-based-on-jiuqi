#include "stdafx.h"
#include <windows.h>
#include "comutil.h"
#include "CommDlg.h"
#include "Shlobj.h"
#include "utils\JNIUitls.h"
#include "UIADialogLibrary.h"

string UIADialogLibrary::InputDialog(string title, string label, vector<string> items, bool isPassword) {
	return "";
}

string UIADialogLibrary::MessageDialog(string title, string message, LONG64 btnSuite) {
	UINT uType = (UINT)btnSuite;
	int returnCode = MessageBox(NULL, _bstr_t(message.data()), _bstr_t(title.data()), uType);
	switch (returnCode) {
	case IDYES:		return("Yes");
	case IDNO:		return("No");
	case IDABORT:	return("Abort");
	case IDCANCEL:	return("Cancel");
	case IDIGNORE:	return("Ignore");
	case IDOK:		return("Ok");
	case IDRETRY:	return("Retry");
	default:		return NULL;
	}
}

string UIADialogLibrary::FileDialog(string initialDir, string filters) {
	TCHAR szBuffer[MAX_PATH] = { 0 };
	OPENFILENAME file = { 0 };

	file.lStructSize = sizeof(file);
	file.lpstrFile = szBuffer;							//路径缓冲区
	file.lpstrFilter = _bstr_t(filters.data());			//文件后缀
	file.lpstrInitialDir = _bstr_t(initialDir.data());	//初始路径
	file.nMaxFile = sizeof(szBuffer) / sizeof(*szBuffer);
	file.nFilterIndex = 0;
	file.Flags = OFN_PATHMUSTEXIST | OFN_FILEMUSTEXIST | OFN_EXPLORER;	//多选加OFN_ALLOWMULTISELECT
	BOOL bSel = GetOpenFileName(&file);

	wstring s = file.lpstrFile;
	return wstring2string(s);
}

string UIADialogLibrary::DirectoryDialog(string initialDir) {
	TCHAR szBuffer[MAX_PATH] = { 0 };
	BROWSEINFO brsInfo;
	ZeroMemory(&brsInfo, sizeof(BROWSEINFO));

	brsInfo.hwndOwner = NULL;
	brsInfo.pidlRoot = NULL;
	brsInfo.pszDisplayName = szBuffer;
	brsInfo.lpszTitle = _bstr_t("选择文件夹");
	brsInfo.ulFlags = BIF_RETURNFSANCESTORS;

	PIDLIST_ABSOLUTE pIDLIST = SHBrowseForFolder(&brsInfo);
	if (pIDLIST != NULL) {
		SHGetPathFromIDList(pIDLIST, szBuffer);

		wstring s = szBuffer;
		return wstring2string(s);
	}

	return "";
}