package com.foodorder.repository;

import com.foodorder.constants.MessageConstants;
import com.foodorder.database.DatabaseConnection;
import com.foodorder.enums.Status;
import com.foodorder.exception.RestaurantNotFoundException;
import com.foodorder.model.Restaurant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        String query = "UPDATE restaurants SET name = ?, status = ? WHERE restaurant_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, restaurant.getName());
            preparedStatement.setString(2, restaurant.getStatus().name());
            preparedStatement.setInt(3, Integer.parseInt(restaurant.getId()));

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new RestaurantNotFoundException(MessageConstants.RESTAURANT_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Restaurant findById(String id) {
        String query = "SELECT * FROM restaurants WHERE restaurant_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(id));

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Restaurant restaurant = new Restaurant();

                restaurant.setId(String.valueOf(resultSet.getInt("restaurant_id")));
                restaurant.setOwnerId(String.valueOf(resultSet.getInt("owner_id")));
                restaurant.setName(resultSet.getString("name"));
                restaurant.setStatus(Status.valueOf(resultSet.getString("status")));
                restaurant.setMobileNumber(resultSet.getString("mobile_no"));
                restaurant.setCity(resultSet.getString("city"));

                return restaurant;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        throw new RestaurantNotFoundException(MessageConstants.RESTAURANT_NOT_FOUND);
    }

    public Restaurant findByOwnerId(String ownerId) {
        String query = "SELECT * FROM restaurants WHERE owner_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(ownerId));

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Restaurant restaurant = new Restaurant();

                restaurant.setId(String.valueOf(resultSet.getInt("restaurant_id")));
                restaurant.setOwnerId(String.valueOf(resultSet.getInt("owner_id")));
                restaurant.setName(resultSet.getString("name"));
                restaurant.setStatus(Status.valueOf(resultSet.getString("status")));
                restaurant.setMobileNumber(resultSet.getString("mobile_no"));
                restaurant.setCity(resultSet.getString("city"));

                return restaurant;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }

    public List<Restaurant> findAll() {
        List<Restaurant> restaurants = new ArrayList<>();

        String query = "SELECT * FROM restaurants";

        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Restaurant restaurant = new Restaurant();

                restaurant.setId(String.valueOf(resultSet.getInt("restaurant_id")));
                restaurant.setOwnerId(String.valueOf(resultSet.getInt("owner_id")));
                restaurant.setName(resultSet.getString("name"));
                restaurant.setStatus(Status.valueOf(resultSet.getString("status")));
                restaurant.setMobileNumber(resultSet.getString("mobile_no"));
                restaurant.setCity(resultSet.getString("city"));

                restaurants.add(restaurant);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return restaurants;
    }

    public List<Restaurant> findAllActive() {
        List<Restaurant> activeRestaurants = new ArrayList<>();

        String query = "SELECT * FROM restaurants WHERE status = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, Status.ACTIVE.name());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Restaurant restaurant = new Restaurant();

                restaurant.setId(String.valueOf(resultSet.getInt("restaurant_id")));
                restaurant.setOwnerId(String.valueOf(resultSet.getInt("owner_id")));
                restaurant.setName(resultSet.getString("name"));
                restaurant.setStatus(Status.valueOf(resultSet.getString("status")));
                restaurant.setMobileNumber(resultSet.getString("mobile_no"));
                restaurant.setCity(resultSet.getString("city"));

                activeRestaurants.add(restaurant);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return activeRestaurants;
    }

    public List<Restaurant> findAllInactive() {
        List<Restaurant> inactiveRestaurants = new ArrayList<>();

        String query = "SELECT * FROM restaurants WHERE status = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, Status.INACTIVE.name());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Restaurant restaurant = new Restaurant();

                restaurant.setId(String.valueOf(resultSet.getInt("restaurant_id")));
                restaurant.setOwnerId(String.valueOf(resultSet.getInt("owner_id")));
                restaurant.setName(resultSet.getString("name"));
                restaurant.setStatus(Status.valueOf(resultSet.getString("status")));
                restaurant.setMobileNumber(resultSet.getString("mobile_no"));
                restaurant.setCity(resultSet.getString("city"));

                inactiveRestaurants.add(restaurant);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return inactiveRestaurants;
    }
}
