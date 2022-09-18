package com.example.demoTest.controller;

import com.example.demoTest.dto.Vehicle;
import com.example.demoTest.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/demo")
public class VehicleController {

    @Autowired
    VehicleService vehicleService;

    @GetMapping(value = "/vehicles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Vehicle>> getAllVehicles() {

        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        if (vehicles.isEmpty()) {
            throw new RuntimeException("No vehicle records were found");
        }
        return new ResponseEntity<List<Vehicle>>(vehicles, HttpStatus.OK);
    }

    @GetMapping(value = "/vehicles/{vin}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vehicle> getVechileByVin(@PathVariable(value = "vin") String vin) {

        Optional<Vehicle> vehicleToUpdate = vehicleService.getVehicleByVin(vin);
        if (!vehicleToUpdate.isPresent()) {
            throw new RuntimeException("Vehicle with VIN (" + vin + ") not found!");
        }
        return new ResponseEntity<Vehicle>(vehicleToUpdate.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/create/vehicle", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody Vehicle createVehicle) {

        Optional<Vehicle> vehicle = vehicleService.getVehicleByVin(createVehicle.getVin());
        if (vehicle.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT,
                    "Vehicle with VIN" + "(" + createVehicle.getVin() + ") already exists");
        }
        return new ResponseEntity<Vehicle>(vehicleService.createVehicle(createVehicle), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/vehicle/{vin}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable String vin, @Valid @RequestBody Vehicle vehicle) {

        return new ResponseEntity<Vehicle>(vehicleService.updateVehicle(vin, vehicle), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/vehicles/{vin}")
    public ResponseEntity<Object> deleteVehicle(@PathVariable(value = "vin") String vin) {

        vehicleService.deleteVehicle(vin);
        return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
    }

}