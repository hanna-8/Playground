#include "Sogo.h"

#include <iostream>
#include <chrono>
#include <fstream>
#include <string>
#include <thread>

const std::string INPUT_FILE_NAME = "in.txt";

std::vector<std::vector<char>> getInput(std::string fileName) {
	std::ifstream in(fileName);
	in.exceptions(std::ifstream::failbit | std::ifstream::badbit);

}

int main() {
    auto start = std::chrono::high_resolution_clock::now();

    try {
    	std::vector<std::vector<char>> input = getInput(INPUT_FILE_NAME);
    	Sogo sogo(input);
    	sogo.run();
    }
    catch (std::ios_base::failure &fail) {
		std::cout << "Could not open/read/close the file " << INPUT_FILE_NAME << std::endl;
	}
	catch (const std::exception& e) {

	}

    // using namespace std::chrono_literals;
    // std::this_thread::sleep_for(1s);

	auto end = std::chrono::high_resolution_clock::now();
	std::chrono::duration<double, std::milli> elapsed = end - start;

    std::cout << "Time: " << elapsed.count() << " ms\n";

    return 0;
}
