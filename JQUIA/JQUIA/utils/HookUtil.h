#pragma once
class HookUtil
{
private:
	bool msgLooping;
	HHOOK _mouseHook, _keyboardHook;
	long preMouseTimeStamp = 0;
	long preKeyTimeStamp = 0;
public:
	HookUtil();
	~HookUtil();

public:
	void MsgLoop();
	void StartHook(HOOKPROC mouseProc, HOOKPROC keyProc);
	void StopHook();
};
