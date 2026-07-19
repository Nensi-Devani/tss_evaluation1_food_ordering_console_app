package com.foodorder.repository;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.database.DatabaseConnection;
import com.foodorder.enums.Role;
import com.foodorder.enums.Status;
import com.foodorder.exception.UserNotFoundException;
import com.foodorder.model.User;
import com.foodorder.util.FileUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
           preparedStatement = connection.prepareStatement(query);
           preparedStatement.setString(1, user.getName());
           preparedStatement.setString(2, user.getEmail());
           preparedStatement.setString(3, user.getPassword());
           preparedStatement.setString(4,user.getRole().name());
           preparedStatement.setString(5,user.getStatus().name());

           preparedStatement.executeUpdate();
       }catch (SQLException e){
           throw new RuntimeException(e.getMessage());
       }
    }

    public void update(User user) {
        List<User> users = FileUtil.readData(FileConstants.USERS_FILE);

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);

                FileUtil.writeData(FileConstants.USERS_FILE, users);
                return;
            }
        }

        throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);
    }

    public User findById(String id) {
        List<User> users = FileUtil.readData(FileConstants.USERS_FILE);

        for (User user : users) {
            if (user.getId().equals(id))
                return user;
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
        return FileUtil.readData(FileConstants.USERS_FILE);
    }

    public List<User> findAllActive() {
        List<User> activeUsers = new ArrayList<>();

        List<User> users = FileUtil.readData(FileConstants.USERS_FILE);

        for (User user : users) {
            if (user.getStatus() == Status.ACTIVE)
                activeUsers.add(user);
        }

        return activeUsers;
    }

    public List<User> findAllInactive() {
        List<User> inactiveUsers = new ArrayList<>();

        List<User> users = FileUtil.readData(FileConstants.USERS_FILE);

        for (User user : users) {
            if (user.getStatus() == Status.INACTIVE)
                inactiveUsers.add(user);
        }

        return inactiveUsers;
    }

    public List<User> findAllDeliveryBoys() {
        List<User> users = FileUtil.readData(FileConstants.USERS_FILE);

        List<User> deliveryBoys = new ArrayList<>();

        for (User user : users) {
            if (user.getRole() == Role.DELIVERY_BOY)
                deliveryBoys.add(user);
        }

        return deliveryBoys;
    }
}
