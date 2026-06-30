package com.foodorder.repository;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.IdConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.exception.UserAddressNotFoundException;
import com.foodorder.model.UserAddress;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class UserAddressRepository {
    public void save(UserAddress userAddress) {
        List<UserAddress> userAddresses = FileUtil.readData(FileConstants.USER_ADDRESSES_FILE);

        userAddress.setId(IdGenerator.generateId(
                FileConstants.USER_ADDRESSES_FILE,
                IdConstants.USER_ADDRESS_ID_PREFIX));

        userAddresses.add(userAddress);

        FileUtil.writeData(FileConstants.USER_ADDRESSES_FILE, userAddresses);
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

        List<UserAddress> userAddresses = FileUtil.readData(FileConstants.USER_ADDRESSES_FILE);

        for (UserAddress userAddress : userAddresses) {
            if (userAddress.getUserId().equals(userId))
                addresses.add(userAddress);
        }

        return addresses;
    }

    public List<UserAddress> findAll() {
        return FileUtil.readData(FileConstants.USER_ADDRESSES_FILE);
    }
}
