#pragma once
#include <string>
#include <vector>
using namespace std;

class PathAttribute {
public:
	string name;
	string value;
	bool enable;
};

class PathElement {
public:
	string runtimeId;
	vector<PathAttribute *> attributes;
	bool enable;
};

class Path {
public:
	vector<PathElement *> elements;
};