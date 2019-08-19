#include "stdafx.h"
#include <comdef.h>
#include "uiautomation.h"
#include "library\UIAControlLibrary.h"
#include "utils/Logger.h"
#include "utils/MouseUtils.h"
#include <iostream>

UIAControlLibrary::UIAControlLibrary(UIAObj *uiaObj) {
	this->uiaObj = uiaObj;
}

void UIAControlLibrary::GetRect(LONG64 id, RECT *rect) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);
	HRESULT hResult = uiElement->get_CurrentBoundingRectangle(rect);
	if (FAILED(hResult))
		throw string("��ȡԪ�ط���ʧ��");
}

string UIAControlLibrary::GetAttributeValue(LONG64 id, string attrName) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);
	PropInfo *propInfo = uiaObj->PropOf(attrName);
	if (!propInfo)
		throw string("δ֪������[" + attrName + "]");

	VARIANT retVal;
	HRESULT hResult = uiElement->GetCurrentPropertyValue(propInfo->id, &retVal);
	if (FAILED(hResult))
		throw string("��ȡԪ������ֵʧ�ܣ�" + propInfo->name);

	return uiaObj->Variant2string(retVal);
}

void UIAControlLibrary::ScrollIntoView(LONG64 id) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

	IUIAutomationScrollItemPattern *uiaScrollItemPattern = NULL;
	HRESULT hResult = uiElement->GetCurrentPatternAs(UIA_ScrollItemPatternId, IID_IUIAutomationScrollItemPattern, (void**)&uiaScrollItemPattern);
	if (FAILED(hResult) || !uiaScrollItemPattern)
		return;

	uiaScrollItemPattern->ScrollIntoView();
	uiaScrollItemPattern->Release();
}

bool getRootWindowByElement(UIAObj* uiaObj, IUIAutomationElement *uiElement, IUIAutomationElement **uiWindowElement);

void UIAControlLibrary::SetFocus(LONG64 id) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

	IUIAutomationElement  *rootWindowElement = NULL;
	if (getRootWindowByElement(uiaObj, uiElement, &rootWindowElement) && rootWindowElement != NULL) {
		UIA_HWND hwnd = NULL;

		HRESULT hResult = rootWindowElement->get_CurrentNativeWindowHandle(&hwnd);
		if (FAILED(hResult) || hwnd == NULL) {
			throw string("Ԫ�����ý���ǰ�����ö�ʧ��");
		}
		else {
			if (IsIconic((HWND)hwnd)) {
				SendMessage((HWND)hwnd, WM_SYSCOMMAND, SC_RESTORE, 0);
			}
			SetForegroundWindow((HWND)hwnd);
		}
	}

	HRESULT hResult = uiElement->SetFocus();
	if (FAILED(hResult))
		throw string("Ԫ�����ý���ʧ��");
}

bool getRootWindowByElement(UIAObj* uiaObj, IUIAutomationElement *uiElement, IUIAutomationElement **uiWindowElement) {
	//���ڵ�����ڵ���ͬ��ΪWindowElement
	IUIAutomationElement *rootElement;
	HRESULT hResult = uiaObj->GetAutomation()->GetRootElement(&rootElement);
	if (FAILED(hResult))
		throw string("��ȡ��Ԫ��ʧ��");

	IUIAutomationElement *pElement = uiElement;

	BOOL pAreSame = FALSE;
	while (!pAreSame) {
		uiElement = pElement;
		hResult = uiaObj->GetWalker()->GetParentElement(uiElement, &pElement);

		if (FAILED(hResult))
			throw string("��ȡ����Ԫ��ʧ��");
		if (!pElement)
			break;

		hResult = uiaObj->GetAutomation()->CompareElements(pElement, rootElement, &pAreSame);
		if (FAILED(hResult))
			throw string("�Ƚ�����Ԫ��ʧ��");
	}

	if (!pAreSame) {
		UIALog("δ�ҵ���Ӧ��WindowElement", LV_DEBUG);
		return FALSE;
	}

	*uiWindowElement = uiElement;
	return TRUE;
}

bool UIAControlLibrary::IsChecked(LONG64 id) {
	return GetCheckState(id) != ToggleState_Off;
}

void UIAControlLibrary::SetChecked(LONG64 id, bool checked) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

	CONTROLTYPEID ctrlType;
	HRESULT hResult = uiElement->get_CurrentControlType(&ctrlType);
	if (FAILED(hResult))
		throw string("��ȡԪ�ؿؼ�����ʧ��");

	if (ctrlType == UIA_CheckBoxControlTypeId) {
		IUIAutomationTogglePattern *uiaTogglePattern = NULL;
		hResult = uiElement->GetCurrentPatternAs(UIA_TogglePatternId, IID_IUIAutomationTogglePattern, (void**)&uiaTogglePattern);
		if (FAILED(hResult) || !uiaTogglePattern)
			throw string("��ȡԪ��Patternʧ�ܣ�UIA_TogglePatternId");

		ToggleState targetState, toggleState;
		hResult = uiaTogglePattern->get_CurrentToggleState(&toggleState);
		if (FAILED(hResult))
			throw string("��ȡԪ�ع�ѡ״̬ʧ��");

		targetState = checked ? ToggleState_On : ToggleState_Off;
		while (toggleState != targetState) {
			uiaTogglePattern->Toggle();
			hResult = uiaTogglePattern->get_CurrentToggleState(&toggleState);
			if (FAILED(hResult))
				throw string("��ȡԪ�ع�ѡ״̬ʧ��");
		}

		uiaTogglePattern->Release();
		uiaTogglePattern = NULL;
	}
	else if (ctrlType == UIA_RadioButtonControlTypeId) {
		if (!checked)
			throw string("Ԫ�ز�֧��ȡ��ѡ��");

		IUIAutomationSelectionItemPattern *uiaSelectionItemPattern = NULL;
		hResult = uiElement->GetCurrentPatternAs(UIA_SelectionItemPatternId, IID_IUIAutomationSelectionItemPattern, (void**)&uiaSelectionItemPattern);
		if (FAILED(hResult) || !uiaSelectionItemPattern)
			throw string("��ȡԪ��Patternʧ�ܣ�UIA_SelectionItemPatternId");

		hResult = uiaSelectionItemPattern->Select();
		if (FAILED(hResult))
			throw string("Ԫ��ѡ��ʧ��");

		uiaSelectionItemPattern->Release();
		uiaSelectionItemPattern = NULL;
	}
	else
		throw string("�ؼ����Ͳ�֧�ֹ�ѡ����");
}

int UIAControlLibrary::GetCheckState(LONG64 id) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

	CONTROLTYPEID ctrlType;
	HRESULT hResult = uiElement->get_CurrentControlType(&ctrlType);
	if (FAILED(hResult))
		throw string("��ȡԪ�ؿؼ�����ʧ��");

	if (ctrlType == UIA_CheckBoxControlTypeId) {
		IUIAutomationTogglePattern *uiaTogglePattern = NULL;
		hResult = uiElement->GetCurrentPatternAs(UIA_TogglePatternId, IID_IUIAutomationTogglePattern, (void**)&uiaTogglePattern);
		if (FAILED(hResult) || !uiaTogglePattern)
			throw string("��ȡԪ��Patternʧ�ܣ�UIA_TogglePatternId");

		ToggleState toggleState;
		hResult = uiaTogglePattern->get_CurrentToggleState(&toggleState);
		if (FAILED(hResult))
			throw string("��ȡԪ�ع�ѡ״̬ʧ��");

		return toggleState;
	}
	else if (ctrlType == UIA_RadioButtonControlTypeId) {
		IUIAutomationSelectionItemPattern *uiaSelectionItemPattern = NULL;
		hResult = uiElement->GetCurrentPatternAs(UIA_SelectionItemPatternId, IID_IUIAutomationSelectionItemPattern, (void**)&uiaSelectionItemPattern);
		if (FAILED(hResult) || !uiaSelectionItemPattern)
			throw string("��ȡԪ��Patternʧ�ܣ�UIA_SelectionItemPatternId");

		BOOL isSelected = FALSE;
		hResult = uiaSelectionItemPattern->get_CurrentIsSelected(&isSelected);
		if (FAILED(hResult))
			throw string("Ԫ��ѡ��״̬ʧ��");

		return isSelected ? ToggleState_On : ToggleState_Off;
	}
	else
		throw string("�ؼ����Ͳ�֧�ֻ�ȡ��ѡ״̬checkState");
}

string UIAControlLibrary::GetText(LONG64 id) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

	IUIAutomationValuePattern  *uiaValuePattern = NULL;
	HRESULT hResult = uiElement->GetCurrentPatternAs(UIA_ValuePatternId, IID_IUIAutomationValuePattern, (void**)&uiaValuePattern);
	if (FAILED(hResult))
		throw string("��ȡԪ��Patternʧ�ܣ�UIA_ValuePatternId");

	BSTR text;
	if (uiaValuePattern) {
		hResult = uiaValuePattern->get_CurrentValue(&text);
		if (FAILED(hResult))
			throw string("��ȡԪ��Valueʧ��");

		uiaValuePattern->Release();
		uiaValuePattern = NULL;
	}
	else {
		hResult = uiElement->get_CurrentName(&text);
		if (FAILED(hResult))
			throw string("��ȡԪ��Nameʧ��");
	}

	return _com_util::ConvertBSTRToString(text);
}

void UIAControlLibrary::SetText(LONG64 id, string text) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

	IUIAutomationValuePattern *uiaValuePattern;
	HRESULT hResult = uiElement->GetCurrentPatternAs(UIA_ValuePatternId, IID_PPV_ARGS(&uiaValuePattern));
	if (FAILED(hResult) || !uiaValuePattern)
		throw string("Ԫ�ز�֧��ValuePattern");

	BSTR bstr = _com_util::ConvertStringToBSTR(text.data());
	hResult = uiaValuePattern->SetValue(bstr);
	if (FAILED(hResult))
		throw string("����Ԫ��Valueʧ��");

	uiaValuePattern->Release();
	uiaValuePattern = NULL;
}

bool UIAControlLibrary::IsPassword(LONG64 id) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

	BOOL isPassword = FALSE;
	HRESULT hResult = uiElement->get_CurrentIsPassword(&isPassword);
	if (FAILED(hResult))
		throw string("����Ԫ��isPasswordʧ��");

	return isPassword;
}

bool UIAControlLibrary::IsTable(LONG64 id) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

	IUnknown *uiaPattern = NULL;
	HRESULT hResult = uiElement->GetCurrentPattern(UIA_GridPatternId, &uiaPattern);
	if (FAILED(hResult) || !uiaPattern)
		return FALSE;

	uiaPattern->Release();
	uiaPattern = NULL;

	return TRUE;
}

bool UIAControlLibrary::Enable(LONG64 id) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

	BOOL retVal;
	HRESULT hResult = uiElement->get_CurrentIsEnabled(&retVal);
	if (FAILED(hResult) || !retVal)
		return FALSE;

	return TRUE;
}

bool UIAControlLibrary::Visible(LONG64 id) {
	IUIAutomationElement *uiElement = uiaObj->Id2Element(id);
	return Enable(id);
}

void UIAControlLibrary::ClearSelection(LONG64 id) {
	IUIAutomationElement *comboBox = uiaObj->Id2Element(id);
	if (comboBox == NULL) return;
	HWND  hwnd = NULL;
	comboBox->get_CurrentNativeWindowHandle((UIA_HWND*)&hwnd);
	IUIAutomationSelectionPattern *selectPattern = NULL;
	comboBox->GetCurrentPattern(UIA_SelectionPatternId, (IUnknown**)&selectPattern);
	if (selectPattern == NULL) {
		//ListBox�����ѡ
		SendMessage(hwnd, LB_SETSEL, 0, -1);
	}
	else {
		//֧��selectionPattern�Ŀؼ������ѡ
		IUIAutomationElementArray *selectedArr = NULL;
		selectPattern->GetCurrentSelection(&selectedArr);
		int len = -1;
		selectedArr->get_Length(&len);
		for (int i = 0; i < len; i++) {
			IUIAutomationElement *crtEle = NULL;
			selectedArr->GetElement(i, &crtEle);
			if (crtEle == NULL) continue;
			IUIAutomationSelectionItemPattern *crtselectPattern = NULL;
			crtEle->GetCurrentPattern(UIA_SelectionItemPatternId, (IUnknown**)&crtselectPattern);
			if (crtselectPattern == NULL) continue;
			crtselectPattern->RemoveFromSelection();

			crtselectPattern->Release();
			crtEle->Release();
		}
		selectedArr->Release();
		selectPattern->Release();
	}
}

void selectItem(UIAObj *uiaObj, IUIAutomationElement* comboBox, string item);
void selectListBoxItems(HWND hwnd, vector<string> items, bool isAddtoSelection);
void selectionPatternSelect(UIAObj *uiaObj, IUIAutomationElement* comboBox, string item, BOOL isMultipleSelect);

void UIAControlLibrary::SelectItems(LONG64 id, vector<string> items) {
	IUIAutomationElement *comboBox = uiaObj->Id2Element(id);

	if (comboBox == NULL) return;
	HWND  hwnd = NULL;
	comboBox->get_CurrentNativeWindowHandle((UIA_HWND*)&hwnd);
	size_t length = items.size();
	if (length == 1)
	{
		//��ѡ
		string crtItem = items[0];
		selectItem(uiaObj, comboBox, crtItem);
	}
	else
	{
		IUIAutomationSelectionPattern *selectPattern = NULL;
		comboBox->GetCurrentPattern(UIA_SelectionPatternId, (IUnknown**)&selectPattern);
		if (selectPattern == NULL) {
			//TODO:UNKnow multiple select control type
			selectListBoxItems(hwnd, items, true);
			return;
		};
		for (int i = 0; i < length; i++) {
			selectionPatternSelect(uiaObj, comboBox, items[i], true);
		}
	}
}

string UIAControlLibrary::GetPageData(vector<Path *> columns) {
	return "";
}

//��ѡ ���ж����ֳ�����comboBox��listBox���Լ�֧��selectionPattern�Ŀؼ�
boolean isSupportedSelectionPattern(IUIAutomationElement* element);
void selectItem(UIAObj *uiaObj, IUIAutomationElement* comboBox, string item) {
	HWND  hwnd = NULL;
	comboBox->get_CurrentNativeWindowHandle((UIA_HWND*)&hwnd);
	if (hwnd == NULL)return;
	if (isSupportedSelectionPattern(comboBox)) {
		selectionPatternSelect(uiaObj, comboBox, item, false);
	}
	else {
		SendMessage(hwnd, CB_SELECTSTRING, -1, (LPARAM)::_com_util::ConvertStringToBSTR(item.data())); //�ı�ComboBox��ֵ
		SendMessage(hwnd, LB_SELECTSTRING, -1, (LPARAM)::_com_util::ConvertStringToBSTR(item.data())); //�ı�ComboBox��ֵ
	}
}

boolean isSupportedSelectionPattern(IUIAutomationElement* element) {
	IUIAutomationSelectionPattern *selectPattern = NULL;
	element->GetCurrentPattern(UIA_SelectionPatternId, (IUnknown**)&selectPattern);
	if (selectPattern == NULL)
		return false;
	else {
		selectPattern->Release();
		return true;
	}
}
//ListBox�Ķ�ѡ
void selectListBoxItems(HWND hwnd, vector<string> items, bool isAddtoSelection) {
	//ListBox multiple select
	if (isAddtoSelection == false || isAddtoSelection == FALSE) {
		SendMessage(hwnd, LB_SETSEL, 0, -1);
	}
	for (int i = 0; i < items.size(); i++) {
		string crtStr = items[i];
		LRESULT crtIndex = SendMessage(hwnd, LB_FINDSTRING, -1, (LPARAM)::_com_util::ConvertStringToBSTR(crtStr.data()));
		if (crtIndex >= 0) {
			SendMessage(hwnd, LB_SETSEL, 1, crtIndex);
		}
	}
}
//֧��selectionPattern��ѡ��isMultipleSelect�������ƶ�ѡ���ǵ�ѡ
void selectionPatternSelect(UIAObj *uiaObj, IUIAutomationElement* comboBox, string item, BOOL isMultipleSelect) {
	string crtItem = item;
	VARIANT value;
	value.vt = VT_BSTR;
	value.bstrVal = ::_com_util::ConvertStringToBSTR(crtItem.data());
	IUIAutomationCondition *condition = NULL;
	uiaObj->GetAutomation()->CreatePropertyCondition(UIA_NamePropertyId, value, &condition);

	IUIAutomationElement *crtSelect = NULL;
	comboBox->FindFirst(TreeScope_Children, condition, &crtSelect);

	if (crtSelect == NULL) return;
	IUIAutomationSelectionItemPattern *selectPattern = NULL;
	crtSelect->GetCurrentPattern(UIA_SelectionItemPatternId, (IUnknown**)&selectPattern);
	if (selectPattern == NULL) return;
	if (isMultipleSelect || isMultipleSelect)
		selectPattern->AddToSelection();
	else
		selectPattern->Select();
	condition->Release();
	selectPattern->Release();
	crtSelect->Release();
}