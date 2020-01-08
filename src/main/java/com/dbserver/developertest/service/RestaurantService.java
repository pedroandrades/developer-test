package com.dbserver.developertest.service;

import com.dbserver.developertest.dto.RestaurantDTO;
import com.dbserver.developertest.exception.ExistingRestaurantException;
import com.dbserver.developertest.model.Restaurant;
import com.dbserver.developertest.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RestaurantService {

    private RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant createRestaurant(RestaurantDTO restaurantDTO){
       if(restaurantRepository.findAll().stream().map(Restaurant::getName).noneMatch(restaurantDTO.getName()::equals)){
          return restaurantRepository.save(Restaurant.builder().name(restaurantDTO.getName()).build());
       }
       throw new ExistingRestaurantException("Restaurant already exists.");
    }

    public Restaurant winnerRestaurant(String winner){
        Restaurant restaurant = restaurantRepository.findByNameEquals(winner).get();
        restaurant.setChosenDate(LocalDate.now());

        return restaurantRepository.save(restaurant);
    }
}
