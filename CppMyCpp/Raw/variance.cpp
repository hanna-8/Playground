#include <memory>
#include <algorithm>

#include <stdlib.h>
#include <chrono>
#include <stdio.h>



class Vehicle {};
class Car: public Vehicle {};

class Metal{};
class Iron: public Metal {};

class VehicleFactory {
    public: 
        virtual Vehicle* create(Metal*) const { return new Vehicle(); }
        virtual ~VehicleFactory() {}
};

class CarFactory: public VehicleFactory {
    public:
        virtual Car* create(Metal*) const override { return new Car(); }
        virtual ~CarFactory() {}
};

int main() {
    
    auto start = std::chrono::system_clock::now();

    std::shared_ptr<Vehicle> vehicle;
    std::shared_ptr<Car> car;

    vehicle = car;
    //car = vehicle;


      
    
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(std::chrono::system_clock::now() - start);

    printf("Time: %d ms\n", duration.count());

    return 0;
}
