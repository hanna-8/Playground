#include <memory>
#include <optional>

#include <stdlib.h>
#include <chrono>
#include <stdio.h>


std::optional<int> tryParseInt(std::string s) {
    std::optional<int> op;

    try {        
        int i = std::stoi(s);
        op = i;
    }
    catch (std::exception) { }
}


int main() {
    
    auto start = std::chrono::system_clock::now();

    optional<int> n = tryParseInt("3");
    printf("%d => %d", n.has_value, *n);

    n = tryParseInt("a");
    printf("%d => %d", n.has_value, *n);

    
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(std::chrono::system_clock::now() - start);

    printf("Time: %d ms\n", duration.count());

    return 0;
}
