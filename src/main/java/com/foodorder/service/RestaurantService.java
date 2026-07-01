package com.foodorder.service;

import java.util.List;

import com.foodorder.constants.MessageConstants;
import com.foodorder.enums.Status;
import com.foodorder.exception.RestaurantAlreadyExistsException;
import com.foodorder.model.Restaurant;
import com.foodorder.model.User;
import com.foodorder.repository.RestaurantRepository;
import com.foodorder.repository.UserRepository;

public class RestaurantService {
    private final RestaurantRepository restaurantRepository = new RestaurantRepository();
    private final UserRepository userRepository = new UserRepository();

    public void register(Restaurant restaurant) {
        Restaurant existingRestaurant = restaurantRepository.findByOwnerId(restaurant.getOwnerId());

        if (existingRestaurant != null)
            throw new RestaurantAlreadyExistsException(MessageConstants.RESTAURANT_ALREADY_EXISTS);

        restaurant.setStatus(Status.INACTIVE);

        restaurantRepository.save(restaurant);
    }

    public Restaurant getRestaurantById(String id) {
        return restaurantRepository.findById(id);
    }

    public Restaurant getRestaurantByOwnerId(String ownerId) {
        return restaurantRepository.findByOwnerId(ownerId);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> getAllActiveRestaurants() {
        return restaurantRepository.findAllActive();
    }

    public List<Restaurant> getAllInactiveRestaurants() {
        return restaurantRepository.findAllInactive();
    }

    public void update(Restaurant restaurant) {
        restaurantRepository.update(restaurant);
    }

    public void approveRestaurant(String restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        restaurant.setStatus(Status.ACTIVE);
        restaurantRepository.update(restaurant);

        User owner = userRepository.findById(restaurant.getOwnerId());
        owner.setStatus(Status.ACTIVE);
        userRepository.update(owner);
    }

    public void deactivateRestaurant(String restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        restaurant.setStatus(Status.INACTIVE);
        restaurantRepository.update(restaurant);

        User owner = userRepository.findById(restaurant.getOwnerId());
        owner.setStatus(Status.INACTIVE);
        userRepository.update(owner);
    }

}
