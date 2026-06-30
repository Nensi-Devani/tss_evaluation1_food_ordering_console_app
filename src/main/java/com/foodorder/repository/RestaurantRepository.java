package com.foodorder.repository;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.IdConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.enums.Status;
import com.foodorder.exception.RestaurantNotFoundException;
import com.foodorder.model.Restaurant;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class RestaurantRepository {
    public void save(Restaurant restaurant) {
        List<Restaurant> restaurants = FileUtil.readData(FileConstants.RESTAURANTS_FILE);

        restaurant.setId(IdGenerator.generateId(
                FileConstants.RESTAURANTS_FILE,
                IdConstants.RESTAURANT_ID_PREFIX));

        restaurants.add(restaurant);

        FileUtil.writeData(FileConstants.RESTAURANTS_FILE, restaurants);
    }

    public void update(Restaurant restaurant) {
        List<Restaurant> restaurants = FileUtil.readData(FileConstants.RESTAURANTS_FILE);

        for (int i = 0; i < restaurants.size(); i++) {
            if (restaurants.get(i).getId().equals(restaurant.getId())) {
                restaurants.set(i, restaurant);

                FileUtil.writeData(FileConstants.RESTAURANTS_FILE, restaurants);
                return;
            }
        }

        throw new RestaurantNotFoundException(MessageConstants.RESTAURANT_NOT_FOUND);
    }

    public Restaurant findById(String id) {
        List<Restaurant> restaurants = FileUtil.readData(FileConstants.RESTAURANTS_FILE);

        for (Restaurant restaurant : restaurants) {
            if (restaurant.getId().equals(id))
                return restaurant;
        }

        throw new RestaurantNotFoundException(MessageConstants.RESTAURANT_NOT_FOUND);
    }

    public Restaurant findByOwnerId(String ownerId) {
        List<Restaurant> restaurants = FileUtil.readData(FileConstants.RESTAURANTS_FILE);

        for (Restaurant restaurant : restaurants) {
            if (restaurant.getOwnerId().equals(ownerId))
                return restaurant;
        }

        return null;
    }

    public List<Restaurant> findAll() {
        return FileUtil.readData(FileConstants.RESTAURANTS_FILE);
    }

    public List<Restaurant> findAllActive() {
        List<Restaurant> activeRestaurants = new ArrayList<>();

        List<Restaurant> restaurants = FileUtil.readData(FileConstants.RESTAURANTS_FILE);

        for (Restaurant restaurant : restaurants) {
            if (restaurant.getStatus() == Status.ACTIVE)
                activeRestaurants.add(restaurant);
        }

        return activeRestaurants;
    }

    public List<Restaurant> findAllInactive() {
        List<Restaurant> inactiveRestaurants = new ArrayList<>();

        List<Restaurant> restaurants = FileUtil.readData(FileConstants.RESTAURANTS_FILE);

        for (Restaurant restaurant : restaurants)
            if (restaurant.getStatus() == Status.INACTIVE) {
                inactiveRestaurants.add(restaurant);
        }

        return inactiveRestaurants;
    }
}
