package com.foodorder.repository;

import java.sql.*;
import java.util.List;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.IdConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.database.DatabaseConnection;
import com.foodorder.enums.PaymentStatus;
import com.foodorder.enums.PaymentType;
import com.foodorder.exception.PaymentFailedException;
import com.foodorder.model.Payment;
import com.foodorder.service.PaymentService;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

public class PaymentRepository {
    Connection connection;
    PreparedStatement preparedStatement;

    public PaymentRepository(){
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public void save(Payment payment) {
        String query = "INSERT INTO payments (order_id, amount, payment_type, payment_status) VALUES (?, ?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setLong(1, Long.parseLong(payment.getOrderId()));
            preparedStatement.setDouble(2, payment.getAmount());
            preparedStatement.setString(3, payment.getPaymentType().name());
            preparedStatement.setString(4, payment.getPaymentStatus().name());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                payment.setId(String.valueOf(resultSet.getLong(1)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void update(Payment payment) {
        String query = "UPDATE payments SET payment_status=? WHERE payment_id=?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, payment.getPaymentStatus().name());
            preparedStatement.setLong(2, Long.parseLong(payment.getId()));

            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Payment findById(String id) {
        String query = "SELECT * FROM payments WHERE payment_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, Long.parseLong(id));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Payment payment = new Payment();

                payment.setId(String.valueOf(resultSet.getLong("payment_id")));
                payment.setOrderId(String.valueOf(resultSet.getLong("order_id")));
                payment.setAmount(resultSet.getDouble("amount"));
                payment.setPaymentType(PaymentType.valueOf(resultSet.getString("payment_type")));
                payment.setPaymentStatus(PaymentStatus.valueOf(resultSet.getString("payment_status")));

                return payment;
            }
        } catch(SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }

    public Payment findByOrderId(String orderId) {
        String query = "SELECT * FROM payments WHERE order_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, Long.parseLong(orderId));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Payment payment = new Payment();

                payment.setId(String.valueOf(resultSet.getLong("payment_id")));
                payment.setOrderId(String.valueOf(resultSet.getLong("order_id")));
                payment.setAmount(resultSet.getDouble("amount"));
                payment.setPaymentType(PaymentType.valueOf(resultSet.getString("payment_type")));
                payment.setPaymentStatus(PaymentStatus.valueOf(resultSet.getString("payment_status")));

                return payment;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }

    public List<Payment> findAll() {
        return FileUtil.readData(FileConstants.PAYMENTS_FILE);
    }

}
