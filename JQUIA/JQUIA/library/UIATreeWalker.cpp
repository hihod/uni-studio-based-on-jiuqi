#include "stdafx.h"
#include <comdef.h>
#include "UIATreeWalker.h"

UIATreeWalker::UIATreeWalker(UIAObj *uiaObj) {
	this->uiaObj = uiaObj;
}

LONG64 UIATreeWalker::GetRoot() {
	IUIAutomationElement *rootElement;
	HRESULT hResult = uiaObj->GetAutomation()->GetRootElement(&rootElement);
	if (FAILED(hResult))
		throw string("获取根元素失败");

	return (LONG64)rootElement;
}
void UIATreeWalker::GetChildren(LONG64 id, vector<LONG64> *children) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

	IUIAutomationElement *childElement = NULL;
	HRESULT hResult = uiaObj->GetWalker()->GetFirstChildElement(uiElement, &childElement);
	if (FAILED(hResult))
		throw string("获取首个子元素失败");

	children->clear();
	while (childElement) {
		LONG64 id = (LONG64)childElement;
		children->push_back(id);

		hResult = uiaObj->GetWalker()->GetNextSiblingElement(childElement, &childElement);
		if (FAILED(hResult))
			throw string("获取下一个子元素失败");
	}
}

LONG64 UIATreeWalker::GetParent(LONG64 id) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

	IUIAutomationElement *parentElement = NULL;
	HRESULT hResult = uiaObj->GetWalker()->GetParentElement(uiElement, &parentElement);
	if (FAILED(hResult))
		throw string("获取父元素失败");

	return (LONG64)parentElement;
}

string UIATreeWalker::GetText(LONG64 id) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

	BSTR name, ctrlType;
	uiElement->get_CurrentName(&name);
	uiElement->get_CurrentLocalizedControlType(&ctrlType);

	string nameStr = name ? _com_util::ConvertBSTRToString(name) : "";
	string ctrlTypeStr = _com_util::ConvertBSTRToString(ctrlType);
	return "\"" + nameStr + "\" " + ctrlTypeStr;
}

void UIATreeWalker::GetProperties(LONG64 id, map<string, string> *properties) {
	uiaObj->GetProperties(id, properties);
}