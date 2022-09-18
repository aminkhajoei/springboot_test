package com.example.demoTest;

import com.example.demoTest.controller.VehicleController;
import com.example.demoTest.dto.Vehicle;
import com.example.demoTest.service.VehicleServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehicleController.class)
class DemoTestApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private VehicleServiceImpl vehicleService;

	private static Vehicle vehicle1;
	private static Vehicle vehicle2;
	private static Vehicle vehicle3;

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
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].vin", is(vehicle3.getVin())))
				.andExpect(jsonPath("$[1].vin", is(vehicle2.getVin())))
				.andExpect(jsonPath("$[2].vin", is(vehicle1.getVin())));

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
