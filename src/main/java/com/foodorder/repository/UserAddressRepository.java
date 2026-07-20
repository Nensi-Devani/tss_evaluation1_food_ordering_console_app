package com.foodorder.repository;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.IdConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.database.DatabaseConnection;
import com.foodorder.enums.Status;
import com.foodorder.exception.UserAddressNotFoundException;
import com.foodorder.model.UserAddress;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserAddressRepository {
    Connection connection;
    PreparedStatement preparedStatement;

    public UserAddressRepository(){
        connection = DatabaseConnection
                        .getInstance()
                        .getConnection();
    }

    public void save(UserAddress userAddress) {
        String query = "INSERT INTO user_addresses (user_id, mobile, address, city, pincode) VALUES (?, ?, ?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, Long.parseLong(userAddress.getUserId()));
            preparedStatement.setString(2, userAddress.getMobileNumber());
            preparedStatement.setString(3, userAddress.getAddress());
            preparedStatement.setString(4, userAddress.getCity());
            preparedStatement.setString(5, userAddress.getPincode());

            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void update(UserAddress userAddress) {
        List<UserAddress> userAddresses = FileUtil.readData(FileConstants.USER_ADDRESSES_FILE);

        for (int i = 0; i < userAddresses.size(); i++) {
            if (userAddresses.get(i).getId().equals(userAddress.getId())) {
                userAddresses.set(i, userAddress);

                FileUtil.writeData(FileConstants.USER_ADDRESSES_FILE, userAddresses);
                return;
            }
        }

        throw new UserAddressNotFoundException(MessageConstants.USER_ADDRESS_NOT_FOUND);
    }

    public UserAddress findById(String id) {
        List<UserAddress> userAddresses = FileUtil.readData(FileConstants.USER_ADDRESSES_FILE);

        for (UserAddress userAddress : userAddresses) {
            if (userAddress.getId().equals(id))
                return userAddress;
        }

        throw new UserAddressNotFoundException(MessageConstants.USER_ADDRESS_NOT_FOUND);
    }

    public List<UserAddress> findByUserId(String userId) {
        List<UserAddress> addresses = new ArrayList<>();

        String query = "SELECT * FROM user_addresses WHERE user_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, Long.parseLong(userId));
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                UserAddress address = new UserAddress();

                address.setId(String.valueOf(resultSet.getLong("address_id")));
                address.setUserId(String.valueOf(resultSet.getLong("user_id")));
                address.setMobileNumber(resultSet.getString("mobile"));
                address.setAddress(resultSet.getString("address"));
                address.setCity(resultSet.getString("city"));
                address.setPincode(resultSet.getString("pincode"));

                addresses.add(address);
            }
        } catch(SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return addresses;
    }

    public List<UserAddress> findAll() {
        return FileUtil.readData(FileConstants.USER_ADDRESSES_FILE);
    }
}
