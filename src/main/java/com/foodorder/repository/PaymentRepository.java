package com.foodorder.repository;

import java.util.List;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.IdConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.exception.PaymentFailedException;
import com.foodorder.model.Payment;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

public class PaymentRepository {

    public void save(Payment payment) {
        List<Payment> payments = FileUtil.readData(FileConstants.PAYMENTS_FILE);

        payment.setId(IdGenerator.generateId(
                FileConstants.PAYMENTS_FILE,
                IdConstants.PAYMENT_ID_PREFIX));

        payments.add(payment);

        FileUtil.writeData(FileConstants.PAYMENTS_FILE, payments);
    }

    public void update(Payment payment) {
        List<Payment> payments = FileUtil.readData(FileConstants.PAYMENTS_FILE);

        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getId().equals(payment.getId())) {
                payments.set(i, payment);

                FileUtil.writeData(FileConstants.PAYMENTS_FILE, payments);
                return;
            }
        }

        throw new PaymentFailedException(MessageConstants.PAYMENT_FAILED);
    }

    public Payment findById(String id) {
        List<Payment> payments = FileUtil.readData(FileConstants.PAYMENTS_FILE);

        for (Payment payment : payments) {
            if (payment.getId().equals(id))
                return payment;
        }

        throw new PaymentFailedException(MessageConstants.PAYMENT_FAILED);
    }

    public Payment findByOrderId(String orderId) {
        List<Payment> payments = FileUtil.readData(FileConstants.PAYMENTS_FILE);

        for (Payment payment : payments) {
            if (payment.getOrderId().equals(orderId))
                return payment;
        }

        return null;
    }

    public List<Payment> findAll() {
        return FileUtil.readData(FileConstants.PAYMENTS_FILE);
    }

}
