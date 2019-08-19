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
			throw string("��ȡ��Ԫ��ʧ��");

		uiElement->Release();
		uiElement = pElement;
	}

	return NULL;
}

IUIAutomationElement *UIAFindLibrary::GetElementByPoint(POINT pt) {
	IUIAutomationElement *uiElement;
	HRESULT hResult = uiaObj->GetAutomation()->ElementFromPoint(pt, &uiElement);
	if (FAILED(hResult) || !uiElement)
		throw string("���ݵ��ȡԪ��ʧ�ܣ�%s" + POINT2string(pt));

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
			throw string("������������ʧ�ܣ�" + prop->name);

		if (tempCondition) {
			hResult = uiaObj->GetAutomation()->CreateAndCondition(tempCondition, newCondition, &tempCondition);
			if (FAILED(hResult))
				throw string("�������Բ�������ʧ�ܣ�" + prop->name);
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
		throw string("��ȡԪ������ֵʧ�ܣ�" + propertyName);

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

	//�ؼ�����
	VARIANT retVal;
	PathAttribute *controlTypeAttribute = TryCreatePropertyAttribute(uiElement, UIA_ControlTypePropertyId, &retVal);
	if (controlTypeAttribute)
		pathElement->attributes.insert(begin(pathElement->attributes), controlTypeAttribute);

	//����Ӧ��
	PathAttribute *appNameAttribute = TryCreateWindowApplicationAttribute(uiElement);
	if (appNameAttribute)
		pathElement->attributes.insert(begin(pathElement->attributes), appNameAttribute);

	//Ԫ������
	PathAttribute *classNameAttribute = TryCreatePropertyAttribute(uiElement, UIA_ClassNamePropertyId, &retVal);
	if (classNameAttribute)
		pathElement->attributes.insert(begin(pathElement->attributes), classNameAttribute);

	//��������
	PathAttribute *nameAttribute = TryCreatePropertyAttribute(uiElement, UIA_NamePropertyId, &retVal);
	if (nameAttribute)
		pathElement->attributes.insert(end(pathElement->attributes), nameAttribute);

	return pathElement;
}

PathElement *UIAFindLibrary::TryCreateControlElement(IUIAutomationElement *pElement, IUIAutomationElement *uiElement, TreeScope scope) {
	PathElement *pathElement = new PathElement();

	/*��������*/
	IUIAutomationCondition *condition = NULL;
	int len = sizeof(PROP_IDS_REQUIRED) / sizeof(PROP_IDS_REQUIRED[0]);
	for (int i = 0; i < len; i++) {
		PROPERTYID propertyId = PROP_IDS_REQUIRED[i];
		string propertyName = uiaObj->PropOf(propertyId)->name;

		//����·������
		VARIANT retVal;
		PathAttribute *pathAttribute = TryCreatePropertyAttribute(uiElement, propertyId, &retVal);
		if (!pathAttribute)
			continue;

		pathElement->attributes.insert(end(pathElement->attributes), pathAttribute);

		//��ǰ���Թ�������
		IUIAutomationCondition *newCondition;
		HRESULT hResult = uiaObj->GetAutomation()->CreatePropertyCondition(propertyId, retVal, &newCondition);
		if (FAILED(hResult))
			throw string("�������Թ�������ʧ�ܣ�" + propertyName);

		//���뵱ǰԪ�ص��������
		if (condition) {
			hResult = uiaObj->GetAutomation()->CreateAndCondition(condition, newCondition, &condition);
			if (FAILED(hResult))
				throw string("�����������ʧ�ܣ�" + propertyName);
		}
		else
			condition = newCondition;
	}

	//��������ƴ�ӵ���������
	IUIAutomationElement *tmpElement;
	HRESULT hResult = pElement->FindFirst(scope, condition, &tmpElement);
	if (FAILED(hResult))
		throw string("����������Ԫ��ʧ��");

	IUIAutomationElementArray *tmpElementArray;
	hResult = pElement->FindAll(scope, condition, &tmpElementArray);
	if (FAILED(hResult))
		throw string("��������������Ԫ��ʧ��");

	int length;
	hResult = tmpElementArray->get_Length(&length);
	if (FAILED(hResult))
		throw string("��������������Ԫ��ʧ��");

	//Ψһȷ����
	if (length == 1) {
		BOOL areSame = FALSE;
		if (tmpElement) {
			hResult = uiaObj->GetAutomation()->CompareElements(tmpElement, uiElement, &areSame);
			if (FAILED(hResult))
				throw string("�Ƚ�Ԫ��ʧ��");

			tmpElement->Release();
			if (areSame)
				return pathElement;
		}
	}

	/*�Ƽ�����*/
	len = sizeof(PROP_IDS_SUGGESTED) / sizeof(PROP_IDS_SUGGESTED[0]);
	for (int i = 0; i < len; i++) {
		PROPERTYID propertyId = PROP_IDS_SUGGESTED[i];
		string propertyName = uiaObj->PropOf(propertyId)->name;

		//����·������
		VARIANT retVal;
		PathAttribute *pathAttribute = TryCreatePropertyAttribute(uiElement, propertyId, &retVal);
		if (!pathAttribute)
			continue;

		pathElement->attributes.insert(end(pathElement->attributes), pathAttribute);

		//��ǰ���Թ�������
		IUIAutomationCondition *newCondition;
		HRESULT hResult = uiaObj->GetAutomation()->CreatePropertyCondition(propertyId, retVal, &newCondition);
		if (FAILED(hResult))
			throw string("�������Թ�������ʧ�ܣ�" + propertyName);

		//���뵱ǰԪ�ص��������
		if (condition) {
			hResult = uiaObj->GetAutomation()->CreateAndCondition(condition, newCondition, &condition);
			if (FAILED(hResult))
				throw string("�����������ʧ�ܣ�" + propertyName);
		}
		else
			condition = newCondition;

		//��������ƴ�ӵ���������
		IUIAutomationElement *tmpElement;
		hResult = pElement->FindFirst(scope, condition, &tmpElement);
		if (FAILED(hResult))
			throw string("����������Ԫ��ʧ��");

		IUIAutomationElementArray *tmpElementArray;
		hResult = pElement->FindAll(scope, condition, &tmpElementArray);
		if (FAILED(hResult))
			throw string("��������������Ԫ��ʧ��");

		int length;
		hResult = tmpElementArray->get_Length(&length);
		if (FAILED(hResult))
			throw string("��������������Ԫ��ʧ��");

		//Ψһȷ����
		if (length == 1) {
			BOOL areSame = FALSE;
			if (tmpElement) {
				hResult = uiaObj->GetAutomation()->CompareElements(tmpElement, uiElement, &areSame);
				if (FAILED(hResult))
					throw string("�Ƚ�Ԫ��ʧ��");

				tmpElement->Release();
				if (areSame)
					return pathElement;
			}
		}
	}

	hResult = pElement->FindAll(scope, condition, &tmpElementArray);
	if (FAILED(hResult))
		throw string("��������������Ԫ��ʧ��");

	hResult = tmpElementArray->get_Length(&length);
	if (FAILED(hResult))
		throw string("��������������Ԫ��ʧ��");

	for (int i = 0; i < length; i++) {
		IUIAutomationElement *tmpElement;
		tmpElementArray->GetElement(i, &tmpElement);

		BOOL areSame = FALSE;
		hResult = uiaObj->GetAutomation()->CompareElements(tmpElement, uiElement, &areSame);
		if (FAILED(hResult))
			throw string("�Ƚ�Ԫ��ʧ��");

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
	//�����������ԣ�����Ƿ���ͨ���
	for (auto iter = pathElement->attributes.begin(); iter < pathElement->attributes.end();) {
		PathAttribute *pathAttribute = *iter;
		if (pathAttribute->enable) {
			string value = pathAttribute->value;

			//����ͨ��������ԣ���ȡ����
			if (value.find("*") != string::npos) {
				vector->insert(vector->end(), pathAttribute);

				//ɾ����iter�Ѿ�ָ����һ��Ԫ���ˣ����Բ���++�ˣ�ֱ��continue
				iter = pathElement->attributes.erase(iter);
				continue;
			}
		}

		++iter;
	}

	//����ͨ�����ԣ�����TRUE
	return vector->size() > 0;
}

bool UIAFindLibrary::FindFirstElement(IUIAutomationElement *pElement, PathElement pathElement, TreeScope scope, IUIAutomationElement **uiElement) {
	//�Ƿ���ͨ���
	vector<PathAttribute *> vector;
	BOOL hasWildcard = GetWildcardAttributes(&pathElement, &vector);

	//��ͨ����Ĵ���
	if (hasWildcard)
		return FindFirstElementWithWildcard(pElement, pathElement, scope, vector, uiElement);

	//��ͨ����Ĵ���
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
				throw string("��ȡ����������Ԫ������ʧ��");

			int length;
			hResult = elementArray->get_Length(&length);
			if (FAILED(hResult) || idx >= length)
				throw string("��ȡԪ�س���ʧ��");

			hResult = elementArray->GetElement(idx, uiElement);
			if (FAILED(hResult))
				throw string("������ָ������[" + to_string(idx) + "]��ȡԪ��ʧ��");
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
	//������������������ȥ��ͨ������Ե�PathElement
	IUIAutomationCondition *elementCondition;
	CreateElementCondition(pathElement, &elementCondition);

	//���ҷ�������������Ԫ��
	IUIAutomationElementArray *uiElementArray;
	HRESULT hResult = pElement->FindAll(scope, elementCondition, &uiElementArray);
	elementCondition->Release();

	if (FAILED(hResult) || !uiElementArray)
		throw string("�ų���ͨ����󣬲���Ԫ��ʧ��");

	int len;
	hResult = uiElementArray->get_Length(&len);
	if (FAILED(hResult))
		throw string("��ȡԪ�����鳤��ʧ��");

	//��������Ԫ��
	for (int i = 0; i < len; i++) {
		IUIAutomationElement *tmpElement;
		hResult = uiElementArray->GetElement(i, &tmpElement);
		if (FAILED(hResult))
			throw string("�������ȡԪ��ʧ��");

		//��ȡԪ������ֵ�����Ӧͨ���ַ�������ƥ��
		BOOL matched = TRUE;
		for (int j = 0; j < vector.size(); j++) {
			PathAttribute *pathAttribute = vector[j];
			PropInfo *prop = uiaObj->PropOf(pathAttribute->name);

			VARIANT retVal;
			hResult = tmpElement->GetCurrentPropertyValue(prop->id, &retVal);
			if (FAILED(hResult))
				throw string("��ȡԪ������ֵʧ��[" + prop->name + "]");

			string ptn = pathAttribute->value;
			string str = uiaObj->Variant2string(retVal);

			//��ƥ�䣬�������һ��Ԫ�ص�ƥ��
			if (!StringMatch(ptn, str)) {
				matched = FALSE;
				break;
			}
		}

		//ƥ����
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
			throw string("��ȡ������ͻ���Ԫ��ʧ��");

		HRESULT hResult = uiClientElement->get_CurrentBoundingRectangle(&rect);
		if (FAILED(hResult))
			throw string("��ȡ������ͻ���Ԫ�ر߿�ʧ��");
	}
	else {
		UIALog("isBrowser[FALSE]", LV_DEBUG);

		HRESULT hResult = uiElement->get_CurrentBoundingRectangle(&rect);
		if (FAILED(hResult))
			throw string("��ȡ������ͻ���Ԫ�ر߿�ʧ��");
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
		throw string("��ȡԪ�ر߿�ʧ��");

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
			throw string("��ȡ��Ԫ��ʧ��");

		uiElement = pElement;
	}

	pElement = vector[0];
	path->elements.clear();
	for (int i = 1; i < vector.size(); i++) {
		uiElement = vector[i];
		CONTROLTYPEID controlType = uiaObj->GetControlType(uiElement);

		PathElement *pathElement;
		//����Ԫ��
		if (i == 1)
			pathElement = TryCreateWindowElement(pElement, uiElement);
		//����Ԫ��
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
	//��ȡ·���е�Application����
	PathAttribute *pathAttribute = GetApplicationAttribute(path);
	if (!pathAttribute || pathAttribute->value == "")
		throw string("δָ��Ӧ�ó�������");

	//����Ӧ�����ƻ�ȡ�����б�
	vector<DWORD> vector;
	application2processid(pathAttribute->value, &vector);
	if (vector.size() == 0)
		throw string("δ�ҵ�ָ����Ӧ��[" + pathAttribute->value + "]");

	//��ȡ��Ԫ�أ��Ӹ�Ԫ�ؿ�ʼ����
	IUIAutomationElement *rootElement;
	HRESULT hResult = uiaObj->GetAutomation()->GetRootElement(&rootElement);
	if (FAILED(hResult) || !rootElement)
		throw string("��ȡ��Ԫ��ʧ��");

	//��Application�����޸�ΪProcessId����
	PropInfo *prop = uiaObj->PropOf(UIA_ProcessIdPropertyId);
	pathAttribute->name = prop->name;

	//�������ƥ�䵽������ProcessId
	for (int i = (int)vector.size() - 1; i >= 0; i--) {
		DWORD processId = vector[i];

		//�滻������
		pathAttribute->value = to_string(processId);

		//�����ڵ�ǰProcessId�²���
		BOOL success = TRUE;
		IUIAutomationElement *uiElement = rootElement;
		for (int i = 0; i < path.elements.size(); i++) {
			PathElement *pathElement = path.elements[i];

			TreeScope scope = (i == 0) ? TreeScope_Children : TreeScope_Descendants;
			bool found = FindFirstElement(uiElement, *pathElement, scope, &uiElement);

			//��ǰProcessId��û�ҵ�
			if (!found) {
				char msg[64];
				sprintf_s(msg, 64, "����[%d]��δ�ҵ���Ԫ�أ�����[%d]", processId, i);
				UIALog(msg, LV_INFO);

				success = FALSE;
				break;
			}
		}

		//�ҵ����أ�û�ҵ����������
		if (success)
			return TRUE;
	}

	//û����
	return FALSE;
}

LONG64 UIAFindLibrary::FindFirst(Path path) {
	//��ȡ·���е�Application����
	PathAttribute *pathAttribute = GetApplicationAttribute(path);
	if (!pathAttribute || pathAttribute->value == "")
		throw string("δָ��Ӧ�ó�������");

	//����Ӧ�����ƻ�ȡ�����б�
	vector<DWORD> vector;
	application2processid(pathAttribute->value, &vector);
	if (vector.size() == 0)
		throw string("δ�ҵ�ָ����Ӧ��[" + pathAttribute->value + "]");

	//��ȡ��Ԫ�أ��Ӹ�Ԫ�ؿ�ʼ����
	IUIAutomationElement *rootElement;
	HRESULT hResult = uiaObj->GetAutomation()->GetRootElement(&rootElement);
	if (FAILED(hResult) || !rootElement)
		throw string("��ȡ��Ԫ��ʧ��");

	//��Application�����޸�ΪProcessId����
	PropInfo *prop = uiaObj->PropOf(UIA_ProcessIdPropertyId);
	pathAttribute->name = prop->name;

	//�������ƥ�䵽������ProcessId
	for (int i = (int)vector.size() - 1; i >= 0; i--) {
		DWORD processId = vector[i];

		//�滻������
		pathAttribute->value = to_string(processId);

		//�����ڵ�ǰProcessId�²���
		BOOL success = TRUE;
		IUIAutomationElement *uiElement = rootElement;
		for (int i = 0; i < path.elements.size(); i++) {
			PathElement *pathElement = path.elements[i];

			TreeScope scope = (i == 0) ? TreeScope_Children : TreeScope_Descendants;
			bool found = FindFirstElement(uiElement, *pathElement, scope, &uiElement);

			//��ǰProcessId��û�ҵ�
			if (!found) {
				char msg[64];
				sprintf_s(msg, 64, "����[%d]��δ�ҵ���Ԫ�أ�����[%d]", processId, i);
				UIALog(msg, LV_INFO);

				success = FALSE;
				break;
			}

			//ΪPathElement���RuntimeId
			pathElement->runtimeId = uiaObj->GetRuntimeId(uiElement);
		}

		//�ҵ����أ�û�ҵ����������
		if (success)
			return (LONG64)uiElement;
	}

	//û����
	throw string("δ�ҵ�ָ��·����Ԫ��");
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
		throw string("��ȡ�����������ʧ��");
	}
	return (LONG64)windowElement;
}

LONG64 UIAFindLibrary::GetWindowByElement(IUIAutomationElement *uiElement) {
	IUIAutomationElement *root;
	HRESULT hResult = uiaObj->GetAutomation()->GetRootElement(&root);
	if (FAILED(hResult))
		throw string("��ȡ��Ԫ��ʧ��");

	IUIAutomationElement *pElement;
	while (uiElement) {
		HRESULT hResult = uiaObj->GetWalker()->GetParentElement(uiElement, &pElement);
		if (FAILED(hResult))
			throw string("��ȡ��Ԫ��ʧ��");

		BOOL areSame;
		hResult = uiaObj->GetAutomation()->CompareElements(pElement, root, &areSame);
		if (FAILED(hResult))
			throw string("�Ƚϸ�Ԫ�����Ԫ��ʧ��");

		//��Ԫ��Ϊ��Ԫ�أ���ǰԪ����Ϊ����������
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
		throw string("��ȡ������ͻ���Ԫ�ر߿�ʧ��");

	uiaRect->left = rect.left;
	uiaRect->top = rect.top;
	uiaRect->right = rect.right;
	uiaRect->bottom = rect.bottom;

	window->Release();
	window = NULL;
}