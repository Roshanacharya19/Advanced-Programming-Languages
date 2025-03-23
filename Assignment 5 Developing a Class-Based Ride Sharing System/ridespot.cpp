// assignment_oop.cpp

#include <iostream>
#include <string>
#include <vector>
#include <iomanip>
#include <memory>

// Base Ride Class
class Ride {
protected:
    int rideID;
    std::string pickupLocation;
    std::string dropoffLocation;
    double distance; // in miles
    double baseFare;

public:
    // Constructor
    Ride(int id, std::string pickup, std::string dropoff, double dist) 
        : rideID(id), pickupLocation(pickup), dropoffLocation(dropoff), distance(dist), baseFare(0) {}

    // Virtual destructor for proper cleanup in inheritance
    virtual ~Ride() {}

    // Virtual method for calculating fare - will be overridden by derived classes
    virtual double calculateFare() const {
        return distance * baseFare;
    }

    // Virtual method to display ride details
    virtual void rideDetails() const {
        std::cout << "Ride ID: " << rideID << std::endl;
        std::cout << "Pickup: " << pickupLocation << std::endl;
        std::cout << "Dropoff: " << dropoffLocation << std::endl;
        std::cout << "Distance: " << distance << " miles" << std::endl;
        std::cout << "Fare: $" << std::fixed << std::setprecision(2) << calculateFare() << std::endl;
    }

    // Getters
    int getRideID() const { return rideID; }
    double getDistance() const { return distance; }
};

// Derived class for Standard Rides
class StandardRide : public Ride {
public:
    StandardRide(int id, std::string pickup, std::string dropoff, double dist) 
        : Ride(id, pickup, dropoff, dist) {
        baseFare = 2.50; // $2.50 per mile for standard rides
    }

    void rideDetails() const override {
        std::cout << "=== STANDARD RIDE ===" << std::endl;
        Ride::rideDetails();
    }
};

// Derived class for Premium Rides
class PremiumRide : public Ride {
private:
    bool refreshmentsIncluded;
    
public:
    PremiumRide(int id, std::string pickup, std::string dropoff, double dist, bool refreshments = true) 
        : Ride(id, pickup, dropoff, dist), refreshmentsIncluded(refreshments) {
        baseFare = 3.75; // $3.75 per mile for premium rides
    }

    // Override calculateFare to add premium features cost
    double calculateFare() const override {
        double fare = Ride::calculateFare();
        // Add $5 if refreshments are included
        if (refreshmentsIncluded) {
            fare += 5.0;
        }
        return fare;
    }

    void rideDetails() const override {
        std::cout << "=== PREMIUM RIDE ===" << std::endl;
        Ride::rideDetails();
        std::cout << "Refreshments: " << (refreshmentsIncluded ? "Included" : "Not Included") << std::endl;
    }
};

// Driver Class
class Driver {
private:
    // Encapsulated attributes
    int driverID;
    std::string name;
    double rating;
    std::vector<std::shared_ptr<Ride>> assignedRides; // Encapsulated list of rides

public:
    Driver(int id, std::string driverName, double initialRating = 5.0) 
        : driverID(id), name(driverName), rating(initialRating) {}

    // Method to add a ride to the driver's list
    void addRide(std::shared_ptr<Ride> ride) {
        assignedRides.push_back(ride);
    }

    // Method to display driver information
    void getDriverInfo() const {
        std::cout << "\n=== DRIVER INFORMATION ===" << std::endl;
        std::cout << "Driver ID: " << driverID << std::endl;
        std::cout << "Name: " << name << std::endl;
        std::cout << "Rating: " << rating << " stars" << std::endl;
        std::cout << "Total Rides: " << assignedRides.size() << std::endl;
        std::cout << "Total Earnings: $" << std::fixed << std::setprecision(2) << getTotalEarnings() << std::endl;
    }

    // Method to calculate total earnings from all rides
    double getTotalEarnings() const {
        double total = 0.0;
        for (const auto& ride : assignedRides) {
            total += ride->calculateFare();
        }
        return total;
    }

    // Method to show all assigned rides
    void showAllRides() const {
        std::cout << "\n=== RIDES BY DRIVER " << name << " ===" << std::endl;
        for (const auto& ride : assignedRides) {
            ride->rideDetails();
            std::cout << "------------------------" << std::endl;
        }
    }

    // Getters and setters
    int getDriverID() const { return driverID; }
    std::string getName() const { return name; }
    double getRating() const { return rating; }
    void setRating(double newRating) { rating = newRating; }
};

// Rider Class
class Rider {
private:
    int riderID;
    std::string name;
    std::vector<std::shared_ptr<Ride>> requestedRides;

public:
    Rider(int id, std::string riderName) : riderID(id), name(riderName) {}

    // Method to request a new ride
    void requestRide(std::shared_ptr<Ride> ride) {
        requestedRides.push_back(ride);
        std::cout << "Ride #" << ride->getRideID() << " requested by " << name << std::endl;
    }

    // Method to view ride history
    void viewRides() const {
        std::cout << "\n=== RIDE HISTORY FOR " << name << " ===" << std::endl;
        for (const auto& ride : requestedRides) {
            ride->rideDetails();
            std::cout << "------------------------" << std::endl;
        }
        std::cout << "Total Spent: $" << std::fixed << std::setprecision(2) << getTotalSpent() << std::endl;
    }

    // Method to calculate total spent on all rides
    double getTotalSpent() const {
        double total = 0.0;
        for (const auto& ride : requestedRides) {
            total += ride->calculateFare();
        }
        return total;
    }

    // Getters
    int getRiderID() const { return riderID; }
    std::string getName() const { return name; }
};

int main() {
    // Create riders
    Rider rider1(101, "Alice Smith");
    Rider rider2(102, "Bob Johnson");

    // Create drivers
    Driver driver1(201, "Dave Wilson", 4.8);
    Driver driver2(202, "Emily Brown", 4.9);

    // Create different types of rides (demonstrating polymorphism)
    std::vector<std::shared_ptr<Ride>> allRides;
    
    auto ride1 = std::make_shared<StandardRide>(1001, "123 Main St", "456 Oak Ave", 5.2);
    auto ride2 = std::make_shared<PremiumRide>(1002, "789 Pine Rd", "321 Elm Blvd", 3.7, true);
    auto ride3 = std::make_shared<StandardRide>(1003, "555 Beach Dr", "777 Lake St", 8.1);
    auto ride4 = std::make_shared<PremiumRide>(1004, "999 Hill Rd", "888 Valley Ave", 4.5, false);
    
    allRides.push_back(ride1);
    allRides.push_back(ride2);
    allRides.push_back(ride3);
    allRides.push_back(ride4);

    // Assign rides to riders
    rider1.requestRide(ride1);
    rider1.requestRide(ride2);
    rider2.requestRide(ride3);
    rider2.requestRide(ride4);

    // Assign rides to drivers
    driver1.addRide(ride1);
    driver1.addRide(ride3);
    driver2.addRide(ride2);
    driver2.addRide(ride4);

    // Display all rides using polymorphism
    std::cout << "\n=== ALL RIDES IN SYSTEM ===" << std::endl;
    for (const auto& ride : allRides) {
        ride->rideDetails(); // Polymorphic call to rideDetails()
        std::cout << "------------------------" << std::endl;
    }

    // Display rider information
    rider1.viewRides();
    rider2.viewRides();

    // Display driver information
    driver1.getDriverInfo();
    driver1.showAllRides();
    driver2.getDriverInfo();
    driver2.showAllRides();

    return 0;
}