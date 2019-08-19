#pragma once
#include <jni.h>
using namespace std;

class UIAPickerCallback
{
private:
	JNIEnv *env;
	jobject callbackObj;
	jmethodID mouseMove, mouseLeftUp, keyEscapeUp;
public:
	UIAPickerCallback(JNIEnv *env, jobject callbackObj);

	void onMouseMove(POINT pt);
	void onMouseLeftUp(POINT pt);
	void onKeyEscapeUp();
};

class UIAPickerLibrary
{
public:
	void StartPick(JNIEnv *env, jobject callbackObj);
	void EndPick();
};

void OpenPickerDlg(jobject callbackObj);