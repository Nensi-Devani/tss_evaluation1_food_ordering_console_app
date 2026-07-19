package com.foodorder.repository;

import com.foodorder.constants.MessageConstants;
import com.foodorder.database.DatabaseConnection;
import com.foodorder.enums.Role;
import com.foodorder.enums.Status;
import com.foodorder.exception.UserNotFoundException;
import com.foodorder.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    Connection connection;
    PreparedStatement preparedStatement;

    public UserRepository(){
        connection = DatabaseConnection
                        .getInstance()
                        .getConnection();
    }

    public void save(User user) {
       String query = "INSERT INTO users (name, email, password, role, status) VALUES (?, ?, ?, ?, ?)";

       try{
           preparedStatement = connection.prepareStatement(query,  Statement.RETURN_GENERATED_KEYS);
           preparedStatement.setString(1, user.getName());
           preparedStatement.setString(2, user.getEmail());
           preparedStatement.setString(3, user.getPassword());
           preparedStatement.setString(4,user.getRole().name());
           preparedStatement.setString(5,user.getStatus().name());

           preparedStatement.executeUpdate();

           ResultSet resultSet = preparedStatement.getGeneratedKeys();
           if (resultSet.next()) {
               user.setId(String.valueOf(resultSet.getInt(1))); // Store generated ID in the User object
           }
       }catch (SQLException e){
           throw new RuntimeException(e.getMessage());
       }
    }

    public void update(User user) {
        String query = "UPDATE users SET name = ?, email = ? WHERE user_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setLong(3, Integer.parseInt(user.getId()));

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public User findById(String id) {
        String query = "SELECT * FROM users WHERE id = ?";

        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(id));

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User();

                user.setId(String.valueOf(resultSet.getInt("user_id")));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(Role.valueOf(resultSet.getString("role")));
                user.setStatus(Status.valueOf(resultSet.getString("status")));

                return user;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }


        throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);
    }

    public User findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                User user = new User();
                user.setId(resultSet.getString("user_id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(Role.valueOf(resultSet.getString("role")));
                user.setStatus(Status.valueOf(resultSet.getString("status")));

                return user;
            }

        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        String query = "SELECT * FROM users";

        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();

                user.setId(String.valueOf(resultSet.getInt("user_id")));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(Role.valueOf(resultSet.getString("role")));
                user.setStatus(Status.valueOf(resultSet.getString("status")));

                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return users;
    }

    public List<User> findAllActive() {
        List<User> activeUsers = new ArrayList<>();

        String query = "SELECT * FROM users WHERE status = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, Status.ACTIVE.name());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();

                user.setId(String.valueOf(resultSet.getInt("user_id")));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(Role.valueOf(resultSet.getString("role")));
                user.setStatus(Status.valueOf(resultSet.getString("status")));

                activeUsers.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return activeUsers;
    }

    public List<User> findAllInactive() {
        List<User> inactiveUsers = new ArrayList<>();

        String query = "SELECT * FROM users WHERE status = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, Status.INACTIVE.name());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();

                user.setId(String.valueOf(resultSet.getInt("user_id")));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(Role.valueOf(resultSet.getString("role")));
                user.setStatus(Status.valueOf(resultSet.getString("status")));

                inactiveUsers.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return inactiveUsers;
    }

    public List<User> findAllDeliveryBoys() {
        List<User> deliveryBoys = new ArrayList<>();

        String query = "SELECT * FROM users WHERE role = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, Role.DELIVERY_BOY.name());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();

                user.setId(String.valueOf(resultSet.getLong("user_id")));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(Role.valueOf(resultSet.getString("role")));
                user.setStatus(Status.valueOf(resultSet.getString("status")));

                deliveryBoys.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return deliveryBoys;
    }
}
