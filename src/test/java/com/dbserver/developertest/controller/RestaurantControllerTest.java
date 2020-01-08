package com.dbserver.developertest.controller;

import com.dbserver.developertest.dto.HungryProfessionalDTO;
import com.dbserver.developertest.dto.RestaurantDTO;
import com.dbserver.developertest.exception.ExistingHungryProfessionalException;
import com.dbserver.developertest.exception.ExistingRestaurantException;
import com.dbserver.developertest.model.Restaurant;
import com.dbserver.developertest.service.HungryProfessionalService;
import com.dbserver.developertest.service.RestaurantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RestaurantControllerTest {

    @MockBean
    private RestaurantService restaurantService;

    @Autowired
    protected MockMvc mockMvc;

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        this.mapper = new ObjectMapper();
    }

    @Test
    public void whenCreateRestaurantItShouldReturnSuccessMessage() throws Exception {
        RestaurantDTO restaurantDTO = new RestaurantDTO("restaurant");

        JSONObject expected = new JSONObject();
        expected.put("message", "Restaurant Created");

        when(restaurantService.createRestaurant(restaurantDTO)).thenReturn(any(Restaurant.class));

        mockMvc.perform(post("/v1/restaurant")
                .content(mapper.writeValueAsString(restaurantDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(String.valueOf(expected)));
    }

    @Test
    public void whenCreateRestaurantWithAExistingNameItShouldReturnErrorMessage() throws Exception {
        RestaurantDTO restaurantDTO = new RestaurantDTO("restaurant");

        when(restaurantService.createRestaurant(restaurantDTO))
                .thenThrow(new ExistingRestaurantException("Restaurant already exists."));

        JSONObject expected = new JSONObject();
        expected.put("status", 400);
        expected.put("message", "Restaurant already exists.");

        mockMvc.perform(post("/v1/restaurant")
                .content(mapper.writeValueAsString(restaurantDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(String.valueOf(expected)));

    }
}
