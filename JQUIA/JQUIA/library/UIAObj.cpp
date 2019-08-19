#include "stdafx.h"
#include <map>
#include <string>
#include <algorithm>
#include <comdef.h>
#include "UIAObj.h"
#include "uiautomation.h"

map<PROPERTYID, PropInfo*> ID_PROPERTY_MAP;
map<string, PropInfo*> NAME_PROPERTY_MAP;

string UIAObj::Variant2string(VARIANT retVal) {
	if ((retVal.vt & VT_ARRAY) != 0) {
		SAFEARRAY *parray = retVal.parray;

		switch (retVal.vt & VT_TYPEMASK) {
		case VT_I4:
			return IntegerArray2string(retVal.parray);
		case VT_R8:
			return DoubleArray2string(retVal.parray);
		case VT_BSTR:
			return StringArray2string(retVal.parray);
		default:
			BSTR bstr = retVal.bstrVal;
			return _com_util::ConvertBSTRToString(bstr);
		}
	}
	else if (retVal.vt == VT_UNKNOWN) {
		//未知类型
		//TODO
		return "UNKNOWN";
	}
	else if (retVal.vt == VT_I4) {
		int intVal = retVal.intVal;
		return to_string(intVal);
	}
	else if (retVal.vt == VT_R8) {
		double doubleVal = retVal.dblVal;
		return to_string(doubleVal);
	}
	else if (retVal.vt == VT_BSTR)
		return retVal.bstrVal ? _com_util::ConvertBSTRToString(retVal.bstrVal) : "";
	else if (retVal.vt == VT_BOOL)
		return retVal.boolVal ? "TRUE" : "FALSE";
	else {
		char msg[64];
		sprintf_s(msg, 64, "未支持的VARIANT类型：%d", retVal.vt);
		return string(msg);
	}
}

string UIAObj::IntegerArray2string(SAFEARRAY *pArray) {
	int pv = 0;

	long low = pArray->rgsabound->lLbound;
	long up = low + pArray->rgsabound->cElements - 1;

	string str = "[";
	for (LONG i = low; i <= up; i++) {
		HRESULT hResult = SafeArrayGetElement(pArray, &i, &pv);
		if (FAILED(hResult))
			throw string("获取整型数组型属性值失败");

		if (i > 0)
			str += ",";
		str += to_string(pv);
	}
	str += "]";

	return str;
}

string UIAObj::DoubleArray2string(SAFEARRAY *pArray) {
	double pv = 0;

	long low = pArray->rgsabound->lLbound;
	long up = low + pArray->rgsabound->cElements - 1;

	string str = "[";
	for (LONG i = low; i <= up; i++) {
		HRESULT hResult = SafeArrayGetElement(pArray, &i, &pv);
		if (FAILED(hResult))
			throw string("获取整型数组型属性值失败");

		if (i > 0)
			str += ",";
		str += to_string(pv);
	}
	str += "]";

	return str;
}

string UIAObj::StringArray2string(SAFEARRAY *pArray) {
	string pv;

	long low = pArray->rgsabound->lLbound;
	long up = low + pArray->rgsabound->cElements - 1;

	string str = "[";
	for (LONG i = low; i <= up; i++) {
		HRESULT hResult = SafeArrayGetElement(pArray, &i, &pv);
		if (FAILED(hResult))
			throw string("获取整型数组型属性值失败");

		if (i > 0)
			str += ",";
		str += pv;
	}
	str += "]";

	return str;
}

void UIAObj::ReleaseById(LONG64 id) {
	IUIAutomationElement *uiElement = Id2Element(id);
	if (uiElement) {
		uiElement->Release();
		uiElement = NULL;
	}
}

void UIAObj::ReleaseByIds(vector<LONG64> ids) {
	for (int i = 0; i < ids.size(); i++)
		ReleaseById(ids[i]);
}

string UIAObj::NameOf(PROPERTYID propertyId, string defaultName) {
	BSTR propertyName;
	HRESULT hResult = _automation->GetPropertyProgrammaticName(propertyId, &propertyName);
	if (FAILED(hResult))
		return defaultName;

	return _com_util::ConvertBSTRToString(propertyName);
}

PropInfo *UIAObj::PropOf(string propName) {
	string propName_upper(propName);
	transform(propName_upper.begin(), propName_upper.end(), propName_upper.begin(), toupper);

	return NAME_PROPERTY_MAP[propName_upper];
}

PropInfo *UIAObj::PropOf(PROPERTYID propID) {
	return ID_PROPERTY_MAP[propID];
}

IUIAutomationElement *UIAObj::Id2Element(LONG64 id) {
	IUIAutomationElement *uiElement = (IUIAutomationElement *)id;
	if (!uiElement) {
		char msg[64];
		sprintf_s(msg, 64, "无效的元素ID[%I64d]", id);
		throw string(msg);
	}

	return uiElement;
}

void UIAObj::GetProperties(LONG64 id, map<string, string> *properties) {
	IUIAutomationElement *uiElement = Id2Element(id);
	GetProperties(uiElement, properties);
}

void UIAObj::GetProperties(IUIAutomationElement *uiElement, map<string, string> *properties) {
	SAFEARRAY *ids, *names;
	HRESULT hResult = GetAutomation()->PollForPotentialSupportedProperties(uiElement, &ids, &names);
	if (FAILED(hResult))
		throw string("拉取元素支持的属性列表失败");

	BSTR propName;
	PROPERTYID propId;

	long low = ids->rgsabound->lLbound;
	long up = low + ids->rgsabound->cElements - 1;
	for (LONG i = low; i <= up; i++) {
		hResult = SafeArrayGetElement(ids, &i, &propId);
		if (FAILED(hResult)) {
			char msg[64];
			sprintf_s(msg, 64, "遍历元素属性ID列表失败：%d", i);
			throw string(msg);
		}

		if (!PropOf(propId))
			continue;

		hResult = SafeArrayGetElement(names, &i, &propName);
		if (FAILED(hResult)) {
			char msg[64];
			sprintf_s(msg, 64, "遍历元素属性NAME列表失败：%d", i);
			throw string(msg);
		}

		VARIANT retVal;
		hResult = uiElement->GetCurrentPropertyValue(propId, &retVal);
		if (FAILED(hResult)) {
			char msg[64];
			sprintf_s(msg, 64, "获取元素属性值失败：%s", _com_util::ConvertBSTRToString(propName));
			throw string(msg);
		}

		string value = Variant2string(retVal);
		if (value.empty())
			continue;

		string name = _com_util::ConvertBSTRToString(propName);
		(*properties)[name] = value;
	}
}

void UIAObj::CacheProp(PROPERTYID propertyId, string defaultName, VARTYPE type) {
	string propName = NameOf(propertyId, defaultName);

	PropInfo *prop = new PropInfo();
	prop->id = propertyId;
	prop->name = propName;
	prop->type = type;

	string propName_upper(propName);
	transform(propName_upper.begin(), propName_upper.end(), propName_upper.begin(), toupper);

	ID_PROPERTY_MAP[propertyId] = prop;
	NAME_PROPERTY_MAP[propName_upper] = prop;
}

void UIAObj::CacheProp(PROPERTYID propertyId, string defaultName) {
	CacheProp(propertyId, defaultName, VT_BSTR);
}

void UIAObj::InitPropertyMap() {
	CacheProp(UIA_RuntimeIdPropertyId, NameOf(UIA_RuntimeIdPropertyId, "RuntimeId"));
	CacheProp(UIA_BoundingRectanglePropertyId, NameOf(UIA_BoundingRectanglePropertyId, "BoundingRectangle"));
	CacheProp(UIA_ProcessIdPropertyId, NameOf(UIA_ProcessIdPropertyId, "ProcessId"), VT_I4);
	CacheProp(UIA_ControlTypePropertyId, NameOf(UIA_ControlTypePropertyId, "ControlType"), VT_I4);
	CacheProp(UIA_LocalizedControlTypePropertyId, NameOf(UIA_LocalizedControlTypePropertyId, "LocalizedControlType"));
	CacheProp(UIA_NamePropertyId, NameOf(UIA_NamePropertyId, "Name"));
	CacheProp(UIA_AcceleratorKeyPropertyId, NameOf(UIA_AcceleratorKeyPropertyId, "AcceleratorKey"));
	CacheProp(UIA_AccessKeyPropertyId, NameOf(UIA_AccessKeyPropertyId, "AccessKey"));
	CacheProp(UIA_HasKeyboardFocusPropertyId, NameOf(UIA_HasKeyboardFocusPropertyId, "HasKeyboardFocus"));
	CacheProp(UIA_IsKeyboardFocusablePropertyId, NameOf(UIA_IsKeyboardFocusablePropertyId, "IsKeyboardFocusable"));
	CacheProp(UIA_IsEnabledPropertyId, NameOf(UIA_IsEnabledPropertyId, "IsEnabled"));
	CacheProp(UIA_AutomationIdPropertyId, NameOf(UIA_AutomationIdPropertyId, "AutomationId"));
	CacheProp(UIA_ClassNamePropertyId, NameOf(UIA_ClassNamePropertyId, "ClassName"));
	CacheProp(UIA_HelpTextPropertyId, NameOf(UIA_HelpTextPropertyId, "HelpText"));
	CacheProp(UIA_ClickablePointPropertyId, NameOf(UIA_ClickablePointPropertyId, "ClickablePoint"));
	CacheProp(UIA_CulturePropertyId, NameOf(UIA_CulturePropertyId, "Culture"));
	CacheProp(UIA_IsControlElementPropertyId, NameOf(UIA_IsControlElementPropertyId, "IsControlElement"));
	CacheProp(UIA_IsContentElementPropertyId, NameOf(UIA_IsContentElementPropertyId, "IsContentElement"));
	CacheProp(UIA_LabeledByPropertyId, NameOf(UIA_LabeledByPropertyId, "LabeledBy"));
	CacheProp(UIA_IsPasswordPropertyId, NameOf(UIA_IsPasswordPropertyId, "IsPassword"));
	CacheProp(UIA_NativeWindowHandlePropertyId, NameOf(UIA_NativeWindowHandlePropertyId, "NativeWindowHandle"));
	CacheProp(UIA_ItemTypePropertyId, NameOf(UIA_ItemTypePropertyId, "ItemType"));
	CacheProp(UIA_IsOffscreenPropertyId, NameOf(UIA_IsOffscreenPropertyId, "IsOffscreen"));
	CacheProp(UIA_OrientationPropertyId, NameOf(UIA_OrientationPropertyId, "Orientation"));
	CacheProp(UIA_FrameworkIdPropertyId, NameOf(UIA_FrameworkIdPropertyId, "FrameworkId"));
	CacheProp(UIA_IsRequiredForFormPropertyId, NameOf(UIA_IsRequiredForFormPropertyId, "IsRequiredForForm"));
	CacheProp(UIA_ItemStatusPropertyId, NameOf(UIA_ItemStatusPropertyId, "ItemStatus"));
	CacheProp(UIA_IsDockPatternAvailablePropertyId, NameOf(UIA_IsDockPatternAvailablePropertyId, "IsDockPatternAvailable"));
	CacheProp(UIA_IsExpandCollapsePatternAvailablePropertyId, NameOf(UIA_IsExpandCollapsePatternAvailablePropertyId, "IsExpandCollapsePatternAvailable"));
	CacheProp(UIA_IsGridItemPatternAvailablePropertyId, NameOf(UIA_IsGridItemPatternAvailablePropertyId, "IsGridItemPatternAvailable"));
	CacheProp(UIA_IsGridPatternAvailablePropertyId, NameOf(UIA_IsGridPatternAvailablePropertyId, "IsGridPatternAvailable"));
	CacheProp(UIA_IsInvokePatternAvailablePropertyId, NameOf(UIA_IsInvokePatternAvailablePropertyId, "IsInvokePatternAvailable"));
	CacheProp(UIA_IsMultipleViewPatternAvailablePropertyId, NameOf(UIA_IsMultipleViewPatternAvailablePropertyId, "IsMultipleViewPatternAvailable"));
	CacheProp(UIA_IsRangeValuePatternAvailablePropertyId, NameOf(UIA_IsRangeValuePatternAvailablePropertyId, "IsRangeValuePatternAvailable"));
	CacheProp(UIA_IsScrollPatternAvailablePropertyId, NameOf(UIA_IsScrollPatternAvailablePropertyId, "IsScrollPatternAvailable"));
	CacheProp(UIA_IsScrollItemPatternAvailablePropertyId, NameOf(UIA_IsScrollItemPatternAvailablePropertyId, "IsScrollItemPatternAvailable"));
	CacheProp(UIA_IsSelectionItemPatternAvailablePropertyId, NameOf(UIA_IsSelectionItemPatternAvailablePropertyId, "IsSelectionItemPatternAvailable"));
	CacheProp(UIA_IsSelectionPatternAvailablePropertyId, NameOf(UIA_IsSelectionPatternAvailablePropertyId, "IsSelectionPatternAvailable"));
	CacheProp(UIA_IsTablePatternAvailablePropertyId, NameOf(UIA_IsTablePatternAvailablePropertyId, "IsTablePatternAvailable"));
	CacheProp(UIA_IsTableItemPatternAvailablePropertyId, NameOf(UIA_IsTableItemPatternAvailablePropertyId, "IsTableItemPatternAvailable"));
	CacheProp(UIA_IsTextPatternAvailablePropertyId, NameOf(UIA_IsTextPatternAvailablePropertyId, "IsTextPatternAvailable"));
	CacheProp(UIA_IsTogglePatternAvailablePropertyId, NameOf(UIA_IsTogglePatternAvailablePropertyId, "IsTogglePatternAvailable"));
	CacheProp(UIA_IsTransformPatternAvailablePropertyId, NameOf(UIA_IsTransformPatternAvailablePropertyId, "IsTransformPatternAvailable"));
	CacheProp(UIA_IsValuePatternAvailablePropertyId, NameOf(UIA_IsValuePatternAvailablePropertyId, "IsValuePatternAvailable"));
	CacheProp(UIA_IsWindowPatternAvailablePropertyId, NameOf(UIA_IsWindowPatternAvailablePropertyId, "IsWindowPatternAvailable"));
	CacheProp(UIA_ValueValuePropertyId, NameOf(UIA_ValueValuePropertyId, "ValueValue"));
	CacheProp(UIA_ValueIsReadOnlyPropertyId, NameOf(UIA_ValueIsReadOnlyPropertyId, "ValueIsReadOnly"));
	CacheProp(UIA_RangeValueValuePropertyId, NameOf(UIA_RangeValueValuePropertyId, "RangeValueValue"));
	CacheProp(UIA_RangeValueIsReadOnlyPropertyId, NameOf(UIA_RangeValueIsReadOnlyPropertyId, "RangeValueIsReadOnly"));
	CacheProp(UIA_RangeValueMinimumPropertyId, NameOf(UIA_RangeValueMinimumPropertyId, "RangeValueMinimum"));
	CacheProp(UIA_RangeValueMaximumPropertyId, NameOf(UIA_RangeValueMaximumPropertyId, "RangeValueMaximum"));
	CacheProp(UIA_RangeValueLargeChangePropertyId, NameOf(UIA_RangeValueLargeChangePropertyId, "RangeValueLargeChange"));
	CacheProp(UIA_RangeValueSmallChangePropertyId, NameOf(UIA_RangeValueSmallChangePropertyId, "RangeValueSmallChange"));
	CacheProp(UIA_ScrollHorizontalScrollPercentPropertyId, NameOf(UIA_ScrollHorizontalScrollPercentPropertyId, "ScrollHorizontalScrollPercent"));
	CacheProp(UIA_ScrollHorizontalViewSizePropertyId, NameOf(UIA_ScrollHorizontalViewSizePropertyId, "ScrollHorizontalViewSize"));
	CacheProp(UIA_ScrollVerticalScrollPercentPropertyId, NameOf(UIA_ScrollVerticalScrollPercentPropertyId, "ScrollVerticalScrollPercent"));
	CacheProp(UIA_ScrollVerticalViewSizePropertyId, NameOf(UIA_ScrollVerticalViewSizePropertyId, "ScrollVerticalViewSize"));
	CacheProp(UIA_ScrollHorizontallyScrollablePropertyId, NameOf(UIA_ScrollHorizontallyScrollablePropertyId, "ScrollHorizontallyScrollable"));
	CacheProp(UIA_ScrollVerticallyScrollablePropertyId, NameOf(UIA_ScrollVerticallyScrollablePropertyId, "ScrollVerticallyScrollable"));
	CacheProp(UIA_SelectionSelectionPropertyId, NameOf(UIA_SelectionSelectionPropertyId, "SelectionSelection"));
	CacheProp(UIA_SelectionCanSelectMultiplePropertyId, NameOf(UIA_SelectionCanSelectMultiplePropertyId, "SelectionCanSelectMultiple"));
	CacheProp(UIA_SelectionIsSelectionRequiredPropertyId, NameOf(UIA_SelectionIsSelectionRequiredPropertyId, "SelectionIsSelectionRequired"));
	CacheProp(UIA_GridRowCountPropertyId, NameOf(UIA_GridRowCountPropertyId, "GridRowCount"));
	CacheProp(UIA_GridColumnCountPropertyId, NameOf(UIA_GridColumnCountPropertyId, "GridColumnCount"));
	CacheProp(UIA_GridItemRowPropertyId, NameOf(UIA_GridItemRowPropertyId, "GridItemRow"));
	CacheProp(UIA_GridItemColumnPropertyId, NameOf(UIA_GridItemColumnPropertyId, "GridItemColumn"));
	CacheProp(UIA_GridItemRowSpanPropertyId, NameOf(UIA_GridItemRowSpanPropertyId, "GridItemRowSpan"));
	CacheProp(UIA_GridItemColumnSpanPropertyId, NameOf(UIA_GridItemColumnSpanPropertyId, "GridItemColumnSpan"));
	CacheProp(UIA_GridItemContainingGridPropertyId, NameOf(UIA_GridItemContainingGridPropertyId, "GridItemContainingGrid"));
	CacheProp(UIA_DockDockPositionPropertyId, NameOf(UIA_DockDockPositionPropertyId, "DockDockPosition"));
	CacheProp(UIA_ExpandCollapseExpandCollapseStatePropertyId, NameOf(UIA_ExpandCollapseExpandCollapseStatePropertyId, "ExpandCollapseExpandCollapseState"));
	CacheProp(UIA_MultipleViewCurrentViewPropertyId, NameOf(UIA_MultipleViewCurrentViewPropertyId, "MultipleViewCurrentView"));
	CacheProp(UIA_MultipleViewSupportedViewsPropertyId, NameOf(UIA_MultipleViewSupportedViewsPropertyId, "MultipleViewSupportedViews"));
	CacheProp(UIA_WindowCanMaximizePropertyId, NameOf(UIA_WindowCanMaximizePropertyId, "WindowCanMaximize"));
	CacheProp(UIA_WindowCanMinimizePropertyId, NameOf(UIA_WindowCanMinimizePropertyId, "WindowCanMinimize"));
	CacheProp(UIA_WindowWindowVisualStatePropertyId, NameOf(UIA_WindowWindowVisualStatePropertyId, "WindowWindowVisualState"));
	CacheProp(UIA_WindowWindowInteractionStatePropertyId, NameOf(UIA_WindowWindowInteractionStatePropertyId, "WindowWindowInteractionState"));
	CacheProp(UIA_WindowIsModalPropertyId, NameOf(UIA_WindowIsModalPropertyId, "WindowIsModal"));
	CacheProp(UIA_WindowIsTopmostPropertyId, NameOf(UIA_WindowIsTopmostPropertyId, "WindowIsTopmost"));
	CacheProp(UIA_SelectionItemIsSelectedPropertyId, NameOf(UIA_SelectionItemIsSelectedPropertyId, "SelectionItemIsSelected"));
	CacheProp(UIA_SelectionItemSelectionContainerPropertyId, NameOf(UIA_SelectionItemSelectionContainerPropertyId, "SelectionItemSelectionContainer"));
	CacheProp(UIA_TableRowHeadersPropertyId, NameOf(UIA_TableRowHeadersPropertyId, "TableRowHeaders"));
	CacheProp(UIA_TableColumnHeadersPropertyId, NameOf(UIA_TableColumnHeadersPropertyId, "TableColumnHeaders"));
	CacheProp(UIA_TableRowOrColumnMajorPropertyId, NameOf(UIA_TableRowOrColumnMajorPropertyId, "TableRowOrColumnMajor"));
	CacheProp(UIA_TableItemRowHeaderItemsPropertyId, NameOf(UIA_TableItemRowHeaderItemsPropertyId, "TableItemRowHeaderItems"));
	CacheProp(UIA_TableItemColumnHeaderItemsPropertyId, NameOf(UIA_TableItemColumnHeaderItemsPropertyId, "TableItemColumnHeaderItems"));
	CacheProp(UIA_ToggleToggleStatePropertyId, NameOf(UIA_ToggleToggleStatePropertyId, "ToggleToggleState"));
	CacheProp(UIA_TransformCanMovePropertyId, NameOf(UIA_TransformCanMovePropertyId, "TransformCanMove"));
	CacheProp(UIA_TransformCanResizePropertyId, NameOf(UIA_TransformCanResizePropertyId, "TransformCanResize"));
	CacheProp(UIA_TransformCanRotatePropertyId, NameOf(UIA_TransformCanRotatePropertyId, "TransformCanRotate"));
	CacheProp(UIA_IsLegacyIAccessiblePatternAvailablePropertyId, NameOf(UIA_IsLegacyIAccessiblePatternAvailablePropertyId, "IsLegacyIAccessiblePatternAvailable"));
	CacheProp(UIA_LegacyIAccessibleChildIdPropertyId, NameOf(UIA_LegacyIAccessibleChildIdPropertyId, "LegacyIAccessibleChildId"));
	CacheProp(UIA_LegacyIAccessibleNamePropertyId, NameOf(UIA_LegacyIAccessibleNamePropertyId, "LegacyIAccessibleName"));
	CacheProp(UIA_LegacyIAccessibleValuePropertyId, NameOf(UIA_LegacyIAccessibleValuePropertyId, "LegacyIAccessibleValue"));
	CacheProp(UIA_LegacyIAccessibleDescriptionPropertyId, NameOf(UIA_LegacyIAccessibleDescriptionPropertyId, "LegacyIAccessibleDescription"));
	CacheProp(UIA_LegacyIAccessibleRolePropertyId, NameOf(UIA_LegacyIAccessibleRolePropertyId, "LegacyIAccessibleRole"), VT_I4);
	CacheProp(UIA_LegacyIAccessibleStatePropertyId, NameOf(UIA_LegacyIAccessibleStatePropertyId, "LegacyIAccessibleState"));
	CacheProp(UIA_LegacyIAccessibleHelpPropertyId, NameOf(UIA_LegacyIAccessibleHelpPropertyId, "LegacyIAccessibleHelp"));
	CacheProp(UIA_LegacyIAccessibleKeyboardShortcutPropertyId, NameOf(UIA_LegacyIAccessibleKeyboardShortcutPropertyId, "LegacyIAccessibleKeyboardShortcut"));
	CacheProp(UIA_LegacyIAccessibleSelectionPropertyId, NameOf(UIA_LegacyIAccessibleSelectionPropertyId, "LegacyIAccessibleSelection"));
	CacheProp(UIA_LegacyIAccessibleDefaultActionPropertyId, NameOf(UIA_LegacyIAccessibleDefaultActionPropertyId, "LegacyIAccessibleDefaultAction"));
	CacheProp(UIA_AriaRolePropertyId, NameOf(UIA_AriaRolePropertyId, "AriaRole"));
	CacheProp(UIA_AriaPropertiesPropertyId, NameOf(UIA_AriaPropertiesPropertyId, "AriaProperties"));
	CacheProp(UIA_IsDataValidForFormPropertyId, NameOf(UIA_IsDataValidForFormPropertyId, "IsDataValidForForm"));
	CacheProp(UIA_ControllerForPropertyId, NameOf(UIA_ControllerForPropertyId, "ControllerFor"));
	CacheProp(UIA_DescribedByPropertyId, NameOf(UIA_DescribedByPropertyId, "DescribedBy"));
	CacheProp(UIA_FlowsToPropertyId, NameOf(UIA_FlowsToPropertyId, "FlowsTo"));
	CacheProp(UIA_ProviderDescriptionPropertyId, NameOf(UIA_ProviderDescriptionPropertyId, "ProviderDescription"));
	CacheProp(UIA_IsItemContainerPatternAvailablePropertyId, NameOf(UIA_IsItemContainerPatternAvailablePropertyId, "IsItemContainerPatternAvailable"));
	CacheProp(UIA_IsVirtualizedItemPatternAvailablePropertyId, NameOf(UIA_IsVirtualizedItemPatternAvailablePropertyId, "IsVirtualizedItemPatternAvailable"));
	CacheProp(UIA_IsSynchronizedInputPatternAvailablePropertyId, NameOf(UIA_IsSynchronizedInputPatternAvailablePropertyId, "IsSynchronizedInputPatternAvailable"));
	CacheProp(UIA_OptimizeForVisualContentPropertyId, NameOf(UIA_OptimizeForVisualContentPropertyId, "OptimizeForVisualContent"));
	CacheProp(UIA_IsObjectModelPatternAvailablePropertyId, NameOf(UIA_IsObjectModelPatternAvailablePropertyId, "IsObjectModelPatternAvailable"));
	CacheProp(UIA_AnnotationAnnotationTypeIdPropertyId, NameOf(UIA_AnnotationAnnotationTypeIdPropertyId, "AnnotationAnnotationTypeId"));
	CacheProp(UIA_AnnotationAnnotationTypeNamePropertyId, NameOf(UIA_AnnotationAnnotationTypeNamePropertyId, "AnnotationAnnotationTypeName"));
	CacheProp(UIA_AnnotationAuthorPropertyId, NameOf(UIA_AnnotationAuthorPropertyId, "AnnotationAuthor"));
	CacheProp(UIA_AnnotationDateTimePropertyId, NameOf(UIA_AnnotationDateTimePropertyId, "AnnotationDateTime"));
	CacheProp(UIA_AnnotationTargetPropertyId, NameOf(UIA_AnnotationTargetPropertyId, "AnnotationTarget"));
	CacheProp(UIA_IsAnnotationPatternAvailablePropertyId, NameOf(UIA_IsAnnotationPatternAvailablePropertyId, "IsAnnotationPatternAvailable"));
	CacheProp(UIA_IsTextPattern2AvailablePropertyId, NameOf(UIA_IsTextPattern2AvailablePropertyId, "IsTextPattern2Available"));
	CacheProp(UIA_StylesStyleIdPropertyId, NameOf(UIA_StylesStyleIdPropertyId, "StylesStyleId"));
	CacheProp(UIA_StylesStyleNamePropertyId, NameOf(UIA_StylesStyleNamePropertyId, "StylesStyleName"));
	CacheProp(UIA_StylesFillColorPropertyId, NameOf(UIA_StylesFillColorPropertyId, "StylesFillColor"));
	CacheProp(UIA_StylesFillPatternStylePropertyId, NameOf(UIA_StylesFillPatternStylePropertyId, "StylesFillPatternStyle"));
	CacheProp(UIA_StylesShapePropertyId, NameOf(UIA_StylesShapePropertyId, "StylesShape"));
	CacheProp(UIA_StylesFillPatternColorPropertyId, NameOf(UIA_StylesFillPatternColorPropertyId, "StylesFillPatternColor"));
	CacheProp(UIA_StylesExtendedPropertiesPropertyId, NameOf(UIA_StylesExtendedPropertiesPropertyId, "StylesExtendedProperties"));
	CacheProp(UIA_IsStylesPatternAvailablePropertyId, NameOf(UIA_IsStylesPatternAvailablePropertyId, "IsStylesPatternAvailable"));
	CacheProp(UIA_IsSpreadsheetPatternAvailablePropertyId, NameOf(UIA_IsSpreadsheetPatternAvailablePropertyId, "IsSpreadsheetPatternAvailable"));
	CacheProp(UIA_SpreadsheetItemFormulaPropertyId, NameOf(UIA_SpreadsheetItemFormulaPropertyId, "SpreadsheetItemFormula"));
	CacheProp(UIA_SpreadsheetItemAnnotationObjectsPropertyId, NameOf(UIA_SpreadsheetItemAnnotationObjectsPropertyId, "SpreadsheetItemAnnotationObjects"));
	CacheProp(UIA_SpreadsheetItemAnnotationTypesPropertyId, NameOf(UIA_SpreadsheetItemAnnotationTypesPropertyId, "SpreadsheetItemAnnotationTypes"));
	CacheProp(UIA_IsSpreadsheetItemPatternAvailablePropertyId, NameOf(UIA_IsSpreadsheetItemPatternAvailablePropertyId, "IsSpreadsheetItemPatternAvailable"));
	CacheProp(UIA_Transform2CanZoomPropertyId, NameOf(UIA_Transform2CanZoomPropertyId, "Transform2CanZoom"));
	CacheProp(UIA_IsTransformPattern2AvailablePropertyId, NameOf(UIA_IsTransformPattern2AvailablePropertyId, "IsTransformPattern2Available"));
	CacheProp(UIA_LiveSettingPropertyId, NameOf(UIA_LiveSettingPropertyId, "LiveSetting"));
	CacheProp(UIA_IsTextChildPatternAvailablePropertyId, NameOf(UIA_IsTextChildPatternAvailablePropertyId, "IsTextChildPatternAvailable"));
	CacheProp(UIA_IsDragPatternAvailablePropertyId, NameOf(UIA_IsDragPatternAvailablePropertyId, "IsDragPatternAvailable"));
	CacheProp(UIA_DragIsGrabbedPropertyId, NameOf(UIA_DragIsGrabbedPropertyId, "DragIsGrabbed"));
	CacheProp(UIA_DragDropEffectPropertyId, NameOf(UIA_DragDropEffectPropertyId, "DragDropEffect"));
	CacheProp(UIA_DragDropEffectsPropertyId, NameOf(UIA_DragDropEffectsPropertyId, "DragDropEffects"));
	CacheProp(UIA_IsDropTargetPatternAvailablePropertyId, NameOf(UIA_IsDropTargetPatternAvailablePropertyId, "IsDropTargetPatternAvailable"));
	CacheProp(UIA_DropTargetDropTargetEffectPropertyId, NameOf(UIA_DropTargetDropTargetEffectPropertyId, "DropTargetDropTargetEffect"));
	CacheProp(UIA_DropTargetDropTargetEffectsPropertyId, NameOf(UIA_DropTargetDropTargetEffectsPropertyId, "DropTargetDropTargetEffects"));
	CacheProp(UIA_DragGrabbedItemsPropertyId, NameOf(UIA_DragGrabbedItemsPropertyId, "DragGrabbedItems"));
	CacheProp(UIA_Transform2ZoomLevelPropertyId, NameOf(UIA_Transform2ZoomLevelPropertyId, "Transform2ZoomLevel"));
	CacheProp(UIA_Transform2ZoomMinimumPropertyId, NameOf(UIA_Transform2ZoomMinimumPropertyId, "Transform2ZoomMinimum"));
	CacheProp(UIA_Transform2ZoomMaximumPropertyId, NameOf(UIA_Transform2ZoomMaximumPropertyId, "Transform2ZoomMaximum"));
	CacheProp(UIA_FlowsFromPropertyId, NameOf(UIA_FlowsFromPropertyId, "FlowsFrom"));
	CacheProp(UIA_IsTextEditPatternAvailablePropertyId, NameOf(UIA_IsTextEditPatternAvailablePropertyId, "IsTextEditPatternAvailable"));
	CacheProp(UIA_IsPeripheralPropertyId, NameOf(UIA_IsPeripheralPropertyId, "IsPeripheral"));
	CacheProp(UIA_IsCustomNavigationPatternAvailablePropertyId, NameOf(UIA_IsCustomNavigationPatternAvailablePropertyId, "IsCustomNavigationPatternAvailable"));
	CacheProp(UIA_PositionInSetPropertyId, NameOf(UIA_PositionInSetPropertyId, "PositionInSet"));
	CacheProp(UIA_SizeOfSetPropertyId, NameOf(UIA_SizeOfSetPropertyId, "SizeOfSet"));
	CacheProp(UIA_LevelPropertyId, NameOf(UIA_LevelPropertyId, "Level"));
	CacheProp(UIA_AnnotationTypesPropertyId, NameOf(UIA_AnnotationTypesPropertyId, "AnnotationTypes"));
	CacheProp(UIA_AnnotationObjectsPropertyId, NameOf(UIA_AnnotationObjectsPropertyId, "AnnotationObjects"));
	CacheProp(UIA_LandmarkTypePropertyId, NameOf(UIA_LandmarkTypePropertyId, "LandmarkType"));
	CacheProp(UIA_LocalizedLandmarkTypePropertyId, NameOf(UIA_LocalizedLandmarkTypePropertyId, "LocalizedLandmarkType"));
	CacheProp(UIA_FullDescriptionPropertyId, NameOf(UIA_FullDescriptionPropertyId, "FullDescription"));
	CacheProp(UIA_FillColorPropertyId, NameOf(UIA_FillColorPropertyId, "FillColor"));
	CacheProp(UIA_OutlineColorPropertyId, NameOf(UIA_OutlineColorPropertyId, "OutlineColor"));
	CacheProp(UIA_FillTypePropertyId, NameOf(UIA_FillTypePropertyId, "FillType"));
	CacheProp(UIA_VisualEffectsPropertyId, NameOf(UIA_VisualEffectsPropertyId, "VisualEffects"));
	CacheProp(UIA_OutlineThicknessPropertyId, NameOf(UIA_OutlineThicknessPropertyId, "OutlineThickness"));
	CacheProp(UIA_CenterPointPropertyId, NameOf(UIA_CenterPointPropertyId, "CenterPoint"));
	CacheProp(UIA_RotationPropertyId, NameOf(UIA_RotationPropertyId, "Rotation"));
	CacheProp(UIA_SizePropertyId, NameOf(UIA_SizePropertyId, "Size"));
	CacheProp(UIA_IsSelectionPattern2AvailablePropertyId, NameOf(UIA_IsSelectionPattern2AvailablePropertyId, "IsSelectionPattern2Available"));
	CacheProp(UIA_Selection2FirstSelectedItemPropertyId, NameOf(UIA_Selection2FirstSelectedItemPropertyId, "Selection2FirstSelectedItem"));
	CacheProp(UIA_Selection2LastSelectedItemPropertyId, NameOf(UIA_Selection2LastSelectedItemPropertyId, "Selection2LastSelectedItem"));
	CacheProp(UIA_Selection2CurrentSelectedItemPropertyId, NameOf(UIA_Selection2CurrentSelectedItemPropertyId, "Selection2CurrentSelectedItem"));
	CacheProp(UIA_Selection2ItemCountPropertyId, NameOf(UIA_Selection2ItemCountPropertyId, "Selection2ItemCount"));
	CacheProp(UIA_HeadingLevelPropertyId, NameOf(UIA_HeadingLevelPropertyId, "HeadingLevel"));
}

void UIAObj::FreePropertyMap() {
	map<PROPERTYID, PropInfo*>::iterator iter;
	for (iter = ID_PROPERTY_MAP.begin(); iter != ID_PROPERTY_MAP.end();) {
		delete iter->second;
		ID_PROPERTY_MAP.erase(iter++);
	}

	ID_PROPERTY_MAP.clear();
	NAME_PROPERTY_MAP.clear();
}

UIAObj::UIAObj() {
	CoInitialize(NULL);

	//创建自动化对象
	HRESULT hResult = CoCreateInstance(__uuidof(CUIAutomation), NULL, CLSCTX_INPROC_SERVER, IID_PPV_ARGS(&_automation));
	if (FAILED(hResult))
		throw string("创建自动化对象失败");

	//创建树形遍历器
	_automation->get_RawViewWalker(&_treeWalker);

	//初始化属性信息缓存
	InitPropertyMap();
}

UIAObj::~UIAObj() {
	//释放属性信息缓存
	FreePropertyMap();

	//释放树形遍历器
	_treeWalker->Release();
	//释放自动化对象
	_automation->Release();

	CoUninitialize();
}

IUIAutomation * UIAObj::GetAutomation() {
	/*if(_automation!=NULL)
		_automation->Release();
	CoInitialize(NULL);
	//创建自动化对象
	HRESULT hResult = CoCreateInstance(__uuidof(CUIAutomation), NULL, CLSCTX_INPROC_SERVER, IID_PPV_ARGS(&_automation));
	if (FAILED(hResult))
		throw string("创建自动化对象失败");
		*/
	return _automation;
}

IUIAutomationTreeWalker * UIAObj::GetWalker() {
	return _treeWalker;
}

string UIAObj::GetRuntimeId(IUIAutomationElement *uiElement) {
	SAFEARRAY *pArray;
	HRESULT hResult = uiElement->GetRuntimeId(&pArray);
	if (FAILED(hResult))
		throw string("获取RuntimeId失败");

	return IntegerArray2string(pArray);
}

CONTROLTYPEID UIAObj::GetControlType(IUIAutomationElement *uiElement) {
	CONTROLTYPEID controlType;
	HRESULT hResult = uiElement->get_CurrentControlType(&controlType);
	if (FAILED(hResult))
		throw string("获取ControlType失败");

	return controlType;
}