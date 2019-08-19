#pragma once
#include <string>
#include <vector>
using namespace std;

class UIADialogLibrary {
public:
	string InputDialog(string title, string label, vector<string> items, bool isPassword);
	string MessageDialog(string title, string message, LONG64 btnSuite);
	string FileDialog(string initialDir, string fiters);
	string DirectoryDialog(string initialDir);
};
