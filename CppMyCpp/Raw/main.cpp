#include <boost/optional/optional.hpp>

#include <chrono>
#include <iostream>

int main() {

    auto start = std::chrono::system_clock::now();

    auto letsSee = [](int x, int y) {
    	boost::optional<std::pair<int, int>> coords;
    	if (x < 3 && y < 3)
    		coords = std::make_pair(x, y);
    	return coords;
    };
    std::cout << "1 " << (*letsSee(1, 2)).first << ", " << (*letsSee(1, 2)).second << std::endl;
    std::cout << "2 " << letsSee(4, 5) << std::endl;

    auto end = std::chrono::system_clock::now();

    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);
    
    std::cout << "t2 = " << duration.count() << std::endl;
}
