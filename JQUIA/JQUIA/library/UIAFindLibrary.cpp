#include "stdafx.h"
#include "stdlib.h"
#include "comdef.h"
#include "algorithm"
#include "iostream"
#include "vector"
#include "uiautomation.h"
#include "utils\logger.h"
#include "utils\StringUtils.h"
#include "library\UIAFindLibrary.h"

UIAFindLibrary::UIAFindLibrary(UIAObj *uiaObj) {
	this->uiaObj = uiaObj;
	this->bcHelper = new BrowserHelper(uiaObj);
}

UIAFindLibrary::~UIAFindLibrary() {
	delete bcHelper;
}

IUIAutomationElement *UIAFindLibrary::GetGridByPoint(POINT pt) {
	IUIAutomationElement *uiElement = GetElementByPoint(pt);

	IUIAutomationElement *pElement;
	while (uiElement) {
		IUnknown *uiaPattern = NULL;
		HRESULT hResult = uiElement->GetCurrentPattern(UIA_GridPatternId, &uiaPattern);
		if (SUCCEEDED(hResult) && uiaPattern) {
			uiaPattern->Release();
			uiaPattern = NULL;

			return uiElement;
		}

		hResult = uiaObj->GetWalker()->GetParentElement(uiElement, &pElement);
		if (FAILED(hResult))
			throw string("获取父元素失败");

		uiElement->Release();
		uiElement = pElement;
	}

	return NULL;
}

IUIAutomationElement *UIAFindLibrary::GetElementByPoint(POINT pt) {
	IUIAutomationElement *uiElement;
	HRESULT hResult = uiaObj->GetAutomation()->ElementFromPoint(pt, &uiElement);
	if (FAILED(hResult) || !uiElement)
		throw string("根据点获取元素失败：%s" + POINT2string(pt));

	return uiElement;
}

void UIAFindLibrary::BuildPathElement(IUIAutomationElement *uiElement, PathElement *pathElement) {
	map<string, string> properties;
	uiaObj->GetProperties(uiElement, &properties);

	map<string, string>::iterator iter;
	for (iter = properties.begin(); iter != properties.end();) {
		string key = iter->first;
		string value = iter->second;

		PathAttribute *pathAttribute = new PathAttribute();
		pathAttribute->name = key;
		pathAttribute->value = value;
		pathAttribute->enable = TRUE;
		pathElement->attributes.insert(end(pathElement->attributes), pathAttribute);
	}
}

void UIAFindLibrary::CreateElementCondition(PathElement pathElement, IUIAutomationCondition **elementCondition) {
	IUIAutomationCondition *tempCondition = NULL;

	for (int i = 0; i < pathElement.attributes.size(); i++) {
		PathAttribute *pathAttribute = pathElement.attributes[i];
		if (!pathAttribute->enable)
			continue;

		VARIANT value;
		string propertyName = pathAttribute->name;

		PropInfo *prop = uiaObj->PropOf(propertyName);
		value.vt = prop->type;
		switch (value.vt) {
		case VT_I4:
			value.intVal = atoi(pathAttribute->value.data());
			break;
		case VT_R8:
			value.dblVal = atof(pathAttribute->value.data());
			break;
		case VT_BSTR:
			value.bstrVal = _com_util::ConvertStringToBSTR(pathAttribute->value.data());
			break;
		default:
			value.bstrVal = _com_util::ConvertStringToBSTR(pathAttribute->value.data());
		}

		IUIAutomationCondition *newCondition;
		HRESULT hResult = uiaObj->GetAutomation()->CreatePropertyCondition(prop->id, value, &newCondition);
		if (FAILED(hResult))
			throw string("创建属性条件失败：" + prop->name);

		if (tempCondition) {
			hResult = uiaObj->GetAutomation()->CreateAndCondition(tempCondition, newCondition, &tempCondition);
			if (FAILED(hResult))
				throw string("创建属性并且条件失败：" + prop->name);
		}
		else
			tempCondition = newCondition;
	}

	*elementCondition = tempCondition;
}

PathAttribute *UIAFindLibrary::TryCreateWindowApplicationAttribute(IUIAutomationElement *uiElement) {
	string appName = bcHelper->GetAppName(uiElement);
	if (!appName.empty()) {
		PathAttribute *appNameAttribute = new PathAttribute();
		appNameAttribute->name = PROP_NAME_WINDOW_APPNAME;
		appNameAttribute->value = appName;
		appNameAttribute->enable = TRUE;
		return appNameAttribute;
	}

	return NULL;
}

PathAttribute *UIAFindLibrary::TryCreateIndexAttribute(int index) {
	PathAttribute *pathAttribute = new PathAttribute();
	pathAttribute->name = PROP_NAME_IDX;
	pathAttribute->value = to_string(index);
	pathAttribute->enable = true;
	return pathAttribute;
}

PathAttribute *UIAFindLibrary::TryCreatePropertyAttribute(IUIAutomationElement *uiElement, PROPERTYID propertyId, VARIANT *retVal) {
	string propertyName = uiaObj->PropOf(propertyId)->name;

	HRESULT hResult = uiElement->GetCurrentPropertyValue(propertyId, retVal);
	if (FAILED(hResult))
		throw string("获取元素属性值失败：" + propertyName);

	string valStr = uiaObj->Variant2string(*retVal);
	if (valStr.empty())
		return NULL;

	PathAttribute *pathAttribute = new PathAttribute();
	pathAttribute->name = propertyName;
	pathAttribute->value = valStr;
	pathAttribute->enable = true;
	return pathAttribute;
}

PathElement *UIAFindLibrary::TryCreateWindowElement(IUIAutomationElement *pElement, IUIAutomationElement *uiElement) {
	PathElement *pathElement = new PathElement();

	//控件类型
	VARIANT retVal;
	PathAttribute *controlTypeAttribute = TryCreatePropertyAttribute(uiElement, UIA_ControlTypePropertyId, &retVal);
	if (controlTypeAttribute)
		pathElement->attributes.insert(begin(pathElement->attributes), controlTypeAttribute);

	//窗口应用
	PathAttribute *appNameAttribute = TryCreateWindowApplicationAttribute(uiElement);
	if (appNameAttribute)
		pathElement->attributes.insert(begin(pathElement->attributes), appNameAttribute);

	//元素类名
	PathAttribute *classNameAttribute = TryCreatePropertyAttribute(uiElement, UIA_ClassNamePropertyId, &retVal);
	if (classNameAttribute)
		pathElement->attributes.insert(begin(pathElement->attributes), classNameAttribute);

	//窗口名称
	PathAttribute *nameAttribute = TryCreatePropertyAttribute(uiElement, UIA_NamePropertyId, &retVal);
	if (nameAttribute)
		pathElement->attributes.insert(end(pathElement->attributes), nameAttribute);

	return pathElement;
}

PathElement *UIAFindLibrary::TryCreateControlElement(IUIAutomationElement *pElement, IUIAutomationElement *uiElement, TreeScope scope) {
	PathElement *pathElement = new PathElement();

	/*必填属性*/
	IUIAutomationCondition *condition = NULL;
	int len = sizeof(PROP_IDS_REQUIRED) / sizeof(PROP_IDS_REQUIRED[0]);
	for (int i = 0; i < len; i++) {
		PROPERTYID propertyId = PROP_IDS_REQUIRED[i];
		string propertyName = uiaObj->PropOf(propertyId)->name;

		//构建路径属性
		VARIANT retVal;
		PathAttribute *pathAttribute = TryCreatePropertyAttribute(uiElement, propertyId, &retVal);
		if (!pathAttribute)
			continue;

		pathElement->attributes.insert(end(pathElement->attributes), pathAttribute);

		//当前属性构建条件
		IUIAutomationCondition *newCondition;
		HRESULT hResult = uiaObj->GetAutomation()->CreatePropertyCondition(propertyId, retVal, &newCondition);
		if (FAILED(hResult))
			throw string("创建属性过滤条件失败：" + propertyName);

		//加入当前元素的组合条件
		if (condition) {
			hResult = uiaObj->GetAutomation()->CreateAndCondition(condition, newCondition, &condition);
			if (FAILED(hResult))
				throw string("创建组合条件失败：" + propertyName);
		}
		else
			condition = newCondition;
	}

	//尝试用已拼接的条件查找
	IUIAutomationElement *tmpElement;
	HRESULT hResult = pElement->FindFirst(scope, condition, &tmpElement);
	if (FAILED(hResult))
		throw string("按条件查找元素失败");

	IUIAutomationElementArray *tmpElementArray;
	hResult = pElement->FindAll(scope, condition, &tmpElementArray);
	if (FAILED(hResult))
		throw string("按条件查找所有元素失败");

	int length;
	hResult = tmpElementArray->get_Length(&length);
	if (FAILED(hResult))
		throw string("按条件查找所有元素失败");

	//唯一确定的
	if (length == 1) {
		BOOL areSame = FALSE;
		if (tmpElement) {
			hResult = uiaObj->GetAutomation()->CompareElements(tmpElement, uiElement, &areSame);
			if (FAILED(hResult))
				throw string("比较元素失败");

			tmpElement->Release();
			if (areSame)
				return pathElement;
		}
	}

	/*推荐属性*/
	len = sizeof(PROP_IDS_SUGGESTED) / sizeof(PROP_IDS_SUGGESTED[0]);
	for (int i = 0; i < len; i++) {
		PROPERTYID propertyId = PROP_IDS_SUGGESTED[i];
		string propertyName = uiaObj->PropOf(propertyId)->name;

		//构建路径属性
		VARIANT retVal;
		PathAttribute *pathAttribute = TryCreatePropertyAttribute(uiElement, propertyId, &retVal);
		if (!pathAttribute)
			continue;

		pathElement->attributes.insert(end(pathElement->attributes), pathAttribute);

		//当前属性构建条件
		IUIAutomationCondition *newCondition;
		HRESULT hResult = uiaObj->GetAutomation()->CreatePropertyCondition(propertyId, retVal, &newCondition);
		if (FAILED(hResult))
			throw string("创建属性过滤条件失败：" + propertyName);

		//加入当前元素的组合条件
		if (condition) {
			hResult = uiaObj->GetAutomation()->CreateAndCondition(condition, newCondition, &condition);
			if (FAILED(hResult))
				throw string("创建组合条件失败：" + propertyName);
		}
		else
			condition = newCondition;

		//尝试用已拼接的条件查找
		IUIAutomationElement *tmpElement;
		hResult = pElement->FindFirst(scope, condition, &tmpElement);
		if (FAILED(hResult))
			throw string("按条件查找元素失败");

		IUIAutomationElementArray *tmpElementArray;
		hResult = pElement->FindAll(scope, condition, &tmpElementArray);
		if (FAILED(hResult))
			throw string("按条件查找所有元素失败");

		int length;
		hResult = tmpElementArray->get_Length(&length);
		if (FAILED(hResult))
			throw string("按条件查找所有元素失败");

		//唯一确定的
		if (length == 1) {
			BOOL areSame = FALSE;
			if (tmpElement) {
				hResult = uiaObj->GetAutomation()->CompareElements(tmpElement, uiElement, &areSame);
				if (FAILED(hResult))
					throw string("比较元素失败");

				tmpElement->Release();
				if (areSame)
					return pathElement;
			}
		}
	}

	hResult = pElement->FindAll(scope, condition, &tmpElementArray);
	if (FAILED(hResult))
		throw string("按条件查找所有元素失败");

	hResult = tmpElementArray->get_Length(&length);
	if (FAILED(hResult))
		throw string("按条件查找所有元素失败");

	for (int i = 0; i < length; i++) {
		IUIAutomationElement *tmpElement;
		tmpElementArray->GetElement(i, &tmpElement);

		BOOL areSame = FALSE;
		hResult = uiaObj->GetAutomation()->CompareElements(tmpElement, uiElement, &areSame);
		if (FAILED(hResult))
			throw string("比较元素失败");

		if (areSame) {
			if (i > 0) {
				PathAttribute *idxAttribute = TryCreateIndexAttribute(i);
				pathElement->attributes.insert(end(pathElement->attributes), idxAttribute);
			}
			return pathElement;
		}
	}

	return NULL;
}

PathAttribute *UIAFindLibrary::GetApplicationAttribute(Path path) {
	if (path.elements.size() > 0) {
		PathElement *pathElement = path.elements[0];

		for (int i = 0; i < pathElement->attributes.size(); i++) {
			PathAttribute *pathAttribute = pathElement->attributes[i];
			if (pathAttribute->name == PROP_NAME_WINDOW_APPNAME)
				return pathAttribute;
		}
	}

	return NULL;
}

PathAttribute *UIAFindLibrary::PopIndexAttribute(PathElement *pathElement) {
	for (int i = 0; i < pathElement->attributes.size(); i++) {
		PathAttribute *pathAttribute = pathElement->attributes[i];
		if (pathAttribute->name == PROP_NAME_IDX) {
			pathElement->attributes.erase(begin(pathElement->attributes) + i);
			return pathAttribute;
		}
	}

	return NULL;
}

bool UIAFindLibrary::GetWildcardAttributes(PathElement *pathElement, vector<PathAttribute *> *vector) {
	//遍历所有属性，检测是否含有通配符
	for (auto iter = pathElement->attributes.begin(); iter < pathElement->attributes.end();) {
		PathAttribute *pathAttribute = *iter;
		if (pathAttribute->enable) {
			string value = pathAttribute->value;

			//含有通配符的属性，提取出来
			if (value.find("*") != string::npos) {
				vector->insert(vector->end(), pathAttribute);

				//删除后iter已经指向下一个元素了，所以不能++了，直接continue
				iter = pathElement->attributes.erase(iter);
				continue;
			}
		}

		++iter;
	}

	//含有通配属性，返回TRUE
	return vector->size() > 0;
}

bool UIAFindLibrary::FindFirstElement(IUIAutomationElement *pElement, PathElement pathElement, TreeScope scope, IUIAutomationElement **uiElement) {
	//是否含有通配符
	vector<PathAttribute *> vector;
	BOOL hasWildcard = GetWildcardAttributes(&pathElement, &vector);

	//含通配符的处理
	if (hasWildcard)
		return FindFirstElementWithWildcard(pElement, pathElement, scope, vector, uiElement);

	//无通配符的处理
	else {
		PathAttribute *idxAttribuute = PopIndexAttribute(&pathElement);

		IUIAutomationCondition *elementCondition;
		CreateElementCondition(pathElement, &elementCondition);
		if (idxAttribuute) {
			int idx = atoi(idxAttribuute->value.data());

			IUIAutomationElementArray *elementArray;
			HRESULT hResult = pElement->FindAll(scope, elementCondition, &elementArray);
			elementCondition->Release();
			if (FAILED(hResult) || !elementArray)
				throw string("获取符合条件的元素数组失败");

			int length;
			hResult = elementArray->get_Length(&length);
			if (FAILED(hResult) || idx >= length)
				throw string("获取元素长度失败");

			hResult = elementArray->GetElement(idx, uiElement);
			if (FAILED(hResult))
				throw string("从数组指定索引[" + to_string(idx) + "]获取元素失败");
		}
		else {
			HRESULT hResult = pElement->FindFirst(scope, elementCondition, uiElement);
			elementCondition->Release();
			if (FAILED(hResult))
				return FALSE;
		}

		return *uiElement ? TRUE : FALSE;
	}
}

bool UIAFindLibrary::FindFirstElementWithWildcard(IUIAutomationElement *pElement, PathElement pathElement, TreeScope scope, vector<PathAttribute *> vector, IUIAutomationElement **uiElement) {
	//创建查找条件：依据去除通配符属性的PathElement
	IUIAutomationCondition *elementCondition;
	CreateElementCondition(pathElement, &elementCondition);

	//查找符合条件的所有元素
	IUIAutomationElementArray *uiElementArray;
	HRESULT hResult = pElement->FindAll(scope, elementCondition, &uiElementArray);
	elementCondition->Release();

	if (FAILED(hResult) || !uiElementArray)
		throw string("排除含通配符后，查找元素失败");

	int len;
	hResult = uiElementArray->get_Length(&len);
	if (FAILED(hResult))
		throw string("获取元素数组长度失败");

	//遍历所有元素
	for (int i = 0; i < len; i++) {
		IUIAutomationElement *tmpElement;
		hResult = uiElementArray->GetElement(i, &tmpElement);
		if (FAILED(hResult))
			throw string("从数组获取元素失败");

		//获取元素属性值，与对应通配字符串进行匹配
		BOOL matched = TRUE;
		for (int j = 0; j < vector.size(); j++) {
			PathAttribute *pathAttribute = vector[j];
			PropInfo *prop = uiaObj->PropOf(pathAttribute->name);

			VARIANT retVal;
			hResult = tmpElement->GetCurrentPropertyValue(prop->id, &retVal);
			if (FAILED(hResult))
				throw string("获取元素属性值失败[" + prop->name + "]");

			string ptn = pathAttribute->value;
			string str = uiaObj->Variant2string(retVal);

			//不匹配，则进行下一个元素的匹配
			if (!StringMatch(ptn, str)) {
				matched = FALSE;
				break;
			}
		}

		//匹配上
		if (matched) {
			*uiElement = tmpElement;

			uiElementArray->Release();
			return TRUE;
		}
	}

	uiElementArray->Release();
	return FALSE;
}

LONG64 UIAFindLibrary::Get(POINT pt) {
	IUIAutomationElement *uiElement = GetElementByPoint(pt);
	return (LONG64)uiElement;
}

void UIAFindLibrary::GetRect(POINT pt, UIARect *uiaRect) {
	IUIAutomationElement *uiElement = GetElementByPoint(pt);

	RECT rect;
	uiaRect->isBrowserClient = bcHelper->IsBrowser(uiElement);
	if (uiaRect->isBrowserClient) {
		UIALog("isBrowser[TRUE]", LV_DEBUG);

		IUIAutomationElement *uiClientElement;
		bcHelper->GetBrowserClientElement(uiElement, &uiClientElement);
		if (!uiClientElement)
			throw string("获取浏览器客户区元素失败");

		HRESULT hResult = uiClientElement->get_CurrentBoundingRectangle(&rect);
		if (FAILED(hResult))
			throw string("获取浏览器客户区元素边框失败");
	}
	else {
		UIALog("isBrowser[FALSE]", LV_DEBUG);

		HRESULT hResult = uiElement->get_CurrentBoundingRectangle(&rect);
		if (FAILED(hResult))
			throw string("获取浏览器客户区元素边框失败");
	}

	UIALog(RECT2string(rect), LV_DEBUG);

	uiaRect->left = rect.left;
	uiaRect->top = rect.top;
	uiaRect->right = rect.right;
	uiaRect->bottom = rect.bottom;

	uiElement->Release();
	uiElement = NULL;
}

void UIAFindLibrary::GetRect(BrowserInfo browserInfo, UIARect *uiaRect) {
	IUIAutomationElement *uiClientElement = NULL;
	bcHelper->GetBrowserClientElement(browserInfo, &uiClientElement);
	if (!uiClientElement)
		return;

	RECT rect;
	HRESULT hResult = uiClientElement->get_CurrentBoundingRectangle(&rect);
	if (FAILED(hResult))
		throw string("获取元素边框失败");

	uiaRect->left = rect.left;
	uiaRect->top = rect.top;
	uiaRect->right = rect.right;
	uiaRect->bottom = rect.bottom;
	uiaRect->isBrowserClient = TRUE;

	uiClientElement->Release();
	uiClientElement = NULL;
}

void UIAFindLibrary::GetBrowserInfo(POINT pt, BrowserInfo *browserInfo) {
	IUIAutomationElement *uiElement = GetElementByPoint(pt);
	bcHelper->GetBrowserInfo(uiElement, browserInfo);
}

void UIAFindLibrary::GetPath(LONG64 id, Path *path) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

	IUIAutomationElement *pElement;
	vector<IUIAutomationElement *> vector;
	while (uiElement) {
		vector.insert(begin(vector), uiElement);

		HRESULT hResult = uiaObj->GetWalker()->GetParentElement(uiElement, &pElement);
		if (FAILED(hResult))
			throw string("获取父元素失败");

		uiElement = pElement;
	}

	pElement = vector[0];
	path->elements.clear();
	for (int i = 1; i < vector.size(); i++) {
		uiElement = vector[i];
		CONTROLTYPEID controlType = uiaObj->GetControlType(uiElement);

		PathElement *pathElement;
		//窗口元素
		if (i == 1)
			pathElement = TryCreateWindowElement(pElement, uiElement);
		//其他元素
		else
			pathElement = TryCreateControlElement(pElement, uiElement, TreeScope_Descendants);

		if (!pathElement)
			continue;

		pathElement->runtimeId = uiaObj->GetRuntimeId(uiElement);
		pathElement->enable = TRUE;
		path->elements.insert(end(path->elements), pathElement);

		pElement = uiElement;
	}
}

bool UIAFindLibrary::Exists(Path path) {
	//获取路径中的Application属性
	PathAttribute *pathAttribute = GetApplicationAttribute(path);
	if (!pathAttribute || pathAttribute->value == "")
		throw string("未指定应用程序名称");

	//根据应用名称获取进程列表
	vector<DWORD> vector;
	application2processid(pathAttribute->value, &vector);
	if (vector.size() == 0)
		throw string("未找到指定的应用[" + pathAttribute->value + "]");

	//获取根元素，从根元素开始查找
	IUIAutomationElement *rootElement;
	HRESULT hResult = uiaObj->GetAutomation()->GetRootElement(&rootElement);
	if (FAILED(hResult) || !rootElement)
		throw string("获取根元素失败");

	//将Application属性修改为ProcessId属性
	PropInfo *prop = uiaObj->PropOf(UIA_ProcessIdPropertyId);
	pathAttribute->name = prop->name;

	//倒序遍历匹配到的所有ProcessId
	for (int i = (int)vector.size() - 1; i >= 0; i--) {
		DWORD processId = vector[i];

		//替换到属性
		pathAttribute->value = to_string(processId);

		//尝试在当前ProcessId下查找
		BOOL success = TRUE;
		IUIAutomationElement *uiElement = rootElement;
		for (int i = 0; i < path.elements.size(); i++) {
			PathElement *pathElement = path.elements[i];

			TreeScope scope = (i == 0) ? TreeScope_Children : TreeScope_Descendants;
			bool found = FindFirstElement(uiElement, *pathElement, scope, &uiElement);

			//当前ProcessId下没找到
			if (!found) {
				char msg[64];
				sprintf_s(msg, 64, "进程[%d]下未找到子元素：索引[%d]", processId, i);
				UIALog(msg, LV_INFO);

				success = FALSE;
				break;
			}
		}

		//找到返回，没找到则继续遍历
		if (success)
			return TRUE;
	}

	//没到到
	return FALSE;
}

LONG64 UIAFindLibrary::FindFirst(Path path) {
	//获取路径中的Application属性
	PathAttribute *pathAttribute = GetApplicationAttribute(path);
	if (!pathAttribute || pathAttribute->value == "")
		throw string("未指定应用程序名称");

	//根据应用名称获取进程列表
	vector<DWORD> vector;
	application2processid(pathAttribute->value, &vector);
	if (vector.size() == 0)
		throw string("未找到指定的应用[" + pathAttribute->value + "]");

	//获取根元素，从根元素开始查找
	IUIAutomationElement *rootElement;
	HRESULT hResult = uiaObj->GetAutomation()->GetRootElement(&rootElement);
	if (FAILED(hResult) || !rootElement)
		throw string("获取根元素失败");

	//将Application属性修改为ProcessId属性
	PropInfo *prop = uiaObj->PropOf(UIA_ProcessIdPropertyId);
	pathAttribute->name = prop->name;

	//倒序遍历匹配到的所有ProcessId
	for (int i = (int)vector.size() - 1; i >= 0; i--) {
		DWORD processId = vector[i];

		//替换到属性
		pathAttribute->value = to_string(processId);

		//尝试在当前ProcessId下查找
		BOOL success = TRUE;
		IUIAutomationElement *uiElement = rootElement;
		for (int i = 0; i < path.elements.size(); i++) {
			PathElement *pathElement = path.elements[i];

			TreeScope scope = (i == 0) ? TreeScope_Children : TreeScope_Descendants;
			bool found = FindFirstElement(uiElement, *pathElement, scope, &uiElement);

			//当前ProcessId下没找到
			if (!found) {
				char msg[64];
				sprintf_s(msg, 64, "进程[%d]下未找到子元素：索引[%d]", processId, i);
				UIALog(msg, LV_INFO);

				success = FALSE;
				break;
			}

			//为PathElement填充RuntimeId
			pathElement->runtimeId = uiaObj->GetRuntimeId(uiElement);
		}

		//找到返回，没找到则继续遍历
		if (success)
			return (LONG64)uiElement;
	}

	//没到到
	throw string("未找到指定路径的元素");
}

void UIAFindLibrary::FindAll(Path path, vector<LONG64> ids) {
	//TODO
}

LONG64 UIAFindLibrary::GetWindowByPoint(POINT pt) {
	IUIAutomationElement *uiElement = GetElementByPoint(pt);
	return GetWindowByElement(uiElement);
}

LONG64 UIAFindLibrary::GetWindowByPath(Path path) {
	path.elements.erase(begin(path.elements) + 1, end(path.elements));
	return FindFirst(path);
}

LONG64 UIAFindLibrary::GetWindowByElement(LONG64 id) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);
	return GetWindowByElement(uiElement);
}

LONG64 UIAFindLibrary::GetWindowByBrowserInfo(BrowserInfo info) {
	IUIAutomationElement *windowElement = NULL;
	HRESULT hresult = bcHelper->GetBrowserWindowElement(info, &windowElement);
	if (FAILED(hresult) || windowElement == NULL) {
		throw string("获取浏览器主窗体失败");
	}
	return (LONG64)windowElement;
}

LONG64 UIAFindLibrary::GetWindowByElement(IUIAutomationElement *uiElement) {
	IUIAutomationElement *root;
	HRESULT hResult = uiaObj->GetAutomation()->GetRootElement(&root);
	if (FAILED(hResult))
		throw string("获取根元素失败");

	IUIAutomationElement *pElement;
	while (uiElement) {
		HRESULT hResult = uiaObj->GetWalker()->GetParentElement(uiElement, &pElement);
		if (FAILED(hResult))
			throw string("获取父元素失败");

		BOOL areSame;
		hResult = uiaObj->GetAutomation()->CompareElements(pElement, root, &areSame);
		if (FAILED(hResult))
			throw string("比较父元素与根元素失败");

		//父元素为根元素，当前元素则为程序主窗体
		if (areSame)
			return (LONG64)uiElement;

		uiElement = pElement;
	}

	return NULL;
}

void UIAFindLibrary::GetWindowRect(POINT pt, UIARect *uiaRect) {
	LONG64 id = GetWindowByPoint(pt);
	IUIAutomationElement *window = uiaObj->Id2Element(id);

	RECT rect;
	HRESULT hResult = window->get_CurrentBoundingRectangle(&rect);
	if (FAILED(hResult))
		throw string("获取浏览器客户区元素边框失败");

	uiaRect->left = rect.left;
	uiaRect->top = rect.top;
	uiaRect->right = rect.right;
	uiaRect->bottom = rect.bottom;

	window->Release();
	window = NULL;
}