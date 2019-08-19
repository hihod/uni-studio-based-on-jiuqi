#include "stdafx.h"
#include "StringUtils.h"

bool StartsWith(string str, string prefix) {
	size_t str_size = str.size();
	size_t prefix_size = prefix.size();
	if (str_size < prefix_size)
		return FALSE;

	string tmp_str = str.substr(0, prefix_size);
	return tmp_str == prefix;
}

bool EndsWith(string str, string suffix) {
	size_t str_size = str.size();
	size_t suffix_size = suffix.size();
	if (str_size < suffix_size)
		return FALSE;

	string tmp_str = str.substr(str_size - suffix_size, suffix_size);
	return tmp_str == suffix;
}

int StringMatchLen(const char *pattern, int patternLen, const char *string, int stringLen, int nocase) {
	while (patternLen) {
		switch (pattern[0]) {
		case '*': {
			while (pattern[1] == '*') {
				pattern++;
				patternLen--;
			}

			//Æ¥ÅäÉÏ
			if (patternLen == 1)
				return 1;

			while (stringLen) {
				//Æ¥ÅäÉÏ
				if (StringMatchLen(pattern + 1, patternLen - 1, string, stringLen, nocase))
					return 1;

				string++;
				stringLen--;
			}

			//²»Æ¥Åä
			return 0;
		}
		default:
			if (!nocase) {
				//²»Æ¥Åä
				if (pattern[0] != string[0])
					return 0;
			}
			else {
				//²»Æ¥Åä
				if (tolower((int)pattern[0]) != tolower((int)string[0]))
					return 0;
			}

			string++;
			stringLen--;
			break;
		}

		pattern++;
		patternLen--;

		if (stringLen == 0) {
			while (*pattern == '*') {
				pattern++;
				patternLen--;
			}

			break;
		}
	}
	if (patternLen == 0 && stringLen == 0)
		return 1;

	return 0;
}

int StringMatch(string pattern, string string) {
	return StringMatch(pattern.data(), string.data());
}

int StringMatch(const char *pattern, const char *string) {
	return StringMatch(pattern, string, false);
}

int StringMatch(const char *pattern, const char *string, int nocase) {
	return StringMatchLen(pattern, (int)strlen(pattern), string, (int)strlen(string), nocase);
}