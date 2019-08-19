#pragma once
#include "utils\JNIUitls.h"
#include "library\UIAObj.h"
using namespace std;

const string BROWSER_NAME_IEXPLORE = "iexplore.exe";
const string BROWSER_NAME_CHROME = "chrome.exe";
const string BROWSER_NAME_FIREFOX = "firefox.exe";
const string BROWSER_NAMES[3] = { BROWSER_NAME_IEXPLORE, BROWSER_NAME_CHROME, BROWSER_NAME_FIREFOX };

class BrowserHelper {
private:
	UIAObj *uiaObj;
private:
	void SplitStr(string str, vector<string> *vector, string s);
	bool Matched(BrowserInfo source, BrowserInfo target);
private:
	void GetBrowserWindowElement(IUIAutomationElement *uiElement, IUIAutomationElement **uiWindowElement);
	bool GetBrowserWindowElementByParent(IUIAutomationElement *uiElement, IUIAutomationElement **uiWindowElement);
	bool GetBrowserWindowElementByProcessId(IUIAutomationElement *uiElement, IUIAutomationElement **uiWindowElement);

	void GetBrowserClientElementByWindowElement(IUIAutomationElement *uiWindowElement, IUIAutomationElement **uiClientElement);
	void GetBrowserClientElementByWindowElement4IEXPLORE(IUIAutomationElement *uiElement, IUIAutomationElement **uiClientElement);
	void GetBrowserClientElementByWindowElement4CHROME(IUIAutomationElement *uiElement, IUIAutomationElement **uiClientElement);
	void GetBrowserClientElementByWindowElement4FIREFOX(IUIAutomationElement *uiElement, IUIAutomationElement **uiClientElement);

	void GetBrowserInfoByWindowElement(IUIAutomationElement *uiWindowElement, BrowserInfo *browserInfo);
	void GetBrowserInfoByWindowElement4IEXPLORE(IUIAutomationElement *uiWindowElement, BrowserInfo *browserInfo);
	void GetBrowserInfoByWindowElement4CHROME(IUIAutomationElement *uiWindowElement, BrowserInfo *browserInfo);
	void GetBrowserInfoByWindowElement4FIREFOX(IUIAutomationElement *uiWindowElement, BrowserInfo *browserInfo);
public:
	BrowserHelper(UIAObj *uiaObj);

	string GetAppName(IUIAutomationElement *uiElement);
	string GetAppTitle(IUIAutomationElement *uiElement);
	bool IsBrowser(IUIAutomationElement *uiElement);

	void GetBrowserInfo(IUIAutomationElement *uiElement, BrowserInfo *browserInfo);
	void GetBrowserClientElement(IUIAutomationElement *uiElement, IUIAutomationElement **uiClientElement);
	void GetBrowserClientElement(BrowserInfo browserInfo, IUIAutomationElement **uiClientElement);
	HRESULT GetBrowserWindowElement(BrowserInfo browserInfo, IUIAutomationElement ** windowElement);
};