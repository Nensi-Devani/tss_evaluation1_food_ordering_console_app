package com.foodorder.service;

import java.sql.Connection;
import java.util.List;

import com.foodorder.database.DatabaseConnection;
import com.foodorder.enums.PaymentStatus;
import com.foodorder.model.Payment;
import com.foodorder.repository.PaymentRepository;

public class PaymentService {
    private final PaymentRepository paymentRepository = new PaymentRepository();

    public Payment createPayment(Payment payment) {
        paymentRepository.save(payment);
        return payment;
    }

    public Payment getPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    public void updatePaymentStatus(String paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId);

        if (payment == null) {
            throw new RuntimeException("Payment not found");
        }

        payment.setPaymentStatus(status);

        paymentRepository.update(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}