#include "SogoBoard.h"

#include <vector>
#include <iostream>
#include <chrono>
#include <fstream>
#include <string>
#include <thread>


const std::string INPUT_FILE_NAME = "in.txt";


int main() {
    auto start = std::chrono::high_resolution_clock::now();

    try {
    	SogoBoard board;
		std::ifstream in(INPUT_FILE_NAME);
			
		// Throw exceptions in case any i/o operation fails.
		in.exceptions(std::ifstream::failbit | std::ifstream::badbit);

		in >> board;
		std::cout << board;

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
