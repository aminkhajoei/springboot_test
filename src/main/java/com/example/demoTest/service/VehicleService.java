package com.example.demoTest.service;

import com.example.demoTest.dto.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleService {
    List<Vehicle> getAllVehicles();

    Optional<Vehicle> getVehicleByVin(String vin);

    Vehicle createVehicle(Vehicle newVehicle);

    Vehicle updateVehicle(String vin, Vehicle vehicleUpdate);

    void deleteVehicle(String vin);
}
