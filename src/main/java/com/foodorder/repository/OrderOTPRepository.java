package com.foodorder.repository;

import java.sql.*;
import java.util.List;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.IdConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.database.DatabaseConnection;
import com.foodorder.exception.OTPNotFoundException;
import com.foodorder.model.OrderOTP;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

import javax.xml.crypto.Data;

public class OrderOTPRepository {
    Connection connection;
    PreparedStatement preparedStatement;

    public OrderOTPRepository(){
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public void save(OrderOTP orderOTP) {
        String query = "INSERT INTO order_otps (order_id, otp, generated_at) VALUES (?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, Long.parseLong(orderOTP.getOrderId()));
            preparedStatement.setString(2, orderOTP.getOtp());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(orderOTP.getGeneratedAt()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void update(OrderOTP orderOTP) {
        List<OrderOTP> orderOTPs = FileUtil.readData(FileConstants.ORDER_OTPS_FILE);

        for (int i = 0; i < orderOTPs.size(); i++) {
            if (orderOTPs.get(i).getId().equals(orderOTP.getId())) {
                orderOTPs.set(i, orderOTP);

                FileUtil.writeData(FileConstants.ORDER_OTPS_FILE, orderOTPs);
                return;
            }
        }

        throw new OTPNotFoundException(MessageConstants.OTP_NOT_FOUND);
    }

    public OrderOTP findById(String id) {
        List<OrderOTP> orderOTPs = FileUtil.readData(FileConstants.ORDER_OTPS_FILE);

        for (OrderOTP orderOTP : orderOTPs) {
            if (orderOTP.getId().equals(id))
                return orderOTP;
        }

        throw new OTPNotFoundException(MessageConstants.OTP_NOT_FOUND);
    }

    public OrderOTP findByOrderId(String orderId) {
        String query = "SELECT * FROM order_otps WHERE order_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, Long.parseLong(orderId));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                OrderOTP orderOTP = new OrderOTP();

                orderOTP.setId(String.valueOf(resultSet.getLong("order_otp_id")));
                orderOTP.setOrderId(String.valueOf(resultSet.getLong("order_id")));
                orderOTP.setOtp(resultSet.getString("otp"));
                orderOTP.setGeneratedAt( resultSet.getTimestamp("generated_at").toLocalDateTime());

                return orderOTP;
            }
        } catch(SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }

    public List<OrderOTP> findAll() {
        return FileUtil.readData(FileConstants.ORDER_OTPS_FILE);
    }

}
