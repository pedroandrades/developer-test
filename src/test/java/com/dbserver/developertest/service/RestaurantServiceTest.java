package com.dbserver.developertest.service;

import com.dbserver.developertest.dto.RestaurantDTO;
import com.dbserver.developertest.exception.ExistingRestaurantException;
import com.dbserver.developertest.model.Restaurant;
import com.dbserver.developertest.repository.RestaurantRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    private RestaurantService restaurantService;

    @Before
    public void setUp(){
        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(Restaurant.builder().name("oldRestaurant").build());
        restaurantService = new RestaurantService(restaurantRepository);
        when(restaurantRepository.findAll()).thenReturn(restaurantList);
    }

    @Test
    public void whenCreateRestaurantItShouldReturnRestaurant(){
        RestaurantDTO restaurantDTO = new RestaurantDTO("restaurant");
        Restaurant restaurantExpected = Restaurant.builder().name("restaurant").build();

        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurantExpected);

        Restaurant restaurantCreated = restaurantService.createRestaurant(restaurantDTO);

        assertThat(restaurantCreated).isEqualToComparingFieldByField(restaurantExpected);
    }

    @Test
    public void whenCheckWinnerItShouldReturnWinnerRestaurant(){
        String winner = "restaurant";
        Optional<Restaurant> restaurantExpected = Optional.ofNullable(Restaurant.builder().id(1L).name("restaurant").build());

        when(restaurantRepository.findByNameEquals(any(String.class))).thenReturn(restaurantExpected);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurantExpected.get());

        Restaurant winnerRestaurant = restaurantService.winnerRestaurant(winner);

        assertThat(winnerRestaurant).isEqualToComparingFieldByField(restaurantExpected.get());
    }

    @Test(expected = ExistingRestaurantException.class)
    public void whenCreateRestaurantWithAExistingNameItShouldReturnException(){
        RestaurantDTO restaurantDTO = new RestaurantDTO("oldRestaurant");

        restaurantService.createRestaurant(restaurantDTO);

    }
}
