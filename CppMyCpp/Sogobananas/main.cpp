// #include "Sogo.h"

#include <vector>
#include <iostream>
#include <chrono>
#include <fstream>
#include <string>
#include <thread>


const std::string INPUT_FILE_NAME = "in.txt";

std::vector<std::vector<int>> buildInput(std::string fileName) {
	auto rows = 0, cols = 0;
	auto objectsCount = 0;

	std::ifstream in(fileName);
	
	// Throw exceptions in case any i/o operation fails.
	in.exceptions(std::ifstream::failbit | std::ifstream::badbit);

	in >> rows >> cols >> objectsCount;
	std::vector<std::vector<int>> input(rows, std::vector<int>(cols, 0));
	
	for(int i = 0; i < objectsCount; ++i) {
		auto type = 0, row = 0, col = 0;
		in >> type >> row >> col;
		input[row][col] = type;
	}

	return input;
}


void print(std::vector<std::vector<int>>& output) {
	for(auto l: output) {
		for(auto c: l)
			std::cout << c << ' ';
		std::cout << std::endl;
	}
}


int main() {
    auto start = std::chrono::high_resolution_clock::now();

    try {
    	std::vector<std::vector<int>> input = buildInput(INPUT_FILE_NAME);
    	print(input);
    	// Sogo sogo(input);
    	// sogo.run();
    }
	catch (const std::ifstream::failure& e) {
		std::cout << "Exception opening / reading / closing the file " << INPUT_FILE_NAME << ".\n";
	}
	catch (const std::exception& e) {
		std::cout << e.what() << std::endl;
	}

	auto end = std::chrono::high_resolution_clock::now();
	std::chrono::duration<double, std::milli> elapsed = end - start;

    std::cout << "Time: " << elapsed.count() << " ms\n";

    return 0;
}
