#include "stdafx.h"
#include "stdlib.h"
#include "tlhelp32.h"
#include <jni.h>
#include <string>
#include <iostream>
#include <Psapi.h>
#include "utils\json\json.h"
#include "utils\JNIUitls.h"
using namespace std;

char *jstring2windows(JNIEnv *env, jstring s) {
	try {
		if (!s)
			return NULL;

		jclass clsstring = env->FindClass("java/lang/String");
		jstring strencode = env->NewStringUTF("GBK");
		jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
		jbyteArray barr = (jbyteArray)env->CallObjectMethod(s, mid, strencode);
		if (barr == nullptr) throw "err";
		jsize alen = env->GetArrayLength(barr);
		jbyte* ba = env->GetByteArrayElements(barr, JNI_FALSE);

		char* rtn = NULL;
		if (alen > 0) {
			rtn = (char*)malloc(alen + 1);
			memcpy(rtn, ba, alen);
			rtn[alen] = 0;
		}
		env->ReleaseByteArrayElements(barr, ba, 0);

		return rtn;
	}
	catch (string s) {
		throw s;
	}
}

jstring windows2jstring(JNIEnv* env, const char *s) {
	//定义java String类 strClass
	jclass strClass = (env)->FindClass("java/lang/String");
	//获取String(byte[],String)的构造器,用于将本地byte[]数组转换为一个新String
	jmethodID ctorID = (env)->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
	//建立byte数组
	jbyteArray bytes = (env)->NewByteArray((jsize)strlen(s));
	//将char* 转换为byte数组
	(env)->SetByteArrayRegion(bytes, 0, (jsize)strlen(s), (jbyte*)s);
	//设置String, 保存语言类型,用于byte数组转换至String时的参数
	jstring encoding = (env)->NewStringUTF("GBK");
	//将byte数组转换为java String,并输出
	return (jstring)(env)->NewObject(strClass, ctorID, bytes, encoding);
}

string wstring2string(const wstring &wstr) {
	LPCWSTR pwszSrc = wstr.c_str();
	int nLen = WideCharToMultiByte(CP_ACP, 0, pwszSrc, -1, NULL, 0, NULL, NULL);
	if (nLen == 0)
		return std::string("");

	char* pszDst = new char[nLen];
	if (!pszDst)
		return std::string("");

	WideCharToMultiByte(CP_ACP, 0, pwszSrc, -1, pszDst, nLen, NULL, NULL);
	std::string str(pszDst);
	delete[] pszDst;
	pszDst = NULL;

	return str;
}

wstring string2wstring(const string &str) {
	LPCSTR pszSrc = str.c_str();
	int nLen = MultiByteToWideChar(CP_ACP, 0, pszSrc, -1, NULL, 0);
	if (nLen == 0)
		return std::wstring(L"");

	wchar_t* pwszDst = new wchar_t[nLen];
	if (!pwszDst)
		return std::wstring(L"");

	MultiByteToWideChar(CP_ACP, 0, pszSrc, -1, pwszDst, nLen);
	std::wstring wstr(pwszDst);
	delete[] pwszDst;
	pwszDst = NULL;

	return wstr;
}

string jstring2string(JNIEnv *env, jstring s) {
	char *c = jstring2windows(env, s);
	return !c ? "" : c;
}

jstring string2jstring(JNIEnv *env, string s) {
	return windows2jstring(env, s.c_str());
}

bool jboolean2bool(jboolean b) {
	return b == JNI_TRUE;
}

jboolean bool2jboolean(bool b) {
	return b ? JNI_TRUE : JNI_FALSE;
}

void jobject2point(JNIEnv *env, jobject pointObj, POINT *pt) {
	jclass pointObjclass = env->FindClass("com/jiuqi/rpa/lib/Point");
	jfieldID fx = env->GetFieldID(pointObjclass, "x", "I");
	jfieldID fy = env->GetFieldID(pointObjclass, "y", "I");

	pt->x = env->GetIntField(pointObj, fx);
	pt->y = env->GetIntField(pointObj, fy);
}

void point2jobject(JNIEnv *env, POINT pt, jobject pointObj) {
	jclass pointObjclass = env->FindClass("com/jiuqi/rpa/lib/Point");
	jfieldID fx = env->GetFieldID(pointObjclass, "x", "I");
	jfieldID fy = env->GetFieldID(pointObjclass, "y", "I");

	env->SetIntField(pointObj, fx, pt.x);
	env->SetIntField(pointObj, fy, pt.y);
}

void jobject2rect(JNIEnv *env, jobject rectObj, RECT *rect) {
	jclass rectObjclass = env->FindClass("com/jiuqi/rpa/lib/find/UIARect");
	jfieldID fx = env->GetFieldID(rectObjclass, "x", "I");
	jfieldID fy = env->GetFieldID(rectObjclass, "y", "I");
	jfieldID fw = env->GetFieldID(rectObjclass, "w", "I");
	jfieldID fh = env->GetFieldID(rectObjclass, "h", "I");

	rect->left = env->GetIntField(rectObj, fx);
	rect->top = env->GetIntField(rectObj, fy);
	rect->right = rect->left + env->GetIntField(rectObj, fw);
	rect->bottom = rect->top + env->GetIntField(rectObj, fh);
}

void rect2jobject(JNIEnv *env, RECT rect, jobject rectObj) {
	jclass rectObjclass = env->FindClass("com/jiuqi/rpa/lib/find/UIARect");
	jfieldID fx = env->GetFieldID(rectObjclass, "x", "I");
	jfieldID fy = env->GetFieldID(rectObjclass, "y", "I");
	jfieldID fw = env->GetFieldID(rectObjclass, "w", "I");
	jfieldID fh = env->GetFieldID(rectObjclass, "h", "I");

	env->SetIntField(rectObj, fx, rect.left);
	env->SetIntField(rectObj, fy, rect.top);
	env->SetIntField(rectObj, fw, rect.right - rect.left);
	env->SetIntField(rectObj, fh, rect.bottom - rect.top);
}

void jobject2uiarect(JNIEnv *env, jobject uiaRectObj, UIARect *uiaRect) {
	jclass rectObjclass = env->FindClass("com/jiuqi/rpa/lib/find/UIARect");
	jfieldID fx = env->GetFieldID(rectObjclass, "x", "I");
	jfieldID fy = env->GetFieldID(rectObjclass, "y", "I");
	jfieldID fw = env->GetFieldID(rectObjclass, "w", "I");
	jfieldID fh = env->GetFieldID(rectObjclass, "h", "I");
	jfieldID fIsBrowserClient = env->GetFieldID(rectObjclass, "isBrowserClient", "Z");

	uiaRect->left = env->GetIntField(uiaRectObj, fx);
	uiaRect->top = env->GetIntField(uiaRectObj, fy);
	uiaRect->right = uiaRect->left + env->GetIntField(uiaRectObj, fw);
	uiaRect->bottom = uiaRect->top + env->GetIntField(uiaRectObj, fh);
	uiaRect->isBrowserClient = env->GetBooleanField(uiaRectObj, fIsBrowserClient);
}

void uiarect2jobject(JNIEnv *env, UIARect uiaRect, jobject rectObj) {
	jclass rectObjclass = env->FindClass("com/jiuqi/rpa/lib/find/UIARect");
	jfieldID fx = env->GetFieldID(rectObjclass, "x", "I");
	jfieldID fy = env->GetFieldID(rectObjclass, "y", "I");
	jfieldID fw = env->GetFieldID(rectObjclass, "w", "I");
	jfieldID fh = env->GetFieldID(rectObjclass, "h", "I");
	jfieldID fIsBrowserClient = env->GetFieldID(rectObjclass, "isBrowserClient", "Z");

	env->SetIntField(rectObj, fx, uiaRect.left);
	env->SetIntField(rectObj, fy, uiaRect.top);
	env->SetIntField(rectObj, fw, uiaRect.right - uiaRect.left);
	env->SetIntField(rectObj, fh, uiaRect.bottom - uiaRect.top);
	env->SetBooleanField(rectObj, fIsBrowserClient, uiaRect.isBrowserClient);
}

void jobject2browserInfo(JNIEnv *env, jobject browserInfoObj, BrowserInfo *browserInfo) {
	jclass rectObjclass = env->FindClass("com/jiuqi/rpa/lib/find/BrowserInfo");
	jfieldID fApplication = env->GetFieldID(rectObjclass, "application", "Ljava/lang/String;");
	jfieldID fTitle = env->GetFieldID(rectObjclass, "title", "Ljava/lang/String;");
	jfieldID fUrl = env->GetFieldID(rectObjclass, "url", "Ljava/lang/String;");

	jstring jApplication = (jstring)env->GetObjectField(browserInfoObj, fApplication);
	jstring jTitle = (jstring)env->GetObjectField(browserInfoObj, fTitle);
	jstring jUrl = (jstring)env->GetObjectField(browserInfoObj, fUrl);

	browserInfo->application = jstring2string(env, jApplication);
	browserInfo->title = jstring2string(env, jTitle);
	browserInfo->url = jstring2string(env, jUrl);
}

void browserinfo2jobject(JNIEnv *env, BrowserInfo browserInfo, jobject browserInfoObj) {
	jclass rectObjclass = env->FindClass("com/jiuqi/rpa/lib/find/BrowserInfo");
	jfieldID fApplication = env->GetFieldID(rectObjclass, "appication", "Ljava/lang/String;");
	jfieldID fTitle = env->GetFieldID(rectObjclass, "title", "Ljava/lang/String;");
	jfieldID fUrl = env->GetFieldID(rectObjclass, "url", "Ljava/lang/String;");

	env->SetObjectField(browserInfoObj, fApplication, string2jstring(env, browserInfo.application));
	env->SetObjectField(browserInfoObj, fTitle, string2jstring(env, browserInfo.title));
	env->SetObjectField(browserInfoObj, fUrl, string2jstring(env, browserInfo.url));
}

COLORREF string2color(string color) {
	const char *c = color.data();

	DWORD r, g, b;
	sscanf_s(c, "#%2X%2X%2X", &r, &g, &b);
	return RGB(r, g, b);
}

string POINT2string(POINT pt) {
	char buf[64];
	sprintf_s(buf, 64, "point(%i,%i)", pt.x, pt.y);
	return buf;
}

string RECT2string(RECT rect) {
	char buf[64];
	int x = rect.left;
	int y = rect.top;
	int w = rect.right - rect.left;
	int h = rect.bottom - rect.top;
	sprintf_s(buf, 64, "rect(%i,%i,%i,%i)", x, y, w, h);
	return buf;
}

jstring path2jstring(JNIEnv *env, Path path) {
	Json::Value elementsJson;
	for (int i = 0; i < path.elements.size(); i++) {
		PathElement *pathElement = path.elements[i];

		Json::Value attributesJson;
		for (int j = 0; j < pathElement->attributes.size(); j++) {
			PathAttribute *pathAttribute = pathElement->attributes[j];

			Json::Value attributeJson;
			attributeJson["name"] = pathAttribute->name;
			attributeJson["value"] = pathAttribute->value;
			attributeJson["enable"] = pathAttribute->enable;
			attributesJson.append(attributeJson);
		}

		Json::Value elementJson;
		elementJson["attributes"] = attributesJson;
		elementJson["runtimeId"] = pathElement->runtimeId;
		elementJson["enable"] = pathElement->enable;
		elementsJson.append(elementJson);
	}

	Json::Value root;
	root["elements"] = elementsJson;
	return string2jstring(env, root.toStyledString());
}

void jstring2path(JNIEnv *env, jstring pathJsonStr, Path *path) {
	string str = jstring2string(env, pathJsonStr);

	Json::CharReaderBuilder b;
	Json::CharReader *reader(b.newCharReader());
	Json::Value root;
	JSONCPP_STRING errs;
	if (!reader->parse(str.c_str(), str.c_str() + str.length(), &root, &errs))
		throw string("解析pathJsonStr出现异常");

	path->elements.clear();

	Json::Value elementsJson = root["elements"];
	for (unsigned int i = 0; i < elementsJson.size(); i++) {
		PathElement *pathElement = new PathElement();

		Json::Value elementJson = elementsJson[i];
		Json::Value attributesJson = elementJson["attributes"];
		for (unsigned int j = 0; j < attributesJson.size(); j++) {
			PathAttribute *pathAttribute = new PathAttribute();

			Json::Value attributeJson = attributesJson[j];
			pathAttribute->name = attributeJson["name"].asString();
			pathAttribute->value = attributeJson["value"].asString();
			pathAttribute->enable = attributeJson["enable"].asBool();
			pathElement->attributes.insert(end(pathElement->attributes), pathAttribute);
		}
		path->elements.insert(end(path->elements), pathElement);
	}

	delete reader;
}

void jintArray2intVector(JNIEnv *env, jintArray jintAry, vector<INT> *vector) {
	jsize size = env->GetArrayLength(jintAry);
	jint *array = env->GetIntArrayElements(jintAry, FALSE);
	for (int i = 0; i < size; i++)
		vector->insert(vector->end(), array[i]);
}

jlongArray longVector2jlongArray(JNIEnv *env, vector<LONG64> vector) {
	int size = static_cast<int>(vector.size());
	jlongArray array = env->NewLongArray(size);
	for (int i = 0; i < vector.size(); i++) {
		jlong jl = vector[i];
		env->SetLongArrayRegion(array, i, 1, &jl);
	}
	return array;
}

void jlongArray2longVector(JNIEnv *env, jlongArray array, vector<LONG64> *vector) {
	jsize size = env->GetArrayLength(array);
	jlong *arr = env->GetLongArrayElements(array, FALSE);
	for (int i = 0; i < size; i++) {
		LONG64 id = arr[i];
		vector->insert(vector->end(), id);
	}
}

void jobjectArray2stringVector(JNIEnv *env, jobjectArray array, vector<string> *vector) {
	jsize size = env->GetArrayLength(array);
	for (int i = 0; i < size; i++) {
		jstring jstr = (jstring)env->GetObjectArrayElement(array, i);
		string str = jstring2string(env, jstr);
		vector->insert(vector->end(), str);
	}
}

jstring properties2jstring(JNIEnv *env, map<string, string> properties) {
	Json::Value root;

	map<string, string>::iterator iter;
	for (iter = properties.begin(); iter != properties.end(); iter++) {
		string key = iter->first;
		string value = iter->second;
		root[key] = value;
	}
	return string2jstring(env, root.toStyledString());
}

string processid2application(DWORD processid) {
	HANDLE hProcess = OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_READ, FALSE, processid);
	LPWSTR lpFilename = new WCHAR[MAX_PATH];
	GetModuleBaseName(hProcess, NULL, lpFilename, MAX_PATH);
	CloseHandle(hProcess);

	return wstring2string(lpFilename);
}

void application2processid(string application, vector<DWORD> *vector) {
	HANDLE hSnapShot = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);
	PROCESSENTRY32 pe;
	pe.dwSize = sizeof(PROCESSENTRY32);
	if (!Process32First(hSnapShot, &pe))
		return;

	wstring applicationName = string2wstring(application);
	transform(applicationName.begin(), applicationName.end(), applicationName.begin(), tolower);

	while (Process32Next(hSnapShot, &pe)) {
		wstring scTmp = pe.szExeFile;
		transform(scTmp.begin(), scTmp.end(), scTmp.begin(), tolower);

		if (!scTmp.compare(applicationName))
			vector->insert(vector->end(), pe.th32ProcessID);
	}
}

void throwNew(JNIEnv *env, string s) {
	jclass exceptionCls = env->FindClass("com/jiuqi/rpa/uiadll/JQUIAException");
	if (exceptionCls == NULL)
		return;

	const char *chr = env->GetStringUTFChars(windows2jstring(env, s.data()), NULL);
	env->ThrowNew(exceptionCls, chr);
	env->DeleteLocalRef(exceptionCls);
}