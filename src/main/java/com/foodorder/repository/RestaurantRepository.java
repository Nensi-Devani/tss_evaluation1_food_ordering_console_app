package com.foodorder.repository;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.database.DatabaseConnection;
import com.foodorder.enums.Status;
import com.foodorder.exception.RestaurantNotFoundException;
import com.foodorder.model.Restaurant;
import com.foodorder.util.FileUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RestaurantRepository {
    Connection connection;
    PreparedStatement preparedStatement;

    public RestaurantRepository(){
        connection = DatabaseConnection
                        .getInstance()
                        .getConnection();
    }

    public void save(Restaurant restaurant) {
        String query = "INSERT INTO restaurants (owner_id, name, status, mobile_no, city) VALUES (?, ?, ?, ?, ?)";

        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(restaurant.getOwnerId()));
            preparedStatement.setString(2, restaurant.getName());
            preparedStatement.setString(3,restaurant.getStatus().name());
            preparedStatement.setString(4, restaurant.getMobileNumber());
            preparedStatement.setString(5, restaurant.getCity());

            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
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
