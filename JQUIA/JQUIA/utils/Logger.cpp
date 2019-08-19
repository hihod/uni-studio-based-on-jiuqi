#include "stdafx.h"
#include "Logger.h"
#include <iostream>

void UIALog(string msg, int level) {
	UIALog(msg.data(), level);
}

void UIALog(const char *msg, int level) {
	if (level < LOG_FILTER)
		return;

	const char *levelStr = "NONE";
	switch (level)
	{
	case LV_ERROR: {
		levelStr = "ERROR";
		break;
	}
	case LV_WARN: {
		levelStr = "WARN";
		break;
	}
	case LV_INFO: {
		levelStr = "INFO";
		break;
	}
	case LV_DEBUG: {
		levelStr = "DEBUG";
		break;
	}
	}
	cout << "[" << levelStr << "] " << msg << endl;
}