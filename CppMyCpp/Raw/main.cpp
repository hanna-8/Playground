#include <chrono>
#include <iostream>

int main() {

    auto start = std::chrono::system_clock::now();


    auto end = std::chrono::system_clock::now();

    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);
    
    std::cout << "t2 = " << duration.count() << std::endl;
}
