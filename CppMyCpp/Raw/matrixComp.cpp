#include <chrono>
#include <vector>
#include <iostream>

class Test1 {
public:
    Test1(std::size_t rows, std::size_t cols) :
        vec(rows)
    {
        for (auto &row: vec) {
            row.assign(cols, 1);
        }
    }
    int fetch(std::size_t x, std::size_t y) const;
private:
    std::vector<std::vector<int>> vec;
};

class Test2 {
public:
    Test2(std::size_t rows, std::size_t cols) :
        height{rows},
        width{cols},
        vec(height * width, 2)
    {}
    int fetch(std::size_t x, std::size_t y) const;
private:
    const size_t height;
    const size_t width;
    std::vector<int> vec;
};

int Test1::fetch(std::size_t x, std::size_t y) const { return vec[x][y]; }
int Test2::fetch(std::size_t x, std::size_t y) const { return vec[x+y*height]; }

int main() {
    std::size_t rows = 42, cols = 1000000;
    Test1 test1(rows, cols);
    Test2 test2(rows, cols);

    int s1 = 0, s2 = 0; // Have to use the fetched values so that they are not optimized out.

    auto start = std::chrono::system_clock::now();
    for(std::size_t i = 0; i < rows; ++i)
        for(std::size_t j = 0; j < cols; ++j)
            s1 += test1.fetch(i, j);
    auto interim = std::chrono::system_clock::now();

    for(std::size_t i = 0; i < rows; ++i)
        for(std::size_t j = 0; j < cols; ++j)
            s2 += test2.fetch(i, j);
    auto end = std::chrono::system_clock::now();

    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(interim - start);
    std::cout << "t1 = " << duration.count() << "; s1 = " << s1 << std::endl;

    duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - interim);
    std::cout << "t2 = " << duration.count() << "; s2 = " << s2 << std::endl;

}
