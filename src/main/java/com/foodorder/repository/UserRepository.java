package com.foodorder.repository;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.IdConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.enums.Status;
import com.foodorder.exception.UserNotFoundException;
import com.foodorder.model.User;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    public void save(User user) {
        List<User> users = FileUtil.readData(FileConstants.USERS_FILE);

        user.setId(IdGenerator.generateId(
                FileConstants.USERS_FILE,
                IdConstants.USER_ID_PREFIX));

        users.add(user);

        FileUtil.writeData(FileConstants.USERS_FILE, users);
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
        List<User> users = FileUtil.readData(FileConstants.USERS_FILE);

        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email))
                return user;
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
}
