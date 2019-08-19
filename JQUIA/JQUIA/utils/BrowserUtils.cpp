#include "stdafx.h"
#include <comdef.h>
#include <algorithm>
#include <iostream>
#include "utils\StringUtils.h"
#include "utils\Logger.h"
#include "BrowserUtils.h"

BrowserHelper::BrowserHelper(UIAObj *uiaObj) {
	this->uiaObj = uiaObj;
}

string BrowserHelper::GetAppName(IUIAutomationElement *uiElement) {
	int processId;
	HRESULT hResult = uiElement->get_CurrentProcessId(&processId);
	if (FAILED(hResult))
		throw string("获取元素进程号失败");

	string appName = processid2application(processId);
	UIALog("processId:" + processId, LV_DEBUG);
	transform(appName.begin(), appName.end(), appName.begin(), tolower);
	UIALog("AppName:" + appName, LV_DEBUG);
	return appName;
}
string BrowserHelper::GetAppTitle(IUIAutomationElement *uiElement) {
	UIA_HWND hwnd = NULL;
	HRESULT hresult = uiElement->get_CurrentNativeWindowHandle(&hwnd);
	if (!FAILED(hresult)) {
		char szWindowTitle[4096];
		::GetWindowTextA((HWND)hwnd, szWindowTitle, sizeof(szWindowTitle));
		string str = szWindowTitle;
		return str;
	}
	return "";
}

bool BrowserHelper::IsBrowser(IUIAutomationElement *uiElement) {
	string appName = GetAppName(uiElement);
	int len = sizeof(BROWSER_NAMES) / sizeof(BROWSER_NAMES[0]);
	for (int i = 0; i < len; i++) {
		if (BROWSER_NAMES[i] == appName)
			return TRUE;
	}

	return FALSE;
}

void BrowserHelper::GetBrowserClientElementByWindowElement(IUIAutomationElement *uiWindowElement, IUIAutomationElement **uiClientElement) {
	string appName = GetAppName(uiWindowElement);
	if (appName == BROWSER_NAME_IEXPLORE)
		GetBrowserClientElementByWindowElement4IEXPLORE(uiWindowElement, uiClientElement);
	else if (appName == BROWSER_NAME_CHROME)
		GetBrowserClientElementByWindowElement4CHROME(uiWindowElement, uiClientElement);
	else if (appName == BROWSER_NAME_FIREFOX)
		GetBrowserClientElementByWindowElement4FIREFOX(uiWindowElement, uiClientElement);
	else
		throw string("未知的浏览器[" + appName + "]");
}

void BrowserHelper::GetBrowserClientElement(IUIAutomationElement *uiElement, IUIAutomationElement **uiClientElement) {
	//获取指定元素的WindowElement
	IUIAutomationElement *uiWindowElement;
	GetBrowserWindowElement(uiElement, &uiWindowElement);
	//通过WindowElement获取ClientElement
	GetBrowserClientElementByWindowElement(uiWindowElement, uiClientElement);

	uiWindowElement->Release();
}

void BrowserHelper::GetBrowserClientElementByWindowElement4IEXPLORE(IUIAutomationElement *uiWindowElement, IUIAutomationElement **uiClientElement) {
	VARIANT propVal;
	propVal.vt = VT_BSTR;
	propVal.bstrVal = _bstr_t("Internet Explorer_Server");

	IUIAutomationCondition *newCondition;
	HRESULT hResult = uiaObj->GetAutomation()->CreatePropertyCondition(UIA_ClassNamePropertyId, propVal, &newCondition);
	if (FAILED(hResult))
		throw string("创建查找条件失败");

	IUIAutomationElement *tmpElement;
	hResult = uiWindowElement->FindFirst(TreeScope_Descendants, newCondition, &tmpElement);
	if (FAILED(hResult) || !tmpElement)
		throw string("按ClassName查找元素失败");
	newCondition->Release();

	*uiClientElement = tmpElement;
}

void BrowserHelper::GetBrowserClientElementByWindowElement4CHROME(IUIAutomationElement *uiWindowElement, IUIAutomationElement **uiClientElement) {
	VARIANT propVal;
	propVal.vt = VT_BSTR;
	propVal.bstrVal = _bstr_t("Chrome_RenderWidgetHostHWND");

	IUIAutomationCondition *newCondition;
	HRESULT hResult = uiaObj->GetAutomation()->CreatePropertyCondition(UIA_ClassNamePropertyId, propVal, &newCondition);
	if (FAILED(hResult))
		throw string("创建查找条件失败");

	IUIAutomationElement *tmpElement;

	VARIANT tmp;

	hResult = ((IUIAutomationPropertyCondition*)newCondition)->get_PropertyValue(&tmp);
	if (FAILED(hResult)) {
		cout << "getPropertyValueFailed" << endl;
	}
	else {
		if (tmp.vt == VT_BSTR) {
			cout << "ConditionPropertyValue" << ::_com_util::ConvertBSTRToString(tmp.bstrVal) << endl;
		}
		else {
			cout << "not bstr" << endl;
		}
	}
	UIA_HWND hwnd = NULL;
	hResult = uiWindowElement->get_CurrentNativeWindowHandle(&hwnd);
	if (FAILED(hResult)) {
		cout << "get_CurrentNativeWindowHandle Failed" << endl;
	}
	else {
		cout << "HWND:" << hwnd << endl;
	}

	hResult = uiWindowElement->FindFirst(TreeScope_Descendants, newCondition, &tmpElement);
	if (FAILED(hResult) || !tmpElement)
		throw string("按ClassName查找浏览器客户区元素失败");
	newCondition->Release();

	*uiClientElement = tmpElement;
}

void BrowserHelper::GetBrowserClientElementByWindowElement4FIREFOX(IUIAutomationElement *uiWindowElement, IUIAutomationElement **uiClientElement) {
	VARIANT propVal;
	propVal.vt = VT_I4;
	propVal.intVal = UIA_DocumentControlTypeId;

	IUIAutomationCondition *newCondition;
	HRESULT hResult = uiaObj->GetAutomation()->CreatePropertyCondition(UIA_ControlTypePropertyId, propVal, &newCondition);
	if (FAILED(hResult))
		throw string("创建查找条件失败");

	IUIAutomationElement *tmpElement;
	hResult = uiWindowElement->FindFirst(TreeScope_Descendants, newCondition, &tmpElement);
	if (FAILED(hResult) || !uiClientElement)
		throw string("按ControlType查找浏览器客户区元素失败");
	newCondition->Release();

	*uiClientElement = tmpElement;
}

void BrowserHelper::GetBrowserInfo(IUIAutomationElement *uiElement, BrowserInfo *browserInfo) {
	//获取指定元素的WindowElement
	IUIAutomationElement *uiWindowElement;
	GetBrowserWindowElement(uiElement, &uiWindowElement);
	//在WindowElement下获取浏览器信息
	GetBrowserInfoByWindowElement(uiWindowElement, browserInfo);
}

void BrowserHelper::GetBrowserWindowElement(IUIAutomationElement *uiElement, IUIAutomationElement **uiWindowElement) {
	if (!GetBrowserWindowElementByParent(uiElement, uiWindowElement)) {
		UIALog("按Parent方式查找窗口元素失败", LV_DEBUG);
		if (!GetBrowserWindowElementByProcessId(uiElement, uiWindowElement)) {
			UIALog("按ProcessId方式查找窗口元素失败", LV_DEBUG);
			throw string("未找到指定元素的窗口元素");
		}
	}

	BSTR bstrVal;
	(*uiWindowElement)->get_CurrentName(&bstrVal);
}

bool BrowserHelper::GetBrowserWindowElementByParent(IUIAutomationElement *uiElement, IUIAutomationElement **uiWindowElement) {
	//父节点与根节点相同的为WindowElement
	IUIAutomationElement *rootElement;
	HRESULT hResult = uiaObj->GetAutomation()->GetRootElement(&rootElement);
	if (FAILED(hResult))
		throw string("获取根元素失败");

	IUIAutomationElement *pElement = uiElement;

	BOOL pAreSame = FALSE;
	while (!pAreSame) {
		uiElement = pElement;
		hResult = uiaObj->GetWalker()->GetParentElement(uiElement, &pElement);

		if (FAILED(hResult))
			throw string("获取父级元素失败");
		if (!pElement)
			break;

		hResult = uiaObj->GetAutomation()->CompareElements(pElement, rootElement, &pAreSame);
		if (FAILED(hResult))
			throw string("比较两个元素失败");
	}

	if (!pAreSame) {
		UIALog("未找到对应的WindowElement", LV_DEBUG);
		return FALSE;
	}

	*uiWindowElement = uiElement;
	return TRUE;
}

bool BrowserHelper::GetBrowserWindowElementByProcessId(IUIAutomationElement *uiElement, IUIAutomationElement **uiWindowElement) {
	//父节点与根节点相同的为WindowElement
	IUIAutomationElement *rootElement;
	HRESULT hResult = uiaObj->GetAutomation()->GetRootElement(&rootElement);
	if (FAILED(hResult))
		throw string("获取根元素失败");

	int processid = 0;
	hResult = uiElement->get_CurrentProcessId(&processid);
	if (FAILED(hResult))
		throw string("获取元素ProcessId失败");

	boolean isFinded = false;
	IUIAutomationTreeWalker *_treeWalker = NULL;
	IUIAutomationElement *crtElem = NULL;

	//创建树形遍历器
	uiaObj->GetAutomation()->get_ControlViewWalker(&_treeWalker);
	_treeWalker->GetFirstChildElement(rootElement, &crtElem);
	while (crtElem != NULL) {
		int crtPID = 0;
		crtElem->get_CurrentProcessId(&crtPID);
		if (crtPID == processid) {
			uiElement = crtElem;
			isFinded = true;
			break;
		}
		IUIAutomationElement *tmpNext = NULL;
		_treeWalker->GetNextSiblingElement(crtElem, &tmpNext);
		crtElem->Release();
		crtElem = tmpNext;
	}
	_treeWalker->Release();
	crtElem = NULL;
	if (isFinded == false) {
		UIALog("未找到对应的WindowElement", LV_DEBUG);
		return FALSE;
	}

	*uiWindowElement = uiElement;
	return TRUE;
}

void BrowserHelper::GetBrowserInfoByWindowElement(IUIAutomationElement *uiWindowElement, BrowserInfo *browserInfo) {
	string appName = GetAppName(uiWindowElement);
	browserInfo->application = appName;

	if (appName == BROWSER_NAME_IEXPLORE)
		GetBrowserInfoByWindowElement4IEXPLORE(uiWindowElement, browserInfo);
	else if (appName == BROWSER_NAME_CHROME)
		GetBrowserInfoByWindowElement4CHROME(uiWindowElement, browserInfo);
	else if (appName == BROWSER_NAME_FIREFOX)
		GetBrowserInfoByWindowElement4FIREFOX(uiWindowElement, browserInfo);
}

void BrowserHelper::SplitStr(string str, vector<string> *vector, string s) {
	char *_String = const_cast<char *>(str.data());
	char *_Delimiter = const_cast<char *>(s.data());
	char *_Context;

	char *c = strtok_s(_String, _Delimiter, &_Context);
	while (c) {
		vector->insert(vector->end(), c);
		c = strtok_s(_Context, _Delimiter, &_Context);
	}
}

void BrowserHelper::GetBrowserInfoByWindowElement4IEXPLORE(IUIAutomationElement *uiWindowElement, BrowserInfo *browserInfo) {
	VARIANT propVal;

	//从浏览器当前选中页签获取信息
	propVal.vt = VT_I4;
	propVal.intVal = UIA_PaneControlTypeId;
	IUIAutomationCondition *paneCondition = NULL;
	HRESULT hResult = uiaObj->GetAutomation()->CreatePropertyCondition(UIA_ControlTypePropertyId, propVal, &paneCondition);
	if (FAILED(hResult))
		throw string("创建paneCondition条件失败");

	propVal.vt = VT_BSTR;
	propVal.bstrVal = _bstr_t("InternetExplorer");
	IUIAutomationCondition *framewordIdCondition = NULL;
	hResult = uiaObj->GetAutomation()->CreatePropertyCondition(UIA_FrameworkIdPropertyId, propVal, &framewordIdCondition);
	if (FAILED(hResult))
		throw string("创建framewordIdCondition条件失败");

	IUIAutomationCondition *andCondition = NULL;
	hResult = uiaObj->GetAutomation()->CreateAndCondition(paneCondition, framewordIdCondition, &andCondition);
	if (FAILED(hResult))
		throw string("创建addCondition条件失败");

	IUIAutomationElement *paneElement;
	hResult = uiWindowElement->FindFirst(TreeScope_Descendants, andCondition, &paneElement);
	if (FAILED(hResult) || !paneElement)
		throw string("查找paneElement失败");

	andCondition->Release();
	framewordIdCondition->Release();
	paneCondition->Release();

	hResult = paneElement->GetCurrentPropertyValue(UIA_LegacyIAccessibleNamePropertyId, &propVal);
	if (FAILED(hResult))
		throw string("获取UIA_LegacyIAccessibleName失败");
	browserInfo->title = uiaObj->Variant2string(propVal);
	hResult = paneElement->GetCurrentPropertyValue(UIA_LegacyIAccessibleValuePropertyId, &propVal);
	if (FAILED(hResult))
		throw string("获取UIA_LegacyIAccessibleValue失败");

	
	browserInfo->url = uiaObj->Variant2string(propVal);
	paneElement->Release();
}

void BrowserHelper::GetBrowserInfoByWindowElement4CHROME(IUIAutomationElement *uiWindowElement, BrowserInfo *browserInfo) {
	VARIANT propVal;

	//获取标题
	BSTR className = NULL;
	HRESULT hResult = uiWindowElement->get_CurrentClassName(&className);
	if (FAILED(hResult))
		throw string("获取ClassName失败");

	//是否CHROME窗体类名
	if (strcmp(_com_util::ConvertBSTRToString(className), "Chrome_WidgetWin_1") != 0)
		return;

	HRESULT hResultr = uiWindowElement->GetCurrentPropertyValue(UIA_LegacyIAccessibleNamePropertyId, &propVal);
	if (FAILED(hResultr))
		throw string("获取LegacyIAccessibleValue失败");
	string chromeTitle = uiaObj->Variant2string(propVal);

	//获取URL
	propVal.vt = VT_BSTR;
	propVal.bstrVal = _bstr_t("Ctrl+L");
	IUIAutomationCondition *AccessibleKeyCondition;
	hResult = uiaObj->GetAutomation()->CreatePropertyCondition(UIA_AccessKeyPropertyId, propVal, &AccessibleKeyCondition);
	if (FAILED(hResult))
		throw string("创建UIA_AccessKeyPropertyId条件失败");
	IUIAutomationElement *urlElement;
	hResult = uiWindowElement->FindFirst(TreeScope_Descendants, AccessibleKeyCondition, &urlElement);
	if (FAILED(hResult))
		throw string("查找地址文本框失败");
	AccessibleKeyCondition->Release();

	if (urlElement == NULL)
		return;

	hResult = urlElement->GetCurrentPropertyValue(UIA_LegacyIAccessibleValuePropertyId, &propVal);
	if (FAILED(hResult))
		throw string("查找地址文本框失败");

	if (FAILED(hResult))
		throw string("获取LegacyIAccessibleValue失败");
	urlElement->Release();
	string chromeUrl = uiaObj->Variant2string(propVal);

	//特殊处理
	string TITLE_SUFFIX = " - Google Chrome";
	if (EndsWith(chromeTitle, TITLE_SUFFIX))
		chromeTitle = chromeTitle.substr(0, chromeTitle.size() - TITLE_SUFFIX.size());
	browserInfo->title = chromeTitle;

	if (!StartsWith(chromeUrl, "http"))
		chromeUrl.insert(0, "http://");
	browserInfo->url = chromeUrl;
}

void getDocumentControlByWalkTree(IUIAutomationElement *root, IUIAutomationElement *docControlEle) {
	IUIAutomationElement *crtElem = NULL;
	int length = 0;
	IUIAutomation *_automation = NULL;
	IUIAutomationTreeWalker *_treeWalker = NULL;
	CoInitialize(NULL);
	//创建自动化对象
	HRESULT hResult = CoCreateInstance(__uuidof(CUIAutomation8), NULL, CLSCTX_INPROC_SERVER, IID_PPV_ARGS(&_automation));
	if (FAILED(hResult))
		throw string("创建自动化对象失败");

	//创建树形遍历器
	_automation->get_ControlViewWalker(&_treeWalker);
	_treeWalker->GetFirstChildElement(root, &crtElem);
	while (crtElem != NULL) {
		VARIANT controlTypeV;
		crtElem->GetCurrentPropertyValue(UIA_ControlTypePropertyId, &controlTypeV);
		if (controlTypeV.intVal == UIA_DocumentControlTypeId) {
			docControlEle = crtElem;
			return;
		}
		getDocumentControlByWalkTree(crtElem, docControlEle);
		IUIAutomationElement *next = NULL;
		_treeWalker->GetNextSiblingElement(crtElem, &next);
		crtElem->Release();
		crtElem = next;
	}
	_treeWalker->Release();
	_automation->Release();
}

void BrowserHelper::GetBrowserInfoByWindowElement4FIREFOX(IUIAutomationElement *uiWindowElement, BrowserInfo *browserInfo) {
	//查找元素，并从该元素获取信息
	VARIANT propVal;
	propVal.vt = VT_I4;
	propVal.intVal = UIA_DocumentControlTypeId;

	IUIAutomationCondition *newCondition;
	HRESULT hResult = uiaObj->GetAutomation()->CreatePropertyCondition(UIA_ControlTypePropertyId, propVal, &newCondition);
	if (FAILED(hResult))
		throw string("创建查找条件失败");

	IUIAutomationElement *uiElement = NULL;
	hResult = uiWindowElement->FindFirst(TreeScope_Subtree, newCondition, &uiElement);
	newCondition->Release();
	if (FAILED(hResult))
		throw string("按FrameworkId查找失败");

	int count = 0;
	while (uiElement == NULL) {
		getDocumentControlByWalkTree(uiWindowElement, uiElement);
		UIALog("查找document失败，1秒后重新查找", LV_DEBUG);
		Sleep(1000);
		if (count++ > 2)
			return;
	}

	//获取标题
	hResult = uiElement->GetCurrentPropertyValue(UIA_LegacyIAccessibleNamePropertyId, &propVal);
	if (FAILED(hResult))
		throw string("获取LegacyIAccessibleName失败");

	browserInfo->title = uiaObj->Variant2string(propVal);
	//获取URL
	hResult = uiElement->GetCurrentPropertyValue(UIA_LegacyIAccessibleValuePropertyId, &propVal);
	if (FAILED(hResult))
		throw string("获取LegacyIAccessibleValue失败");

	browserInfo->url = uiaObj->Variant2string(propVal);

	uiElement->Release();
}

bool BrowserHelper::Matched(BrowserInfo source, BrowserInfo target) {
	bool matched = TRUE;
	if (!target.application.empty())
		matched = matched && target.application == source.application;
	if (!target.title.empty())
		matched = matched && target.title == source.title;
	if (!target.url.empty()) {
		string targetUrl = target.url;
		if (EndsWith(targetUrl, "/"))
			targetUrl.pop_back();

		if (!source.url.empty()) {
			string sourceUrl = source.url;
			if (EndsWith(sourceUrl, "/"))
				sourceUrl.pop_back();

			matched = matched && StringMatch(targetUrl,sourceUrl);
		}
		else
			matched = FALSE;
	}

	return matched;
}

void BrowserHelper::GetBrowserClientElement(BrowserInfo browserInfo, IUIAutomationElement **uiClientElement) {
	IUIAutomationElement *windowElement = NULL;
	HRESULT hresult = GetBrowserWindowElement(browserInfo, &windowElement);
	if (FAILED(hresult) || windowElement == NULL) {
		throw string("获取浏览器窗口失败");
	}
	GetBrowserClientElementByWindowElement(windowElement, uiClientElement);
	return;
}

HRESULT BrowserHelper::GetBrowserWindowElement(BrowserInfo browserInfo, IUIAutomationElement **windowElement) {
	IUIAutomationElement *rootElement;
	HRESULT hResult = uiaObj->GetAutomation()->GetRootElement(&rootElement);
	if (FAILED(hResult))
		throw string("获取根元素失败");

	IUIAutomationElement *uiWindowElement;
	hResult = uiaObj->GetWalker()->GetFirstChildElement(rootElement, &uiWindowElement);
	if (FAILED(hResult))
		throw string("获取首个子元素失败");

	//遍历所有WindowElement
	while (uiWindowElement) {
		//如果是浏览器，开始匹配
		if (IsBrowser(uiWindowElement)) {
			BrowserInfo tempInfo;
			try {
				GetBrowserInfoByWindowElement(uiWindowElement, &tempInfo);
			}
			catch (...) {
			}
			if (Matched(tempInfo, browserInfo)) {
				//匹配上，获取该浏览器的ClientElement
				*windowElement = uiWindowElement;
				return S_OK;
			}
		}

		//非浏览器，取下一个
		hResult = uiaObj->GetWalker()->GetNextSiblingElement(uiWindowElement, &uiWindowElement);
		if (FAILED(hResult))
			throw string("获取下一个兄弟元素失败");
	}
	return E_FAIL;
}