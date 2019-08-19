#pragma once
#include <string>
using namespace std;

bool StartsWith(string str, string prefix);
bool EndsWith(string str, string suffix);

int StringMatch(string pattern, string string);
int StringMatch(const char *pattern, const char *string);
int StringMatch(const char *pattern, const char *string, int nocase);