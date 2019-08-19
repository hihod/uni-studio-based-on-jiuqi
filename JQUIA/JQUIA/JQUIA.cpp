// JQUIA.cpp : 定义 DLL 应用程序的导出函数。
//

#include "jni.h"
#include "stdafx.h"
#include "com_jiuqi_rpa_uiadll_JQUIA.h"
#include "utils\JNIUitls.h"
#include "library\UIAObj.h"
#include "library\UIATreeWalker.h"
#include "library\UIAFindLibrary.h"
#include "library\UIAMouseLibrary.h"
#include "library\UIAImageLibrary.h"
#include "library\UIAPickerLibrary.h"
#include "library\UIADrawerLibrary.h"
#include "library\UIAWindowLibrary.h"
#include "library\UIADialogLibrary.h"
#include "library\UIAControlLibrary.h"
#include "library\UIAKeyboardLibrary.h"
#include "library\UIAApplicationLibrary.h"
using namespace std;

UIAObj *uiaObj;
UIATreeWalker *treeWalker;
UIAFindLibrary *findLibrary;
UIAMouseLibrary *mouseLibrary;
UIAImageLibrary *imageLibrary;
UIAPickerLibrary *pickerLibrary;
UIADrawerLibrary *drawerLibrary;
UIAWindowLibrary *windowLibrary;
UIADialogLibrary *dialogLibrary;
UIAControlLibrary *controlLibrary;
UIAKeyboardLibrary *keyboardLibrary;
UIAApplicationLibrary *applicationLibrary;

void DoInitialize() {
	if (uiaObj)
		return;
	uiaObj = new UIAObj();
	treeWalker = new UIATreeWalker(uiaObj);
	findLibrary = new UIAFindLibrary(uiaObj);
	mouseLibrary = new UIAMouseLibrary(uiaObj);
	pickerLibrary = new UIAPickerLibrary();
	drawerLibrary = new UIADrawerLibrary();
	windowLibrary = new UIAWindowLibrary(uiaObj);
	dialogLibrary = new UIADialogLibrary();
	controlLibrary = new UIAControlLibrary(uiaObj);
	keyboardLibrary = new UIAKeyboardLibrary(uiaObj);
	applicationLibrary = new UIAApplicationLibrary(uiaObj);
	imageLibrary = new UIAImageLibrary();
}

void UnInitialize() {
	delete applicationLibrary;
	delete keyboardLibrary;
	delete controlLibrary;
	delete dialogLibrary;
	delete windowLibrary;
	delete drawerLibrary;
	delete pickerLibrary;
	delete mouseLibrary;
	delete findLibrary;
	delete treeWalker;
	delete uiaObj;
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA__1initialize(JNIEnv *env, jclass jCls) {
	DoInitialize();
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA__1finalize(JNIEnv *env, jclass jCls) {
	UnInitialize();
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_releaseById(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		uiaObj->ReleaseById(id);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_releaseByIds(JNIEnv *env, jclass jCls, jlongArray ids) {
	try {
		DoInitialize();

		vector<LONG64> vector;
		jlongArray2longVector(env, ids, &vector);
		uiaObj->ReleaseByIds(vector);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT jlong JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_find_1get(JNIEnv *env, jclass jCls, jobject pointObj) {
	try {
		DoInitialize();

		POINT pt = { 0, 0 };
		jobject2point(env, pointObj, &pt);

		return findLibrary->Get(pt);
	}
	catch (string s) {
		throwNew(env, s);
		return 0;
	}
}

JNIEXPORT jlong JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_find_1getWindow(JNIEnv *env, jclass jCls, jobject pointObj) {
	try {
		DoInitialize();

		POINT pt = { 0, 0 };
		jobject2point(env, pointObj, &pt);

		return findLibrary->GetWindowByPoint(pt);
	}
	catch (string s) {
		throwNew(env, s);
		return 0;
	}
}

JNIEXPORT jlong JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_find_1getWindowByPath(JNIEnv *env, jclass jCls, jstring pathstr) {
	try {
		DoInitialize();

		Path path;
		jstring2path(env, pathstr, &path);

		return findLibrary->GetWindowByPath(path);
	}
	catch (string s) {
		throwNew(env, s);
		return 0;
	}
}

JNIEXPORT jlong JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_find_1getWindowByElement(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();
		IUIAutomationElement *uiElement = uiaObj->Id2Element(id);

		return findLibrary->GetWindowByElement(uiElement);
	}
	catch (string s) {
		throwNew(env, s);
		return 0;
	}
}

JNIEXPORT jlong JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_find_1getWindowByBrowserInfo(JNIEnv *env, jclass jCls, jobject jObj) {
	try {
		DoInitialize();
		BrowserInfo info;
		jobject2browserInfo(env, jObj, &info);

		return findLibrary->GetWindowByBrowserInfo(info);
	}
	catch (string s) {
		throwNew(env, s);
		return 0;
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_find_1getRect(JNIEnv *env, jclass jCls, jobject pointObj, jobject rectObj) {
	try {
		DoInitialize();

		POINT pt = { 0, 0 };
		jobject2point(env, pointObj, &pt);

		UIARect uiaRect = { 0, 0, 0, 0, FALSE };
		findLibrary->GetRect(pt, &uiaRect);
		uiarect2jobject(env, uiaRect, rectObj);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_find_1getWindowRect(JNIEnv *env, jclass jCls, jobject pointObj, jobject rectObj) {
	try {
		DoInitialize();

		POINT pt = { 0, 0 };
		jobject2point(env, pointObj, &pt);

		UIARect uiaRect = { 0, 0, 0, 0, FALSE };
		findLibrary->GetWindowRect(pt, &uiaRect);
		uiarect2jobject(env, uiaRect, rectObj);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_find_1getBrowserCientRect(JNIEnv *env, jclass jCls, jobject browserInfoObj, jobject rectObj) {
	try {
		DoInitialize();

		BrowserInfo browserInfo;
		jobject2browserInfo(env, browserInfoObj, &browserInfo);

		UIARect uiaRect = { 0, 0, 0, 0, FALSE };
		findLibrary->GetRect(browserInfo, &uiaRect);
		uiarect2jobject(env, uiaRect, rectObj);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT jboolean JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_find_1exists(JNIEnv *env, jclass jCls, jstring pathJsonStr) {
	try {
		DoInitialize();

		Path path;
		jstring2path(env, pathJsonStr, &path);

		return bool2jboolean(findLibrary->Exists(path));
	}
	catch (string s) {
		throwNew(env, s);
		return FALSE;
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_find_1getBrowserInfo(JNIEnv *env, jclass jCls, jobject pointObj, jobject browserInfoObj) {
	try {
		DoInitialize();

		POINT pt = { 0, 0 };
		jobject2point(env, pointObj, &pt);

		BrowserInfo browserInfo;
		findLibrary->GetBrowserInfo(pt, &browserInfo);
		browserinfo2jobject(env, browserInfo, browserInfoObj);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT jstring JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_find_1getPath(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		Path *path = new Path();
		findLibrary->GetPath(id, path);

		jstring pathJStr = path2jstring(env, *path);
		delete path;

		return pathJStr;
	}
	catch (string s) {
		throwNew(env, s);
		return env->NewStringUTF("");
	}
}

JNIEXPORT jlong JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_find_1findFirst(JNIEnv *env, jclass jCls, jstring pathJsonStr) {
	try {
		DoInitialize();

		Path *path = new Path();
		jstring2path(env, pathJsonStr, path);

		jlong id = findLibrary->FindFirst(*path);
		delete path;

		return id;
	}
	catch (string s) {
		throwNew(env, s);
		return 0;
	}
}

JNIEXPORT jlong JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_find_1findFirstEx(JNIEnv *env, jclass jCls, jstring pathJsonStr, jobject runtimeIdListObj) {
	try {
		DoInitialize();

		Path *path = new Path();
		jstring2path(env, pathJsonStr, path);

		jlong id = findLibrary->FindFirst(*path);

		if (runtimeIdListObj != NULL) {
			jclass cls_list = env->GetObjectClass(runtimeIdListObj);
			jmethodID mtd_add = env->GetMethodID(cls_list, "add", "(Ljava/lang/Object;)Z");

			for (size_t i = 0; i < path->elements.size(); i++) {
				jstring runtimeId = env->NewStringUTF(path->elements[i]->runtimeId.data());
				env->CallBooleanMethod(runtimeIdListObj, mtd_add, runtimeId);
			}
		}

		delete path;

		return id;
	}
	catch (string s) {
		throwNew(env, s);
		return 0;
	}
}

JNIEXPORT jlongArray JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_find_1findAll(JNIEnv *env, jclass jCls, jstring pathJsonStr) {
	try {
		DoInitialize();

		Path *path = new Path();
		jstring2path(env, pathJsonStr, path);

		vector<LONG64> ids;
		findLibrary->FindAll(*path, ids);
		jlongArray jlongAry = longVector2jlongArray(env, ids);
		delete path;

		return jlongAry;
	}
	catch (string s) {
		throwNew(env, s);
		return env->NewLongArray(0);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_mouse_1mouseMove(JNIEnv *env, jclass jCls, jobject pointObj) {
	try {
		DoInitialize();

		POINT pt = { 0, 0 };
		jobject2point(env, pointObj, &pt);

		mouseLibrary->MouseMove(pt);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_mouse_1mouseClick(JNIEnv *env, jclass jCls, jobject pointObj, jint clickType, jint mousekey, jintArray maskkeyArray) {
	try {
		DoInitialize();

		POINT pt = { 0, 0 };
		jobject2point(env, pointObj, &pt);

		vector<INT> maskkeys;
		jintArray2intVector(env, maskkeyArray, &maskkeys);

		mouseLibrary->MouseClick(pt, clickType, mousekey, maskkeys);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_mouse_1simulateClick(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		mouseLibrary->SimulateClick(id);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_mouse_1messageClick(JNIEnv *env, jclass jCls, jlong id, jobject pointObj, jint clickType, jint mouseKey, jintArray maskKeyArray) {
	try {
		DoInitialize();

		POINT pt = { 0, 0 };
		jobject2point(env, pointObj, &pt);

		jsize size = env->GetArrayLength(maskKeyArray);
		jint *array = env->GetIntArrayElements(maskKeyArray, FALSE);

		vector<INT> maskKeys;
		for (int i = 0; i < size; i++)
			maskKeys.insert(maskKeys.end(), array[i]);

		mouseLibrary->MessageClick(id, pt, clickType, mouseKey, maskKeys);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_keyboard_1clearText(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		keyboardLibrary->ClearText(id);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_keyboard_1typeText(JNIEnv *env, jclass jCls, jstring text) {
	try {
		DoInitialize();

		string textStr = jstring2string(env, text);
		keyboardLibrary->TypeText(textStr);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_keyboard_1simulateTypeText(JNIEnv *env, jclass jCls, jlong id, jstring text) {
	try {
		DoInitialize();

		string textStr = jstring2string(env, text);
		controlLibrary->SetText(id, textStr);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_keyboard_1sendHotKey(JNIEnv *env, jclass jCls, jint hotKey, jintArray maskKeyArray) {
	try {
		DoInitialize();

		vector<INT> maskKeys;
		jintArray2intVector(env, maskKeyArray, &maskKeys);

		keyboardLibrary->SendHotKey(hotKey, maskKeys);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_keyboard_1sendMessageHotkey(JNIEnv *env, jclass jCls, jlong id, jint hotKey, jintArray maskKeyArray) {
	try {
		DoInitialize();

		vector<INT> maskKeys;
		jintArray2intVector(env, maskKeyArray, &maskKeys);

		keyboardLibrary->SendMessageHotkey(id, hotKey, maskKeys);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT jlong JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_tree_1getRoot(JNIEnv *env, jclass jCls) {
	try {
		DoInitialize();

		return treeWalker->GetRoot();
	}
	catch (string s) {
		throwNew(env, s);
		return 0;
	}
}

JNIEXPORT jlongArray JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_tree_1getChildren(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		vector<LONG64> children;
		treeWalker->GetChildren(id, &children);
		return longVector2jlongArray(env, children);
	}
	catch (string s) {
		throwNew(env, s);
		return env->NewLongArray(0);
	}
}

JNIEXPORT jlong JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_tree_1getParent(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		return treeWalker->GetParent(id);
	}
	catch (string s) {
		throwNew(env, s);
		return 0;
	}
}

JNIEXPORT jstring JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_tree_1getText(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		string textStr = treeWalker->GetText(id);
		return string2jstring(env, textStr);
	}
	catch (string s) {
		throwNew(env, s);
		return env->NewStringUTF("");
	}
}

JNIEXPORT jstring JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_tree_1getProperties(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		map<string, string> properties;
		treeWalker->GetProperties(id, &properties);
		jstring s = properties2jstring(env, properties);
		return s;
	}
	catch (string s) {
		throwNew(env, s);
		return env->NewStringUTF("");
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_control_1getRect(JNIEnv *env, jclass jCls, jlong id, jobject rectObj) {
	try {
		DoInitialize();

		RECT rect;
		controlLibrary->GetRect(id, &rect);
		rect2jobject(env, rect, rectObj);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT jstring JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_control_1getAttributeValue(JNIEnv *env, jclass jCls, jlong id, jstring attrName) {
	try {
		DoInitialize();

		string attrNameStr = jstring2string(env, attrName);
		string attrValueStr = controlLibrary->GetAttributeValue(id, attrNameStr);
		return string2jstring(env, attrValueStr);
	}
	catch (string s) {
		throwNew(env, s);
		return env->NewStringUTF("");
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_control_1ScrollIntoView(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		controlLibrary->ScrollIntoView(id);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_control_1setFocus(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		controlLibrary->SetFocus(id);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT jboolean JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_control_1isChecked(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		return controlLibrary->IsChecked(id);
	}
	catch (string s) {
		throwNew(env, s);
		return FALSE;
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_control_1setChecked(JNIEnv *env, jclass jCls, jlong id, jboolean checked) {
	try {
		DoInitialize();

		controlLibrary->SetChecked(id, checked);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT jint JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_control_1getCheckState(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		return controlLibrary->GetCheckState(id);
	}
	catch (string s) {
		throwNew(env, s);
		return 0;
	}
}

JNIEXPORT jstring JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_control_1getText(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		string text = controlLibrary->GetText(id);
		return string2jstring(env, text);
	}
	catch (string s) {
		throwNew(env, s);
		return env->NewStringUTF("");
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_control_1setText(JNIEnv *env, jclass jCls, jlong id, jstring text) {
	try {
		DoInitialize();

		string textStr = jstring2string(env, text);
		controlLibrary->SetText(id, textStr);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT jboolean JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_control_1isPassword(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		return controlLibrary->IsPassword(id);
	}
	catch (string s) {
		throwNew(env, s);
		return FALSE;
	}
}

JNIEXPORT jboolean JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_control_1enable(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		return controlLibrary->Enable(id);
	}
	catch (string s) {
		throwNew(env, s);
		return FALSE;
	}
}

JNIEXPORT jboolean JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_control_1visible(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		return controlLibrary->Visible(id);
	}
	catch (string s) {
		throwNew(env, s);
		return FALSE;
	}
}

JNIEXPORT jboolean JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_control_1isTable(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		return controlLibrary->IsTable(id);
	}
	catch (string s) {
		throwNew(env, s);
		return FALSE;
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_control_1clearSelection(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		controlLibrary->ClearSelection(id);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_control_1selectItems(JNIEnv *env, jclass jCls, jlong id, jobjectArray itemArray) {
	try {
		DoInitialize();

		vector<string> items;
		jobjectArray2stringVector(env, itemArray, &items);
		controlLibrary->SelectItems(id, items);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT jstring JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_control_1getPageData(JNIEnv *env, jclass jCls, jlong, jobjectArray columnArray) {
	try {
		DoInitialize();

		vector<string> columnStrs;
		jobjectArray2stringVector(env, columnArray, &columnStrs);

		vector<Path *> columns;
		for (unsigned int i = 0; i < columnStrs.size(); i++) {
			Path *path = new Path();
			jstring2path(env, string2jstring(env, columnStrs[i]), path);
			columns.insert(columns.end(), path);
		}

		string dataStr = controlLibrary->GetPageData(columns);
		return string2jstring(env, dataStr);

		for (unsigned int i = 0; i < columns.size(); i++)
			delete columns[i];
		columns.clear();
	}
	catch (string s) {
		throwNew(env, s);
		return env->NewStringUTF("");
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_window_1setWindowState(JNIEnv *env, jclass jCls, jlong id, jint ws) {
	try {
		DoInitialize();

		windowLibrary->SetWindowState(id, ws);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_window_1moveWindow(JNIEnv *env, jclass jCls, jlong id, jobject rectObj) {
	try {
		DoInitialize();

		RECT rect;
		jobject2rect(env, rectObj, &rect);
		windowLibrary->MoveWindow_(id, rect);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_window_1closeWindow(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		windowLibrary->CloseWindow(id);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT jstring JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_dialog_1inputDialog(JNIEnv *env, jclass jCls, jstring title, jstring label, jobjectArray items, jboolean isPassword) {
	try {
		DoInitialize();

		string titleStr = jstring2string(env, title);
		string labelStr = jstring2string(env, label);

		vector<string> vector;
		jobjectArray2stringVector(env, items, &vector);

		string retStr = dialogLibrary->InputDialog(titleStr, labelStr, vector, isPassword);
		return string2jstring(env, retStr);
	}
	catch (string s) {
		throwNew(env, s);
		return env->NewStringUTF("");
	}
}

JNIEXPORT jstring JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_dialog_1messageDialog(JNIEnv *env, jclass jCls, jstring title, jstring message, jlong btnSuite) {
	try {
		DoInitialize();

		string titleStr = jstring2string(env, title);
		string messageStr = jstring2string(env, message);

		string retStr = dialogLibrary->MessageDialog(titleStr, messageStr, btnSuite);
		return string2jstring(env, retStr);
	}
	catch (string s) {
		throwNew(env, s);
		return env->NewStringUTF("");
	}
}

JNIEXPORT jstring JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_dialog_1fileDialog(JNIEnv *env, jclass jCls, jstring initialDir, jstring filters) {
	try {
		DoInitialize();

		string initialDirStr = jstring2string(env, initialDir);
		string filtersStr = jstring2string(env, filters);

		string retStr = dialogLibrary->FileDialog(initialDirStr, filtersStr);
		return string2jstring(env, retStr);
	}
	catch (string s) {
		throwNew(env, s);
		return env->NewStringUTF("");
	}
}

JNIEXPORT jstring JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_dialog_1directoryDialog(JNIEnv *env, jclass jCls, jstring initialDir) {
	try {
		DoInitialize();

		string initialDirStr = jstring2string(env, initialDir);

		string retStr = dialogLibrary->DirectoryDialog(initialDirStr);
		return string2jstring(env, retStr);
	}
	catch (string s) {
		throwNew(env, s);
		return env->NewStringUTF("");
	}
}

JNIEXPORT jlong JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_application_1startProcess(JNIEnv *env, jclass jCls, jstring applicationPath, jstring args) {
	try {
		DoInitialize();

		string applicationPathStr = jstring2string(env, applicationPath);
		string argsStr = jstring2string(env, args);
		return applicationLibrary->StartProcess(applicationPathStr, argsStr);
	}
	catch (string s) {
		throwNew(env, s);
		return 0;
	}
}

JNIEXPORT jlong JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_application_1getApplicationWindow(JNIEnv *env, jclass jCls, jlong pId) {
	try {
		DoInitialize();

		return applicationLibrary->GetApplicationWindow(pId);
	}
	catch (string s) {
		throwNew(env, s);
		return 0;
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_application_1closeApplication(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		applicationLibrary->CloseApplication(id);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT jlongArray JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_application_1findProcess(JNIEnv *env, jclass jCls, jstring processName) {
	try {
		DoInitialize();

		string processNameStr = jstring2string(env, processName);

		vector<LONG64> ids;
		applicationLibrary->FindProcess(processNameStr, &ids);
		return longVector2jlongArray(env, ids);
	}
	catch (string s) {
		throwNew(env, s);
		return env->NewLongArray(0);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_application_1killProcess(JNIEnv *env, jclass jCls, jlong id) {
	try {
		DoInitialize();

		applicationLibrary->KillProcess(id);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_picker_1startPick(JNIEnv *env, jclass jCls, jobject callbackObj) {
	try {
		DoInitialize();

		pickerLibrary->StartPick(env, callbackObj);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_picker_1endPick(JNIEnv *env, jclass jCls) {
	try {
		DoInitialize();

		pickerLibrary->EndPick();
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_drawer_1startDraw(JNIEnv *env, jclass jCls, jobject rectObj, jstring color) {
	try {
		DoInitialize();

		RECT rect = { 0, 0, 0, 0 };
		jobject2rect(env, rectObj, &rect);

		string colorStr = jstring2string(env, color);
		COLORREF colorref = string2color(colorStr);

		drawerLibrary->StartDraw(rect, colorref);
	}
	catch (string s) {
		throwNew(env, s);
	}
}

JNIEXPORT void JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_drawer_1endDraw(JNIEnv *env, jclass jCls) {
	try {
		DoInitialize();

		drawerLibrary->EndDraw();
	}
	catch (string s) {
		throwNew(env, s);
	}
}
JNIEXPORT jbyteArray JNICALL Java_com_jiuqi_rpa_uiadll_JQUIA_image_1doScreenShot(JNIEnv *env, jclass jCls, jobject rectObj) {
	try {
		DoInitialize();

		RECT rect = { 0, 0, 0, 0 };
		jobject2rect(env, rectObj, &rect);
		BYTE* imgBytes;
		DWORD size = 0;

		size = imageLibrary->doSnapShot(&imgBytes, rect);

		jbyte *by = (jbyte*)imgBytes;
		jsize nOutSize = size;
		jbyteArray jarray = env->NewByteArray(nOutSize);
		env->SetByteArrayRegion(jarray, 0, nOutSize, by);
		return jarray;
	}
	catch (string s) {
		throwNew(env, s);
		return env->NewByteArray(0);
	}
}