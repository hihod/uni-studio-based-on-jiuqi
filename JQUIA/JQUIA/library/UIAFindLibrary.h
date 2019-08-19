#pragma once
#include <string>
#include "basetsd.h"
#include "uiautomation.h"
#include "utils\JNIUitls.h"
#include "utils\BrowserUtils.h"
#include "library\UIAObj.h"
using namespace std;

const string PROP_NAME_IDX = "Idx";
const string PROP_NAME_WINDOW_APPNAME = "Application";
const long PROP_IDS_REQUIRED[3] = { UIA_ControlTypePropertyId, UIA_ClassNamePropertyId, UIA_NamePropertyId };
const long PROP_IDS_SUGGESTED[2] = { UIA_LegacyIAccessibleNamePropertyId, UIA_LegacyIAccessibleRolePropertyId/*, UIA_AutomationIdPropertyId*/ };

class UIAFindLibrary {
private:
	UIAObj *uiaObj;
	BrowserHelper *bcHelper;
private:
	IUIAutomationElement *GetGridByPoint(POINT pt);
	IUIAutomationElement *GetElementByPoint(POINT pt);
	void CreateElementCondition(PathElement pathElement, IUIAutomationCondition **elementCondition);
	void BuildPathElement(IUIAutomationElement *uiElement, PathElement *pathElement);
private:
	PathAttribute *TryCreateWindowApplicationAttribute(IUIAutomationElement *uiElement);
	PathAttribute *TryCreateIndexAttribute(int index);
	PathAttribute *TryCreatePropertyAttribute(IUIAutomationElement *uiElement, PROPERTYID propertyId, VARIANT *retVal);
	PathElement *TryCreateWindowElement(IUIAutomationElement *pElement, IUIAutomationElement *uiElement);
	PathElement *TryCreateControlElement(IUIAutomationElement *pElement, IUIAutomationElement *uiElement, TreeScope scope);
private:
	PathAttribute *GetApplicationAttribute(Path path);
	PathAttribute *PopIndexAttribute(PathElement *pathElement);
	bool GetWildcardAttributes(PathElement *pathElement, vector<PathAttribute *> *vector);
	bool FindFirstElement(IUIAutomationElement *pElement, PathElement pathElement, TreeScope scope, IUIAutomationElement **uiElement);
	bool FindFirstElementWithWildcard(IUIAutomationElement *pElement, PathElement pathElement, TreeScope scope, vector<PathAttribute *> vector, IUIAutomationElement **uiElement);
public:
	UIAFindLibrary(UIAObj *uiaObj);
	~UIAFindLibrary();
public:
	LONG64 Get(POINT pt);
	void GetRect(POINT pt, UIARect *uiaRect);
	void GetRect(BrowserInfo browserInfo, UIARect *rect);
	bool Exists(Path path);
	void GetBrowserInfo(POINT pt, BrowserInfo *browserInfo);
	void GetPath(LONG64 id, Path *path);
	LONG64 FindFirst(Path path);
	void FindAll(Path path, vector<LONG64> ids);

	LONG64 GetWindowByPath(Path path);
	LONG64 GetWindowByPoint(POINT pt);
	LONG64 GetWindowByElement(LONG64 id);
	LONG64 GetWindowByBrowserInfo(BrowserInfo info);
	LONG64 GetWindowByElement(IUIAutomationElement *uiElement);
	void GetWindowRect(POINT pt, UIARect *uiaRect);
};