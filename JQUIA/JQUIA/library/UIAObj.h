#pragma once
#include <map>
#include <string>
#include <vector>
#include "uiautomation.h"
using namespace std;

struct PropInfo {
	PROPERTYID id;
	string name;
	VARTYPE type;
};

class UIAObj {
private:
	IUIAutomation *_automation = NULL;
	IUIAutomationTreeWalker *_treeWalker = NULL;
public:
	UIAObj();
	~UIAObj();
private:
	void CacheProp(PROPERTYID propertyId, string defaultName);
	void CacheProp(PROPERTYID propertyId, string defaultName, VARTYPE type);
	void InitPropertyMap();
	void FreePropertyMap();
public:
	string GetRuntimeId(IUIAutomationElement *uiElement);
	CONTROLTYPEID GetControlType(IUIAutomationElement *uiElement);
public:
	IUIAutomation *GetAutomation();
	IUIAutomationTreeWalker *GetWalker();

	string Variant2string(VARIANT retVal);
	string IntegerArray2string(SAFEARRAY *pArray);
	string DoubleArray2string(SAFEARRAY *pArray);
	string StringArray2string(SAFEARRAY *pArray);

	string NameOf(PROPERTYID propertyId, string defaultName);
	PropInfo *PropOf(string propName);
	PropInfo *PropOf(PROPERTYID propID);

	IUIAutomationElement *Id2Element(LONG64 id);

	void GetProperties(LONG64 id, map<string, string> *properties);
	void GetProperties(IUIAutomationElement *uiElement, map<string, string> *properties);

	void ReleaseById(LONG64 id);
	void ReleaseByIds(vector<LONG64> ids);
};
