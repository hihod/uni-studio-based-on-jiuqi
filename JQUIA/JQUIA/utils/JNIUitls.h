#pragma once
#include <jni.h>
#include <map>
#include <string>
#include "Path.h"
using namespace std;

struct UIARect {
	LONG    left;
	LONG    top;
	LONG    right;
	LONG    bottom;
	bool	isBrowserClient;
};

struct BrowserInfo {
	string	application;
	string	title;
	string	url;
};

void throwNew(JNIEnv *env, string s);

string wstring2string(const wstring &ws);
wstring string2wstring(const string &s);

string jstring2string(JNIEnv *env, jstring s);
jstring string2jstring(JNIEnv *env, string s);

bool jboolean2bool(jboolean b);
jboolean bool2jboolean(bool b);

void jobject2point(JNIEnv *env, jobject pointObj, POINT *pt);
void point2jobject(JNIEnv *env, POINT pt, jobject pointObj);

void jobject2rect(JNIEnv *env, jobject rectObj, RECT *rect);
void rect2jobject(JNIEnv *env, RECT rect, jobject rectObj);
void jobject2uiarect(JNIEnv *env, jobject uiaRectObj, UIARect *uiaRect);
void uiarect2jobject(JNIEnv *env, UIARect uiaRect, jobject uiaRectObj);
void jobject2browserInfo(JNIEnv *env, jobject browserInfoObj, BrowserInfo *browserInfo);
void browserinfo2jobject(JNIEnv *env, BrowserInfo browserInfo, jobject browserInfoObj);

COLORREF string2color(string color);

string POINT2string(POINT pt);
string RECT2string(RECT pt);

jstring path2jstring(JNIEnv *env, Path path);
void jstring2path(JNIEnv *env, jstring pathJsonStr, Path *path);

void jintArray2intVector(JNIEnv *env, jintArray jintAry, vector<INT> *vector);

jlongArray longVector2jlongArray(JNIEnv *env, vector<LONG64> vector);
void jlongArray2longVector(JNIEnv *env, jlongArray array, vector<LONG64> *vector);
void jobjectArray2stringVector(JNIEnv *env, jobjectArray array, vector<string> *vector);

jstring properties2jstring(JNIEnv *env, map<string, string> properties);

string processid2application(DWORD processid);
void application2processid(string application, vector<DWORD> *vector);