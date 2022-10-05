package com.example.demoTest;

import com.example.demoTest.dto.Vehicle;
import com.example.demoTest.service.VehicleServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class BaseContainer {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VehicleServiceImpl vehicleService;

    private static Vehicle vehicle1;
    private static Vehicle vehicle2;
    private static Vehicle vehicle3;

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer
            (DockerImageName.parse("postgres:latest"))
            .withUsername("admin")
            .withPassword("admin")
            .withDatabaseName("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }



    @BeforeAll
    static void setVehicle(){

        vehicle1 = Vehicle.builder()
                .vin("vin1")
                .year(2000)
                .model("model")
                .is_older(true)
                .make("make")
                .build();

        vehicle2 = Vehicle.builder()
                .vin("vin2")
                .year(2002)
                .model("model2")
                .is_older(true)
                .make("make2")
                .build();

        vehicle3 = Vehicle.builder()
                .vin("vin3")
                .year(2003)
                .model("mode3")
                .is_older(true)
                .make("make3")
                .build();

    }


    @Test
    @DisplayName("getAllVehicles withVehiclesList ReturnOkAndTheList")
    void getAllVehicles_withVehiclesList_ReturnOkAndVehiclesList() throws Exception {

        when(vehicleService.getAllVehicles()).thenReturn(List.of(vehicle3, vehicle2, vehicle1));

        mvc.perform(get("/demo/vehicles").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(vehicleService, times(1)).getAllVehicles();

    }

    @Test
    @DisplayName("getAllVehicles withEmptyList ReturnException")
    void getAllVehicles_withEmptyVehiclesList_ReturnException() throws Exception {

        when(vehicleService.getAllVehicles()).thenReturn(List.of());
        assertThrows(NestedServletException.class, ()->mvc.perform(get("/demo/vehicles")));

    }

    @Test
    void createVehicle_withValidBody_returnOk() throws Exception {

        when(vehicleService.getVehicleByVin(vehicle2.getVin())).thenReturn(Optional.of(vehicle2));

        mvc.perform(post("/demo//create/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicle1)))
                .andExpect(status().isCreated());

    }
}
