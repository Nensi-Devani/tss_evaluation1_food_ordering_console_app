package com.foodorder.service;

import java.util.List;

import com.foodorder.constants.MessageConstants;
import com.foodorder.enums.Role;
import com.foodorder.enums.Status;
import com.foodorder.exception.DuplicateEmailException;
import com.foodorder.exception.UnauthorizedOperationException;
import com.foodorder.model.User;
import com.foodorder.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository = new UserRepository();

    public void register(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            throw new DuplicateEmailException(MessageConstants.EMAIL_ALREADY_EXISTS);
        }

        if (user.getRole() == Role.CUSTOMER) {
            user.setStatus(Status.ACTIVE);
        } else if (user.getRole() == Role.RESTAURANT
                || user.getRole() == Role.DELIVERY_BOY) {
            user.setStatus(Status.INACTIVE);
        }

        userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UnauthorizedOperationException(MessageConstants.INVALID_CREDENTIALS);
        }

        if (!user.getPassword().equals(password)) {
            throw new UnauthorizedOperationException(MessageConstants.INVALID_CREDENTIALS);
        }

        if (user.getStatus() == Status.INACTIVE) {
            throw new UnauthorizedOperationException(MessageConstants.ACCESS_DENIED);
        }

        return user;
    }

    public User getUserById(String id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllActiveUsers() {
        return userRepository.findAllActive();
    }

    public List<User> getAllInactiveUsers() {
        return userRepository.findAllInactive();
    }

    public void update(User user) {
        userRepository.update(user);
    }

    public void approveUser(String userId) {
        User user = userRepository.findById(userId);

        user.setStatus(Status.ACTIVE);

        userRepository.update(user);
    }

    public void deactivateUser(String userId) {
        User user = userRepository.findById(userId);

        user.setStatus(Status.INACTIVE);

        userRepository.update(user);
    }

}
