#pragma once
#include <string>
using namespace std;

const int LOG_FILTER = 0;

const int LV_ERROR = 40;
const int LV_WARN = 30;
const int LV_INFO = 20;
const int LV_DEBUG = 10;

void UIALog(string msg, int level);
void UIALog(const char *msg, int level);
