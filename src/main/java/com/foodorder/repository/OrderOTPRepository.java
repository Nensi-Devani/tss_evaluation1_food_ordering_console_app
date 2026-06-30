package com.foodorder.repository;

import java.util.List;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.IdConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.exception.OTPNotFoundException;
import com.foodorder.model.OrderOTP;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

public class OrderOTPRepository {

    public void save(OrderOTP orderOTP) {
        List<OrderOTP> orderOTPs = FileUtil.readData(FileConstants.ORDER_OTPS_FILE);

        orderOTP.setId(IdGenerator.generateId(
                FileConstants.ORDER_OTPS_FILE,
                IdConstants.ORDER_OTP_ID_PREFIX));

        orderOTPs.add(orderOTP);

        FileUtil.writeData(FileConstants.ORDER_OTPS_FILE, orderOTPs);
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
        List<OrderOTP> orderOTPs = FileUtil.readData(FileConstants.ORDER_OTPS_FILE);

        for (OrderOTP orderOTP : orderOTPs) {
            if (orderOTP.getOrderId().equals(orderId))
                return orderOTP;
        }

        return null;
    }

    public List<OrderOTP> findAll() {
        return FileUtil.readData(FileConstants.ORDER_OTPS_FILE);
    }

}
