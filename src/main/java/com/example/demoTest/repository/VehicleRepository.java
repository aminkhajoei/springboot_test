package com.example.demoTest.repository;

import com.example.demoTest.dto.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {
}
